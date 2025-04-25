// Servidor do Uber Report

// Necessário instalar as bibliotecas com npm install
var express = require("express");
var app = express();
var cors = require("cors");
var sqlite3 = require("sqlite3").verbose();
const bcrypt = require("bcrypt");
const saltRounds = 10; // 10 rodadas por salt
const jwt = require("jsonwebtoken");
var morgan = require("morgan"); // logs do servidor
require("dotenv").config(); // Carrega as variáveis de ambiente do arquivo .env

const SECRET_KEY = process.env.SECRET_KEY_JWT;

var port = process.env.PORT || 3002;
var CAMINHO_DB = "uberDB.db";

const { criptografar, descriptografar } = require("./criptografia"); // Importa as funções de criptografia

// Middleware
app.use(morgan("dev"));
app.use(cors());
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
const { autenticarToken, logRequestBody } = require("./middleware"); // Importa o middleware de autenticação
app.use(logRequestBody);
require("./tokenJwt")(app); // Importa e registra a rota de geração de token

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
    tipo      TEXT    NOT NULL,
    cnh       INTEGER NULL,
    validade  TEXT    NULL
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

// Buscando usuários

app.get("/users", autenticarToken, function (req, res) {
  db.all(`SELECT * FROM users`, [], (err, rows) => {
    if (err) {
      return res.status(500).send("Erro ao buscar usuários: " + err);
    }

    // Retorna os dados diretamente sem descriptografar
    res.json(rows);
  });
});

// Buscando usuário específico
app.get("/users/:id", autenticarToken, function (req, res) {
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

    // Criptografa os dados do usuário antes de enviá-los
    const usuarioCriptografado = {
      idUser: criptografar(row.idUser.toString()),
      nome: criptografar(row.nome),
      sobrenome: criptografar(row.sobrenome),
      email: criptografar(row.email),
      cpf: criptografar(row.cpf.toString()),
      telefone: criptografar(row.telefone.toString()),
      tipo: criptografar(row.tipo),
      cnh: row.cnh ? criptografar(row.cnh.toString()) : null,
      validade: row.validade ? criptografar(row.validade) : null,
    };

    res.json(usuarioCriptografado);
  });
});

// Rota para criação de usuários

