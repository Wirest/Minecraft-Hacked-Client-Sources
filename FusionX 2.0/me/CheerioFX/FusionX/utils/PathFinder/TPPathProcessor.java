// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.client.entity.EntityPlayerSP;
import com.google.common.collect.Iterables;
import net.minecraft.util.Vec3i;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;

public class TPPathProcessor extends PathProcessor
{
    private final boolean creativeFlying;
    private boolean stopped;
    public static boolean tpback;
    public static ArrayList<BlockPos> tpPosList;
    
    static {
        TPPathProcessor.tpback = false;
        TPPathProcessor.tpPosList = new ArrayList<BlockPos>();
    }
    
    public TPPathProcessor(final ArrayList<PathPos> path, final boolean creativeFlying) {
        super(path);
        this.creativeFlying = creativeFlying;
    }
    
    @Override
    public void process() {
        for (int o = 0; o < this.path.size(); ++o) {
            final BlockPos pos = new BlockPos(this.mc.thePlayer);
            final BlockPos nextPos = this.path.get(this.index);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5, true));
            this.mc.thePlayer.setPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5);
            TPPathProcessor.tpPosList.add(nextPos);
            ++this.index;
            if (this.index < this.path.size()) {
                if (this.creativeFlying && this.index >= 2) {
                    final BlockPos prevPos = this.path.get(this.index - 1);
                    if (!this.path.get(this.index).subtract(prevPos).equals(prevPos.subtract(this.path.get(this.index - 2))) && !this.stopped) {
                        final EntityPlayerSP thePlayer = this.mc.thePlayer;
                        thePlayer.motionX /= Math.max(Math.abs(this.mc.thePlayer.motionX) * 50.0, 1.0);
                        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                        thePlayer2.motionY /= Math.max(Math.abs(this.mc.thePlayer.motionY) * 50.0, 1.0);
                        final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                        thePlayer3.motionZ /= Math.max(Math.abs(this.mc.thePlayer.motionZ) * 50.0, 1.0);
                        this.stopped = true;
                    }
                }
            }
            else {
                this.done = true;
            }
        }
        if (!TPPathProcessor.tpback) {
            return;
        }
        for (int o = 0; o < TPPathProcessor.tpPosList.size(); ++o) {
            final BlockPos tpPos = (BlockPos)Iterables.getLast((Iterable)TPPathProcessor.tpPosList);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5, true));
            TPPathProcessor.tpPosList.remove(TPPathProcessor.tpPosList.size() - 1);
        }
        TPPathProcessor.tpPosList.clear();
    }
    
    @Override
    public void lockControls() {
        super.lockControls();
        this.mc.thePlayer.capabilities.isFlying = this.creativeFlying;
    }
}
