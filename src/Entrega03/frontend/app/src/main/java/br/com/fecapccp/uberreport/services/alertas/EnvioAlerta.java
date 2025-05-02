package br.com.fecapccp.uberreport.services.alertas;

import br.com.fecapccp.uberreport.services.alertas.model.Alerta;

public interface EnvioAlerta {
    void enviarAlerta(Alerta alerta);
}
