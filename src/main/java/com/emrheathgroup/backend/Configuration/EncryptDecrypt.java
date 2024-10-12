package com.emrheathgroup.backend.Configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

class EncryptDecrypt {

	static byte[] key = "!@#$!@#$%^&**&^%".getBytes();
	final static String algorithm = "AES";

	public static String encrypt(String data) {

		byte[] dataToSend = data.getBytes();
		Cipher c = null;
		try {
			c = Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		SecretKeySpec k = new SecretKeySpec(key, algorithm);
		try {
			c.init(Cipher.ENCRYPT_MODE, k);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] encryptedData = "".getBytes();
		try {
			encryptedData = c.doFinal(dataToSend);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		byte[] encryptedByteValue = Base64.getEncoder().encode(encryptedData);
		return new String(encryptedByteValue);// .toString();
	}

	public static String decrypt(String data) {

		byte[] encryptedData = Base64.getDecoder().decode(data);
		Cipher c = null;
		try {
			c = Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		SecretKeySpec k = new SecretKeySpec(key, algorithm);
		try {
			c.init(Cipher.DECRYPT_MODE, k);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
		byte[] decrypted = null;
		try {
			decrypted = c.doFinal(encryptedData);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return new String(decrypted);
	}
}