package br.com.fecapccp.uberreport.logicas.tokenjwt;

import br.com.fecapccp.uberreport.logicas.criptografia.CriptografiaAES;

public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void criptografaDadosSensiveis(String chaveAES) throws Exception {
        this.email = CriptografiaAES.criptografar(this.email, chaveAES);
        this.senha = CriptografiaAES.criptografar(this.senha, chaveAES);
    }
}
