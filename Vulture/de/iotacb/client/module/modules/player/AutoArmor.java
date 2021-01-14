package de.iotacb.client.module.modules.player;

import java.util.Arrays;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.GuiCloseEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.InventoryUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoArmor", description = "Auto equips armor", category = Category.PLAYER)
public class AutoArmor extends Module {
	
	private Timer clickDelay;
	
	private int itemIds[][];
	
	public int runs;
	
	@Override
	public void onInit() {
		this.clickDelay = new Timer();
		
		itemIds = new int[][] { new int[] {
								301, 305, 309, 313, 317 // Boots
							  }, new int[] {
								300, 304, 308, 312, 316 // Leggings
							  }, new int[] {
								299, 303, 307, 311, 315 // Chestplate
							  }, new int[] {
								298, 302, 306, 310, 314 // Helmet
							  }};
							  
		addValue(new Value("AutoArmorOpen inventory", true));
		addValue(new Value("AutoArmorDelay", 60, new ValueMinMax(0, 2000, 1)));
	}

	@Override
	public void onEnable() {
		runs = 0;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		setSettingInfo(""+getValueByName("AutoArmorDelay").getNumberValue());
		if (getMc().thePlayer == null || getMc().theWorld == null || (getValueByName("AutoArmorOpen inventory").getBooleanValue() && !(getMc().currentScreen instanceof GuiInventory))) return;
		
		int itemSlotToEquip = -1;
		int slotToTrash = -1;
		
		if (runs == 4) return;
		
		for (int i = 9; i < 45;) {
			final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null) {
				if (stack.getItem() instanceof ItemArmor) {
					final ItemArmor armor = ((ItemArmor) stack.getItem());
					switch (armor.armorType) {
					case 0:
						if (Client.INVENTORY_UTIL.getArmorInSlot(3) != null) {
							final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(3);
							final ItemStack armorToCheck = stack;
							if (Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
								slotToTrash = 5;
							}
						} else {
							itemSlotToEquip = i;
						}
						break;

					case 1:
						if (Client.INVENTORY_UTIL.getArmorInSlot(2) != null) {
							final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(2);
							final ItemStack armorToCheck = stack;
							if (Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
								slotToTrash = 6;
							}
						} else {
							itemSlotToEquip = i;
						}
						break;

					case 2:
						if (Client.INVENTORY_UTIL.getArmorInSlot(1) != null) {
							final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(1);
							final ItemStack armorToCheck = stack;
							if (Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
								slotToTrash = 7;
							}
						} else {
							itemSlotToEquip = i;
						}
						break;

					case 3:
						if (Client.INVENTORY_UTIL.getArmorInSlot(0) != null) {
							final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(0);
							final ItemStack armorToCheck = stack;
							if (Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
								slotToTrash = 8;
							}
						} else {
							itemSlotToEquip = i;
						}
						break;
					}
				}
			}
			i++;
			runs++;
		}
		
		if (itemSlotToEquip != -1) {
			if (clickDelay.delay((long) getValueByName("AutoArmorDelay").getNumberValue())) {
				getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, itemSlotToEquip, 0, 1, getMc().thePlayer);
			}
		}
		
		if (slotToTrash != -1) {
			if (clickDelay.delay((long) getValueByName("AutoArmorDelay").getNumberValue())) {
				getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, slotToTrash, 1, 4,
						getMc().thePlayer);
			}
		}
	}
	
	@EventTarget
	public void onGuiClose(GuiCloseEvent event) {
		runs = 0;
	}
	
	private boolean isArmorSlotEmpty(int slot) {
		return getMc().thePlayer.inventory.armorInventory[slot] == null;
	}
	
	private int amountOfFilledSlots() {
		int amount = 0;
		for (int i = 0; i < 45; i++) {
			if (getMc().thePlayer.inventoryContainer.getSlot(i).getStack() != null) {
				amount++;
			}
		}
		return amount;
	}
	
	private int remapSlotIds(int oldSlotId) {
		int newSlotId = -1;
		if (oldSlotId == 3) {
			newSlotId = 5;
		} else if (oldSlotId == 2) {
			newSlotId = 6;
		} else if (oldSlotId == 1) {
			newSlotId = 7;
		} else if (oldSlotId == 0) {
			newSlotId = 8;
		}
		return newSlotId;
	}

}
