package br.com.fecapccp.uberreport.logicas.requisicoes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServidorApi {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lwjy86-3000.csb.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ChamadasServidorApi getServicoApi() {
        return retrofit.create(ChamadasServidorApi.class);
    }
}
