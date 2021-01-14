package info.sigmaclient.module.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;

public class LongJump extends Module {
	/**
	 * class made by LeakedPvP
	 */
    public static String OFF = "TOGGLE";
    public static String MODE = "MODE";
    public static String BOOST = "BOOST";
    public static String GLIDE = "GLIDE";
    private float air, groundTicks;
    private int stage;
    

    public LongJump(ModuleData data) {
        super(data);
      
        settings.put(MODE, new Setting<>(MODE, new Options("LongJump Mode", "NCP", new String[]{"NCP", "Hypixel", "Mineplex", "Cubecraft", "Custom"}), "LongJump method."));
        settings.put(BOOST, new Setting<>(BOOST, 1, "Speed boost for custom mode.", 0.05, 0.1, 5));
        settings.put(OFF, new Setting<>(OFF, true, "Auto disable on landing."));
        settings.put(GLIDE, new Setting<>(GLIDE, false, "New NCP Bypassing \"glide\"."));
    }

   
    @Override
    public void onEnable() {
        air = 0;
        stage = 0;
        groundTicks = 0;
        if (premiumAddon != null) {
            premiumAddon.onEnable();
        }
   
    }
	public static void vclip1(){
		double X = mc.thePlayer.posX;
		double Y = mc.thePlayer.posY;
		double Z = mc.thePlayer.posZ;
		double y = 0.42;
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
		y+= 0.333;
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
		y+= 0.247;
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
		y+= 0.164;
		mc.thePlayer.setPosition(X, Y +y, Z);
		mc.thePlayer.onGround = true;
	}
    @Override
    public void onDisable() {
    	String mode = ((Options) settings.get(MODE).getValue()).getSelected();
    	switch(mode){
    	case"Mineplex":
    		mc.thePlayer.motionX *= 0.5;
    		mc.thePlayer.motionZ *= 0.5;
    		mc.thePlayer.jumpMovementFactor = 0.3f;
    		break;
    		
    	case"Cubecraft":
    		mc.thePlayer.motionX *= 0;
    		mc.thePlayer.motionZ *= 0;
    		mc.thePlayer.jumpMovementFactor = 0f;
    		break;
    	default:
    		mc.thePlayer.motionX *= 0;
    		mc.thePlayer.motionZ *= 0;
    		mc.thePlayer.jumpMovementFactor = 0f;
    			break;
    	}
        if (premiumAddon != null) {
            premiumAddon.onDisable();
        }
        mc.timer.timerSpeed = 1f;

    }


