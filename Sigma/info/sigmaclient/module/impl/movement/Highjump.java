package info.sigmaclient.module.impl.movement;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;

public class Highjump extends Module {
	public static String MODE = "MODE";
	String BOOST = "BOOST";
	public static String OFF = "TOGGLE";
	int stage = 0;
	Timer timer = new Timer();
	double speed, fall;
	boolean jumped = false;
    public Highjump(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Highjump Mode", "Mineplex", new String[]{"Mineplex", "AAC" , "Basic", "Cubecraft", "Hypixel"}), "Highjump bypass method."));
        settings.put(BOOST, new Setting<>(BOOST, 3, "Highjump boost.", 0.1, 1, 10));
        settings.put(OFF, new Setting<>(OFF, true, "Auto disable on landing."));
    }
    int groundTicks = 0;
    @Override
    public void onDisable(){
    	setMotion(0.2);
    	mc.timer.timerSpeed = 1f;
    }
    @Override
    public void onEnable() {
    	fall = 0;
    	stage = -1;
    	jumped = false;
    	groundTicks = 0;
    	speed =0;
    	if(mc.theWorld != null)
    	if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Cubecraft"))
    	if(MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically){
    		double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
    		for (int index = 0; index <49; index++) {
    			mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625D, z, false));
        	    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        	}
    		mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
    		groundTicks ++;
			mc.thePlayer.motionY =2.2;
			stage = -2;
		
		}else{
			stage = 999;
		}

    
    }
 
    
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class, EventJump.class})
    public void onEvent(Event event) {
    	String currentMode = ((Options) settings.get(MODE).getValue()).getSelected();
    	this.setSuffix(currentMode);
    	if(event instanceof EventUpdate){
    		EventUpdate em = (EventUpdate)event;
    		if(currentMode.equalsIgnoreCase("Hypixel")){
    			if(em.isPre()){
    				if(MoveUtils.isOnGround(0.0001) && mc.thePlayer.isCollidedVertically){
    					fall = 0;
    					jumped = false;
    				}
    					
    				double motion = ((Number) settings.get(BOOST).getValue()).doubleValue();
    				if(mc.thePlayer.motionY < -0.08)
        				fall -= mc.thePlayer.motionY;
    				if(fall >9){
    					if(!jumped || mc.thePlayer.posY < speed){
    						
        					speed = mc.thePlayer.posY;
        					MoveUtils.setMotion(0.28);
        					fall = 0;
            				mc.thePlayer.motionY = motion;
    					}
    					jumped = true;
    				}
    			}
    		}
    		if(currentMode.equalsIgnoreCase("Cubecraft")){
    			if(em.isPre()){

    				if(!MoveUtils.isOnGround(0.4)){
    					if(stage == 0){
    						mc.thePlayer.motionY = 0.42;
    					}
    					if(stage != -999){
    						double airspeed = 0.46 - (double)stage/200;
    						
    						setMotion(airspeed);				
    						stage ++;
    						if(mc.thePlayer.motionY < -0.2){
    							double val = 0.05;
    							double diff = val * (mc.thePlayer.fallDistance);
    							double test = diff>val?val - mc.thePlayer.fallDistance/300:diff - mc.thePlayer.fallDistance/1500;
    							if(test < 0)
    								test = 0;
    							mc.timer.timerSpeed = 0.3f;
    							speed ++;
    							if(speed >=2){
    								speed = 0;								
    								setMotion(1.9);
    							}else{
    								setMotion(0.3);
    							}
    						}
    					}
    				}else{
    					if(mc.timer.timerSpeed == 0.3f){
    						setMotion(0.1);
    						mc.timer.timerSpeed = 1;
    					}	
    					stage = -999;
    					if (mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.001)){
    	    				if((Boolean) settings.get(OFF).getValue()){
    	    					if(groundTicks >= 1 ){
    	            				groundTicks = 0;
    	            				toggle();
    	            			}
    	    				}else{
    	    					
    	    		    		double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
    	    		    		for (int index = 0; index <49; index++) {
    	    		    			mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625D, z, false));
    	    		        	    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    	    		        	}
    	    		    		mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
    	    		    		groundTicks ++;
    	    					mc.thePlayer.motionY =2.2;
    	    					stage = -2;
    	    					speed =0;
    	    				}
    					}
    				}
    					
    			}
    		}
    		if(currentMode.equalsIgnoreCase("Mineplex")){
    			if(!Client.um.isPremium()){
					Notifications.getManager().post("Premium Bypass", "This mode is a premium only bypass", Notifications.Type.WARNING);
					toggle();
					return;
				}
    			double x = mc.thePlayer.posX;
                double z = mc.thePlayer.posZ;
                double y = mc.thePlayer.posY;
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float YAW = mc.thePlayer.rotationYaw;
                double dist = 0.1;
    			double nextX = x + (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            	double nextZ = z + (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            	
	    		if(em.isPre()){
	    			
	    			AxisAlignedBB bb = new AxisAlignedBB(nextX-0.3, y, nextZ-0.3, nextX+0.3, y+2, nextZ+0.3);
	    			boolean should = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, -1, 0)).isEmpty();
	    			if(jumped){
	    				if(!MoveUtils.isOnGround(0.001)){
	    					if(mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0){
		    					float var1 = mc.thePlayer.rotationYaw * 0.017453292F;
		    					mc.thePlayer.motionX =- MathHelper.sin(var1)*0.05;
		    					mc.thePlayer.motionZ =+  MathHelper.cos(var1)*0.05;
		    				}					
	    					double speed = 0.55 - (double)stage/650;
	    					if(speed < MoveUtils.defaultSpeed())
	    						speed = MoveUtils.defaultSpeed();
		    				MoveUtils.setMotion(speed);
		    				stage ++;
	    				}else{
	    					if((Boolean) settings.get(OFF).getValue()){
		            			toggle();
		            		}
	    					jumped = false;
	    				}
	    			}
	    			if(should && MoveUtils.isOnGround(0.001)){
	    				double a = getBestMineplexExploit(nextX, y, nextZ);
	    				if(a != 11){
	    			
		    				stage = 1;
		    				jumped = true;
		    				C04PacketPlayerPosition p1 = new C04PacketPlayerPosition(nextX, y+a, nextZ, true);
		    				C04PacketPlayerPosition p2 = new C04PacketPlayerPosition(nextX, y, nextZ, true);
		    				mc.thePlayer.sendQueue.addToSendQueue(p2);
		    				mc.thePlayer.sendQueue.addToSendQueue(p1);
		    				mc.thePlayer.setPosition(nextX, y, nextZ);
		    				em.setX(nextX);
		    				em.setZ(nextZ);
		    				mc.thePlayer.motionY = ((Number) settings.get(BOOST).getValue()).doubleValue();
	    				}
	    			}
	    		} 
    		}
    		if(currentMode.equalsIgnoreCase("AAC")){
    			if(em.isPre()){
    				if(mc.thePlayer.onGround){
    					jumped = true;
        				stage = 0;
        			}else{
        				double motionY = 0;
        				if(stage == 0){
        					if(mc.thePlayer.motionY == 0.3479000067710857)
        						motionY = 0.360899999999992;
        					else
        						jumped = false;
        				}else if(stage == 1){
        					motionY = 0.290241999999991;
        				}else if(stage == 2){
        					motionY = 0.220997159999987;
        				}else if(stage == 3){
        					motionY = 0.13786084000003104;
        				}else if(stage == 4){
        					motionY = 0.055;
        				}	
        				if(stage < 5 && jumped){
        					mc.thePlayer.motionY = motionY;
        					stage ++;
        				}
        				
        			}
    			}
    		}
    	}
    	if(event instanceof EventPacket){
    		EventPacket ep = (EventPacket)event;
    		Packet p = ep.getPacket();
    		if(currentMode.equalsIgnoreCase("Mineplex")){
    		if(ep.isIncoming()){
    			if(p instanceof S08PacketPlayerPosLook){
    				toggle();
    				mc.thePlayer.onGround = false;
    				mc.thePlayer.motionX *= 0;
    				mc.thePlayer.motionZ *= 0;
    				mc.thePlayer.jumpMovementFactor = 0;
    				Notifications.getManager().post("Lagback checks", "Disabled highjump",  Notifications.Type.WARNING);
    			}
    		}
    		}
    	}
    	if(event instanceof EventJump){
    		EventJump ej = (EventJump)event;
    		if(currentMode.equalsIgnoreCase("Basic")){
    			if(ej.isPre()){
    				ej.setMotionY(((Number) settings.get(BOOST).getValue()).doubleValue());
    			}
    		}else if(currentMode.equalsIgnoreCase("AAC")){
    			if(ej.isPre()){
    				ej.setMotionY(0.434999999999998);
    			}
    		}else if(currentMode.equalsIgnoreCase("Mineplex")){
    			if(timer.delay(2000)){
    				timer.reset();
    				Notifications.getManager().post("HighJump", "Walk off an island to highjump.",  Notifications.Type.INFO);
    			}
    		}else if(currentMode.equalsIgnoreCase("Hypixel")){
    			if(timer.delay(2000)){
    				timer.reset();
    				Notifications.getManager().post("HighJump", "Jump in the void to highjump.",  Notifications.Type.INFO);
    			}
    		}
    	}
  
    }
    public double getBestMineplexExploit(double x, double y, double z){
    	double yOff = -1.5;
    	AxisAlignedBB bb = new AxisAlignedBB(x-0.3, y + yOff, z-0.3, x + 0.3, y+2 + yOff, z + 0.3);
    	do{
    		bb = new AxisAlignedBB(x-0.3, y + yOff, z-0.3, x + 0.3, y+2 + yOff, z + 0.3);
    		if((!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, -1, 0)).isEmpty() &&
    				mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty() &&
    				mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0, -0.5, 0)).isEmpty()&& yOff <= -4.5) || yOff <= -9){
    			
    			return yOff;
    		}
    		yOff-=0.5;
    	}while(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty());	
    	return 11;
    }
    public boolean isInBlock(Vec3 vec){
    	double width = 0.3;
    	Vec3 vec1 = new Vec3(vec.xCoord-width, vec.yCoord, vec.zCoord-width);
    	Vec3 vec2 = new Vec3(vec.xCoord+width, vec.yCoord + mc.thePlayer.getEyeHeight(), vec.zCoord + width);
    	Vec3 vec3 = new Vec3(vec.xCoord-width, vec.yCoord, vec.zCoord+width);
    	Vec3 vec4 = new Vec3(vec.xCoord+width, vec.yCoord + mc.thePlayer.getEyeHeight(), vec.zCoord-width);
    	Vec3 vec5 = new Vec3(vec.xCoord+width, vec.yCoord, vec.zCoord+width);
    	Vec3 vec6 = new Vec3(vec.xCoord-width, vec.yCoord + mc.thePlayer.getEyeHeight(), vec.zCoord-width);
    	Vec3 vec7 = new Vec3(vec.xCoord+width, vec.yCoord, vec.zCoord-width);
    	Vec3 vec8 = new Vec3(vec.xCoord-width, vec.yCoord + mc.thePlayer.getEyeHeight(), vec.zCoord + width);
    	if(mc.theWorld.rayTraceBlocks(vec1, vec2) != null && mc.theWorld.rayTraceBlocks(vec1, vec2).typeOfHit == MovingObjectType.BLOCK){
    		BlockPos pos = mc.theWorld.rayTraceBlocks(vec1, vec2).getBlockPos();
    		Block block = mc.theWorld.getBlockState(pos).getBlock();
    		if(!block.isTranslucent()){
    			return true;
    		}
    	}
    	if(mc.theWorld.rayTraceBlocks(vec3, vec4) != null && mc.theWorld.rayTraceBlocks(vec3, vec4).typeOfHit == MovingObjectType.BLOCK){
    		BlockPos pos = mc.theWorld.rayTraceBlocks(vec3, vec4).getBlockPos();
    		Block block = mc.theWorld.getBlockState(pos).getBlock();
    		if(!block.isTranslucent()){
    			return true;
    		}
    	}
    	if(mc.theWorld.rayTraceBlocks(vec5, vec6) != null && mc.theWorld.rayTraceBlocks(vec5, vec6).typeOfHit == MovingObjectType.BLOCK){
    		BlockPos pos = mc.theWorld.rayTraceBlocks(vec5, vec6).getBlockPos();
    		Block block = mc.theWorld.getBlockState(pos).getBlock();
    		if(!block.isTranslucent()){
    			return true;
    		}
    	}
    	if(mc.theWorld.rayTraceBlocks(vec7, vec8) != null && mc.theWorld.rayTraceBlocks(vec7, vec8).typeOfHit == MovingObjectType.BLOCK){
    		BlockPos pos = mc.theWorld.rayTraceBlocks(vec7, vec8).getBlockPos();
    		Block block = mc.theWorld.getBlockState(pos).getBlock();
    		if(!block.isTranslucent()){
    			return true;
    		}
    	}
    	return false;
    }
	private void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = (0.0D);
        	mc.thePlayer.motionX = (0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
}
