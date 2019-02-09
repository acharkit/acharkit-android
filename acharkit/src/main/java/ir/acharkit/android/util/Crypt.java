package ir.acharkit.android.util;


import android.support.annotation.Size;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/26/2017
 * Email:   alirezat775@gmail.com
 */
public class Crypt {

    private static final String TAG = Crypt.class.getName();
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static final String UNICODE_FORMAT = "UTF8";
    private SecretKey secretKey;
    private Cipher cipher;

    /**
     * @param key
     */
    public Crypt(@Size(min = 24) String key) {
        String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        try {
            byte[] arrayBytes = key.getBytes(UNICODE_FORMAT);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            secretKey = skf.generateSecret(ks);
        } catch (UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            Logger.w(TAG, e);
        }
    }

    /**
     * @param s
     * @return
     */
    private byte[] decode(String s) {
        return Base64.decode(s.getBytes(), Base64.DEFAULT);
    }

    /**
     * @param bytes
     * @return
     */
    private String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * @param unencryptedString
     * @return
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = encode(encryptedText);
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
        return encryptedString;
    }

    /**
     * @param encryptedString
     * @return
     */
    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedText = decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
        return decryptedText;
    }
}
