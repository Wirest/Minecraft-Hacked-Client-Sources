/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.mod.mods.MOVEMENT;

import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

public class Sprint
extends Mod {
    public Value<Boolean> omni = new Value<Boolean>("Sprint_Omni", true);

    public Sprint() {
        super("Sprint", "Sprint", Category.MOVEMENT);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (!Minecraft.thePlayer.isCollidedHorizontally && !Minecraft.thePlayer.isSneaking() && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6 && (this.omni.getValueState() != false ? this.isMoving() : Minecraft.thePlayer.moveForward > 0.0f)) {
            Minecraft.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.setSprinting(false);
        super.onDisable();
    }

    public boolean isMoving() {
        return Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f;
    }
}

