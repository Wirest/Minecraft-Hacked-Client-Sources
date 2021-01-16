package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class EntityFireworkStarterFX_Factory implements IParticleFactory
{
    private static final String __OBFID = "CL_00002603";

    public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
    {
        EntityFireworkSparkFX var16 = new EntityFireworkSparkFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Minecraft.getMinecraft().effectRenderer);
        var16.setAlphaF(0.99F);
        return var16;
    }
}
