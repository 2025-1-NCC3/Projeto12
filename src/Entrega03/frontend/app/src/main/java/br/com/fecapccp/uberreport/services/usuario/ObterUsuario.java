package br.com.fecapccp.uberreport.services.usuario;

import java.util.function.Consumer;

import br.com.fecapccp.uberreport.models.Usuario;

public interface ObterUsuario {
    void obterUsuario(int userId, Consumer<Usuario> onSucesso, Consumer<String> onErro);
}
