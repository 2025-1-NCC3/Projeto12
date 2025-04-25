package br.com.fecapccp.uberreport.logicas.usuario;

import br.com.fecapccp.uberreport.logicas.tokenjwt.LoginRequest;

public interface LoginUsuario {
    void login(LoginRequest loginRequest);
}
