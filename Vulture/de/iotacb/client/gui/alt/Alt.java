package de.iotacb.client.gui.alt;

public class Alt {
	
	private String email, password;
	
	public Alt(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Alt setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public Alt setPassword(String password) {
		this.password = password;
		return this;
	}
}
