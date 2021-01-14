package com.etb.client.module.modules.player;

import com.etb.client.event.events.game.TickEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.Random;

public class SkinFlash extends Module {
    public SkinFlash() {
        super("SkinFlash", Category.PLAYER, new Color(255, 147, 186).getRGB());
        setDescription("Flashes skin layers");
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
            if (parts != null) {
                EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1;
                int j = (arrayOfEnumPlayerModelParts1 = parts).length;
                for (int i = 0; i < j; i++) {
                    EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                    mc.gameSettings.setModelPartEnabled(part, true);
                }
            }
        }
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (mc.thePlayer != null) {
            EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
            if (parts != null) {
                EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1;
                int j = (arrayOfEnumPlayerModelParts1 = parts).length;
                for (int i = 0; i < j; i++) {
                    EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                    boolean newState = isEnabled() ? new Random().nextBoolean() : true;
                    mc.gameSettings.setModelPartEnabled(part, newState);
                }
            }
        }
    }
}