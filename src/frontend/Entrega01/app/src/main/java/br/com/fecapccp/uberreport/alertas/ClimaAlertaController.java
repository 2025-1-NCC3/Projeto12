package br.com.fecapccp.uberreport.alertas;

// Bibliotecas
import android.view.View;
import android.widget.LinearLayout;

public class ClimaAlertaController implements ControladorAlerta {
    private final LinearLayout containerAlertas;
    private final LinearLayout containerAlertasClima;

    public ClimaAlertaController(LinearLayout containerAlertas, LinearLayout containerAlertasClima) {
        this.containerAlertas = containerAlertas;
        this.containerAlertasClima = containerAlertasClima;
    }

    @Override
    public void controlarAlertas() {
        //Todo: Implementar lógica de controle de alertas
        containerAlertas.setVisibility(View.GONE);
        containerAlertasClima.setVisibility(View.VISIBLE);
    }
}
