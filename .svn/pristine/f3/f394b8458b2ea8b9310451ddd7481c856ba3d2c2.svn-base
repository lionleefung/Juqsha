package com.lightcone.feedback.http;


import android.util.Base64;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtil {

    private static final String KEY = "com.lightcone.app";// 对称密钥,两段一致
    private static final String ALGORITHM_CIPHER = "DES/CBC/PKCS7Padding";// 这里不可修改

    public static String encrypt(String data) {
        if (data == null) {
            return null;
        }
        byte[] bt = null;
        try {
            bt = encrypt(data.getBytes("utf-8"), KEY.getBytes("utf-8"));
        } catch (Exception e) {
        }
        String strs = Base64.encodeToString(bt, Base64.NO_WRAP);
        return strs;
    }

    public static String decrypt(String data) {
        if (data == null) {
            return null;
        }
        byte[] buf;
        byte[] bt = null;
        try {
            buf = Base64.decode(data.getBytes("utf-8"), Base64.NO_WRAP);
            bt = decrypt(buf, KEY.getBytes());
        } catch (Exception e) {
        }
        if (null == bt) {
            return "";
        }
        try {
            return new String(bt, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        // SecureRandom sr = new SecureRandom();//有IV可以不用随机种子

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER, "BC");

        // 用密钥初始化Cipher对象
        byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec iv2 = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv2);

        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        // SecureRandom sr = new SecureRandom();//有IV可以不用随机种子

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER, "BC");

        // 用密钥初始化Cipher对象
        byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec iv2 = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv2);
        // cipher.init(Cipher.DECRYPT_MODE, securekey, sr);//不用传递IV的方式
        return cipher.doFinal(data);
    }

    public static String getMD5(String value) {

        String s = null;
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;

    }
}
