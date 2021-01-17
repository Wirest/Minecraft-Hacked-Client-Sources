// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;

public class PlayerUtils
{
    public static boolean aacdamage;
    public static double aacdamagevalue;
    public static Minecraft mc;
    
    static {
        PlayerUtils.aacdamage = false;
        PlayerUtils.mc = Minecraft.getMinecraft();
    }
    
    public static void damagePlayer(final double value) {
        PlayerUtils.aacdamage = true;
        PlayerUtils.aacdamagevalue = value + 2.85;
        Minecraft.getMinecraft();
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        ++thePlayer.moveForward;
        Minecraft.getMinecraft();
        final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
        --thePlayer2.moveForward;
        Minecraft.getMinecraft();
        final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
        --thePlayer3.moveStrafing;
        Minecraft.getMinecraft();
        final EntityPlayerSP thePlayer4 = Minecraft.thePlayer;
        ++thePlayer4.moveStrafing;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.jump();
    }
    
    public static boolean playeriswalking() {
        return PlayerUtils.mc.gameSettings.keyBindForward.pressed || PlayerUtils.mc.gameSettings.keyBindBack.pressed || PlayerUtils.mc.gameSettings.keyBindLeft.pressed || PlayerUtils.mc.gameSettings.keyBindRight.pressed;
    }
    
    public static boolean playeriswalkingforward() {
        return !PlayerUtils.mc.gameSettings.keyBindBack.pressed && !PlayerUtils.mc.gameSettings.keyBindLeft.pressed && !PlayerUtils.mc.gameSettings.keyBindRight.pressed && PlayerUtils.mc.gameSettings.keyBindForward.pressed;
    }
    
    public static void sendPosition(final double x, final double y, final double z, final boolean onGround) {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, onGround));
    }
    
    public static void sendGround(final boolean onGround) {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
    }
}
