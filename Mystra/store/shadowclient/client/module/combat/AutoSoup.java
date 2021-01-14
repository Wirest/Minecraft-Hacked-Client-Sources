package store.shadowclient.client.module.combat;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventPreMotionUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoSoup extends Module {
	private TimeHelper time = new TimeHelper();

	public AutoSoup() {
		super("AutoSoup", 0, Category.COMBAT);

		Shadow.instance.settingsManager.rSetting(new Setting("Health", this, 10, 0, 20, true));
	}
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth() * 20 < Shadow.instance.settingsManager.getSettingByName("Health").getValDouble()) {
            if (time.hasReached(50)) {
                if (hotbarHasSoups()) {
                    useSoup();
                } else {
                    getSoupFromInventory();
                    time.reset();
                }
            }
        }
	}
	 private boolean hotbarHasSoups() {
	        for (int index = 36; index < 45; index++) {
	            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if (itemStack != null) {
	                if (itemStack.getItem() instanceof ItemSoup) {
	                    return true;
	                }
	            }
	        }

	        return false;
	    }

	    private int countSoups() {
	        int items = 0;
	        for (int index = 9; index < 45; index++) {
	            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if (itemStack != null) {
	                if (itemStack.getItem() instanceof ItemSoup) {
	                    items += itemStack.stackSize;
	                }
	            }
	        }

	        return items;
	    }

	    private void getSoupFromInventory() {
	        int item = -1;
	        boolean found = false;
	        for (int index = 36; index >= 9; index--) {
	            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if (itemStack != null) {
	                if (itemStack.getItem() instanceof ItemSoup) {
	                    item = index;
	                    found = true;
	                    break;
	                }
	            }
	        }
	        if (found) {
	            for (int index = 0; index < 45; index++) {
	                ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	                if (itemStack != null) {
	                    if ((itemStack.getItem() == Items.bowl) && (index >= 36) && (index <= 44)) {
	                        mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
	                        mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
	                    }
	                    break;
	                }
	            }
	            mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
	        }
	    }

	    private void useSoup() {
	        int item = -1;
	        boolean found = false;
	        for (int index = 36; index < 45; index++) {
	            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	            if (itemStack != null) {
	                if (itemStack.getItem() instanceof ItemSoup) {
	                    item = index;
	                    found = true;
	                    break;
	                }
	            }
	        }
	        if (found) {
	            for (int index = 0; index < 45; index++) {
	                ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
	                if (itemStack != null) {
	                    if ((itemStack.getItem() == Items.bowl) && (index >= 36) && (index <= 44)) {
	                        mc.playerController.windowClick(0, index, 0, 0, mc.thePlayer);
	                        mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
	                    }
	                }
	            }
	            int slot = mc.thePlayer.inventory.currentItem;
	            mc.thePlayer.inventory.currentItem = (item - 36);
	            mc.playerController.updateController();
	            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
	            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
	            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	            mc.thePlayer.stopUsingItem();

	            mc.thePlayer.inventory.currentItem = slot;
	        }
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
