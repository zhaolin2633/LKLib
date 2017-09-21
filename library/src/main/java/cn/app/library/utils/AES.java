package cn.app.library.utils;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/05/04
 * desc :
 * </pre>
 */
public class AES {
    // 设置加密密匙
    public static String masterPassword = "C5vEBo83dh/3h2W#";

    // AES加密
    // encryptText为要加密的内容
    // seed为密匙
    public static String encrypt(String encryptText)
            throws Exception {
        byte[] rawKey = getRawKey(masterPassword.getBytes("UTF-8"));
        byte[] result = encrypt(rawKey, encryptText.getBytes("UTF-8"));
        return toHex(result);
    }

    // AES解密
    // decryptText为需要解密的内容
    // seed为密匙
    public static String decrypt(String decryptText)
            throws Exception {
        byte[] rawKey = getRawKey(masterPassword.getBytes());
        byte[] enc = toByte(decryptText);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);

    }

    // AES加密
    // encryptText为要加密的内容
    // seed为密匙
    public static String encrypt(String seed, String encryptText)
            throws Exception {

        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, encryptText.getBytes());
        return toHex(result);
    }

    // AES解密
    // decryptText为需要解密的内容
    // seed为密匙
    public static String decrypt(String seed, String decryptText)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(decryptText);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);

    }

    // 加密
    private static byte[] encrypt(byte[] raw, byte[] bytes) throws Exception {
        // 生成一组扩展密匙，并放入一个数组之中
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        // 用ENCRYPT_MODE模式,用skeySpec密码组，生成AES加密方法
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        // 得到加密数据
        byte[] encrypted = cipher.doFinal(bytes);
        return encrypted;
    }

    // 解密
    private static byte[] decrypt(byte[] rawKey, byte[] enc) throws Exception {
        // 生成一组扩展密匙，并放入一个数组之中
        SecretKeySpec skeyKeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // 用DECRYPT_MODE模式,用skeySpec密码组，生成AES解密方法
        cipher.init(Cipher.DECRYPT_MODE, skeyKeySpec);
        // 得到解密数据
        byte[] decrypted = cipher.doFinal(enc);
        return decrypted;
    }

    // 对密匙进行编码
    private static byte[] getRawKey(byte[] bytes) throws Exception {
        // 获取密匙生成器
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(bytes);
        // 生成256位的AES密码生成器
        kgen.init(256);
        // 生成密匙
        SecretKey sKey = kgen.generateKey();
        // 编码格式
        byte[] raw = sKey.getEncoded();
        return raw;
    }

    // 将十六进制字符串为十进制字符串
    private static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    // 将十六进制字符串为十进制字节数组
    private static byte[] toByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hex.substring(2 * i, 2 * i + 2), 16)
                    .byteValue();
        }
        return result;
    }

    // 把一个十进制字节数组转换成十六进制
    private static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    // 把一个十进制字节数组转换成十六进制
    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789abcdef";

    private static void appendHex(StringBuffer result, byte b) {
        result.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0X0f));

    }

    /**
     * 加密
     * ECB 密码必须16 否则报错
     * @param encryptText
     * @return
     * @throws Exception
     */
    public static String encrypt2(String encryptText) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(masterPassword.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptText.getBytes("utf-8"));

            return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 解密
     * ECB 密码必须16 否则报错
     * @param decryptText
     * @return
     * @throws Exception
     */
    public static String decrypt2(String decryptText) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(masterPassword.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = android.util.Base64.decode(decryptText, android.util.Base64.DEFAULT);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 加密（两次base64）
     * ECB 密码必须16 否则报错
     * @param encryptText
     * @return
     * @throws Exception
     */
    public static String encrypt2Base64(String encryptText) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(masterPassword.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptText.getBytes("utf-8"));
            String encodeToString = Base64.encodeToString(encrypted, Base64.DEFAULT);
            return Base64.encodeToString(encodeToString.getBytes(), Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密（两次base64）
     * ECB 密码必须16 否则报错
     * @param decryptText
     * @return
     * @throws Exception
     */
    public static String decrypt2Base64(String decryptText) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(masterPassword.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = android.util.Base64.decode(android.util.Base64.decode(decryptText, android.util.Base64.DEFAULT),android.util.Base64.DEFAULT);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
