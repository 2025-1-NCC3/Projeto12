package br.com.fecapccp.uberreport.activities.recuperarsenha;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.login.LoginActivity;
import br.com.fecapccp.uberreport.services.tokenjwt.LoginRequest;
import br.com.fecapccp.uberreport.services.usuario.ResetarSenha;
import br.com.fecapccp.uberreport.services.usuario.ResetarSenhaImpl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetarSenhaActivity extends AppCompatActivity {

    private EditText novaSenha, confirmarSenha;
    private Button btnRedefinirSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resetar_senha);

        // Recupera o e-mail vindo da tela anterior
        String email = getIntent().getStringExtra("email");

        // Referências dos campos
        novaSenha = findViewById(R.id.btnDigiteNovaSenha);
        confirmarSenha = findViewById(R.id.btnConfirmeNovaSenha);
        btnRedefinirSenha = findViewById(R.id.btnRedefinirSenha);

        btnRedefinirSenha.setOnClickListener(v -> {
            String senha = novaSenha.getText().toString();
            String confirmacao = confirmarSenha.getText().toString();

            if (senha.isEmpty() || confirmacao.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmacao)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            // Envia a nova senha para o servidor
            LoginRequest loginRequest = new LoginRequest(email, senha);
            ResetarSenhaImpl resetarSenha = new ResetarSenhaImpl(getApplicationContext());
            try {
                resetarSenha.resetarSenha(loginRequest);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
                Log.i("ResetarSenhaActivity", "Erro ao resetar: " + e.getMessage());
            }
        });

        // Ajuste visual para barras de status/navegação
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
