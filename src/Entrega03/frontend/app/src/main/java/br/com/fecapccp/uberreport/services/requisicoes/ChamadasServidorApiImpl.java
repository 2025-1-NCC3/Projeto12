package br.com.fecapccp.uberreport.services.requisicoes;

import br.com.fecapccp.uberreport.services.usuario.response.CodigoResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ChamadasServidorApiImpl {
    private static Retrofit retrofit;

    public static ChamadasServidorApi getServicoApi() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://fnnpyl-3002.csb.app")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ChamadasServidorApi.class);
    }

    public interface ServicoApi {
        @POST("/enviar-codigo")
        Call<CodigoResponse> enviarCodigo(@Query("email") String email);

        @POST("/verificar-codigo")
        Call<Void> verificarCodigo(@Query("email") String email, @Query("codigo") String codigo);
    }

}
