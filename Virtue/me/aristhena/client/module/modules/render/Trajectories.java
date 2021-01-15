// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import com.google.common.base.Predicate;
import me.aristhena.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import java.util.ArrayList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import me.aristhena.utils.RenderUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemBow;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.Render3DEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Trajectories extends Module
{
    @EventTarget(4)
    public void onRender(final Render3DEvent event) {
        final double renderPosX = ClientUtils.player().lastTickPosX + (ClientUtils.player().posX - ClientUtils.player().lastTickPosX) * event.getPartialTicks();
        final double renderPosY = ClientUtils.player().lastTickPosY + (ClientUtils.player().posY - ClientUtils.player().lastTickPosY) * event.getPartialTicks();
        final double renderPosZ = ClientUtils.player().lastTickPosZ + (ClientUtils.player().posZ - ClientUtils.player().lastTickPosZ) * event.getPartialTicks();
        if (ClientUtils.player().getHeldItem() == null || ClientUtils.mc().gameSettings.thirdPersonView != 0) {
            return;
        }
        if (!(ClientUtils.player().getHeldItem().getItem() instanceof ItemBow)) {
            return;
        }
        final ItemStack stack = ClientUtils.player().getHeldItem();
        final Item item = ClientUtils.player().getHeldItem().getItem();
        double posX = renderPosX - MathHelper.cos(ClientUtils.player().rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        double posY = renderPosY + ClientUtils.player().getEyeHeight() - 0.1000000014901161;
        double posZ = renderPosZ - MathHelper.sin(ClientUtils.player().rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        double motionX = -MathHelper.sin(ClientUtils.player().rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(ClientUtils.player().rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionY = -MathHelper.sin(ClientUtils.player().rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionZ = MathHelper.cos(ClientUtils.player().rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(ClientUtils.player().rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        final int var6 = 72000 - ClientUtils.player().getItemInUseCount();
        float power = var6 / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power < 0.1) {
            return;
        }
        if (power > 1.0f) {
            power = 1.0f;
        }
        final float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        final float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((ClientUtils.player().getHeldItem().getItem() == Items.experience_bottle) ? 0.9f : 1.0f));
        motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((ClientUtils.player().getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((ClientUtils.player().getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((ClientUtils.player().getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        RenderUtils.enableGL3D(0.4f);
        if (power > 0.6f) {
            GlStateManager.color(0.0f, 1.0f, 0.0f, 1.0f);
        }
        else {
            GlStateManager.color(0.8f, 0.5f, 0.0f, 1.0f);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.startDrawing(3);
        renderer.addVertex(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
        final List<double[]> tm = new ArrayList<double[]>();
        final float size = (float)((item instanceof ItemBow) ? 0.3 : 0.25);
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        MovingObjectPosition landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            final Vec3 present = new Vec3(posX, posY, posZ);
            final Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            final MovingObjectPosition possibleLandingStrip = ClientUtils.mc().theWorld.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }
            final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
            final List entities = this.getEntitiesWithinAABB(arrowBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
            for (final Object entity : entities) {
                final Entity boundingBox = (Entity)entity;
                if (boundingBox.canBeCollidedWith() && boundingBox != ClientUtils.player()) {
                    final float var7 = 0.3f;
                    final AxisAlignedBB var8 = boundingBox.getEntityBoundingBox().expand(var7, var7, var7);
                    final MovingObjectPosition possibleEntityLanding = var8.calculateIntercept(present, future);
                    if (possibleEntityLanding == null) {
                        continue;
                    }
                    hasLanded = true;
                    landingOnEntity = boundingBox;
                    landingPosition = possibleEntityLanding;
                }
            }
            if (landingOnEntity != null) {
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            final float motionAdjustment = 0.99f;
            motionX *= motionAdjustment;
            motionY *= motionAdjustment;
            motionZ *= motionAdjustment;
            motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
            renderer.addVertex(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
        }
        tessellator.draw();
        if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
            final int side = landingPosition.field_178784_b.getIndex();
            if (side == 2) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            }
            else if (side == 3) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            }
            else if (side == 4) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            else if (side == 5) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            final Cylinder c = new Cylinder();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            c.setDrawStyle(100011);
            if (landingOnEntity != null) {
                GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0f);
                GL11.glLineWidth(2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth(0.1f);
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }
            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }
        RenderUtils.disableGL3D();
    }
    
    private List getEntitiesWithinAABB(final AxisAlignedBB bb) {
        final ArrayList list = new ArrayList();
        final int chunkMinX = MathHelper.floor_double((bb.minX - 2.0) / 16.0);
        final int chunkMaxX = MathHelper.floor_double((bb.maxX + 2.0) / 16.0);
        final int chunkMinZ = MathHelper.floor_double((bb.minZ - 2.0) / 16.0);
        final int chunkMaxZ = MathHelper.floor_double((bb.maxZ + 2.0) / 16.0);
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (ClientUtils.mc().theWorld.getChunkProvider().chunkExists(x, z)) {
                    ClientUtils.mc().theWorld.getChunkFromChunkCoords(x, z).func_177414_a(ClientUtils.player(), bb, list, null);
                }
            }
        }
        return list;
    }
}
