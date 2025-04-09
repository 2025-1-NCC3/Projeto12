package br.com.fecapccp.uberreport.logicas.requisicoes;

import java.util.List;

import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ChamadasServidorApi {
    @GET("/alertas")
    Call<List<Alerta>> getAlerta(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("raio") double raio
    );

    @POST("/criarAlerta")
    Call<String> postAlerta(@Body Alerta alerta);
}
