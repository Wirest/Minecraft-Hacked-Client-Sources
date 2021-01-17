/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.note;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Note {
    private Minecraft mc = Minecraft.getMinecraft();
    private BlockPos pos;
    private int instrument;
    private int note;

    public Note(BlockPos pos, int instrument, int note) {
        this.pos = pos;
        this.instrument = instrument;
        this.note = note;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getInstrument() {
        return this.instrument;
    }

    public int getNote() {
        return this.note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void play(BlockPos pos) {
        if (pos != null) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
        }
    }
}

