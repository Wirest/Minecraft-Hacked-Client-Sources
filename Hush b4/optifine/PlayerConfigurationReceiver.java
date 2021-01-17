// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PlayerConfigurationReceiver implements IFileDownloadListener
{
    private String player;
    
    public PlayerConfigurationReceiver(final String p_i72_1_) {
        this.player = null;
        this.player = p_i72_1_;
    }
    
    @Override
    public void fileDownloadFinished(final String p_fileDownloadFinished_1_, final byte[] p_fileDownloadFinished_2_, final Throwable p_fileDownloadFinished_3_) {
        if (p_fileDownloadFinished_2_ != null) {
            try {
                final String s = new String(p_fileDownloadFinished_2_, "ASCII");
                final JsonParser jsonparser = new JsonParser();
                final JsonElement jsonelement = jsonparser.parse(s);
                final PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
                final PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
                if (playerconfiguration != null) {
                    playerconfiguration.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration(this.player, playerconfiguration);
                }
            }
            catch (Exception exception) {
                Config.dbg("Error parsing configuration: " + p_fileDownloadFinished_1_ + ", " + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }
}
