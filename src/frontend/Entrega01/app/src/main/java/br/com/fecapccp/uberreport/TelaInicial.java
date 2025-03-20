package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicio_app);

        Button botaoEntrar = findViewById(R.id.botaoTelaInicial);

        botaoEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(TelaInicial.this, TelaProcurarCorridaPassageiro.class);
            startActivity(intent);
        });
    }
}