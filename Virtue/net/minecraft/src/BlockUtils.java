package net.minecraft.src;

import net.minecraft.block.Block;

public class BlockUtils
{
    private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    private static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(ForgeBlock, "setLightOpacity");
    private static boolean directAccessValid = true;

    public static void setLightOpacity(Block block, int opacity)
    {
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
}
