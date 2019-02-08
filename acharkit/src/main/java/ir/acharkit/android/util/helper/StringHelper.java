package ir.acharkit.android.util.helper;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ir.acharkit.android.util.Logger;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/24/2017
 * Email:   alirezat775@gmail.com
 */
public class StringHelper {

    private static final String TAG = StringHelper.class.getName();

    /**
     * @param string
     * @return
     */
    public static String upperCaseFirst(@NonNull String string) {
        if (isEmpty(string)) return string;
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String lowerCaseFirst(@NonNull String string) {
        if (isEmpty(string)) return string;
        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }

    /**
     * @param string
     * @return
     */
    public static String capitalizeString(@NonNull String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    /**
     * @param charSequence
     * @param <E>
     * @return
     */
    public static <E> boolean isEmpty(CharSequence charSequence) {
        return ((charSequence == null) || (charSequence.length() == 0));
    }

    /**
     * @param charSequence
     * @param <E>
     * @return
     */
    public static <E> boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    /**
     * @param text
     * @return
     */
    public static String SHA1(@NonNull String text) {
        MessageDigest md = null;
        byte[] textBytes = new byte[0];
        try {
            md = MessageDigest.getInstance("SHA-1");
            textBytes = text.getBytes("iso-8859-1");

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
             Logger.w(TAG, e);
        }
        if (md != null) {
            md.update(textBytes, 0, textBytes.length);
        }
        byte[] sha1hash = md != null ? md.digest() : new byte[0];
        return ConvertHelper.byteToHex(sha1hash);
    }
}
