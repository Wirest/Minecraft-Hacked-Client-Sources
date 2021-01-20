package optifine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfigurations
{
    private static Map mapConfigurations = null;

    public static void renderPlayerItems(ModelBiped p_renderPlayerItems_0_, AbstractClientPlayer p_renderPlayerItems_1_, float p_renderPlayerItems_2_, float p_renderPlayerItems_3_)
    {
        PlayerConfiguration playerconfiguration = getPlayerConfiguration(p_renderPlayerItems_1_);

        if (playerconfiguration != null)
        {
            playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
        }
    }

    public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer p_getPlayerConfiguration_0_)
    {
        String s = p_getPlayerConfiguration_0_.getNameClear();

        if (s == null)
        {
            return null;
        }
        else
        {
            PlayerConfiguration playerconfiguration = (PlayerConfiguration)getMapConfigurations().get(s);

            if (playerconfiguration == null)
            {
                playerconfiguration = new PlayerConfiguration();
                getMapConfigurations().put(s, playerconfiguration);
                PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s);
                String s1 = "http://s.optifine.net/users/" + s + ".cfg";
                FileDownloadThread filedownloadthread = new FileDownloadThread(s1, playerconfigurationreceiver);
                filedownloadthread.start();
            }

            return playerconfiguration;
        }
    }

    public static synchronized void setPlayerConfiguration(String p_setPlayerConfiguration_0_, PlayerConfiguration p_setPlayerConfiguration_1_)
    {
        getMapConfigurations().put(p_setPlayerConfiguration_0_, p_setPlayerConfiguration_1_);
    }

    private static Map getMapConfigurations()
    {
        if (mapConfigurations == null)
        {
            mapConfigurations = new HashMap();
        }

        return mapConfigurations;
    }
}
