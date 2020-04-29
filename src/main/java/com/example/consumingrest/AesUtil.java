package com.example.consumingrest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AesUtil {
    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;
    private final String passphrase;
    
    public AesUtil(int keySize, int iterationCount, String passphrase) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.passphrase = passphrase;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException e) {
            throw fail(e);
        } catch (NoSuchPaddingException e) {
			throw fail(e);
		}
    }
    
    public String encrypt(String salt, String iv, String plaintext) {
        try {
            SecretKey key = generateKey(salt);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }
    
    public String decrypt(String salt, String iv, String ciphertext) {
        try {
            SecretKey key = generateKey(salt);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
            return null;
        }catch (Exception e){
        	e.printStackTrace();
            return null;
        }
    }
    
    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }
    
    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException e) {
        	e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e){
        	e.printStackTrace();
        	return null;
        } catch (IllegalBlockSizeException e){
        	e.printStackTrace();
        	return null;
        } catch (BadPaddingException e){
        	e.printStackTrace();
        	return null;
        }
    }
    
    private SecretKey generateKey(String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(this.passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
            return null;
        } catch (InvalidKeySpecException e) {
        	e.printStackTrace();
        	return null; 
		}
    }
    
    private static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }
    
    private static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }
    
    private static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
    
    private static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private IllegalStateException fail(Exception e) {
    	e.printStackTrace();
        return null;
    }

}
