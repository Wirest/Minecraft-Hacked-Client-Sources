package me.ihaq.iClient.modules.Combat;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovingObjectPosition;

public class Aura extends Module {

	private static String mode;
	int triggerDelay;
	//private NumberValue range = new NumberValue(this, "Range", "karange", 4.5, 3, 7, 0.1);

	public Aura() {
		super("Aura", Keyboard.KEY_NONE, Category.COMBAT, mode);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {

		if (!this.isToggled())
			return;

		triggerBot();
	}

	public void triggerBot() {
		setMode(": \u00A7fTriggerBot");
		List list = mc.theWorld.playerEntities;

		for (Iterator<Entity> entities = mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
			Object theObject = entities.next();
			if (theObject instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) theObject;

				if (entity instanceof EntityPlayerSP) {
					return;
				}

				if (mc.objectMouseOver.entityHit == null) {
					return;
				}
				if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
					return;
				}

				if (!entity.equals(mc.objectMouseOver.entityHit)) {
					return;
				}

				if (mc.thePlayer.getDistanceToEntity(entity) <= 4.7F && triggerDelay > 3) {
					if (entity.isEntityAlive()) {
						if (AntiBot.active) {
							if (AntiBot.isNotBot(entity)) {
								attackEntity(entity);
							}
						} else {
							attackEntity(entity);
						}

					}
				}
				triggerDelay++;
			}
		}
	}

	public void attackEntity(EntityLivingBase entity) {
		if (Criticals.active == true) {
			Criticals.doCrits();
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
			mc.thePlayer.swingItem();
			triggerDelay = 0;
			return;
		} else {
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
			mc.thePlayer.swingItem();
			triggerDelay = 0;
			return;
		}
	}
}
