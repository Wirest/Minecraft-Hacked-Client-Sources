package me.Corbis.Execution.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Timer;

public class Wrapper {

    public static Timer getTimer(){
        return Minecraft.getMinecraft().timer;
    }

    public static EntityPlayerSP getPlayer(){
        return Minecraft.getMinecraft().thePlayer;
    }

    public static WorldClient getWorld(){
        return Minecraft.getMinecraft().theWorld;
    }
}
