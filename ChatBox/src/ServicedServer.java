

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ServicedServer extends Service <String>{
	int port;
	ObservableList<Client> ipAddressList;
	private Server server;
	private HashMap<String, User> users;
	
	public ServicedServer (int port, ObservableList<Client> ipAddressList, HashMap<String, User> users)
	{
		setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				// TODO Auto-generated method stub
				
				System.out.println("Ali");
				
			}
		});
		this.port = port;
		this.ipAddressList = ipAddressList;
		this.users = users;
	}

	@Override
	protected Task<String> createTask() {
		// TODO Auto-generated method stub
		
		return new Task <String>()
		{
			@Override
			protected String call() throws Exception {
				//Thread.sleep(10000);
				
				System.out.println("b or a");
				server = new Server(port, ipAddressList, users);
				
				return "nothing";
			}
		};
        
	}

	public Server getServer() {
		return server;
	}
}
