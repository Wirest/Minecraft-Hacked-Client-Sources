package optifine;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PlayerConfigurationReceiver
        implements IFileDownloadListener {
    private String player = null;

    public PlayerConfigurationReceiver(String paramString) {
        this.player = paramString;
    }

    public void fileDownloadFinished(String paramString, byte[] paramArrayOfByte, Throwable paramThrowable) {
        if (paramArrayOfByte != null) {
            try {
                String str = new String(paramArrayOfByte, "ASCII");
                JsonParser localJsonParser = new JsonParser();
                JsonElement localJsonElement = localJsonParser.parse(str);
                PlayerConfigurationParser localPlayerConfigurationParser = new PlayerConfigurationParser(this.player);
                PlayerConfiguration localPlayerConfiguration = localPlayerConfigurationParser.parsePlayerConfiguration(localJsonElement);
                if (localPlayerConfiguration != null) {
                    localPlayerConfiguration.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration(this.player, localPlayerConfiguration);
                }
            } catch (Exception localException) {
                Config.dbg("Error parsing configuration: " + paramString + ", " + localException.getClass().getName() + ": " + localException.getMessage());
            }
        }
    }
}




