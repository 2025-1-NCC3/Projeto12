package br.com.fecapccp.uberreport.logicas.alertas.marcador;

import com.google.android.gms.maps.model.Marker;

public interface AlertasListener {
    void onAlertaMarcadorClicado(Marker marker);
}
