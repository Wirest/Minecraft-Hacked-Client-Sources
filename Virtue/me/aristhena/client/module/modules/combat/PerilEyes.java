// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import me.aristhena.event.events.InsideBlockRenderEvent;
import me.aristhena.event.events.PushOutOfBlocksEvent;
import net.minecraft.util.AxisAlignedBB;
import me.aristhena.event.events.BoundingBoxEvent;
import me.aristhena.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.utils.Timer;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Peril Eyes")
public class PerilEyes extends Module
{
    private Timer timer;
    
    public PerilEyes() {
        this.timer = new Timer();
    }
    
    @Override
    public void enable() {
        this.timer.reset();
        super.enable();
    }
    
    @EventTarget
    public void onPost(final UpdateEvent event) {
        if (event.getState().equals(Event.State.POST) && ClientUtils.player().isCollidedHorizontally && !ClientUtils.player().isOnLadder()) {
            double xOff = 0.0;
            double zOff = 0.0;
            final double multiplier = 0.3;
            final double mx = Math.cos(Math.toRadians(ClientUtils.yaw() + 90.0f));
            final double mz = Math.sin(Math.toRadians(ClientUtils.yaw() + 90.0f));
            xOff = ClientUtils.movementInput().moveForward * multiplier * mx + ClientUtils.movementInput().moveStrafe * multiplier * mz;
            zOff = ClientUtils.movementInput().moveForward * multiplier * mz - ClientUtils.movementInput().moveStrafe * multiplier * mx;
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x() + xOff, ClientUtils.y(), ClientUtils.z() + zOff, false));
            for (int i = 1; i < 10; ++i) {
                ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), 8.988465674311579E307, ClientUtils.z(), false));
            }
            ClientUtils.player().setPosition(ClientUtils.x() + xOff, ClientUtils.y(), ClientUtils.z() + zOff);
        }
    }
    
    @EventTarget
    public void onBB(final BoundingBoxEvent event) {
        if (this.isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > ClientUtils.player().boundingBox.minY) {
            event.setBoundingBox(null);
        }
    }
    
    @EventTarget
    public void onPush(final PushOutOfBlocksEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onInsideBlockRender(final InsideBlockRenderEvent event) {
        event.setCancelled(true);
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
