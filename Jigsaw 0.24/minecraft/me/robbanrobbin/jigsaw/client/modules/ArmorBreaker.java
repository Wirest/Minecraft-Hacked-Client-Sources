package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ArmorBreaker extends Module {

	int slotBefore;
	int bestSlot;
	float eating;
	static boolean disable;

	public ArmorBreaker() {
		super("ArmorBreaker", Keyboard.KEY_NONE, Category.COMBAT, "Deals more damage to armor.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}

	@Override
	public void onLeftClick() {

	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) packet).getAction() == Action.ATTACK) {
			breaker(((C02PacketUseEntity) packet).getEntityFromWorld(mc.theWorld));
		}
		super.onPacketSent(packet);
	}

	public void breaker(Entity en) {
		if (!mc.thePlayer.onGround) {
			return;
		}
		ItemStack current = mc.thePlayer.getHeldItem();
		for (int i = 0; i < 46; i++) {
			ItemStack toSwitch = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if ((current != null) && (toSwitch != null) && ((toSwitch.getItem() instanceof ItemSword))) {
				mc.playerController.windowClick(0, i, mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
			}
		}
	}

}
