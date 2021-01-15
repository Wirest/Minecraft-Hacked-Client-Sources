package info.spicyclient.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventRender3D;
import info.spicyclient.modules.Module;
import info.spicyclient.util.PlayerUtils;
import info.spicyclient.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module {
	
	public Tracers() {
		super("Tracers", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventRender3D && e.isPre()) {
			
			int entityNum = 0;
			
			for (Entity entity : mc.theWorld.getLoadedEntityList()) {
				
				if (entity instanceof EntityPlayer && ((EntityPlayer)entity) != mc.thePlayer) {
					
					entityNum++;
					
					double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
					double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY + entity.getEyeHeight();
					double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
					
					GL11.glPushMatrix();
					GL11.glLoadIdentity();
					mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_LINE_SMOOTH);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glBlendFunc(770, 771);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glLineWidth(2.0f);
					GL11.glBegin(2);
					GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
					GL11.glVertex3d(xPos, yPos, zPos);
					GL11.glEnd();
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glDisable(GL11.GL_LINE_SMOOTH);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glPopMatrix();
					
				}
				
			}
			
			this.additionalInformation = entityNum + " Entities";
			
		}
		
	}
	
}
