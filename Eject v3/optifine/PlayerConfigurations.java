package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

import java.util.HashMap;
import java.util.Map;

public class PlayerConfigurations {
    private static Map mapConfigurations = null;

    public static void renderPlayerItems(ModelBiped paramModelBiped, AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2) {
        PlayerConfiguration localPlayerConfiguration = getPlayerConfiguration(paramAbstractClientPlayer);
        if (localPlayerConfiguration != null) {
            localPlayerConfiguration.renderPlayerItems(paramModelBiped, paramAbstractClientPlayer, paramFloat1, paramFloat2);
        }
    }

    public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer paramAbstractClientPlayer) {
        String str1 = paramAbstractClientPlayer.getNameClear();
        if (str1 == null) {
            return null;
        }
        PlayerConfiguration localPlayerConfiguration = (PlayerConfiguration) getMapConfigurations().get(str1);
        if (localPlayerConfiguration == null) {
            localPlayerConfiguration = new PlayerConfiguration();
            getMapConfigurations().put(str1, localPlayerConfiguration);
            PlayerConfigurationReceiver localPlayerConfigurationReceiver = new PlayerConfigurationReceiver(str1);
            String str2 = "http://s.optifine.net/users/" + str1 + ".cfg";
            FileDownloadThread localFileDownloadThread = new FileDownloadThread(str2, localPlayerConfigurationReceiver);
            localFileDownloadThread.start();
        }
        return localPlayerConfiguration;
    }

    public static synchronized void setPlayerConfiguration(String paramString, PlayerConfiguration paramPlayerConfiguration) {
        getMapConfigurations().put(paramString, paramPlayerConfiguration);
    }

    private static Map getMapConfigurations() {
        if (mapConfigurations == null) {
            mapConfigurations = new HashMap();
        }
        return mapConfigurations;
    }
}




