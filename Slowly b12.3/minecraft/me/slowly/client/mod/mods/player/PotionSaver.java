/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Collection;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.PotionEffect;

public class PotionSaver
extends Mod {
    public PotionSaver() {
        super("PotionSaver", Mod.Category.PLAYER, Colors.DARKRED.c);
    }

    @EventTarget
    public void onpre(PotionSaver event) {
        this.setColor(-16711883);
        if (PotionSaver.savePotion()) {
            for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
                effect.getDuration();
            }
        }
    }

    public static boolean savePotion() {
        if (!Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty() && ModManager.getModByName("PotionSaver").isEnabled() && Minecraft.getMinecraft().thePlayer.motionX == 0.0 && Minecraft.getMinecraft().thePlayer.motionZ == 0.0) {
            return true;
        }
        return false;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("PotionSaver Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("PotionSaver Enable", ClientNotification.Type.SUCCESS);
    }
}

