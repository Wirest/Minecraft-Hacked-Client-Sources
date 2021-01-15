// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;
import net.minecraft.entity.EntityLivingBase;
import me.CheerioFX.FusionX.utils.GuiUtils;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Tracers extends Module
{
    boolean oldBobbingSetting;
    
    public Tracers() {
        super("Tracer", 0, Category.RENDER);
    }
    
    @Override
    public void onRender() {
        if (GuiUtils.isInGui()) {
            return;
        }
        if (!this.getState()) {
            return;
        }
        for (final Object theObject : Tracers.mc.theWorld.loadedEntityList) {
            if (!(theObject instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)theObject;
            if (Targets.antibot()) {
                if (!entity.isEntityAlive() || entity.ticksExisted <= 100 || entity.isInvisible()) {
                    return;
                }
                if (entity.posY - Wrapper.mc.thePlayer.posY == 3.609375) {
                    return;
                }
            }
            if (entity instanceof EntityPlayer && Targets.players()) {
                if (entity == Tracers.mc.thePlayer) {
                    continue;
                }
                this.player(entity);
            }
            else if (entity instanceof EntityMob && Targets.mobs()) {
                this.mob(entity);
            }
            else {
                if (!(entity instanceof EntityAnimal) || !Targets.animals()) {
                    continue;
                }
                this.animal(entity);
            }
        }
        super.onRender();
    }
    
    public void player(final EntityLivingBase entity) {
        final float red = 0.1f;
        final float green = 1.0f;
        final float blue = 1.0f;
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        if (entity.hurtTime != 0) {
            this.render(255.0f, 0.0f, 0.0f, xPos, yPos, zPos);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos);
        }
    }
    
    public void mob(final EntityLivingBase entity) {
        final float red = 0.25f;
        final float green = 0.1f;
        final float blue = 1.0f;
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        if (entity.hurtTime != 0) {
            this.render(255.0f, green, blue, xPos, yPos, zPos);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos);
        }
    }
    
    public void animal(final EntityLivingBase entity) {
        final float red = 0.0f;
        final float green = 0.5f;
        final float blue = 0.5f;
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Tracers.mc.timer.renderPartialTicks;
        Tracers.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        if (entity.hurtTime != 0) {
            this.render(255.0f, green, blue, xPos, yPos, zPos);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos);
        }
    }
    
    public void render(final float red, final float green, final float blue, final double x, final double y, final double z) {
        RenderUtils.drawTracerLine(x, y, z, red, green, blue, 0.3f, 2.0f);
    }
    
    @Override
    public void onEnable() {
        this.oldBobbingSetting = Tracers.mc.gameSettings.viewBobbing;
        Tracers.mc.gameSettings.viewBobbing = false;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Tracers.mc.gameSettings.viewBobbing = this.oldBobbingSetting;
        super.onDisable();
    }
}
