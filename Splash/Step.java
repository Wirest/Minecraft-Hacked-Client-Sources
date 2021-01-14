package splash.client.modules.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventStep;
import splash.client.modules.combat.Criticals;
import splash.client.modules.combat.Criticals.Mode;
import splash.client.modules.movement.Flight;
import splash.utilities.time.Stopwatch;

public class Step extends Module { 

	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.NCP, this);
	public enum Mode {
		NCP, MOTION, VANILLA
	}
    public static long lastStep, lastFuck, lastPacket;
    private boolean stepping;
    private static Map<Float, float[]> offsets = new HashMap<>();
    private int ticks;
    private int ncpNextStep;
    public Step() {
		super("Step", "Goes up blocks for you.", ModuleCategory.MOVEMENT);
        offsets.put(1.0F, new float[]{0.41999998688698f, 0.753f});
        offsets.put(1.5F, new float[]{0.41999998688698f, 0.75f, 1, 1.16f});
        offsets.put(2.0F, new float[]{0.41999998688698f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f});
    }
    
    @Collect
    public void onMove(EventMove e) {
 
    	if (modeValue.getValue().equals(Mode.MOTION) && mc.thePlayer.isCollidedHorizontally) {
    		/*
    		 * 
    		 * @author LiquidBounce devs
    		 * 
    		 * Credit: whoever made the motion step for liquidbounce, this was from their open source client on github
    		 * 
    		 * */
    		if (couldStep() && !mc.thePlayer.isOnLadder()) {
    			mc.thePlayer.motionY = 0;
    			e.setY(0.41999998688698);
    			e.setX(0);
    			e.setY(0);
    			ncpNextStep = 2;
    		}
    		
    		if (ncpNextStep == 2) {
    			e.setY(0.7531999805212 - 0.41999998688698);
    			e.setX(0);
    			e.setY(0);
    			ncpNextStep = 3;
    		}
    		
    		if (ncpNextStep == 3) {
    			e.setY(1.001335979112147 - 0.7531999805212);
    			float yaw = mc.thePlayer.getDirection();
    			e.setMovementSpeed(.1);
    			ncpNextStep = 0;
    		}
    	}
    }
    
    @Collect
    public void onPlayerUpdate(EventPlayerUpdate e) {
    	if (modeValue.getValue().equals(Mode.NCP)) {
            if ((System.currentTimeMillis() - lastStep) > 230L) {
                ticks = 0;
            } else {
                if (mc.thePlayer.ticksExisted % 2 == 0) ticks++;
            }
    	}
    }
    

    @Collect
    public void onStep(EventStep e) {

    	if (modeValue.getValue().equals(Mode.VANILLA) && mc.thePlayer.isCollidedHorizontally) {
            if (Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive()) return;
            if (Splash.getInstance().getModuleManager().getModuleByClass(Speed.class).isModuleActive()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown()) return;
    		e.setStepHeight(2.5f);
    	}
    	if (modeValue.getValue().equals(Mode.NCP)) {

            if (Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive()) return;
            if (Splash.getInstance().getModuleManager().getModuleByClass(Speed.class).isModuleActive()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown()) return;
    		/*
    		 * 
    		 * @author FlyCode
    		 * 
    		 * NCP Step Credit: FlyCode
    		 * 
    		 * Code edits: interfaced with criticals, replaced mc.thePlayer.isOnLiquid() && mc.thePlayer.isInLiquid() with .isInWater & .isInLava
    		 * 
    		 * */
	        if (mc.thePlayer != null && mc.theWorld != null && (System.currentTimeMillis() - lastFuck) > 20L) {
	
	            if (e.getStepHeight() != 0.6f || mc == null || mc.thePlayer == null)
	                return;
	
	            if (!mc.thePlayer.isCollidedHorizontally || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isOnLadder() || mc.thePlayer.checkBlockAbove(0.1f))
	                return;

	
	            float stepHeight;
	            if ((stepHeight = getNeededStepHeight()) > 2D)
	                return;
	
	            if ((stepHeight == 1 && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
	                    mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX, 0.6, mc.thePlayer.motionZ)).isEmpty())
	                    || !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(
	                    mc.thePlayer.motionX, stepHeight + 0.01, mc.thePlayer.motionZ)).isEmpty())
	                return;
	
	
	            double radius = 0.50;
	
	            double currentX = mc.thePlayer.posX, currentY = mc.thePlayer.posY, currentZ = mc.thePlayer.posZ;
	
	            boolean isInvalid = false;
	            String[] invalidBlocks = {"snow", "chest", "slab", "stair"};
	
	            for (double x = currentX - radius; x <= currentX + radius; x++) {
	                for (double y = currentY - radius; y <= currentY + radius; y++) {
	                    for (double z = currentZ - radius; z <= currentZ + radius; z++) {
	                        if (!isInvalid) {
	                            String blockName = getBlockAtPos(new BlockPos(x, y, z)).getUnlocalizedName().toLowerCase();
	                            for (String s : invalidBlocks) {
	                                if (blockName.contains(s.toLowerCase())) isInvalid = true;
	                            }
	                        }
	                    }
	                }
	            }
	
	
	            if (isInvalid)
	                return;
	
	            float needed = getNeededStepHeight();
            	Criticals crits = ((Criticals)Splash.getInstance().getModuleManager().getModuleByClass(Criticals.class));
	            if (Splash.getInstance().getModuleManager().getModuleByClass(Criticals.class).isModuleActive() && crits.airTime != 0) {
	            	if (crits.modeValue.getValue().equals(Criticals.Mode.POSITION)) {
	            		crits.waitTicks = 4;
	            		crits.forceUpdate();
	            	}
	            }
	            e.setStepHeight(stepHeight);
	
	
	            if ((System.currentTimeMillis() - lastPacket) > (needed > 1.00 ? 355L : 100L)) {
	                offsets.forEach((a, b) -> {
	                    if (a == needed) {
	                        for (double ab : b) {
	                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + ab, mc.thePlayer.posZ, true));
	                        }
	                        lastPacket = System.currentTimeMillis();
	                    }
	                });
	            }
	
	            lastStep = System.currentTimeMillis();
	            stepping = true;
	            lastFuck = System.currentTimeMillis();
	        }
	        if ((System.currentTimeMillis() - lastStep) > 150L && stepping) {
	            stepping = false;
	            mc.timer.timerSpeed = 1f;
	        }
    	}
    }
    
    private boolean couldStep() {
        float yaw = mc.thePlayer.getDirection();
        double x = -Math.sin(yaw) * 0.4;
        double z = Math.cos(yaw) * 0.4;
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(x, 1.001335979112147, z)).isEmpty();
    }
    
    public float getNeededStepHeight() {

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX, 1.1, mc.thePlayer.motionZ)).size() == 0)
            return 1.0F;

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX, 1.6, mc.thePlayer.motionZ)).size() == 0)
            return 1.5F;

        return (float) 2D;
    }
    public Block getBlockAtPos(BlockPos pos) {
        IBlockState blockState = getBlockStateAtPos(pos);
        if (blockState == null)
            return null;
        return blockState.getBlock();
    }

    public IBlockState getBlockStateAtPos(BlockPos pos) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null)
            return null;
        return Minecraft.getMinecraft().theWorld.getBlockState(pos);
    }

    public boolean isOnGround() {
        for (double d = 0.0; d <= 1.00; d+=0.05) {
            if (!Objects.requireNonNull(getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - d, mc.thePlayer.posZ))).getUnlocalizedName().toLowerCase().contains("air")) {
                return true;
            }
        }
        return false;
    }
}
