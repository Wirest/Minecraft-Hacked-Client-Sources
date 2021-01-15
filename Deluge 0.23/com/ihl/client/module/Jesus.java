package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@EventHandler(events = {EventPacket.class, EventPlayerMove.class})
public class Jesus extends Module {

    private boolean dip;

    public Jesus(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerMove) {
            EventPlayerMove e = (EventPlayerMove) event;
            if (e.type == Event.Type.PRE) {
                if (isInLiquid(0) && !isInLiquid(Helper.player().getEyeHeight()) && !Helper.player().isSneaking()) {
                    e.y = 0.115;
                    Helper.player().motionY = 0;
                }
            }
        } else if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.SEND) {
                if (e.packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) e.packet;
                    dip = !dip;
                    if (dip && onLiquid() && !isInLiquid(0) && !Helper.player().isSneaking()) {
                        packet.y -= 0.01;
                    }
                }
            }
        }
    }

    /*
    *
    * Credit to Lord Pankake
    * */
    public static boolean onLiquid() {
        boolean onLiquid = false;
        int y = (int) Helper.player().getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
        for (int x = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minX); x < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minZ); z < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxZ) + 1; z++) {
                Block block = Helper.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                if ((block != null) && (!(block instanceof BlockAir))) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    /*
    *
    * Credit to Lord Pankake
    * */
    public static boolean isInLiquid(double yOffset) {
        boolean inLiquid = false;
        int y = (int)(Helper.player().getEntityBoundingBox().minY+yOffset);
        for (int x = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minX); x < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(Helper.player().getEntityBoundingBox().minZ); z < MathHelper.floor_double(Helper.player().getEntityBoundingBox().maxZ) + 1; z++) {
                Block block = Helper.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                if ((block != null) && (!(block instanceof BlockAir))) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
}
