package de.iotacb.client.utilities.render;

import java.awt.Color;

import javax.vecmath.Vector3d;

import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.RenderChanger;
import de.iotacb.client.utilities.math.Vec;
import de.iotacb.cu.core.color.ColorUtil;
import de.iotacb.cu.core.render.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class Render3D {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
    public final void drawBoxFilled(AxisAlignedBB axisAlignedBB) {
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    	}
    	GL11.glEnd();
    }
    
    public final void drawBox(AxisAlignedBB axisAlignedBB) {
    	GL11.glBegin(GL11.GL_LINES);
    	{
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    	}
    	GL11.glEnd();
    }
    
    public final void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
    	GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(3F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        Client.RENDER2D.color(color);
        RenderUtil.INSTANCE.makeOutlinedBoundingBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public final void drawAxisAlignedBBFilled(AxisAlignedBB axisAlignedBB, Color color, boolean depth) {
    	GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (depth) GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        Client.RENDER2D.color(color);
        drawBoxFilled(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (depth) GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public final void drawAxisAlignedBBShadedFilled(AxisAlignedBB axisAlignedBB, Color color, boolean depth) {
    	GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableAlpha();
        if (depth) GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        final Color alphaColor = ColorUtil.INSTANCE.setAlpha(0, color);
        GL11.glShadeModel(GL11.GL_SMOOTH);
    	GL11.glBegin(GL11.GL_QUADS);
    	{
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(color);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
    		
    		Client.RENDER2D.color(alphaColor);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
    		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    	}
    	GL11.glEnd();
    	GL11.glShadeModel(GL11.GL_FLAT);
    	GlStateManager.enableAlpha();
        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (depth) GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public final void drawLine(Vec3 pos1, Vec3 pos2, Color color) {
        final double x = pos2.xCoord - MC.getRenderManager().renderPosX;
        final double y = pos2.yCoord - MC.getRenderManager().renderPosY;
        final double z = pos2.zCoord - MC.getRenderManager().renderPosZ;
    	
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		Client.RENDER2D.color(Client.INSTANCE.getClientColor());
		GL11.glLineWidth(1.5F);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		{
			GL11.glVertex3d(0, 0, 0);
			GL11.glVertex3d(x, y, z);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
    }
    
    public final void drawBox(Entity entity, Color color) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        final boolean flag = changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue();
        
        final double offset = -.2 + (flag ? .2 : 0);
        
        drawAxisAlignedBBFilled(new AxisAlignedBB(axisAlignedBB.minX + offset, axisAlignedBB.minY, axisAlignedBB.minZ + offset, axisAlignedBB.maxX - offset, axisAlignedBB.maxY - offset - (flag ? .6 : 0), axisAlignedBB.maxZ - offset), color, true);
    }
    
    public final void drawBoxShaded(Entity entity, Color color) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        final boolean flag = changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue();
        
        final double offset = -.2 + (flag ? .2 : 0);
        
        drawAxisAlignedBBShadedFilled(new AxisAlignedBB(axisAlignedBB.minX + offset, axisAlignedBB.minY, axisAlignedBB.minZ + offset, axisAlignedBB.maxX - offset, axisAlignedBB.maxY - offset - (flag ? .6 : 0), axisAlignedBB.maxZ - offset), color, true);
    }
    
    public final void drawBox(TileEntity entity, Color color) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final double x = entity.getPos().getX() - renderManager.renderPosX;
        final double y = entity.getPos().getY() - renderManager.renderPosY;
        final double z = entity.getPos().getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = MC.theWorld.getBlockState(entity.getPos()).getBlock();

        if (block != null) {
            final EntityPlayer player = MC.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(MC.theWorld, entity.getPos()).expand(.002, .002, .002).offset(-posX, -posY, -posZ);
            
            drawAxisAlignedBBFilled(axisAlignedBB, color, true);
        }
    }
    
    public final void drawBoxShaded(TileEntity entity, Color color) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final double x = entity.getPos().getX() - renderManager.renderPosX;
        final double y = entity.getPos().getY() - renderManager.renderPosY;
        final double z = entity.getPos().getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = MC.theWorld.getBlockState(entity.getPos()).getBlock();

        if (block != null) {
            final EntityPlayer player = MC.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(MC.theWorld, entity.getPos()).expand(.002, .002, .002).offset(-posX, -posY, -posZ);
            
            drawAxisAlignedBBShadedFilled(axisAlignedBB, color, true);
        }
    }
    
    public final void drawBox(BlockPos pos, Color color, boolean depth) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = MC.theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = MC.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(MC.theWorld, pos).expand(.002, .002, .002).offset(-posX, -posY, -posZ);
            
            drawAxisAlignedBBFilled(axisAlignedBB, color, depth);
        }
    }
    
    public final void drawBox(Vec3 pos, Vec3 size, Color color) {
        final RenderManager renderManager = MC.getRenderManager();
        final Timer timer = MC.timer;
        
        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + size.xCoord, y + size.yCoord, z + size.zCoord);
        final Block block = MC.theWorld.getBlockState(pos.getBlockPos()).getBlock();

        if (block != null) {
            drawAxisAlignedBBFilled(axisAlignedBB, color, true);
        }
    }
}
