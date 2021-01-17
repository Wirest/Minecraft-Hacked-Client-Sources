/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventBlockBB;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class LiquidJump
extends Mod {
    private int stage;
    private boolean canjump;
    private int delay;
    private int timer;

    public LiquidJump() {
        super("LiquidJump", Mod.Category.MOVEMENT, Colors.AQUA.c);
    }

    @EventTarget
    public void onMotion(EventPreMotion event) {
        if (this.mc.thePlayer.isCollidedVertically && !this.canjump && PlayerUtil.getBlockUnderPlayer(this.mc.thePlayer) instanceof BlockLiquid) {
            ++this.delay;
            this.stage = 0;
            this.mc.thePlayer.motionY = 0.1;
        } else if (this.mc.thePlayer.onGround && !(PlayerUtil.getBlockAtPosC(this.mc.thePlayer, 0.0, 1.0, 0.0) instanceof BlockLiquid)) {
            this.canjump = false;
            this.delay = 0;
        }
        if (this.delay > 2) {
            this.canjump = true;
        }
        if (this.canjump) {
            ++this.stage;
            double moty = this.mc.thePlayer.motionY;
            switch (this.stage) {
                case 1: {
                    moty = 0.5;
                    break;
                }
                case 2: {
                    moty = 0.483;
                    break;
                }
                case 3: {
                    moty = 0.46;
                    break;
                }
                case 4: {
                    moty = 0.42;
                    break;
                }
                case 5: {
                    moty = 0.388;
                    break;
                }
                case 6: {
                    moty = 0.348;
                    break;
                }
                case 7: {
                    moty = 0.316;
                    break;
                }
                case 8: {
                    moty = 0.284;
                    break;
                }
                case 9: {
                    moty = 0.252;
                    break;
                }
                case 10: {
                    moty = 0.22;
                    break;
                }
                case 11: {
                    moty = 0.188;
                    break;
                }
                case 12: {
                    moty = 0.166;
                    break;
                }
                case 13: {
                    moty = 0.165;
                    break;
                }
                case 14: {
                    moty = 0.16;
                    break;
                }
                case 15: {
                    moty = 0.136;
                    break;
                }
                case 16: {
                    moty = 0.11;
                    break;
                }
                case 17: {
                    moty = 0.111;
                    break;
                }
                case 18: {
                    moty = 0.1095;
                    break;
                }
                case 19: {
                    moty = 0.073;
                    break;
                }
                case 20: {
                    moty = 0.085;
                    break;
                }
                case 21: {
                    moty = 0.06;
                    break;
                }
                case 22: {
                    moty = 0.036;
                    break;
                }
                case 23: {
                    moty = 0.04;
                    break;
                }
                case 24: {
                    moty = 0.03;
                    break;
                }
                case 25: {
                    moty = 0.004;
                    break;
                }
                case 26: {
                    moty = 0.0025;
                    break;
                }
                case 27: {
                    moty = 0.002;
                    break;
                }
                case 28: {
                    moty = 0.0015;
                    break;
                }
                case 29: {
                    moty = -0.025;
                    break;
                }
                case 30: {
                    moty = -0.06;
                    break;
                }
                case 31: {
                    moty = -0.09;
                    break;
                }
                case 32: {
                    moty = -0.12;
                    break;
                }
                case 33: {
                    moty = -0.15;
                    break;
                }
                case 34: {
                    moty = -0.18;
                    break;
                }
                case 35: {
                    moty = -0.21;
                    break;
                }
                case 36: {
                    moty = -0.24;
                    break;
                }
                case 37: {
                    moty = -0.28;
                    break;
                }
                case 38: {
                    moty = -0.34;
                    break;
                }
                case 39: {
                    moty = -0.4;
                    break;
                }
                case 40: {
                    moty = -0.46;
                    break;
                }
                case 41: {
                    moty = -0.52;
                    break;
                }
                case 42: {
                    moty = -0.58;
                    break;
                }
                case 43: {
                    moty = -0.65;
                    break;
                }
                case 44: {
                    moty = -0.71;
                    break;
                }
                case 45: {
                    this.canjump = false;
                }
            }
            this.mc.thePlayer.motionY = moty;
        }
        if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f && !this.mc.thePlayer.isSneaking() && this.getColliding(0)) {
            int delay = 40;
            if (this.timer < delay) {
                ++this.timer;
            } else {
                ++this.timer;
                if (this.timer < delay + 5) {
                    this.mc.thePlayer.motionX = 0.1;
                } else if (this.timer < delay + 20 && this.timer > delay + 10) {
                    this.mc.thePlayer.motionZ = -0.1;
                } else if (this.timer < delay + 30 && this.timer > delay + 20) {
                    this.mc.thePlayer.motionX = -0.1;
                } else if (this.timer < delay + 40 && this.timer > delay + 30) {
                    this.mc.thePlayer.motionZ = 0.1;
                }
                if (this.timer > delay + 40) {
                    this.timer = delay;
                }
            }
        } else {
            this.timer = 0;
        }
    }

    @EventTarget
    public void EventBB(EventBlockBB event) {
        if (event.getBlock() instanceof BlockLiquid && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava() && !this.mc.thePlayer.isSneaking()) {
            if (this.getColliding(0)) {
                event.setBoundingBox(AxisAlignedBB.fromBounds((double)event.getX() - 1.0, event.getY(), (double)event.getZ() - 1.0, (double)event.getX() + 0.9, (double)event.getY() + 0.86, (double)event.getZ() + 0.9));
            } else {
                event.setBoundingBox(AxisAlignedBB.fromBounds((double)event.getX() - 2.0, event.getY(), (double)event.getZ() - 2.0, (double)event.getX() + 2.0, (double)event.getY() + 1.0, (double)event.getZ() + 2.0));
            }
        }
    }

    private boolean getColliding(int i) {
        int mx = i;
        int mz = i;
        int max = i;
        int maz = i;
        if (this.mc.thePlayer.motionX > 0.01) {
            mx = 0;
        } else if (this.mc.thePlayer.motionX < -0.01) {
            max = 0;
        }
        if (this.mc.thePlayer.motionZ > 0.01) {
            mz = 0;
        } else if (this.mc.thePlayer.motionZ < -0.01) {
            maz = 0;
        }
        int xmin = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minX - (double)mx);
        int ymin = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY - 1.0);
        int zmin = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minZ - (double)mz);
        int xmax = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minX + (double)max);
        int ymax = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY + 1.0);
        int zmax = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minZ + (double)maz);
        int x = xmin;
        while (x <= xmax) {
            int y = ymin;
            while (y <= ymax) {
                int z = zmin;
                while (z <= zmax) {
                    Block block = LiquidJump.getBlock(new BlockPos(x, y, z));
                    if (block instanceof BlockLiquid && !this.mc.thePlayer.isInsideOfMaterial(Material.lava) && !this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                        return true;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("LiquidJump Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("LiquidJump Enable", ClientNotification.Type.SUCCESS);
    }
}

