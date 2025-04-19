package br.com.fecapccp.uberreport.logicas.tokenjwt;

import android.content.Context;
import android.content.SharedPreferences;

public class SessaoManager {
    private static final String PREF_NAME = "SessaoUsuario";
    private static final String KEY_TOKEN = "access_token";
    private final SharedPreferences sharedPreferences;

    public SessaoManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void salvarToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String obterToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void limparSessao() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
