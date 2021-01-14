package cheatware.module.movement;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

public class GuiMove extends Module {
    private Object GuiContainer;
    private GuiScreen GuiIngameMenu;
	
	public GuiMove() {
		super("GuiMove", Keyboard.KEY_NONE, Category.MOVEMENT);
		
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen != GuiIngameMenu && (mc.currentScreen != GuiContainer) && !(mc.currentScreen instanceof GuiChat)) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack);
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight);
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
        }
	}

}
