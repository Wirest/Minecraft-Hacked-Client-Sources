package de.iotacb.client.module.modules.movement;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.GuiCloseEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;

@ModuleInfo(name = "InventoryWalk", description = "Enables you to walk around while screens are opened", category = Category.MOVEMENT)
public class InventoryWalk extends Module {

	@Override
	public void onInit() {
		addValue("Only inventory", false);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getMc().currentScreen instanceof GuiChat || (!(getMc().currentScreen instanceof GuiInventory) && getValueByName("InventoryWalkOnly inventory").getBooleanValue())) return;
		
		if (getMc().currentScreen != null) {
			checkKeys();
		}
	}
	
	@EventTarget
	public void onGuiClose(GuiCloseEvent event) {
		if (getMc().currentScreen instanceof GuiChat || (!(getMc().currentScreen instanceof GuiInventory) && getValueByName("InventoryWalkOnly inventory").getBooleanValue())) return;
		
		checkKeys();
	}
	
	private void checkKeys() {
		final KeyBinding[] keys = {getMc().gameSettings.keyBindForward, getMc().gameSettings.keyBindRight, getMc().gameSettings.keyBindBack, getMc().gameSettings.keyBindLeft, getMc().gameSettings.keyBindJump};
		for (final KeyBinding key : keys) {
			KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode()));
		}
	}

}
