package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.utils.Printer;
import org.apache.commons.lang3.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

/**
 * made by oHare for oHareWare
 *
 * @since 6/11/2019
 **/
public class Teleport extends Command {
    private int x, z;
    private boolean gotonigga, niggay;
    private final Minecraft mc = Minecraft.getMinecraft();

    public Teleport() {
        super("Teleport",new String[]{"tp","tele","teleport"},"Teleport");
    }

    @Override
    public void onRun(final String[] args) {
        if (args[1].toLowerCase().equals("stop")) {
            if (gotonigga) {
                x = 0;
                z = 0;
                gotonigga = false;
                niggay = false;
                EventBus.getDefault().unregister(this);
                Printer.print("Stopped.");
            } else {
                Printer.print("Not currently running.");
            }
        } else {
            if (!gotonigga) {
                if (!isUnderBlock()) {
                    if (args.length > 1 && NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2])) {
                        x = Integer.parseInt(args[1]);
                        z = Integer.parseInt(args[2]);
                        gotonigga = true;
                        Printer.print("Teleporting to x:" + x + " z:" + z + ".");
                        EventBus.getDefault().register(this);
                    } else {
                        Printer.print("Invalid arguments.");
                    }
                } else {
                    Printer.print("You are under a block!");
                }
            } else {
                Printer.print("Already active!");
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (gotonigga) {
            final float storedangles = getRotationFromPosition(x, z);
            final double distancex = -4 * Math.sin(storedangles);
            final double distancez = 4 * Math.cos(storedangles);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                if (mc.thePlayer.posY < 250) {
                    mc.thePlayer.motionY = 5;
                } else {
                	mc.thePlayer.motionY = 0;
                    niggay = true;
                }
                if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) >= 32) {
                    if (niggay) {
                    	mc.thePlayer.motionX = distancex;
                    	mc.thePlayer.motionZ = distancez;
                    }
                } else {
                	mc.thePlayer.motionX = 0;
                	mc.thePlayer.motionZ = 0;
                    Printer.print("Finished you have arrived at x:" + (int)mc.thePlayer.posX + " z:" + (int)mc.thePlayer.posZ);
                    gotonigga = false;
                    niggay = false;
                    mc.renderGlobal.loadRenderers();
                    EventBus.getDefault().unregister(this);
                }
            }
        }
    }

    private boolean isUnderBlock() {
        for (int i = (int) (Minecraft.getMinecraft().thePlayer.posY + 2); i < 255; ++i) {
            BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ);
            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockFenceGate || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockSign || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockButton)
                continue;
            return true;
        }
        return false;
    }

    private float getRotationFromPosition(final double x, final double z) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final float yaw = (float) Math.atan2(zDiff, xDiff) - 1.57079632679f;
        return yaw;
    }
}
