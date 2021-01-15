// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;
import net.minecraft.entity.EntityLivingBase;
import me.CheerioFX.FusionX.utils.GuiUtils;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class ESP extends Module
{
    public ESP() {
        super("ESP", 0, Category.RENDER);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onRender() {
        if (!this.getState()) {
            return;
        }
        if (GuiUtils.isInGui()) {
            return;
        }
        for (final Object theObject : ESP.mc.theWorld.loadedEntityList) {
            if (!(theObject instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)theObject;
            if (Targets.antibot() && (!entity.isEntityAlive() || entity.ticksExisted <= 100 || entity.isInvisible() || entity.getHealth() <= 0.0f)) {
                return;
            }
            if (entity instanceof EntityPlayer && Targets.players()) {
                if (entity.posY - Wrapper.mc.thePlayer.posY == 3.609375) {
                    return;
                }
                if (entity == ESP.mc.thePlayer) {
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
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        if (entity.hurtTime != 0) {
            this.render(255.0f, 0.0f, 0.0f, xPos, yPos, zPos, entity.width, entity.height);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
        }
    }
    
    public void mob(final EntityLivingBase entity) {
        final float red = 0.25f;
        final float green = 0.1f;
        final float blue = 1.0f;
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        if (entity.hurtTime != 0) {
            this.render(255.0f, green, blue, xPos, yPos, zPos, entity.width, entity.height + entity.height / 5.0f);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height + entity.height / 5.0f);
        }
    }
    
    public void animal(final EntityLivingBase entity) {
        final float red = 0.0f;
        final float green = 0.5f;
        final float blue = 0.5f;
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks;
        ESP.mc.getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        RenderUtils.drawEsp(entity, 0.0f, 0, 0);
        if (entity.hurtTime != 0) {
            this.render(255.0f, green, blue, xPos, yPos, zPos, entity.width, entity.height);
        }
        else {
            this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
        }
    }
    
    public void render(final float red, final float green, final float blue, final double x, final double y, final double z, final float width, final float height) {
        RenderUtils.drawEntityESP(x, y, z, width, height, red, green, blue, 0.11f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    static String removeDuplicates(final String s) {
        final StringBuilder noDupes = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final String si = s.substring(i, i + 1);
            if (noDupes.indexOf(si) == -1) {
                noDupes.append(si);
            }
        }
        return noDupes.toString();
    }
}
