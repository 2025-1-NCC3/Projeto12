package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiImpl;
import br.com.fecapccp.uberreport.services.requisicoes.RotasApi;
import br.com.fecapccp.uberreport.services.usuario.request.LoginRequest;
import br.com.fecapccp.uberreport.services.usuario.response.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetarSenhaImpl implements ResetarSenha {

    private Context context;

    public ResetarSenhaImpl(Context context) {
        this.context = context;
    }

    @Override
    public void resetarSenha(LoginRequest loginRequest) {

        try{
            // Criptografa os dados sensíveis
            String chaveAES = BuildConfig.AES_KEY;
            loginRequest.criptografaDadosSensiveis(chaveAES);

            // Realiza a chamada ao servidor

            RotasApi chamadasServidorApi = ChamadasServidorApiImpl.getServicoApi();
            chamadasServidorApi.postResetarSenha(loginRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                    Log.i("ResetarSenhaImpl", "Senha redefinida com sucesso.");
                    Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                    Log.i("ResetarSenhaImpl", "Erro de conexão: " + t.getMessage());
                    Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Erro ao processar a resposta do servidor", Toast.LENGTH_SHORT).show();
        }
    }
}
