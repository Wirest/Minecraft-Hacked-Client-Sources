package info.sigmaclient.management.agora;

import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;
import info.sigmaclient.management.agora.component.AgoraChannel;
import info.sigmaclient.management.agora.component.AgoraChatMessage;
import info.sigmaclient.management.agora.component.AgoraRank;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Agora {
    private static Agora instance;
    private static String token;
    private ConcurrentHashMap<String, AgoraRank> ranksFromName = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<AgoraRank> ranks = new CopyOnWriteArrayList<>();
    private ConcurrentHashMap<String, AgoraChannel> channelsFromName = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<AgoraChannel> channels = new CopyOnWriteArrayList<>();
    private static Thread rebootThread = null;

    private AgoraWebsocketClient websocketClient;

    public static Agora getInstance() {
        if (instance == null) {
            new Agora("0000000000000000000000000000000000000000000000000000000000000000");
        }
        return instance;
    }

    public Agora(String token2) {
        token = token2;
        instance = this;
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Cookie", "agoratk=00");
            headers.put("X-Forwarded-For", "1.1.1.1");
            headers.put("User-Agent", "Agora client");
            headers.put("Referer", "https://agora.sigmaclient.info");
            websocketClient = new AgoraWebsocketClient(this, token, headers);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(AgoraChannel channel, String msg) {
        JsonObject data = new JsonObject();
        data.set("type", "message");
        data.set("channel", channel.getName());
        data.set("msg", msg);
        websocketClient.send(data.toString());
    }

    //Depuis le jeu même les admins ont uniquement la perm de supprimer leurs messages
    public void deleteMessage(String messageUsername, long messageTimestamp) {
        JsonObject data = new JsonObject();
        data.set("type", "deleteMessage");
        data.set("username", messageUsername);
        data.set("timestamp", messageTimestamp);
        websocketClient.send(data.toString());
    }

    public ConcurrentHashMap<String, AgoraRank> getRanksFromName() {
        return ranksFromName;
    }

    public CopyOnWriteArrayList<AgoraRank> getRanks() {
        return ranks;
    }

    public ConcurrentHashMap<String, AgoraChannel> getChannelsFromName() {
        return channelsFromName;
    }

    public CopyOnWriteArrayList<AgoraChannel> getChannels() {
        return channels;
    }

    public void handleMessage(JsonObject data) {
        switch (data.getString("type", null)) {
            case "channelsAndRanks": {
                ranks.clear();
                ranksFromName.clear();
                channels.clear();
                channelsFromName.clear();
                for (JsonValue val : data.get("ranks").asArray()) {
                    AgoraRank rank = new AgoraRank(val.asObject());
                    ranks.add(rank);
                    ranksFromName.put(rank.getName(), rank);
                }
                for (JsonValue val : data.get("channels").asArray()) {
                    AgoraChannel channel = new AgoraChannel(val.asObject());
                    channels.add(channel);
                    channelsFromName.put(channel.getName(), channel);
                }
                break;
            }
            case "newMessage": {
                String channelName = data.getString("channel", null);
                AgoraChannel channel = channelsFromName.get(channelName);
                channel.addMessage(new AgoraChatMessage(data.get("message").asObject()));
                break;
            }
            case "deleteMessage": {
                String username = data.getString("username", null);
                long timestamp = data.getLong("timestamp", 0);
                for (AgoraChannel agoraChannel : channels) {
                    agoraChannel.getMessages().removeIf(c -> c.getUsername().equals(username) && c.getTimestamp() == timestamp);
                }
                break;
            }
            case "userConnected": {
                String rankName = data.getString("rank", null);
                JsonObject userJson = data.get("user").asObject();
                String username = userJson.getString("username", null);
                AgoraRank rank = ranksFromName.get(rankName);
                if (rank != null) {
                    rank.getOnlineMembers().add(username);
                }
                break;
            }
            case "userDisconnected": {
                String rankName = data.getString("rank", null);
                JsonObject userJson = data.get("user").asObject();
                String username = userJson.getString("username", null);
                AgoraRank rank = ranksFromName.get(rankName);
                if (rank != null) {
                    rank.getOnlineMembers().remove(username);
                }
                break;
            }
        }
    }

    public static synchronized void onConnectionLost() {
        if (rebootThread == null) {
            rebootThread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                instance = new Agora(token);
                rebootThread = null;
            });
            rebootThread.start();
        }
    }
}
