package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventRender3D;
import me.onlyeli.ice.main.Ice;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.utils.ModeUtils;
import me.onlyeli.ice.utils.RenderUtils;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module {
	public Tracers() {
		super("Tracers", Keyboard.KEY_U, Category.RENDER);
	}

	@Override
	public void onEnable() {
		EventManager.register(this);
	}

	@Override
	public void onDisable() {
		EventManager.unregister(this);
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		for (Object obj : this.mc.theWorld.loadedEntityList) {
			if (obj instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) obj;
				if (entity == this.mc.thePlayer || !ModeUtils.isValidForTracers(entity)) {
					continue;
				}
				GL11.glLoadIdentity();
				this.mc.entityRenderer.orientCamera(event.partialTicks);
				double posX = entity.posX - this.mc.getRenderManager().renderPosX;
				double posY = entity.posY - this.mc.getRenderManager().renderPosY;
				double posZ = entity.posZ - this.mc.getRenderManager().renderPosZ;
				if (entity.hurtTime > 5) {
					RenderUtils.drawTracerLine(posX, posY, posZ, 1, 0, 0, 0.5F, 1);
				} else if (entity instanceof EntityPlayer)
					if (!Ice.getIce().friendUtils.isFriend(entity.getName())) {
					RenderUtils.drawTracerLine(posX, posY, posZ, 1, 1, 1, 0.5F, 1);
				} else if ((entity instanceof EntityMob || entity instanceof EntitySlime))
					if (!Ice.getIce().friendUtils.isFriend(entity.getName())) {
					RenderUtils.drawTracerLine(posX, posY, posZ, 1, 0, 0, 0.5F, 1);
				} else if ((entity instanceof EntityCreature || entity instanceof EntityBat
						|| entity instanceof EntitySquid || entity instanceof EntityVillager))
					if (!Ice.getIce().friendUtils.isFriend(entity.getName())) {
					RenderUtils.drawTracerLine(posX, posY, posZ, 0, 1, 0.3F, 0.5F, 1);
				} else {
					if (!Ice.getIce().friendUtils.isFriend(entity.getName())) {
						continue;
					}
					RenderUtils.drawTracerLine(posX, posY, posZ, 0, 1, 1, 0.5F, 1);
				}
			}
		}
	}
}
