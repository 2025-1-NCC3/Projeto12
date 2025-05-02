package br.com.fecapccp.uberreport.models;

public class Passageiro extends Usuario {
    public Passageiro(String nome, String sobrenome, String email, String senha, String cpf, String telefone) {
        setNome(nome);
        setSobrenome(sobrenome);
        setEmail(email);
        setSenha(senha);
        setCpf(cpf);
        setTelefone(telefone);
        setTipo("Passageiro");
    }
}
