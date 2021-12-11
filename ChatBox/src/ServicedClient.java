import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ServicedClient extends Service <String>{
	int port;
	UIClient uiClient;
	ClientService  clientService;
	String ipAddress;
	
	public ServicedClient (int port, UIClient uiClient, String ipAddress)
	{
		setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				// TODO Auto-generated method stub
				
				System.out.println("Ali");
				
			}
		});
		this.port = port;
		this.uiClient = uiClient;
		this.ipAddress = ipAddress;
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
				clientService = new ClientService(port, uiClient, ipAddress);
				
				return "nothign";
			}
		};
        
	}


}



