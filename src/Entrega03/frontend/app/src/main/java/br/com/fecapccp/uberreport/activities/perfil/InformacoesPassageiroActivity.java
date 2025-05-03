package br.com.fecapccp.uberreport.activities.perfil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import br.com.fecapccp.uberreport.R;
import br.com.fecapccp.uberreport.models.Usuario;

public class InformacoesPassageiroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_passageiro);

        // Referências aos elementos
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
        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (usuario != null) {
            // Preenche os campos com as informações do usuário
            tituloNome.setText("Olá, " + usuario.getNome() + " !");
            nome.setText(usuario.getNome());
            sobrenome.setText(usuario.getSobrenome());
            email.setText(usuario.getEmail());
            telefone.setText(usuario.getTelefone());
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
            // Aqui você pode implementar a lógica para salvar as informações atualizadas

            // Desabilitar campos após salvar
            nome.setEnabled(false);
            sobrenome.setEnabled(false);
            email.setEnabled(false);
            telefone.setEnabled(false);
            contatoEmergencia.setEnabled(false);

            // Alternar visibilidade dos botões
            btnContainer.setVisibility(View.GONE);
            btnEditarInfos.setVisibility(View.VISIBLE);
        });
    }
}