/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventBlockBB;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.world.World;

public class Phase
extends Mod {
    public static Value<String> mode = new Value("Phase", "Mode", 0);

    public Phase() {
        super("Phase", Mod.Category.PLAYER, Colors.RED.c);
        Phase.mode.mode.add("CubeCraft");
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
        ClientUtil.sendClientMessage("Phase Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Phase Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    private void onUpdate(EventPreMotion event) {
        if (this.isInsideBlock() && this.mc.thePlayer.isSneaking()) {
            float yaw = this.mc.thePlayer.rotationYaw;
            this.mc.thePlayer.boundingBox.offset(3.0 * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, 3.0 * Math.sin(Math.toRadians(yaw + 90.0f)));
        }
        if (PlayerUtil.MovementInput()) {
            this.mc.timer.timerSpeed = 0.21f;
            this.mc.thePlayer.onGround = true;
            PlayerUtil.setSpeed(2.9);
        } else {
            this.mc.timer.timerSpeed = 1.0f;
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.motionX = 0.0;
        }
    }

    @EventTarget
    private void onSetBoundingbox(EventBlockBB event) {
        if (event.getBoundingBox() != null && event.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mc.thePlayer.isSneaking()) {
            event.setBoundingBox(null);
        }
    }

    private boolean isInsideBlock() {
        int x = MathHelper.floor_double(this.mc.thePlayer.boundingBox.minX);
        while (x < MathHelper.floor_double(this.mc.thePlayer.boundingBox.maxX) + 1) {
            int y = MathHelper.floor_double(this.mc.thePlayer.boundingBox.minY);
            while (y < MathHelper.floor_double(this.mc.thePlayer.boundingBox.maxY) + 1) {
                int z = MathHelper.floor_double(this.mc.thePlayer.boundingBox.minZ);
                while (z < MathHelper.floor_double(this.mc.thePlayer.boundingBox.maxZ) + 1) {
                    Block block = this.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(this.mc.theWorld, new BlockPos(x, y, z), this.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && this.mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }
}

