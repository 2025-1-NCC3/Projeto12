package br.com.fecapccp.uberreport.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uberreport.R;

public class EscolherUsuarioActivity extends AppCompatActivity {

    private LinearLayout btnPassageiro;
    private LinearLayout btnMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_escolher_usuario);

        btnPassageiro = findViewById(R.id.layoutPassageiro);
        btnMotorista = findViewById(R.id.layoutMotorista);
        Intent intent = new Intent(this, FormaEntradaActivity.class);

        btnPassageiro.setOnClickListener(v -> {
            intent.putExtra("tipoUsuario", "passageiro");
            startActivity(intent);
            finish();
        });

        btnMotorista.setOnClickListener(v -> {
            intent.putExtra("tipoUsuario", "motorista");
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}