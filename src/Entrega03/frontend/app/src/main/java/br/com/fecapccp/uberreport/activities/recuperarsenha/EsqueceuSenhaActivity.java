package br.com.fecapccp.uberreport.activities.recuperarsenha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.services.requisicoes.ChamadasServidorApiImpl;
import br.com.fecapccp.uberreport.services.usuario.response.CodigoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button btnEnviarCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        editTextEmail = findViewById(R.id.btnemailesqueceuSenha);
        btnEnviarCodigo = findViewById(R.id.btnEnviarCodigo);

        btnEnviarCodigo.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(EsqueceuSenhaActivity.this, CodigoEsqueceuSenhaActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);

//            ChamadasServidorApiImpl.getServicoApi().enviarCodigo(email).enqueue(new Callback<>() {
//                @Override
//                public void onResponse(Call<CodigoResponse> call, Response<CodigoResponse> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Toast.makeText(EsqueceuSenhaActivity.this, "Código enviado com sucesso!", Toast.LENGTH_SHORT).show();
//
//
//                    } else {
//                        Toast.makeText(EsqueceuSenhaActivity.this, "Erro ao enviar código. Verifique o e-mail.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CodigoResponse> call, Throwable t) {
//                    Toast.makeText(EsqueceuSenhaActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
//                }
//            });
        });
    }
}