package br.com.fecapccp.uberreport.logicas.alertas;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.fecapccp.uberreport.logicas.alertas.model.Alerta;
import br.com.fecapccp.uberreport.logicas.requisicoes.ChamadasServidorApi;
import br.com.fecapccp.uberreport.logicas.requisicoes.ChamadasServidorApiHeaderImpl;
import br.com.fecapccp.uberreport.logicas.tokenjwt.AutenticadorInterceptor;
import br.com.fecapccp.uberreport.logicas.tokenjwt.SessaoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnvioAlertaImpl implements EnvioAlerta {
    private final Context context;
    private AutenticadorInterceptor autenticadorInterceptor;

    public EnvioAlertaImpl(Context context) {
        this.context = context;
    }

    @Override
    public void enviarAlerta(Alerta alerta) {
        ChamadasServidorApi chamadasServidorApi = ChamadasServidorApiHeaderImpl.getServicoApi(context);

        chamadasServidorApi.postAlerta(alerta).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 401 || response.code() == 403) {
                    Log.e("EnvioAlertaImpl", "Token expirado. Redirecionando para login.");
                    new SessaoManager(context).limparSessao();
                    return;
                }

                if (response.isSuccessful()) {
                    Log.d("EnvioAlertaImpl", "Alerta enviado com sucesso: " + response.body());
                    exibirPopUp("Enviado com sucesso");
                } else {
                    String errorMessage = "Erro ao enviar alerta: " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("EnvioAlertaImpl", "Erro ao ler o corpo do erro: " + e.getMessage());
                        }
                    }
                    Log.e("EnvioAlertaImpl", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String errorMessage = "Erro ao enviar alerta: " + t.getMessage();
                Log.e("EnvioAlertaImpl", errorMessage);
            }
        });
    }

    private void exibirPopUp(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensagem)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Voltar ao estado inicial
                });
        builder.create().show();
    }

    public String getDataHoraAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}