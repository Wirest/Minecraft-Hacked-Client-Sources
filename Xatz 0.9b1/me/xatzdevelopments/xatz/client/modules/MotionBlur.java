package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.xatzdevelopments.xatz.client.Colors;
import me.xatzdevelopments.xatz.client.events.EventRender;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;

public class MotionBlur extends Module {

	public MotionBlur() {
		super("MotionBlur", Keyboard.KEY_NONE, Category.RENDER, "test");
		
	}

	
	public void onUpdate(EventRender event) {
		EntityPlayerSP player = mc.thePlayer;
		GL11.glPushMatrix();
		pre3D();
		mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
		double x = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks()
				- RenderManager.renderPosX;
		double y = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks()
				- RenderManager.renderPosY;
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks()
				- RenderManager.renderPosZ;
		double xDelta = player.posX - player.prevPosX;
		double yDelta = player.posY - player.prevPosY;
		double zDelta = player.posZ - player.prevPosZ;
		int color = Colors.getColor(0, 255, 0);
		double yMotion = 0.0D;
		double initVel = mc.thePlayer.motionY;

		for (int i = 0; i < 6; ++i) {
			yMotion += initVel - 0.002D * (double) i;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + xDelta * (double) i, y + (yDelta + yMotion) * (double) i,
					z + zDelta * (double) i);
			AxisAlignedBB var11 = player.getEntityBoundingBox();
			AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - player.posX, var11.minY - player.posY,
					var11.minZ - player.posZ, var11.maxX - player.posX, var11.maxY - player.posY,
					var11.maxZ - player.posZ);
			glColor(color);
			RenderUtils.drawOutlinedBoundingBox(var12);
			GlStateManager.popMatrix();
		}

		post3D();
		GL11.glPopMatrix();
	}

	public static void glColor(int hex) {
		float alpha = (float) (hex >> 24 & 255) / 255.0F;
		float red = (float) (hex >> 16 & 255) / 255.0F;
		float green = (float) (hex >> 8 & 255) / 255.0F;
		float blue = (float) (hex & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDisable(2896);
		GL11.glDepthMask(false);
		GL11.glHint(3154, 4354);
	}

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (!GL11.glIsEnabled(2896)) {
			GL11.glEnable(2896);
		}

	}

}
