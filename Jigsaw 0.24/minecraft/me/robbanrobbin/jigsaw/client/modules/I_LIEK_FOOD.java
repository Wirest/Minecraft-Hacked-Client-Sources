package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class I_LIEK_FOOD extends Module {

	public I_LIEK_FOOD() {
		super("I LIEK FOOD", Keyboard.KEY_NONE, Category.FUN, "Yum");
	}

	@Override
	public void onUpdate() {
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemFood)) {
			return;
		}
		sendPacket(new C08PacketPlayerBlockPlacement(stack));
		// sendPacket(new C03PacketPlayer(true));
		// sendPacket(new C03PacketPlayer(true));
		// sendPacket(new C03PacketPlayer(true));
		// sendPacket(new C03PacketPlayer(true));
		sendPacket(new C09PacketHeldItemChange(1));
		mc.thePlayer.stopUsingItem();
		super.onUpdate();
	}

}
