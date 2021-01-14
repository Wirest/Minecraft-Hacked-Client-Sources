package info.sigmaclient.management.agora;

import info.sigmaclient.json.Json;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class AgoraWebsocketClient extends WebSocketClient {
    private Agora agora;

    public AgoraWebsocketClient(Agora agora, String token, HashMap<String, String> headers) throws URISyntaxException {
        super(new URI("wss://wsprg.sigmaclient.info/ws/agora/" + token), headers);
        this.agora = agora;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
    }

    @Override
    public void onMessage(String message) {
        agora.handleMessage(Json.parse(message).asObject());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Agora.onConnectionLost();
    }

    @Override
    public void onError(Exception ex) {
        Agora.onConnectionLost();
    }
}
