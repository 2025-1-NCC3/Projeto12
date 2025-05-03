package br.com.fecapccp.uberreport.models;

import br.com.fecapccp.uberreport.services.criptografia.CriptografiaAES;

public class Motorista extends Usuario {
    private String cnh;
    private String validadeCnh;

    public Motorista(String nome, String sobrenome, String email, String senha, String cpf, String telefone, String cnh, String validadeCnh) {
        setNome(nome);
        setSobrenome(sobrenome);
        setEmail(email);
        setSenha(senha);
        setCpf(cpf);
        setTelefone(telefone);
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        setTipo("Motorista");
    }

    public Motorista(String nome, String sobrenome, String email, String telefone, String cnh, String validadeCnh) {
        setNome(nome);
        setSobrenome(sobrenome);
        setEmail(email);
        setTelefone(telefone);
        this.cnh = cnh;
        this.validadeCnh = validadeCnh;
        setTipo("Motorista");
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getValidadeCnh() {
        return validadeCnh;
    }

    public void setValidadeCnh(String validadeCnh) {
        this.validadeCnh = validadeCnh;
    }

    @Override
    public void criptografaDadosSensiveis(String chaveAES) throws Exception {
        super.criptografaDadosSensiveis(chaveAES); // Criptografa os dados do Usuario
        this.cnh = CriptografiaAES.criptografar(this.cnh, chaveAES);
        this.validadeCnh = CriptografiaAES.criptografar(this.validadeCnh, chaveAES);
    }
}