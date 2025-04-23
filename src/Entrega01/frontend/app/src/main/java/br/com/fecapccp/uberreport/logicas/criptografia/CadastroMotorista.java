package br.com.fecapccp.uberreport.logicas.criptografia;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CadastroMotorista {

    public static void realizarCadastro(
            Context context,
            String nome,
            String sobrenome,
            String cpf,
            String email,
            String telefone,
            String cnh,
            String validade,
            String senha,
            String confirmaSenha
    ) {
        String senhaCripto = CriptografiaDeCaesar.criptografar(senha);
        String confirmaSenhaCripto = CriptografiaDeCaesar.criptografar(confirmaSenha);

        String url = "https://swtx66-3000.csb.app/cadastro";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Resposta de sucesso
                    String respostaDescriptografada = CriptografiaDeCaesar.descriptografar(response);
                    Toast.makeText(context, respostaDescriptografada, Toast.LENGTH_LONG).show();

                    // Exibindo o pop-up de sucesso
                    Toast.makeText(context, "Cadastro enviado com sucesso!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Resposta de erro
                    Toast.makeText(context, "Erro ao enviar os dados!", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("sobrenome", sobrenome);
                params.put("cpf", cpf);
                params.put("email", email);
                params.put("telefone", telefone);
                params.put("cnh", cnh);
                params.put("validade", validade);
                params.put("senha", senhaCripto);
                params.put("confirmaSenha", confirmaSenhaCripto);
                return params;
            }
        };

        queue.add(stringRequest);}
}