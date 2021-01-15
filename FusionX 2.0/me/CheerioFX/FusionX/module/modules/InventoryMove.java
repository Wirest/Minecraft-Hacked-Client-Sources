// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Objects;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class InventoryMove extends Module
{
    public InventoryMove() {
        super("InvMove", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void call(final EventPreMotionUpdates event) {
        final KeyBinding[] moveKeys = { InventoryMove.mc.gameSettings.keyBindRight, InventoryMove.mc.gameSettings.keyBindLeft, InventoryMove.mc.gameSettings.keyBindBack, InventoryMove.mc.gameSettings.keyBindForward, InventoryMove.mc.gameSettings.keyBindJump, InventoryMove.mc.gameSettings.keyBindSprint };
        if (InventoryMove.mc.currentScreen instanceof GuiScreen && !(InventoryMove.mc.currentScreen instanceof GuiMainMenu) && !(InventoryMove.mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] array;
            for (int length = (array = moveKeys).length, i = 0; i < length; ++i) {
                final KeyBinding key = array[i];
                key.pressed = Keyboard.isKeyDown(key.getKeyCode());
            }
        }
        else if (Objects.isNull(InventoryMove.mc.currentScreen)) {
            KeyBinding[] array2;
            for (int length2 = (array2 = moveKeys).length, j = 0; j < length2; ++j) {
                final KeyBinding bind = array2[j];
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }
}
