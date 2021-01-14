package de.iotacb.client.module.modules.player;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.GuiCloseEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.InventoryUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "ChestStealer", description = "Auto loots chests", category = Category.PLAYER)
public class ChestStealer extends Module {
	
	private Timer clickDelay, openingDelay;
	private int stealy;
	
	private final List<Item> itemsToTrash = new ArrayList<Item>();
	
	@Override
	public void onInit() {
		this.clickDelay = new Timer();
		this.openingDelay = new Timer();
		
		addValue(new Value("ChestStealerClose screen", true));
		addValue(new Value("ChestStealerCheck name", true));
		addValue(new Value("ChestStealerDelay", 60, new ValueMinMax(0, 2000, 1)));
		addValue(new Value("ChestStealerOpening delay", 200, new ValueMinMax(0, 1000, 1)));
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
		if (getMc().thePlayer == null) return;
		if (stealy < 4 && getValueByName("ChestStealerOpening delay").getNumberValue() != 0) {
			if (openingDelay.delay((long)getValueByName("ChestStealerOpening delay").getNumberValue())) {
				stealy++;
			}
		}
		if (stealy < 4 && getValueByName("ChestStealerOpening delay").getNumberValue() != 0) return;
		setSettingInfo(""+getValueByName("ChestStealerDelay").getNumberValue());
		if (!(getMc().currentScreen instanceof GuiChest) || (getValueByName("ChestStealerCheck name").getBooleanValue() && !Client.INVENTORY_UTIL.isValidContainerName(((GuiChest) getMc().currentScreen)))) return;
		
		final GuiChest chest = (GuiChest) getMc().currentScreen;
		
		if (chest.inventorySlots == null) return;
		
		for (int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; i++) {
			final ItemStack item = chest.inventorySlots.getSlot(i).getStack();
			if (item != null) {
				
				if (isTrash(Item.getIdFromItem(item.getItem()))) continue;
				
				if (clickDelay.delay((long) getValueByName("ChestStealerDelay").getNumberValue())) {
					getMc().playerController.windowClick(chest.inventorySlots.windowId, i, 0, 1, getMc().thePlayer);
				}
			}
		}
		if (isEmpty(chest) && getValueByName("ChestStealerClose screen").getBooleanValue())
			getMc().thePlayer.closeScreen();
	}
	
	@EventTarget
	public void onGuiClose(GuiCloseEvent event) {
		if (event.getScreen() instanceof GuiContainer) {
			stealy = 0;
			openingDelay.reset();
		}
	}
	
	private boolean isEmpty(GuiChest chest) {
		for (int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; i++) {
			final ItemStack item = chest.inventorySlots.getSlot(i).getStack();
			if (item != null) {
				if (isTrash(Item.getIdFromItem(item.getItem()))) continue;
				return false;
			}
		}
		return true;
	}
	
	private boolean isTrash(int itemId) {
		return itemsToTrash.stream().filter(item -> Item.getIdFromItem(item) == itemId).findFirst().orElse(null) != null;
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

}
