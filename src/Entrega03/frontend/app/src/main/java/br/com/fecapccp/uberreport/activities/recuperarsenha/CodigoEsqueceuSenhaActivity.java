package br.com.fecapccp.uberreport.activities.recuperarsenha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uberreport.R;

public class CodigoEsqueceuSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_codigo_esqueceu_senha);

        // Configura o botão "Próximo"
        Button btnProximoCodigoEsqueceuSenha = findViewById(R.id.btnProximoCodigoEsqueceuSenha);
        btnProximoCodigoEsqueceuSenha.setOnClickListener(v -> {
            Intent intent = new Intent(CodigoEsqueceuSenhaActivity.this, ResetarSenhaActivity.class);
            startActivity(intent);
        });

        // Configura os insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}