/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ahmed
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.crypto.modes.*;
import org.bouncycastle.crypto.paddings.*;
import org.bouncycastle.crypto.params.*;


public class TestDESBouncyCastle
{

    private SecretKey secretkey; 
    
    
    public TestDESBouncyCastle() throws NoSuchAlgorithmException 
    {
        generateKey();
    }
    
    
    /**
	* Step 1. Generate a DES key using KeyGenerator 
    */
    
    public void generateKey() throws NoSuchAlgorithmException 
    {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        this.setSecretkey(keyGen.generateKey());        
    }
    
    public byte[] encryptDec (String strDataToEncrypt) throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, 
            BadPaddingException, NoSuchProviderException, ShortBufferException, 
            UnsupportedEncodingException, UnsupportedEncodingException, UnsupportedEncodingException
    {        
        
        /*Cipher desCipher = Cipher.getInstance("DES"); // Must specify the mode explicitly as most JCE providers default to ECB mode!!
        desCipher.init(Cipher.ENCRYPT_MODE, this.getSecretkey());
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
	byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);       
        return byteCipherText;*/
        
        
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, this.secretkey);
        
        
        
        // create the cipherText array 128 in case of AES for example
        byte[] cipherText = new byte[cipher.getOutputSize(byteDataToEncrypt.length)];
        
       
        
        int ctLength = cipher.update(byteDataToEncrypt, 0, byteDataToEncrypt.length, cipherText, 0);
        
        
        ctLength += cipher.doFinal(cipherText, ctLength);
        System.out.println("cipher: " + new String(cipherText).getBytes("UTF-8").toString() + " bytes: " + ctLength);
        
        byte[] cipherFinal = new String(cipherText).getBytes("UTF-8");
        
        
        
        return cipherFinal;
    }
    
    public String decryptData(byte[] encrypt) throws ShortBufferException, 
    NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, 
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	
    	Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC");
//        cipher.init(Cipher.ENCRYPT_MODE, this.secretkey);
    	cipher.init(Cipher.DECRYPT_MODE, this.secretkey);
        
        
        // create the cipherText array 128 in case of AES for example
//        byte[] cipherText = new byte[cipher.getOutputSize(encrypt.length)];
        
       
        
        int ctLength = cipher.update(encrypt, 0, encrypt.length, encrypt, 0);
    	
    	// Decrypt
        
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(encrypt, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        System.out.println("plain : " + new String(plainText) + " bytes: " + ptLength);
        
        return new String(plainText);
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
    
    public static void main(String[] args){
        // TODO code application logic here
        try
        {
            TestDESBouncyCastle des1 = new TestDESBouncyCastle();
            String msg = "Welcome everybody. This is Ali :)\n";
            for(int i=0; i<10; i++)
            	msg += "Welcome everybody. This is Ali :)\n";
            
           byte[] encrypt = des1.encryptDec(msg);
            System.out.println( encrypt);
            System.out.println(des1.decryptData(encrypt));
            
        }
        catch(Exception e)
        {
            System.out.println("Error in DES: "+e);   
            e.printStackTrace();
        }
    }

}