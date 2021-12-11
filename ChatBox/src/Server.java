import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import javafx.collections.ObservableList;

public class Server {
	ServerSocket listener;
	
	DatabaseHandler dbHandler;
	private HashMap<String, User> users;
	
	public Server(int port, ObservableList<Client> ipAddressList, HashMap<String, User> users)throws IOException, InterruptedException, ExecutionException,  InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		listener = new ServerSocket(port);
		System.out.println("Server started on " + port);
		
		dbHandler = new DatabaseHandler();
		
		try {
			while(true && listener!=null) {
				Socket socket = listener.accept();
				new ServerThread(socket, ipAddressList, dbHandler, users).start();
			}
			System.out.println("Outta loop");
		}
		finally {
			listener.close();
		}
		
		
		
    }

	public void myStop()
	{
		System.out.println("Crazy");
		try {
			listener.close();
			listener = null;
			System.out.println("Server Stopped!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * Runs the server.
     */
//    public static void main(String[] args) throws IOException {
//        new Server();
//    }
	
	
}
