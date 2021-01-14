package com.etb.client.module.modules.player;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

public class NoFall extends Module {
    private EnumValue<mode> Mode = new EnumValue("Mode", mode.Hypixel);

    public NoFall() {
        super("NoFall", Category.PLAYER, new Color(200, 255, 87).getRGB());
        setDescription("Don't take fall damage");
        addValues(Mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(Mode.getValue().name());
        switch (Mode.getValue()) {
            case Hypixel:
                if (mc.thePlayer.fallDistance > 3 && isBlockUnder()) {
                    event.setOnGround(true);
                    mc.thePlayer.fallDistance = 0;
                }
                break;
            case Normal:
                if (mc.thePlayer.fallDistance > 3) {
                    event.setOnGround(true);
                }
                break;
            case Teleport:
                if (mc.thePlayer.fallDistance > 3 && isBlockUnder()) {
                    final BlockPos blockpos = getClosestBlockUnder();
                    mc.thePlayer.fallDistance = 0;
                    mc.thePlayer.getEntityBoundingBox().offsetAndUpdate(0, -(mc.thePlayer.posY - blockpos.getY() - 1), 0);
                }
                break;
            default:
                break;
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    private BlockPos getClosestBlockUnder() {
        for (int i = (int) mc.thePlayer.posY - 1; i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                return new BlockPos(pos.getX(),pos.getY() + 1,pos.getZ());
            } else {
                return pos;
            }
        }
        return mc.thePlayer.getPosition();
    }

    public enum mode {
        Hypixel, Normal, Teleport
    }
}
