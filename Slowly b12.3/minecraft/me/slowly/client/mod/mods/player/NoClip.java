/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class NoClip
extends Mod {
    public NoClip() {
        super("NoClip", Mod.Category.PLAYER, Colors.RED.c);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        double movementMultiplier = (double)this.mc.thePlayer.capabilities.getFlySpeed() * 20.0;
        movementMultiplier *= 0.25;
        this.mc.thePlayer.noClip = true;
        this.mc.gameSettings.thirdPersonView = 0;
        float pitch = 0.0f;
        float yaw = this.mc.thePlayer.rotationYaw;
        float var3 = MathHelper.cos((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
        float var4 = MathHelper.sin((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
        float var5 = - MathHelper.cos((- pitch) * 0.01745329f);
        float var6 = MathHelper.sin((- pitch) * 0.01745329f);
        Vec3 look = new Vec3(var4 * var5, var6, var3 * var5).normalize();
        this.mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        double xMotion = 0.0;
        double yMotion = 0.0;
        double zMotion = 0.0;
        if (this.mc.currentScreen == null || ModManager.getModByName("InventoryMove").isEnabled() && this.mc.currentScreen instanceof GuiContainer) {
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindForward.getKeyCode())) {
                xMotion += look.xCoord;
                zMotion += look.zCoord;
            }
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindBack.getKeyCode())) {
                xMotion += - look.xCoord;
                zMotion += - look.zCoord;
            }
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindRight.getKeyCode())) {
                pitch = 0.0f;
                yaw = this.mc.thePlayer.rotationYaw + 90.0f;
                var3 = MathHelper.cos((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
                var4 = MathHelper.sin((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
                var5 = - MathHelper.cos((- pitch) * 0.01745329f);
                var6 = MathHelper.sin((- pitch) * 0.01745329f);
                look = new Vec3(var4 * var5, var6, var3 * var5).normalize();
                xMotion += look.xCoord;
                zMotion += look.zCoord;
            }
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindLeft.getKeyCode())) {
                pitch = 0.0f;
                yaw = this.mc.thePlayer.rotationYaw - 90.0f;
                var3 = MathHelper.cos((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
                var4 = MathHelper.sin((float)((double)((- yaw) * 0.01745329f) - 3.141592653589793));
                var5 = - MathHelper.cos((- pitch) * 0.01745329f);
                var6 = MathHelper.sin((- pitch) * 0.01745329f);
                look = new Vec3(var4 * var5, var6, var3 * var5).normalize();
                xMotion += look.xCoord;
                zMotion += look.zCoord;
            }
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindJump.getKeyCode())) {
                yMotion += 1.0;
            }
            if (Keyboard.isKeyDown((int)this.mc.gameSettings.keyBindSneak.getKeyCode())) {
                yMotion += -1.0;
            }
        }
        this.mc.thePlayer.setEntityBoundingBox(this.mc.thePlayer.getEntityBoundingBox().offset(xMotion *= movementMultiplier, yMotion *= movementMultiplier, zMotion *= movementMultiplier));
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            packet.y -= 0.1;
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoClip Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoClip Enable", ClientNotification.Type.SUCCESS);
    }
}

