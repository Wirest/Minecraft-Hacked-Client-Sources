package optifine;

import java.lang.reflect.Field;
import java.util.Map;

public class LaunchUtils
{
    private static Boolean forgeServer = null;

    public static boolean isForgeServer()
    {
        if (forgeServer == null)
        {
            try
            {
                Class oclass = Class.forName("net.minecraft.launchwrapper.Launch");
                Field field = oclass.getField("blackboard");
                Map<String, Object> map = (Map)field.get((Object)null);
                Map<String, String> map1 = (Map)map.get("launchArgs");
                String s = (String)map1.get("--accessToken");
                String s1 = (String)map1.get("--version");
                boolean flag = s == null && Utils.equals(s1, "UnknownFMLProfile");
                forgeServer = Boolean.valueOf(flag);
            }
            catch (Throwable throwable)
            {
                System.out.println("Error checking Forge server: " + throwable.getClass().getName() + ": " + throwable.getMessage());
                forgeServer = Boolean.FALSE;
            }
        }

        return forgeServer.booleanValue();
    }
}
