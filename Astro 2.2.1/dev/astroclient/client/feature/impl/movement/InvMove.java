package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.util.Objects;

@Toggleable(label = "InvMove", category = Category.MOVEMENT)
public class InvMove extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (Objects.nonNull(mc.currentScreen) && !(mc.currentScreen instanceof GuiChat))
            for (KeyBinding keyBinding : new KeyBinding[]{mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump})
                keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
    }
}
