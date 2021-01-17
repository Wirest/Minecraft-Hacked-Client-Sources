/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.events.EventRender2D;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.handler.MouseInputHandler;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.util.vector.Vector3f;

public class ItemTeleport
extends Mod {
    private Value<String> mode = new Value("ItemTeleport", "Mode", 0);
    private double tpX = 2.147483647E9;
    private double tpY = 2.147483647E9;
    private double tpZ = 2.147483647E9;
    private MouseInputHandler handler = new MouseInputHandler(0);
    private int tick = 0;

    public ItemTeleport() {
        super("ItemTeleport", Mod.Category.PLAYER, Colors.DARKORANGE.c);
        this.mode.mode.add("AAC3.3.9");
        this.mode.mode.add("OldAAC");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClientUtil.sendClientMessage("ItemTeleport Enable", ClientNotification.Type.SUCCESS);
    }
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("ItemTeleport Disable", ClientNotification.Type.ERROR);
    }

    private void teleport(double x, double y, double z) {
        if (this.mode.isCurrentMode("AAC3.3.9")) {
            this.doAAC(x, y, z);
        } else {
            this.doOldAAC(x, y, z);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.mc.gameSettings.keyBindSneak.pressed && this.tpX != 2.147483647E9 && this.tpY != 2.147483647E9 && this.tpZ != 2.147483647E9) {
            this.teleport(this.tpX, this.tpY, this.tpZ);
        }
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
    }

    @EventTarget
    public void onEvent(EventRender2D event) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit.equals((Object)MovingObjectPosition.MovingObjectType.BLOCK) && this.mc.currentScreen == null) {
            double x = this.mc.objectMouseOver.getBlockPos().getX();
            double y = this.mc.objectMouseOver.getBlockPos().getY();
            double z = this.mc.objectMouseOver.getBlockPos().getZ();
            if (this.handler.canExcecute()) {
                this.tpX = x + 0.5;
                this.tpY = y + 1.0;
                this.tpZ = z + 0.5;
                ClientUtil.sendClientMessage("(X: " + this.tpX + " | Y: " + this.tpY + " | Z: " + this.tpZ + ")", ClientNotification.Type.INFO);
            }
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            double eX = this.tpX - packet.x;
            double eY = this.tpY - packet.y;
            double eZ = this.tpZ - packet.z;
            double x = eX;
            double y = eY;
            double d = eZ;
        }
    }

    private void doAAC(double x, double y, double z) {
        ArrayList<Vector3f> vecs = PlayerUtil.vanillaTeleportPositions(x, y, z, 5.0);
        int i = 0;
        while (i < vecs.size()) {
            Vector3f vec = vecs.get(i);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 4.0, this.mc.thePlayer.posZ, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, true));
            PlayerUtil.setSpeed(0.04);
            ++i;
        }
    }

    private void doOldAAC(double x, double y, double z) {
        ArrayList<Vector3f> vecs = PlayerUtil.vanillaTeleportPositions(x, y, z, 9.0);
        int i = 0;
        while (i < vecs.size()) {
            Vector3f vec = vecs.get(i);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, true));
            ++i;
        }
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(-2.0, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
    }
}

