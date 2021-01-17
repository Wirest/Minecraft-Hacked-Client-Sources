// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLanServerPing extends Thread
{
    private static final AtomicInteger field_148658_a;
    private static final Logger logger;
    private final String motd;
    private final DatagramSocket socket;
    private boolean isStopping;
    private final String address;
    
    static {
        field_148658_a = new AtomicInteger(0);
        logger = LogManager.getLogger();
    }
    
    public ThreadLanServerPing(final String p_i1321_1_, final String p_i1321_2_) throws IOException {
        super("LanServerPinger #" + ThreadLanServerPing.field_148658_a.incrementAndGet());
        this.isStopping = true;
        this.motd = p_i1321_1_;
        this.address = p_i1321_2_;
        this.setDaemon(true);
        this.socket = new DatagramSocket();
    }
    
    @Override
    public void run() {
        final String s = getPingResponse(this.motd, this.address);
        final byte[] abyte = s.getBytes();
        while (!this.isInterrupted() && this.isStopping) {
            try {
                final InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
                final DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
                this.socket.send(datagrampacket);
            }
            catch (IOException ioexception) {
                ThreadLanServerPing.logger.warn("LanServerPinger: " + ioexception.getMessage());
                break;
            }
            try {
                Thread.sleep(1500L);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    @Override
    public void interrupt() {
        super.interrupt();
        this.isStopping = false;
    }
    
    public static String getPingResponse(final String p_77525_0_, final String p_77525_1_) {
        return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
    }
    
    public static String getMotdFromPingResponse(final String p_77524_0_) {
        final int i = p_77524_0_.indexOf("[MOTD]");
        if (i < 0) {
            return "missing no";
        }
        final int j = p_77524_0_.indexOf("[/MOTD]", i + "[MOTD]".length());
        return (j < i) ? "missing no" : p_77524_0_.substring(i + "[MOTD]".length(), j);
    }
    
    public static String getAdFromPingResponse(final String p_77523_0_) {
        final int i = p_77523_0_.indexOf("[/MOTD]");
        if (i < 0) {
            return null;
        }
        final int j = p_77523_0_.indexOf("[/MOTD]", i + "[/MOTD]".length());
        if (j >= 0) {
            return null;
        }
        final int k = p_77523_0_.indexOf("[AD]", i + "[/MOTD]".length());
        if (k < 0) {
            return null;
        }
        final int l = p_77523_0_.indexOf("[/AD]", k + "[AD]".length());
        return (l < k) ? null : p_77523_0_.substring(k + "[AD]".length(), l);
    }
}
