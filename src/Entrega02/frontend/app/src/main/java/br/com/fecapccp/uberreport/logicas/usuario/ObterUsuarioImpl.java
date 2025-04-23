package br.com.fecapccp.uberreport.logicas.usuario;

import android.content.Context;
import android.util.Log;

import br.com.fecapccp.uberreport.entity.Usuario;
import br.com.fecapccp.uberreport.logicas.requisicoes.ChamadasServidorApi;
import br.com.fecapccp.uberreport.logicas.requisicoes.ChamadasServidorApiHeaderImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObterUsuarioImpl implements ObterUsuario {
    private Context context;

    public ObterUsuarioImpl(Context context) {
        this.context = context;
    }

    @Override
    public Usuario obterUsuario(int userId) {
        ChamadasServidorApi chamadasServidorApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);

        chamadasServidorApi.getUsuario(userId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ObterUsuarioImpl", "Usuário obtido: " + response.body());
                } else {
                    Log.e("ObterUsuarioImpl", "Erro ao obter usuário: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("ObterUsuarioImpl", "Erro ao obter usuário: " + t.getMessage());
            }
        });

        return null; // Retorne o usuário de forma síncrona, se necessário.
    }
}