// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner>
{
    @Override
    public void renderTileEntityAt(final TileEntityMobSpawner te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5f, (float)y, (float)z + 0.5f);
        renderMob(te.getSpawnerBaseLogic(), x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }
    
    public static void renderMob(final MobSpawnerBaseLogic mobSpawnerLogic, final double posX, final double posY, final double posZ, final float partialTicks) {
        final Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());
        if (entity != null) {
            final float f = 0.4375f;
            GlStateManager.translate(0.0f, 0.4f, 0.0f);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * partialTicks) * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, 0.0f);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
    }
}
