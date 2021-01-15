package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.ChatUtils;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class KillerPot extends Module {

	public KillerPot() {
		super("KillerPot.", Keyboard.KEY_NONE, Category.WORLD);
	}

	@Override
	public void onEnable() {
		if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
			ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
			if(!this.isToggled())
				return;
		}
		if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
			ChatUtils.sendMessageToPlayer("Creative mode only.");
			if(!this.isToggled())
				return;
		}
		ItemStack stack = new ItemStack(Items.potionitem, 64);
		stack.setItemDamage(16384);
		NBTTagList effects = new NBTTagList();
		NBTTagCompound effect = new NBTTagCompound();
		effect.setInteger("Amplifier", 125);
		effect.setInteger("Duration", 2000);
		effect.setInteger("Id", 6);
		effects.appendTag(effect);
		stack.setTagInfo("CustomPotionEffects", effects);
		stack.setStackDisplayName("§4Killer §cPot.");
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		Wrapper.mc.thePlayer.closeScreen();
		ChatUtils.sendMessageToPlayer("Potion created.");
		this.setToggled(false);
	}

}
