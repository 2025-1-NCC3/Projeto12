package br.com.fecapccp.uberreport.activities.recuperarsenha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodigoEsqueceuSenhaActivity extends AppCompatActivity {

    private EditText codigo1, codigo2, codigo3, codigo4;
    private Button btnProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_esqueceu_senha);

        codigo1 = findViewById(R.id.btnCodigoEnviado1);
        codigo2 = findViewById(R.id.btnCodigoEnviado2);
        codigo3 = findViewById(R.id.btnCodigoEnviado3);
        codigo4 = findViewById(R.id.btnCodigoEnviado4);
        btnProximo = findViewById(R.id.btnProximoCodigoEsqueceuSenha);

        btnProximo.setOnClickListener(view -> {
            String codigo = codigo1.getText().toString() + codigo2.getText().toString() +
                    codigo3.getText().toString() + codigo4.getText().toString();

            String email = getIntent().getStringExtra("email");
            Intent intent = new Intent(this, ResetarSenhaActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();

//            ChamadasServidorApiImpl.getServicoApi().verificarCodigo(email, codigo).enqueue(new Callback<>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(CodigoEsqueceuSenhaActivity.this, "Código verificado com sucesso!", Toast.LENGTH_SHORT).show();
//                        // Redirecionar para redefinir senha
//                    } else {
//                        Toast.makeText(CodigoEsqueceuSenhaActivity.this, "Código inválido!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Toast.makeText(CodigoEsqueceuSenhaActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
//                }
//            });
        });
    }
}