package modification.modules.world;

import modification.enummerates.Category;
import modification.events.EventPostMotion;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.RotationUtil;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public final class BedFucker
        extends Module {
    private final Value<Float> range = new Value("Range", Float.valueOf(4.5F), 1.0F, 6.0F, 1, this, new String[0]);
    private final Value<Float> failRate = new Value("Fail rate", Float.valueOf(1.0F), 1.0F, 5.0F, 0, this, new String[0]);
    private final Value<Boolean> moveFix = new Value("Move fix", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> autoTool = new Value("Auto tool", Boolean.valueOf(true), this, new String[0]);
    private int fails;
    private int currentItem;
    private BlockPos blockPos;
    private boolean rotated;
    private boolean triedPenis;
    private boolean switchedItem;

    public BedFucker(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.blockPos = null;
        this.fails = 0;
        this.triedPenis = false;
        this.currentItem = MC.thePlayer.inventory.currentItem;
        if (this.rotated) {
            RotationUtil.currentRotation = null;
            this.rotated = false;
        }
        if (this.switchedItem) {
            MC.thePlayer.inventory.currentItem = this.currentItem;
            this.switchedItem = false;
        }
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.blockPos = searchBed();
            if (this.blockPos == null) {
                onActivated();
                return;
            }
            float f1 = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.yaw, Modification.ROTATION_UTIL.rotationsToPos(this.blockPos)[0], 180.0F);
            float f2 = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.pitch, Modification.ROTATION_UTIL.rotationsToPos(this.blockPos)[1], 180.0F);
            RotationUtil.currentRotation = RotationUtil.fixedRotations(f1, f2);
            RotationUtil.moveToRotation = ((Boolean) this.moveFix.value).booleanValue();
            this.rotated = true;
        }
        if (((paramEvent instanceof EventPostMotion)) && (this.blockPos != null) && (RotationUtil.currentRotation != null)) {
            if (!this.triedPenis) {
                destroy(this.blockPos, EnumFacing.UP);
                this.fails |= 0x1;
                if (this.fails == ((Float) this.failRate.value).floatValue()) {
                    this.triedPenis = true;
                }
                return;
            }
            MovingObjectPosition localMovingObjectPosition = Modification.RAY_TRACE_UTIL.rayTraceBlock(RotationUtil.currentRotation.yaw, RotationUtil.currentRotation.pitch);
            if ((localMovingObjectPosition != null) && (!MC.theWorld.isAirBlock(localMovingObjectPosition.getBlockPos()))) {
                int i = findPickAxe();
                if ((i != MC.thePlayer.inventory.currentItem) && (((Boolean) this.autoTool.value).booleanValue())) {
                    this.currentItem = MC.thePlayer.inventory.currentItem;
                    MC.thePlayer.inventory.currentItem = i;
                    this.switchedItem = true;
                }
                destroy(localMovingObjectPosition.getBlockPos(), localMovingObjectPosition.sideHit);
            }
        }
    }

    protected void onDeactivated() {
    }

    private int findPickAxe() {
        for (int i = 0; i < 9; i++) {
            ItemStack localItemStack = MC.thePlayer.inventoryContainer.getSlot(i | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemPickaxe))) {
                return i;
            }
        }
        return MC.thePlayer.inventory.currentItem;
    }

    private void destroy(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        MC.getNetHandler().addToSendQueue(new C0APacketAnimation());
        MC.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, paramBlockPos, paramEnumFacing));
        MC.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, paramBlockPos, paramEnumFacing));
    }

    private BlockPos searchBed() {
        // Byte code:
        //   0: aload_0
        //   1: getfield 48	modification/modules/world/BedFucker:range	Lmodification/extenders/Value;
        //   4: getfield 153	modification/extenders/Value:value	Ljava/lang/Object;
        //   7: checkcast 36	java/lang/Float
        //   10: invokevirtual 176	java/lang/Float:floatValue	()F
        //   13: f2i
        //   14: istore_1
        //   15: iload_1
        //   16: idiv
        //   17: istore_2
        //   18: iload_2
        //   19: iload_1
        //   20: if_icmpgt +110 -> 130
        //   23: iload_1
        //   24: idiv
        //   25: istore_3
        //   26: iload_3
        //   27: iload_1
        //   28: if_icmpgt +96 -> 124
        //   31: iload_1
        //   32: idiv
        //   33: istore 4
        //   35: iload 4
        //   37: iload_1
        //   38: if_icmpgt +80 -> 118
        //   41: new 262	net/minecraft/util/BlockPos
        //   44: dup
        //   45: getstatic 81	modification/modules/world/BedFucker:MC	Lnet/minecraft/client/Minecraft;
        //   48: getfield 87	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
        //   51: getfield 266	net/minecraft/client/entity/EntityPlayerSP:posX	D
        //   54: iload_2
        //   55: i2d
        //   56: dadd
        //   57: getstatic 81	modification/modules/world/BedFucker:MC	Lnet/minecraft/client/Minecraft;
        //   60: getfield 87	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
        //   63: getfield 269	net/minecraft/client/entity/EntityPlayerSP:posY	D
        //   66: iload_3
        //   67: i2d
        //   68: dadd
        //   69: getstatic 81	modification/modules/world/BedFucker:MC	Lnet/minecraft/client/Minecraft;
        //   72: getfield 87	net/minecraft/client/Minecraft:thePlayer	Lnet/minecraft/client/entity/EntityPlayerSP;
        //   75: getfield 272	net/minecraft/client/entity/EntityPlayerSP:posZ	D
        //   78: iload 4
        //   80: i2d
        //   81: dadd
        //   82: invokespecial 275	net/minecraft/util/BlockPos:<init>	(DDD)V
        //   85: astore 5
        //   87: getstatic 81	modification/modules/world/BedFucker:MC	Lnet/minecraft/client/Minecraft;
        //   90: getfield 190	net/minecraft/client/Minecraft:theWorld	Lnet/minecraft/client/multiplayer/WorldClient;
        //   93: aload 5
        //   95: invokevirtual 279	net/minecraft/client/multiplayer/WorldClient:getBlockState	(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;
        //   98: invokeinterface 285 1 0
        //   103: getstatic 291	net/minecraft/init/Blocks:bed	Lnet/minecraft/block/Block;
        //   106: if_acmpne +6 -> 112
        //   109: aload 5
        //   111: areturn
        //   112: iinc 4 1
        //   115: goto -80 -> 35
        //   118: iinc 3 1
        //   121: goto -95 -> 26
        //   124: iinc 2 1
        //   127: goto -109 -> 18
        //   130: aconst_null
        //   131: areturn
    }
}




