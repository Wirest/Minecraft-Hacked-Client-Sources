/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AACLongJumpFix
extends Mod {
    private double posX;
    private double posZ;

    public AACLongJumpFix() {
        super("AACLongJumpFix", Mod.Category.MOVEMENT, Colors.GREY.c);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClientUtil.sendClientMessage("Patching LongJump..", ClientNotification.Type.INFO);
        this.posX = this.mc.thePlayer.posX;
        this.posZ = this.mc.thePlayer.posZ;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AACLongJumpFix Disable", ClientNotification.Type.ERROR);
        this.mc.gameSettings.keyBindForward.pressed = false;
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.disableKeyInputs();
        if (this.isFixed()) {
            this.close();
        } else {
            this.doFix();
        }
    }

    private void doFix() {
        this.mc.thePlayer.setSprinting(true);
        this.mc.gameSettings.keyBindForward.pressed = true;
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = 0.42;
            PlayerUtil.setSpeed(0.36);
        } else {
            this.mc.thePlayer.motionX = Math.sin(PlayerUtil.getDirection()) * PlayerUtil.getSpeed();
            this.mc.thePlayer.motionZ = (- Math.cos(PlayerUtil.getDirection())) * PlayerUtil.getSpeed();
        }
    }

    private void close() {
        ClientUtil.sendClientMessage("LongJump should be fixed ! Still not working? Try fixing again in 20 seconds.", ClientNotification.Type.SUCCESS);
        this.set(false);
    }

    private boolean isFixed() {
        int range = 2;
        double diffX = this.posX - this.mc.thePlayer.posX;
        double diffZ = this.posZ - this.mc.thePlayer.posZ;
        if (Math.sqrt(diffX * diffX + diffZ * diffZ) > (double)range) {
            return true;
        }
        return false;
    }

    private void disableKeyInputs() {
        this.mc.gameSettings.keyBindForward.pressed = false;
        this.mc.gameSettings.keyBindBack.pressed = false;
        this.mc.gameSettings.keyBindRight.pressed = false;
        this.mc.gameSettings.keyBindLeft.pressed = false;
        this.mc.gameSettings.keyBindJump.pressed = false;
    }
}

