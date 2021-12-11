import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import javafx.collections.ObservableList;

public class ClientThread extends Thread{
	Socket socket;
	UIClient uiClient;
	
	public ClientThread(Socket s, UIClient uiClient) {
		this.socket = s;
		this.uiClient = uiClient;
	}
	
	
	public void run() {
		BufferedReader input = null;
		try {
			input = new 
					BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while(true) {
			try {
				
				while(input.ready()) {
					String line = input.readLine();
					System.out.println(line);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println("connect");
					if(line.equals("open")) {
						System.out.println("sending connect");
//						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println("connect");
					}
				}
				

//				System.out.println(input.readLine());
//
//				System.out.println("A server request recevied at "+ socket);
//				
//				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//				
//				
//				
//				out.println("connect");


//				String answer = input.readLine();
//				

//				System.out.println(answer);
//				try {
//				if(answer.equals("connectionSuccess")) {
//					out.println("charlotte");
//					System.out.println("charlotte");
//					out.println("ralphs");
//					System.out.println("ralphs");
//					out.println("daniel1995");
//					System.out.println("daniel1995");
//				}	System.out.println("closing connection..");
			}finally {
				System.out.println("out of loop");
			}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		
	}
			
	}
}
