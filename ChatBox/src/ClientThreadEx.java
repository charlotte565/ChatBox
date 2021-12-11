import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientThreadEx extends Thread {

	Socket socket;
	UIClient uiClient;
	String firstName;
	String lastName;
	String username;
	BufferedReader input;
	PrintWriter out;
	String password;
	Label connectLabel;
	boolean connectedSuccess;

	public ClientThreadEx(Socket s, UIClient uiClient, String firstName, String lastName, String username, String password, Label connectLabel) {
		this.socket = s;
		this.uiClient = uiClient;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.connectLabel = connectLabel;
		this.connectedSuccess = false;
	}

	public void run() {
		System.out.println("In run...");
		try {
			
//			uiClient.label.set(0, "Trying to connect...");
			
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			System.out.println("waiting for server");

			out = new PrintWriter(socket.getOutputStream(), true);

			out.println("connect");

			String answer = input.readLine();

			if (answer.equals("connectionSuccess")) {
				
				out.println(firstName);
				System.out.println(firstName);
				out.println(lastName);
				System.out.println(lastName);
				out.println(username);
				System.out.println(username);
				out.println(password);
			}
			if(input.readLine().equals("correctPassword")) {
				System.out.println("pass word correct");
//				uiClient.label.set(0,"Connected to server successfully");
				connectedSuccess = true;
			out.println("users list");
			int numUsers = Integer.valueOf(input.readLine());
//			uiClient.options.clear();
			for(int i = 0; i < numUsers ; i++ ) {
				String username = input.readLine();
				uiClient.options.add(username);
			}

//					String line = input.readLine();
//					System.out.println(line);
//					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//					out.println("connect");
//					if(line.equals("open")) {
//						System.out.println("sending connect");
//
//						out.println("connected");
//						
//				}
		}else {
			System.out.println("Incorrect password");
//			uiClient.label.set(0, "Incorrect password or username");
			connectedSuccess = false;
		}
			} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void sendMessage(String recipient, String message) throws IOException {
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.println("waiting for server");

		out = new PrintWriter(socket.getOutputStream(), true);

		out.println("sendMessage");

		String answer = input.readLine();

		System.out.println(answer);
		try {
			if (answer.equals("waitingForMessage")) {
				out.println(username);
				System.out.println(username);

				out.println(recipient);
				System.out.println(recipient);
				out.println(message);
				System.out.println(message);
			}
		} catch (Exception e) {

		}
	}

	public void checkMessage(ListView<String> chatBox) throws NumberFormatException, IOException {
		System.out.println("I want to send message");
		out.println("doesAnyoneLoveMe?");
		System.out.println(input.readLine());
		out.println(username);
		if (input.readLine().equals("yes")) {
			System.out.println("yes");
			String num = input.readLine();
			System.out.println(num);
			chatBox.getItems().clear();
			for (int numUnread = Integer.valueOf(num); numUnread > 0; numUnread--) {
//				System.out.println(Integer.valueOf(input.readLine()));
//				String recipient = input.readLine();
				String message = input.readLine();
//				System.out.println(recipient + " " + message);

				Label messageLabel = new Label();
//				messageLabel.setText(recipient + " " + message);
				chatBox.getItems().add(message);

			}

		}
	}

	public boolean isConnectedSuccess() {
		return connectedSuccess;
	}

	public void setConnectedSuccess(boolean connectedSuccess) {
		this.connectedSuccess = connectedSuccess;
	}

}
