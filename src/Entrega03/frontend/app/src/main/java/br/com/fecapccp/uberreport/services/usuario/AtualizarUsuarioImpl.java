package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.services.criptografia.CriptografiaAES;
import br.com.fecapccp.uberreport.services.requisicoes.RotasApi;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiHeaderImpl;
import br.com.fecapccp.uberreport.services.usuario.request.AtualizarUsuarioRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtualizarUsuarioImpl implements AtualizarUsuario {

    private final Context context;

    public AtualizarUsuarioImpl(Context context) {
        this.context = context;
    }

    @Override
    public void atualizarUsuario(int userId, AtualizarUsuarioRequest request) {
        try {
            // Criptografar os dados sensíveis
            String chaveAES = BuildConfig.AES_KEY;
            request.setNome(CriptografiaAES.criptografar(request.getNome(), chaveAES));
            request.setSobrenome(CriptografiaAES.criptografar(request.getSobrenome(), chaveAES));
            request.setEmail(CriptografiaAES.criptografar(request.getEmail(), chaveAES));
            request.setTelefone(CriptografiaAES.criptografar(request.getTelefone(), chaveAES));
            if (request.getContatoEmergencia() != null) {
                request.setContatoEmergencia(CriptografiaAES.criptografar(request.getContatoEmergencia(), chaveAES));
            }

            // Enviar a requisição PUT
            RotasApi rotasApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);
            rotasApi.putUsuario(userId, request).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.i("PUT", "Usuário atualizado com sucesso!");
                    } else {
                        Log.e("PUT", "Erro ao atualizar usuário: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("PUT", "Erro de conexão: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("PUT", "Erro ao processar os dados: " + e.getMessage());
        }
    }
}