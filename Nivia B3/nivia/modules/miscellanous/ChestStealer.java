package nivia.modules.miscellanous;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

import java.util.ArrayList;
import java.util.Random;

public class ChestStealer extends Module {
	private final Timer timer = new Timer();


	public ChestStealer() {
		super("ChestStealer", 0, 0x005C00, Category.MISCELLANEOUS, "Gets all the items from a chest.",
				new String[] { "csteal", "chests", "cstealer" }, true);
	}
	public final ArrayList<Item> items = new ArrayList<Item>();
	public PropertyManager.DoubleProperty delay = new PropertyManager.DoubleProperty(this, "Delay", 130L, 0, 2000);
	public Property<Boolean> selective = new Property<Boolean>(this, "Selective", false);
	public Property<Boolean> dropper = new Property<Boolean>(this, "Dropper", false);
	public Property<Boolean> invert = new Property<Boolean>(this, "Invert Items", false);
	@EventTarget
	public void onPre(EventPreMotionUpdates e) {
		if (mc.currentScreen instanceof GuiChest) {
			GuiChest chest = (GuiChest) mc.currentScreen;
			if (isChestEmpty(chest) || Helper.playerUtils().isInventoryFull())
				mc.thePlayer.closeScreen();

			for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
				ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
				if (stack == null )
					continue;
				boolean x = invert.value == items.contains(stack.getItem());
				if(selective.value && x)
					continue;
				if (timer.hasTimeElapsed((long) ((delay.getValue() - 50) + MathHelper.getRandomDoubleInRange(new Random(), 25, 50)), false)) {
					if(dropper.value)
						mc.playerController.windowClick(chest.inventorySlots.windowId, index, 1	, 4, mc.thePlayer);
					else
						mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
					timer.reset();
				}
			}
		}
	}

	private boolean isChestEmpty(GuiChest chest) {
		for (int index = 0; index <= chest.getLowerChestInventory().getSizeInventory(); index++) {
			ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
			if (stack != null)
				return false;
		}

		return true;
	}
	public Item getItem(String message){
		Item i = null;
		try {
			i = Item.getItemById(Integer.parseInt(message));
		} catch (NumberFormatException e) {
			Item item = null;
			loop: {
				for (final Object object : Item.itemRegistry) {
					item = (Item) object;
					final String label = item.getItemStackDisplayName(new ItemStack(item)).replace(" ", "");
					if (label.toLowerCase().startsWith(message) || label.toLowerCase().contains(message)) {
						break loop;
					}
				}
			}
			if(item != null)
				i = item;
		}
		return i;
	}
	public boolean contains(Item s){

		return false;
	}
	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("ChestStealer", "Manages cstealer",
				Logger.LogExecutionFail("Option, Options:",
						new String[] { "Add", "Remove", "Clear", "Selective", "Dropper", "List" }),
				"csteal", "chests", "cstealer", "cs") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1], message2 = "";
				try { message2 = arguments[2]; } catch (Exception e) {}

				switch (message.toLowerCase()) {
					case "selective":case "s":case "select":
						selective.value = !selective.value;
						Logger.logSetMessage("ChestStealer", "Selective", selective);
						break;
					case "invert":case "i":case "inv":
						invert.value = !invert.value;
						Logger.logSetMessage("ChestStealer", "Invert", invert);
						break;
					case "dropper":case "drop":case "d":
						dropper.value = !dropper.value;
						Logger.logSetMessage("ChestStealer", "Dropper", dropper);
						break;
					case "del":
					case "delete":
					case "remove": {
						Item stack = getItem(message2);
						if (stack == null || !items.contains(stack)) {
							Logger.LogExecutionFail("Item");
							return;
						}
						items.remove(stack);
						Logger.logChat("Removed " + EnumChatFormatting.AQUA + stack.getItemStackDisplayName(new ItemStack(stack)) + EnumChatFormatting.GRAY + " from cheststealer's list.");
						break;
					}
					case "add":
					case "a":
						Item stack = getItem(message2);
						if (stack == null || items.contains(stack)) {
							Logger.LogExecutionFail("Item");
							return;
						}
						items.add(stack);
						Logger.logChat("Added " + EnumChatFormatting.AQUA + stack.getItemStackDisplayName(new ItemStack(stack)) + EnumChatFormatting.GRAY + " to cheststealer's list.");
						break;
					case "clear":
					case "empty":
					case "c":
						Logger.logChat("Cleared cheststealer's list." + items.size());
						items.clear();
						break;
					case "list":case "l":
						if(items.size() == 0) {
							Logger.logChat("No Items added");
							break;
						}
						Logger.logChat("Current items on cheststealer:");
						for(Item i : items) {
							Logger.logChat(EnumChatFormatting.GOLD + i.getItemStackDisplayName(new ItemStack(i)));
						}
						break;
					case "values":
						logValues();
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}
		});
	}

}
