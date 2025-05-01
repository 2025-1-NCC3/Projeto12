package br.com.fecapccp.uberreport.activities.criarconta;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.models.Passageiro;
import br.com.fecapccp.uberreport.services.usuario.CadastroUsuarioImpl;

public class CriarContaPassageiroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criar_conta_passageiro);

        // Pegando os campos do layout
        EditText nomeInput = findViewById(R.id.editTextNome);
        EditText sobrenomeInput = findViewById(R.id.editTextSobrenome);
        EditText cpfInput = findViewById(R.id.editTextCPF);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText telefoneInput = findViewById(R.id.editTextTel);
        EditText senhaInput = findViewById(R.id.editTextDigitaSenha);
        EditText confirmaSenhaInput = findViewById(R.id.editTextNovamente);
        Button botaoCadastrar = findViewById(R.id.btncContinuar);

        // Limitar caracteres nos campos de nome e sobrenome
        nomeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        sobrenomeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        // Adicionar validação de CPF
        cpfInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        cpfInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 11) {
                    s.delete(11, s.length());
                }
            }
        });

        // Adicionar validação e formatação de telefone
        telefoneInput.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;

                // Remove todos os caracteres não numéricos
                String digits = s.toString().replaceAll("[^\\d]", "");

                // Limita o número de dígitos a 11
                if (digits.length() > 11) {
                    digits = digits.substring(0, 11);
                }

                // Aplica a formatação
                String formatted;
                if (digits.length() == 11) {
                    formatted = String.format("(%s) %s-%s",
                            digits.substring(0, 2),
                            digits.substring(2, 7),
                            digits.substring(7));
                } else if (digits.length() >= 7) {
                    formatted = String.format("(%s) %s-%s",
                            digits.substring(0, 2),
                            digits.substring(2, 6),
                            digits.substring(6));
                } else if (digits.length() >= 2) {
                    formatted = String.format("(%s) %s",
                            digits.substring(0, 2),
                            digits.substring(2));
                } else {
                    formatted = digits;
                }

                // Atualiza o texto no campo
                s.replace(0, s.length(), formatted);

                isFormatting = false;
            }
        });

        // Adicionar validação de email
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    emailInput.setError("Email inválido");
                }
            }
        });

        // Ação ao clicar no botão
        botaoCadastrar.setOnClickListener(view -> {
            String nome = nomeInput.getText().toString();
            String sobrenome = sobrenomeInput.getText().toString();
            String cpf = cpfInput.getText().toString().replaceAll("[^\\d]", "");
            String email = emailInput.getText().toString();
            String telefone = telefoneInput.getText().toString().replaceAll("[^\\d]", "");
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

            Passageiro passageiro = new Passageiro(
                    nome, sobrenome, email, senha, cpf, telefone
            );

            try {
                CadastroUsuarioImpl cadastroUsuario = new CadastroUsuarioImpl(getApplicationContext());
                cadastroUsuario.cadastrarUsuario(passageiro);
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

//                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao processar os dados!", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}