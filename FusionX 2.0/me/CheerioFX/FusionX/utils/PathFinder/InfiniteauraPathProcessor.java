// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import com.google.common.collect.Iterables;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.modules.InfiniteAura;
import me.CheerioFX.FusionX.utils.EntityUtils;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.util.Vec3i;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;

public class InfiniteauraPathProcessor extends PathProcessor
{
    private final boolean creativeFlying;
    private boolean stopped;
    public static ArrayList<BlockPos> tpPosList;
    private EntityLivingBase target;
    
    static {
        InfiniteauraPathProcessor.tpPosList = new ArrayList<BlockPos>();
    }
    
    public InfiniteauraPathProcessor(final ArrayList<PathPos> path, final boolean creativeFlying, final EntityLivingBase target) {
        super(path);
        this.creativeFlying = creativeFlying;
        this.target = target;
    }
    
    @Override
    public void process() {
        if (this.target == null) {
            return;
        }
        for (int o = 0; o < this.path.size(); ++o) {
            final BlockPos pos = new BlockPos(this.mc.thePlayer);
            final BlockPos nextPos = this.path.get(this.index);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5, true));
            InfiniteauraPathProcessor.tpPosList.add(nextPos);
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
        EntityUtils.attackEntityAtPos(this.target, FusionX.theClient.moduleManager.getModuleByName("criticals").getState(), (int)this.target.posX + 0.5, Wrapper.removeDecimals(this.target.posY), (int)this.target.posZ + 0.5);
        if (!((InfiniteAura)FusionX.theClient.moduleManager.getModule(InfiniteAura.class)).isTpBack()) {
            return;
        }
        for (int o = 0; o < InfiniteauraPathProcessor.tpPosList.size(); ++o) {
            final BlockPos tpPos = (BlockPos)Iterables.getLast((Iterable)InfiniteauraPathProcessor.tpPosList);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5, true));
            InfiniteauraPathProcessor.tpPosList.remove(InfiniteauraPathProcessor.tpPosList.size() - 1);
        }
    }
    
    @Override
    public void lockControls() {
    }
}
