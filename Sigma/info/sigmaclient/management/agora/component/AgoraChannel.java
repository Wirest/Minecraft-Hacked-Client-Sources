package info.sigmaclient.management.agora.component;

import info.sigmaclient.json.JsonArray;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AgoraChannel {
    private String name;
    private Queue<AgoraChatMessage> messages = new ConcurrentLinkedQueue<>();
    private boolean canTalk;

    public AgoraChannel(JsonObject data) {
        name = data.getString("name", null);
        canTalk = data.getBoolean("canTalk", false);

        JsonArray messagesArray = data.get("messages").asArray();
        for (JsonValue val : messagesArray.values()) {
            messages.add(new AgoraChatMessage(val.asObject()));
        }
    }

    public void addMessage(AgoraChatMessage message) {
        messages.add(message);
        if (messages.size() > 100) {
            messages.poll();
        }
    }

    public String getName() {
        return name;
    }

    public Queue<AgoraChatMessage> getMessages() {
        return messages;
    }

    public boolean canTalk() {
        return canTalk;
    }
}
