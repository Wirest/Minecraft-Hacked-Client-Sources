
package me.xatzdevelopments.xatz.alts;

import com.google.gson.JsonObject;

public final class Alt {
    private String mask = "";
    private String password;
    public String email;
	public String name;
	public boolean cracked;

    public Alt(String username, String password) {
        this(username, password, "");
    }

    public Alt(String name, String password, String mask) {
        this.name = name;
        this.password = password;
        this.mask = mask;
    }

    public String getMask() {
        return this.mask;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.name;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public JsonObject saveToJson(JsonObject obj) {
		obj.addProperty("name", name);
		obj.addProperty("password", password);
		return obj;
	}
}

