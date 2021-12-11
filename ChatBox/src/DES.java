/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.CryptoException;

//import sun.misc.BASE64Encoder;

/**
 *
 * @author ahmed
 */
public class DES {

	private SecretKey secretkey;

	public DES() throws NoSuchAlgorithmException {
		generateKey();
	}

	/**
	 * Step 1. Generate a DES key using KeyGenerator
	 */

	public void generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		this.setSecretkey(keyGen.generateKey());
	}

	public void saveKey(String secretKeyFileName) throws FileNotFoundException, IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(secretKeyFileName));
		try {
			oout.writeObject(this.secretkey);
		} finally {
			oout.close();
		}
	}

	public void loadKey(String secretKeyFileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(secretKeyFileName));
		try {
			this.secretkey = (SecretKey) in.readObject();
		} finally {
			in.close();
		}
	}

	public byte[] encrypt(String strDataToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES"); // Must specify the mode explicitly as most JCE providers default
														// to ECB mode!!
		desCipher.init(Cipher.ENCRYPT_MODE, this.getSecretkey());
		byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
		byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);
		return byteCipherText;
	}

	public String decrypt(byte[] strCipherText) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES"); // Must specify the mode explicitly as most JCE providers default
														// to ECB mode!!
		desCipher.init(Cipher.DECRYPT_MODE, this.getSecretkey());
		byte[] byteDecryptedText = desCipher.doFinal(strCipherText);
		return new String(byteDecryptedText);
	}

	public String decrypt(String strCipherText, SecretKey sKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES"); // Must specify the mode explicitly as most JCE providers default
														// to ECB mode!!
		byte[] byteDataToDecrypt = strCipherText.getBytes();
		desCipher.init(Cipher.DECRYPT_MODE, sKey);
		byte[] byteDecryptedText = desCipher.doFinal(byteDataToDecrypt);
		return new String(byteDecryptedText);
	}

	public String decryptWithKey(byte[] strCipherText, SecretKey sKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES"); // Must specify the mode explicitly as most JCE providers default
														// to ECB mode!!
		desCipher.init(Cipher.DECRYPT_MODE, sKey);
		byte[] byteDecryptedText = desCipher.doFinal(strCipherText);
		return new String(byteDecryptedText);
	}

	public void encrypt(File inputFile, File outputFile) throws CryptoException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {

		Cipher desCipher = Cipher.getInstance("DES");
		desCipher.init(Cipher.ENCRYPT_MODE, this.getSecretkey());

		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] inputBytes = new byte[(int) inputFile.length()];
		inputStream.read(inputBytes);

		byte[] outputBytes = desCipher.doFinal(inputBytes);

		FileOutputStream outputStream = new FileOutputStream(outputFile);
		outputStream.write(outputBytes);

		inputStream.close();
		outputStream.close();

//        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}

	public void decrypt(File inputFile, File outputFile) throws CryptoException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		Cipher desCipher = Cipher.getInstance("DES");
		desCipher.init(Cipher.DECRYPT_MODE, this.getSecretkey());

		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] inputBytes = new byte[(int) inputFile.length()];
		inputStream.read(inputBytes);

		byte[] outputBytes = desCipher.doFinal(inputBytes);

		FileOutputStream outputStream = new FileOutputStream(outputFile);
		outputStream.write(outputBytes);

		inputStream.close();
		outputStream.close();
	}

	/**
	 * @return the secretkey
	 */
	public SecretKey getSecretkey() {
		return secretkey;
	}

	/**
	 * @param secretkey the secretkey to set
	 */
	public void setSecretkey(SecretKey secretkey) {
		this.secretkey = secretkey;
	}
}
