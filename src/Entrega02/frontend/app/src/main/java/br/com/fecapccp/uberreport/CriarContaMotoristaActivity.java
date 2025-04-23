package br.com.fecapccp.uberreport;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import br.com.fecapccp.uberreport.entity.Motorista;
import br.com.fecapccp.uberreport.logicas.usuario.CadastroUsuarioImpl;

public class CriarContaMotoristaActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextSobrenome;
    private EditText editTextCpf;
    private EditText editTextTelefone;
    private EditText editTextEmail;
    private EditText editTextValidade;
    private EditText editTextCnh;
    private EditText editTextSenha;
    private EditText editTextSenhaNovamente;
    private Button botaoCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criar_conta_motorista);

        // Inicialização das variáveis
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobrenome = findViewById(R.id.editTextSobrenome);
        editTextCpf = findViewById(R.id.editTextCPF);
        editTextTelefone = findViewById(R.id.editTextTel);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextValidade = findViewById(R.id.editTextValidade);
        editTextCnh = findViewById(R.id.editTextCNH);
        editTextSenha = findViewById(R.id.editTextDigitaSenha);
        editTextSenhaNovamente = findViewById(R.id.editTextNovamente);
        botaoCadastrar = findViewById(R.id.btncContinuar);

        // CPF: Verifica se tem 11 números
        editTextCpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 11) {
                    editTextCpf.setError("O CPF deve ter 11 números");
                }
            }
        });

        // CNH: Verifica se tem 11 números
        editTextCnh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 11) {
                    editTextCnh.setError("O CNH deve ter 11 números");
                }
            }
        });


        // Email: Validação
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    editTextEmail.setError("Email inválido");
                }
            }
        });

        // Validade da CNH: Formatação automática
        editTextValidade.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;
                String unformatted = s.toString().replaceAll("[^\\d]", "");
                if (unformatted.length() > 8) {
                    unformatted = unformatted.substring(0, 8);
                }

                String formatted;
                if (unformatted.length() <= 2) {
                    formatted = unformatted;
                } else if (unformatted.length() <= 4) {
                    formatted = unformatted.substring(0, 2) + "/" + unformatted.substring(2);
                } else {
                    formatted = unformatted.substring(0, 2) + "/" + unformatted.substring(2, 4) + "/" + unformatted.substring(4);
                }

                editTextValidade.setText(formatted);
                editTextValidade.setSelection(formatted.length());
                isFormatting = false;
            }
        });

        // Botão de cadastro
        botaoCadastrar.setOnClickListener(view -> {
            String nome = editTextNome.getText().toString();
            String sobrenome = editTextSobrenome.getText().toString();
            String senha = editTextSenha.getText().toString();
            String confirmaSenha = editTextSenhaNovamente.getText().toString();
            String cpf = editTextCpf.getText().toString();
            String telefone = editTextTelefone.getText().toString();
            String email = editTextEmail.getText().toString();
            String cnh = editTextCnh.getText().toString();
            String validade = editTextValidade.getText().toString();

            if (cpf.length() != 11) {
                Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nome.isEmpty() || sobrenome.isEmpty() || cpf.isEmpty() || email.isEmpty()
                    || telefone.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()
                    || cnh.isEmpty() || validade.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmaSenha)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(validade);
            } catch (ParseException e) {
                Toast.makeText(this, "Data de validade inválida!", Toast.LENGTH_SHORT).show();
                return;
            }

            Motorista motorista = new Motorista(
                    nome, sobrenome, email, senha, cpf, telefone, cnh, validade
            );

            try {
            // Chamar o metodo de cadastro
                CadastroUsuarioImpl cadastroUsuario = new CadastroUsuarioImpl(getApplicationContext());
                cadastroUsuario.cadastrarUsuario(motorista);
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                finish();
            } catch (Exception e){
                Toast.makeText(this, "Erro ao processar os dados!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}