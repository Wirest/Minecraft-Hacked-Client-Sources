package modification.commands;

import modification.extenders.Command;
import modification.main.Modification;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class Online
        extends Command {
    public Online(String paramString1, String paramString2) {
        super(paramString1, paramString2);
    }

    public void execute(String paramString, String[] paramArrayOfString) {
        if (MC.getCurrentServerData() != null) {
            String str = String.format("https://cheating-shop.de/eject-irc/index.php?action=count_users&minecraft_server=%s", new Object[]{MC.getCurrentServerData().serverIP});
            try {
                HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                localHttpURLConnection.setRequestProperty("User-Agent", "EJECT-IRC-T5QE7GWJPF");
                localHttpURLConnection.connect();
                Scanner localScanner = new Scanner(localHttpURLConnection.getInputStream());
                Modification.LOG_UTIL.sendChatMessage("Current online on this server: §f".concat(Integer.toString(Integer.parseInt(localScanner.nextLine()) - 1)));
            } catch (IOException localIOException) {
                Modification.LOG_UTIL.sendChatMessage("§4Error: Couldn't connect to online servers");
            }
            return;
        }
        Modification.LOG_UTIL.sendChatMessage("§4Error: You are not on a server");
    }
}




