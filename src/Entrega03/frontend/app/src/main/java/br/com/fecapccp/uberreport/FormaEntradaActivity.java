package br.com.fecapccp.uberreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormaEntradaActivity extends AppCompatActivity {

    private Button btnEntrarConta;
    private Button btnCriarConta;
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forma_entrada);

        inicializaUi();
        configuraBotoes();
        applyWindowInsets();
    }

    private void inicializaUi() {
        btnEntrarConta = findViewById(R.id.btnEntrarConta);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnVoltar = findViewById(R.id.btnVoltar);
    }

    private void configuraBotoes() {
        btnCriarConta.setOnClickListener(v -> avancaProximaActivity(true));
        btnEntrarConta.setOnClickListener(v -> avancaProximaActivity(false));
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void avancaProximaActivity(boolean contaNova) {
        String tipoUsuario = getUsuario();
        if (tipoUsuario == null) return;

        Intent intent;
        if (tipoUsuario.equals("passageiro")) {
            intent = new Intent(this, contaNova ? CriarContaPassageiroActivity.class : LoginActivity.class);
        } else {
            intent = new Intent(this, contaNova ? CriarContaMotoristaActivity.class : LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private String getUsuario() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return null;
        return bundle.getString("tipoUsuario");
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}