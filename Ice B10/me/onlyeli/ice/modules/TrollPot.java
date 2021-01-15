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

public class TrollPot extends Module {

	public TrollPot() {
		super("TrollPot", Keyboard.KEY_Y, Category.WORLD);

	}

	@Override
	public void onEnable() {
		if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
			ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
			this.setToggled(false);
			return;
		}
		if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
			ChatUtils.sendMessageToPlayer("Creative mode only.");
			this.setToggled(false);
			return;
		}
		ItemStack stack = new ItemStack(Items.potionitem, 64);
		stack.setItemDamage(16384);
		NBTTagList effects = new NBTTagList();
		for (int i = 1; i <= 23; ++i) {
			NBTTagCompound effect = new NBTTagCompound();
			effect.setInteger("Amplifier", Integer.MAX_VALUE);
			effect.setInteger("Duration", Integer.MAX_VALUE);
			effect.setInteger("Id", i);
			effects.appendTag(effect);
		}
		stack.setTagInfo("CustomPotionEffects", effects);
		stack.setStackDisplayName("§l§4Troll §l§cPot.");
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		ChatUtils.sendMessageToPlayer("Potion created");
		this.setToggled(false);
	}

}
