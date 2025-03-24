// Servidor do Uber Report

// Necessário instalar as bibliotecas com npm install
var express = require("express");
var app = express();
var cors = require("cors");
var bodyParser = require("body-parser");
var sqlite3 = require("sqlite3").verbose();
const bcrypt = require("bcrypt");
const saltRounds = 10; // 10 rodadas por salt
var morgan = require("morgan"); // logs do servidor

var port = process.env.PORT || 3000;
var CAMINHO_DB = "uberDB.db";

// Middleware
app.use(morgan("dev"));
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Banco de Dados
var db = new sqlite3.Database(CAMINHO_DB);

db.run(`CREATE TABLE IF NOT EXISTS users (
    idUser    INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,
    nome      TEXT    NOT NULL,
    sobrenome TEXT    NOT NULL,
    email     TEXT    NOT NULL,
    cpf       INTEGER UNIQUE NOT NULL,
    senha     TEXT    NOT NULL,
    telefone  INTEGER NOT NULL,
    tipo      TEXT    NOT NULL
)`);

db.run(`CREATE TABLE IF NOT EXISTS alertas (
  idAlerta         INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,
  nomeAlerta       TEXT    NOT NULL,
  dataHoraAlerta   TEXT    NOT NULL,
  tipoAlerta       TEXT    NOT NULL,
  latitude         REAL    NOT NULL,
  longitude        REAL    NOT NULL,
  fk_idUser        INTEGER REFERENCES users (idUser) NOT NULL
)`);

// Rotas para usuários - PRIMEIRA ENTREGA NÃO VAI SER USADO!

// Buscando usuários

app.get("/users", function (req, res) {
  db.all(`SELECT * FROM users`, [], (err, rows) => {
    if (err) {
      return res.send("Erro ao buscar usuários: " + err);
    }
    res.json(rows);
  });
});

// Buscando usuário específico

app.get("/users/:id", function (req, res) {
  const idUser = req.params.id; // Pega o ID da URL

  db.get(`SELECT * FROM users WHERE idUser = ?`, [idUser], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar usuário", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }
    res.json(row); // Retorna o usuário encontrado
  });
});

// Rota para criação de usuários

app.post("/criarUsuario", function (req, res) {
  var { nome, sobrenome, email, cpf, senha, telefone, tipo } = req.body;

  // Verificação se todos os campos necessários foram enviados
  if (!nome || !sobrenome || !email || !cpf || !senha || !telefone || !tipo) {
    return res.send("Todos os campos são obrigatórios.");
  }

  // Validação de CPF e telefone para que sejam apenas números
  if (isNaN(cpf) || isNaN(telefone)) {
    return res.send("CPF e telefone devem ser números.");
  }

  const sqlBusca = `SELECT * FROM users WHERE cpf = ?`; // Busca por CPF (único)
  db.all(sqlBusca, [cpf], (err, rows) => {
    if (err) {
      return res.send("Erro na busca: " + err);
    }
    if (rows.length > 0) {
      return res.send("Usuário com esse CPF já existe!");
    }

    // Criptografa a senha
    bcrypt.hash(senha, saltRounds, function (err, hash) {
      if (err) {
        return res
          .status(500)
          .json({ error: "Erro ao criptografar senha", details: err.message });
      }

      // Insere o novo usuário
      const sqlInsert = `INSERT INTO users (nome, sobrenome, email, cpf, senha, telefone, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)`;
      db.run(
        sqlInsert,
        [nome, sobrenome, email, cpf, hash, telefone, tipo],
        function (err) {
          if (err) {
            return res
              .status(500)
              .json({ error: "Erro na gravação", details: err.message });
          }
          res.status(201).json({ message: "Usuário cadastrado!" });
        }
      );
    });
  });
}); // Finalização da rota POST para CRIAR USUÁRIOS

// Rota para atualizar os dados de um usuário

