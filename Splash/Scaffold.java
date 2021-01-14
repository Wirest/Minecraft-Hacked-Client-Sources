package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventSafeWalk;
import splash.client.events.render.EventRender2D;
import splash.utilities.math.Vec3d;
import splash.utilities.player.BackupRotations;
import splash.utilities.player.BlockInfo;
import splash.utilities.player.MovementUtils;
import splash.utilities.player.RotationUtils;
import splash.utilities.player.ScaffoldUtils;
import splash.utilities.time.Stopwatch;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class Scaffold extends Module {
	private final List<Block> invalid;
 
	ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.NORMAL, this);
    BooleanValue<Boolean> tower = new BooleanValue<>("Tower", true, this); 
    BooleanValue<Boolean> switcher = new BooleanValue<>("Switch", true, this);
 
    private final MouseFilter pitchMouseFilter = new MouseFilter();
    private final MouseFilter yawMouseFilter = new MouseFilter();

	private BlockData lastBlockData;
	
    public Scaffold() {
        super("Scaffold", "Places blocks for you.", ModuleCategory.PLAYER);
    	invalid = Arrays.asList(Blocks.anvil, Blocks.wooden_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.sapling,
                Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.gravel);

    }
    
    @Override
    public void onEnable() {
    	lastBlockData = null;
    	super.onEnable();
    	mc.timer.timerSpeed = 1.0f;
 
    }
    
    @Override
    public void onDisable() {
    	super.onDisable();
    	mc.timer.timerSpeed = 1;
    }
    
    @Collect
    public void onRender2D(EventRender2D event) {
    	ScaledResolution sr = new ScaledResolution(mc);
        Splash.INSTANCE.getFontRenderer().drawStringWithShadow(Integer.toString(getBlockCount()), (sr.getScaledWidth() >> 1)  - Splash.INSTANCE.getFontRenderer().getStringWidth(Integer.toString(getBlockCount())) / 2, (sr.getScaledHeight() >> 1) - 3, Splash.INSTANCE.getClientColor());
    }
    
    @Collect
    public void onPacketSend(EventPacketSend event) {
    	if (event.getSentPacket() instanceof C0BPacketEntityAction) {
    		C0BPacketEntityAction packet = (C0BPacketEntityAction)event.getSentPacket();
    		event.setCancelled(packet.getAction().equals(Action.START_SNEAKING) || packet.getAction().equals(Action.STOP_SNEAKING));
    		//Not sending sneak packets with downwards scaffold, bypasses sprint speed checks by making the server think our dumbass player enity using invalid yaw direction is somehow moving normally
    	}
    }
    
    @Collect
    public void onUpdate(EventPlayerUpdate event) {
        double addition = mc.thePlayer.isCollidedHorizontally ? 0 : 0.1;
        final double x2 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double z2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double xOffset = MovementInput.moveForward * addition * x2 + MovementInput.moveStrafe * addition * z2;
        final double zOffset = MovementInput.moveForward * addition * z2 - MovementInput.moveStrafe * addition * x2;

        BlockPos blockBelow = new BlockPos(mc.thePlayer.posX + xOffset, mc.thePlayer.posY -1, mc.thePlayer.posZ + zOffset);

        BlockData blockEntry = mc.gameSettings.keyBindSneak.isKeyDown() ? findDown(new Vec3(0, 0, 0)) : mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air ? blockEntry = getBlockData2(blockBelow) : null;

		float speed = 0.4f;
		float yaw = 0;
		float pitch = 0;
        if (blockEntry == null) {
        	if (lastBlockData != null) {
        		float[] rotations = getFacingRotations(lastBlockData.position.getX(), lastBlockData.position.getY(), lastBlockData.position.getZ());
        		yaw = (yawMouseFilter.smooth(rotations[0] + ScaffoldUtils.getRandomInRange(-1f, 5f), speed));
        		pitch = (pitchMouseFilter.smooth(rotations[1] + ScaffoldUtils.getRandomInRange(-1.20f, 3.50f), speed));
        		event.setPitch(pitch);
        		event.setYaw(yaw);   
        	}
        }
        if (blockEntry == null) return; 
        
    	if (event.getStage() == Stage.PRE) {
         
    		float[] rotations = getFacingRotations(blockEntry.position.getX(), blockEntry.position.getY(), blockEntry.position.getZ());
    		yaw = (yawMouseFilter.smooth(rotations[0] + ScaffoldUtils.getRandomInRange(-1f, 5f), speed));
    		pitch = (pitchMouseFilter.smooth(rotations[1] + ScaffoldUtils.getRandomInRange(-1.20f, 3.50f), speed));
    		event.setPitch(pitch);
    		event.setYaw(yaw);   
    	} else {
    		if (getBlockCount() <= 0 || (switcher.getValue().equals(false) && mc.thePlayer.getCurrentEquippedItem() != null && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock))) {
    			return;
    		}
    		final int heldItem = mc.thePlayer.inventory.currentItem;
    		boolean hasBlock = false;
    		if (switcher.getValue().equals(true)) {
    			for (int i = 0; i < 9; ++i) {
    				if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0 && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {

    		        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i)); 
    		        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    					hasBlock = true;
    					break;
    				}
    			}
    			if (!hasBlock) {
    				for (int i = 0; i < 45; ++i) {
    					if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0 && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
    						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 8, 2, mc.thePlayer);
    						break;
    					}
    				}
    			}
    		}
    		
            if (tower.getValue().equals(true)) {
                if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isPotionActive(Potion.jump)) {
                    if (!mc.thePlayer.isMoving()) {
                        mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 8 == 0 ? -.42f : 0.42F;
                        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                    } else {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.42f;
                        } else if (mc.thePlayer.motionY < 0.17D && mc.thePlayer.motionY > 0.16D) {
                            mc.thePlayer.motionY = -0.01f;
                        }
                    }
                }
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.onGround) {
                blockEntry.position.add(0, -1, 0);
            }
    		if (onServer("hypixel")) {
      			mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockEntry.position.add(0, (mc.gameSettings.keyBindSneak.isKeyDown() ? (mc.thePlayer.onGround ? -1 : 0) : 0), 0), blockEntry.face, new Vec3(blockEntry.position.getX() + Math.random(), blockEntry.position.getY() + Math.random(), blockEntry.position.getZ() + Math.random()));
      		  		} else {
    			mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockEntry.position.add(0, (mc.gameSettings.keyBindSneak.isKeyDown() ? (mc.thePlayer.onGround ? -1 : 0) : 0), 0), blockEntry.face, new Vec3(blockEntry.position.getX() + Math.random(), blockEntry.position.getY() + Math.random(), blockEntry.position.getZ() + Math.random()));
    		} 
    		lastBlockData = blockEntry;
    		
    		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
    		
    		if (switcher.getValue().equals(true)) {
    			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = heldItem));
    		}
        }
    }
    
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || invalid.contains(((ItemBlock) item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
    
    
    
	/*
	 * 
	 * Credits for findDown, find, and rayTracing:
	 * 
	 * FlyCode
	 *  
	 * Taken from: The private edition of Depression src he gave me
	 * 
	 * */
    
    public BlockData findDown(Vec3 offset3) {
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector().add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || !rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockData(offset, invert[facing.ordinal()]);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || !rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new BlockData(offset2, invert[facing.ordinal()]);
            }
        }
        return null;
    }
    
    public BlockData find(Vec3 offset3) {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(new Vec3(x,y,z).add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || !rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockData(offset, invert[facing.ordinal()]);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0), new BlockPos(-3, 0, 0), new BlockPos(3, 0, 0), new BlockPos(2, -1, 0), new BlockPos(-2, -1, 0), new BlockPos(0, -1, 2), new BlockPos(0, -1, -2)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || !rayTrace(mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new BlockData(offset2, invert[facing.ordinal()]);
            }
        }
        return null;
    }
    
    public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.75, (double) position.getZ() + 0.5);
        return point.add(offset);
    }
    
    public boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!(boundingBox = boundingBox.offset(blockPosition)).isVecInside(point)) continue;
            return false;
        }
        return true;
    }

    private int getBlockColor(int count) {
        float f = count;
        float f1 = 64;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }

    public static float[] getFacingRotations(final int paramInt1, final double d, final int paramInt3) {
        final EntityPig localEntityPig = new EntityPig(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return getRotationsNeeded(localEntityPig);
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }
    
    public static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
    
    public enum Mode {
    	NORMAL
    }
  
    public BlockData getBlockData2(BlockPos pos) {
        if (!invalid.contains(mc.theWorld.getBlockState((pos.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos3.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos3.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos3.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos3.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos2.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos2.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos2.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos2.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos1.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos1.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos1.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos1.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos1.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos1.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos1.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos4.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos4.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos4.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos4.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos5.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos5.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos5.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos5.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos5.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos5.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos5.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos5.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos5.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos6.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos6.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos6.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos6.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState((pos6.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos6.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos6.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos6.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos6.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos6.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos6.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos6.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState((pos7.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos7.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos8.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos9.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 1, 0), EnumFacing.DOWN);
        }
        return null;
    }
}
