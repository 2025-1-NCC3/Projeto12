package br.com.fecapccp.uberreport.services.alertas;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.fecapccp.uberreport.services.alertas.model.Alerta;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApi;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiHeaderImpl;
import br.com.fecapccp.uberreport.utils.GeoUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObterAlertaImpl implements ObterAlerta {
    private static final long DEBOUNCE_DELAY = 2000; // 2 segundos
    private static final double DISTANCIA_MINIMA = 500; // 500 metros

    private Context context;
    private Handler handler = new Handler();
    private Runnable debounceRunnable;
    private double ultimaLatitude = 0;
    private double ultimaLongitude = 0;
    private List<Alerta> cacheAlertas = new ArrayList<>();

    public ObterAlertaImpl(Context context) {
        this.context = context;
    }

    @Override
    public Alerta obterAlerta() {
        throw new UnsupportedOperationException("Use obterAlertasProximos para buscar alertas próximos.");
    }

    public void obterAlertasProximos(double latitude, double longitude, double raio, Callback<List<Alerta>> callback) {
        if (GeoUtils.calcularDistancia(ultimaLatitude, ultimaLongitude, latitude, longitude) < DISTANCIA_MINIMA) {
            Log.d("ObterAlertaImpl", "Distância mínima não atingida. Usando cache.");
            callback.onResponse(null, Response.success(cacheAlertas));
            return;
        }

        ultimaLatitude = latitude;
        ultimaLongitude = longitude;

        if (debounceRunnable != null) {
            handler.removeCallbacks(debounceRunnable);
        }

        debounceRunnable = () -> {
            ChamadasServidorApi chamadasServidorApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);
            chamadasServidorApi.getAlerta(latitude, longitude, raio).enqueue(new Callback<List<Alerta>>() {
                @Override
                public void onResponse(@NonNull Call<List<Alerta>> call, @NonNull Response<List<Alerta>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("ObterAlertaImpl", "Alertas obtidos: " + response.body());
                        cacheAlertas = response.body();
                        callback.onResponse(call, Response.success(cacheAlertas));
                    } else {
                        Log.e("ObterAlertaImpl", "Erro ao obter alertas: " + response.message());
                        callback.onFailure(call, new Throwable(response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Alerta>> call, @NonNull Throwable t) {
                    Log.e("ObterAlertaImpl", "Erro ao obter alertas: " + t.getMessage());
                    callback.onFailure(call, t);
                }
            });
        };

        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
    }
}