    @Override
    @RegisterEvent(events = {EventUpdate.class, EventJump.class})
    public void onEvent(Event event) {
        String currentLongJump = ((Options) settings.get(MODE).getValue()).getSelected();
        this.setSuffix(currentLongJump);
        boolean glide =(Boolean) settings.get(GLIDE).getValue();
    	if(event instanceof EventJump){
    		EventJump ej = (EventJump)event;
    		if(ej.isPre()){
    			ej.setMotionY(glide? 0.425 : 0.425); 			
    		}
    	}
    	if(event instanceof EventUpdate){
    	EventUpdate em = (EventUpdate)event;

        if(em.isPre()){
        if (currentLongJump.equalsIgnoreCase("Hypixel")) {

            float x2 = 0.7f + MoveUtils.getSpeedEffect() * 0.45f;
            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
            	if((Boolean) settings.get(OFF).getValue() && groundTicks > 0){
            		groundTicks = 0;
            		this.toggle();
            		return;
            	}
            	groundTicks++;
              	MoveUtils.setMotion(0.15);
                mc.thePlayer.jump();
                stage = 1;
            }
            if (MoveUtils.isOnGround(0.001)) {
                air = 0;
            } else {
            	if(mc.thePlayer.isCollidedVertically)
            		stage = 0;
               	if(stage > 0 && stage <= 3 && glide){
            		mc.thePlayer.onGround = true;
            	}
                double speed = (0.95 + MoveUtils.getSpeedEffect() * 0.2) - air / 25;
                if(glide){
                	speed = (1.1 + MoveUtils.getSpeedEffect() * 0.2f) - air / 20;
                }
                if(speed < MoveUtils.defaultSpeed()-0.05){ // + (0.025*MoveUtils.getSpeedEffect())
                	speed = MoveUtils.defaultSpeed()-0.05;
                }
                if(stage < 4 && glide)
                	speed = MoveUtils.defaultSpeed();
                MoveUtils.setMotion(speed);
                if(glide){
                	mc.thePlayer.motionY = getMotion(stage);
                }else{
                	mc.thePlayer.motionY = getOldMotion(stage);
                }
                if(stage > 0){
                	stage ++;
                }
                air += x2;
            }
        }
        if (currentLongJump.equalsIgnoreCase("Mineplex")) {
            if (premiumAddon != null) {
                premiumAddon.onEvent(event);;
            } else {
                Notifications.getManager().post("Premium Bypass", "Mineplex longjump mode is a premium only bypass", Notifications.Type.WARNING);
                toggle();
            }
        }
        if (currentLongJump.equalsIgnoreCase("NCP")) {
        	mc.thePlayer.lastReportedPosY = 0;
            float x2 = 1f + MoveUtils.getSpeedEffect() * 0.45f;
            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
              	if((Boolean) settings.get(OFF).getValue() && groundTicks > 0){
            		groundTicks = 0;
            		this.toggle();
            		return;
            	}
              	stage = 1;
            	groundTicks++;

                mc.thePlayer.jump();

            }
            if (MoveUtils.isOnGround(0.01)) {
                air = 0;
            } else {
            	if(mc.thePlayer.isCollidedVertically)
            		stage = 0;
            	if(stage > 0 && stage <= 3 && glide){
            		//if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,mc.thePlayer.boundingBox.expand(-0.3, -2, -0.3).expand(0.3, 0, 0.3)).isEmpty()){
            			mc.thePlayer.onGround = true;
            	//	}
            	
            		//mc.thePlayer.isCollidedVertically = false;
            	}
                double speed = (0.75f + MoveUtils.getSpeedEffect() * 0.2f) - air / 25;
                if(speed < MoveUtils.defaultSpeed()){ // + (0.025*MoveUtils.getSpeedEffect())
                	speed = MoveUtils.defaultSpeed();
                }
                if(glide){
                	speed = (0.8f + MoveUtils.getSpeedEffect() * 0.2f) - air / 25;
                    if(speed < MoveUtils.defaultSpeed()){ // + (0.025*MoveUtils.getSpeedEffect())
                    	speed = MoveUtils.defaultSpeed();
                    }
                }
                mc.thePlayer.jumpMovementFactor = 0;
                if(stage < 4 && glide)
                	speed = MoveUtils.defaultSpeed();
                MoveUtils.setMotion(speed);
                if(glide){
                	mc.thePlayer.motionY = getMotion(stage);
                }else{
                	mc.thePlayer.motionY = getOldMotion(stage);
                }
                if(stage > 0){
                	stage ++;
                }
                air += x2;
            }
        }
        if (currentLongJump.equalsIgnoreCase("Custom")) {

            float x2 = 1f + MoveUtils.getSpeedEffect() * 0.45f;
            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
            	Module longjump = Client.getModuleManager().get(LongJump.class);
              	if((Boolean) longjump.getSetting(LongJump.OFF).getValue() && groundTicks > 0){
            		groundTicks = 0;
            		this.toggle();
            		return;
            	}
            	groundTicks++;
                mc.thePlayer.motionX *= 1;
                mc.thePlayer.motionZ *= 1;
                mc.thePlayer.jump();
            }
            if (mc.thePlayer.onGround && BlockUtils.isOnGround(0.01)) {
                air = 0;
            } else {
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                float speed = (((Number) settings.get(BOOST).getValue()).floatValue() + MoveUtils.getSpeedEffect() * 0.2f) - air / 25;
                mc.thePlayer.jumpMovementFactor = speed > 0.28f ? speed : 0.28f;
                air += x2 *((Number) settings.get(BOOST).getValue()).floatValue() * 2;
            }
        }
        if(currentLongJump.equalsIgnoreCase("Cubecraft")){
            if (premiumAddon != null) {
                premiumAddon.onEvent(event);;
            } else {
                Notifications.getManager().post("Premium Bypass", "Cubecraft longjump mode is a premium only bypass", Notifications.Type.WARNING);
                toggle();
            }
        }
        }
    	}
    	
    }
    double getMotion(int stage){
    	boolean isMoving = (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0);
     	double[] mot = {0.396,-0.122,-0.1,0.423, 0.35,0.28,0.217,0.15, 0.025,-0.00625,-0.038,-0.0693,-0.102,-0.13,
    			-0.018,-0.1,-0.117,-0.14532,-0.1334, -0.1581, -0.183141, -0.170695, -0.195653, -0.221, -0.209, -0.233, -0.25767,
    			-0.314917, -0.371019, -0.426};
    	stage --;
    	if(stage >= 0 && stage < mot.length){
    		double motion = mot[stage];
    		return motion;
    	}else{
    		return mc.thePlayer.motionY;
    	}
    }
    double getOldMotion(int stage){
    	boolean isMoving = (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0);
    	double[] mot = {0.345,0.2699,0.183,0.103,0.024,-0.008,-0.04,-0.072,-0.104,-0.13,-0.019,-0.097};
    	double[] notMoving = {0.345,0.2699,0.183,0.103,0.024,-0.008,-0.04,-0.072,-0.14,-0.17,-0.019,-0.13};
    	stage --;
    	if(stage >= 0 && stage < mot.length){
    		if((mc.thePlayer.moveStrafing == 0 && mc.thePlayer.moveForward == 0) || mc.thePlayer.isCollidedHorizontally){
    			return notMoving[stage];
    		}
    		return mot[stage];
    	}else{
    		return mc.thePlayer.motionY;
    	}
    }
    public void setMineplexAddon(Module mineplexAddon) {
        this.premiumAddon = mineplexAddon;
    }

}
