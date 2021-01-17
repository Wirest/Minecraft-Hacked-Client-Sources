package net.minecraft.optifine;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.block.Block;

public class BlockUtils
{
    private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    private static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(ForgeBlock, "setLightOpacity");
    private static boolean directAccessValid = true;
    private static Map mapOriginalOpacity = new IdentityHashMap();

    public static void setLightOpacity(Block block, int opacity)
    {
        if (!mapOriginalOpacity.containsKey(block))
        {
            mapOriginalOpacity.put(block, Integer.valueOf(block.getLightOpacity()));
        }

        if (directAccessValid)
        {
            try
            {
                block.setLightOpacity(opacity);
                return;
            }
            catch (IllegalAccessError var3)
            {
                directAccessValid = false;

                if (!ForgeBlock_setLightOpacity.exists())
                {
                    throw var3;
                }
            }
        }

        Reflector.callVoid(block, ForgeBlock_setLightOpacity, new Object[] {Integer.valueOf(opacity)});
    }

    public static void restoreLightOpacity(Block block)
    {
        if (mapOriginalOpacity.containsKey(block))
        {
            int opacity = ((Integer)mapOriginalOpacity.get(block)).intValue();
            setLightOpacity(block, opacity);
        }
    }
}
