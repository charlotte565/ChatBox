import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;



public class DatabaseHandler {
	
	private Firestore db;
	private SecretKey sKey;
//	private DES des;
	
    public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException,  InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
    	
        new DatabaseHandler();
    }

	
	public DatabaseHandler() throws IOException, InterruptedException, ExecutionException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		//authentication
		GoogleCredentials credentials = authExplicit("chatbox-daa84-f785816c7c38.json");
		
		//get the instance of the project
		db = getInstance(credentials, "chatbox-daa84");
		
//		saveSomeUsers(db);
//		printUser(db);
//		saveMoreUsers(db);
		//mainpulate the instance
//		saveData(db);
//		saveDataBreakfast(db);
//		viewBreakfast(db);
	}
	
	public Firestore getInstance(GoogleCredentials credentials, String projectId) {
		FirebaseOptions options = new FirebaseOptions.Builder()
			    .setCredentials(credentials)
			    .setProjectId(projectId)
			    .build();
			FirebaseApp.initializeApp(options);

			Firestore db = FirestoreClient.getFirestore();
			return db;
	}
	
	static GoogleCredentials authExplicit(String jsonPath) throws IOException {
		  // You can specify a credential file by providing a path to GoogleCredentials.
		  // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
		  GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
		        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		  
		  return credentials;

		  
		}
	
	public void saveData(Firestore db) throws InterruptedException, ExecutionException {
		DocumentReference docRef = db.collection("studentsBio").document("daniel");
		// Add document data  with id "alovelace" using a hashmap
		Map<String, Object> data = new HashMap<>();
		data.put("first", "Daniel");
		data.put("last", "Ralphs");
		data.put("born", 1995);
		data.put("birthplace", "Northamptom");
		//asynchronously write data
		ApiFuture<WriteResult> result = docRef.set(data);
		// ...
		// result.get() blocks on response
		result.get();
		System.out.println("data saved");
		
	}
	
	public void saveMessages(Firestore db, String message, String recipient, String sender) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InterruptedException, ExecutionException {
		
		// Add document data  with id "alovelace" using a hashmap
		Map<String, Object> data = new HashMap<>();
		data.put("recipient", recipient);
		data.put("sender", sender);
		
		DES des = new DES();
		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		String keyString = "";
		for (QueryDocumentSnapshot document : documents) {
			if(document.getId().equals(sender)) {
			keyString = document.getString("key");
			}
		}
		SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(keyString), 0, Base64.getDecoder().decode(keyString).length, "DES");
		des.setSecretkey(originalKey);
		
		byte[] messageEncrypt = des.encrypt(message);
		String messString = Base64.getEncoder().encodeToString(messageEncrypt);
		
		data.put("message", messString);
		
		db.collection("Users").document(recipient).collection("Messages").add(data);
		
		//asynchronously write data
//		ApiFuture<WriteResult> result = docRef.set(data);
		// ...
		// result.get() blocks on response
//		result.get();
		System.out.println("data saved");
	}
	
	public ArrayList<String> readMessage(Firestore db, String recipient) throws InterruptedException, ExecutionException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		DES des = new DES();
		ApiFuture<QuerySnapshot> query = db.collection("Users").document(recipient).collection("Messages").get();
		// ...
		// query.get() blocks on response
		ArrayList<String> messages = new ArrayList<String>();
		
		HashMap<String, SecretKey> secretKeys = getKeys(db);
		
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		String keyString = "";
		String message = "";
		String sender = "";
		for (QueryDocumentSnapshot document : documents) {
			if(document.getString("recipient").equals(recipient)) {
			message = document.getString("message");
			sender = document.getString("sender");
			
			
			
			byte[] decMessage = Base64.getDecoder().decode(message);
			String messDecrypt = des.decryptWithKey(decMessage, secretKeys.get(sender));
			
			messages.add("From: " + sender+ ": "+ messDecrypt);
			}
		}
		
		
		return messages;
	}
	
	public HashMap<String , SecretKey> getKeys(Firestore db) throws InterruptedException, ExecutionException{
		
		HashMap<String, SecretKey> secretKeys = new HashMap<String, SecretKey>();
		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {
		    
		  String sender = document.getString("username");
		  String key = document.getString("key");
		  
		  SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(key), 0, Base64.getDecoder().decode(key).length, "DES");
		  secretKeys.put(sender, originalKey);
	}
		return secretKeys;}
	
	public ArrayList<String> getUsers(Firestore db) throws InterruptedException, ExecutionException{
		ArrayList<String> users = new ArrayList<String>();
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {
		  
		   users.add(document.getString("username"));
		}
		return users;
	}
	
	public void saveSomeUsers(Firestore db) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		DocumentReference docRef = db.collection("Users").document("Char020");
		// Add document data  with id "alovelace" using a hashmap
		Map<String, Object> data = new HashMap<>();
		data.put("first", "Charlotte");
		data.put("last", "Ralphs");
		data.put("username", "Char020");
		String password = "password";
		DES des = new DES();
		byte[] passEncrypt = des.encrypt(password);
		String passString = Base64.getEncoder().encodeToString(passEncrypt);
		data.put("password", passString);
		
		sKey = des.getSecretkey();
//		System.out.println(sKey);
//		System.out.println(sKey.getAlgorithm());
//		System.out.println(Base64.getEncoder().encodeToString(sKey.getEncoded()));
		String key =Base64.getEncoder().encodeToString(sKey.getEncoded());
