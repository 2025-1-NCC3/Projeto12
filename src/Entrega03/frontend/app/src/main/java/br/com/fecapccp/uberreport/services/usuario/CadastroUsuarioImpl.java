package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import br.com.fecapccp.uberreport.BuildConfig;
import br.com.fecapccp.uberreport.activities.login.LoginActivity;
import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.requisicoes.RotasApi;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiImpl;
import br.com.fecapccp.uberreport.services.usuario.response.CadastroUsuarioResponse;
import br.com.fecapccp.uberreport.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroUsuarioImpl {

    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public CadastroUsuarioImpl(Context context) {
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
    }

    public void cadastrarUsuario(Usuario usuario) {
        try {
            // Criptografa
            String chaveAES = BuildConfig.AES_KEY;
            usuario.criptografaDadosSensiveis(chaveAES);

            // Envia requisicao
            RotasApi rotasApi = ChamadasServidorApiImpl.getServicoApi();
            rotasApi.postUsuario(usuario).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<CadastroUsuarioResponse> call, Response<CadastroUsuarioResponse> response) {
                    if (response.isSuccessful()) {
                        sharedPreferencesManager.salvarIdUsuario(response.body().getIdUser());
                        Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CadastroUsuarioResponse> call, Throwable t) {
                    Toast.makeText(context, "Erro de conexão!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Erro ao processar os dados!", Toast.LENGTH_SHORT).show();
        }
    }
}