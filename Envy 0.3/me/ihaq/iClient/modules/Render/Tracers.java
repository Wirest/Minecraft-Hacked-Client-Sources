package me.ihaq.iClient.modules.Render;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventRender3D;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;

public class Tracers extends Module {
	public Tracers() {
		super("Tracers", Keyboard.KEY_NONE, Category.RENDER, "");
	}

	@EventTarget
	public void onRender(EventRender3D e) {
		
		if(!this.isToggled()){
			return;
		}
		Iterator var2 = mc.theWorld.loadedEntityList.iterator();

		while (var2.hasNext()) {
			Object theObject = var2.next();
			if (theObject instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) theObject;
				if (entity instanceof EntityPlayer) {
					Minecraft var10000 = mc;
						Minecraft var10001 = mc;
						if (entity != Minecraft.thePlayer) {
							this.player(entity);
						}
				}
			}
		}
	}

	public void player(EntityLivingBase entity) {
		float red = 1.0F;
		float green = 1.0F;
		float blue = 1.0F;
		double var10001 = entity.posX - entity.lastTickPosX;
		Timer var10002 = mc.timer;
		double var10000 = entity.lastTickPosX + var10001 * (double) Timer.renderPartialTicks;
		mc.getRenderManager();
		double xPos = var10000 - RenderManager.renderPosX;
		var10001 = entity.posY - entity.lastTickPosY;
		var10002 = mc.timer;
		var10000 = entity.lastTickPosY + var10001 * (double) Timer.renderPartialTicks;
		mc.getRenderManager();
		double yPos = var10000 - RenderManager.renderPosY;
		var10001 = entity.posZ - entity.lastTickPosZ;
		var10002 = mc.timer;
		var10000 = entity.lastTickPosZ + var10001 * (double) Timer.renderPartialTicks;
		mc.getRenderManager();
		double zPos = var10000 - RenderManager.renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos);
	}

	public void render(float red, float green, float blue, double x, double y, double z) {
		RenderUtils.drawTracerLine1(x, y, z, 255.0F, 100.0F, 255.0F, 12.0F, 1.0F);
	}


}
