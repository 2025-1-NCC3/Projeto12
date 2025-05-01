package br.com.fecapccp.uberreport.services.alertas.controller;

import android.view.View;
import android.widget.LinearLayout;

import br.com.fecapccp.uberreport.services.alertas.ControladorAlerta;

public class CrimesAlertaController implements ControladorAlerta {
    private final LinearLayout containerAlertas;
    private final LinearLayout containerAlertasBotoes;
    private final LinearLayout layoutAlertasClima;
    private final LinearLayout layoutAlertasAcidentes;
    private final LinearLayout layoutAlertasCrimes;

    public CrimesAlertaController(
            LinearLayout containerAlertas,
            LinearLayout containerAlertasBotoes,
            LinearLayout layoutAlertasClima,
            LinearLayout layoutAlertasAcidentes,
            LinearLayout layoutAlertasCrimes
    ) {
        this.containerAlertas = containerAlertas;
        this.containerAlertasBotoes = containerAlertasBotoes;
        this.layoutAlertasClima = layoutAlertasClima;
        this.layoutAlertasAcidentes = layoutAlertasAcidentes;
        this.layoutAlertasCrimes = layoutAlertasCrimes;
    }

    @Override
    public void controlarAlertas() {
        containerAlertas.setVisibility(View.GONE);
        layoutAlertasAcidentes.setVisibility(View.GONE);
        layoutAlertasClima.setVisibility(View.GONE);

        containerAlertasBotoes.setVisibility(View.VISIBLE);
        layoutAlertasCrimes.setVisibility(View.VISIBLE);
    }
}
