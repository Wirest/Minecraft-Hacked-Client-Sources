// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class HighJump extends Module
{
    public HighJump() {
        super("HighJump", "HighJump", 2756017, 34, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (HighJump.mc.gameSettings.keyBindJump.pressed) {
            return;
        }
        final Minecraft mc = HighJump.mc;
        if (Minecraft.thePlayer.hurtTime != 0) {
            final Minecraft mc2 = HighJump.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = HighJump.mc;
                Minecraft.thePlayer.motionY = 1.1;
                for (int i = 0; i < 70; ++i) {
                    Minecraft.getMinecraft();
                    final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
                    Minecraft.getMinecraft();
                    final double posX = Minecraft.thePlayer.posX;
                    Minecraft.getMinecraft();
                    final double posY = Minecraft.thePlayer.posY + 0.06;
                    Minecraft.getMinecraft();
                    sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, Minecraft.thePlayer.posZ, false));
                    Minecraft.getMinecraft();
                    final NetHandlerPlayClient sendQueue2 = Minecraft.thePlayer.sendQueue;
                    Minecraft.getMinecraft();
                    final double posX2 = Minecraft.thePlayer.posX;
                    Minecraft.getMinecraft();
                    final double posY2 = Minecraft.thePlayer.posY;
                    Minecraft.getMinecraft();
                    sendQueue2.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX2, posY2, Minecraft.thePlayer.posZ, false));
                }
                Minecraft.getMinecraft();
                final NetHandlerPlayClient sendQueue3 = Minecraft.thePlayer.sendQueue;
                Minecraft.getMinecraft();
                final double posX3 = Minecraft.thePlayer.posX;
                Minecraft.getMinecraft();
                final double posY3 = Minecraft.thePlayer.posY + 0.1;
                Minecraft.getMinecraft();
                sendQueue3.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX3, posY3, Minecraft.thePlayer.posZ, false));
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
