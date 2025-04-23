package br.com.fecapccp.uberreport;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import br.com.fecapccp.uberreport.logicas.criptografia.CadastroPassageiro;
public class CriarContaPassageiroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta_passageiro);

        // Pegando os campos do layout
        EditText nomeInput = findViewById(R.id.editTextNome);
        EditText sobrenomeInput = findViewById(R.id.editTextSobrenome);
        EditText cpfInput = findViewById(R.id.editTextCPF);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText telefoneInput = findViewById(R.id.editTextTelefone);
        EditText senhaInput = findViewById(R.id.editTextDigitaSenha);
        EditText confirmaSenhaInput = findViewById(R.id.editTextNovamente);
        Button botaoCadastrar = findViewById(R.id.btncContinuar);

        // Ação ao clicar no botão
        botaoCadastrar.setOnClickListener(view -> {
            String nome = nomeInput.getText().toString();
            String sobrenome = sobrenomeInput.getText().toString();
            String cpf = cpfInput.getText().toString();
            String email = emailInput.getText().toString();
            String telefone = telefoneInput.getText().toString();
            String senha = senhaInput.getText().toString();
            String confirmaSenha = confirmaSenhaInput.getText().toString();

            if (nome.isEmpty() || sobrenome.isEmpty() || cpf.isEmpty() || email.isEmpty()
                    || telefone.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmaSenha)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chamada da classe responsável por criptografar e enviar os dados
            CadastroPassageiro.realizarCadastro(
                    CriarContaPassageiroActivity.this,
                    nome,
                    sobrenome,
                    cpf,
                    email,
                    telefone,
                    senha,
                    confirmaSenha
            );
   });
}
}