/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;

public class Step
extends Mod {
    public static Value<String> mode;
    private boolean sent;
    private boolean stepped;
    private TimeHelper time = new TimeHelper();
    private Value<Double> stepHeight = new Value<Double>("Step_Height", 1.0, 1.0, 10.0, 0.1);

    public Step() {
        super("Step", Mod.Category.MOVEMENT, Colors.DARKGREEN.c);
        mode = new Value("Step", "Mode", 0);
        Step.mode.mode.add("Vanilla");
        Step.mode.mode.add("FastAAC");
        Step.mode.mode.add("AAC");
        Step.mode.mode.add("NCP");
        this.showValue = mode;
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        String mode = Step.mode.getModeAt(Step.mode.getCurrentMode());
        this.mc.thePlayer.stepHeight = 0.5f;
        if (mode.equalsIgnoreCase("Vanilla")) {
            this.mc.thePlayer.stepHeight = this.stepHeight.getValueState().floatValue();
        } else if (mode.equalsIgnoreCase("AAC")) {
            if (this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround) {
                if (this.time.isDelayComplete(300L)) {
                    int stepMinX = this.getStepHeight(new BlockPos(this.mc.thePlayer.getEntityBoundingBox().minX - 1.0, this.mc.thePlayer.posY, this.mc.thePlayer.posZ));
                    int stepMaxX = this.getStepHeight(new BlockPos(this.mc.thePlayer.getEntityBoundingBox().maxX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ));
                    int stepMinZ = this.getStepHeight(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.getEntityBoundingBox().minZ - 1.0));
                    int stepMaxZ = this.getStepHeight(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.getEntityBoundingBox().maxZ));
                    if (this.isFullNumber(this.mc.thePlayer.getEntityBoundingBox().minX)) {
                        if (stepMinX != -1) {
                            this.doAACStep(stepMinX);
                        }
                    } else if (this.isFullNumber(this.mc.thePlayer.getEntityBoundingBox().maxX)) {
                        if (stepMaxX != -1) {
                            this.doAACStep(stepMaxX);
                        }
                    } else if (this.isFullNumber(this.mc.thePlayer.getEntityBoundingBox().minZ)) {
                        if (stepMinZ != -1) {
                            this.doAACStep(stepMinZ);
                        }
                    } else if (this.isFullNumber(this.mc.thePlayer.getEntityBoundingBox().maxZ) && stepMaxZ != -1) {
                        this.doAACStep(stepMaxZ);
                    }
                    this.stepped = stepMinX != -1 || stepMaxX != -1 || stepMinZ != -1 || stepMaxZ != -1;
                    this.time.reset();
                }
            } else if (this.stepped) {
                PlayerUtil.toFwd(0.2);
                this.stepped = false;
            }
        } else if (mode.equalsIgnoreCase("NCP")) {
            if (this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround) {
                if (!this.sent) {
                    this.sent = true;
                    this.doNCPStep();
                }
            } else {
                this.sent = false;
            }
        }
    }

    private void doAACStep(double height) {
        this.mc.timer.timerSpeed = 0.02f;
        double posX = this.mc.thePlayer.posX;
        double posY = this.mc.thePlayer.posY;
        double posZ = this.mc.thePlayer.posZ;
        PlayerUtil.setSpeed(0.0);
        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + this.stepHeight.getValueState(), this.mc.thePlayer.posZ);
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.42, posZ, this.mc.thePlayer.onGround));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.752499705212015, posZ, this.mc.thePlayer.onGround));
        this.mc.timer.timerSpeed = 1.0f;
    }

    private int getStepHeight(BlockPos pos) {
        int step = -1;
        int i = 0;
        while ((double)i < this.stepHeight.getValueState() + 0.1) {
            if (this.mc.theWorld.getBlockState(pos).getBlock().getMaterial() != Material.air && this.mc.theWorld.getBlockState(pos.add(0, i, 0)).getBlock().getMaterial() == Material.air && this.mc.theWorld.getBlockState(pos.add(0, i + 1, 0)).getBlock().getMaterial() == Material.air) {
                step = i;
            }
            ++i;
        }
        return step;
    }

    private boolean isFullNumber(double num) {
        if (num == (double)((int)num)) {
            return true;
        }
        return false;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mode.isCurrentMode("FastAAC")) {
            BlockPos pos1 = new BlockPos(this.mc.thePlayer.posX + 1.0, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ);
            BlockPos pos2 = new BlockPos(this.mc.thePlayer.posX - 1.0, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ);
            BlockPos pos3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ + 1.0);
            BlockPos pos4 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ - 1.0);
            Block block1 = this.mc.theWorld.getBlockState(pos1).getBlock();
            Block block2 = this.mc.theWorld.getBlockState(pos2).getBlock();
            Block block3 = this.mc.theWorld.getBlockState(pos3).getBlock();
            Block block4 = this.mc.theWorld.getBlockState(pos4).getBlock();
            if (PlayerUtil.MovementInput() && this.mc.thePlayer.isCollidedHorizontally && (block1 == Blocks.air || block2 == Blocks.air || block3 == Blocks.air || block4 == Blocks.air)) {
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.jump();
                    this.mc.thePlayer.motionY = 0.386;
                } else {
                    PlayerUtil.toFwd(0.26);
                }
            }
        }
    }

    private void doNCPStep() {
        double posX = this.mc.thePlayer.posX;
        double posY = this.mc.thePlayer.posY;
        double posZ = this.mc.thePlayer.posZ;
        this.mc.thePlayer.stepHeight = this.stepHeight.getValueState().floatValue();
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.42, posZ, this.mc.thePlayer.onGround));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.748, posZ, this.mc.thePlayer.onGround));
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.stepHeight = 0.5f;
        super.onDisable();
        ClientUtil.sendClientMessage("Step Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Step Enable", ClientNotification.Type.SUCCESS);
    }
}

