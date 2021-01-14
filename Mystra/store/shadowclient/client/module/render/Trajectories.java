package store.shadowclient.client.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.Event3D;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

public class Trajectories extends Module {
	
	private boolean isBow;

	public Trajectories() {
		super("Trajectories", 0, Category.RENDER);
	}
	
	@EventTarget
	public void onRender(Event3D event) {
		final ItemStack stack = mc.thePlayer.getHeldItem();
		if (stack == null || !isItemValid(stack)) return;
		
		isBow = stack.getItem() instanceof ItemBow;
		
		final double playerYaw = mc.thePlayer.rotationYaw;
		final double playerPitch = mc.thePlayer.rotationPitch;
		
		/**
		 * Start position
		 */
		double projectilePosX = mc.getRenderManager().renderPosX - Math.cos(Math.toRadians(playerYaw)) * .16F;
		double projectilePosY = mc.getRenderManager().renderPosY + mc.thePlayer.getEyeHeight();
		double projectilePosZ = mc.getRenderManager().renderPosZ - Math.sin(Math.toRadians(playerYaw)) * .16F;
		
		/**
		 * Die berechnung verstehe ich noch nicht wirklich
		 */
		double projectileMotionX = (-Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
		double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - (isThrowablePotion(stack) ? 20 : 0))) * (isBow ? 1 : .4);
		double projectileMotionZ = (Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
		
		double shootPower = mc.thePlayer.getItemInUseDuration();
		
		if (isBow) {
			shootPower /= 20;
			// Das auch noch nicht
			shootPower = ((shootPower * shootPower) + (shootPower * 2)) / 3;
			
			if (shootPower < .1) return;
			if (shootPower > 1) shootPower = 1;
		}
		
		final double distance = Math.sqrt(projectileMotionX * projectileMotionX + projectileMotionY * projectileMotionY + projectileMotionZ * projectileMotionZ);
		
		projectileMotionX /= distance;
		projectileMotionY /= distance;
		projectileMotionZ /= distance;
		
		projectileMotionX *= (isBow ? shootPower : .5) * 3;
		projectileMotionY *= (isBow ? shootPower : .5) * 3;
		projectileMotionZ *= (isBow ? shootPower : .5) * 3;
		
		boolean projectileHasLanded = false;
		MovingObjectPosition landingPosition = null;
		
		GlStateManager.resetColor();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableTexture2D();
		Shadow.RENDER2D.color(10, 0, 200, 200);
		GL11.glLineWidth(1.5F);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		{
			while (!projectileHasLanded && projectilePosY > 0) {
				final Vec3 currentPosition = new Vec3(projectilePosX, projectilePosY, projectilePosZ);
				final Vec3 nextPosition = new Vec3(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);
				
				final MovingObjectPosition possibleLandingPositon = mc.theWorld.rayTraceBlocks(currentPosition, nextPosition, false, true, false);
				
				if (possibleLandingPositon != null) {
					if (possibleLandingPositon.typeOfHit != MovingObjectType.MISS) {
						landingPosition = possibleLandingPositon;
						projectileHasLanded = true;
					}
				}
				
				// Position updated
				projectilePosX += projectileMotionX;
				projectilePosY += projectileMotionY;
				projectilePosZ += projectileMotionZ;
				
				
				// Motions updaten
				projectileMotionX *= .99;
				projectileMotionY *= .99;
				projectileMotionZ *= .99;
				
				projectileMotionY -= (isBow ? .05 : isThrowablePotion(stack) ? .05 : .03); // Gravitation
				
				GL11.glVertex3d(projectilePosX - mc.getRenderManager().renderPosX, projectilePosY - mc.getRenderManager().renderPosY, projectilePosZ - mc.getRenderManager().renderPosZ);
			}
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
		
		if (landingPosition != null) {
			if (landingPosition.typeOfHit == MovingObjectType.BLOCK) {
				Shadow.RENDER3D.drawBox(landingPosition.getBlockPos(), new Color(10, 0, 200, 200), false);
			}
		}
	}
	
	private boolean isItemValid(ItemStack stack) {
		return (stack.getItem() instanceof ItemBow) || (stack.getItem() instanceof ItemEnderPearl) || (stack.getItem() instanceof ItemEgg) || (stack.getItem() instanceof ItemSnowball) || isThrowablePotion(stack);
	}
	
	private boolean isThrowablePotion(ItemStack stack) {
		return stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage());
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}
