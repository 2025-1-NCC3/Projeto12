package br.com.fecapccp.uberreport.logicas.criptografia;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CadastroPassageiro {

    public static void realizarCadastro(
            Context context,
            String nome,
            String sobrenome,
            String cpf,
            String email,
            String telefone,
            String senha,
            String confirmaSenha
    ) {
        // Criptografando os dados sensíveis
        String nomeCripto = CriptografiaDeCaesar.criptografar(nome);
        String sobrenomeCripto = CriptografiaDeCaesar.criptografar(sobrenome);
        String cpfCripto = CriptografiaDeCaesar.criptografar(cpf);
        String emailCripto = CriptografiaDeCaesar.criptografar(email);
        String telefoneCripto = CriptografiaDeCaesar.criptografar(telefone);
        String senhaCripto = CriptografiaDeCaesar.criptografar(senha);
        String confirmaSenhaCripto = CriptografiaDeCaesar.criptografar(confirmaSenha);

        String url = "https://swtx66-3000.csb.app/cadastro";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Resposta de sucesso
                    String respostaDescriptografada = CriptografiaDeCaesar.descriptografar(response);
                    Toast.makeText(context, respostaDescriptografada, Toast.LENGTH_LONG).show();

                    // Pop-up adicional de sucesso
                    Toast.makeText(context, "Cadastro enviado com sucesso!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Resposta de erro
                    Toast.makeText(context, "Erro ao enviar os dados!", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nomeCripto);
                params.put("sobrenome", sobrenomeCripto);
                params.put("cpf", cpfCripto);
                params.put("email", emailCripto);
                params.put("telefone", telefoneCripto);
                params.put("senha", senhaCripto);
                params.put("confirmaSenha", confirmaSenhaCripto);
                return params;
            }
        };

        queue.add(stringRequest);}
}