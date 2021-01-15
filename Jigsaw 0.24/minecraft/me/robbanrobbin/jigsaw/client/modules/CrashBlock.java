package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CrashBlock extends Module {
	
	public CrashBlock() {
		super("CrashBlock", Keyboard.KEY_NONE, Category.EXPLOITS);
	}
	@Override
	public void onEnable() {
		if (!mc.thePlayer.capabilities.isCreativeMode) {
			Jigsaw.chatMessage("§cYou need to be in creative mode!");
			this.setToggled(false, true);
			return;
		}
		ItemStack crashBlock = new ItemStack(Blocks.anvil);
		crashBlock.setItemDamage(16384);
		crashBlock.setStackDisplayName(Jigsaw.headerNoBrackets + ": " + Jigsaw.header + "§bPlace me for a surprise! " + Jigsaw.header);
		crashBlock.stackSize = 64;
		mc.thePlayer.inventory.armorInventory[0] = crashBlock;
		Jigsaw.chatMessage("Item was placed in your shoe slot!");
		Minecraft.getMinecraft();
		InventoryPlayer invp = mc.thePlayer.inventory;
		for (int i = 9; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null) {
				itemStack.getItem();
				if (mc.thePlayer.inventory.armorInventory[0] == crashBlock) {
					mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer);
					mc.playerController.windowClick(0, 64537, 0, 0, mc.thePlayer);
				}
			}
		}
		this.setToggled(false, true);
		super.onEnable();
	}
	
}