package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {

	public FastBow() {
		super("FastBow", Keyboard.KEY_NONE, Category.COMBAT, "Enables you to shoot without charging your bow first.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!currentMode.equalsIgnoreCase("Fast")) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			// return;
		}
		if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& mc.gameSettings.keyBindUseItem.pressed) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

			mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(),
					mc.theWorld, mc.thePlayer);

			for (int i = 0; i < 20; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}

			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

			mc.thePlayer.inventory.getCurrentItem().getItem()
					.onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 10);
		}

		super.onUpdate();
	}

	@Override
	public void onRightClick() {
		if (!currentMode.equalsIgnoreCase("Single")) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& mc.gameSettings.keyBindUseItem.pressed) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

			mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(),
					mc.theWorld, mc.thePlayer);

			for (int i = 0; i < 20; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}

			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

			mc.thePlayer.inventory.getCurrentItem().getItem()
					.onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 10);
		}

		super.onRightClick();
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	@Override
	public String[] getModes() {
		return new String[] { "Single", "Fast" };
	}

}
