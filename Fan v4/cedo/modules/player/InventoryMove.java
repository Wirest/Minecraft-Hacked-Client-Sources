package cedo.modules.player;

import cedo.events.Event;
import cedo.modules.Module;
import cedo.ui.clickgui.ClickGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class InventoryMove extends Module {
    public InventoryMove() {
        super("InvMove", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e) {

        // TODO: Use arrow keys to control camera

        if ((mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof ClickGUI)) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack);
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight);
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
        }
    }

    public void onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null)
            mc.gameSettings.keyBindForward.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null)
            mc.gameSettings.keyBindBack.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null)
            mc.gameSettings.keyBindRight.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null)
            mc.gameSettings.keyBindLeft.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null)
            mc.gameSettings.keyBindJump.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null)
            mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
}