package br.com.fecapccp.uberreport.services.requisicoes;

import java.util.List;

import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.alertas.model.Alerta;
import br.com.fecapccp.uberreport.services.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.services.usuario.response.CadastroUsuarioResponse;
import br.com.fecapccp.uberreport.services.usuario.response.LoginUsuarioResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChamadasServidorApi {
    @GET("/alertas")
    Call<List<Alerta>> getAlerta(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("raio") double raio
    );

    @GET("users/{id}")
    Call<Usuario> getUsuario(@Path("id") int userId);

    @POST("/alertas")
    Call<String> postAlerta(@Body Alerta alerta);

    @POST("/login")
    Call<LoginUsuarioResponse> postLogin(@Body LoginRequest loginRequest);

    @POST("/token")
    Call<String> postToken(@Body String refreshToken);

    @POST("/users")
    Call<CadastroUsuarioResponse> postUsuario(@Body Usuario usuario);
}
