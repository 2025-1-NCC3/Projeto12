package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.logicas.requisicoes.ServidorApi;
import br.com.fecapccp.uberreport.logicas.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.logicas.tokenjwt.SessaoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_passageiro);

        EditText emailInput = findViewById(R.id.btnemaillogin);
        EditText senhaInput = findViewById(R.id.btnsenhalogin);
        Button botaoLogin = findViewById(R.id.btncContinuar);

        botaoLogin.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String senha = senhaInput.getText().toString();

            LoginRequest loginRequest = new LoginRequest(email, senha);

            ServidorApi.getServicoApi().login(loginRequest).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body();

                        // Salva o token na sessão
                        SessaoManager sessaoManager = new SessaoManager(LoginActivity.this);
                        sessaoManager.salvarToken(token);

                        // Redireciona para a MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login falhou", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}