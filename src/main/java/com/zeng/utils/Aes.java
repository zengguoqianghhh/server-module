package com.zeng.utils;


import com.zeng.bean.UFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

public class Aes {

    /**
     *  字节流转字符串
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
    /**
     *  生成随机秘钥
     */
    public String getKey() {

        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            return s;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }
        return "null";
    }
    /**
     * 加密字符串
     *
     * @param content 需要加密的内容
     * @param sKey 密钥
     * @return
     */
    public byte[] encrypt(String content, String sKey) {
        try {
            // 初始化 加密器
            Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
            byte[] byteContent = content.getBytes("utf-8");
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密字符串
     * @param content  加密的内容
     * @param sKey      加密使用的密钥
     * @return
     */
    public byte[] decrypt(byte[] content, String sKey) {
        try {
            // 初始化 加密器
            Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对文件进行AES加密
     * @param inputStream  获取文件的输入流
     * @param encrypfile  加密文件
     * @param sKey 加密密钥
     */
    public File encryptFile(InputStream inputStream, File encrypfile, String sKey) {

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(encrypfile);
            Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
            // 以加密流写入文件
            CipherInputStream cipherInputStream = new CipherInputStream(
                    inputStream, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
            cipherInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encrypfile;
    }


    /**
     *  AES方式解密文件
     *  @param  u   需要解密的文件
     * @return  解密完成的文件
     */
    public File decryptFile(UFile u) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File outFile = null;
        byte[] re = null;
        try {
            Cipher cipher = initAESCipher(u.getSecurity(), Cipher.DECRYPT_MODE);
            inputStream = new FileInputStream(u.getSaveAddr());
            outFile= new File(".\\src\\main\\webapp\\files\\"+u.getPrimeName()+"."+u.getFileType());
            outputStream = new FileOutputStream(outFile);

            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream,cipher);

            byte[] buffer = new byte[1024];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }
            cipherOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException ee) {
                ee.printStackTrace();

            }
        }
        return outFile;
    }

    /**
     * 初始化 AES Cipher
     * @param sKey     密钥
     * @param cipherMode        加密的模式
     * @return
     */
    public Cipher initAESCipher(String sKey, int cipherMode) {
        // 创建Key gen
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] codeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
            cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        return cipher;
    }
}
