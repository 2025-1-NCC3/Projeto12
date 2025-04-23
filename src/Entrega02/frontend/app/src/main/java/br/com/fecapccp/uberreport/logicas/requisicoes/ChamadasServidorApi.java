package br.com.fecapccp.uberreport.logicas.requisicoes;

import java.util.List;

import br.com.fecapccp.uberreport.entity.Usuario;
import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;
import br.com.fecapccp.uberreport.logicas.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.logicas.usuario.response.CadastroUsuarioResponse;
import br.com.fecapccp.uberreport.logicas.usuario.response.LoginUsuarioResponse;
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
