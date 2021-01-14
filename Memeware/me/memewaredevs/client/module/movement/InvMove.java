/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

public class InvMove extends Module {
    public InvMove() {
        super("InvMove", 0, Module.Category.MOVEMENT);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        if (this.mc.currentScreen instanceof GuiChat) {
            return;
        }
        KeyBinding keyBindings[] = new KeyBinding[] {
                mc.gameSettings.keyBindForward,
                mc.gameSettings.keyBindRight,
                mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindJump
        };

        for (KeyBinding keyBinding : keyBindings) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }

    };
}
