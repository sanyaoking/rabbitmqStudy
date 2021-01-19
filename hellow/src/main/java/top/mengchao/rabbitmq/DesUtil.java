package top.mengchao.rabbitmq;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Security;

/**
 * DES加密 解密算法
 *
 * @author lifq
 * @date 2015-3-17 上午10:12:11
 */
public class DesUtil {

    private final static String DES = "DES";
    private final static String ENCODE = "GBK";

    public static void main(String[] args) throws Exception {
        /**
         * 加密方法
         */
        //约定的加密key
        String key = "yw_jd_ysyw_jd_ys";
        String m = DesUtil.encrypt("select * from dual",key);
        System.out.println("加密后字符串："+m);
        /**
         * 解密方法
         */
        String s = DesUtil.decrypt(m,key);
        System.out.println("解密后字符串："+s);
    }


    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
//        String strs = new BASE64Encoder().encode(bt);
        String strs = Base64.encodeBase64String(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null) {
            return null;
        }
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] buf = decoder.decodeBuffer(data);
        byte[] buf = Base64.decodeBase64(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }



    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        IvParameterSpec iv = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(key);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        try {
            byte[] original = cipher.doFinal(data);
            return original;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}