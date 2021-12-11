import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


import javafx.collections.ObservableList;

public class ClientService {
//	ServerSocket listener;
	Socket socket;
	UIClient uiClient;
	
	public ClientService(int port , UIClient uiClient, String ipAddress)throws IOException, InterruptedException, ExecutionException {
		
		socket = new Socket(ipAddress, port);
		System.out.println("Server started on " + port);
		
		
		

				new ClientThread(socket, uiClient).start();

		
		
		
    }
	
	

	public void myStop()
	{
		System.out.println("Crazy");
	}
}
