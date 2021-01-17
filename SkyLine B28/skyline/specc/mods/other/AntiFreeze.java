package skyline.specc.mods.other;
import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S30PacketWindowItems;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.helper.Player;

public class AntiFreeze extends Module {

	public AntiFreeze() {
		super(new ModData("AntiFreeze", 0, new Color(155, 110, 220)), ModType.PLAYER);
	}

@Override
public void onEnable() {

}

@Override
public void onDisable() {
	
}

@EventListener
public boolean onUpdate(EventPlayerUpdate e) {
   if (mc.currentScreen instanceof GuiChest &&
       GuiChest.lowerChestInventory.getName().contains("Freeze") 
       && GuiChest.lowerChestInventory.getName().contains("Screenshare") 
       && GuiChest.lowerChestInventory.getName().contains("Frozen")
       && GuiChest.lowerChestInventory.getName().contains("TeamSpeak")
       && GuiChest.lowerChestInventory.getName().contains("You are Frozen")
       && GuiChest.lowerChestInventory.getName().contains("Froze")
       && GuiChest.lowerChestInventory.getName().contains("Join TS")
       && GuiChest.lowerChestInventory.getName().contains("Youve been caught")) {
	   mc.displayGuiScreen(null);
	  }
return false;
   }
}
