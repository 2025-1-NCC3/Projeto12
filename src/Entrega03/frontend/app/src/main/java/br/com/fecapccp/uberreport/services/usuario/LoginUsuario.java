package br.com.fecapccp.uberreport.services.usuario;

import br.com.fecapccp.uberreport.services.usuario.request.LoginRequest;

public interface LoginUsuario {
    void login(LoginRequest loginRequest);
}
