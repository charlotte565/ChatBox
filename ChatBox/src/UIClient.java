import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIClient extends Application {
	
	private String ipAddress = "";
	private int port = 0;
	private String firstName = "Charlotte";
	private String lastName = "Ralphs";
	private String myipAddress = "ipAddress";
	private String username = "";
	private ClientThreadEx clientThread;
//	private Socket clientSocket;
	
	ServicedClient servicedClient;
	
	ObservableList<String> options = 
		    FXCollections.observableArrayList(
		     
		    );
		final ComboBox<String> comboBox = new ComboBox<String>(options);

		Label connectLabel = new Label();
		ObservableList<String> label = 
			    FXCollections.observableArrayList(
			    		"Enter port and IP to connect to server...."
			    );
				
		
	@Override
	public void start(Stage primaryStage) throws Exception {
		// port and ip display

		TextField portInput = new TextField();
		portInput.setText("9030");
		Label portLabel = new Label();
		portLabel.setText("Enter Port #:");

		HBox portHBox = new HBox(10);
		portHBox.getChildren().addAll(portLabel, portInput);

		TextField ipInput = new TextField();
		ipInput.setText("192.168.56.1");
		Label ipAddressLabel = new Label();
		ipAddressLabel.setText("IP Address: ");
		
		HBox ipAddressHBox = new HBox(10);
		ipAddressHBox.getChildren().addAll(ipAddressLabel, ipInput);
		
		//login set up
		
		TextField usernameInput = new TextField();
		usernameInput.setPromptText("Username");
		
		TextField passwordInput = new TextField();
		passwordInput.setPromptText("password");
		
		Button loginButton = new Button();
		loginButton.setText("Login");

		// button set up

		Button connectButton = new Button();
		connectButton.setText("Connect to Server");
		connectLabel.setText("Enter port and IP to connect to server....");
	
		//Scroll messages
		ScrollPane chatScroll = new ScrollPane();
		
		ListView<String> chatBox = new ListView<String>();
		chatScroll.setContent(chatBox);
		
		//recipient input
		Label recipientLabel = new Label();
		recipientLabel.setText("Select a recipient: ");
		
	
			
		HBox recipientBox = new HBox(10);
		recipientBox.getChildren().addAll(recipientLabel, comboBox);
		
		//Message Input
		TextField messageText = new TextField();
		messageText.setPromptText("Input message to send");
		
		Button sendButton = new Button();
		sendButton.setText("Send");
		
		HBox messageBox = new HBox(10);
		messageBox.getChildren().addAll(messageText, sendButton);
		
		Button checkButton = new Button();
		checkButton.setText("Check for messages");


		// border pane set up
		VBox bigBox = new VBox(10);
		bigBox.getChildren().addAll(portHBox, ipAddressHBox,connectLabel,  usernameInput, passwordInput, loginButton, chatScroll, recipientBox, messageBox, checkButton);

		// button action set up
		loginButton.setOnAction(value -> {
			System.out.println("trying to connect...");
			connectLabel.setText("Enter port and IP to connect to server....");
			chatBox.getItems().clear();
			try {
				
				ipAddress = ipInput.getText();
				port = Integer.valueOf(portInput.getText()) ;
				System.out.println(port);
				System.out.println(ipAddress);
				
//				String connectionStatus = connectToServer();
				
//				connectWithThread();
				connectWithThreads(usernameInput.getText(), passwordInput.getText());
				Thread.sleep(2000);
				if(clientThread.isConnectedSuccess()) {
					connectLabel.setText("Connection to port "+port+" success");
				}else {
					connectLabel.setText("Incorrect password or username entered");
				}
				
			} catch (Exception e) {

			}
		});
		
		checkButton.setOnAction(value->{
			try {
//				checkForMessage(chatBox);
				checkMessageThread(chatBox);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

//		loginButton.setOnAction(value -> {
//			username = usernameInput.getText();
//		});

		sendButton.setOnAction(value -> {
			try {
//				sendAMessage(messageText.getText());
				sendMessageThread(messageText.getText());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// scene set up
		primaryStage.setScene(new Scene(bigBox, 300, 400));
		primaryStage.sizeToScene();
//				primaryStage.getIcons().add(imageAvo);
		primaryStage.setTitle("Chat Box Clientside");
		primaryStage.show();
		

			
//			checkForMessage(chatBox);
//			Thread.sleep(10000);
		

	}
	
	public void connectWithThread() {
		try{servicedClient = new ServicedClient(port, this, ipAddress);
		servicedClient.start();
		
		}
		catch(Exception e) {
			
		}
	}
	
	public void connectWithThreads(String username, String password) throws UnknownHostException, IOException {
		
		
		try{
			options.clear();
//			connectLabel.setText("Trying to connect...");
			if(!password.equals("")&&!username.equals("")) {
			Socket clientSocket = new Socket(ipAddress, port);
			clientThread = new ClientThreadEx(clientSocket, this, firstName, lastName, username, password, connectLabel);
		System.out.println("made client thread");
		clientThread.start();
			}
			else {
				connectLabel.setText("Enter a password and username");
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public String connectToServer() throws UnknownHostException, IOException {
		
		Socket clientSocket = new Socket(ipAddress, port);
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.println("connect");

		String answer = input.readLine();
		

		System.out.println(answer);
		try {
		if(answer.equals("connectionSuccess")) {
			out.println(firstName);
			System.out.println(firstName);
			out.println(lastName);
			System.out.println(lastName);
			out.println(username);
			System.out.println(username);
		}
		

		
			System.out.println("closing connection..");

			clientSocket.close();
			return answer;
		} catch (Exception e) {

		}
		return answer;
		

		
	}
	
	public String sendAMessage(String message) throws UnknownHostException, IOException {
		
		Socket clientSocket = new Socket(ipAddress, port);
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.println("sendMessage");

		String answer = input.readLine();
		

		System.out.println(answer);
		try {
		if(answer.equals("waitingForMessage")) {
			out.println(username);
			System.out.println(username);
			String recipient = comboBox.getValue();
			out.println(recipient);
			System.out.println(recipient);
			out.println(message);
			System.out.println(message);
		}
		

		
			System.out.println("closing connection..");

			clientSocket.close();
			return answer;
		} catch (Exception e) {

		}
		return answer;
		

		
	}
	
	public void sendMessageThread(String message) throws IOException {
		String recipient = comboBox.getValue();
		if(!recipient.equals("")) {
		clientThread.sendMessage(recipient, message);
		}
	}
	
	public void checkMessageThread(ListView<String> chatBox) throws NumberFormatException, IOException {
		clientThread.checkMessage(chatBox);
	}
	
	public void checkForMessage(HBox chatBox) throws IOException {
		Socket clientSocket = new Socket(ipAddress, port);
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		
		out.println("doesAnyoneLoveMe?");
		
		if(input.readLine().equals("yes")){
			System.out.println("yes");
			
			for(int numUnread = Integer.valueOf(input.readLine()) ; numUnread>0; numUnread --) {
				System.out.println(Integer.valueOf(input.readLine()));
				String recipient = input.readLine();
				String message = input.readLine();
				System.out.println(recipient + " "+message);
				
				Label messageLabel = new Label();
				messageLabel.setText(recipient + " "+message);
				chatBox.getChildren().add(messageLabel);
				
			}
			
			
		}
		
		clientSocket.close();
	}

	public String addMenu() throws IOException {
		String serverAddress = "192.168.56.1";
		int port = 9030;
		Socket clientSocket = new Socket(serverAddress, port);
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

//		String answer = input.readLine();
//		System.out.println(answer);

		out.println("loadMenu");

		String answer = input.readLine();
		System.out.println("connected");

		System.out.println(answer);

		try {
			System.out.println("closing connection..");

			clientSocket.close();
			return answer;
		} catch (Exception e) {

		}
		return answer;
	}

	public void objectRead() throws ClassNotFoundException, IOException {
		String serverAddress = "192.168.56.1";
		int port = 9030;
		Socket clientSocket = new Socket(serverAddress, port);

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		out.println("objectSend");

		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

//		Food food = (Food) ois.readObject();

//		System.out.println("Server said: ID = " + food.getName() + ", and date: " + food.getDescription());

		try {
			clientSocket.close();
		} catch (Exception e) {

		}
	}

	public void populateMenu(String menu, VBox menuList) {
		String[] menuArray = menu.split(",");
		for (int i = 0; i < menuArray.length; i++) {
			Button menuItem = new Button();

			menuItem.setText(menuArray[i]);
			menuList.getChildren().add(menuItem);

			Label itemInfoLabel = new Label();
			menuList.getChildren().add(itemInfoLabel);

			menuItem.setOnAction(value -> {
				try {
					String itemInfo = searchMenuItem(menuItem.getText());
					setLabelText(itemInfoLabel, itemInfo);

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}
	}

	public void setLabelText(Label itemInfoLabel, String itemInfo) {
		String[] itemArray = itemInfo.split(",");
		String labelSet = "";
		for (int i = 0; i < itemArray.length; i++) {
			labelSet = labelSet + itemArray[i] + "\n";

		}
		itemInfoLabel.setText(labelSet);
	}

	public String searchMenuItem(String request) throws UnknownHostException, IOException {
		String foodInfo = "";

		String serverAddress = "192.168.56.1";
		int port = 9030;
		Socket clientSocket = new Socket(serverAddress, port);
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

//		String answer = input.readLine();
//		System.out.println(answer);

		out.println("loadMenuItem");

		out.println(request);

		String answer = input.readLine();
		System.out.println("connected");

		System.out.println(answer);

		try {
			System.out.println("closing connection..");

			clientSocket.close();
			return answer;
		} catch (Exception e) {

		}
		return answer;
	}

	public static void main(String[] args) {
		launch(args);

	}

	public Label getConnectLabel() {
		return connectLabel;
	}

	public void setConnectLabel(Label connectLabel) {
		this.connectLabel = connectLabel;
	}

}
