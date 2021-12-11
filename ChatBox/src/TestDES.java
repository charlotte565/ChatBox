/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import com.sun.xml.internal.messaging.saaj.util.Base64;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.crypto.CryptoException;
//import sun.misc.BASE64Encoder;

/**
 *
 * @author ahmed
 */
public class TestDES {
	
	public TestDES() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IOException {
//		TestDesFile();
		TestDesByAli();
	}
	
	public void TestDesByAli() {
		try
        {
            DES des1 = new DES();
            String msg = "Welcome everybody. This is Ali :)";
            
            
            System.out.println("The plain text: "+msg);
            
            byte[] encText = des1.encrypt(msg);
            
            //String cipherText = new String(encText);
            //System.out.println("The DES encrypted message: "+ cipherText);
            //System.out.println("The DES encrypted message 64: "+ (new BASE64Encoder().encode(encText)));
            System.out.println("cipher: " + new String(encText).getBytes("UTF-8").toString());
            
            des1.saveKey("sessionKey1.key");
                        
            DES des2 = new DES();
            des2.loadKey("sessionKey1.key");
            //des2.setSecretkey(des1.getSecretkey());      
         
            
            String decText = des2.decrypt(encText);
            System.out.println("The DES decrypted message: "+decText);
            
        }
        catch(Exception e)
        {
            System.out.println("Error in DES: "+e);   
            e.printStackTrace();
        }
	}
	
	public void TestDesFile() throws InvalidKeyException, IllegalBlockSizeException, 
	BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException {
		
		
        File inputFile = new File("document.txt");
        File encryptedFile = new File("document.encrypted");
        File decryptedFile = new File("document.decrypted");
         
        try {
        	DES des1 = new DES();
            des1.encrypt(inputFile, encryptedFile);
            
            des1.saveKey("sessionKey1.key");
            
            DES des2 = new DES();
            des2.loadKey("sessionKey1.key");
            
            des1.decrypt( encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
	}

    /**
     * @param args the command line arguments
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws InvalidKeyException 
     */
    public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IOException{
        new TestDES();
        
    }
    
    
    
}
