package br.com.fecapccp.uberreport.logicas.requisicoes;

import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ChamadasServidorApi {
    @GET
    Call<String> getAlerta(@Url String url);

    @POST("/criarAlerta")
    Call<String> postAlerta(@Body Alerta alerta);
}
