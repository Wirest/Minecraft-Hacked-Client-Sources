package info.spicyclient.util;

import org.lwjgl.opengl.GL11;

import info.spicyclient.chatCommands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class RenderUtils {
	
	// Made by lavaflowglow 11/19/2020 3:39 AM
	
	public static boolean SetCustomYaw = false;
	public static float CustomYaw = 0;
	
	public static void setCustomYaw(float customYaw) {
		CustomYaw = customYaw;
		SetCustomYaw = true;
	}
	
	public static void resetPlayerYaw() {
		SetCustomYaw = false;
	}
	
	public static float getCustomYaw() {
		
		return CustomYaw;
		
	}
	public static boolean SetCustomPitch = false;
	public static float CustomPitch = 0;
	
	public static void setCustomPitch(float customPitch) {
		CustomPitch = customPitch;
		SetCustomPitch = true;
	}
	
	public static void resetPlayerPitch() {
		SetCustomPitch = false;
	}
	
	public static float getCustomPitch() {
		
		return CustomPitch;
		
	}
	
	// Made by lavaflowglow 11/19/2020 3:39 AM
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
	public static Tessellator tessellator = Tessellator.getInstance();
	
	// Someone gave me this code
	public static void drawPlayerBox(Double posX, Double posY, Double posZ, AbstractClientPlayer player){
		double x =
			posX - 0.5
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			posY
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			posZ - 0.5
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
		RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 2.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        minX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        minY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        minZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
        maxX -= Minecraft.getMinecraft().getRenderManager().renderPosX;
        maxY -= Minecraft.getMinecraft().getRenderManager().renderPosY;
        maxZ -= Minecraft.getMinecraft().getRenderManager().renderPosZ;
        
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(3.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
        
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(minX, minY, minZ).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).endVertex();
        tessellator.draw();
        
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
        
	}
	
}
