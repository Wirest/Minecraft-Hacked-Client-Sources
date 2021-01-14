package cheatware.module.player;

import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import de.Hero.settings.Setting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class ChestSteal extends Module {

    public ChestSteal() {
        super("ChestSteal", Keyboard.KEY_V, Category.PLAYER);
    }
    
    int slot;
    int delay;

    @Override
    public void setup() {
    	Cheatware.instance.settingsManager.rSetting(new Setting("ChestStealer Delay", this, 0, 0, 50, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	 if(mc.currentScreen instanceof GuiChest) {
             ContainerChest kiste = (ContainerChest)mc.thePlayer.openContainer;
             
             delay++;
             slot++;
             
             if(slot > kiste.getLowerChestInventory().getSizeInventory()) {
                 slot = 0;
             }
             
             if(delay > Cheatware.instance.settingsManager.getSettingByName("ChestStealer Delay").getValDouble() && kiste.getLowerChestInventory().getStackInSlot(slot) != null) {
                 mc.playerController.windowClick(kiste.windowId, slot, 0, 1, mc.thePlayer);
                 delay = 0;
             }
         }
    }
}
