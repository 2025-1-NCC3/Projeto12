package br.com.fecapccp.uberreport.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.criarconta.CriarContaMotoristaActivity;
import br.com.fecapccp.uberreport.activities.criarconta.CriarContaPassageiroActivity;
import br.com.fecapccp.uberreport.activities.outros.PoliticasPrivacidadeActivity;

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
        configuraHiperlinks(); // Adiciona os hiperlinks no TextView
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
            intent.putExtra("tipoUsuario", "passageiro");
        } else {
            intent = new Intent(this, contaNova ? CriarContaMotoristaActivity.class : LoginActivity.class);
            intent.putExtra("tipoUsuario", "motorista");
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

    private void configuraHiperlinks() {
        TextView txtTermosPoliticas = findViewById(R.id.txtIrPoliticasPrivacidade);

        String texto = "Ao prosseguir, você aceita nossos Termos de Serviço e Políticas de Privacidade";
        SpannableString spannableString = new SpannableString(texto);



        // Link para Políticas de Privacidade
        ClickableSpan politicasClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(FormaEntradaActivity.this, PoliticasPrivacidadeActivity.class);
                startActivity(intent);
            }
        };

        // Aplicando os spans
        spannableString.setSpan(politicasClick, 54, 78, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTermosPoliticas.setText(spannableString);
        txtTermosPoliticas.setMovementMethod(LinkMovementMethod.getInstance());
    }
}