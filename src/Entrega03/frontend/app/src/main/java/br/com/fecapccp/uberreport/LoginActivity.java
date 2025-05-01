package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        configuraHiperlinkEsqueceuSenha(); // Adiciona o hiperlink
    }

    private void configuraHiperlinkEsqueceuSenha() {
        TextView txtIrParaEsqueceuSenha = findViewById(R.id.txtIrEsqueceuSenha);

        String texto = "Esqueceu a senha? Clique aqui";
        SpannableString spannableString = new SpannableString(texto);

        // Define o comportamento do clique
        ClickableSpan esqueceuSenhaClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, EsqueceuSenha.class);
                startActivity(intent);
            }
        };

        // Aplica o ClickableSpan ao texto
        spannableString.setSpan(esqueceuSenhaClick, 17, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtIrParaEsqueceuSenha.setText(spannableString);
        txtIrParaEsqueceuSenha.setMovementMethod(LinkMovementMethod.getInstance());
    }
}