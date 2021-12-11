import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
	private final SimpleStringProperty userName;
//    private final SimpleStringProperty lastName;
    private final SimpleStringProperty ipAddress;
 
    public Client(String username,  String ip) {
        this.userName = new SimpleStringProperty(username);
//        this.lastName = new SimpleStringProperty(lName);
        this.ipAddress = new SimpleStringProperty(ip);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String username) {
        userName.set(username);
    }


    public String getIpAddress() {
        return ipAddress.get();
    }

    public void setIpAddress(String username) {
        ipAddress.set(username);
    }



	@Override
	public String toString() {
		return "Client [firstName=" + userName + ", ipAddress=" + ipAddress + "]";
	}
}
