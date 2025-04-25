package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.logicas.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.logicas.usuario.LoginUsuario;
import br.com.fecapccp.uberreport.logicas.usuario.LoginUsuarioImpl;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.btnemaillogin);
        EditText senhaInput = findViewById(R.id.btnsenhalogin);
        Button botaoLogin = findViewById(R.id.btncContinuar);

        LoginUsuario loginUsuario = new LoginUsuarioImpl(this);

        botaoLogin.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String senha = senhaInput.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, senha);

            loginUsuario.login(loginRequest);
        });
    }
}