// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import com.google.common.collect.Iterables;
import net.minecraft.util.Vec3i;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;

public class TPnBACKPathProcessor extends PathProcessor
{
    private final boolean creativeFlying;
    private boolean stopped;
    public static ArrayList<BlockPos> tpPosList;
    
    static {
        TPnBACKPathProcessor.tpPosList = new ArrayList<BlockPos>();
    }
    
    public TPnBACKPathProcessor(final ArrayList<PathPos> path, final boolean creativeFlying) {
        super(path);
        this.creativeFlying = creativeFlying;
    }
    
    @Override
    public void process() {
        TPnBACKPathProcessor.tpPosList.add(new BlockPos(this.mc.thePlayer));
        for (int o = 0; o < this.path.size(); ++o) {
            final BlockPos pos = new BlockPos(this.mc.thePlayer);
            final BlockPos nextPos = this.path.get(this.index);
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5, true));
            TPnBACKPathProcessor.tpPosList.add(nextPos);
            ++this.index;
            if (this.index < this.path.size()) {
                if (this.creativeFlying && this.index >= 2) {
                    final BlockPos prevPos = this.path.get(this.index - 1);
                    if (!this.path.get(this.index).subtract(prevPos).equals(prevPos.subtract(this.path.get(this.index - 2))) && !this.stopped) {
                        this.stopped = true;
                    }
                }
            }
            else {
                this.done = true;
            }
        }
        for (int ii = 0; ii < TPnBACKPathProcessor.tpPosList.size(); ++ii) {
            final BlockPos tpPos = (BlockPos)Iterables.getLast((Iterable)TPnBACKPathProcessor.tpPosList);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5, true));
            TPnBACKPathProcessor.tpPosList.remove(TPnBACKPathProcessor.tpPosList.size() - 1);
        }
    }
    
    @Override
    public void lockControls() {
    }
}
