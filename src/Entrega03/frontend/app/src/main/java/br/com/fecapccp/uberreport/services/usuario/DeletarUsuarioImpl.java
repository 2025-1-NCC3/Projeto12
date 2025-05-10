package br.com.fecapccp.uberreport.services.usuario;

import android.content.Context;
import android.util.Log;

import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiHeaderImpl;
import br.com.fecapccp.uberreport.services.requisicoes.RotasApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeletarUsuarioImpl implements DeletarUsuario {

    private final Context context;

    public DeletarUsuarioImpl(Context context){
        this.context = context;
    }

    @Override
    public void deletarUsuario(int userId) {
        try {
            RotasApi rotasApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);
            rotasApi.deletaUsuario(userId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.i("DELETE", "Usuário deletado com sucesso!");
                    } else {
                        Log.e("DELETE", "Erro ao deletar usuário: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("DELETE", "Erro de conexão: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("DELETE", "Erro na tentativa de deletar usuário: " + e.getMessage());
        }
    }
}
