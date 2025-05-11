package br.com.fecapccp.uberreport.services.usuario.request;

import androidx.annotation.Nullable;

public class AtualizarUsuarioRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    @Nullable
    private String contatoEmergencia = null;

    // Construtor
    public AtualizarUsuarioRequest(String nome, String sobrenome, String email, String telefone, @Nullable String contatoEmergencia) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.contatoEmergencia = contatoEmergencia;
    }

    // Getters e Setters
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Nullable
    public String getContatoEmergencia() {
        return contatoEmergencia;
    }

    public void setContatoEmergencia(@Nullable String contatoEmergencia) {
        this.contatoEmergencia = contatoEmergencia;
    }
}