app.post("/users", async function (req, res) {
  console.log(`[LOG] Rota: POST /users`);
  console.log(
    `[LOG] Dados recebidos (criptografados):`,
    JSON.stringify(req.body, null, 2)
  );

  try {
    // Descriptografa os dados recebidos do front-end
    const nome = descriptografar(req.body.nome);
    const sobrenome = descriptografar(req.body.sobrenome);
    const email = descriptografar(req.body.email);
    const cpf = descriptografar(req.body.cpf);
    const senha = descriptografar(req.body.senha);
    const telefone = descriptografar(req.body.telefone);
    const tipo = req.body.tipo;
    const cnh = req.body.cnh ? descriptografar(req.body.cnh) : null;
    const validade = req.body.validade
      ? descriptografar(req.body.validade)
      : null;

    console.log(`[LOG] Dados descriptografados:`, {
      nome,
      sobrenome,
      email,
      cpf,
      senha,
      telefone,
      tipo,
      cnh,
      validade,
    });

    // Validação dos campos obrigatórios
    if (!nome || !sobrenome || !email || !cpf || !senha || !telefone || !tipo) {
      console.warn(`[WARN] Campos obrigatórios ausentes.`);
      return res
        .status(400)
        .send("Todos os campos obrigatórios devem ser preenchidos.");
    }

    // Verifica se o CPF já existe no banco de dados
    const rows = await new Promise((resolve, reject) => {
      db.all(`SELECT * FROM users WHERE cpf = ?`, [cpf], (err, rows) => {
        if (err) reject(err);
        else resolve(rows);
      });
    });

    if (rows.length > 0) {
      console.warn(`[WARN] Usuário com CPF ${cpf} já existe.`);
      return res.status(422).send("Usuário com esse CPF já existe!");
    }

    // Criptografa a senha antes de armazenar no banco
    const hashSenha = await bcrypt.hash(senha, saltRounds);

    // Insere o usuário no banco de dados
    const idUser = await new Promise((resolve, reject) => {
      const sqlInsert = `INSERT INTO users (nome, sobrenome, email, cpf, senha, telefone, tipo, cnh, validade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;
      db.run(
        sqlInsert,
        [
          nome,
          sobrenome,
          email,
          cpf,
          hashSenha,
          telefone,
          tipo,
          cnh || null,
          validade || null,
        ],
        function (err) {
          if (err) reject(err);
          else resolve(this.lastID); // Captura o ID do último registro inserido
        }
      );
    });

    console.log(`[LOG] Usuário criado com sucesso! ID: ${idUser}`);
    res.status(201).json({ message: "Usuário cadastrado!", idUser });
  } catch (err) {
    console.error(`[ERROR] Erro ao criar usuário:`, err.message);
    res.status(500).json({ error: "Erro no servidor", details: err.message });
  }
});

// Rota para atualizar os dados de um usuário

app.put("/users/:id", autenticarToken, async function (req, res) {
  const idUser = req.params.id; // Pega o ID da URL
  const { nome, sobrenome, email, cpf, senha, telefone, tipo, cnh, validade } =
    req.body;

  try {
    // Verifica se o usuário existe
    const user = await new Promise((resolve, reject) => {
      const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
      db.get(sqlBuscaUser, [idUser], (err, row) => {
        if (err) reject(err);
        else resolve(row);
      });
    });

    if (!user) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }

    // Criptografa os campos sensíveis apenas se forem enviados
    const hashSenha = senha ? await bcrypt.hash(senha, saltRounds) : user.senha;
    const hashCpf = cpf
      ? await bcrypt.hash(cpf.toString(), saltRounds)
      : user.cpf;
    const hashEmail = email ? await bcrypt.hash(email, saltRounds) : user.email;

    // Monta a query dinamicamente com os campos enviados
    const campos = [];
    const valores = [];

    if (nome) {
      campos.push("nome = ?");
      valores.push(nome);
    }
    if (sobrenome) {
      campos.push("sobrenome = ?");
      valores.push(sobrenome);
    }
    if (email) {
      campos.push("email = ?");
      valores.push(hashEmail);
    }
    if (cpf) {
      campos.push("cpf = ?");
      valores.push(hashCpf);
    }
    if (senha) {
      campos.push("senha = ?");
      valores.push(hashSenha);
    }
    if (telefone) {
      campos.push("telefone = ?");
      valores.push(telefone);
    }
    if (tipo) {
      campos.push("tipo = ?");
      valores.push(tipo);
    }
    if (cnh) {
      campos.push("cnh = ?");
      valores.push(cnh);
    }
    if (validade) {
      campos.push("validade = ?");
      valores.push(validade);
    }

    // Adiciona o ID do usuário no final dos valores
    valores.push(idUser);

    // Atualiza o usuário
    const sqlUpdate = `
        UPDATE users 
        SET ${campos.join(", ")} 
        WHERE idUser = ?
      `;
    await new Promise((resolve, reject) => {
      db.run(sqlUpdate, valores, function (err) {
        if (err) reject(err);
        else resolve();
      });
    });

    res.json({ message: "Usuário atualizado com sucesso!" });
  } catch (err) {
    res
      .status(500)
      .json({ error: "Erro ao atualizar usuário", details: err.message });
  }
});

// Rota para deletar um usuário

app.delete("/users/:id", autenticarToken, async function (req, res) {
  const idUser = req.params.id; // Pega o ID da URL

  try {
    // Valida se o ID é um número
    if (isNaN(idUser)) {
      return res.status(400).json({ error: "ID do usuário inválido." });
    }

    // Verifica se o usuário existe
    const user = await new Promise((resolve, reject) => {
      const sqlBuscaUser = `SELECT * FROM users WHERE idUser = ?`;
      db.get(sqlBuscaUser, [idUser], (err, row) => {
        if (err) reject(err);
        else resolve(row);
      });
    });

    if (!user) {
      return res.status(404).json({ error: "Usuário não encontrado." });
    }

    // Deleta o usuário
    await new Promise((resolve, reject) => {
      const sqlDelete = `DELETE FROM users WHERE idUser = ?`;
      db.run(sqlDelete, [idUser], function (err) {
        if (err) reject(err);
        else resolve();
      });
    });

    res.json({ message: "Usuário deletado com sucesso!" });
  } catch (err) {
    res
      .status(500)
      .json({ error: "Erro ao deletar usuário", details: err.message });
  }
});

// Rotas para alertas

// Buscando alertas

app.get("/alertas", autenticarToken, function (req, res) {
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

app.get("/alertas/:id", autenticarToken, function (req, res) {
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

app.post("/alertas", autenticarToken, function (req, res) {
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

app.put("/alertas/:id", autenticarToken, function (req, res) {
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

app.delete("/alertas/:id", autenticarToken, function (req, res) {
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

// Rota de login do usuário
app.post("/login", (req, res) => {
  console.log(`[LOG] Rota: POST /login`);
  console.log(
    `[LOG] Dados recebidos (criptografados):`,
    JSON.stringify(req.body, null, 2)
  );

  try {
    // Descriptografa os dados recebidos do front-end
    const email = descriptografar(req.body.email);
    const senha = descriptografar(req.body.senha);

    console.log(`[LOG] Dados descriptografados:`, { email, senha });

    // Validação dos campos obrigatórios
    if (!email || !senha) {
      console.warn(`[WARN] Campos obrigatórios ausentes.`);
      return res.status(400).json({ error: "Email e senha são obrigatórios." });
    }

    console.log(`[LOG] Buscando usuário no banco de dados...`);
    const sqlBuscaUser = `SELECT * FROM users WHERE email = ?`;
    db.get(sqlBuscaUser, [email], (err, user) => {
      if (err) {
        console.error(`[ERROR] Erro ao buscar usuário:`, err.message);
        return res
          .status(500)
          .json({ error: "Erro ao buscar usuário", details: err.message });
      }

      if (!user) {
        console.warn(`[WARN] Usuário com email ${email} não encontrado.`);
        return res.status(404).json({ error: "Usuário não encontrado." });
      }

      console.log(`[LOG] Verificando a senha fornecida...`);
      bcrypt.compare(senha, user.senha, (err, isMatch) => {
        if (err) {
          console.error(`[ERROR] Erro ao verificar senha:`, err.message);
          return res
            .status(500)
            .json({ error: "Erro ao verificar senha", details: err.message });
        }
        if (!isMatch) {
          console.warn(`[WARN] Credenciais inválidas para o email ${email}.`);
          return res.status(401).json({ error: "Credenciais inválidas." });
        }

        console.log(`[LOG] Login realizado com sucesso! Gerando token JWT...`);
        // Gera o token JWT
        const token = jwt.sign({ idUser: user.idUser }, SECRET_KEY, {
          expiresIn: "1m", // Token expira em 1 hora
        });

        console.log(`[LOG] Login realizado com sucesso!`);
        res.json({
          message: "Login realizado com sucesso!",
          idUser: user.idUser,
          token,
        });
      });
    });
  } catch (err) {
    console.error(`[ERROR] Erro no processamento do login:`, err.message);
    res.status(500).json({ error: "Erro no servidor", details: err.message });
  }
});

// Outras rotas

// Get
app.get("/", function (req, res) {
  res.send("Olá! Vim do servidor!");
});

// Listen
app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
