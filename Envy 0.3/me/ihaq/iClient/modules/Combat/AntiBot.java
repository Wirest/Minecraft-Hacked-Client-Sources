package me.ihaq.iClient.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventPreMotionUpdates;
import me.ihaq.iClient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {

	public static boolean active;

	public AntiBot() {
		super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, "");
	}

	@Override
	public void onEnable() {
		active = true;
	}

	@Override
	public void onDisable() {
		active = false;
	}

	public static boolean isNotBot(final EntityLivingBase  e) {
		for (final NetworkPlayerInfo npi : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
			if (npi != null && npi.getGameProfile() != null && npi.getGameProfile().getName().equalsIgnoreCase(e.getName()) && e.ticksExisted >= 35 && e.getEntityId() <= 1000000000 && !e.getName().startsWith("§c")) {
				if (npi.getDisplayName() != null) {
					if (npi.getDisplayName().getFormattedText().startsWith("&f")) {
						continue;
					}
					if (!npi.getDisplayName().getFormattedText().startsWith("&")) {
						continue;
					}
				}
				return true;
			}
		}
		return false;
	}
}
