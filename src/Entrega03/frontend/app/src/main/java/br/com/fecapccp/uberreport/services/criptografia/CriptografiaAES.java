package br.com.fecapccp.uberreport.services.criptografia;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CriptografiaAES {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    // Criptografa um texto usando uma chave AES
    public static String criptografar(String texto, String chave) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(chave.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(texto.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
    }

    // Descriptografa um texto usando uma chave AES
    public static String descriptografar(String textoCriptografado, String chave) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(chave.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.decode(textoCriptografado, Base64.NO_WRAP));
        return new String(decryptedBytes);
    }
}