// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.HashMap;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import java.util.Map;

public class PlayerConfigurations
{
    private static Map mapConfigurations;
    
    static {
        PlayerConfigurations.mapConfigurations = null;
    }
    
    public static void renderPlayerItems(final ModelBiped p_renderPlayerItems_0_, final AbstractClientPlayer p_renderPlayerItems_1_, final float p_renderPlayerItems_2_, final float p_renderPlayerItems_3_) {
        final PlayerConfiguration playerconfiguration = getPlayerConfiguration(p_renderPlayerItems_1_);
        if (playerconfiguration != null) {
            playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
        }
    }
    
    public static synchronized PlayerConfiguration getPlayerConfiguration(final AbstractClientPlayer p_getPlayerConfiguration_0_) {
        final String s = p_getPlayerConfiguration_0_.getNameClear();
        if (s == null) {
            return null;
        }
        PlayerConfiguration playerconfiguration = getMapConfigurations().get(s);
        if (playerconfiguration == null) {
            playerconfiguration = new PlayerConfiguration();
            getMapConfigurations().put(s, playerconfiguration);
            final PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s);
            final String s2 = "http://s.optifine.net/users/" + s + ".cfg";
            final FileDownloadThread filedownloadthread = new FileDownloadThread(s2, playerconfigurationreceiver);
            filedownloadthread.start();
        }
        return playerconfiguration;
    }
    
    public static synchronized void setPlayerConfiguration(final String p_setPlayerConfiguration_0_, final PlayerConfiguration p_setPlayerConfiguration_1_) {
        getMapConfigurations().put(p_setPlayerConfiguration_0_, p_setPlayerConfiguration_1_);
    }
    
    private static Map getMapConfigurations() {
        if (PlayerConfigurations.mapConfigurations == null) {
            PlayerConfigurations.mapConfigurations = new HashMap();
        }
        return PlayerConfigurations.mapConfigurations;
    }
}
