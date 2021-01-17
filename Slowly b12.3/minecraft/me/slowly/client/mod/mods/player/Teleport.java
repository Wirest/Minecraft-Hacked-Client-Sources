/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.io.PrintStream;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.events.EventRender;
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

public class Teleport
extends Mod {
    private double tpX = 2.147483647E9;
    private double tpY = 2.147483647E9;
    private double tpZ = 2.147483647E9;
    private Value<String> mode = new Value("Teleport", "Mode", 0);
    private Value<String> useMode = new Value("Teleport", "UseMode", 0);
    private MouseInputHandler handler = new MouseInputHandler(0);
    private int tick = 0;

    public Teleport() {
        super("Teleport", Mod.Category.PLAYER, Colors.AQUA.c);
        this.mode.mode.add("Vanilla");
        this.mode.mode.add("AAC1.9.10");
        this.mode.mode.add("AAC-Latest");
        this.mode.mode.add("Minesucht");
        this.useMode.addValue("Sneak");
        this.useMode.addValue("Click");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Teleport Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Teleport Enable", ClientNotification.Type.SUCCESS);
    }

    private void teleport(double x, double y, double z) {
        if (this.mode.isCurrentMode("Vanilla")) {
            this.doVanilla(x, y, z);
        } else if (this.mode.isCurrentMode("AAC1.9.10")) {
            this.doAAC(x, y, z);
        } else if (this.mode.isCurrentMode("AAC-Latest")) {
            this.doLatestAAC(x, y, z);
        } else if (this.mode.isCurrentMode("Minesucht")) {
            this.doMinesucht(x, y, z);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.mc.gameSettings.keyBindSneak.pressed && this.useMode.isCurrentMode("Sneak") && this.tpX != 2.147483647E9 && this.tpY != 2.147483647E9 && this.tpZ != 2.147483647E9) {
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
                if (this.useMode.isCurrentMode("Click")) {
                    this.teleport(this.tpX, this.tpY, this.tpZ);
                }
            }
        }
    }

    @EventTarget
    public void onEvent(EventRender event) {
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
            double z = eZ;
            if (Math.sqrt(eX * eX + eY * eY + eZ * eZ) < 1.0) {
                System.out.println("Teleported");
            }
        }
    }

    private void doAAC(double x, double y, double z) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 4.0, this.mc.thePlayer.posZ, true));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        PlayerUtil.setSpeed(0.04);
    }

    private void doLatestAAC(double x, double y, double z) {
        ArrayList<Vector3f> vecs = PlayerUtil.vanillaTeleportPositions(x, y, z, 9.0);
        int i = 0;
        while (i < vecs.size()) {
            Vector3f vec = vecs.get(i);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, true));
            ++i;
        }
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(-2.0, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
    }

    private void doMinesucht(double posX, double posY, double posZ) {
        int i = 0;
        while (i < 5) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.tpX, this.tpY + 0.200000003, this.tpZ, true));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.tpX, this.tpY, this.tpZ, true));
            ++i;
        }
    }

    private void doVanilla(double posX, double posY, double posZ) {
        ArrayList<Vector3f> vecs = PlayerUtil.vanillaTeleportPositions(posX, posY, posZ, 8.0);
        for (Vector3f vec : vecs) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, true));
        }
    }
    
}

