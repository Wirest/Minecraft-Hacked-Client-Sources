/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Strafe
extends Mod {
    public static Value<String> mode = new Value("Strafe", "Mode", 0);

    public Strafe() {
        super("Strafe", Mod.Category.MOVEMENT, Colors.GREEN.c);
        Strafe.mode.mode.add("NCP");
        Strafe.mode.mode.add("AAC");
        this.showValue = mode;
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.showValue = mode;
        if (PlayerUtil.MovementInput() && !ModManager.getModByName("Speed").isEnabled()) {
            if (this.mc.gameSettings.keyBindJump.pressed) {
                PlayerUtil.setSpeed(mode.isCurrentMode("NCP") ? 0.26 : 0.23);
            } else {
                PlayerUtil.setSpeed(mode.isCurrentMode("AAC") ? 0.17 : 0.135);
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Strafe Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Strafe Enable", ClientNotification.Type.SUCCESS);
    }
}

