package Model;

import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String password;
	public final String PRODUCT_OWNER = "PO";
	public final String DEVLOPER = "DEV";
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPRODUCT_OWNER() {
		return PRODUCT_OWNER;
	}
	public String getDEVLOPER() {
		return DEVLOPER;
	}
	
	
}
