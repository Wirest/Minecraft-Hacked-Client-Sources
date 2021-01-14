package cheatware.module.movement;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventPreMotionUpdate;
import cheatware.event.events.EventReceivePacket;
import cheatware.event.events.EventSendPacket;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.utils.BlockUtil;
import cheatware.utils.MoveUtils;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class Fly extends Module {
	boolean done, back;
	double mineplexSpeed;
	public Setting speed;
	public int lastDist;
	
    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Watchdog");
        options.add("Vanilla");
        options.add("Mineplex");
        options.add("Viper");
        Cheatware.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Mineplex", options));
        Cheatware.instance.settingsManager.rSetting(speed = new Setting("Flight Speed", this, 1, 0, 10, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        this.setDisplayName("Fly §7" + mode);

        if(mode.equalsIgnoreCase("Watchdog")) {
        	if(mc.thePlayer.hurtTime >= mc.thePlayer.maxHurtTime) {
        		this.done = true;
        	}
        	
        	if(done) {
        		mc.thePlayer.motionY = 0;
        		if(mc.thePlayer.onGround) {
        			mc.thePlayer.motionY = 0.45F;
        		}
        		MoveUtils.setMotion(3);
        	} else {
        		MoveUtils.setMotion(0.0);
        	}

        	if(mc.thePlayer.isCollidedHorizontally) {
        		MoveUtils.setMotion(0);
            }
        	
            mc.thePlayer.capabilities.isCreativeMode = true;
        	
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = (int) Math.sqrt(xDist * xDist + zDist * zDist);
            
        } else if(mode.equalsIgnoreCase("Viper")) {
        	if(mc.thePlayer.isMoving()) {
        		mc.thePlayer.setSpeed(4F);
        		if(mc.gameSettings.keyBindJump.isKeyDown()) {
        			mc.thePlayer.motionY = 0.5F;
        		} else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
        			mc.thePlayer.motionY = -0.5F;
        		}
        		
        		if(!mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
        			mc.thePlayer.motionY = 0;
        		}
        		
        	} else {
            	mc.thePlayer.setVelocity(0, 0, 0);
        	}
        } else  if(mode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.capabilities.allowFlying = true;
            mc.thePlayer.capabilities.setFlySpeed(0.05F);
        }
        
      
    }
 
	@EventTarget
    public void onMove(EventPreMotionUpdate event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        if(mode.equalsIgnoreCase("Mineplex")) {
        	if (airSlot() == -10) {
                this.toggle();
                return;
            }
            if (!this.done) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(airSlot()));
                
                BlockUtil.placeHeldItemUnderPlayer();
                MoveUtils.setMotion(this.back ? (-this.mineplexSpeed) : this.mineplexSpeed);
                
                this.back = !this.back;
                
                if (this.mc.thePlayer.isMoving() && mc.thePlayer.onGround && this.mc.thePlayer.ticksExisted % 2 == 0) {
                    this.mineplexSpeed += RandomUtils.nextDouble(0.125, 0.12505);
                }
                
                if (this.mineplexSpeed >= this.speed.getValDouble() * 1.3 && this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
                    event.setY(this.mc.thePlayer.motionY = 0.41999998688697815);
                    MoveUtils.setMotion(0.0);
                    
                    this.done = true;
                    
                }
            }
            else {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                
                if (this.mc.thePlayer.fallDistance == 0.0f) {
                    final EntityPlayerSP thePlayer = this.mc.thePlayer;
                    event.setY(thePlayer.motionY += 0.039);
                }
                else 
                	if (this.mc.thePlayer.fallDistance <= 1.4) {
                    final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                    event.setY(thePlayer2.motionY += 0.032);
                }
                
                MoveUtils.setMotion(this.mineplexSpeed *= 0.979);
                
                if (MoveUtils.isMoving() && this.mc.thePlayer.isCollidedVertically) {
                    this.done = false;
                }
            }
        }

        if(mode.equalsIgnoreCase("Watchdog")) {
        	if(mc.thePlayer.ticksExisted % 2 == 0) {
        		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextDouble(0.005, 0.008), mc.thePlayer.posZ);
        	} else {
        		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - RandomUtils.nextDouble(0.005, 0.008), mc.thePlayer.posZ);
        	}
        }
    }
    
    @EventTarget
    public void onSend(EventSendPacket event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("Fly Mode").getValString();
    }
    
    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        if(mode.equalsIgnoreCase("Watchdog")) {
	    	if(event.getPacket() instanceof S32PacketConfirmTransaction) {
	    		S32PacketConfirmTransaction packet = (S32PacketConfirmTransaction) event.getPacket();
	             if(packet.getActionNumber() < 0){
	                  event.setCancelled(true);
	             }
    		 }
    	}
    }
    
    public static int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        return -10;
    }

    @Override
    public void onEnable() {
    	super.onEnable();
        String mode = Cheatware.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        if(mode.equalsIgnoreCase("Watchdog")) {
        	//if (mc.thePlayer.onGround) {
               /* final double offset = 0.4122222218322211111111F;
                final NetHandlerPlayClient netHandler = mc.getNetHandler();
                final EntityPlayerSP player = mc.thePlayer;
                final double x = player.posX;
                final double y = player.posY;
                final double z = player.posZ;
                
                for (int i = 0; i < 9; i++) {
                    netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
                    netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.000002737272, z, false));
                    netHandler.addToSendQueue(new C03PacketPlayer(false));
                }
                netHandler.addToSendQueue(new C03PacketPlayer(true));
            }*/
        }
    	done = false; 
    	back = false;
    	mineplexSpeed = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.capabilities.isCreativeMode = false;
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.thePlayer.capabilities.isFlying = false;
    }
}