package br.com.fecapccp.uberreport.alertas;

// Bibliotecas
import android.view.View;
import android.widget.LinearLayout;

public class ClimaAlertaController implements ControladorAlerta {
    private final LinearLayout containerAlertas;
    private final LinearLayout containerAlertasBotoes;
    private final LinearLayout layoutAlertasClima;
    private final LinearLayout layoutAlertasAcidentes;
    private final LinearLayout layoutAlertasCrimes;

    public ClimaAlertaController(
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
        layoutAlertasCrimes.setVisibility(View.GONE);

        containerAlertasBotoes.setVisibility(View.VISIBLE);
        layoutAlertasClima.setVisibility(View.VISIBLE);
    }
}
