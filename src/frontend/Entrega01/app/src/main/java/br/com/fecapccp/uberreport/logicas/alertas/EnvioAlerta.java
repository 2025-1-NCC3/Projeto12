package br.com.fecapccp.uberreport.logicas.alertas;

import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;

public interface EnvioAlerta {
    void enviarAlerta(Alerta alerta);
}
