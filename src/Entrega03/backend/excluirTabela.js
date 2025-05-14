var sqlite3 = require("sqlite3").verbose();
var CAMINHO_DB = "uberDB.db";

// Abrindo o banco de dados
var db = new sqlite3.Database(CAMINHO_DB);

// Excluindo a tabela (substitua "alertas" pelo nome da sua tabela)
db.run("DROP TABLE IF EXISTS alertas", (err) => {
  if (err) {
    console.error("Erro ao excluir tabela:", err.message);
  } else {
    console.log("Tabela 'alertas' excluída com sucesso.");
  }
});

db.run("DROP TABLE IF EXISTS users", (err) => {
  if (err) {
    console.error("Erro ao excluir tabela:", err.message);
  } else {
    console.log("Tabela 'users' excluída com sucesso.");
  }
});
