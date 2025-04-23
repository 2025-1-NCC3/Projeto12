package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.logicas.tokenjwt.SessaoManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessaoManager sessaoManager = new SessaoManager(this);
        String token = sessaoManager.obterToken();

        if (token == null) {
            // Redireciona para a tela de login se não houver token
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Button botaoEntrar = findViewById(R.id.botaoTelaInicial);
        botaoEntrar.setOnClickListener(v -> {
            // Continue para a próxima tela
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}