/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Mod.mods.render;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventRender3D;
import Blizzard.Mod.Mod;
import Blizzard.Utils.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Mod {
	public static boolean Players = true;
	public static boolean Mobs = true;
	public static boolean Animals = true;

	public ESP() {
		super("ESP", "ESP", 0, Category.RENDER);
	}

	@EventTarget
	public void onRender(EventRender3D event) {
		for (Object theObject : ESP.mc.theWorld.loadedEntityList) {
			if (!(theObject instanceof EntityLivingBase))
				continue;
			EntityLivingBase entity = (EntityLivingBase) theObject;
			if (entity instanceof EntityPlayer && Players) {
				if (entity == ESP.mc.thePlayer)
					continue;
				this.player(entity);
				continue;
			}
			if (entity instanceof EntityMob && Mobs) {
				this.mob(entity);
				continue;
			}
			if (!(entity instanceof EntityAnimal) || !Animals)
				continue;
			this.animal(entity);
		}
		super.onRender();
	}

	public void player(EntityLivingBase entity) {
		float red = 0.5f;
		float green = 0.5f;
		float blue = 1.0f;
		double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosX;
		double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosY;
		double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
	}

	public void mob(EntityLivingBase entity) {
		float red = 1.0f;
		float green = 0.0f;
		float blue = 0.0f;
		double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosX;
		double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosY;
		double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
	}

	public void animal(EntityLivingBase entity) {
		float red = 0.5f;
		float green = 0.5f;
		float blue = 0.5f;
		double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosX;
		double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosY;
		double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ESP.mc.timer.renderPartialTicks
				- ESP.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
	}

	public void render(float red, float green, float blue, double x, double y, double z, float width, float height) {
		RenderUtils.drawEntityESP(x, y, z, width, height, red, green, blue, 0.45f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f);
	}
}
