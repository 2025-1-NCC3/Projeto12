package br.com.fecapccp.uberreport.services.requisicoes;

import android.content.Context;

import br.com.fecapccp.uberreport.services.tokenjwt.AutenticadorInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChamadasServidorApiHeaderImpl {

    private static Retrofit retrofit;

    public static RotasApi getServicoApi(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AutenticadorInterceptor(context)) // Adiciona o token no header
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://cqtc62-5000.csb.app/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(RotasApi.class);
    }
}