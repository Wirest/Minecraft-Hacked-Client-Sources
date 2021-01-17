package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.helpers.RenderHelper;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.utils.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.*;

public class Trajectories extends Module implements RenderHelper, Runnable
{
    public static boolean state;
    public static float alpha;
    
    public Trajectories() {
        this.setKey("");
        this.setName("Trajectories");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(4)
    public void setupViewBobbing(final EventBobWorld event) {
        if (this.isToggled()) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            boolean bow = false;
            boolean potion = false;
            if (Trajectories.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            if ((!(Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) || !Trajectories.mc.thePlayer.isUsingItem()) && !(Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemSnowball) && !(Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl) && !(Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemEgg) && (!(Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) || !ItemPotion.isSplash(Trajectories.mc.thePlayer.getHeldItem().getItemDamage()))) {
                return;
            }
            bow = (Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow);
            potion = (Trajectories.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
            final float throwingYaw = Trajectories.mc.thePlayer.rotationYaw;
            final float throwingPitch = Trajectories.mc.thePlayer.rotationPitch;
            Trajectories.mc.getRenderManager();
            double posX = RenderManager.renderPosX - MathHelper.cos(throwingYaw / 180.0f * 3.141593f) * 0.16f;
            Trajectories.mc.getRenderManager();
            double posY = RenderManager.renderPosY + Trajectories.mc.thePlayer.getEyeHeight() - 0.1000000014901161;
            Trajectories.mc.getRenderManager();
            double posZ = RenderManager.renderPosZ - MathHelper.sin(throwingYaw / 180.0f * 3.141593f) * 0.16f;
            double motionX = -MathHelper.sin(throwingYaw / 180.0f * 3.141593f) * MathHelper.cos(throwingPitch / 180.0f * 3.141593f) * (bow ? 1.0 : 0.4);
            double motionY = -MathHelper.sin((throwingPitch - (potion ? 20 : 0)) / 180.0f * 3.141593f) * (bow ? 1.0 : 0.4);
            double motionZ = MathHelper.cos(throwingYaw / 180.0f * 3.141593f) * MathHelper.cos(throwingPitch / 180.0f * 3.141593f) * (bow ? 1.0 : 0.4);
            final int var6 = 72000 - Trajectories.mc.thePlayer.getItemInUseCount();
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
            motionX *= (bow ? (power * 2.0f) : 1.0f) * (potion ? 0.5 : 1.5);
            motionY *= (bow ? (power * 2.0f) : 1.0f) * (potion ? 0.5 : 1.5);
            motionZ *= (bow ? (power * 2.0f) : 1.0f) * (potion ? 0.5 : 1.5);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnable(3553);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0f, 0.0f);
            GL11.glDisable(2896);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glPushMatrix();
            GL11.glColor4f(0.5f, 1.0f, 1.0f, Trajectories.alpha);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            boolean hasLanded = false;
            final Entity hitEntity = null;
            MovingObjectPosition landingPosition = null;
            while (!hasLanded && posY > 0.0) {
                final Vec3 present = new Vec3(posX, posY, posZ);
                final Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                final MovingObjectPosition possibleLandingStrip = Trajectories.mc.theWorld.rayTraceBlocks(present, future, false, true, false);
                if (possibleLandingStrip != null) {
                    if (possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                        landingPosition = possibleLandingStrip;
                        hasLanded = true;
                    }
                }
                else {
                    final Entity entityHit = this.getEntityHit(bow, present, future);
                    if (entityHit != null) {
                        landingPosition = new MovingObjectPosition(entityHit);
                        hasLanded = true;
                    }
                }
                posX += motionX;
                posY += motionY;
                posZ += motionZ;
                final float motionAdjustment = 0.99f;
                motionX *= motionAdjustment;
                motionY *= motionAdjustment;
                motionZ *= motionAdjustment;
                motionY -= (potion ? 0.05 : (bow ? 0.05 : 0.03));
                final double n = posX;
                Trajectories.mc.getRenderManager();
                final double n2 = n - RenderManager.renderPosX;
                final double n3 = posY;
                Trajectories.mc.getRenderManager();
                final double n4 = n3 - RenderManager.renderPosY;
                final double n5 = posZ;
                Trajectories.mc.getRenderManager();
                GL11.glVertex3d(n2, n4, n5 - RenderManager.renderPosZ);
            }
            GL11.glEnd();
            GL11.glPushMatrix();
            final double n6 = posX;
            Trajectories.mc.getRenderManager();
            final double n7 = n6 - RenderManager.renderPosX;
            final double n8 = posY;
            Trajectories.mc.getRenderManager();
            final double n9 = n8 - RenderManager.renderPosY;
            final double n10 = posZ;
            Trajectories.mc.getRenderManager();
            GL11.glTranslated(n7, n9, n10 - RenderManager.renderPosZ);
            if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final int land = landingPosition.field_178784_b.getIndex();
                if (land == 1) {
                    GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                }
                else if (land == 2) {
                    GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                }
                else if (land == 3) {
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                else if (land == 4) {
                    GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                else if (land == 5) {
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glScalef(0.05f, 0.05f, 0.05f);
                Trajectories.guiUtils.drawOutlineRect(-8.25f, -8.25f, 8.25f, 8.25f, -15636856);
                final GuiUtils guiUtils = Trajectories.guiUtils;
                GuiUtils.drawGradientRect(-8.25, -8.25, 8.25, 8.25, 1712416375, 1712416392);
            }
            GL11.glPopMatrix();
            if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                Trajectories.mc.getRenderManager();
                final double n11 = -RenderManager.renderPosX;
                Trajectories.mc.getRenderManager();
                final double n12 = -RenderManager.renderPosY;
                Trajectories.mc.getRenderManager();
                GL11.glTranslated(n11, n12, -RenderManager.renderPosZ);
                Trajectories.guiUtils.drawOutlineForEntity(null, landingPosition.entityHit.getEntityBoundingBox().expand(0.035, 0.0, 0.035).offset(0.0, 0.1, 0.0), 1.0f, 0.1f, 0.6f, 0.8f, Trajectories.alpha);
                Trajectories.mc.getRenderManager();
                final double renderPosX = RenderManager.renderPosX;
                Trajectories.mc.getRenderManager();
                final double renderPosY = RenderManager.renderPosY;
                Trajectories.mc.getRenderManager();
                GL11.glTranslated(renderPosX, renderPosY, RenderManager.renderPosZ);
            }
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
        }
    }
    
    public ArrayList getEntities() {
        final ArrayList ret = new ArrayList();
        for (final Object e : Trajectories.mc.theWorld.loadedEntityList) {
            if (e != Trajectories.mc.thePlayer && e instanceof EntityLivingBase) {
                ret.add(e);
            }
        }
        return ret;
    }
    
    public Entity getEntityHit(final boolean bow, final Vec3 vecOrig, final Vec3 vecNew) {
        for (final Object o : this.getEntities()) {
            final EntityLivingBase entity = (EntityLivingBase)o;
            if (entity != Trajectories.mc.thePlayer) {
                final float expander = 0.2f;
                final AxisAlignedBB bounding2 = entity.getEntityBoundingBox().expand(expander, expander, expander);
                final MovingObjectPosition possibleEntityLanding = bounding2.calculateIntercept(vecOrig, vecNew);
                if (possibleEntityLanding != null) {
                    return entity;
                }
                continue;
            }
        }
        return null;
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        Trajectories.state = true;
        new Thread(new Trajectories()).start();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Trajectories.state = false;
        new Thread(new Trajectories()).start();
    }
    
    @Override
    public void run() {
        if (Trajectories.state) {
            while (Trajectories.alpha < 0.9f) {
                if (!Client.getModules().isModToggled("Trajectories")) {
                    break;
                }
                Trajectories.alpha += 0.01f;
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Trajectories.alpha = 0.0f;
        }
    }
}
