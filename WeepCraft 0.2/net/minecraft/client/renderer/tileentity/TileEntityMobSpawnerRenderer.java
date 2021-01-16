package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner>
{
    public void func_192841_a(TileEntityMobSpawner p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_, (float)p_192841_6_ + 0.5F);
        renderMob(p_192841_1_.getSpawnerBaseLogic(), p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_);
        GlStateManager.popMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks)
    {
        Entity entity = mobSpawnerLogic.getCachedEntity();

        if (entity != null)
        {
            float f = 0.53125F;
            float f1 = Math.max(entity.width, entity.height);

            if ((double)f1 > 1.0D)
            {
                f /= f1;
            }

            GlStateManager.translate(0.0F, 0.4F, 0.0F);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.2F, 0.0F);
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        }
    }
}
