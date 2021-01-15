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
import me.aristhena.event.events.BlockCullEvent;
import me.aristhena.event.events.InsideBlockRenderEvent;
import me.aristhena.event.events.PushOutOfBlocksEvent;
import net.minecraft.util.AxisAlignedBB;
import me.aristhena.event.events.BoundingBoxEvent;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.event.events.TickEvent;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import me.aristhena.event.events.PacketReceiveEvent;
import me.aristhena.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.KeyPressEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Vanilla Phase")
public class VanillaPhase extends Module
{
    @Option.Op(min = 0.0, max = 2.0, increment = 0.1)
    private double speed;
    private int moveUnder;
    private boolean colliding;
    
    public VanillaPhase() {
        this.speed = 0.2;
    }
    
    @EventTarget
    private void onKeyPress(final KeyPressEvent event) {
        if (event.getKey() == 37) {
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() - 2.0, ClientUtils.z(), true));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
            this.moveUnder = 2;
        }
    }
    
    @EventTarget
    private void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && this.moveUnder == 2) {
            this.moveUnder = 1;
        }
    }
    
    @EventTarget
    private void onUpdate(final TickEvent event) {
        if (ClientUtils.player() != null && this.moveUnder == 1) {
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() - 2.0, ClientUtils.z(), true));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
            this.moveUnder = 0;
        }
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        if (this.isInsideBlock()) {
            if (ClientUtils.movementInput().jump) {
                event.setY(ClientUtils.player().motionY = this.speed);
            }
            else if (ClientUtils.movementInput().sneak) {
                event.setY(ClientUtils.player().motionY = -this.speed);
            }
            else {
                event.setY(ClientUtils.player().motionY = 0.0);
            }
            ClientUtils.setMoveSpeed(event, this.speed);
        }
    }
    
    @EventTarget
    private void onSetBoundingbox(final BoundingBoxEvent event) {
        if (this.isInsideBlock()) {
            event.setBoundingBox(null);
        }
    }
    
    @EventTarget
    private void onPushOutOfBlocks(final PushOutOfBlocksEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onInsideBlockRender(final InsideBlockRenderEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onBlockCull(final BlockCullEvent event) {
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
