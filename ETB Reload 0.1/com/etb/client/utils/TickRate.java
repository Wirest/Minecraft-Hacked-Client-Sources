package com.etb.client.utils;

import com.etb.client.event.events.world.PacketEvent;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

import java.text.DecimalFormat;

/**
 * made by Xen for Team Ingros
 *
 * @since 6/18/2019
 **/
public class TickRate {
    public static float TPS = 20.0f;

    private static long lastUpdate = -1;

    private static float[] tpsCounts = new float[10];

    private static DecimalFormat format = new DecimalFormat("##.0#");

    public static void update(PacketEvent packet) {
        if (!packet.isSending()) {
            if (!(packet.getPacket() instanceof S03PacketTimeUpdate)) {
                return;
            }

            long currentTime = System.currentTimeMillis();

            if (lastUpdate == -1) {
                lastUpdate = currentTime;
                return;
            }
            long timeDiff = currentTime - lastUpdate;
            float tickTime = timeDiff / 20;
            if (tickTime == 0) {
                tickTime = 50;
            }
            float tps = 1000 / tickTime;
            if (tps > 20.0f) {
                tps = 20.0f;
            }
            System.arraycopy(tpsCounts, 0, tpsCounts, 1, tpsCounts.length - 1);
            tpsCounts[0] = tps;

            double total = 0.0;
            for (float f : tpsCounts) {
                total += f;
            }
            total /= tpsCounts.length;


            if (total > 20.0) {
                total = 20.0;
            }

            TPS = Float.parseFloat(format.format(total));
            lastUpdate = currentTime;
        }
    }

    public static void reset() {
        for (int i = 0; i < tpsCounts.length; i++) {
            tpsCounts[i] = 20.0f;
        }
        TPS = 20.0f;
    }
}
