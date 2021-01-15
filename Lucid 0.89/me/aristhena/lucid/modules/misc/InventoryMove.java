/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Keyboard
 */
package me.aristhena.lucid.modules.misc;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.Render2DEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

@Mod
public class InventoryMove
extends Module {
    @EventTarget
    private void onRender(Render2DEvent event) {
        KeyBinding[] moveKeys = new KeyBinding[] {
                mc.gameSettings.keyBindForward,
                mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindRight,
                mc.gameSettings.keyBindJump
        };
        if (mc.currentScreen instanceof GuiContainer) {
            for (KeyBinding bind : moveKeys) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
            }
        } else if (mc.currentScreen == null) {
            for (KeyBinding bind : moveKeys) {
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }
}
