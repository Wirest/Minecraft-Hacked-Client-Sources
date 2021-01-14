package info.sigmaclient.management.agora.component;


import info.sigmaclient.json.JsonObject;
import info.sigmaclient.management.agora.Agora;
import info.sigmaclient.util.Timer;

public class AgoraChatMessage {

    private String username, message;
    private long timestamp;
    private AgoraRank rank;
    private Timer timer = new Timer();
    public AgoraChatMessage(JsonObject data) {
    	timer.reset();
        username = data.getString("username", null);
        message = data.getString("message", null);
        timestamp = data.getLong("timestamp", 0);
        rank = Agora.getInstance().getRanksFromName().get(data.getString("rank", null));
       
    }
    public long getAge(){
    	return this.timer.getDifference();
    }
    public String getUsername() {
        return this.username;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message.replace('§', '&');
    }

    public AgoraRank getRank() {
        return this.rank;
    }
}
