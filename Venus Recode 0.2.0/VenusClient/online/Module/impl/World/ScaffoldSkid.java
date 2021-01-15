package VenusClient.online.Module.impl.World;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ScaffoldSkid extends Module {
    public ScaffoldSkid() {
        super("Scaffold", "Scaffold", Keyboard.KEY_C, Category.WORLD);
    }

    @Override
    public void setup() {
        Client.instance.setmgr.rSetting(new Setting("Scaffold Tower", this, true));
        Client.instance.setmgr.rSetting(new Setting("Scaffold Boost", this, false));

    }

    private final List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, (Block)Blocks.chest, Blocks.dispenser, Blocks.air, (Block)Blocks.water, (Block)Blocks.lava,
            (Block)Blocks.flowing_water, (Block)Blocks.flowing_lava, (Block)Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, (Block)Blocks.stone_slab, (Block)Blocks.wooden_slab, (Block)Blocks.stone_slab2, (Block)Blocks.red_mushroom, (Block)Blocks.brown_mushroom,
            (Block)Blocks.yellow_flower, (Block)Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, (Block)Blocks.stained_glass_pane, Blocks.iron_bars, (Block)Blocks.cactus, Blocks.ladder, Blocks.web);

    private final List<Block> validBlocks = Arrays.asList(Blocks.air, (Block)Blocks.water, (Block)Blocks.flowing_water, (Block)Blocks.lava, (Block)Blocks.flowing_lava);

    private final BlockPos[] blockPositions = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };

    private final EnumFacing[] facings = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };

    private final TimeHelper towerStopwatch = new TimeHelper();

    private final Random rng = new Random();

    private float[] angles = new float[2];

    private boolean rotating;

    private int slot;

    @Override
    public void onEnable() {
        this.angles[0] = 999.0F;
        this.angles[1] = 999.0F;
        this.towerStopwatch.reset();
        this.slot = mc.thePlayer.inventory.currentItem;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.inventory.currentItem = this.slot;
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }

	@EventTarget
	public final void onUpdate(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
			EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
	  		return;
		}
	    boolean tower = Client.instance.setmgr.getSettingByName("Scaffold Tower").getValBoolean();
	    mc.thePlayer.cameraYaw = 0.105F;
	    EntityPlayerSP player = mc.thePlayer;
	    WorldClient world = mc.theWorld;
	    double yDif = 1.0D;
	    BlockData data = null;
	    double posY;
	    for (posY = player.posY - 1.0D; posY > 0.0D; posY--) {
	        BlockData newData = getBlockData(new BlockPos(player.posX, posY, player.posZ));
	        if (newData != null) {
	            yDif = player.posY - posY;
	            if (yDif <= 3.0D) {
	                data = newData;
	                break;
	            }
	        }
	    }
	    int slot = -1;
	    int blockCount = 0;
	    for (int i = 0; i < 9; i++) {
	        ItemStack itemStack = player.inventory.getStackInSlot(i);
	        if (itemStack != null) {
	            int stackSize = itemStack.stackSize;
	            if (isValidItem(itemStack.getItem()) && stackSize > blockCount) {
	                blockCount = stackSize;
	                slot = i;
	            }
	        }
	    }
	    if (slot == -1);
	    if (data != null && slot != -1) {
	
	        BlockPos pos = data.pos;
	        Block block = world.getBlockState(pos.offset(data.face)).getBlock();
	        Vec3 hitVec = getVec3(data);
	        if (!this.validBlocks.contains(block) || isBlockUnder(yDif))
	            return;
	        if (event.isPre()) {
	            event.setPitch(79.44F);
	            if (mc.gameSettings.keyBindJump.isKeyDown()) {
	                if (!player.isMoving() && tower) {
	                    player.motionX = 0.0D;
	                    player.motionY = 0.41982D;
	                    player.motionZ = 0.0D;
	                    if (this.towerStopwatch.hasReached(1500L)) {
	                        player.motionY = -0.28D;
	                        this.towerStopwatch.reset();
	                    }
	                }
	
	            }
	            this.towerStopwatch.reset();
	        }
	        if (event.getType() == EventMotionUpdate.Type.POST){
	            int last;
	            last = player.inventory.currentItem;
	            player.inventory.currentItem = slot;
	            if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos, data.face, hitVec))
	                //if (swing.getObject()) {
	                 //   player.swingItem();
	               // }else {
	                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C0APacketAnimation());
	                }
	            //player.inventory.currentItem = last;
	
	        }
	    }
	    //}
	
		@EventTarget
		public void onMotion(EventMove event) {
		    boolean boost = Client.instance.setmgr.getSettingByName("Scaffold Boost").getValBoolean();
		    if(boost) {
		    	if(!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0) {
		    		mc.timer.timerSpeed = 1.5f;
		    	}
		    }
		}

    private boolean isBlockUnder(double yOffset) {
        EntityPlayerSP player = mc.thePlayer;
        return !this.validBlocks.contains(mc.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
    }

    private Vec3 getVec3(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        x += face.getFrontOffsetX() / 2.0D;
        z += face.getFrontOffsetZ() / 2.0D;
        y += face.getFrontOffsetY() / 2.0D;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber(0.3D, -0.3D);
            z += randomNumber(0.3D, -0.3D);
        } else {
            y += randomNumber(0.49D, 0.5D);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST)
            z += randomNumber(0.3D, -0.3D);
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH)
            x += randomNumber(0.3D, -0.3D);
        return new Vec3(x, y, z);
    }

    private double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    private BlockData getBlockData(BlockPos pos) {
        BlockPos[] blockPositions = this.blockPositions;
        EnumFacing[] facings = this.facings;
        WorldClient world = mc.theWorld;
        BlockPos posBelow = new BlockPos(0, -1, 0);
        List<Block> validBlocks = this.validBlocks;
        if (!validBlocks.contains(world.getBlockState(pos.add((Vec3i)posBelow)).getBlock()))
            return new BlockData(pos.add((Vec3i)posBelow), EnumFacing.UP);
        for (int i = 0, blockPositionsLength = blockPositions.length; i < blockPositionsLength; i++) {
            BlockPos blockPos = pos.add((Vec3i)blockPositions[i]);
            if (!validBlocks.contains(world.getBlockState(blockPos).getBlock()))
                return new BlockData(blockPos, facings[i]);
            for (int i1 = 0; i1 < blockPositionsLength; i1++) {
                BlockPos blockPos1 = pos.add((Vec3i)blockPositions[i1]);
                BlockPos blockPos2 = blockPos.add((Vec3i)blockPositions[i1]);
                if (!validBlocks.contains(world.getBlockState(blockPos1).getBlock()))
                    return new BlockData(blockPos1, facings[i1]);
                if (!validBlocks.contains(world.getBlockState(blockPos2).getBlock()))
                    return new BlockData(blockPos2, facings[i1]);
            }
        }
        return null;
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock iBlock = (ItemBlock)item;
            Block block = iBlock.getBlock();
            return !this.invalidBlocks.contains(block);
        }
        return false;
    }

    private static class BlockData {
        public final BlockPos pos;

        public final EnumFacing face;

        private BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

}
