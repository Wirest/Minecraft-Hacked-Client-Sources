package com.ihl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;

public class Helper {

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP player() {
        return mc().thePlayer;
    }

    public static WorldClient world() {
        return mc().theWorld;
    }

    public static PlayerControllerMP controller() {
        return mc().playerController;
    }

    public static ScaledResolution scaled() {
        return new ScaledResolution(mc(), mc().displayWidth, mc().displayHeight);
    }

}
