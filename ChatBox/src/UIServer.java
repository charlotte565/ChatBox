import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class UIServer extends Application {
	
	

	private TableView<Client> tableConnected = new TableView<Client>();
	final ObservableList<Client> ipAddressList = FXCollections.observableArrayList(
		    
			);
	final HashMap<String, User> users = new HashMap<String, User>();
	

	private ServicedServer servicedServer;
	Server s;

	public void start(Stage primaryStage) throws Exception {

		// port and ip display

		TextField portInput = new TextField();
		portInput.setText("9030");
		Label portLabel = new Label();
		portLabel.setText("Enter Port #:");
		
		HBox portHBox = new HBox(10);
		portHBox.getChildren().addAll(portLabel, portInput);

		Label ipAddressLabel = new Label();
		ipAddressLabel.setText("IP Address: ....192.168.56.1");

		// button set up

		Button serverButton = new Button();
		serverButton.setText("Start Server");

		Button serverOffButton = new Button();
		serverOffButton.setText("Stop Server");
		
		Button test = new Button();
		test.setText("test");

		// Hbox button set up
		HBox buttonsBox = new HBox(10);
		buttonsBox.getChildren().addAll(serverButton, serverOffButton);
		
		//feebacklabel
		Label feedback = new Label();
		feedback.setText("Enter Port to connect");

		// table set up
//		tableConnected.setEditable(true);
//
		Label tableLabel = new Label();
		tableLabel.setText("Connected Clients");

		tableConnected.setEditable(true);
		 
        TableColumn usernameCol = new TableColumn("Username");
        usernameCol.setMinWidth(100);
        usernameCol.setCellValueFactory(
                new PropertyValueFactory<Client, String>("userName"));
 
//        TableColumn lastNameCol = new TableColumn("Last Name");
//        lastNameCol.setMinWidth(100);
//        lastNameCol.setCellValueFactory(
//                new PropertyValueFactory<Client, String>("lastName"));
 
        TableColumn emailCol = new TableColumn("IP Address");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Client, String>("ipAddress"));
 
        tableConnected.setItems(ipAddressList);
        tableConnected.getColumns().addAll(usernameCol,  emailCol);

		
	

		// border pane set up
		VBox bigBox = new VBox(10);
		bigBox.getChildren().addAll(portHBox, ipAddressLabel, buttonsBox, feedback, tableLabel, tableConnected);
		
		test.setOnAction(value -> {
			ipAddressList.add(new Client("username",  "ip"));
			
		});
		

		// button action set up
		serverButton.setOnAction(value -> {
			if(portInput.getText().equals("")) {
				feedback.setText("Invalid port entered");
			}
			else {
			System.out.println("Server started");
			System.out.println( Integer.valueOf(portInput.getText()));
			
			try {
				
				
				String port = portInput.getText();
				int portGo = Integer.valueOf(port);
				
				System.out.println(portGo);

				servicedServer = new ServicedServer(portGo, ipAddressList, users);
				s = servicedServer.getServer();
				
				servicedServer.start();
				
				
				feedback.setText("Connected to port: " + portGo);
				
			} catch (Exception e) {
				feedback.setText("Could not connect to port: " + portInput.getText());
			}
			}
		});
		
		serverOffButton.setOnAction(value -> {
			//System.out.println("Server stopped");
			try {
				// new ThreadedDateServer();

				// Thread.sleep(10000);

				System.out.println("Crazy2");

				//Server s = servicedServer.getServer();
				s.myStop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		intialiseUsers();

		// scene set up
		primaryStage.setScene(new Scene(bigBox, 300, 300));
		primaryStage.sizeToScene();
//		primaryStage.getIcons().add(imageAvo);
		primaryStage.setTitle("Chat Box Severside");
		primaryStage.show();

	}
	
	public void intialiseUsers() {
		users.put("nick123", new User("nick123"));
		users.put("chris505", new User("chris505"));
		users.put("daniel1995", new User("daniel1995"));
	}

	
	public static void main(String[] args) {
		launch(args);

	}
}