//		System.out.println(Base64.getDecoder().decode(key));
		SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(key), 0, Base64.getDecoder().decode(key).length, "DES");
//		System.out.println(originalKey);
//		System.out.println(des.decryptWithKey(passEncrypt, originalKey));
//		String keyString = Base64.getEncoder().encodeToString(sKey.getEncoded());
//		data.put("key", (sKey.getEncoded()).toString());
		
		data.put("key", key);
		
		//asynchronously write data
		ApiFuture<WriteResult> result = docRef.set(data);
		// ...
		// result.get() blocks on response
		result.get();
		System.out.println("data saved");
		
	}
	
	public void saveMoreUsers(Firestore db) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		DocumentReference docRef = db.collection("Users").document("Dan123");
		// Add document data  with id "alovelace" using a hashmap
		Map<String, Object> data = new HashMap<>();
		data.put("first", "Dan");
		data.put("last", "Ralphs");
		data.put("username", "Dan123");
		String password = "passwordDan";
		DES des = new DES();
		byte[] passEncrypt = des.encrypt(password);
		String passString = Base64.getEncoder().encodeToString(passEncrypt);
		data.put("password", passString);
		
		sKey = des.getSecretkey();
//		System.out.println(sKey);
//		System.out.println(sKey.getAlgorithm());
//		System.out.println(Base64.getEncoder().encodeToString(sKey.getEncoded()));
		String key =Base64.getEncoder().encodeToString(sKey.getEncoded());
//		System.out.println(Base64.getDecoder().decode(key));
		SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(key), 0, Base64.getDecoder().decode(key).length, "DES");
//		System.out.println(originalKey);
//		System.out.println(des.decryptWithKey(passEncrypt, originalKey));
//		String keyString = Base64.getEncoder().encodeToString(sKey.getEncoded());
//		data.put("key", (sKey.getEncoded()).toString());
		
		data.put("key", key);
		
		//asynchronously write data
		ApiFuture<WriteResult> result = docRef.set(data);
		// ...
		// result.get() blocks on response
		result.get();
		System.out.println("data saved");
		
	}
	
	public boolean checkPassword(Firestore db, String username, String passwordCheck) throws InterruptedException, ExecutionException, 
	UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			if(document.getString("username").equals(username)) {
				String key = document.getString("key");
				  
				SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(key), 0, Base64.getDecoder().decode(key).length, "DES");
				
				String passwordString = document.getString("password");
				byte[] decPass = Base64.getDecoder().decode(passwordString);
				System.out.println(passwordString);
				byte [] passbyte = passwordString.getBytes("UTF-8");
				DES des = new DES();
//				des.setSecretkey(originalKey);
//				System.out.println(originalKey);
				String password = des.decryptWithKey(decPass, originalKey);
				
				if(password.equals(passwordCheck)) {
					return true;
				}
				 
			}

		  
		  
	
		}
		
		return false;
	}
	

	
//	public void saveDataBreakfast(Firestore db) throws  IOException, JAXBException, InterruptedException, ExecutionException {
//		
//		BreakfastMenu breakfastMenu = getBreakfast();
//		
//		for(Food food : breakfastMenu.getBreakfastMenu()) {
//			DocumentReference docRef = db.collection("BreakfastMenu").document(food.getName());
//			// Add document data  with id "alovelace" using a hashmap
//			Map<String, Object> data = new HashMap<>();
//			data.put("name", food.getName());
//			data.put("price", food.getPrice());
//			data.put("description", food.getDescription());
//			data.put("calories", food.getCalories());
//			//asynchronously write data
//			ApiFuture<WriteResult> result = docRef.set(data);
//			// ...
//			// result.get() blocks on response
//			result.get();
//		}
//		
//		
//		System.out.println("data saved");
//		
//
//		
//	}
	
	public void printUser(Firestore db) throws InterruptedException, ExecutionException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// asynchronously retrieve all users
				ApiFuture<QuerySnapshot> query = db.collection("Users").get();
				// ...
				// query.get() blocks on response
				QuerySnapshot querySnapshot = query.get();
				List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
				for (QueryDocumentSnapshot document : documents) {
				  System.out.println("id: " + document.getId());
				  System.out.println("first: " + document.getString("first"));
				  System.out.println("last: " + document.getString("last"));
			
				  System.out.println("username: " + document.getString("username"));
				  
				  
				  String key = document.getString("key");
				  
				  SecretKey originalKey = new SecretKeySpec(Base64.getDecoder().decode(key), 0, Base64.getDecoder().decode(key).length, "DES");
				
				String passwordString = document.getString("password");
				byte[] decPass = Base64.getDecoder().decode(passwordString);
				System.out.println(passwordString);
				byte [] passbyte = passwordString.getBytes("UTF-8");
				DES des = new DES();
//				des.setSecretkey(originalKey);
//				System.out.println(originalKey);
				String password = des.decryptWithKey(decPass, originalKey);
				
				  System.out.println("password: " + password);
				}
	}
	
	public void viewBreakfast(Firestore db) throws InterruptedException, ExecutionException {
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection("BreakfastMenu").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {
		  System.out.println("id: " + document.getId());
		  System.out.println("name: " + document.getString("name"));
		  System.out.println("price: " + document.getString("price"));
	
		  System.out.println("description: " + document.getString("description"));
		  System.out.println("calories: " + document.getLong("calories"));
		}
	}
	

	public Firestore getDb() {
		return db;
	}


}
