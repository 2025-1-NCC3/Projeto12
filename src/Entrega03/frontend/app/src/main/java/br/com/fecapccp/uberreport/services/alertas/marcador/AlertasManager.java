package br.com.fecapccp.uberreport.services.alertas.marcador;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import android.os.Handler;
import android.os.Looper;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.services.alertas.ObterAlertaImpl;
import br.com.fecapccp.uberreport.services.alertas.model.Alerta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertasManager {
    private final GoogleMap gMap;
    private final Context context;
    private final AlertasListener listener;
    private final Map<String, Marker> marcadorMap = new ConcurrentHashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());


    public AlertasManager(GoogleMap gMap, Context context, AlertasListener listener) {
        this.gMap = gMap;
        this.context = context;
        this.listener = listener;
        setupMarkerClickListener();
    }

    public void fetchAndDisplayAlertas(double latitude, double longitude, double raio) {

        new ObterAlertaImpl(context).obterAlertasProximos(latitude, longitude, raio, new Callback<List<Alerta>>() {
            @Override
            public void onResponse(Call<List<Alerta>> call, Response<List<Alerta>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<Alerta> alertas = response.body();

                    for (Alerta alerta : alertas) {
                        String alertaKey = gerarChaveUnica(alerta.getLatitude(), alerta.getLongitude());
                        if (!marcadorMap.containsKey(alertaKey)) {
                            LatLng position = new LatLng(alerta.getLatitude(), alerta.getLongitude());
                            Bitmap markerBitmap = criarMarcadorCustomizadoAlerta(alerta.getTipoAlerta(), 1);

                            Marker marker = gMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));

                            if (marker != null) {
                                marker.setTag(alerta);
                                marcadorMap.put(alertaKey, marker);

                                // Salva tempo de expiração no SharedPreferences
                                long tempoFinal = System.currentTimeMillis() + (1 * 60 * 1000);
                                SharedPreferences prefs = context.getSharedPreferences("ContadorPrefs", Context.MODE_PRIVATE);
                                prefs.edit().putLong(alertaKey, tempoFinal).apply();


                                // Remover o alerta após o tempo determinado abaixo!
                                handler.postDelayed(() -> {
                                    removerMarcador(alertaKey);
                                    prefs.edit().remove(alertaKey).apply();
                                }, 1 * 60 * 1000);
                            }
                        }
                    }
                } else {
                    Log.e("AlertasManager", "Failed to fetch alerts: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Alerta>> call, Throwable t) {
                Log.e("AlertasManager", "Error fetching alerts: " + t.getMessage());
            }
        });
    }

    private String gerarChaveUnica(Double latitude, Double longitude) {
        return latitude + "_" + longitude;
    }

    private void removerMarcador(String alertaId) {
        Marker marker = marcadorMap.remove(alertaId);
        if (marker != null) {
            marker.remove();
        }
    }

    private void setupMarkerClickListener() {
        gMap.setOnMarkerClickListener(marker -> {
            if (listener != null) {
                listener.onAlertaMarcadorClicado(marker);
            }
            return true;
        });
    }

    private int getLogoTipoAlerta(String tipoAlerta) {
        switch (tipoAlerta) {
            case "ClimaAdverso":
                return R.drawable.weather;
            case "Acidentes":
                return R.drawable.acidente;
            case "Crimes":
                return R.drawable.crimes;
            default:
                return R.drawable.alerta;
        }
    }

    private Bitmap criarMarcadorCustomizadoAlerta(String tipoAlerta, int count) {
        View markerView = LayoutInflater.from(context).inflate(R.layout.marcador_customizado, null);

        // Set the alert icon
        ImageView alertIcon = markerView.findViewById(R.id.alert_icon);
        alertIcon.setImageResource(getLogoTipoAlerta(tipoAlerta));

        // Set the alert count
        TextView alertCount = markerView.findViewById(R.id.alert_count);
        if (count > 1) {
            alertCount.setText(String.valueOf(count));
            alertCount.setVisibility(View.VISIBLE);
        } else {
            alertCount.setVisibility(View.GONE);
        }

        // Convert the view to a bitmap
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerView.layout(0, 0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerView.draw(canvas);

        return bitmap;
    }
}
