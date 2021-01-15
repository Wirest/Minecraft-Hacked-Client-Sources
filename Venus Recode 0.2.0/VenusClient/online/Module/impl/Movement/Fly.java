package VenusClient.online.Module.impl.Movement;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Fly extends Module{

    public static double moveSpeed;
	public static int stage;
	public static int booststage;
	public static double lastDistance;
    
    public TimeHelper timer = new TimeHelper();
    
	public Fly() {
		super("Fly", "Fly", Category.MOVEMENT, Keyboard.KEY_F);
		}
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		final Block blockUnderPlayer = getBlockUnderPlayer(this.mc.thePlayer, 0.2);

	    double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
	    double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
	    this.lastDistance = Math.sqrt(xDif * xDif + zDif * zDif);
        if (!this.isOnGround(1.0E-7) && !blockUnderPlayer.isFullBlock() && !(blockUnderPlayer instanceof BlockGlass)) {
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.cameraYaw = 0.06F;
           // if (this.bob.getValueState()) {
            //}
            float n = 0.29f + this.getSpeedEffect() * 0.06f;
            this.mc.thePlayer.jumpMovementFactor = 0.0f;
            switch (++this.stage) {
                case 1: {
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ);
                    break;
                }
                case 2: {
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-12, this.mc.thePlayer.posZ);
                    break;
                }
                case 3: {
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ);
                    this.stage = 0;
                    break;
                }
            }
            
        }
    }
	
	@EventTarget
	public void onMove(EventMove event) {
        if (mc.thePlayer.isMoving()) {
            switch (this.booststage) {
              case 0:
                if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
            		int i = 0;
                    while (i <= 48) {
                    	mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0514865, this.mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0618865, this.mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ, false));
                        ++i;
                    }
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            		
                  this.moveSpeed = 0.5D * 0.8F;
                } 
                break;
              case 1:
                if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)
                  event.y = mc.thePlayer.motionY = getJumpBoostModifier(0.39999994D); 
                this.moveSpeed *= 2.149D;
                break;
              case 2:
                this.moveSpeed = 1.3D * 0.8F;
                break;
              default:
                this.moveSpeed = this.lastDistance - this.lastDistance / 159.0D;
                break;
            } 
            setSpeed(event, Math.max(this.moveSpeed, getBaseMoveSpeed()));
            this.booststage++;
          }
	}
	
	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += ((amplifier + 1) * 0.1F);
		} 
		return baseJumpHeight;
	}
	
	public static double getBaseMoveSpeed() {
		 double baseSpeed = 0.2875D;
		 if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
			 baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1); 
		 return baseSpeed;
	 }
	  
	  
	public static void setSpeed(EventMove moveEvent, double moveSpeed) {
		setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
	}  

	public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
		  double forward = pseudoForward;
		  double strafe = pseudoStrafe;
		  float yaw = pseudoYaw;
		  if (forward != 0.0D) {
			  if (strafe > 0.0D) {
				  yaw += ((forward > 0.0D) ? -45 : 45);
			  } else if (strafe < 0.0D) {
				  yaw += ((forward > 0.0D) ? 45 : -45);
			  }
			  strafe = 0.0D;
			  if (forward > 0.0D) {
				  forward = 1.0D;
			  } else if (forward < 0.0D) {
				  forward = -1.0D;
		      }
		} 
	    if (strafe > 0.0D) {
	      strafe = 1.0D;
	    } else if (strafe < 0.0D) {
	      strafe = -1.0D;
	    } 
	    double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
	    double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
	    moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
	    moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
	}
	
	@Override
	protected void onEnable() {
	    this.lastDistance = 0.0D;
	    this.moveSpeed = 0.0D;
	    this.booststage = 0;
	    this.mc.thePlayer.motionX = 0.0D;
	    this.mc.thePlayer.motionZ = 0.0D;
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
        mc.timer.timerSpeed = 1;
        timer.reset();
		super.onDisable();
	}
    public static Block getBlockUnderPlayer(final EntityPlayerSP entityPlayerMP, final double n) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(entityPlayerMP.posX, entityPlayerMP.posY - n, entityPlayerMP.posZ)).getBlock();
    }
    
    public boolean isOnGround(final double n) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty();
    }
    
    public static double defaultSpeed() {
        double n = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }
    public int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }
    
}
