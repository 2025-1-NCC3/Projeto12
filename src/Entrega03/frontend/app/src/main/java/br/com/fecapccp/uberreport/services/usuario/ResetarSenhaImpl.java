package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.activities.login.LoginActivity;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApi;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiImpl;
import br.com.fecapccp.uberreport.services.tokenjwt.LoginRequest;
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

            ChamadasServidorApi chamadasServidorApi = ChamadasServidorApiImpl.getServicoApi();
            chamadasServidorApi.postResetarSenha(loginRequest).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        Log.i("ResetarSenhaImpl", "senha resetada: " + response.body());
                        Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro ao redefinir a senha.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("ResetarSenhaImpl", "Erro de conexão: " + t.getMessage());
                    Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Erro ao processar a resposta do servidor", Toast.LENGTH_SHORT).show();
        }
    }
}
