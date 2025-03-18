package br.com.fecapccp.uberreport.logicas.alertas;

public interface EnvioAlerta {
    void enviarAlerta(String tipoAlerta, String dataHora);
}
