package br.com.fecapccp.uberreport.activities.login;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.recuperarsenha.EsqueceuSenhaActivity;
import br.com.fecapccp.uberreport.services.usuario.request.LoginRequest;
import br.com.fecapccp.uberreport.services.usuario.LoginUsuario;
import br.com.fecapccp.uberreport.services.usuario.LoginUsuarioImpl;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.btnemaillogin);
        EditText senhaInput = findViewById(R.id.btnsenhalogin);
        Button botaoLogin = findViewById(R.id.btncContinuar);
        ImageButton btnVoltarTela = findViewById(R.id.btnVoltarTela);

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

        btnVoltarTela.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormaEntradaActivity.class);
            String usuario = getUsuario();
            try {
                if ("passageiro".equals(usuario)) {
                    intent.putExtra("tipoUsuario", "passageiro");
                } else if ("motorista".equals(usuario)) {
                    intent.putExtra("tipoUsuario", "motorista");
                }
            } catch (Exception e) {
                Log.e("tipoUsuario", "Erro ao identificar tipo usuário", e);
            }
            startActivity(intent);
            finish();
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
                Intent intent = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
                startActivity(intent);
            }
        };

        // Aplica o ClickableSpan ao texto
        spannableString.setSpan(esqueceuSenhaClick, 17, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtIrParaEsqueceuSenha.setText(spannableString);
        txtIrParaEsqueceuSenha.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getUsuario() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return null;
        return bundle.getString("tipoUsuario");
    }
}