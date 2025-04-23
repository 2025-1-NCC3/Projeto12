package br.com.fecapccp.uberreport.logicas.usuario;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.ProcurarCorridaPassageiroActivity;
import br.com.fecapccp.uberreport.logicas.requisicoes.ChamadasServidorApiImpl;
import br.com.fecapccp.uberreport.logicas.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.logicas.tokenjwt.SessaoManager;
import br.com.fecapccp.uberreport.logicas.usuario.response.LoginUsuarioResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUsuarioImpl implements LoginUsuario {

    private Context context;

    public LoginUsuarioImpl(Context context) {
        this.context = context;
    }


    @Override
    public void login(LoginRequest loginRequest) {
        try {
            // Criptografa os dados sensíveis
            String chaveAES = BuildConfig.AES_KEY;
            loginRequest.criptografaDadosSensiveis(chaveAES);

            // Realiza a chamada ao servidor
            ChamadasServidorApiImpl.getServicoApi().postLogin(loginRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<LoginUsuarioResponse> call, @NonNull Response<LoginUsuarioResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            LoginUsuarioResponse loginUsuarioResponse = response.body();
                            int idUser = loginUsuarioResponse.getIdUser(); // Obtém o idUser do JSON
                            String token = loginUsuarioResponse.getToken();

                            // Salva o idUser e Token em SharedPreferences
                            SessaoManager sessaoManager = new SessaoManager(context);
                            sessaoManager.salvarIdUser(idUser);
                            sessaoManager.salvarToken(token);

                            // Navega para a próxima Activity
                            Intent intent = new Intent(context, ProcurarCorridaPassageiroActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(context, "Erro ao processar a resposta do servidor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Login falhou", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginUsuarioResponse> call, Throwable t) {
                    Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Erro ao processar os dados", Toast.LENGTH_SHORT).show();
        }
    }
}