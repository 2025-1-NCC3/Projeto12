package br.com.fecapccp.uberreport.services.usuario;

import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.usuario.request.AtualizarUsuarioRequest;

public interface AtualizarUsuario {
    void atualizarUsuario(int userId, AtualizarUsuarioRequest atualizarUsuarioRequest);
}
