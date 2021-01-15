// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.client.entity.EntityPlayerSP;
import com.google.common.collect.Iterables;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.BlockUtils2;
import net.minecraft.util.Vec3i;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.module.modules.InfiniteEggAura;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;

public class EggauraPathProcessor extends PathProcessor
{
    private final boolean creativeFlying;
    private boolean stopped;
    public static ArrayList<BlockPos> tpPosList;
    
    static {
        EggauraPathProcessor.tpPosList = new ArrayList<BlockPos>();
    }
    
    public EggauraPathProcessor(final ArrayList<PathPos> path, final boolean creativeFlying) {
        super(path);
        this.creativeFlying = creativeFlying;
    }
    
    @Override
    public void process() {
        if (!InfiniteEggAura.isOldMode()) {
            EggauraPathProcessor.tpPosList.add(new BlockPos(this.mc.thePlayer));
        }
        for (int o = 0; o < this.path.size(); ++o) {
            if (InfiniteEggAura.isOldMode()) {
                final BlockPos pos = new BlockPos(this.mc.thePlayer);
                final BlockPos nextPos = this.path.get(this.index);
                this.mc.thePlayer.setPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5);
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5, true));
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
                    if (this.creativeFlying) {
                        final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                        thePlayer4.motionX /= Math.max(Math.abs(this.mc.thePlayer.motionX) * 50.0, 1.0);
                        final EntityPlayerSP thePlayer5 = this.mc.thePlayer;
                        thePlayer5.motionY /= Math.max(Math.abs(this.mc.thePlayer.motionY) * 50.0, 1.0);
                        final EntityPlayerSP thePlayer6 = this.mc.thePlayer;
                        thePlayer6.motionZ /= Math.max(Math.abs(this.mc.thePlayer.motionZ) * 50.0, 1.0);
                    }
                    this.done = true;
                }
            }
            else {
                final BlockPos pos = new BlockPos(this.mc.thePlayer);
                final BlockPos nextPos = this.path.get(this.index);
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5, true));
                EggauraPathProcessor.tpPosList.add(nextPos);
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
        }
        final BlockPos eggPos = new BlockPos(InfiniteEggAura.eggPosX, InfiniteEggAura.eggPosY, InfiniteEggAura.eggPosZ);
        if (InfiniteEggAura.isRightClick()) {
            BlockUtils2.rcActionLegit(eggPos);
            FusionX.addChatMessage("Aa " + FusionX.theClient.setmgr.getSetting("Block Mode").getValString() + " at X:" + eggPos.getX() + " Y:" + eggPos.getY() + " Z:" + eggPos.getZ() + " has been right-clicked!");
        }
        else {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, eggPos, EnumFacing.DOWN));
            FusionX.addChatMessage("A " + FusionX.theClient.setmgr.getSetting("Block Mode").getValString() + " at X:" + eggPos.getX() + " Y:" + eggPos.getY() + " Z:" + eggPos.getZ() + " has been broken!");
        }
        for (int ii = 0; ii < EggauraPathProcessor.tpPosList.size(); ++ii) {
            final BlockPos tpPos = (BlockPos)Iterables.getLast((Iterable)EggauraPathProcessor.tpPosList);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5, true));
            EggauraPathProcessor.tpPosList.remove(EggauraPathProcessor.tpPosList.size() - 1);
        }
    }
    
    @Override
    public void lockControls() {
    }
}
