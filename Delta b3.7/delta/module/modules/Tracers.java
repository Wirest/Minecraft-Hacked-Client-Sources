/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender3D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package delta.module.modules;

import delta.Class108;
import java.awt.Color;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender3D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    private boolean viewBobbing;

    public void onEnable() {
        this.viewBobbing = this.mc.gameSettings.viewBobbing;
        super.onEnable();
    }

    public void onDisable() {
        this.mc.gameSettings.viewBobbing = this.viewBobbing;
        super.onDisable();
    }

    private void drawTracer(Entity entity, Color color, float f) {
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f - RenderManager.renderPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f - RenderManager.renderPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f - RenderManager.renderPosZ;
        Class108 class108 = new Class108(0.0, 0.0, 1.0)._helen((float)(-Math.toRadians(this.mc.thePlayer.rotationPitch)))._eleven((float)(-Math.toRadians(this.mc.thePlayer.rotationYaw)));
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)((double)color.getRed() / 255.0), (double)((double)color.getGreen() / 255.0), (double)((double)color.getBlue() / 255.0), (double)((double)color.getAlpha() / 255.0));
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)class108.premier$, (double)class108.hebrew$, (double)class108.resume$);
        GL11.glVertex3d((double)d, (double)(d2 + (double)entity.getEyeHeight()), (double)d3);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public boolean isValidEntity(EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityClientPlayerMP) {
            return false;
        }
        if (!this.getSetting("Players").getCheckValue() && entityLivingBase instanceof EntityPlayer) {
            return false;
        }
        if (!this.getSetting("Monsters").getCheckValue() && (entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntitySlime)) {
            return false;
        }
        if (!this.getSetting("Animals").getCheckValue() && (entityLivingBase instanceof EntityAnimal || entityLivingBase instanceof EntitySquid || entityLivingBase instanceof EntityBat)) {
            return false;
        }
        return this.getSetting("Invisibles").getCheckValue() || !entityLivingBase.isInvisible();
    }

    public Tracers() {
        super("Tracers", Category.Render);
        this.setDescription("Trace des traits vers les autres joueurs");
        this.addSetting(new Setting("Players", (IModule)this, true));
        this.addSetting(new Setting("Monsters", (IModule)this, true));
        this.addSetting(new Setting("Animals", (IModule)this, true));
        this.addSetting(new Setting("Invisibles", (IModule)this, true));
    }

    @EventTarget
    public void onRender3D(EventRender3D eventRender3D) {
        this.mc.gameSettings.viewBobbing = false;
        for (Object e : this.mc.theWorld.loadedEntityList) {
            EntityLivingBase entityLivingBase;
            if (!(e instanceof EntityLivingBase) || !this.isValidEntity(entityLivingBase = (EntityLivingBase)e)) continue;
            int n = (int)(this.mc.thePlayer.getDistanceToEntity((Entity)entityLivingBase) * 2.0f);
            if (n > 255) {
                n = 255;
            }
            Color color = new Color(255 - n, n, 0, 150);
            this.drawTracer((Entity)entityLivingBase, color, eventRender3D.getPartialTicks());
        }
    }
}

