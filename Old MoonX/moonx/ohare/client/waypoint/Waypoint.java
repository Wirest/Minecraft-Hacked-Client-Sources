package moonx.ohare.client.waypoint;

import com.google.gson.JsonObject;

public class Waypoint {
    private String label, server;
    private double x, y, z;
    private int dimension;

    Waypoint(String label, double x, double y, double z, String server, int dimension) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
        this.server = server;
        this.dimension = dimension;
    }

    public Waypoint() {
    }

    public String getLabel() {
        return this.label;
    }

    public String getServer() {
        return this.server;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getDimension() {
        return dimension;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("label", label);
        jsonObject.addProperty("server", server);
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y",y);
        jsonObject.addProperty("z",z);
        jsonObject.addProperty("dimension",dimension);

        return jsonObject;
    }

    public void fromJson(JsonObject json){
        this.label = json.get("label").getAsString();
        this.server = json.get("server").getAsString();
        this.x = json.get("x").getAsInt();
        this.y = json.get("y").getAsInt();
        this.z = json.get("z").getAsInt();
        this.dimension = json.get("dimension").getAsInt();
    }
}
