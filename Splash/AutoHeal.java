package splash.client.modules.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice Created: 17:52, 30-May-20 Project: Client
 */
public class AutoHeal extends Module {
	private static boolean doThrow;
	int slot;
	private final Stopwatch timer = new Stopwatch();

	public AutoHeal() {
		super("AutoHeal", "Auto throws pots or soups", ModuleCategory.COMBAT);
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() - 10.0f && timer.delay(50.0f)) {
			if (hotbarHasSoups()) {
				soup();
			} else {
				getSoupFromInventory();
				timer.reset();
			}
			int j;
			ItemStack stack;
			if (mc.thePlayer.getHealth() >= 9.0 || mc.thePlayer.capabilities.isCreativeMode) {
				return;
			}
			if (eventPlayerUpdate.getStage().equals(Stage.POST) && doThrow) {
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(
						new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90, mc.thePlayer.onGround));
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
				mc.thePlayer.sendQueue
						.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(slot)));
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				doThrow = false;
			}
			if (!timer.delay(500.0f)) {
				return;
			}
			slot = -1;
			boolean hasPots = false;
			for (j = 0; j < 9; ++j) {
				stack = mc.thePlayer.inventory.getStackInSlot(j);
				if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
						|| !isValidPotion((ItemPotion) stack.getItem(), stack)) {
					continue;
				}
				hasPots = true;
			}
			if (!hasPots) {
				for (j = 9; j < 36; ++j) {
					stack = mc.thePlayer.inventoryContainer.getSlot(j).getStack();
					if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
							|| !isValidPotion(stack)) {
						continue;
					}
					mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, j, 1, 2, mc.thePlayer);
					break;
				}
			}
			for (j = 0; j < 9; ++j) {
				stack = mc.thePlayer.inventory.getStackInSlot(j);
				if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemPotion)
						|| !isValidPotion((ItemPotion) stack.getItem(), stack)) {
					continue;
				}
				slot = j;
				break;
			}
			if (slot == -1) {
				return;
			}
			timer.reset();
			doThrow = true;
			eventPlayerUpdate.setPitch(90.0f);
		}
	}

	public boolean isValidPotion(final ItemPotion potion, final ItemStack stack) {
		if (ItemPotion.isSplash(stack.getItemDamage())) {
			for (final Object o : potion.getEffects(stack)) {
				if (!(o instanceof PotionEffect) || ((PotionEffect) o).getPotionID() != Potion.heal.id) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	public boolean isValidPotion(final ItemStack stack) {
		return isValidPotion((ItemPotion) stack.getItem(), stack);
	}

	public static boolean isPotting() {
		return doThrow;
	}

	private boolean hotbarHasSoups() {
		for (int index = 36; index < 45; ++index) {
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
				continue;
			}
			return true;
		}
		return false;
	}

	private void getSoupFromInventory() {
		int index;
		ItemStack itemStack;
		int item = -1;
		boolean found = false;
		for (index = 36; index >= 9; --index) {
			itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
				continue;
			}
			item = index;
			found = true;
			break;
		}
		if (found) {
			for (index = 0; index < 45; ++index) {
				itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
				if (itemStack == null) {
					continue;
				}
				if (itemStack.getItem() != Items.bowl || index < 36 || index > 44) {
					break;
				}
				mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
				mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
				break;
			}
			mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
		}
	}

	private void soup() {
		int index;
		ItemStack itemStack;
		int item = -1;
		boolean found = false;
		for (index = 36; index < 45; ++index) {
			itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (itemStack == null || !(itemStack.getItem() instanceof ItemSoup)) {
				continue;
			}
			item = index;
			found = true;
			break;
		}
		if (found) {
			for (index = 0; index < 45; ++index) {
				itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
				if (itemStack == null || itemStack.getItem() != Items.bowl || index < 36 || index > 44) {
					continue;
				}
				mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
				mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
			}
			final int slot = mc.thePlayer.inventory.currentItem;
			mc.thePlayer.inventory.currentItem = item - 36;
			mc.playerController.updateController();
			mc.thePlayer.sendQueue
					.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			mc.thePlayer.stopUsingItem();
			mc.thePlayer.inventory.currentItem = slot;
		}
	}
}
