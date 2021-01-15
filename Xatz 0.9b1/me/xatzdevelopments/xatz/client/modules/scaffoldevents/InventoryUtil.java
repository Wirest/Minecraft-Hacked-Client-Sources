package me.xatzdevelopments.xatz.client.modules.scaffoldevents;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class InventoryUtil {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public int itemSlot = 5;
	
	public final boolean isArmorBetter(ItemStack firstArmor, ItemStack secondArmor) {
		if (firstArmor == null || secondArmor == null) return false;
		
		final ItemArmor first = (ItemArmor) firstArmor.getItem();
		final ItemArmor second = (ItemArmor) secondArmor.getItem();
		
		return (first.damageReduceAmount + (1.25 * getEnchantment(firstArmor, Enchantment.protection))) >= (second.damageReduceAmount + (1.25 * getEnchantment(firstArmor, Enchantment.protection)));
	}
	
	public final boolean isSwordBetter(ItemStack firstSword, ItemStack secondSword) {
		if (firstSword == null || secondSword == null) return false;
		
		final ItemSword first = (ItemSword) firstSword.getItem();
		final ItemSword second = (ItemSword) secondSword.getItem();
		
		return (first.getDamageVsEntity() + (1.25 * getEnchantment(firstSword, Enchantment.sharpness))) >= (second.getDamageVsEntity() + (1.25 * getEnchantment(secondSword, Enchantment.sharpness)));
	}
	
	public final double getArmorProtection(ItemStack armor) {
		if (armor == null) return 0;
		
		final ItemArmor first = (ItemArmor) armor.getItem();
		
		return first.damageReduceAmount + (1.25 * getEnchantment(armor, Enchantment.protection));
	}
	
	public final double getSwordDamage(ItemStack sword) {
		if (sword == null) return 0;
		
		final ItemSword first = (ItemSword) sword.getItem();
		
		return first.getDamageVsEntity() + (1.25 * getEnchantment(sword, Enchantment.sharpness)) + (first.getToolMaterialName().equalsIgnoreCase("WOOD") || first.getToolMaterialName().equalsIgnoreCase("GOLD") ? .1 : 0);
	}
	
	public final boolean hasSpaceInHotbar() {
		for (int i = 36; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null) {
				return false;
			}
		}
		return true;
	}
	
	public final boolean hasBlockInHotbar() {
		return findBlock(true) != -1;
	}
	
	public final boolean hasItemInInventory(int itemId) {
		return findItemSlot(itemId) != -1;
	}
	
	/**
	 * From Liquid Bounce
	 * @param itemStack
	 * @param enchantment
	 * @return
	 */
	public final int getEnchantment(ItemStack itemStack, Enchantment enchantment) {
		if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags()) return 0;
		
		for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); i++) {
			final NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i); // Tag des Items getten
			
			if (tagCompound.hasKey("ench") && tagCompound.getShort("ench") == enchantment.effectId || tagCompound.hasKey("id") && tagCompound.getShort("id") == enchantment.effectId) { // Checken ob das item das entchantment hat
				return tagCompound.getShort("lvl"); // Das level des Entchatnments
			}
		}
		return 0;
	}
	
	public final ItemStack getArmorInSlot(int armorSlot) {
		return MC.thePlayer.inventory.armorInventory[armorSlot];
	}
	
	public final int findBlock(boolean hotbar) {
		for (int i = hotbar ? 36 : 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock) {
				final Block block = ((ItemBlock) stack.getItem()).getBlock();
				if (block.isFullBlock()) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public final int findSword() {
		for (int i = 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemSword) {
				return i;
			}
		}
		return -1;
	}
	
	public final int findItemSlot(int itemId) {
		for (int i = 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() == Item.getItemById(itemId)) {
				return i;
			}
		}
		return -1;
	}
	
	public final int findItemSlot(ItemStack item) {
		for (int i = 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() == item.getItem()) {
				return i;
			}
		}
		return -1;
	}
	
	public final int findBestTool(BlockPos breakingPos) {
		int bestTool = -1;
		double bestDamage = 1;
		
		for (int i = 0; i < 9; i++) {
			final ItemStack stack = MC.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) continue;
			final double damage = stack.getStrVsBlock(breakingPos.getBlock());
			
			if (damage > bestDamage) {
				bestDamage = damage;
				bestTool = i;
			}
		}
		
		return bestTool;
	}
	
	public final boolean isValidContainerName(GuiContainer screen) {
		return (screen.containerTitle.contains("Chest") || screen.containerTitle.contains("Large Chest") || screen.containerTitle.contains("Gro�e Truhe") || screen.containerTitle.contains("Truhe") || screen.containerTitle.contains("Lost Backpack") || screen.containerTitle.contains("Medical Supplies") || screen.containerTitle.contains("Supply Crate"));
	}

}
