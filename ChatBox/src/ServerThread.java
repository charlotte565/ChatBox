import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import javafx.collections.ObservableList;

public class ServerThread extends Thread{

	Socket socket;
	ObservableList<Client> ipAddressList;
	DatabaseHandler dbHandler;
	HashMap<String, User> users;
	String ipAddress;

	public ServerThread(Socket s, ObservableList<Client> ipAddressList, DatabaseHandler dbHandler, HashMap<String, User> users) {
		this.socket = s;
		this.ipAddressList = ipAddressList;
		this.dbHandler = dbHandler;
		this.users = users;
		this.ipAddress =  socket.getLocalAddress().toString();
	}
	
	public void run() {
		try {

			

			System.out.println("A client request recevied at "+ socket);
			
		
			
			PrintWriter out = new 
					PrintWriter(socket.getOutputStream(), true);
			
			BufferedReader input = new 
					BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
			
			
			while(true) {
				String answer = input.readLine();
				
				
				System.out.println(answer);
				if(answer.equals("connect")) {
					out.println("connectionSuccess");
					
				String clientFName = input.readLine();
				String clientLName = input.readLine();
				String username = input.readLine();
				String password = input.readLine();
				
				if(!dbHandler.checkPassword(dbHandler.getDb(), username, password)) {
					out.println("incorrectPassword");
				}
				else {
					out.println("correctPassword");	
				Client client = new Client(username, socket.getInetAddress().toString());
				
//				users.get(username).setClient(client);
//				users.get(username).setSocket(socket);
				
				
				
				System.out.println(client);
				ipAddressList.add(client);
				
				if(input.readLine().equals("users list")) {
					ArrayList<String> users = dbHandler.getUsers(dbHandler.getDb());
					out.println(users.size());
					for(String user : users) {
						out.println(user);
					}
				}
				
				}}
				else if(answer.equals("sendMessage")) {
//				out = new PrintWriter(socket.getOutputStream(), true);
				
				out.println("waitingForMessage");
				
				String username = input.readLine();
				String recipient = input.readLine();
				String message = input.readLine();
				
//				Message messageObject = new Message(message, username, recipient);
//				System.out.println(messageObject);
//				System.out.println(users.get(recipient));
//				users.get(recipient).getMessages().add(messageObject);
				dbHandler.saveMessages(dbHandler.getDb(), message, recipient, username);
				
//				users.get(recipient).setUnreadMessages(true);
//				users.get(recipient).setUndreadMessageNum(users.get(recipient).getUndreadMessageNum()+1);
				
				
				
			}
				else if(answer.equals("doesAnyoneLoveMe?")) {
					System.out.println("someone might love you");
					out.println("someoneMight");
					String username = input.readLine();
					System.out.println(username);
					System.out.println("found");
					ArrayList<String> messages = dbHandler.readMessage(dbHandler.getDb(), username);
					if(!messages.isEmpty()) {
						out.println("yes");
						out.println(messages.size());
						for(String m : messages) {
							out.println(m);
						}
					}
					
//					if(users.get(username).isUnreadMessages()) {
//						out.println("yes");
//						System.out.println("yes");
//						out.println(users.get(username).getUndreadMessageNum());
//						System.out.println(users.get(username).getUndreadMessageNum());
//						for(Message m : users.get(username).getMessages()) {
//							if(!m.isSent()) {
//								System.out.println(m.getRecipient() + " "+m.getMessage());
//								out.println(m.getUsername());
//								out.println(m.getMessage());
//							}
//						}
//						out.println("endOfMessages");
//					}
					
					else {
						out.println("no");
						System.out.println("no messages");
					}
				}
//					else {
//						out.println("noLoser");
//					}
				
//				System.out.println("waiting..");
//				String line = input.readLine();
//				System.out.println("M: " + line);
				
//				line = input.readLine();
//				System.out.println("M1" + line);
//			
			}
			//System.out.println("Finished .......");
			
//			String answer = input.readLine();
//			
//			System.out.println(answer);
//			
//			if(answer.equals("connect")) {
//				
//
//				out.println("connectionSuccess");
//				
//				String clientFName = input.readLine();
//				String clientLName = input.readLine();
//				String username = input.readLine();
//				
//				Client client = new Client(clientFName, clientLName, socket.getInetAddress().toString());
//				
//				users.get(username).setClient(client);
//				users.get(username).setSocket(socket);
//				
//				
//				
//				System.out.println(client);
//				ipAddressList.add(client);
//				
//				
//			}
//			else if(answer.equals("sendMessage")) {
//				out = new PrintWriter(socket.getOutputStream(), true);
//				
//				out.println("waitingForMessage");
//				
//				String username = input.readLine();
//				String recipient = input.readLine();
//				String message = input.readLine();
//				
//				users.get(recipient).getMessages().add(new Message(message, username, recipient));
//				users.get(recipient).setUnreadMessages(true);
//				users.get(recipient).setUndreadMessageNum(users.get(recipient).getUndreadMessageNum()+1);
//				
//				
//				
////				out.println(breakfastMenu.aFoodItemAsString(answer));
//				
//				
//			}
//			else if(answer.equals("objectSend")) {
//				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//    			// write object to file
////    			oos.writeObject(food);
//    		
//			}
//			else if(answer.equals("doesAnyoneLoveMe?")) {
//				String username = input.readLine();
//				out = new PrintWriter(socket.getOutputStream(), true);
//				System.out.println("found");
//				if(users.get(username).isUnreadMessages()) {
//					out.println("yes");
//					System.out.println("yes");
//					out.println(users.get(username).getUndreadMessageNum());
//					System.out.println(users.get(username).getUndreadMessageNum());
//					for(Message m : users.get(username).getMessages()) {
//						if(!m.isSent()) {
//							System.out.println(m.getRecipient() + " "+m.getMessage());
//							out.println(m.getRecipient());
//							out.println(m.getMessage());
//						}
//					}
//					out.println("endOfMessages");
//				}
////				else {
////					out.println("noLoser");
////				}
//				
//			
//					
//				
//			}
			
			
//			socket.close();
					
		}
		catch(IOException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	public BreakfastMenu getBreakfast() throws JAXBException, IOException {
//		JAXBContext jaxbContext = JAXBContext.newInstance(BreakfastMenu.class);
//	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//	     
//	    //We had written this file in marshalling example
//	   
//	    
//	    BreakfastMenu breakfastMenu = (BreakfastMenu) jaxbUnmarshaller.unmarshal( new File("C:\\Users\\ralphschar\\Documents\\breakfast.xml") );
//	    for(Food food : breakfastMenu.getBreakfastMenu())
//	    {
//	        System.out.println(food.getName());
//	        System.out.println(food.getPrice());
//	        System.out.println(food.getDescription());
//	        System.out.println(food.getCalories());
//	    }
//	    return breakfastMenu;
//	}
}
