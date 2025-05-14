const jwt = require("jsonwebtoken"); // Importa o módulo JSON Web Token
const SECRET_KEY = process.env.SECRET_KEY_JWT;

module.exports = (app) => {
  // Rota para gerar token JWT com credenciais secretas
  app.post("/token", (req, res) => {
    const { chave1, chave2 } = req.body;

    // Validação dos campos obrigatórios
    if (!chave1 || !chave2) {
      return res
        .status(400)
        .json({ error: "As credenciais são obrigatórias." });
    }

    // Verifica se as credenciais estão corretas
    
    const CREDENCIAL_CHAVE1 = "uber_report_key";
    const CREDENCIAL_CHAVE2 = "2025uberreport";

    if (chave1 !== CREDENCIAL_CHAVE1 || chave2 !== CREDENCIAL_CHAVE2) {
      return res.status(401).json({ error: "Credenciais inválidas." });
    }

    // Gera o token JWT
    const token = jwt.sign({ chave1, chave2 }, SECRET_KEY, { expiresIn: "1h" });

    // Retorna o token no corpo da resposta
    res.json({ message: "Token gerado com sucesso!", token });
  });
};
