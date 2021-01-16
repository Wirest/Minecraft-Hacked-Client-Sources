/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.razerboy420.weepcraft.module.modules.movement;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;

@Module.Mod(category=Module.Category.MOVEMENT, description="Move while in the inventory", key=0, name="InventoryMove")
public class InvMove
extends Module {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        KeyBinding[] keys = new KeyBinding[]{Wrapper.mc().gameSettings.keyBindRight, Wrapper.mc().gameSettings.keyBindLeft, Wrapper.mc().gameSettings.keyBindBack, Wrapper.mc().gameSettings.keyBindForward, Wrapper.mc().gameSettings.keyBindJump, Wrapper.mc().gameSettings.keyBindSprint};
        if (!(Wrapper.mc().currentScreen instanceof GuiContainer) && (Wrapper.mc().currentScreen == null || Wrapper.mc().currentScreen instanceof GuiChat || Wrapper.mc().currentScreen instanceof Click)) {
            if (!Objects.isNull(Wrapper.mc().currentScreen)) return;
            KeyBinding[] arrayOfKeyBinding1 = keys;
            int nignog = keys.length;
            int hereInMyGarage = 0;
            while (hereInMyGarage < nignog) {
                KeyBinding var7 = arrayOfKeyBinding1[hereInMyGarage];
                if (!Keyboard.isKeyDown((int)var7.getKeyCode())) {
                    KeyBinding.setKeyBindState(var7.getKeyCode(), false);
                }
                ++hereInMyGarage;
            }
            return;
        } else {
            int bind;
            KeyBinding[] arrayOfKeyBinding1 = keys;
            int nignog = keys.length;
            if (Keyboard.isKeyDown((int)205)) {
                bind = 0;
                while (bind < 8) {
                    Wrapper.getPlayer().rotationYaw += 1.0f;
                    ++bind;
                }
            }
            if (Keyboard.isKeyDown((int)203)) {
                bind = 0;
                while (bind < 8) {
                    Wrapper.getPlayer().rotationYaw -= 1.0f;
                    ++bind;
                }
            }
            if (Keyboard.isKeyDown((int)200)) {
                bind = 0;
                while (bind < 8) {
                    Wrapper.getPlayer().rotationPitch -= 1.0f;
                    ++bind;
                }
            }
            if (Keyboard.isKeyDown((int)208)) {
                bind = 0;
                while (bind < 8) {
                    Wrapper.getPlayer().rotationPitch += 1.0f;
                    ++bind;
                }
            }
            int hereInMyGarage = 0;
            while (hereInMyGarage < nignog) {
                KeyBinding var7 = arrayOfKeyBinding1[hereInMyGarage];
                var7.pressed = Keyboard.isKeyDown((int)var7.getKeyCode());
                ++hereInMyGarage;
            }
        }
    }
}

