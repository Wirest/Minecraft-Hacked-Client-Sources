package VenusClient.online.Module.impl.Movement;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.*;
import VenusClient.online.Event.impl.EventMotionUpdate.Type;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.BlockUtils;
import VenusClient.online.Utils.MathUtils;
import VenusClient.online.Utils.TimeHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import de.Hero.settings.Setting;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import org.lwjgl.input.Keyboard;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OldFly extends Module {
	
	private List<Packet> packets = new CopyOnWriteArrayList<>();
	
	private double randomValue;
	
	private double timerSpeed;
	
    private int stage, ticks;

    private double lastDist;

    private double moveSpeed, y;

    private boolean allowFly;

    public boolean watchdog, reset;

    TimeHelper timeHelper = new TimeHelper();

    TimeHelper blinkHelper = new TimeHelper();
    
    public OldFly() {
        super("Fly", "Fly", Keyboard.KEY_F, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> flyOptions = new ArrayList<>();
        flyOptions.add("Motion");
        flyOptions.add("McCentral");
        flyOptions.add("Cubecraft");
        flyOptions.add("Hypixel");
        flyOptions.add("Hypixel Disabler");
        Client.instance.setmgr.rSetting(new Setting("Fly Mode", this, "Hypixel", flyOptions));
        ArrayList<String> hypixelFlyOptions = new ArrayList<>();
        hypixelFlyOptions.add("Hypixel Normal");
        hypixelFlyOptions.add("Hypixel Fast");
        hypixelFlyOptions.add("Hypixel Fast2");
        hypixelFlyOptions.add("Hypixel Fast3");
        hypixelFlyOptions.add("Hypixel Zoom Test");
        Client.instance.setmgr.rSetting(new Setting("Hypixel Fly Mode", this, "Hypixel Normal", hypixelFlyOptions));
        Client.instance.setmgr.rSetting(new Setting("Fly Speed", this, 1, 0.01, 10, false));
        Client.instance.setmgr.rSetting(new Setting("Fly Blink", this, true));
        Client.instance.setmgr.rSetting(new Setting("Fly Bobing", this, true));
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
        String flyModeSelected = Client.instance.setmgr.getSettingByName("Fly Mode").getValString();
        String HypixelflyModeSelected = Client.instance.setmgr.getSettingByName("Hypixel Fly Mode").getValString();

        double flySpeed = Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble();
        boolean bobing = Client.instance.setmgr.getSettingByName("Fly Bobing").getValBoolean();

        setDisplayName(getName() + " " + ChatFormatting.WHITE + flyModeSelected);

        if (bobing && mc.thePlayer.isMoving()) {
            mc.thePlayer.cameraYaw = 0.089f;
        }
        
        if (flyModeSelected.equalsIgnoreCase("Motion")) {
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.thePlayer.motionY = flySpeed / 2;
            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.thePlayer.motionY = -flySpeed / 2;
            else mc.thePlayer.motionY = 0;
        }

        if (flyModeSelected.equalsIgnoreCase("McCentral")) {
            event.setMotion(0.5);
            mc.thePlayer.motionY = 0.0F;
            mc.timer.timerSpeed = 1;
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.thePlayer.motionY = 0.5 / 2;
            else if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.thePlayer.motionY = -0.5 / 2;
        }
        
        if (flyModeSelected.equalsIgnoreCase("Cubecraft")) {
        	mc.thePlayer.motionY = 0;
			if (mc.thePlayer.isMoving()) {
				if (timeHelper.hasReached(20)) {
					final double yaw = event.getYaw();
					final double x = -Math.sin(yaw) * 1.5;
					final double z = Math.cos(yaw) * 1.5;
					mc.thePlayer.setPosition(x, -.175, z);
				}
				
				if (timeHelper.hasReached(40)) {
					final double yaw = event.getYaw();
					final double x = -Math.sin(yaw) * 1.8;
					final double z = Math.cos(yaw) * 1.8;
					mc.thePlayer.setPosition(x, .195, z);
					timeHelper.reset();
				}
			}
        }

        if (flyModeSelected.equalsIgnoreCase("Hypixel")) {
        	
        	//if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Normal") || HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast")) {
	        	mc.thePlayer.onGround = true;
				double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
				double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
				lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
				if (mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && (mc.thePlayer.posY - (int) mc.thePlayer.posY) == 0) {
					event.setPosY(event.getPosY() + 4.25E-12);
				}
				mc.thePlayer.motionY = 0;
				double value = mc.thePlayer.ticksExisted % 3 == 0 ? -.000325 : .000325 / 2;
				if (BlockUtils.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ)) instanceof BlockAir) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ);
	
			//}
        	
				if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast2")) {
			            if (timeHelper.hasReached(500) && !timeHelper.hasReached(5000)) {
			            	switch(ticks) {
			            	case 0:
			            		mc.timer.timerSpeed = (float) (1.5F * flySpeed);
			            		break;
			            	case 1:
			            		mc.timer.timerSpeed = (float) (2F * flySpeed);
			            		break;
			            	case 2: 
			            		mc.timer.timerSpeed = (float) (2.2F * flySpeed);
			            		break;
			            	case 3: 
			            		mc.timer.timerSpeed = (float) (3F * flySpeed);
			            		break;
			            	case 4: 
			            		mc.timer.timerSpeed = (float) (2.2F * flySpeed);
			            		break;
			            	case 5: 
			            		mc.timer.timerSpeed = (float) (2F * flySpeed);
			            		break;
			            	case 6: 
			            		mc.timer.timerSpeed = (float) (1.5F * flySpeed);
			            		break;
			            	}
			            	ticks++;
			            } else {
			                mc.timer.timerSpeed = 1.0F;
			            }
				}
				if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast3")) {
		            if (timeHelper.hasReached(500) && !timeHelper.hasReached(2000)) {
		            	switch(ticks) {
		            	case 0:
		            		mc.timer.timerSpeed = (float) (1.5F * flySpeed);
		            		break;
		            	case 1:
		            		mc.timer.timerSpeed = (float) (2F * flySpeed);
		            		break;
		            	case 2: 
		            		mc.timer.timerSpeed = (float) (2.2F * flySpeed);
		            		break;
		            	case 3: 
		            		mc.timer.timerSpeed = (float) (3F * flySpeed);
		            		break;
		            	}
		            	ticks++;
		            } else {
		                mc.timer.timerSpeed = 1.0F;
		            }
	            }

	            double x = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
	            double z = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;

	            this.lastDist = Math.sqrt(x * x + z * z);
	        }
				
        
        
            if (flyModeSelected.equalsIgnoreCase("Hypixel Disabler")) {
                if (!watchdog) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        double x = mc.thePlayer.posX;
                        double y = mc.thePlayer.posY;
                        double z = mc.thePlayer.posZ;
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                        mc.thePlayer.sendQueue.addToSendQueueSilent((new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.16, z, true)));
                        mc.thePlayer.sendQueue.addToSendQueueSilent((new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.07, z, true)));
                        watchdog = true;
                        EventChat.addchatmessage("Fly - Wait 5 - 10 s.");
                		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
                    }
                } else {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionZ = 0;
                    mc.thePlayer.jumpMovementFactor = 0;
                    mc.thePlayer.noClip = true;
                    mc.thePlayer.onGround = false;
                }
            if (allowFly){
            	//allowBlink = true;
            	if(timeHelper.hasReached(1000)) {
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY = 0.5 / 2;
                else if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -0.5 / 2;
                else mc.thePlayer.motionY = 0;
                event.setMotion(flySpeed);
            }
        }
            }
        }
	
    }
    @EventTarget
    
    public void onMove(EventMove event) {

    	String flyModeSelected = Client.instance.setmgr.getSettingByName("Fly Mode").getValString();
        String HypixelflyModeSelected = Client.instance.setmgr.getSettingByName("Hypixel Fly Mode").getValString();

        double flySpeed = Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble();

        if (flyModeSelected.equalsIgnoreCase("Motion")) {
            event.setMotion(flySpeed);
        }

        if ((flyModeSelected.equalsIgnoreCase("Hypixel"))) {
        	
        	if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Normal")) {
        		
	        }
        	
        	if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast")) {
               // if (allowFly) {
                   


                    if (mc.thePlayer.isMoving()) {
                        switch (stage) {
                        case 0:
                        		damageHypixel();
                                mc.timer.timerSpeed = 0.5f;
                                
                            case 1:
                                
                                mc.timer.timerSpeed = 0.7f;


                                moveSpeed = 0.635;

                                break;
                            case 2:
                                mc.timer.timerSpeed = 2.5f;
                                moveSpeed = Math.min(1.5 / 1.635, 1.635);
                                break;

                            default:

                                if(stage > 10){
                                    mc.timer.timerSpeed = 1.0f;
                                }
                                moveSpeed = lastDist - lastDist / 159.9999;

                                break;


                        }
                        moveSpeed = Math.max(moveSpeed, event.getBaseMoveSpeed());
                        ++stage;
                        if (stage > 0)
                        	event.setMotion(moveSpeed);


                    } else {
                    	event.setMotion( 0);
                        this.moveSpeed = 0;
                    }


                    if (mc.thePlayer.isCollidedHorizontally) {
                        moveSpeed = 0;
                    }
                    event.setMotion(Math.max(moveSpeed, event.getBaseMoveSpeed()));
                    
                } 
        	//}
        	if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast2") || HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast3") ) {
                final double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.71D : 1.83D;
                //if(allowFly) {
	        	switch (stage) {
	        	case 0:
                	//event.setY(mc.thePlayer.motionY = event.getJumpBoostModifier(0.39999994D));
	            	damageHypixel();
	            	break;
	            case 1:
	                this.moveSpeed = boost * event.getBaseMoveSpeed();
	                break;
	            case 2:
	                this.moveSpeed *= 2.0D;
	                break;
	            default:
	                this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
	                break;
	        }
	
	        this.moveSpeed = Math.max(this.moveSpeed, event.getBaseMoveSpeed());
	
	        event.setMotion(this.moveSpeed);
	        stage++;
	        	}
        }
        //double flySpeed = Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble();

        if(HypixelflyModeSelected.equalsIgnoreCase("Hypixel Zoom Test") ) {
            double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2 : 2 * 1.15;
            if(mc.thePlayer.isMoving()) {
            switch (stage) {
                case 1:
                	damageHypixel();
                    moveSpeed = boost * event.getBaseMoveSpeed();
                    break;
                case 2:
                    moveSpeed *= boost;
                    break;
                case 3:
                    moveSpeed = lastDist - (lastDist / 156) * (lastDist - event.getBaseMoveSpeed());
                    break;
                default:
                	mc.timer.timerSpeed = 1.0F;
                    moveSpeed = lastDist - (lastDist / 600);
                    break;
            }
            stage++;
            event.setMotion(moveSpeed = Math.max(event.getBaseMoveSpeed(), moveSpeed));
        }
        }
    }
	      //}
	    
	  //  }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        boolean blink = Client.instance.setmgr.getSettingByName("Fly Blink").getValBoolean();
        String flyModeSelected = Client.instance.setmgr.getSettingByName("Fly Mode").getValString();
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (flyModeSelected.equalsIgnoreCase("Hypixel Disabler") && watchdog) {
                event.setCancelled(true);
            }
        }
        
        
        if (Client.instance.setmgr.getSettingByName("Fly Blink").getValBoolean()){
        	
			Packet p = event.getPacket();
			if (event.getPacket() instanceof C03PacketPlayer) {
				event.setCancelled(true);
                
                if(!(p instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(p instanceof C03PacketPlayer.C06PacketPlayerPosLook))
	                return;
	
	            packets.add(p);
            }else if ((event.getPacket() instanceof C02PacketUseEntity)) {
            	event.setCancelled(true);
            	packets.add(p);
            }else if ((event.getPacket() instanceof C07PacketPlayerDigging)) {
            	C07PacketPlayerDigging in = (C07PacketPlayerDigging) event.getPacket();
            	event.setCancelled(true);
            	
            	packets.add(p);
            }else if ((event.getPacket() instanceof C08PacketPlayerBlockPlacement)) {
            	event.setCancelled(true);
            	packets.add(p);
            }else if ((event.getPacket() instanceof C0APacketAnimation)) {
            	event.setCancelled(true);
            	packets.add(p);
            }
            
        }
        if (flyModeSelected.equalsIgnoreCase("Hypixel") && !Client.instance.setmgr.getSettingByName("Fly Blink").getValBoolean()){
        	if ((mc.thePlayer.ticksExisted % 20 == 0)) { 
			Packet p = event.getPacket();
			if (event.getPacket() instanceof C03PacketPlayer) {
				event.setCancelled(true);
                
                if(!(p instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(p instanceof C03PacketPlayer.C06PacketPlayerPosLook))
	                return;
	
	            packets.add(p);
            }else if ((event.getPacket() instanceof C08PacketPlayerBlockPlacement)) {
            	event.setCancelled(true);
            	//packets.add(p);
            }else if ((event.getPacket() instanceof C0APacketAnimation)) {
            	event.setCancelled(true);
            	//packets.add(p);
            }
            
        }else {
        	
        }
		}
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        String flyModeSelected = Client.instance.setmgr.getSettingByName("Fly Mode").getValString();
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) event.getPacket();
            pac.yaw = mc.thePlayer.rotationYaw;
            pac.pitch = mc.thePlayer.rotationPitch;
            if (watchdog && flyModeSelected.equalsIgnoreCase("Hypixel Disabler")) {
                EventChat.addchatmessage("Fly - Exploit Compleate");
                allowFly = true;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Client.instance.setmgr.getSettingByName("Fly Blink").getValBoolean()){
        for (Packet packet : packets) {
    		mc.thePlayer.sendQueue.addToSendQueueSilent(packet);
        }
        packets.clear();
        }
        if (allowFly){
            EventChat.addchatmessage("Fly - Please Wait 20 Before Fly Again");
        }
        mc.timer.timerSpeed = 1f;

        mc.thePlayer.stepHeight = 0.625F;
        mc.thePlayer.motionX = 0.0D;
        mc.thePlayer.motionZ = 0.0D;
        mc.thePlayer.motionY = 0.0D;
    	
    }

    @Override
    public void onEnable() {
        super.onEnable();
        String flyModeSelected = Client.instance.setmgr.getSettingByName("Fly Mode").getValString();
        String HypixelflyModeSelected = Client.instance.setmgr.getSettingByName("Hypixel Fly Mode").getValString();
        if (flyModeSelected.equalsIgnoreCase("Hypixel Disabler")) {
            EventChat.addchatmessage("Fly - Please press jump key");
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.16, z, true));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.07, z, true));
                watchdog = true;
                EventChat.addchatmessage("Fly - Wait 5 - 10 s.");

            } else {
                watchdog = false;
            }

        }
        if (flyModeSelected.equalsIgnoreCase("Cubecraft")) {
    		mc.timer.timerSpeed = .4F;
        }
       
        if (flyModeSelected.equalsIgnoreCase("Hypixel") && HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast") && HypixelflyModeSelected.equalsIgnoreCase("Hypixel Fast2")) {
        	if(mc.thePlayer.onGround) {
        		allowFly = true;
        	}else {
        		toggle();
        		EventChat.addchatmessage("You need to be onground Before Fly (Damage Stage)");
        		return;
        	}
        }else {
    		allowFly = false;
        }
        if (flyModeSelected.equalsIgnoreCase("Hypixel") && HypixelflyModeSelected.equalsIgnoreCase("Hypixel Zoom Test")) {
        	//mc.thePlayer.jump();
        }
        timeHelper.reset();
        blinkHelper.reset();
        this.y = 0.0D;
        this.lastDist = 0.0D;
        this.moveSpeed = 0.0D;
        this.stage = 0;
        this.ticks = 0;

        mc.thePlayer.stepHeight = 0.0F;
        mc.thePlayer.motionX = 0.0D;
        mc.thePlayer.motionZ = 0.0D;
    }
    public boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public static void damageHypixel() {
    	final double offset = 0.060100000351667404;
        final NetHandlerPlayClient netHandler = mc.getNetHandler();
        final EntityPlayerSP player = mc.thePlayer;
        final double x = player.posX;
        final double y = player.posY;
        final double z = player.posZ;
        for (int i = 0; i < getMaxFallDist() / 0.05510000046342611 + 1.0; ++i) {
            netHandler.addToSendQueueSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404, z, false));
            netHandler.addToSendQueueSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4, z, false));
            netHandler.addToSendQueueSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291 + 6.01000003516674E-8, z, false));
        }
        netHandler.addToSendQueueSilent((Packet)new C03PacketPlayer(true));
	}
    
    public static float getMaxFallDist() {
        final PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
        final int f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0;
        return (float)(mc.thePlayer.getMaxFallHeight() + f);
    }
    public static float getDirection() {

		float yaw = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (mc.thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (mc.thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (mc.thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		yaw *= 0.017453292f;
		return yaw;
	}

}
