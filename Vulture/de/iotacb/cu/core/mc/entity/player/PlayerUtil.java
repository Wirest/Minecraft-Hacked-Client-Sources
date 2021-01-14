package de.iotacb.cu.core.mc.entity.player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.iotacb.cu.core.mc.entity.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class PlayerUtil {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	public static final PlayerUtil INSTANCE = new PlayerUtil();

	/**
	 * Returns true if the player is moving
	 * @return
	 */
	public final boolean isMoving() {
		return MC.thePlayer.movementInput.moveForward != 0 || MC.thePlayer.movementInput.moveStrafe != 0;
	}
	
	/**
	 * Return the nearest entity to the player
	 * @return
	 */
	public final Entity getNearestEntity() {
		return EntityUtil.INSTANCE.getNearestEntity(MC.thePlayer);
	}
	
	/**
	 * Returns the slot of an item if it is found in the inventory using the given item id
	 * @param itemId
	 * @param hotbar
	 * @return
	 */
	public final int findItemSlotInInventory(final int itemId, final boolean hotbar) {
		for (int i = hotbar ? 36 : 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null) {
				if (Item.getIdFromItem(stack.getItem()) == itemId) {
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Returns the slot of a block if a block is found in the inventory
	 * @param hotbar
	 * @return
	 */
	public final int findBlockSlotInInventory(final boolean hotbar) {
		for (int i = hotbar ? 36 : 9; i < 45; i++) {
			final ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null) {
				if (stack.getItem() instanceof ItemBlock) {
					final Block block = ((ItemBlock) stack.getItem()).getBlock();
					if (block.isFullBlock()) return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Returns true if a block is found in the inventory
	 * @param hotbar
	 * @return
	 */
	public boolean hasBlockInInventory(final boolean hotbar) {
		return findBlockSlotInInventory(hotbar) != -1;
	}
	
	/**
	 * Return true if an item is found in the inventory using the given item id
	 * @param itemId
	 * @param hotbar
	 * @return
	 */
	public boolean hasItemInInventory(final int itemId, final boolean hotbar) {
		return findItemSlotInInventory(itemId, hotbar) != -1;
	}
	
	/**
	 * Adds a client side message to the chat
	 * @param message
	 */
	public final void sendChatMessage(final String message) {
		MC.thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
}
