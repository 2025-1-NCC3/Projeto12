package br.com.fecapccp.uberreport.logicas.requisicoes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChamadasServidorApiImpl {
    private static Retrofit retrofit;

    public static ChamadasServidorApi getServicoApi() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://wlmvqg-3002.csb.app")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ChamadasServidorApi.class);
    }
}
