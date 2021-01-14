package info.sigmaclient.module.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventBlockBounds;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.misc.ChatUtil;

public class Spider extends Module {
	String MODE = "MODE";
	String MOTION = "MOTION";
    public Spider(ModuleData data) {
        super(data);
		settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Mineplex",
				new String[] { "Mineplex", "Vanilla", "Jump", "Cubecraft" }), "Spider method."));
		settings.put(MOTION, new Setting<>(MOTION, 0.2, "Spider motion", 0.005, 0.2, 3));
    }

    @Override
    public void onEnable(){
    }
    @Override
    public void onDisable(){
    	mc.timer.timerSpeed = 1;
    }
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventBlockBounds.class})
    public void onEvent(Event event) {
    	double motion = ((Number) settings.get(MOTION).getValue()).doubleValue();
        String mode = ((Options) settings.get(MODE).getValue()).getSelected();
        this.setSuffix(mode);
    	if(event instanceof EventUpdate){
    		EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {   
            	if(mc.thePlayer.isCollidedHorizontally && !isInsideBlock()){
            		int collideDir = getCollidedDir();
            		double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
            		switch(mode){
            		case"Jump":
            			if(mc.thePlayer.motionY <= -0.230527368912964 || mc.thePlayer.onGround){
            				if(!mc.thePlayer.onGround)
            				mc.thePlayer.setPosition(x, y - (y-(int)y), z);
        					mc.thePlayer.jump();
        				}
            			double phase = 0.006;
            			switch(collideDir){
            			case 1:
            				em.setX(mc.thePlayer.posX + phase);
            				break;
            			case 2:
            				em.setX(mc.thePlayer.posX - phase);
            				break;
            			case 3:
            				em.setZ(mc.thePlayer.posZ + phase);
            				break;
            			case 4:
            				em.setZ(mc.thePlayer.posZ - phase);
            				break;
            			}
            			break;
            		case"Mineplex":           			
            			switch(collideDir){
            			case 1:
            				//X+
            				mc.thePlayer.motionY = motion;
            				if(x < 0){
                        		em.setX((int)x- 0.3);
                        	}else{
                        		em.setX((int)(x+1)- 0.3);
                        	}
            				break;
            			case 2:
            				//X-
            			/*	if(x < 0){
                        		em.setX((int)(x-1)+ 0.3);
                        	}else{
                        		em.setX((int)x+ 0.3);
                        	} */
            				break;
            			case 3:
            				//Z+
            				mc.thePlayer.motionY = motion;
            				if(z < 0){
                        		em.setZ((int)z- 0.3);
                        	}else{
                        		em.setZ((int)(z+1)- 0.3);
                        	}
            				break;
            			case 4:
            				//Z-
            			/*	if(z < 0){
                        		em.setZ((int)(z-1)+ 0.3);
                        	}else{
                        		em.setZ((int)z+ 0.3);
                        	} */
            				break;
            			}
            			break;
            		case"Cubecraft":
            			phase = 0.0625;
            			switch(collideDir){
            			case 1:
            				em.setX(mc.thePlayer.posX + phase);
            				break;
            			case 2:
            				em.setX(mc.thePlayer.posX - phase);
            				break;
            			case 3:
            				em.setZ(mc.thePlayer.posZ + phase);
            				break;
            			case 4:
            				em.setZ(mc.thePlayer.posZ - phase);
            				break;
            			}
            			mc.timer.timerSpeed = 0.3f;
            			if(collideDir != 0){
            				mc.thePlayer.motionY = motion;
            			}
            			break;
            		case"Vanilla":
            			mc.thePlayer.motionY = motion;
            			break;
            		}
            	}else if(mode.equalsIgnoreCase("Cubecraft")){
            		if(mc.timer.timerSpeed == 0.3f){
            			mc.timer.timerSpeed = 1;
            		}
            	}
            }
    	}else if (event instanceof EventBlockBounds) {
            EventBlockBounds ebb = (EventBlockBounds) event;
            assert mc.thePlayer != null;
            if ((ebb.getBounds() != null) && (ebb.getBounds().minY > mc.thePlayer.boundingBox.minY+1) && mode.equalsIgnoreCase("Jump")) {
                ebb.setCancelled(true);
            }
        }
        
    }
    int getCollidedDir(){
    	if(!mc.thePlayer.isCollidedHorizontally)
    		return 0;
    	double width = 0.2;
		AxisAlignedBB bb =new AxisAlignedBB(mc.thePlayer.posX - 0.29, mc.thePlayer.posY+1.9, mc.thePlayer.posZ + 0.29,
				mc.thePlayer.posX + 0.29, mc.thePlayer.posY ,mc.thePlayer.posZ - 0.29);
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3000001, 0, 0)).isEmpty()) {
			return 1;
		}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3000001, 0, 0)).isEmpty()) {
			return 2;
		}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, 0.3000001)).isEmpty()) {
			return 3;
		}else if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, 0, -0.3000001)).isEmpty()) {
			return 4;
		}
		return 0;
	
    }
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null) {
                            if (mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
