/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove
extends Mod {
    public InventoryMove() {
        super("InventoryMove", Mod.Category.MOVEMENT, Colors.DARKMAGENTA.c);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("InventoryMove Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("InventoryMove Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] key;
            KeyBinding[] arrkeyBinding = key = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump};
            int n = arrkeyBinding.length;
            int n2 = 0;
            while (n2 < n) {
                KeyBinding b = arrkeyBinding[n2];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown((int)b.getKeyCode()));
                ++n2;
            }
        }
    }
}

