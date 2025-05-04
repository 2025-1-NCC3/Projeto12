package br.com.fecapccp.uberreport.models;

import java.io.Serializable;

import br.com.fecapccp.uberreport.services.criptografia.CriptografiaAES;

public class Usuario implements Serializable {

    private int idUser;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String tipo;


    // Getters and Setters
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipo() {
        return tipo;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void criptografaDadosSensiveis(String chaveAES) throws Exception {
        this.nome = CriptografiaAES.criptografar(this.nome, chaveAES);
        this.sobrenome = CriptografiaAES.criptografar(this.sobrenome, chaveAES);
        this.telefone = CriptografiaAES.criptografar(this.telefone, chaveAES);
        this.email = CriptografiaAES.criptografar(this.email, chaveAES);
        this.senha = CriptografiaAES.criptografar(this.senha, chaveAES);
        this.cpf = CriptografiaAES.criptografar(this.cpf, chaveAES);
    }
}