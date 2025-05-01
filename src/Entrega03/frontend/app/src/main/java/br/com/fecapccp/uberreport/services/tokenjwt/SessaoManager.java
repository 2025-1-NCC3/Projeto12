package br.com.fecapccp.uberreport.services.tokenjwt;

import android.content.Context;
import android.util.Base64;

import org.json.JSONObject;

import br.com.fecapccp.uberreport.utils.SharedPreferencesManager;

public class SessaoManager {
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    public SessaoManager(Context context) {
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public void salvarToken(String token) {
        sharedPreferencesManager.salvarToken(token); // Usa SharedPreferencesManager para salvar o token
    }

    public String obterToken() {
        return sharedPreferencesManager.obterToken(); // Usa SharedPreferencesManager para obter o token
    }

    public void limparSessao() {
        sharedPreferencesManager.limparSessao(); // Usa SharedPreferencesManager para limpar a sessão
    }

    public void salvarIdUser(int idUser){
        sharedPreferencesManager.salvarIdUsuario(idUser); // Usa SharedPreferencesManager salvar ID
    }

    public boolean isTokenExpirado() {
        String token = obterToken();
        if (token == null) return true;

        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
            JSONObject jsonPayload = new JSONObject(payload);

            long exp = jsonPayload.getLong("exp");
            long currentTime = System.currentTimeMillis() / 1000;
            return exp < currentTime;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}