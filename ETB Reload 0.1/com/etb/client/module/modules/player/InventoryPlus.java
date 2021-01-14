package com.etb.client.module.modules.player;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

import com.etb.client.event.events.world.PacketEvent;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableSet;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class InventoryPlus extends Module {
    private BooleanValue rotate = new BooleanValue("Rotate", true);
    private BooleanValue sw = new BooleanValue("ScreenWalk", true);
    private BooleanValue xc = new BooleanValue("MoreInventory", true);
    private KeyBinding[] movementKeys;

    public InventoryPlus() {
        super("Inventory+", Category.PLAYER, new Color(154, 168, 255, 255).getRGB());
        setDescription("Walk while having screens open");
        setRenderlabel("Inventory Plus");
        addValues(sw, xc, rotate);
        GameSettings settings = mc.gameSettings;
        this.movementKeys = new KeyBinding[]{settings.keyBindForward, settings.keyBindRight, settings.keyBindBack, settings.keyBindLeft, settings.keyBindJump, settings.keyBindSprint};
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            if (((event.getPacket() instanceof C0DPacketCloseWindow)) && xc.getValue()) {
                event.setCanceled(true);
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        KeyBinding[] moveKeys = {mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint,};
        if ((mc.currentScreen instanceof GuiScreen) && !(mc.currentScreen instanceof GuiMainMenu) && !(mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] array;
            if (sw.isEnabled()) {
                int length = (array = moveKeys).length;
                for (int i = 0; i < length; i++) {
                    KeyBinding key = array[i];
                    key.pressed = Keyboard.isKeyDown(key.getKeyCode());
                }
            }
            if (rotate.isEnabled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.thePlayer.rotationYaw += 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.thePlayer.rotationYaw -= 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && mc.thePlayer.rotationPitch - 4 > -90) {
                    mc.thePlayer.rotationPitch -= 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && mc.thePlayer.rotationPitch + 4 < 90) {
                    mc.thePlayer.rotationPitch += 4;
                }
            }
        } else if (Objects.isNull(mc.currentScreen) && sw.isEnabled()) {
            KeyBinding[] array2;
            int length2 = (array2 = moveKeys).length;
            for (int j = 0; j < length2; j++) {
                KeyBinding bind = array2[j];
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }
}