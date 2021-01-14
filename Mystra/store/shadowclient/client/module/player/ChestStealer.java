package store.shadowclient.client.module.player;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class ChestStealer extends Module{
	private TimeHelper timer = new TimeHelper();

	public ChestStealer() {
		super("ChestStealer", 0, Category.PLAYER);
		
		Shadow.instance.settingsManager.rSetting(new Setting("CS Delay", this, 50, 0, 1000, false));
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(!this.isToggled()) {
			return;
		}
		if((this.mc.thePlayer.openContainer != null) && ((this.mc.thePlayer.openContainer instanceof ContainerChest))) {
	        ContainerChest chest = (ContainerChest) this.mc.thePlayer.openContainer;
	        for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
	            if((chest.getLowerChestInventory().getStackInSlot(i) != null) && (this.timer.hasReached(Shadow.instance.settingsManager.getSettingByName("CS Delay").getValDouble()))) {
	                this.mc.playerController.windowClick(chest.windowId, i, 0, 1, this.mc.thePlayer);
	                this.timer.reset();
	            }
	        }
	        if(chest.getInventory().isEmpty()) {
	            this.mc.displayGuiScreen(null);
	        }
	    }
	}
	
	public static boolean isFullInv() {
        for (int i = 9; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || itemStack.getUnlocalizedName().contains("air")) {
                return false;
            }
        }
        return true;
    }
	
	@EventTarget
    private void onTick(final ClientTickEvent event) {
        if (mc.theWorld == null || isFullInv()) {
            return;
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
