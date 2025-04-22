package br.com.fecapccp.uberreport.telas.fluxoEntrada;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import br.com.fecapccp.uberreport.R;


public class PaginaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_inicial_main);

        Button botaoEntrar = findViewById(R.id.botaoTelaInicial);

        botaoEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(PaginaInicial.this, PaginaHome.class);
            startActivity(intent);
        });
    }
}