package store.shadowclient.client.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InventoryHelper extends Module {

	private TimeHelper timer = new TimeHelper();
	private int[] bestArmorDamageReducement;
	private int[] bestArmorSlots;
	private float bestSwordDamage;
	private int bestSwordSlot;
	private List<Integer> trash = new ArrayList<>();
	
	public InventoryHelper() {
		super("InvCleaner", 0, Category.PLAYER);
		
		Shadow.instance.settingsManager.rSetting(new Setting("InvCleaner Delay", this, 135, 0.0, 500, true));
	}
	
	@EventTarget
	public void onPreMotion(EventUpdate event) {
		if (mc.thePlayer.isUsingItem())
			return;
		if (isToggled()) {
				CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList<Integer>();
				if(timer.hasReached((long) Shadow.instance.settingsManager.getSettingByName("InvCleaner Delay").getValDouble())){
				for (int o = 0; o < 45; ++o) {
					if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
						ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
						if (mc.thePlayer.inventory.armorItemInSlot(0) == item
								|| mc.thePlayer.inventory.armorItemInSlot(1) == item
								|| mc.thePlayer.inventory.armorItemInSlot(2) == item
								|| mc.thePlayer.inventory.armorItemInSlot(3) == item)
							continue;
						if (item != null && item.getItem() != null && Item.getIdFromItem(item.getItem()) != 0
								&& !stackIsUseful(o)) {
							uselessItems.add(o);
						}
					}
				}
				if (!uselessItems.isEmpty()) {
					mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId,
							uselessItems.get(0), 1, 4, mc.thePlayer);
					uselessItems.remove(0);
					timer.reset();
				}
			}
		}
	}
	private boolean stackIsUseful(int i) {
		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
		boolean hasAlreadyOrBetter = false;
		if (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemPickaxe
				|| itemStack.getItem() instanceof ItemAxe) {
			for (int o = 0; o < 45; ++o) {
				if (o == i)
					continue;
				if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
					ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
					if (item != null && item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
							|| item.getItem() instanceof ItemPickaxe) {
					}
				}
			}
		} else if (itemStack.getItem() instanceof ItemArmor) {
			for (int o = 0; o < 45; ++o) {
				if (i == o)
					continue;
				if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
					ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
					if (item != null && item.getItem() instanceof ItemArmor) {
						List<Integer> helmet = Arrays.asList(298, 314, 302, 306, 310);
						List<Integer> chestplate = Arrays.asList(299, 315, 303, 307, 311);
						List<Integer> leggings = Arrays.asList(300, 316, 304, 308, 312);
						List<Integer> boots = Arrays.asList(301, 317, 305, 309, 313);
						if (helmet.contains(Item.getIdFromItem(item.getItem()))
								&& helmet.contains(Item.getIdFromItem(itemStack.getItem()))) {
							if (helmet.indexOf(Item.getIdFromItem(itemStack.getItem())) < helmet
									.indexOf(Item.getIdFromItem(item.getItem()))) {
								hasAlreadyOrBetter = true;
								break;
							}
						} else if (chestplate.contains(Item.getIdFromItem(item.getItem()))
								&& chestplate.contains(Item.getIdFromItem(itemStack.getItem()))) {
							if (chestplate.indexOf(Item.getIdFromItem(itemStack.getItem())) < chestplate
									.indexOf(Item.getIdFromItem(item.getItem()))) {
								hasAlreadyOrBetter = true;
								break;
							}
						} else if (leggings.contains(Item.getIdFromItem(item.getItem()))
								&& leggings.contains(Item.getIdFromItem(itemStack.getItem()))) {
							if (leggings.indexOf(Item.getIdFromItem(itemStack.getItem())) < leggings
									.indexOf(Item.getIdFromItem(item.getItem()))) {
								hasAlreadyOrBetter = true;
								break;
							}
						} else if (boots.contains(Item.getIdFromItem(item.getItem()))
								&& boots.contains(Item.getIdFromItem(itemStack.getItem()))) {
							if (boots.indexOf(Item.getIdFromItem(itemStack.getItem())) < boots
									.indexOf(Item.getIdFromItem(item.getItem()))) {
								hasAlreadyOrBetter = true;
								break;
							}
						}
					}
				}
			}
		}
		for (int o = 0; o < 45; ++o) {
			if (i == o)
				continue;
			if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
				ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
				if (item != null && (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
						|| item.getItem() instanceof ItemBow || item.getItem() instanceof ItemFishingRod
						|| item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemAxe
						|| item.getItem() instanceof ItemPickaxe || Item.getIdFromItem(item.getItem()) == 346)) {
					Item found = (Item) item.getItem();
					if (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())) {
						hasAlreadyOrBetter = true;
						break;
					}
				}
			}
		}
		if (Item.getIdFromItem(itemStack.getItem()) == 367)
			return false; // rotten flesh
		if (Item.getIdFromItem(itemStack.getItem()) == 30)
			return true; // cobweb
		if (Item.getIdFromItem(itemStack.getItem()) == 259)
			return true; // flint & steel
		if (Item.getIdFromItem(itemStack.getItem()) == 262)
			return true; // arrow
		if (Item.getIdFromItem(itemStack.getItem()) == 264)
			return true; // diamond
		if (Item.getIdFromItem(itemStack.getItem()) == 265)
			return true; // iron
		if (Item.getIdFromItem(itemStack.getItem()) == 346)
			return true; // fishing rod
		if (Item.getIdFromItem(itemStack.getItem()) == 384)
			return true; // bottle o' enchanting
		if (Item.getIdFromItem(itemStack.getItem()) == 345)
			return true; // compass
		if (Item.getIdFromItem(itemStack.getItem()) == 296)
			return true; // wheat
		if (Item.getIdFromItem(itemStack.getItem()) == 336)
			return true; // brick
		if (Item.getIdFromItem(itemStack.getItem()) == 266)
			return true; // gold ingot
		if (Item.getIdFromItem(itemStack.getItem()) == 280)
			return true; // stick
		if (itemStack.hasDisplayName())
			return true;
		if (hasAlreadyOrBetter) {
			return false;
		}
		if (itemStack.getItem() instanceof ItemArmor)
			return true;
		if (itemStack.getItem() instanceof ItemAxe)
			return true;
		if (itemStack.getItem() instanceof ItemBow)
			return true;
		if (itemStack.getItem() instanceof ItemSword)
			return true;
		if (itemStack.getItem() instanceof ItemPotion)
			return true;
		if (itemStack.getItem() instanceof ItemFlintAndSteel)
			return true;
		if (itemStack.getItem() instanceof ItemEnderPearl)
			return true;
		if (itemStack.getItem() instanceof ItemBlock)
			return true;
		if (itemStack.getItem() instanceof ItemFood)
			return true;
		if (itemStack.getItem() instanceof ItemPickaxe)
			return true;
		return false;
	}
	
	
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

}
