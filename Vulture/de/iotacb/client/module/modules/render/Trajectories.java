package de.iotacb.client.module.modules.render;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ModuleInfo(name = "Trajectories", description = "Draws a preview line where arrows will land", category = Category.RENDER)
public class Trajectories extends Module {
	
	private boolean isBow;

	@Override
	public void onInit() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	/**
	 * Saint, wenn ichs richtig raffe try ichs selbst mal iskde
	 * @param event
	 */
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			final ItemStack stack = getMc().thePlayer.getHeldItem();
			if (stack == null || !isItemValid(stack)) return;
			
			isBow = stack.getItem() instanceof ItemBow;
			
			final double playerYaw = getMc().thePlayer.rotationYaw;
			final double playerPitch = getMc().thePlayer.rotationPitch;
			
			/**
			 * Start position
			 */
			double projectilePosX = getMc().getRenderManager().renderPosX - Math.cos(Math.toRadians(playerYaw)) * .16F;
			double projectilePosY = getMc().getRenderManager().renderPosY + getMc().thePlayer.getEyeHeight();
			double projectilePosZ = getMc().getRenderManager().renderPosZ - Math.sin(Math.toRadians(playerYaw)) * .16F;
			
			/**
			 * Die berechnung verstehe ich noch nicht wirklich
			 */
			double projectileMotionX = (-Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
			double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - (isThrowablePotion(stack) ? 20 : 0))) * (isBow ? 1 : .4);
			double projectileMotionZ = (Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
			
			double shootPower = getMc().thePlayer.getItemInUseDuration();
			
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
			Client.RENDER2D.color(Client.INSTANCE.getClientColor());
			GL11.glLineWidth(1.5F);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			{
				while (!projectileHasLanded && projectilePosY > 0) {
					final Vec3 currentPosition = new Vec3(projectilePosX, projectilePosY, projectilePosZ);
					final Vec3 nextPosition = new Vec3(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);
					
					final MovingObjectPosition possibleLandingPositon = getMc().theWorld.rayTraceBlocks(currentPosition, nextPosition, false, true, false);
					
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
					
					GL11.glVertex3d(projectilePosX - getMc().getRenderManager().renderPosX, projectilePosY - getMc().getRenderManager().renderPosY, projectilePosZ - getMc().getRenderManager().renderPosZ);
				}
			}
			GL11.glEnd();
			GlStateManager.enableTexture2D();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.resetColor();
			
			if (landingPosition != null) {
				if (landingPosition.typeOfHit == MovingObjectType.BLOCK) {
					Client.RENDER3D.drawBox(landingPosition.getBlockPos(), Client.INSTANCE.getClientColor().setAlpha(100), false);
				}
			}
		}
	}
	
	private boolean isItemValid(ItemStack stack) {
		return (stack.getItem() instanceof ItemBow) || (stack.getItem() instanceof ItemEnderPearl) || (stack.getItem() instanceof ItemEgg) || (stack.getItem() instanceof ItemSnowball) || isThrowablePotion(stack);
	}
	
	private boolean isThrowablePotion(ItemStack stack) {
		return stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(getMc().thePlayer.getHeldItem().getItemDamage());
	}

}
