/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventRender;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Trajectories
extends Mod {
    public static float yaw;
    public static float pitch;
    private boolean bow = false;
    private boolean pot = false;

    public Trajectories() {
        super("Trajectories", Mod.Category.RENDER, Colors.BLUE.c);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        yaw = event.yaw;
        pitch = event.pitch;
    }

    @EventTarget
    public void onRender(EventRender event) {
        if (this.mc.thePlayer.getHeldItem() == null) {
            return;
        }
        if (!(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow || this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSnowball || this.mc.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl || this.mc.thePlayer.getHeldItem().getItem() instanceof ItemEgg || this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && ItemPotion.isSplash(this.mc.thePlayer.getHeldItem().getItemDamage()))) {
            return;
        }
        this.bow = this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow;
        this.pot = this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion;
        float throwingYaw = yaw;
        float throwingPitch = pitch;
        double posX = RenderManager.renderPosX - (double)(MathHelper.cos(throwingYaw / 180.0f * 3.1415927f) * 0.16f);
        double posY = RenderManager.renderPosY + (double)this.mc.thePlayer.getEyeHeight() - 0.1000000014901161;
        double posZ = RenderManager.renderPosZ - (double)(MathHelper.sin(throwingYaw / 180.0f * 3.1415927f) * 0.16f);
        double motionX = (double)((- MathHelper.sin(throwingYaw / 180.0f * 3.1415927f)) * MathHelper.cos(throwingPitch / 180.0f * 3.1415927f)) * (this.bow ? 1.0 : 0.4);
        double motionY = (double)(- MathHelper.sin((throwingPitch - (float)(this.pot ? 20 : 0)) / 180.0f * 3.1415927f)) * (this.bow ? 1.0 : 0.4);
        double motionZ = (double)(MathHelper.cos(throwingYaw / 180.0f * 3.1415927f) * MathHelper.cos(throwingPitch / 180.0f * 3.1415927f)) * (this.bow ? 1.0 : 0.4);
        int var = 72000 - this.mc.thePlayer.getItemInUseCount();
        float power = (float)var / 20.0f;
        if ((double)(power = (power * power + power * 2.0f) / 3.0f) < 0.1) {
            return;
        }
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        motionX *= (double)(this.bow ? power * 2.0f : 1.0f) * (this.pot ? 0.5 : 1.5);
        motionY *= (double)(this.bow ? power * 2.0f : 1.0f) * (this.pot ? 0.5 : 1.5);
        motionZ *= (double)(this.bow ? power * 2.0f : 1.0f) * (this.pot ? 0.5 : 1.5);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable((int)3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0f, 0.0f);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glPushMatrix();
        GL11.glColor4f((float)0.20392157f, (float)0.59607846f, (float)0.85882354f, (float)0.5f);
        GL11.glDisable((int)3553);
        GL11.glDepthMask((boolean)false);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)3.0f);
        GL11.glBegin((int)3);
        boolean hasLanded = false;
        Object hitEntity = null;
        MovingObjectPosition landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            Vec3 present = new Vec3(posX, posY, posZ);
            Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition possibleLandingStrip = this.mc.theWorld.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null) {
                if (possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                    landingPosition = possibleLandingStrip;
                    hasLanded = true;
                }
            } else {
                Entity entityHit = this.getEntityHit(this.bow, present, future);
                if (entityHit != null) {
                    landingPosition = new MovingObjectPosition(entityHit);
                    hasLanded = true;
                }
            }
            float motionAdjustment = 0.99f;
            motionY *= (double)motionAdjustment;
            this.mc.getRenderManager();
            this.mc.getRenderManager();
            this.mc.getRenderManager();
            GL11.glVertex3d((double)((posX += (motionX *= (double)motionAdjustment)) - RenderManager.renderPosX), (double)((posY += (motionY -= this.pot ? 0.05 : (this.bow ? 0.05 : 0.03))) - RenderManager.renderPosY), (double)((posZ += (motionZ *= (double)motionAdjustment)) - RenderManager.renderPosZ));
        }
        GL11.glEnd();
        GL11.glPushMatrix();
        this.mc.getRenderManager();
        this.mc.getRenderManager();
        this.mc.getRenderManager();
        GL11.glTranslated((double)(posX - RenderManager.renderPosX), (double)(posY - RenderManager.renderPosY), (double)(posZ - RenderManager.renderPosZ));
        if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int index = landingPosition.sideHit.getIndex();
            if (index == 1) {
                GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            } else if (index == 2) {
                GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            } else if (index == 3) {
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            } else if (index == 4) {
                GL11.glRotatef((float)-90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            } else if (index == 5) {
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glRotatef((float)this.mc.thePlayer.rotationYaw, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            RenderHelper.drawBorderedRect(-0.4f, -0.4f, 0.4f, 0.4f, 0.5f, FlatColors.DARK_BLUE.c, ClientUtil.reAlpha(FlatColors.BLUE.c, 0.5f));
        }
        GL11.glPopMatrix();
        if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            GL11.glTranslated((double)(- RenderManager.renderPosX), (double)(- RenderManager.renderPosY), (double)(- RenderManager.renderPosZ));
            GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.0f, (float)0.8f);
            AxisAlignedBB bb = landingPosition.entityHit.getEntityBoundingBox();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY + 0.1, bb.maxZ));
            GL11.glColor4f((float)0.20392157f, (float)0.59607846f, (float)0.85882354f, (float)1.0f);
            GL11.glLineWidth((float)0.5f);
            GL11.glTranslated((double)RenderManager.renderPosX, (double)RenderManager.renderPosY, (double)RenderManager.renderPosZ);
        }
        GL11.glDisable((int)3042);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)3553);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    private ArrayList getEntities() {
        ArrayList ret = new ArrayList();
        for (Object e : this.mc.theWorld.loadedEntityList) {
            if (e == this.mc.thePlayer || !(e instanceof EntityLivingBase)) continue;
            ret.add(e);
        }
        return ret;
    }

    private Entity getEntityHit(boolean bow, Vec3 vecOrig, Vec3 vecNew) {
        for (Object o : this.getEntities()) {
            EntityLivingBase entity = (EntityLivingBase)o;
            if (entity == this.mc.thePlayer) continue;
            float expander = 0.2f;
            AxisAlignedBB bounding2 = entity.getEntityBoundingBox().expand(expander, expander, expander);
            MovingObjectPosition possibleEntityLanding = bounding2.calculateIntercept(vecOrig, vecNew);
            if (possibleEntityLanding == null) continue;
            return entity;
        }
        return null;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Trajectories Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Trajectories Enable", ClientNotification.Type.SUCCESS);
    }
}

