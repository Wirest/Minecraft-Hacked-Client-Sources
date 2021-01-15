// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.AxisAlignedBB;
import me.aristhena.event.events.BoundingBoxEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Phase extends Module
{
    @Option.Op(min = 0.0, max = 10.0, increment = 0.1)
    private double distance;
    
    public Phase() {
        this.distance = 1.2;
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState().equals(Event.State.PRE) && this.isInsideBlock() && ClientUtils.player().isSneaking()) {
            final float yaw = ClientUtils.yaw();
            ClientUtils.player().boundingBox.offsetAndUpdate(this.distance * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, this.distance * Math.sin(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    @EventTarget
    private void onSetBoundingbox(final BoundingBoxEvent event) {
        if (event.getBoundingBox() != null && event.getBoundingBox().maxY > ClientUtils.player().boundingBox.minY && ClientUtils.player().isSneaking()) {
            event.setBoundingBox(null);
        }
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; ++z) {
                    final Block block = ClientUtils.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(ClientUtils.world(), new BlockPos(x, y, z), ClientUtils.world().getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && ClientUtils.player().boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
