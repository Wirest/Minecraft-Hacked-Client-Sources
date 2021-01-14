package info.sigmaclient.module.impl.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.sigmaclient.Client;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.combat.Criticals;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Step extends Module {
	public final static String MODE = "MODE";
	public final static String STEP = "HEIGHT";
	public final static String NCPHEIGHT = "HEIGHT2";
	public final static String TIMER = "TIMER";
	public final static String DELAY= "DELAY";
	boolean resetTimer;
	Timer time = new Timer();
	public static Timer lastStep = new Timer();
    public Step(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Step Mode", "NCP", new String[]{"NCP", "Vanilla", "AAC", "Cubecraft"}), "Step method."));
        settings.put(STEP, new Setting<>(STEP, 2, "Vanilla & Cubecraft step height.", 0.5, 1, 10));
        settings.put(NCPHEIGHT, new Setting<>(NCPHEIGHT, 2.5, "NCP step height.", 0.5, 1, 2.5));
        settings.put(TIMER, new Setting<>(TIMER, 0.37, "NCP step timer.", 0.01, 0.2, 1));
        settings.put(DELAY, new Setting<>(DELAY, 0.5, "Delay before steping again.", 0.1, 0, 2));
    }

    @Override
    public void onEnable(){
    	resetTimer = false;
    }
    @Override
    public void onDisable(){
    	mc.timer.timerSpeed = 1;
    }
    @Override
    @RegisterEvent(events = {EventStep.class, EventJump.class})
    public void onEvent(Event event) {
    	String Mode = ((Options) settings.get(MODE).getValue()).getSelected();
    	this.setSuffix(Mode);
    	double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
    	if(event instanceof EventJump){
    		EventJump ej = (EventJump)event;
    		if(ej.isPre()){
    			if(!lastStep.delay(60) && Mode.equalsIgnoreCase("Cubecraft")){
    				ej.setCancelled(true);
    			}
    		}
    	}
        if (event instanceof EventStep) {
        	EventStep es = (EventStep)event;
        	double stepValue = 1.5D;
        	final float timer = ((Number)settings.get(TIMER).getValue()).floatValue();
        	final float delay = ((Number)settings.get(DELAY).getValue()).floatValue() * 1000;
        	
        	if(Mode.equalsIgnoreCase("Vanilla") || Mode.equalsIgnoreCase("Cubecraft"))
        		stepValue = ((Number)settings.get(STEP).getValue()).doubleValue();
        	if(Mode.equalsIgnoreCase("NCP"))
        		stepValue = ((Number)settings.get(NCPHEIGHT).getValue()).doubleValue();
        	
        	if(resetTimer){
        		resetTimer = !resetTimer;
        		mc.timer.timerSpeed = 1;
        	}
        	if(!PlayerUtil.isInLiquid())
        	if (es.isPre()) {
        		if(mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.getIsKeyPressed() && time.delay(delay)){
        			es.setStepHeight(stepValue);		
                    es.setActive(true);
                    
        		}
        		
            }else{
           
            	double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
            	boolean canStep = rheight >= 0.625;
        		if(canStep){
        		 	lastStep.reset();
        		 	time.reset();
              		if (Killaura.isSetupTick() && Client.getModuleManager().isEnabled(Killaura.class)) {
        				Module crits = Client.getModuleManager().get(Criticals.class);
        				if(((Options) crits.getSetting(Criticals.PACKET).getValue()).getSelected().equalsIgnoreCase("minis") ||
        						((Options) crits.getSetting(Criticals.PACKET).getValue()).getSelected().equalsIgnoreCase("hminis")){
         	        		//if minis criticals is not on ground, send packet onground to be able to jump
        					Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
        							mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
         	        	}
        			}
        		}      
        		switch(Mode){
        		case"NCP": 		
        			if(canStep){
        				mc.timer.timerSpeed = timer - (rheight >= 1 ? Math.abs(1-(float)rheight)*((float)timer*0.55f) : 0);
        				if(mc.timer.timerSpeed <= 0.05f){
        					mc.timer.timerSpeed = 0.05f;
        				}
            			resetTimer = true;
            			ncpStep(rheight);	
        			}	
        			break;
        		case"Cubecraft":
        			if(canStep){   	 			
            			cubeStep(rheight);	
            			resetTimer = true;
        				mc.timer.timerSpeed = rheight < 2 ? 0.6f : 0.3f;
            		}
        			break;
        		case"AAC":
        			if(canStep){
        				if(rheight < 1.1){
        					mc.timer.timerSpeed = 0.5F;
        					resetTimer = true;
        				}else{
        					mc.timer.timerSpeed = 1 - (float)rheight*0.57f;
        					resetTimer = true;
        				}
            			aacStep(rheight);
            		}
        			break;
        		case "Vanilla":
        			
        			return;
        		}	
  

            }
        }
    }
    void cubeStep(double height){
    	double posX = mc.thePlayer.posX; double posZ = mc.thePlayer.posZ;
    	double y = mc.thePlayer.posY;
    	double first = 0.42;
    	double second = 0.75;
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    }
    void ncpStep(double height){
    	List<Double>offset = Arrays.asList(0.42,0.333,0.248,0.083,-0.078);
    	double posX = mc.thePlayer.posX; double posZ = mc.thePlayer.posZ;
    	double y = mc.thePlayer.posY;
    	if(height < 1.1){
    		double first = 0.42;
    		double second = 0.75;
    		if(height != 1){
    			first *= height;
    			second *= height;
        		if(first > 0.425){
        			first = 0.425;
        		}
        		if(second > 0.78){
        			second = 0.78;
        		}
        		if(second < 0.49){
        			second = 0.49;
        		}
    		}
    		if(first == 0.42)
    			first = 0.41999998688698;
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    		if(y+second < y + height)
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
    		return;
    	}else if(height <1.6){
    		for(int i = 0; i < offset.size(); i++){
        		double off = offset.get(i);
        		y += off;
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
        	}
    	}else if(height < 2.1){
    		double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869};
			for(double off : heights){
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}else{
        	double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869,2.019,1.907};
        	for(double off : heights){
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}
    	
    }
    void aacStep(double height){
    	double posX = mc.thePlayer.posX; double posY = mc.thePlayer.posY; double posZ = mc.thePlayer.posZ;
    	if(height < 1.1){
    		double first = 0.42;
    		double second = 0.75;

    		if(height > 1){
    			first *= height;
    			second *= height;
        		if(first > 0.4349){
        			first = 0.4349;
        		}else if(first < 0.405){
        			first = 0.405;
        		} 
    		}
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + first, posZ, false));
    		if(posY+second < posY + height)
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + second, posZ, false));
    		return;
    	}
    	List<Double> offset = Arrays.asList(0.434999999999998,0.360899999999992,0.290241999999991,0.220997159999987,0.13786084000003104,0.055);
    	double y = mc.thePlayer.posY;
    	for(int i = 0; i < offset.size(); i++){
    		double off = offset.get(i);
    		y += off;
    		if(y > mc.thePlayer.posY + height){
    			double x = mc.thePlayer.posX; double z = mc.thePlayer.posZ;
    			double forward = mc.thePlayer.movementInput.moveForward;
    			double strafe = mc.thePlayer.movementInput.moveStrafe;
   	         	float YAW = mc.thePlayer.rotationYaw;
   	         	double speed = 0.3;
   	         	if(forward != 0 && strafe != 0)
   	         		speed -= 0.09;
   	         	x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) *1;
   	         	z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) *1;
   	         	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
   	         			x, y,z, false));	
   	        
   	         	break;
   	         	
    		}
    		if(i== offset.size() - 1){			
    			double x = mc.thePlayer.posX; double z = mc.thePlayer.posZ;
    			double forward = mc.thePlayer.movementInput.moveForward;
    			double strafe = mc.thePlayer.movementInput.moveStrafe;
   	         	float YAW = mc.thePlayer.rotationYaw;
   	         	double speed = 0.3;
   	         	if(forward != 0 && strafe != 0)
   	         		speed -= 0.09;
   	         	x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) *1;
   	         	z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) *1;
   	         	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
   	         			x, y,z, false));	
    		}else{
    			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
    		}
    	}
    }
}
