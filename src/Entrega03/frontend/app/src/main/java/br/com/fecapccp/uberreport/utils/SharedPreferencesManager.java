package br.com.fecapccp.uberreport.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_USER_ID = "idUser"; // Chave para armazenar o ID do usuário
    private static final String KEY_ACCESS_TOKEN = "access_token"; // Chave para armazenar o token de acesso
    private static final int DEFAULT_USER_ID = -1; // Valor padrão para ID do usuário

    private final SharedPreferences sharedPreferences;

    // Construtor que inicializa o SharedPreferences com base no Context
    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Salvar o ID do usuário
    // Salvar o ID do usuário
    public void salvarIdUsuario(int idUser) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, idUser); // Usa a chave correta
        editor.apply();
    }

    // Obter o ID do usuário
    public int obterIdUsuario() {
        return sharedPreferences.getInt(KEY_USER_ID, DEFAULT_USER_ID); // Retorna DEFAULT_USER_ID se não estiver salvo
    }

    // Salvar o token de acesso
    public void salvarToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
    }

    // Obter o token de acesso
    public String obterToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    // Limpar todas as preferências
    public void limparSessao() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}