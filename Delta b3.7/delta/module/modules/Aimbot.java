/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 */
package delta.module.modules;

import delta.Class175;
import delta.Class202;
import delta.Class6;
import delta.Class8;
import delta.Class9;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Aimbot
extends Module {
    private Class202 tissue$ = new Class202(70.0f, 125.0f, 20.0f, 65.0f);

    public Aimbot() {
        super("Aimbot", Category.Combat);
        this.setDescription("Aide \u00e0 viser les enemis");
        this.addSetting(new Setting("Smooth Aim", (IModule)this, false));
        this.addSetting(new Setting("X Smoothing", (IModule)this, 1.0, 1.0, 10.0, true));
        this.addSetting(new Setting("Y Smoothing", (IModule)this, 1.0, 1.0, 10.0, true));
    }

    @EventTarget
    public void Hv2T(EventMotion eventMotion) {
        if (eventMotion.getType() == Event.State.POST) {
            return;
        }
        EntityLivingBase entityLivingBase = Class175._surname();
        if (!Class175._suppose(entityLivingBase)) {
            return;
        }
        if (!Class175._heather(entityLivingBase, 6.0)) {
            return;
        }
        if (this.getSetting("Smooth Aim").getCheckValue()) {
            Class6<Double> class6 = new Class6<Double>(entityLivingBase.boundingBox.minX + (entityLivingBase.boundingBox.maxX - entityLivingBase.boundingBox.minX) / 2.0, entityLivingBase.boundingBox.minY + (double)entityLivingBase.getEyeHeight(), entityLivingBase.boundingBox.minZ + (entityLivingBase.boundingBox.maxZ - entityLivingBase.boundingBox.minZ) / 2.0);
            Class6<Double> class62 = new Class6<Double>(this.mc.thePlayer.boundingBox.minX + (this.mc.thePlayer.boundingBox.maxX - this.mc.thePlayer.boundingBox.minX) / 2.0, this.mc.thePlayer.posY, this.mc.thePlayer.boundingBox.minZ + (this.mc.thePlayer.boundingBox.maxZ - this.mc.thePlayer.boundingBox.minZ) / 2.0);
            Class9 class9 = this.tissue$._acids(class6, class62);
            Class9 class92 = new Class9(Float.valueOf(this.mc.thePlayer.prevRotationYaw), Float.valueOf(this.mc.thePlayer.prevRotationPitch));
            Class9 class93 = this.tissue$._draws(class9, class92, (float)this.getSetting("X Smoothing").getSliderValue(), (float)this.getSetting("Y Smoothing").getSliderValue());
            eventMotion.setYaw(this.mc.thePlayer.prevRotationYaw + MathHelper.wrapAngleTo180_float((float)(class93._hidden().floatValue() - Minecraft.getMinecraft().thePlayer.prevRotationYaw)));
            eventMotion.setPitch(class93._border().floatValue());
            eventMotion.setSilent(false);
        } else {
            float[] arrf = Class8._brakes(entityLivingBase);
            eventMotion.setYaw(this.mc.thePlayer.prevRotationYaw + MathHelper.wrapAngleTo180_float((float)(arrf[0] - Minecraft.getMinecraft().thePlayer.prevRotationYaw)));
            eventMotion.setPitch(arrf[1]);
            eventMotion.setSilent(false);
        }
    }
}

