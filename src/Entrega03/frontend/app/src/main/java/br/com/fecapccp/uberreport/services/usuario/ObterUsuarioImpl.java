package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.util.Log;

import java.util.function.Consumer;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.criptografia.CriptografiaAES;
import br.com.fecapccp.uberreport.services.requisicoes.RotasApi;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiHeaderImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObterUsuarioImpl implements ObterUsuario {
    private final Context context;

    public ObterUsuarioImpl(Context context) {
        this.context = context;
    }

    public void obterUsuario(int userId, Consumer<Usuario> onSuccess, Consumer<String> onError) {
        RotasApi rotasApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);

        rotasApi.getUsuario(userId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Usuario usuario = response.body();

                        // Descriptografa dados do back-end
                        String chaveAES = BuildConfig.AES_KEY;
                        usuario.setNome(CriptografiaAES.descriptografar(usuario.getNome(), chaveAES));
                        usuario.setSobrenome(CriptografiaAES.descriptografar(usuario.getSobrenome(), chaveAES));
                        usuario.setEmail(CriptografiaAES.descriptografar(usuario.getEmail(), chaveAES));
                        usuario.setTelefone(CriptografiaAES.descriptografar(usuario.getTelefone(), chaveAES));
                        if (usuario.getContatoEmergencia() != null) {
                            usuario.setContatoEmergencia(CriptografiaAES.descriptografar(usuario.getContatoEmergencia(), chaveAES));
                        }
                        onSuccess.accept(usuario);
                    } catch (Exception e) {
                        onError.accept("Erro ao descriptografar: " + e.getMessage());
                        Log.e("GET", "Erro ao obter usuário 1: " + e.getMessage());
                    }
                } else {
                    onError.accept("Error ao obter usuário: " + response.message());
                    Log.e("GET", "Erro ao obter usuário 2: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                onError.accept("Error ao obter usuário: " + t.getMessage());
                Log.e("GET", "Erro ao obter usuário 3: " + t.getMessage());
            }
        });
    }
}