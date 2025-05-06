package br.com.fecapccp.uberreport.activities.outros;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.login.FormaEntradaActivity;

public class PoliticasPrivacidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_politicas_privacidade);

        // Configura o botão "Aceito"
        Button btnAceito = findViewById(R.id.btnCriarConta2);
        btnAceito.setOnClickListener(v -> {
            // Volta para a página de FormaEntradaActivity
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