package splash.client.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.util.IChatComponent;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.math.MathUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.player.PlayerUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 17:41, 30-May-20
 * Project: Client
 */
public class Flight extends Module {

    private Stopwatch timer = new Stopwatch();

    private double x, y, z, mineplexSpeed, lastDist, speed,randomValue;
    private boolean back,down,done, damageFly, allowed;
    private int counter, ticks;
    public boolean reset;
	public float timerSpeed;
	public long lastDisable;
    public NumberValue<Double> prop_speed = new NumberValue<>("Speed", 2.5D, 0.1D, 5D, this);
    public BooleanValue<Boolean> antikick = new BooleanValue<>("Anti Kick", true, this);
    public ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.VANILLA, this);

    public Flight() {
        super("Flight", "Lets you fly.", ModuleCategory.MOVEMENT);
        setModuleMacro(Keyboard.KEY_R);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (onServer("hypixel") && antikick.getValue()) {
        	ClientLogger.printToMinecraft("AntiKick may result in watchdog bans, please do not use it");
        }
        if (mc.thePlayer.isMoving() && System.currentTimeMillis() - Splash.INSTANCE.lastFlag >= 5000 && !mc.gameSettings.keyBindSprint.isKeyDown() && System.currentTimeMillis() - lastDisable > 1500L) {
        	damageFly = true;
        	allowed = !allowed;
        }else {
        	damageFly = false;
        }
        
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        mineplexSpeed = 0;
        mc.thePlayer.stepHeight = 0;
        speed = 0;
        timer.reset();
        done = false;
        back = false;
        down = false;
        counter = 0;
        ticks = 0;
        if (mode.getValue().equals(Mode.WATCHDOG)) {
        	randomValue = 0.001111111111111F;
        }
        if(mode.getValue().equals(Mode.GWEN)) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @Override
    public void onDisable() {
        if (mode.getValue().equals(Mode.SENTINEL)) {
        	mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5F, mc.thePlayer.posZ);
            mc.gameSettings.keyBindForward.pressed = false;
        }
        lastDisable = System.currentTimeMillis();
		mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.stepHeight = .6f;
        mc.thePlayer.setStill();
        mc.timer.timerSpeed = 1f;
        if (mode.getValue().equals(Mode.WATCHDOG) && reset) {
        	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - randomValue, mc.thePlayer.posZ);
        }    
        super.onDisable();
    }

    public int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        ClientLogger.printToMinecraft("Clear a hotbar slot.");
        return -10;
    }
    
    @Collect
    public void onUpdate(EventPlayerUpdate e) {
    	if (e.getStage().equals(Stage.PRE) && mode.getValue().equals(Mode.WATCHDOG)) {
    		mc.thePlayer.onGround = true;
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        	if (counter > 1 || !damageFly) {
            	mc.thePlayer.motionY = 0;
	        	if (mc.thePlayer.ticksExisted % 2 == 0) {
	        		reset = true;
	        		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + randomValue, mc.thePlayer.posZ);
	        	} else {
	        		reset = false;
	        	}
	        	if (!mc.thePlayer.isMoving()) {
	        		double speed = 0.1;

	        			mc.thePlayer.motionX = (-Math.sin(mc.thePlayer.getDirection())) * speed;
	        			mc.thePlayer.motionZ = Math.cos(mc.thePlayer.getDirection()) * speed;
	        			
	        	}
	        	

	        	if (mc.thePlayer.ticksExisted % 5 == 0) {
	        		randomValue += MathUtils.secRanDouble(-0.000009D, 0.000009D);
	        	}
        	}
			
    	}
        if (mode.getValue() == Mode.SENTINEL && e.getStage().equals(Stage.PRE)) {
        	
        }
    }

    @Collect
    public void onPlayerMove(EventMove eventMove) {
        if (antikick.getValue() && mc.thePlayer.ticksExisted % 78 == 0 && !onServer("hypixel")) { //Vanilla will kick you after 80 ticks, so use 78 to be safe as if a player is lagging 79 might still kick
            MovementUtils.fallPacket();
            MovementUtils.ascendPacket();
        }
        if (mode.getValue().equals(Mode.SENTINEL)) {
        	mc.timer.timerSpeed = 0.3F;
        	eventMove.setY(mc.thePlayer.motionY = 1e-1);
            if(mc.thePlayer.ticksExisted % 3 != 0) {
            	eventMove.setMovementSpeed(0.0);
            } else { 
            	eventMove.setMovementSpeed(2.0); 
            	eventMove.setY(mc.thePlayer.motionY = -1.99e-1);
            }
        }
        if (mode.getValue() == Mode.GWEN) {
            if (airSlot() == -10){
                MovementUtils.setMoveSpeed(eventMove, 0);
                return;
            }

            if (!done) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(airSlot()));
                BlockUtils.placeHeldItemUnderPlayer();
                MovementUtils.setMoveSpeed(eventMove, back ? -mineplexSpeed : mineplexSpeed);
                back = !back;
                if (mc.thePlayer.isMovingOnGround() && mc.thePlayer.ticksExisted % 2 == 0) {
                    mineplexSpeed += RandomUtils.nextDouble(0.125D, 0.12505D);
                }
                if (mineplexSpeed >= prop_speed.getValue() * 1.3 && mc.thePlayer.isCollidedVertically && mc.thePlayer.isMovingOnGround()) {
                    eventMove.setY(mc.thePlayer.motionY = 0.42F);
                    MovementUtils.setMoveSpeed(eventMove, 0);
                    done = true;
                    return;
                }
            } else {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                if (mc.thePlayer.fallDistance == 0) {
                    eventMove.setY(mc.thePlayer.motionY += 0.039);
                } else if (mc.thePlayer.fallDistance <= 1.4) {
                    eventMove.setY(mc.thePlayer.motionY += 0.032);
                }
                MovementUtils.setMoveSpeed(eventMove, mineplexSpeed *= 0.979);
                if (mc.thePlayer.isMoving() && mc.thePlayer.isCollidedVertically) {
                    done = false;
                }
            }
        }
        if (mode.getValue() == Mode.VANILLA) {
        	
            if (!mc.thePlayer.isMoving()) mc.thePlayer.setStill();
	
            if (mc.gameSettings.keyBindJump.pressed) mc.thePlayer.motionY = prop_speed.getValue() / 1;
	
            if (mc.gameSettings.keyBindSneak.pressed) mc.thePlayer.motionY = -prop_speed.getValue() / 1;
	
            mc.thePlayer.setSpeed(prop_speed.getValue());
 
        }
        if (mode.getValue() == Mode.PVPTEMPLE) {
        	
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.ticksExisted % 4 == 0) {
	            if (!mc.thePlayer.isMoving()) mc.thePlayer.setStill();
	
	            if (mc.gameSettings.keyBindJump.pressed) mc.thePlayer.motionY = prop_speed.getValue() / 1;
	
	            if (mc.gameSettings.keyBindSneak.pressed) mc.thePlayer.motionY = -prop_speed.getValue() / 1;
	
	            mc.thePlayer.setSpeed(prop_speed.getValue());
            } else {
	            mc.thePlayer.setSpeed(.15);
            }
        }

        if (mode.getValue() == Flight.Mode.WATCHDOG && damageFly){
        	
        	mc.thePlayer.onGround = true;
        	if (mc.thePlayer.ticksExisted % 10 == 0 && mc.thePlayer.isMoving()) {
        		mc.thePlayer.cameraYaw = 0.16f;
        	}
        	if (damageFly) {
	        	switch (counter) {
		        	case 0:
		        		if (timer.delay(allowed ? 250 : 150)) {
			        		PlayerUtils.damageHypixel();
			        		speed = eventMove.getMovementSpeed() * (allowed ? 1.25 : 1.25); 
			        		timer.reset();
			        		
			        		
			        		counter = 1;
		        		} else {
		        			speed = 0;
		        			eventMove.setX(mc.thePlayer.motionX = 0);
		        			eventMove.setY(mc.thePlayer.motionY = 0);
		        			eventMove.setZ(mc.thePlayer.motionZ = 0);
		        		}
		        	break;
		        	case 1: 
		        		speed *= 2.14999;
		        		eventMove.setY(mc.thePlayer.motionY = 0.41999998688697815D);
		        		counter = 2;
		        	break;
		            case 2: 
		        		speed = (allowed ? 1.37 : 1.42); 
		        		counter = 3;
		        	break;
		            default:
		        		if (counter > 10) {
		        			if (timerSpeed > 1.0) { 
		        				mc.timer.timerSpeed = timerSpeed -= 0.055;
		        			} else {
			        			mc.timer.timerSpeed = 1.0f;
		        			}
		        		} else if (counter == 9) {
		        			// timerSpeed = 2.6f;
		        			timerSpeed = 1.4f;
		        		}

		        		if (mc.thePlayer.isCollidedHorizontally) {
		        			mc.timer.timerSpeed = 1.0f;
		        			speed *= .5;
		        		}
		        		speed -= speed / 159; 
		        		counter++;
		        	break;
	        	}
				eventMove.setMoveSpeed(speed == 0 ? 0 : Math.max(speed, eventMove.getMovementSpeed()));
        	}
        }
    }
    
    
    public enum Mode {
        VANILLA, PVPTEMPLE, GWEN, WATCHDOG, SENTINEL
    }
}



