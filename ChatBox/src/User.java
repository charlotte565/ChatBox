import java.net.Socket;
import java.util.ArrayList;

public class User {
	private Client client;
	private String username;
	private Socket socket;
	private ArrayList<Message> messages;
	private boolean unreadMessages;
	private int undreadMessageNum;
	
	public User(String username) {
		this.username = username;
		this.unreadMessages = false;
		this.undreadMessageNum = 0;
		this.messages = new ArrayList<Message>();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public ArrayList<Message> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
	public boolean isUnreadMessages() {
		return unreadMessages;
	}
	public void setUnreadMessages(boolean unreadMessages) {
		this.unreadMessages = unreadMessages;
	}
	public int getUndreadMessageNum() {
		return undreadMessageNum;
	}
	public void setUndreadMessageNum(int undreadMessageNum) {
		this.undreadMessageNum = undreadMessageNum;
	}
}
