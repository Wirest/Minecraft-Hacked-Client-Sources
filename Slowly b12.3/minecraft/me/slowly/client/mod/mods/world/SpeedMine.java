/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpeedMine
extends Mod {
    public static Value<String> mode = new Value("SpeedMine", "Mode", 0);
    public static Value<Double> speed = new Value<Double>("SpeedMine_Speed", 3.0, 1.0, 5.0, 1.0);

    public SpeedMine() {
        super("SpeedMine", Mod.Category.WORLD, Colors.YELLOW.c);
        SpeedMine.mode.mode.add("Normal");
        SpeedMine.mode.mode.add("Potion");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(Colors.GREEN.c);
        if (mode.isCurrentMode("Potion")) {
            this.mc.playerController.blockHitDelay = 0;
            boolean item = this.mc.thePlayer.getCurrentEquippedItem() == null;
            this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 0, item ? 1 : 0));
        }
    }

    @Override
    public void onDisable() {
        if (mode.isCurrentMode("Potion")) {
            this.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
            ClientUtil.sendClientMessage("SpeedMine Disable", ClientNotification.Type.ERROR);
        }
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("SpeedMine Enable", ClientNotification.Type.SUCCESS);
    }
}

