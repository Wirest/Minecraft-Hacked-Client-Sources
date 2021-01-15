package net.minecraft.optifine;

import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ReflectorForge
{
    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation loc, String message)
    {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists())
        {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackBrokenTexture, new Object[] {loc, message});
        }
    }

    public static void FMLClientHandler_trackMissingTexture(ResourceLocation loc)
    {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists())
        {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackMissingTexture, new Object[] {loc});
        }
    }

    public static void putLaunchBlackboard(String key, Object value)
    {
        Map blackboard = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);

        if (blackboard != null)
        {
            blackboard.put(key, value);
        }
    }
}
