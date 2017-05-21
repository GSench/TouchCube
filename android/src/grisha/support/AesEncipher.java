package grisha.support;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * ????? ??????????? ?????? ? ?????????? ?????????? AES
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class AesEncipher {
    Cipher ecipher;
    Cipher dcipher;

    /**
     * ???????????
     * @param key ????????? ???? ????????? AES. ????????? ?????? SecretKey
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    @SuppressLint("TrulyRandom") public AesEncipher(SecretKey key){
        try {
            ecipher = Cipher.getInstance("AES");
            dcipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public AesEncipher(String keyStr){
        SecretKey key = stringToKey(keyStr);
        try {
            ecipher = Cipher.getInstance("AES");
            dcipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SecretKey stringToKey(String keyString){
        byte[] keyStrict = keyString.getBytes();
        keyStrict = Arrays.copyOf(keyStrict, 24);
        SecretKey key = new SecretKeySpec(keyStrict, "AES");
        return key;
    }

    /**
     * ??????? ??????????
     * @param str ?????? ????????? ??????
     * @return ????????????? ?????? ? ??????? Base64
     */
    public String encrypt(String str){
        byte[] utf8;
        byte[] enc = null;
        try {
            utf8 = str.getBytes("UTF8");
            enc = ecipher.doFinal(utf8);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return MyBase64.encode(enc);
    }

    /**
     * ??????? ?????????????
     * @param str ????????????? ?????? ? ??????? Base64
     * @return ?????????????? ??????
     */
    public String decrypt(String str){
        byte[] dec = MyBase64.decode(str);
        byte[] utf8 = null;
        String ret = null;
        try {
            utf8 = dcipher.doFinal(dec);
            ret = new String(utf8, "UTF8");
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public byte[] encryptBytes(byte[] bytes){
        byte[] ret = null;
        try {
            ret = ecipher.doFinal(bytes);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public byte[] decryptBytes(byte[] bytes){
        byte[] ret = null;
        try {
            ret=dcipher.doFinal(bytes);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
}