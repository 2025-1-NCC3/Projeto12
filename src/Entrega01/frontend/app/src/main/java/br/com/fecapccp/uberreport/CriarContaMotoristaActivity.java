package br.com.fecapccp.uberreport;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import br.com.fecapccp.uberreport.logicas.criptografia.CadastroMotorista;

public class CriarContaMotoristaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_motorista);

        // Referências para os campos do layout
        EditText editTextNome = findViewById(R.id.editTextNome);
        EditText editTextSobrenome = findViewById(R.id.editTextSobrenome);
        EditText editTextCpf = findViewById(R.id.editTextCPF);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextTelefone = findViewById(R.id.editTextTelefone);
        EditText editTextCNH = findViewById(R.id.editTextCNH);
        EditText editTextValidade = findViewById(R.id.editTextValidade);
        EditText editTextSenha = findViewById(R.id.editTextDigitaSenha);
        EditText editTextNovamente = findViewById(R.id.editTextNovamente);
        Button botaoCadastrar = findViewById(R.id.btncContinuar);

        // Ação do botão
        botaoCadastrar.setOnClickListener(view -> {
            String nome = editTextNome.getText().toString();
            String sobrenome = editTextSobrenome.getText().toString();
            String cpf = editTextCpf.getText().toString();
            String email = editTextEmail.getText().toString();
            String telefone = editTextTelefone.getText().toString();
            String cnh = editTextCNH.getText().toString();
            String validade = editTextValidade.getText().toString();
            String senha = editTextSenha.getText().toString();
            String confirmaSenha = editTextNovamente.getText().toString();

            // Chamada correta ao método realizarCadastro
            CadastroMotorista.realizarCadastro(
                    CriarContaMotoristaActivity.this,
                    nome, sobrenome, cpf, email, telefone,
                    cnh, validade, senha, confirmaSenha
            );
        })
        ;}
}