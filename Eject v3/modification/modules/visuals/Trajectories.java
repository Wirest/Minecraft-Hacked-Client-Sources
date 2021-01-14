package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventRender3D;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;

public final class Trajectories
        extends Module {
    public Trajectories(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventRender3D)) {
            ItemStack localItemStack = MC.thePlayer.getCurrentEquippedItem();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBow))) {
                int i = MC.thePlayer.getItemInUseDuration();
                float f1 = i / 20.0F;
                f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;
                if (f1 < 0.1D) {
                    return;
                }
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }
                float f2 = f1 * 2.0F;
                float f3 = MC.thePlayer.rotationYawHead;
                float f4 = MC.thePlayer.rotationPitchHead;
                double d1 = MC.getRenderManager().renderPosX - MathHelper.cos(f3 / 180.0F * 3.1415927F) * 0.16F;
                double d2 = MC.getRenderManager().renderPosY + MC.thePlayer.getEyeHeight() - 0.10000000149011612D;
                double d3 = MC.getRenderManager().renderPosZ - MathHelper.sin(f3 / 180.0F * 3.1415927F) * 0.16F;
                double d4 = d1;
                double d5 = d2;
                double d6 = d3;
                double d7 = -MathHelper.sin(f3 / 180.0F * 3.1415927F) * MathHelper.cos(f4 / 180.0F * 3.1415927F);
                double d8 = MathHelper.cos(f3 / 180.0F * 3.1415927F) * MathHelper.cos(f4 / 180.0F * 3.1415927F);
                double d9 = -MathHelper.sin(f4 / 180.0F * 3.1415927F);
                float f5 = MathHelper.sqrt_double(d7 * d7 + d9 * d9 + d8 * d8);
                d7 /= f5;
                d9 /= f5;
                d8 /= f5;
                d7 *= f2;
                d9 *= f2;
                d8 *= f2;
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Modification.RENDER_UTIL.glSetColor(Modification.color.getRGB());
                GL11.glBegin(3);
                int j = 0;
                MovingObjectPosition localMovingObjectPosition1 = null;
                Object localObject1 = null;
                Object localObject2;
                while ((j == 0) && (d2 > 0.0D)) {
                    localObject2 = new Vec3(d1, d2, d3);
                    Vec3 localVec3 = new Vec3(d1 + d7, d2 + d9, d3 + d8);
                    MovingObjectPosition localMovingObjectPosition2 = MC.theWorld.rayTraceBlocks((Vec3) localObject2, localVec3, false, true, false);
                    if (localMovingObjectPosition2 != null) {
                        if (localMovingObjectPosition2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                            localObject1 = localMovingObjectPosition2;
                            j = 1;
                        }
                    } else {
                        Entity localEntity = searchEntity((Vec3) localObject2, localVec3);
                        if (localEntity != null) {
                            localMovingObjectPosition1 = new MovingObjectPosition(localEntity);
                            j = 1;
                        }
                    }
                    d1 += d7;
                    d2 += d9;
                    d3 += d8;
                    float f6 = 0.99F;
                    float f7 = 0.02F;
                    d7 *= f6;
                    d9 *= f6;
                    d8 *= f6;
                    d9 -= f7;
                    GL11.glVertex3d(d1 - MC.getRenderManager().renderPosX, d2 - MC.getRenderManager().renderPosY, d3 - MC.getRenderManager().renderPosZ);
                }
                GL11.glEnd();
                GL11.glPushMatrix();
                if ((localObject1 != null) && (((MovingObjectPosition) localObject1).getBlockPos() != null)) {
                    GL11.glTranslated(((MovingObjectPosition) localObject1).getBlockPos().getX() + 0.5D, ((MovingObjectPosition) localObject1).getBlockPos().getY() + 0.5D, ((MovingObjectPosition) localObject1).getBlockPos().getZ() + 0.5D);
                    localObject2 = new AxisAlignedBB(-0.51D, -0.51D, -0.51D, 0.51D, 0.51D, 0.51D);
                    Modification.RENDER_UTIL.renderAABB((AxisAlignedBB) localObject2, Color.WHITE, false);
                }
                GL11.glPopMatrix();
                if ((localMovingObjectPosition1 != null) && (localMovingObjectPosition1.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)) {
                    localObject2 = localMovingObjectPosition1.entityHit;
                    Modification.RENDER_UTIL.renderAABB(((Entity) localObject2).getEntityBoundingBox().offset(0.0D, 0.1D, 0.0D).expand(0.0D, 0.2D, 0.0D), ColorUtil.MAIN_COLOR_NON_HEX, true);
                }
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }

    protected void onDeactivated() {
    }

    private Entity searchEntity(Vec3 paramVec31, Vec3 paramVec32) {
        Iterator localIterator = MC.theWorld.loadedEntityList.iterator();
        while (localIterator.hasNext()) {
            Entity localEntity = (Entity) localIterator.next();
            if ((localEntity != MC.thePlayer) && ((localEntity instanceof EntityLivingBase))) {
                float f = 0.3F;
                AxisAlignedBB localAxisAlignedBB = localEntity.getEntityBoundingBox().expand(f, f, f);
                MovingObjectPosition localMovingObjectPosition = localAxisAlignedBB.calculateIntercept(paramVec31, paramVec32);
                if (localMovingObjectPosition != null) {
                    return localEntity;
                }
            }
        }
        return null;
    }
}




