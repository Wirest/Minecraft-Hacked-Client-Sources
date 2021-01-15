package me.robbanrobbin.jigsaw.client.modules;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Module {

	public InventoryMove() {
		super("InvMove", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to walk when displaying inventory.");
	}

	@Override
	public void onUpdate() {
		KeyBinding[] moveKeys = { mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
				mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump,
				mc.gameSettings.keyBindSprint };
		if ((mc.currentScreen instanceof GuiContainer)) {
			KeyBinding[] array;
			int length = (array = moveKeys).length;
			for (int i = 0; i < length; i++) {
				KeyBinding key = array[i];
				key.pressed = Keyboard.isKeyDown(key.getKeyCode());
			}
		} else if (Objects.isNull(mc.currentScreen)) {
			KeyBinding[] array2;
			int length2 = (array2 = moveKeys).length;
			for (int j = 0; j < length2; j++) {
				KeyBinding bind = array2[j];
				if (!Keyboard.isKeyDown(bind.getKeyCode())) {
					KeyBinding.setKeyBindState(bind.getKeyCode(), false);
				}
			}
		}
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		KeyBinding[] moveKeys = { mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
				mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump,
				mc.gameSettings.keyBindSprint };
		if ((mc.currentScreen instanceof GuiContainer)) {
			KeyBinding[] array;
			int length = (array = moveKeys).length;
			for (int i = 0; i < length; i++) {
				KeyBinding key = array[i];
				key.pressed = Keyboard.isKeyDown(key.getKeyCode());
			}
		} else if (Objects.isNull(mc.currentScreen)) {
			KeyBinding[] array2;
			int length2 = (array2 = moveKeys).length;
			for (int j = 0; j < length2; j++) {
				KeyBinding bind = array2[j];
				if (!Keyboard.isKeyDown(bind.getKeyCode())) {
					KeyBinding.setKeyBindState(bind.getKeyCode(), false);
				}
			}
		}
		super.onLivingUpdate();
	}

}
