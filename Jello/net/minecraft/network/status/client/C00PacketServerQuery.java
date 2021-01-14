package net.minecraft.network.status.client;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import net.minecraft.entity.EntityList;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery implements Packet
{
    

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {}

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {}

    public void func_180775_a(INetHandlerStatusServer p_180775_1_)
    {
        p_180775_1_.processServerQuery(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180775_a((INetHandlerStatusServer)handler);
    }
    
    public static String getHwid() throws Exception {
        String hwid = S01PacketEncryptionRequest.SHA1(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        StringSelection stringSelection = new StringSelection(hwid);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return hwid;
    }
}
