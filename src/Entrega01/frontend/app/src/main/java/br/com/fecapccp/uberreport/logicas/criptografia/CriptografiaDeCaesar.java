package br.com.fecapccp.uberreport.logicas.criptografia;

public class CriptografiaDeCaesar {

    private static final int CHAVE = 3; // valor da "chave" para deslocar os caracteres

    public static String criptografar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + CHAVE));
        }
        return resultado.toString();
    }

    public static String descriptografar(String textoCriptografado) {
        StringBuilder resultado = new StringBuilder();
        for (char c : textoCriptografado.toCharArray()) {
            resultado.append((char) (c - CHAVE));
        }
        return resultado.toString();}
}