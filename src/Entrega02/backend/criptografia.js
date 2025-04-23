const crypto = require("crypto");

// Chave secreta para criptografia (deve ter 16 caracteres para AES-128)
const SECRET_KEY = process.env.SECRET_KEY_CRIPTO;

if (!SECRET_KEY || SECRET_KEY.length !== 16) {
  throw new Error(
    "A chave de criptografia (SECRET_KEY_CRIPTO) não está definida ou não possui 16 caracteres."
  );
}

// Função para criptografar
function criptografar(texto) {
  const cipher = crypto.createCipheriv(
    "aes-128-ecb",
    Buffer.from(SECRET_KEY),
    null
  );
  let encrypted = cipher.update(texto, "utf8", "base64");
  encrypted += cipher.final("base64");
  return encrypted;
}

// Função para descriptografar
function descriptografar(textoCriptografado) {
  const decipher = crypto.createDecipheriv(
    "aes-128-ecb",
    Buffer.from(SECRET_KEY),
    null
  );
  let decrypted = decipher.update(textoCriptografado, "base64", "utf8");
  decrypted += decipher.final("utf8");
  return decrypted;
}

// Exporta as funções
module.exports = { criptografar, descriptografar };
