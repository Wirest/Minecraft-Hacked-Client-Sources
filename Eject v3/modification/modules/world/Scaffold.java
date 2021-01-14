package modification.modules.world;

import modification.enummerates.Category;
import modification.events.EventFallDown;
import modification.events.EventRender2D;
import modification.events.EventSendPacket;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.ColorUtil;
import modification.utilities.RotationUtil;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class Scaffold
        extends Module {
    private static final List<Vec3i> DIRECTION_VECTORS = ;
    public final Value<Boolean> intave;
    private final Value<Boolean> silent;
    private final Value<Boolean> showBlocks;
    private final Value<Boolean> toggleAura;
    private int slot;
    private int item;
    private int counter;
    private float[] rotations;
    private boolean prevAura;
    private boolean rotated;

    public Scaffold(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
        for (EnumFacing localEnumFacing : EnumFacing.values()) {
            DIRECTION_VECTORS.add(localEnumFacing.getDirectionVec());
        }
        DIRECTION_VECTORS.add(new Vec3i(1, 0, -1));
        DIRECTION_VECTORS.add(new Vec3i(-1, 0, 1));
        DIRECTION_VECTORS.add(new Vec3i(1, 0, 1));
        DIRECTION_VECTORS.add(new Vec3i(-1, 0, -1));
        this.silent = new Value("Silent", Boolean.valueOf(true), this, new String[0]);
        this.intave = new Value("Intave", Boolean.valueOf(true), this, new String[0]);
        this.showBlocks = new Value("Show blocks", Boolean.valueOf(true), this, new String[0]);
        this.toggleAura = new Value("Toggle aura", Boolean.valueOf(true), this, new String[0]);
    }

    protected void onActivated() {
        this.rotations = null;
        if (this.rotated) {
            RotationUtil.resetRotations();
            this.rotated = false;
        }
        this.slot = MC.thePlayer.inventory.currentItem;
        this.prevAura = ((Module) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("KillAura"))).enabled;
    }

    public void onEvent(Event paramEvent) {
        Object localObject;
        if ((paramEvent instanceof EventTick)) {
            this.tag = (((Boolean) this.silent.value).booleanValue() ? "Silent" : "");
            this.item = findBlock(MC.thePlayer.inventoryContainer);
            if (shouldPlace(this.item)) {
                if (((Boolean) this.toggleAura.value).booleanValue()) {
                    ((Module) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("KillAura"))).enabled = false;
                }
                if ((this.item != -1) && (this.slot != this.item)) {
                    MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.slot = this.item));
                }
                localObject = new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ);
                if (this.rotations != null) {
                    if (((Boolean) this.intave.value).booleanValue()) {
                        RotationUtil.currentRotation = RotationUtil.fixedRotations(MC.thePlayer.rotationYaw + 180.0F, 82.5D);
                        this.rotated = true;
                        if (!MC.thePlayer.onGround) {
                            MC.thePlayer.motionX *= 0.8D;
                            MC.thePlayer.motionZ *= 0.8D;
                        }
                    } else {
                        RotationUtil.currentRotation = RotationUtil.fixedRotations(this.rotations[0], this.rotations[1]);
                        this.rotated = true;
                    }
                }
                if ((MC.theWorld.isAirBlock((BlockPos) localObject)) && (RotationUtil.currentRotation != null)) {
                    if (!allowPlacing()) {
                        return;
                    }
                    ItemStack localItemStack = ((Boolean) this.silent.value).booleanValue() ? MC.thePlayer.inventoryContainer.getSlot(this.slot | 0x24).getStack() : MC.thePlayer.getCurrentEquippedItem();
                    MovingObjectPosition localMovingObjectPosition = Modification.RAY_TRACE_UTIL.rayTraceBlock(RotationUtil.lastRotation.yaw, RotationUtil.lastRotation.pitch);
                    if ((localMovingObjectPosition != null) && (!MC.theWorld.isAirBlock(localMovingObjectPosition.getBlockPos())) && (MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, localItemStack, localMovingObjectPosition.getBlockPos(), localMovingObjectPosition.sideHit, localMovingObjectPosition.hitVec))) {
                        this.counter |= 0x1;
                        if (((Boolean) this.intave.value).booleanValue()) {
                            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                        MC.thePlayer.swingItem();
                        if (((Boolean) this.intave.value).booleanValue()) {
                            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                    }
                }
            }
        }
        if (((paramEvent instanceof EventSendPacket)) && (((Boolean) this.silent.value).booleanValue()) && (this.slot != MC.thePlayer.inventory.currentItem)) {
            localObject = (EventSendPacket) paramEvent;
            if ((((EventSendPacket) localObject).packet instanceof C09PacketHeldItemChange)) {
                ((EventSendPacket) localObject).packet = new C09PacketHeldItemChange(this.slot);
            }
        }
        if (((paramEvent instanceof EventRender2D)) && (((Boolean) this.showBlocks.value).booleanValue())) {
            localObject = (EventRender2D) paramEvent;
            int i = findBlockValue(MC.thePlayer.inventoryContainer);
            if (i > 0) {
                int j = -2 | 0x4;
                int k = -2 | 0x4;
                Modification.RENDER_UTIL.drawBorderedRect(j, k, MC.fontRendererObj.getStringWidth(Integer.toString(i).concat(" Blocks ")), MC.fontRendererObj.FONT_HEIGHT, 1, ColorUtil.BACKGROUND_DARKER, Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(Integer.toString(i).concat(" Blocks "), j, k | 0x1, -1);
            }
        }
        if ((paramEvent instanceof EventFallDown)) {
            localObject = new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ);
            Object[] arrayOfObject = findBlockData((BlockPos) localObject);
            if (arrayOfObject != null) {
                rotate(arrayOfObject);
            }
            ((EventFallDown) paramEvent).canceled = ((((Boolean) this.intave.value).booleanValue()) || (this.item == -1));
        }
    }

    protected void onDeactivated() {
        if ((((Boolean) this.silent.value).booleanValue()) && (this.slot != MC.thePlayer.inventory.currentItem)) {
            MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(MC.thePlayer.inventory.currentItem));
        }
        RotationUtil.currentRotation = null;
        ((Module) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("KillAura"))).enabled = this.prevAura;
    }

    private boolean allowPlacing() {
        double d = 0.024D;
        BlockPos localBlockPos1 = new BlockPos(MC.thePlayer.posX - 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.024D);
        BlockPos localBlockPos2 = new BlockPos(MC.thePlayer.posX - 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.024D);
        BlockPos localBlockPos3 = new BlockPos(MC.thePlayer.posX + 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.024D);
        BlockPos localBlockPos4 = new BlockPos(MC.thePlayer.posX + 0.024D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.024D);
        return (MC.thePlayer.worldObj.getBlockState(localBlockPos1).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos2).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos3).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos4).getBlock() == Blocks.air);
    }

    private boolean allowRotation() {
        double d = 0.1D;
        BlockPos localBlockPos1 = new BlockPos(MC.thePlayer.posX - 0.1D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.1D);
        BlockPos localBlockPos2 = new BlockPos(MC.thePlayer.posX - 0.1D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.1D);
        BlockPos localBlockPos3 = new BlockPos(MC.thePlayer.posX + 0.1D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ + 0.1D);
        BlockPos localBlockPos4 = new BlockPos(MC.thePlayer.posX + 0.1D, MC.thePlayer.posY - 0.5D, MC.thePlayer.posZ - 0.1D);
        return (MC.thePlayer.worldObj.getBlockState(localBlockPos1).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos2).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos3).getBlock() == Blocks.air) && (MC.thePlayer.worldObj.getBlockState(localBlockPos4).getBlock() == Blocks.air);
    }

    private void rotate(Object[] paramArrayOfObject) {
        BlockPos localBlockPos = new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.0D, MC.thePlayer.posZ);
        Vec3 localVec31 = new Vec3(MC.thePlayer.posX, MC.thePlayer.getEntityBoundingBox().minY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);
        Vec3 localVec32 = new Vec3((Vec3i) paramArrayOfObject[1]);
        Vec3 localVec33 = new Vec3(localBlockPos).add(localVec32).addVector(0.5D, -3.0D, 0.5D);
        float[] arrayOfFloat = Modification.ROTATION_UTIL.rotationsToVector(localVec33);
        MovingObjectPosition localMovingObjectPosition = Modification.RAY_TRACE_UTIL.rayTraceBlock(arrayOfFloat[0], arrayOfFloat[1]);
        if (allowRotation()) {
            this.rotations = arrayOfFloat;
        }
    }

    private Object[] findBlockData(BlockPos paramBlockPos) {
        Iterator localIterator = DIRECTION_VECTORS.iterator();
        while (localIterator.hasNext()) {
            Vec3i localVec3i = (Vec3i) localIterator.next();
            BlockPos localBlockPos = paramBlockPos.add(localVec3i);
            if (!MC.theWorld.isAirBlock(localBlockPos)) {
                return new Object[]{localBlockPos, localVec3i};
            }
        }
        return null;
    }

    private boolean shouldPlace(int paramInt) {
        ItemStack localItemStack = MC.thePlayer.getCurrentEquippedItem();
        if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
            return true;
        }
        return (((Boolean) this.silent.value).booleanValue()) && (paramInt != -1);
    }

    public final int findBlock(Container paramContainer) {
        for (int i = 0; i < 9; i++) {
            ItemStack localItemStack = paramContainer.getSlot(i | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                return i;
            }
        }
        return -1;
    }

    private int findBlockValue(Container paramContainer) {
        int i = 0;
        for (int j = 0; j < 9; j++) {
            ItemStack localItemStack = paramContainer.getSlot(j | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                i |= localItemStack.stackSize;
            }
        }
        return i;
    }
}




