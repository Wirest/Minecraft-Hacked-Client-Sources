package me.robbanrobbin.jigsaw.client.alts;

import com.google.gson.JsonObject;

public class Alt {

	public String email;
	public String name;
	public String password;
	public boolean cracked;

	public Alt(String email, String password) {
		this.email = email;
		if (password == null || password.isEmpty()) {
			this.name = email;
			this.password = null;
			this.cracked = true;
		} else {
			this.name = /* fix name lol */ email;
			this.password = password;
			this.cracked = false;
		}
	}
	
	public JsonObject saveToJson(JsonObject obj) {
		obj.addProperty("name", name);
		obj.addProperty("password", password);
		obj.addProperty("email", email);
		obj.addProperty("cracked", cracked);
		return obj;
	}

}
