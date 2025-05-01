package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PoliticasPrivacidade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_politicas_privacidade);

        // Configura o botão "Aceito"
        Button btnAceito = findViewById(R.id.btnCriarConta2);
        btnAceito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volta para a página de FormaEntradaActivity
                Intent intent = new Intent(PoliticasPrivacidade.this, FormaEntradaActivity.class);
                startActivity(intent);
                finish(); // Finaliza a atividade atual
            }
        });

        // Configura os insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}