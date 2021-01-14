package de.iotacb.client.module.modules.player;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.DisconnectEvent;
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
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

@ModuleInfo(name = "InventoryCleaner", description = "Auto cleans your inventory", category = Category.PLAYER)
public class InventoryCleaner extends Module {

	private Timer clickDelay, clickDelay2, clickDelay3, clickDelay4, clickDelay5;

	private final List<Item> itemsToTrash = new ArrayList<Item>();

	private long delay = 20;

	private double tempSwordDamage, tempToolDamage;

	private boolean isDoneSwords;

	@Override
	public void onInit() {
		clickDelay = new Timer();
		clickDelay2 = new Timer();
		clickDelay3 = new Timer();
		clickDelay4 = new Timer();
		clickDelay5 = new Timer();

		addValue(new Value("InventoryCleanerOpen inventory", true));
		addValue(new Value("InventoryCleanerDelay", 60, new ValueMinMax(0, 2000, 1)));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		itemsToTrash.clear();
		addTrashItems();
	}

	@EventTarget
	public void onTick(TickEvent event) {
		setSettingInfo("" + getValueByName("InventoryCleanerDelay").getNumberValue());
		if (getMc().thePlayer == null || getMc().theWorld == null || (getValueByName("InventoryCleanerOpen inventory").getBooleanValue() && !(getMc().currentScreen instanceof GuiInventory)))
			return;
		final AutoArmor autoArmor = (AutoArmor) Client.INSTANCE.getModuleManager().getModuleByClass(AutoArmor.class);
		if (autoArmor.isEnabled()) {
			if (autoArmor.runs < 4)
				return;
		}
		final long longyDelay = (long) getValueByName("InventoryCleanerDelay").getNumberValue();

		int slotToTrash = -1;
		for (int i = 9; i < 45;) {
			final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null) {
				if (isTrash(Item.getIdFromItem(stack.getItem()))) {
					slotToTrash = i;
				} else {
					if (stack.getItem() instanceof ItemSword) {
						final ItemSword sword = ((ItemSword) stack.getItem());
						if (Client.INVENTORY_UTIL.getSwordDamage(stack) >= tempSwordDamage) {
							if (Client.INVENTORY_UTIL.getSwordDamage(stack) == tempSwordDamage) {
								if (amountOfSwords() > 1) {
									if (i < 36) {
										slotToTrash = i;
									}
								}
							} else {
								tempSwordDamage = Client.INVENTORY_UTIL.getSwordDamage(stack);
							}
						} else {
							slotToTrash = i;
							break;
						}
//						if (amountOfSwords() == 1 && getValueByName("InventoryCleanerSort inventory").getBooleanValue()) {
//							if (clickDelay2.delay(longyDelay)) {
//								if (getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerSword slot").getNumberValue() - 1).getStack() == null || !(getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerSword slot").getNumberValue() - 1).getStack().getItem() instanceof ItemSword)) {
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 0, 2, getMc().thePlayer);
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 36 + (int) getValueByName("InventoryCleanerSword slot").getNumberValue() - 1, 0, 2, getMc().thePlayer);
//									return;
//								}
//							}
//						}
					}
					if (stack.getItem() instanceof ItemTool) {
						final ItemTool tool = ((ItemTool) stack.getItem());
						final ToolType type = getTypeOfTool(stack);
						if (amountOfTools(type) > 1) {
							if (tool.getMaxDamage() > tempToolDamage) {
								tempToolDamage = tool.getMaxDamage();
							} else {
								if (i < 36) {
									slotToTrash = i;
								}
								break;
							}
						}
						if (clickDelay3.delay(longyDelay + 50)) {
//							if (amountOfTools(ToolType.AXE) == 1 && getValueByName("InventoryCleanerSort inventory").getBooleanValue()) {
//								if (getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerAxe slot").getNumberValue() - 1).getStack() == null || !(getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerAxe slot").getNumberValue() - 1).getStack().getItem() instanceof ItemTool)) {
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 0, 2, getMc().thePlayer);
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 36 + (int) getValueByName("InventoryCleanerAxe slot").getNumberValue() - 1, 0, 2, getMc().thePlayer);
//									return;
//								}
//							}
						}
						if (clickDelay4.delay(longyDelay + 100)) {
//							if (amountOfTools(ToolType.PICKAXE) == 1 && getValueByName("InventoryCleanerSort inventory").getBooleanValue()) {
//								if (getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerPickAxe slot").getNumberValue() - 1).getStack() == null || !(getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerPickAxe slot").getNumberValue() - 1).getStack().getItem() instanceof ItemTool)) {
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 0, 2, getMc().thePlayer);
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 36 + (int) getValueByName("InventoryCleanerPickAxe slot").getNumberValue() - 1, 0, 2, getMc().thePlayer);
//									return;
//								}
//							}
						}
					}
					if (stack.getItem() instanceof ItemBow) {
						final ItemBow bow = ((ItemBow) stack.getItem());
						if (amountOfBows() > 1) {
							if (i < 36) {
								slotToTrash = i;
							}
						}
						if (clickDelay5.delay(longyDelay + 150)) {
//							if (amountOfBows() == 1 && getValueByName("InventoryCleanerSort inventory").getBooleanValue()) {
//								if (getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerBow slot").getNumberValue() - 1).getStack() == null || !(getMc().thePlayer.inventoryContainer.getSlot(36 + (int) getValueByName("InventoryCleanerBow slot").getNumberValue() - 1).getStack().getItem() instanceof ItemBow)) {
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 0, 2, getMc().thePlayer);
//									getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, 36 + (int) getValueByName("InventoryCleanerBow slot").getNumberValue() - 1, 0, 2, getMc().thePlayer);
//									return;
//								}
//							}
						}
					}
					if (stack.getItem() instanceof ItemFishingRod) {
						final ItemFishingRod bow = ((ItemFishingRod) stack.getItem());
						if (amountOfRods() > 1) {
							if (i < 36) {
								slotToTrash = i;
							}
						}
					}
					if (stack.getItem() instanceof ItemArmor) {
						final ItemArmor armor = ((ItemArmor) stack.getItem());

						switch (armor.armorType) {
						case 0:
							if (Client.INVENTORY_UTIL.getArmorInSlot(3) != null) {
								final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(3);
								final ItemStack armorToCheck = stack;
								if (!Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
									slotToTrash = i;
								}
							}
							break;

						case 1:
							if (Client.INVENTORY_UTIL.getArmorInSlot(2) != null) {
								final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(2);
								final ItemStack armorToCheck = stack;
								if (!Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
									slotToTrash = i;
								}
							}
							break;

						case 2:
							if (Client.INVENTORY_UTIL.getArmorInSlot(1) != null) {
								final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(1);
								final ItemStack armorToCheck = stack;
								if (!Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
									slotToTrash = i;
								}
							}
							break;

						case 3:
							if (Client.INVENTORY_UTIL.getArmorInSlot(0) != null) {
								final ItemStack equippedArmor = Client.INVENTORY_UTIL.getArmorInSlot(0);
								final ItemStack armorToCheck = stack;
								if (!Client.INVENTORY_UTIL.isArmorBetter(armorToCheck, equippedArmor)) {
									slotToTrash = i;
								}
							}
							break;
						}
					}
				}
			}
			i++;
		}

		if (slotToTrash == -1)
			return;

		if (clickDelay.delay(longyDelay)) {
			getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, slotToTrash, 1, 4, getMc().thePlayer);
			tempSwordDamage = -1;
			tempToolDamage = -1;
		}
	}

	@EventTarget
	public void onGuiClose(GuiCloseEvent event) {
		tempSwordDamage = -1;
		tempToolDamage = -1;
	}

	@EventTarget
	public void onDisconnect(DisconnectEvent event) {
		tempSwordDamage = -1;
		tempToolDamage = -1;
	}

	private boolean isTrash(int itemId) {
		for (Item item : itemsToTrash) {
			if (item.getIdFromItem(item) == itemId) {
				return true;
			}
		}
		return false;
	}

	private int findItemSlot(int itemId) {
		for (int i = 9; i < 45; i++) {
			final ItemStack item = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == itemId) {
				return i;
			}
		}
		return -1;
	}

	private void addTrashItems() {
		itemsToTrash.add(Items.string);
		itemsToTrash.add(Items.wheat_seeds);
		itemsToTrash.add(Items.bowl);
		itemsToTrash.add(Items.cake);
		itemsToTrash.add(Items.blaze_rod);
		itemsToTrash.add(Items.banner);
		itemsToTrash.add(Items.coal);
		itemsToTrash.add(Items.dye);
		itemsToTrash.add(Items.fireworks);
		itemsToTrash.add(Items.flint);
		itemsToTrash.add(Items.gold_nugget);
		itemsToTrash.add(Items.wheat);
		itemsToTrash.add(Items.spider_eye);
		itemsToTrash.add(Items.fermented_spider_eye);
		itemsToTrash.add(Items.melon_seeds);
		itemsToTrash.add(Items.bone);
		itemsToTrash.add(Items.leather);
		itemsToTrash.add(Items.feather);
		itemsToTrash.add(Items.chicken);
		itemsToTrash.add(Items.shears);
		itemsToTrash.add(Items.bucket);
		itemsToTrash.add(Items.experience_bottle);
		itemsToTrash.add(Items.compass);
		itemsToTrash.add(Item.getItemFromBlock(Blocks.chest));
	}

	private boolean isArmorBetter(int armorSlot, int itemId) {
		final ItemStack armorEquipped = getMc().thePlayer.inventory.armorInventory[armorSlot];
		final ItemStack itemToCheck = new ItemStack(Item.getItemById(itemId));

		if (armorEquipped != null && itemToCheck != null) {
			final ItemArmor oldArmor = (ItemArmor) armorEquipped.getItem();
			final ItemArmor newArmor = (ItemArmor) itemToCheck.getItem();
			if (newArmor.damageReduceAmount > oldArmor.damageReduceAmount) {
				return true;
			}
		}

		return false;
	}

	private int amountOfSwords() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			final ItemStack item = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (item != null && item.getItem() instanceof ItemSword) {
				amount++;
			}
		}
		return amount;
	}

	private int amountOfTools(ToolType toolType) {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			final ItemStack item = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (item != null && item.getItem() instanceof ItemTool) {
				if (getTypeOfTool(item) == toolType) {
					amount++;
				}
			}
		}
		return amount;
	}

	private int amountOfBows() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			final ItemStack item = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (item != null && item.getItem() instanceof ItemBow) {
				amount++;
			}
		}
		return amount;
	}

	private int amountOfRods() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			final ItemStack item = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (item != null && item.getItem() instanceof ItemFishingRod) {
				amount++;
			}
		}
		return amount;
	}

	private ToolType getTypeOfTool(ItemStack stack) {
		if (stack.getItem() instanceof ItemPickaxe) {
			return ToolType.PICKAXE;
		} else if (stack.getItem() instanceof ItemAxe) {
			return ToolType.AXE;
		} else if (stack.getItem() instanceof ItemSpade) {
			return ToolType.SPADE;
		}
		return null;
	}

	public enum ToolType {
		PICKAXE, AXE, SPADE;
	}

}