app.put("/users/:id", function (req, res) {
  const idUser = req.params.id; // Pega o ID da URL
  var { nome, sobrenome, email, cpf, senha, telefone, tipo } = req.body;

  // Validação dos campos obrigatórios
  if (!nome || !sobrenome || !email || !cpf || !senha || !telefone || !tipo) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  // Validação de CPF e telefone para que sejam apenas números
  if (isNaN(cpf) || isNaN(telefone)) {
    return res.status(400).json({ error: "CPF e telefone devem ser números." });
  }

  // Verifica se o usuário existe
  const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
  db.get(sqlBuscaUser, [idUser], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar usuário", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }

    // Atualiza o usuário
    const sqlUpdate = `UPDATE users SET nome = ?, sobrenome = ?, email = ?, cpf = ?, senha = ?, telefone = ?, tipo = ? WHERE idUser = ?`;
    db.run(
      sqlUpdate,
      [nome, sobrenome, email, cpf, senha, telefone, tipo, idUser],
      function (err) {
        if (err) {
          return res
            .status(500)
            .json({ error: "Erro ao atualizar usuário", details: err.message });
        }
        res.json({ message: "Usuário atualizado com sucesso!" });
      }
    );
  });
});

// Rota para deletar um usuário

app.delete("/users/:id", function (req, res) {
  const idUser = req.params.id; // Pega o ID da URL

  // Verifica se o usuário existe
  const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
  db.get(sqlBuscaUser, [idUser], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar usuário", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }

    // Deleta o usuário
    const sqlDelete = `DELETE FROM users WHERE idUser = ?`;
    db.run(sqlDelete, [idUser], function (err) {
      if (err) {
        return res
          .status(500)
          .json({ error: "Erro ao deletar usuário", details: err.message });
      }
      res.json({ message: "Usuário deletado com sucesso!" });
    });
  });
});

// ===============================================================================

// Rotas para alertas

// Buscando alertas

app.get("/alertas", function (req, res) {
  db.all(`SELECT * FROM alertas`, [], (err, rows) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar alertas", details: err.message });
    }
    res.json(rows);
  });
});

// Buscando alertas específicos

app.get("/alertas/:id", function (req, res) {
  const idAlerta = req.params.id; // Pega o ID da URL

  db.get(`SELECT * FROM alertas WHERE idAlerta = ?`, [idAlerta], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar alerta", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Alerta não encontrado." });
    }
    res.json(row); // Retorna o alerta encontrado
  });
});

// Criando alertas

