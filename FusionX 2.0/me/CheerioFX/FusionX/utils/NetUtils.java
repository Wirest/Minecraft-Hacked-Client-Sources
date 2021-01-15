// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtils
{
    public static void sendPacket(final Packet packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().dispatchPacket(packet, null);
    }
}
