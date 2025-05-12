package br.com.fecapccp.uberreport.activities.perfil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.activities.MainActivity;
import br.com.fecapccp.uberreport.models.Usuario;
import br.com.fecapccp.uberreport.services.usuario.AtualizarUsuarioImpl;
import br.com.fecapccp.uberreport.services.usuario.DeletarUsuarioImpl;
import br.com.fecapccp.uberreport.services.usuario.request.AtualizarUsuarioRequest;
import br.com.fecapccp.uberreport.utils.SharedPreferencesManager;

public class InformacoesPassageiroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_passageiro);

        // Referências aos elementos
        ImageButton btnVoltarTela = findViewById(R.id.btnVoltarTela);
        ImageButton btnConfiguracoes = findViewById(R.id.btnConfiguracoes);
        Button btnEditarInfos = findViewById(R.id.btnEditarInfos);
        Button btnSalvarInfos = findViewById(R.id.btnSalvarInfos);
        Button btnCancelarInfos = findViewById(R.id.btnCancelarInfos);
        LinearLayout btnContainer = findViewById(R.id.btnContainer);

        TextView tituloNome = findViewById(R.id.tituloNomePassageiro);
        EditText nome = findViewById(R.id.PassageiroNome);
        EditText sobrenome = findViewById(R.id.PassageiroSobreNome);
        EditText email = findViewById(R.id.PassageiroEmail);
        EditText telefone = findViewById(R.id.PassageiroTelefone);
        EditText contatoEmergencia = findViewById(R.id.PassageiroContatoEmergencia);

        // Obtém o objeto Usuario passado pela Intent
        Usuario passageiro = (Usuario) getIntent().getSerializableExtra("usuario");

        if (passageiro != null) {
            // Preenche os campos com as informações do usuário
            tituloNome.setText("Olá, " + passageiro.getNome() + "!");
            nome.setText(passageiro.getNome());
            sobrenome.setText(passageiro.getSobrenome());
            email.setText(passageiro.getEmail());
            telefone.setText(passageiro.getTelefone());
            contatoEmergencia.setText(passageiro.getContatoEmergencia());
        } else {
            // Exibe uma mensagem de erro caso o objeto seja nulo
            tituloNome.setText("Erro ao carregar informações");
        }

        // Botão Editar
        btnEditarInfos.setOnClickListener(v -> {
            // Habilitar campos para edição
            nome.setEnabled(true);
            sobrenome.setEnabled(true);
            email.setEnabled(true);
            telefone.setEnabled(true);
            contatoEmergencia.setEnabled(true);

            // Alternar visibilidade dos botões
            btnEditarInfos.setVisibility(View.GONE);
            btnContainer.setVisibility(View.VISIBLE);
        });

        // Botão Cancelar
        btnCancelarInfos.setOnClickListener(v -> {
            // Desabilitar campos
            nome.setEnabled(false);
            sobrenome.setEnabled(false);
            email.setEnabled(false);
            telefone.setEnabled(false);
            contatoEmergencia.setEnabled(false);

            // Alternar visibilidade dos botões
            btnContainer.setVisibility(View.GONE);
            btnEditarInfos.setVisibility(View.VISIBLE);
        });

        // Botão Salvar
        btnSalvarInfos.setOnClickListener(v -> {
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    nome.getText().toString(),
                    sobrenome.getText().toString(),
                    email.getText().toString(),
                    telefone.getText().toString(),
                    contatoEmergencia.getText().toString().isEmpty() ? null : contatoEmergencia.getText().toString()
            );

            try {
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

                AtualizarUsuarioImpl atualizarUsuario = new AtualizarUsuarioImpl(getApplicationContext());
                atualizarUsuario.atualizarUsuario(sharedPreferencesManager.obterIdUsuario(), request);

                Toast.makeText(this, "Informações atualizadas com sucesso!", Toast.LENGTH_SHORT).show();

                // Desabilitar campos após salvar
                nome.setEnabled(false);
                sobrenome.setEnabled(false);
                email.setEnabled(false);
                telefone.setEnabled(false);
                contatoEmergencia.setEnabled(false);

                btnContainer.setVisibility(View.GONE);
                btnEditarInfos.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao atualizar os dados!", Toast.LENGTH_SHORT).show();
                Log.e("PUT", "Erro ao atualizar os dados: " + e.getMessage());
            }
        });

        btnConfiguracoes.setOnClickListener(v -> {
            // Inflar o layout do pop-up
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_deletar_conta, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Opções");
            builder.setView(dialogView);

            // Configura o clique na opção de deletar conta
            builder.setPositiveButton("Deletar", (dialog, which) -> {
                // Segundo pop-up para confirmação
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
                confirmDialog.setTitle("Confirmação");
                confirmDialog.setMessage("Tem certeza de que deseja deletar sua conta?");

                confirmDialog.setPositiveButton("Deletar", (confirmDialogInterface, confirmWhich) -> {
                    SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

                    DeletarUsuarioImpl deletarUsuario = new DeletarUsuarioImpl(getApplicationContext());
                    deletarUsuario.deletarUsuario(sharedPreferencesManager.obterIdUsuario());


                    Toast.makeText(this, "Conta deletada com sucesso.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    sharedPreferencesManager.limparSessao();
                });

                confirmDialog.setNegativeButton("Cancelar", (confirmDialogInterface, confirmWhich) -> {
                    confirmDialogInterface.dismiss();
                });

                confirmDialog.show();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            builder.show();
        });

        // Botão voltar
        btnVoltarTela.setOnClickListener(v -> finish());
    }
}