app.post("/criarAlerta", function (req, res) {
  var {
    nomeAlerta,
    dataHoraAlerta,
    tipoAlerta,
    latitude,
    longitude,
    fk_idUser,
  } = req.body;

  console.log("Recebendo solicitação para criar alerta", req.body);

  // Validação dos campos obrigatórios
  if (
    !nomeAlerta || // Clima adverso OU Acidentes OU Crimes
    !dataHoraAlerta ||
    !tipoAlerta || // Se clima (Alagamento, deslizamento, temporal) | Se acidentes (Carros, pedestres) | Se crimes (Assaltos, confrontos, arrastão)
    !latitude ||
    !longitude ||
    !fk_idUser
  ) {
    console.error("Erro: Campos obrigatórios faltando", req.body);
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  // Validação se latitude e longitude são números
  if (isNaN(latitude) || isNaN(longitude)) {
    console.error("Erro: Latitude ou longitude não são números", {
      latitude,
      longitude,
    });
    return res
      .status(400)
      .json({ error: "Latitude e longitude devem ser números." });
  }

  console.log("Validando existência do usuário com ID:", fk_idUser);
  // Validando se o usuário existe
  const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
  db.get(sqlBuscaUser, [fk_idUser], (err, row) => {
    if (err) {
      console.error("Erro ao buscar usuário no banco de dados", err.message);
      return res
        .status(500)
        .json({ error: "Erro ao buscar usuário", details: err.message });
    }
    if (!row) {
      console.warn("Usuário não encontrado", fk_idUser);
      return res.status(404).json({ error: "Usuário não encontrado." });
    }

    console.log("Usuário encontrado. Inserindo alerta...");
    // Inserindo o novo alerta
    const sqlInsert = `INSERT INTO alertas (nomeAlerta, dataHoraAlerta, tipoAlerta, latitude, longitude, fk_idUser) VALUES (?, ?, ?, ?, ?, ?)`;
    db.run(
      sqlInsert,
      [nomeAlerta, dataHoraAlerta, tipoAlerta, latitude, longitude, fk_idUser],
      function (err) {
        if (err) {
          console.error(
            "Erro ao inserir alerta no banco de dados",
            err.message
          );
          return res
            .status(500)
            .json({ error: "Erro ao criar alerta", details: err.message });
        }
        console.log("Alerta criado com sucesso! ID:", this.lastID);
        res.status(201).json({
          message: "Alerta criado com sucesso!",
          idAlerta: this.lastID,
        });
      }
    );
  });
});

// Rota para atualizar os dados de um alerta

app.put("/alertas/:id", function (req, res) {
  const idAlerta = req.params.id; // Pega o ID da URL
  var {
    nomeAlerta,
    dataHoraAlerta,
    tipoAlerta,
    latitude,
    longitude,
    fk_idUser,
  } = req.body;

  // Validação dos campos obrigatórios
  if (
    !nomeAlerta ||
    !dataHoraAlerta ||
    !tipoAlerta ||
    !latitude ||
    !longitude ||
    !fk_idUser
  ) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios." });
  }

  // Validação de latitude e longitude para que sejam números
  if (isNaN(latitude) || isNaN(longitude)) {
    return res
      .status(400)
      .json({ error: "Latitude e longitude devem ser números." });
  }

  // Verifica se o alerta existe
  const sqlBuscaAlerta = `SELECT * FROM alertas WHERE idAlerta = ?`;
  db.get(sqlBuscaAlerta, [idAlerta], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar alerta", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Alerta não encontrado." });
    }

    // Verifica se o usuário associado existe
    const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
    db.get(sqlBuscaUser, [fk_idUser], (err, row) => {
      if (err) {
        return res
          .status(500)
          .json({ error: "Erro ao buscar usuário", details: err.message });
      }
      if (!row) {
        return res.status(404).json({ error: "Usuário não encontrado." });
      }

      // Atualiza o alerta
      const sqlUpdate = `UPDATE alertas SET nome = ?, dataHora = ?, tipoAlerta = ?, latitude = ?, longitude = ?, fk_idUser = ? WHERE idAlerta = ?`;
      db.run(
        sqlUpdate,
        [
          nomeAlerta,
          dataHoraAlerta,
          tipoAlerta,
          latitude,
          longitude,
          fk_idUser,
          idAlerta,
        ],
        function (err) {
          if (err) {
            return res.status(500).json({
              error: "Erro ao atualizar alerta",
              details: err.message,
            });
          }
          res.json({ message: "Alerta atualizado com sucesso!" });
        }
      );
    });
  });
});

// Rota para deletar um alerta

app.delete("/alertas/:id", function (req, res) {
  const idAlerta = req.params.id; // Pega o ID da URL

  // Verifica se o alerta existe
  const sqlBuscaAlerta = `SELECT * FROM alertas WHERE idAlerta = ?`;
  db.get(sqlBuscaAlerta, [idAlerta], (err, row) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Erro ao buscar alerta", details: err.message });
    }
    if (!row) {
      return res.status(404).json({ error: "Alerta não encontrado." });
    }

    // Deleta o alerta
    const sqlDelete = `DELETE FROM alertas WHERE idAlerta = ?`;
    db.run(sqlDelete, [idAlerta], function (err) {
      if (err) {
        return res
          .status(500)
          .json({ error: "Erro ao deletar alerta", details: err.message });
      }
      res.json({ message: "Alerta deletado com sucesso!" });
    });
  });
});

// Outras rotas

// Get
app.get("/", function (req, res) {
  res.send("Olá! Vim do servidor!");
});

// POST
app.post("/user", function (req, res) {
  var { nome, senha } = req.body;
  console.log(`Nome: ${nome}, Senha: ${senha}`);
  res.send("Dados recebidos!");
});

// Listen
app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
