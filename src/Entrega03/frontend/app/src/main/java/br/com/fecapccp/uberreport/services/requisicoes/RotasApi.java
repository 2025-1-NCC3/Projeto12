package br.com.fecapccp.uberreport.services.requisicoes;

import java.util.List;

import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.alertas.model.Alerta;
import br.com.fecapccp.uberreport.services.usuario.request.LoginRequest;
import br.com.fecapccp.uberreport.services.usuario.request.AtualizarUsuarioRequest;
import br.com.fecapccp.uberreport.services.usuario.response.CadastroUsuarioResponse;
import br.com.fecapccp.uberreport.services.usuario.response.CodigoResponse;
import br.com.fecapccp.uberreport.services.usuario.response.LoginUsuarioResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RotasApi {
    @GET("/alertas")
    Call<List<Alerta>> getAlerta(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("raio") double raio
    );

    @POST("/alertas")
    Call<String> postAlerta(@Body Alerta alerta);

    @POST("/users")
    Call<CadastroUsuarioResponse> postUsuario(@Body Usuario usuario);

    @GET("users/{id}")
    Call<Usuario> getUsuario(@Path("id") int userId);

    @PUT("users/{id}")
    Call<String> putUsuario(@Path("id") int userId, @Body AtualizarUsuarioRequest atualizarUsuarioRequest);

    @DELETE("users/{id}")
    Call<String> deletaUsuario(@Path("id") int userId);

    @POST("/login")
    Call<LoginUsuarioResponse> postLogin(@Body LoginRequest loginRequest);

    @POST("/token")
    Call<String> postToken(@Body String refreshToken);

    @POST("/reset-password")
    Call<String> postResetarSenha(@Body LoginRequest loginRequest);

    @POST("/verificar-codigo")
    Call<Void> verificarCodigo(@Query("email") String email, @Query("codigo") String codigo);

    @POST("/enviar-codigo")
    Call<CodigoResponse> enviarCodigo(@Query("email") String email);
}
