package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botaoEntrar = findViewById(R.id.botaoTelaInicial);

        botaoEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EscolherUsuarioActivity.class);
            startActivity(intent);
        });
    }
}