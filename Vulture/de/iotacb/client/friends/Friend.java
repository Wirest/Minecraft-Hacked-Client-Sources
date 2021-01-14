package de.iotacb.client.friends;

public class Friend {
	
	private String playerName, aliasName;
	
	public Friend(String playerName) {
		this.playerName = playerName;
		this.aliasName = "";
	}
	
	public Friend(String playerName, String aliasName) {
		this.playerName = playerName;
		this.aliasName = aliasName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getAliasName() {
		return aliasName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
}
