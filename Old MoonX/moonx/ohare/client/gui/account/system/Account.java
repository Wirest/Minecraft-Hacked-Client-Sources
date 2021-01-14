package moonx.ohare.client.gui.account.system;


import com.google.gson.JsonObject;

/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class Account {
    private String email, password, name;
    private boolean banned;

    public Account(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Account(){}

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getName(){
        return name;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("banned",banned);

        return jsonObject;
    }

    public void fromJson(JsonObject json){
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
        this.name = json.get("name").getAsString();
        this.banned = json.get("banned").getAsBoolean();
    }

}
