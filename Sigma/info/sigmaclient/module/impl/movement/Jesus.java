package info.sigmaclient.module.impl.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventLiquidCollide;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;



public class Jesus extends Module {
	int stage, water;
	String MODE = "MODE";
	Timer timer = new Timer();
    public Jesus(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Basic", new String[]{"Basic", "Dolphin", "AAC"}), "Jesus method."));
       // settings.put(ONJUMP, new Setting<>("LagBack", false, "Disable speed if you get lagback."));
    }
    @Override
    public void onEnable(){
    	stage = 0;
    	water = 0;
    }
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventLiquidCollide.class, EventJump.class, EventPacket.class})
    public void onEvent(Event event) {
    	String currentJesus = ((Options) settings.get(MODE).getValue()).getSelected();
    	this.setSuffix(currentJesus);
    	if(event instanceof EventPacket){
    		EventPacket ep = (EventPacket)event;
    		Packet p = ep.getPacket();
    		if(p instanceof S08PacketPlayerPosLook){
    			stage = 0;
    		}
    	}
    	if(event instanceof EventJump){
    		EventJump ej = (EventJump)event;
    		if(BlockUtils.isOnLiquid(0.001))
    	    if(BlockUtils.isTotalOnLiquid(0.001) && mc.thePlayer.onGround && !mc.thePlayer.isInWater()){
    	    	ej.setCancelled(mc.thePlayer.ticksExisted % 2 != 0);   	
    	    }
    	}
    	if(event instanceof EventUpdate){
    		EventUpdate em = (EventUpdate)event;
    		if(!em.isPre())
    			return;
    		boolean sh = shouldJesus();
    		if(mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && sh && !currentJesus.equalsIgnoreCase("AAC")){
    			mc.thePlayer.motionY = 0.09;
    		}
    		if(currentJesus.equalsIgnoreCase("Basic")){
    			if(BlockUtils.isOnLiquid(0.001))
    	    	if(BlockUtils.isTotalOnLiquid(0.001) && mc.thePlayer.onGround && !mc.thePlayer.isInWater()){
    	    		em.setY(em.getY() + (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0000000001D : -0.000000000001D));
    	    	}
    		}

    		if(currentJesus.equalsIgnoreCase("Dolphin")){    
    		if(mc.thePlayer.onGround && !mc.thePlayer.isInWater() && sh){
    			stage = 1;
				timer.reset();
    		}
    		if(stage > 0 && !timer.delay(2500)){
    			if((mc.thePlayer.isCollidedVertically  && !MoveUtils.isOnGround(0.001)) || mc.thePlayer.isSneaking()){
    				stage = -1;
    			}
    			mc.thePlayer.motionX *= 0;
    			mc.thePlayer.motionZ *= 0;
    			if(!PlayerUtil.isInLiquid() && !mc.thePlayer.isInWater()){
    				MoveUtils.setMotion(0.25 + MoveUtils.getSpeedEffect() * 0.05);
    			}
    			double motionY = getMotionY(stage);
    			if(motionY != -999){
    				mc.thePlayer.motionY = motionY;
    				
    			}
    				
    				stage +=1;
    			}
    		}
    		if(currentJesus.equalsIgnoreCase("AAC")){
    			if(mc.thePlayer.isInWater() && BlockUtils.isTotalOnLiquid(-1)){
    				if(water == 1){
    					stage ++;
                		double motionY = -0.021;
                		if(stage >= 2){
                			if(stage == 2){
                    			motionY = -0.004;
                			}else{
                				stage = 0;
                    			motionY = 0.025;
                			}
                		}
                		mc.thePlayer.motionX *= 1.17;
                		mc.thePlayer.motionZ *= 1.17;
                		mc.thePlayer.motionY = 0;
                		double x = mc.thePlayer.posX;
                		double z = mc.thePlayer.posZ;
                		double y = mc.thePlayer.posY;
                    	if(!mc.gameSettings.keyBindJump.isPressed())
                    		mc.thePlayer.setPosition(x, y + motionY, z);
                		
    				}
    				water = 1;
            		
            	}else{
            		water = 0;
            	}
    		}
    	}
    	
    	if(event instanceof EventLiquidCollide && !currentJesus.equalsIgnoreCase("AAC")){
    		EventLiquidCollide ebb = (EventLiquidCollide) event;
    		
    		int nigga = -1;
    		 
            if (ebb.getPos().getY() + 0.9 < mc.thePlayer.boundingBox.minY) {
            	if(mc.theWorld.getBlockState(ebb.getPos()).getProperties().get(BlockLiquid.LEVEL) instanceof Integer){
        			nigga = (int)mc.theWorld.getBlockState(ebb.getPos()).getProperties().get(BlockLiquid.LEVEL);
        		} 
            	if(nigga <= 4){
            		ebb.setBounds(new AxisAlignedBB(ebb.getPos().getX(), ebb.getPos().getY(), ebb.getPos().getZ(), ebb.getPos().getX() + 1, ebb.getPos().getY() + (currentJesus.equalsIgnoreCase("Basic")? 1 : 0.999), ebb.getPos().getZ() + 1));
                    ebb.setCancelled(shouldSetBoundingBox());
            	}
                
            }
    	}   
    }
    boolean shouldJesus(){
    	double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
    	ArrayList<BlockPos>pos = new ArrayList<BlockPos>(Arrays.asList(new BlockPos(x + 0.3, y, z+0.3),
    			 new BlockPos(x - 0.3, y, z+0.3),new BlockPos(x + 0.3, y, z-0.3),new BlockPos(x - 0.3, y, z-0.3)));
    	for(BlockPos po : pos){
    		if(!(mc.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid))
    			continue;
    		if(mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) instanceof Integer){
        		if((int)mc.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) <= 4){
        			return true;
        		}
        	}
    	}
    	
    	
    	return false;
    }
    public double getMotionY(double stage){
    	stage --;
    	double[] motion = new double[]{0.500,0.484,0.468,0.436,0.404,0.372,0.340,0.308,0.276,0.244,0.212,0.180,0.166,0.166,
    			0.156,0.123,0.135,0.111,0.086,0.098,0.073,0.048,0.06,0.036,0.0106,0.015,0.004,0.004,0.004,0.004,
    					-0.013,-0.045,-0.077,-0.109};
    	if(stage < motion.length && stage >= 0)
    		return motion[(int)stage];
    	else
    		return -999;
    	
    }
    private boolean shouldSetBoundingBox() { 	
        return (!mc.thePlayer.isSneaking()) && (mc.thePlayer.fallDistance < 12.0F);
    }
}
