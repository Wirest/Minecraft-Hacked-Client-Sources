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

public class Sprint
extends Mod {
    public Sprint() {
        super("Sprint", "Sprint", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (Sprint.mc.thePlayer.moveForward > 0.0f) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}

