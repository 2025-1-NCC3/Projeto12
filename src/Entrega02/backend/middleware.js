module.exports = { autenticarToken, logRequestBody };
const jwt = require("jsonwebtoken");
const SECRET_KEY = process.env.SECRET_KEY_JWT;

// Middleware de autenticação
function autenticarToken(req, res, next) {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1]; // O token deve ser enviado como "Bearer <token>"

  if (!token) {
    return res.status(401).json({ error: "Token não fornecido." });
  }

  jwt.verify(token, SECRET_KEY, (err, user) => {
    if (err) {
      return res.status(403).json({ error: "Token inválido ou expirado." });
    }

    req.user = user; // Adiciona os dados do usuário ao objeto `req` para uso nas rotas
    next(); // Continua para a próxima função
  });
}

// Middleware para logar o corpo das requisições
function logRequestBody(req, res, next) {
  if (req.body && Object.keys(req.body).length > 0) {
    console.log(`[LOG] Rota: ${req.method} ${req.originalUrl}`);
    console.log(
      `[LOG] Corpo da requisição:`,
      JSON.stringify(req.body, null, 2)
    );
  }
  next();
}
