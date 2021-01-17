package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventSafeWalk;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class ScaffoldWalk
extends Mod {
    private BlockData blockData;
    private TimeHelper time = new TimeHelper();
    private TimeHelper delay = new TimeHelper();
    private Value<Boolean> noSwing = new Value<Boolean>("ScaffoldWalk_NoSwing", false);
    private Value<Boolean> silent = new Value<Boolean>("ScaffoldWalk_Silent", false);
    private Value<Double> delayValue = new Value<Double>("ScaffoldWalk_Delay", 250.0, 0.0, 800.0, 10.0);
    private Value<Double> speed = new Value<Double>("ScaffoldWalk_AACSpeed", 0.1, 0.0, 0.25, 0.01);
    public static Value<String> mode = new Value("ScaffoldWalk", "Mode", 0);
    private double olddelay;

    public ScaffoldWalk() {
        super("ScaffoldWalk", Mod.Category.WORLD, -2098646);
        ScaffoldWalk.mode.mode.add("Normal");
        ScaffoldWalk.mode.mode.add("AAC");
        ScaffoldWalk.mode.mode.add("Gomme");
        ScaffoldWalk.mode.mode.add("Unlegit");
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setColor(-6710887);
        this.showValue = mode;
        if (this.mc.thePlayer == null) {
            return;
        }
        this.blockData = this.getBlockData(new BlockPos(this.mc.thePlayer).add(0.0, -0.75, 0.0), 1);
        int block = this.getBlockItem();
        Item item = this.mc.thePlayer.inventory.getStackInSlot(block).getItem();
        if (block != -1 && item != null && item instanceof ItemBlock) {
            if (this.silent.getValueState().booleanValue()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
            }
            if (mode.isCurrentMode("AAC") || mode.isCurrentMode("Gomme")) {
                if (mode.isCurrentMode("Gomme")) {
                    this.mc.thePlayer.setSprinting(false);
                }
                if (mode.isCurrentMode("Gomme") && PlayerUtil.getSpeed() > 0.08) {
                    PlayerUtil.setSpeed(0.08);
                }
                if (mode.isCurrentMode("AAC") && PlayerUtil.getSpeed() > 0.08)  {
                	PlayerUtil.setSpeed(speed.getValueState());
                    this.mc.thePlayer.setSprinting(false);
                }
                if (mode.isCurrentMode("Gomme") && PlayerUtil.MovementInput()) {
                    PlayerUtil.setSpeed(0.1);
                }
            }
            if (mode.isCurrentMode("AAC")) {
                event.pitch = 71.0f;
                this.mc.thePlayer.setSprinting(false);
            }
            if (mode.isCurrentMode("CubeCraft") && PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(0.04);
            }
        }
        Random r = new Random();
        if (this.blockData != null && block != -1 && item != null && item instanceof ItemBlock) {
            Vec3 pos = this.getBlockSide(this.blockData.position, this.blockData.face);
            float[] rot = CombatUtil.getRotationsNeededBlock(pos.xCoord, pos.yCoord, pos.zCoord);
            float[] rots = CombatUtil.getDirectionToBlock(pos.xCoord, pos.yCoord, pos.zCoord, this.blockData.face);
            event.pitch = mode.isCurrentMode("Unlegit") ? rots[1] : rot[1];
            if (mode.isCurrentMode("CubeCraft")) {
                if (this.mc.gameSettings.keyBindForward.pressed) {
                    event.yaw = this.mc.thePlayer.rotationYaw >= 180.0f ? this.mc.thePlayer.rotationYaw - 180.0f : this.mc.thePlayer.rotationYaw + 180.0f;
                } else if (this.mc.gameSettings.keyBindBack.pressed) {
                    event.yaw = this.mc.thePlayer.rotationYaw;
                } else if (this.mc.gameSettings.keyBindLeft.pressed) {
                    event.yaw = this.mc.thePlayer.rotationYaw + 90.0f;
                } else if (this.mc.gameSettings.keyBindRight.pressed) {
                    event.yaw = this.mc.thePlayer.rotationYaw - 90.0f;
                }
                this.mc.thePlayer.rotationYawHead = event.yaw;
            } else {
                event.yaw = mode.isCurrentMode("Unlegit") ? rots[0] : rot[0];
            }
            this.mc.thePlayer.rotationYawHead = event.yaw;
        }
    }

    private double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
    }

    @EventTarget
    public void onSafe(EventSafeWalk event) {
        event.setSafe(true);
        if (this.mc.thePlayer == null) {
            return;
        }
        if (this.blockData != null) {
            int block = this.getBlockItem();
            Random rand = new Random();
            Item item = this.mc.thePlayer.inventory.getStackInSlot(block).getItem();
            if (block != -1 && item != null && item instanceof ItemBlock) {
                Vec3 hitVec = new Vec3(this.blockData.position).addVector(0.5, 0.5, 0.5).add(new Vec3(this.blockData.face.getDirectionVec()).scale(0.5));
                if ((!mode.isCurrentMode("AAC") && !mode.isCurrentMode("Gomme") || this.delay.isDelayComplete(mode.isCurrentMode("Gomme") ? 0 + rand.nextInt(50) : this.delayValue.getValueState().intValue() + rand.nextInt(50))) && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(block), this.blockData.position, this.blockData.face, hitVec)) {
                    this.delay.reset();
                    this.blockData = null;
                    this.time.reset();
                    if (this.noSwing.getValueState().booleanValue()) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    } else {
                        this.mc.thePlayer.swingItem();
                    }
                } else if (mode.isCurrentMode("CubeCraft")) {
                    if (this.delay.isDelayComplete(this.delayValue.getValueState().intValue() + rand.nextInt(50)) && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(block), this.blockData.position, this.blockData.face, hitVec)) {
                        this.delay.reset();
                        this.blockData = null;
                        this.time.reset();
                        if (this.noSwing.getValueState().booleanValue()) {
                            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        } else {
                            this.mc.thePlayer.swingItem();
                        }
                    } else if (this.delay.isDelayComplete(this.delayValue.getValueState().longValue()) && mode.isCurrentMode("Normal")) {
                        if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(block), this.blockData.position, this.blockData.face, hitVec)) {
                            this.delay.reset();
                            this.blockData = null;
                            if (this.noSwing.getValueState().booleanValue()) {
                                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            } else {
                                this.mc.thePlayer.swingItem();
                            }
                        }
                        this.delay.reset();
                    }
                }
            }
        }
    }

    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        if (heldStack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
        }
        return false;
    }

    private void sendCurrentItem() {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
    }

    private int getBlockItem() {
        int block = -1;
        int i = 8;
        while (i >= 0) {
            if (this.mc.thePlayer.inventory.getStackInSlot(i) != null && this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && (this.mc.thePlayer.getHeldItem() == this.mc.thePlayer.inventory.getStackInSlot(i) || this.silent.getValueState().booleanValue())) {
                block = i;
            }
            --i;
        }
        return block;
    }

    public BlockData getBlockData(BlockPos pos, int i) {
        return this.mc.theWorld.getBlockState(pos.add(0, 0, i)).getBlock() != Blocks.air ? new BlockData(pos.add(0, 0, i), EnumFacing.NORTH) : (this.mc.theWorld.getBlockState(pos.add(0, 0, - i)).getBlock() != Blocks.air ? new BlockData(pos.add(0, 0, - i), EnumFacing.SOUTH) : (this.mc.theWorld.getBlockState(pos.add(i, 0, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(i, 0, 0), EnumFacing.WEST) : (this.mc.theWorld.getBlockState(pos.add(- i, 0, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(- i, 0, 0), EnumFacing.EAST) : (this.mc.theWorld.getBlockState(pos.add(0, - i, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(0, - i, 0), EnumFacing.UP) : null))));
    }

    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.NORTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5);
        }
        if (face == EnumFacing.EAST) {
            return new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ());
        }
        if (face == EnumFacing.SOUTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5);
        }
        if (face == EnumFacing.WEST) {
            return new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ());
        }
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClientUtil.sendClientMessage("Scaffold Enable", ClientNotification.Type.SUCCESS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.sendCurrentItem();
        this.mc.gameSettings.keyBindSneak.pressed = false;
        this.mc.timer.timerSpeed = 1.0f;
        ClientUtil.sendClientMessage("Scaffold Disable", ClientNotification.Type.ERROR);
    }

    public class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}

