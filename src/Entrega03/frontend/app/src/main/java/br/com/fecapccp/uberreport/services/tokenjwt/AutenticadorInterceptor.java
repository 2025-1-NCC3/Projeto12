package br.com.fecapccp.uberreport.services.tokenjwt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import br.com.fecapccp.uberreport.activities.login.LoginActivity;
import br.com.fecapccp.uberreport.utils.SharedPreferencesManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AutenticadorInterceptor implements Interceptor {
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public AutenticadorInterceptor(Context context) {
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = sharedPreferencesManager.obterToken();
        Request originalRequest = chain.request();

        if (token != null && precisaDeAutenticacao(originalRequest.url().encodedPath(), originalRequest.method())) {
            Request modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = chain.proceed(modifiedRequest);

            if (response.code() == 401 || response.code() == 403) {
                // Token expirado, limpar sessão e redirecionar para login
                sharedPreferencesManager.limparSessao();
                exibirPopUpTokenExpirado();
            }

            return response;
        }

        return chain.proceed(originalRequest);
    }

    private void exibirPopUpTokenExpirado() {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Sessão Expirada")
                    .setMessage("Seu token expirou. Por favor, faça login novamente.")
                    .setPositiveButton("OK", (dialog, which) -> redirecionarParaLogin())
                    .setCancelable(false)
                    .show();
        });
    }

    private void redirecionarParaLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private boolean precisaDeAutenticacao(String path, String method) {
        if (method.equals("GET") && path.startsWith("/users")) {
            return true;
        }
        if (method.equals("POST") && path.equals("/users")) {
            return false;
        }
        return true;
    }
}