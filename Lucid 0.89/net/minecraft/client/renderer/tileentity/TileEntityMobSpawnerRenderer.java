package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer
{

    /**
     * Render the rotating mob inside the spawner
     */
    public void renderMobSpawnerTileEntity(TileEntityMobSpawner mobSpawnerTileEntity, double posX, double posY, double posZ, float partialTicks, int p_180539_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX + 0.5F, (float)posY, (float)posZ + 0.5F);
        renderMob(mobSpawnerTileEntity.getSpawnerBaseLogic(), posX, posY, posZ, partialTicks);
        GlStateManager.popMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks)
    {
        Entity var8 = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());

        if (var8 != null)
        {
            float var9 = 0.4375F;
            GlStateManager.translate(0.0F, 0.4F, 0.0F);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.4F, 0.0F);
            GlStateManager.scale(var9, var9, var9);
            var8.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(var8, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }

    @Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderMobSpawnerTileEntity((TileEntityMobSpawner)te, x, y, z, partialTicks, destroyStage);
    }
}
