package VenusClient.online.Event.impl;

import VenusClient.online.Event.Event;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;

public class EventSendPacket extends Event {
    private Packet packet;

    public EventSendPacket(Packet packet) {
    	this.packet = packet;
        setPacket(packet);
    }

    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
    
    public static Block getBlockAtPos(BlockPos inBlockPos) {
        IBlockState s = Minecraft.getMinecraft().theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }
    
}