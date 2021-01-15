package me.onlyeli.ice.modules;

import me.onlyeli.ice.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import java.util.*;
import org.lwjgl.input.*;
import me.onlyeli.ice.utils.*;

public class ESP extends Module
{
    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER);
    }
    
    public void onRender() {
        if (!this.isToggled()) {
            return;
        }
        for (final Object theObject : Wrapper.mc.theWorld.loadedEntityList) {
            if (!(theObject instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)theObject;
            if (entity instanceof EntityPlayer) {
                this.player(entity);
            }
            else if (entity instanceof EntityMob) {
                this.mob(entity);
            }
            else if (entity instanceof EntityAnimal) {
                this.animal(entity);
            }
            else {
                this.passive(entity);
            }
        }
        super.onRender();
    }
    
    public void player(final EntityLivingBase entity) {
        final float red = 1.0f;
        final float green = 5.0f;
        final float blue = 1.0f;
        final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosX;
        final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosY;
        final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }
    
    public void mob(final EntityLivingBase entity) {
        final float red = 1.9f;
        final float green = 0.0f;
        final float blue = 0.2f;
        final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosX;
        final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosY;
        final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }
    
    public void animal(final EntityLivingBase entity) {
        final float red = 0.4f;
        final float green = 3.3f;
        final float blue = 0.4f;
        final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosX;
        final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosY;
        final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }
    
    public void passive(final EntityLivingBase entity) {
        final float red = 0.3f;
        final float green = 0.3f;
        final float blue = 0.3f;
        final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosX;
        final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosY;
        final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.mc.timer.renderPartialTicks - Wrapper.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }
    
    public void render(final float red, final float green, final float blue, final double x, final double y, final double z, final float width, final float height) {
        RenderUtils.drawEntityESP(x, y, z, width, height, red, green, blue, 0.45f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f);
    }
}
