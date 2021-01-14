package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.math.MathUtils;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 21:44, 13-Jun-20
 * Project: Client
 */
public class ChestStealer extends Module {

    private Stopwatch stopwatch = new Stopwatch();
    public BooleanValue<Boolean> ignoreCustomName = new BooleanValue<>("Ignore Custom Name", true, this);
    public NumberValue<Integer> chestStealDelayValue = new NumberValue<>("Steal Delay", 500, 1, 1000, this);
    private int exploitTime;
    private boolean contains;
    public boolean stealing;

    public ChestStealer() {
        super("ChestStealer", "Steals from chests.", ModuleCategory.PLAYER);
    }

    @Collect
    public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
        if(mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest && !(mc.currentScreen instanceof GuiChat)) {
            if(mc.thePlayer.openContainer instanceof ContainerChest && mc.currentScreen instanceof GuiChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            	if (ignoreCustomName.getValue() && !chest.getLowerChestInventory().getName().toLowerCase().contains("chest")) {

                	stealing = false;
                	return;
            	}
                if(!isContainerEmpty(chest)) {
                	stealing = true;
                    int slotID = getNextSlot(chest);
                    if(stopwatch.delay(chestStealDelayValue.getValue())) {
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slotID, 0, 1, mc.thePlayer);
                        if (exploitTime++ > 4) {
                            exploitTime = 0;
                            mc.playerController.windowClick(((ContainerChest) mc.thePlayer.openContainer).windowId, (int) MathUtils.getRandomInRange(11, 17), 0, 1, mc.thePlayer);
                        }
                        stopwatch.reset();
                    }
                } else {
                    mc.thePlayer.closeScreen();
                	stealing = false;
                }
            }
        } else {
        	stealing = false;
        }
    }

    private int getNextSlot(Container container)
    {
        int i = 0;
        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 35; i < slotAmount; i++) {
            if (container.getInventory().get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    public boolean isContainerEmpty(Container container)
    {
        boolean temp = true;
        int i = 0;
        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 35; i < slotAmount; i++) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
}
