package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoBlock extends Module {

	public AutoBlock() {
		super("AutoBlock", Keyboard.KEY_NONE, Category.COMBAT, "Blocks everytime an aura is attacking, to minimize damage.");
	}

	@Override
	public void onUpdate() {
		if (!doBlock()) {
			return;
		}
		if(mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
		mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(),
				this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
	}

	public static void startBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
					mc.thePlayer.getHeldItem().getMaxItemUseDuration());
		}
	}

	public static void stopBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
	}
	
	public static boolean doBlock() {
		return !(!KillAura.doBlock() && !TpAura.doBlock());
	}

}
