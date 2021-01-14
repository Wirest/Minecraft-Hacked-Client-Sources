package info.sigmaclient.management.agora.component;

import info.sigmaclient.json.JsonArray;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class AgoraRank {

    private String name;
    private int color;
    private CopyOnWriteArrayList<String> onlineMembers = new CopyOnWriteArrayList<>();

    public AgoraRank(JsonObject data) {
        this.name = data.getString("name", null);
        this.color = data.getInt("color", 0);
        JsonArray membersArray = data.get("members").asArray();
        for (JsonValue val : membersArray) {
            onlineMembers.add(val.asObject().getString("username", null));
        }
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    public CopyOnWriteArrayList<String> getOnlineMembers() {
        return this.onlineMembers;
    }
}
