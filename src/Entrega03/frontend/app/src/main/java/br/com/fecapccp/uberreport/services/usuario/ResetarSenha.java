package br.com.fecapccp.uberreport.services.usuario;

import br.com.fecapccp.uberreport.services.tokenjwt.LoginRequest;

public interface ResetarSenha {

    void resetarSenha(LoginRequest loginRequest);

}
