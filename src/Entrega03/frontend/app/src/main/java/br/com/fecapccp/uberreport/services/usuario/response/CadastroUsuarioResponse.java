package br.com.fecapccp.uberreport.services.usuario.response;

public class CadastroUsuarioResponse {
    private String message;
    private int idUser;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}