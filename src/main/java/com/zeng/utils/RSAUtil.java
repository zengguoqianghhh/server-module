package com.zeng.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * <p>
 * 封装同RSA非对称加密算法有关的方法，可用于数字签名，RSA加密解密
 * </p>
 *
 * @Copyright:WDSsoft
 */

public class RSAUtil {

    public RSAUtil() {
    }

    /**
     * 使用私钥加密数据
     * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
     *
     * @param keyInByte 打包成byte[]的私钥
     * @param source    要签名的数据，一般应是数字摘要
     * @return 签名 byte[]
     */
    public static byte[] sign(byte[] keyInByte, byte[] source) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(keyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(privKey);
            sig.update(source);
            return sig.sign();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证数字签名
     *
     * @param keyInByte 打包成byte[]形式的公钥
     * @param source    原文的数字摘要
     * @param sign      签名（对原文的数字摘要的签名）
     * @return 是否证实 boolean
     */
    public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            Signature sig = Signature.getInstance("SHA1withRSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            sig.initVerify(pubKey);
            sig.update(source);
            return sig.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
     *
     * @return 包含打包成byte[]形式的私钥和公钥的object[], 其中，object[0]为私钥byte[],object[1]为公钥byte[]
     */
    public static Object[] giveRSAKeyPairInByte() {
        KeyPair newKeyPair = creatmyKey();
        if (newKeyPair == null) {
            return null;
        }
        Object[] re = new Object[2];
        if (newKeyPair != null) {
            PrivateKey priv = newKeyPair.getPrivate();
            byte[] b_priv = priv.getEncoded();
            PublicKey pub = newKeyPair.getPublic();
            byte[] b_pub = pub.getEncoded();
            re[0] = b_priv;
            re[1] = b_pub;
            return re;
        }
        return null;
    }

    /**
     * 新建密钥对
     *
     * @return KeyPair对象
     */
    public static KeyPair creatmyKey() {
        KeyPair myPair;
        long mySeed;
        mySeed = System.currentTimeMillis();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();
        } catch (Exception e1) {
            return null;
        }
        return myPair;
    }

    /**
     * 使用RSA公钥加密数据
     *
     * @param pubKeyInByte 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public  byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 用RSA私钥解密
     *
     * @param privKeyInByte 私钥打包成byte[]形式
     * @param data          要解密的数据
     * @return 解密数据
     */
    public  byte[] decryptByRSA(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 使用RSA私钥加密数据
     *
     * @param privKeyInByte 打包的byte[]形式私钥
     * @param data          要加密的数据
     * @return 加密数据
     */
    public  byte[] encryptByRSA1(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 用RSA公钥解密
     *
     * @param pubKeyInByte 公钥打包成byte[]形式
     * @param data         要解密的数据
     * @return 解密数据
     */
    public  byte[] decryptByRSA1(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 计算字符串的SHA数字摘要，以byte[]形式返回
     */
    public static byte[] MdigestSHA(String source) {
        //byte[] nullreturn = { 0 };
        try {
            MessageDigest thisMD = MessageDigest.getInstance("SHA");
            byte[] digest = thisMD.digest(source.getBytes("UTF-8"));
            return digest;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 测试
     //     */
//    public static void main(String[] args) {
//        try {
//            String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEGENnf3rdiO20isoLQqezw12FoWXII9FBw8nR1MWQ3X0CVzOsqY1hOmxD/YI9OB7WVIaVax5tj1l+wk6A0v85Z4OpGWqz4B5L3fCUlBwf/M6DXHlSN1OZttvQF3OeWvc6gvJHihR7pp18zc4KfCJx0Ry6IrGH/2SNOVE1AIgvRQIDAQAB";
//            String PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIQYQ2d/et2I7bSKygtCp7PDXYWhZcgj0UHDydHUxZDdfQJXM6ypjWE6bEP9gj04HtZUhpVrHm2PWX7CToDS/zlng6kZarPgHkvd8JSUHB/8zoNceVI3U5m229AXc55a9zqC8keKFHumnXzNzgp8InHRHLoisYf/ZI05UTUAiC9FAgMBAAECgYAGNcHNds/G5G4QY8n1149cwx19b8YCL7Thu5ucUr1q/w6mcoUKY/oyjPWUCLH7wMyqVNTy51NJ4UhazjW0lrbK4ZbPDHFij9CiZ7QFASiQ/TQWaL+KSIWnE6/rK9IdouwFKxk+cvvLteZoAXP6mFcrsa7LzfkENiIMu7mjpTNHAQJBANXv9U5JWOAVhWHDQcEWKn7YKpAVRleXdeUeJrXcdkqBDI+P6suA9j+ahDREfu+x65wUsrJotPHUXgJG0TarJIUCQQCeEPLrv6Qvi5+nbn2Eifn/fjsmIdI0U2WZKDHWJEnLsRUuGDNYxVE/SPDNDedA2OHeFB6j0Kk/ECdsWnUq6zvBAkAgUGViFMwa1MVX1fFZo+p5TFdpef0s/9Cr8djxAULQ0BtAmAFkCa+oPcOYTXxK4jnvUmUHc69ZE7W7bEzvj/wtAkB50X4mClAzBFxK4XCC0QOG0HYtcStbgFpwqvWdn+Hvxc4Y9DW+WHPBXimXHvv2ki+gw8jJX2rQW1bGvwBFz30BAkASPkORJxVWv91StjI2f/HXDO5eG5/su/XIb3eajaLUSEdaQlcs3ywLrrJ0o3VAR0J9aq59cmp6em017AMnmbF7";
//
//            byte[] signPrivate = Base64.decodeBase64(PRIVATEKEY.getBytes());
//            byte[] signPublic = Base64.decodeBase64(PUBLICKEY.getBytes());
//            //私钥加密 公钥解密
//            //生成私钥-公钥对
//            Object[] v = giveRSAKeyPairInByte();
//            //获得摘要
//            byte[] source = MdigestSHA("假设这是要加密的客户数据");
//            //使用私钥对摘要进行加密 获得密文 即数字签名
//            byte[] sign = sign(signPrivate, source);
//            System.out.println("signature : "+new String(sign));
//            //使用公钥对密文进行解密,解密后与摘要进行匹配
//            boolean yes = verify(signPublic, source, sign);
//            if (yes) {
//                System.out.println("匹配成功 合法的签名!");
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
