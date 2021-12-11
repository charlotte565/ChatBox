
public class Message {
	private String message;
	private String username;
	private String recipient;
	private boolean sent;
	
	
	
	public Message(String message, String username, String recipient) {
		super();
		this.message = message;
		this.username = username;
		this.recipient = recipient;
		this.sent = false;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
}
