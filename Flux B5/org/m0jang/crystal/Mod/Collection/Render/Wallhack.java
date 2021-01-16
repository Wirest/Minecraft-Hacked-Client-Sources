package org.m0jang.crystal.Mod.Collection.Render;

import org.m0jang.crystal.Mod.*;
import org.m0jang.crystal.Events.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.entity.*;
import org.m0jang.crystal.*;
import net.minecraft.entity.*;
import java.awt.*;
import org.m0jang.crystal.Utils.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;

public class Wallhack extends Module
{
    public Wallhack() {
        super("Wallhack", Category.Render, false, new SubModule[0]);
    }
    
    public void onEnable() {
        super.onEnable();
    }
    
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        for (final Object obj : Minecraft.theWorld.playerEntities) {
            if (obj instanceof EntityPlayer && obj != Minecraft.thePlayer) {
                final EntityPlayer entity = (EntityPlayer)obj;
                GL11.glPushMatrix();
                GlStateManager.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
                final double x = entity.posX;
                final double y = entity.posY + entity.getEyeHeight() + 0.3;
                final double z = entity.posZ;
                String color = "";
                if (entity.getHealth() >= 15.0f && entity.getHealth() <= 20.0f) {
                    color = " \u00c2¡ìa";
                }
                else if (entity.getHealth() >= 10.0f && entity.getHealth() < 15.0f) {
                    color = " \u00c2¡ì6";
                }
                else if (entity.getHealth() >= 5.0f && entity.getHealth() < 10.0f) {
                    color = " \u00c2¡ìc";
                }
                else if (entity.getHealth() >= 0.0f && entity.getHealth() < 5.0f) {
                    color = " \u00c2¡ì4";
                }
                String meme = "";
                if (!Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ìf";
                }
                else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ì3[F] \u00c2¡ì3";
                }
                String dank = "";
                if (!entity.isSneaking()) {
                    dank = "";
                }
                else if (entity.isSneaking()) {
                    dank = "\u00c2¡ì6[S] ";
                }
                final int kush = 17;
                int c = 0;
                if (!entity.isOnSameTeam((EntityLivingBase)Minecraft.thePlayer)) {
                    c = new Color(255, 102, 0, 255).getRGB();
                }
                else if (entity.isOnSameTeam((EntityLivingBase)Minecraft.thePlayer)) {
                    c = new Color(0, 166, 22, 255).getRGB();
                }
                this.renderNameTag(String.valueOf(color) + MathUtils.round((double)entity.getHealth(), 2), x, y, z, c);
                GlStateManager.translate(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);
                GL11.glPopMatrix();
            }
        }
    }
    
    @EventTarget
    public void onRender3DDD(final EventRender3D event) {
        for (final Object obj : Minecraft.theWorld.playerEntities) {
            if (obj instanceof EntityPlayer && obj != Minecraft.thePlayer) {
                final EntityPlayer entity = (EntityPlayer)obj;
                GL11.glPushMatrix();
                GlStateManager.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
                final double x = entity.posX;
                final double y = entity.posY + entity.getEyeHeight() + 0.2;
                final double z = entity.posZ;
                String color = "";
                if (entity.getHealth() >= 15.0f && entity.getHealth() <= 20.0f) {
                    color = " \u00c2¡ìa";
                }
                else if (entity.getHealth() >= 10.0f && entity.getHealth() < 15.0f) {
                    color = " \u00c2¡ì6";
                }
                else if (entity.getHealth() >= 5.0f && entity.getHealth() < 10.0f) {
                    color = " \u00c2¡ìc";
                }
                else if (entity.getHealth() >= 0.0f && entity.getHealth() < 5.0f) {
                    color = " \u00c2¡ì4";
                }
                String meme = "";
                if (!Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ìf";
                }
                else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ì3[F] \u00c2¡ì3";
                }
                String dank = "";
                if (!entity.isSneaking()) {
                    dank = "";
                }
                else if (entity.isSneaking()) {
                    dank = "\u00c2¡ì6[S] ";
                }
                GL11.glPopMatrix();
            }
        }
    }
    
    @EventTarget
    public void onRender3DD(final EventRender3D event) {
        for (final Object obj : Minecraft.theWorld.playerEntities) {
            if (obj instanceof EntityPlayer && obj != Minecraft.thePlayer) {
                final EntityPlayer entity = (EntityPlayer)obj;
                GL11.glPushMatrix();
                GlStateManager.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
                final double x = entity.posX;
                final double y = entity.posY + entity.getEyeHeight() + 0.2;
                final double z = entity.posZ;
                String color = "";
                if (entity.getHealth() >= 15.0f && entity.getHealth() <= 20.0f) {
                    color = " \u00c2¡ìa";
                }
                else if (entity.getHealth() >= 10.0f && entity.getHealth() < 15.0f) {
                    color = " \u00c2¡ì6";
                }
                else if (entity.getHealth() >= 5.0f && entity.getHealth() < 10.0f) {
                    color = " \u00c2¡ìc";
                }
                else if (entity.getHealth() >= 0.0f && entity.getHealth() < 5.0f) {
                    color = " \u00c2¡ì4";
                }
                String meme = "";
                if (!Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ìf";
                }
                else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
                    meme = "\u00c2¡ì3[F] \u00c2¡ì3";
                }
                String dank = "";
                if (!entity.isSneaking()) {
                    dank = "";
                }
                else if (entity.isSneaking()) {
                    dank = "\u00c2¡ì6[S] ";
                }
                GL11.glPopMatrix();
            }
        }
    }
    
    public void renderNameTag(final String tag, final double pX, final double pY, final double pZ, final int Color) {
        final FontRenderer var12 = this.mc.fontRendererObj;
        float var13 = (float)(Minecraft.thePlayer.getDistance(pX, pY, pZ) / 4.0);
        if (var13 < 1.6f) {
            var13 = 1.6f;
        }
        final RenderManager renderManager = this.mc.getRenderManager();
        float scale = var13;
        scale /= 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)pY + 0.3f, (float)pZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final Tessellator var14 = Tessellator.getInstance();
        final WorldRenderer var15 = var14.getWorldRenderer();
        final int width = this.mc.fontRendererObj.getStringWidth(tag) / 2;
        final int kush = 17;
        final int meme = 0;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScalef(0.0165f, 0.0165f, 0.0165f);
        GL11.glEnable(2848);
        GL11.glScalef(-1.0f, -1.0f, -1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public void renderWhTag(final String tag, final double pX, final double pY, final double pZ) {
        final FontRenderer var12 = this.mc.fontRendererObj;
        float var13 = (float)(Minecraft.thePlayer.getDistance(pX, pY, pZ) / 4.0);
        if (var13 < 1.6f) {
            var13 = 1.6f;
        }
        final RenderManager renderManager = this.mc.getRenderManager();
        float scale = var13;
        scale /= 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)pY + 0.3f, (float)pZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-0.033f, -0.033f, 0.033f);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final Tessellator var14 = Tessellator.getInstance();
        final WorldRenderer var15 = var14.getWorldRenderer();
        final int width = this.mc.fontRendererObj.getStringWidth(tag) / 2;
        final int kush = 17;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        this.mc.fontRendererObj.drawStringWithShadow(tag, (float)(kush + 1), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT + 1) + 23 + 1), -1);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public void renderWhName(final String tag, final double pX, final double pY, final double pZ) {
        final FontRenderer var12 = this.mc.fontRendererObj;
        float var13 = (float)(Minecraft.thePlayer.getDistance(pX, pY, pZ) / 4.0);
        if (var13 < 1.6f) {
            var13 = 1.6f;
        }
        final RenderManager renderManager = this.mc.getRenderManager();
        float scale = var13;
        scale /= 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)pY + 0.3f, (float)pZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-0.033f, -0.033f, 0.033f);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final Tessellator var14 = Tessellator.getInstance();
        final WorldRenderer var15 = var14.getWorldRenderer();
        final int width = this.mc.fontRendererObj.getStringWidth(tag) / 2;
        final int kush = 17;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        this.mc.fontRendererObj.drawStringWithShadow(" " + tag, (float)(kush + 1), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT + 1) + 13 + 1), -1);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
