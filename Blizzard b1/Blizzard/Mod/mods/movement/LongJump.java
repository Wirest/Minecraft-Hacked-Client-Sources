/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Mod.mods.movement;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class LongJump
extends Mod {
    public LongJump() {
        super("LongJump", "LongJump", 37, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (LongJump.mc.gameSettings.keyBindForward.pressed || LongJump.mc.gameSettings.keyBindBack.pressed || LongJump.mc.gameSettings.keyBindLeft.pressed || LongJump.mc.gameSettings.keyBindRight.pressed && LongJump.mc.gameSettings.keyBindJump.pressed) {
            if (LongJump.mc.thePlayer.isAirBorne) {
                LongJump.mc.thePlayer.motionX *= 1.11;
                LongJump.mc.thePlayer.motionZ *= 1.11;
            }
        } else {
            LongJump.mc.thePlayer.motionX *= 1.0;
            LongJump.mc.thePlayer.motionZ *= 1.0;
        }
    }
}

