package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResetarSenha extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resetar_senha);

        // Configura o botão "Voltar ao Login"
        Button btnVoltarAoLogin = findViewById(R.id.btnVoltarAoLogin);
        btnVoltarAoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ResetarSenha.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finaliza a atividade atual
        });

        // Configura os insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}