package br.com.fecapccp.uberreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.login.EscolherUsuarioActivity;
import br.com.fecapccp.uberreport.services.tokenjwt.SessaoManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        SessaoManager sessaoManager = new SessaoManager(this);
        Button botaoEntrar = findViewById(R.id.botaoTelaInicial);

        botaoEntrar.setOnClickListener(v -> {
//            sessaoManager.limparSessao();
            if (sessaoManager.isTokenExpirado()) {
                // Redireciona para a tela de escolha de usuário se o token expirou
                Intent intent = new Intent(MainActivity.this, EscolherUsuarioActivity.class);
                startActivity(intent);
            } else if (!sessaoManager.isTokenExpirado()) {
                // Redireciona para a tela de procurar corrida se o token ainda é válido
                Intent intent = new Intent(MainActivity.this, ProcurarCorridaPassageiroActivity.class);
                startActivity(intent);
            } else {
                // Caso contrário, limpa a sessão e redireciona para a tela de escolha de usuário
                sessaoManager.limparSessao();
                Intent intent = new Intent(MainActivity.this, EscolherUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }
}