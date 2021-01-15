package VenusClient.online.Module.impl.Movement;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Speed extends Module {

	public Speed() {
		super("Speed", "Speed", Keyboard.KEY_M, Category.MOVEMENT);
	}
	
	private TargetStrafe targetStrafe;

	public void setup() {
		ArrayList<String> options = new ArrayList<>();
		options.add("All AC Bhop");
		options.add("McCentral");
		options.add("Hypixel");
		Client.instance.setmgr.rSetting(new Setting("Speed Mode", this, "Hypixel", options));
	}
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
			EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
	  		return;
		}
		String speedModeSelected = Client.instance.setmgr.getSettingByName("Speed Mode").getValString();
		setDisplayName(getName() + " " + ChatFormatting.WHITE + speedModeSelected);
		if (event.getType() == EventMotionUpdate.Type.PRE) {

			if (speedModeSelected.equalsIgnoreCase("All AC Bhop")) {
				AllAC();
			}

			if (speedModeSelected.equalsIgnoreCase("McCentral")) {
				mcCentral(event);
			}

			if (speedModeSelected.equalsIgnoreCase("Hypixel")) {
				hypixel(event);
			}
			
		}
	}

	@EventTarget
	public void onMove(EventMove event) {
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1;

		mc.thePlayer.capabilities.isFlying = false;
	}

	public void AllAC() {
		if (mc.thePlayer != null && mc.theWorld != null) {
			if (mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				mc.gameSettings.keyBindJump.pressed = false;
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.timer.timerSpeed = 1.05F;
					mc.thePlayer.motionX *= 1.0708F;
					mc.thePlayer.motionZ *= 1.0708F;
					mc.thePlayer.moveStrafing *= 2;
				} else {
					mc.thePlayer.jumpMovementFactor = 0.0265F;
				}
			}
		}
	}

	public void mcCentral(EventMotionUpdate event) {
		if (mc.thePlayer != null && mc.theWorld != null) {
			if (mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				mc.gameSettings.keyBindJump.pressed = false;
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					event.setMotion(0.5);
					mc.timer.timerSpeed = 1;
				}
			}
		}
	}
	private boolean boosted;

	public void hypixel(EventMotionUpdate event) {
		if (this.mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || this.mc.gameSettings.keyBindRight.pressed || this.mc.gameSettings.keyBindLeft.pressed) {

			this.mc.gameSettings.keyBindSprint.pressed = true;

			if (this.mc.thePlayer.onGround) {

				this.mc.gameSettings.keyBindJump.pressed = false;
				this.mc.thePlayer.jump();
				this.boosted = false;

			} else {
				double speed = 1;
                double Motion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
                if (mc.thePlayer.hurtTime < 5) {
                   mc.thePlayer.motionX = -Math.sin((double)getDirection()) * speed * Motion;
                   mc.thePlayer.motionZ = Math.cos((double)getDirection()) * speed * Motion;
                
				if (!this.boosted) {

					this.mc.timer.timerSpeed = 1.05f;

					double motionV = 0.15;

					double x = this.getPosForSetPosX(motionV);
					double z = this.getPosForSetPosZ(motionV);

					this.mc.thePlayer.motionX = this.mc.thePlayer.motionX + x;
					this.mc.thePlayer.motionZ = this.mc.thePlayer.motionZ + z;
					this.boosted = true;
					}
                }
			}
    	}	
	}
	 public static float getDirectionAAC() {
	        Minecraft mc = Minecraft.getMinecraft();
	        float var1 = mc.thePlayer.rotationYaw;

	        if (mc.thePlayer.moveForward < 0.0F) {
	            var1 += 180.0F;
	        }

	        float forward = 1.0F;

	        if (mc.thePlayer.moveForward < 0.0F) {
	            forward = -0.5F;
	        } else if (mc.thePlayer.moveForward > 0.0F) {
	            forward = 0.5F;
	        }

	        if (mc.thePlayer.moveStrafing > 0.0F) {
	            var1 -= 90.0F * forward;
	        }

	        if (mc.thePlayer.moveStrafing < 0.0F) {
	            var1 += 90.0F * forward;
	        }

	        var1 *= 0.017453292F;
	        return var1;
	    }
	    
	    public static float getDirection() {
	        float var1 = mc.thePlayer.rotationYaw;
	        if (mc.thePlayer.moveForward < 0.0F) {
	           var1 += 180.0F;
	        }

	        float forward = 1.0F;
	        if (mc.thePlayer.moveForward < 0.0F) {
	           forward = -0.5F;
	        } else if (mc.thePlayer.moveForward > 0.0F) {
	           forward = 0.5F;
	        }

	        if (mc.thePlayer.moveStrafing > 0.0F) {
	           var1 -= 90.0F * forward;
	        }

	        if (mc.thePlayer.moveStrafing < 0.0F) {
	           var1 += 90.0F * forward;
	        }

	        var1 *= 0.017453292F;
	        return var1;
	     }
	    
	    public double getPosForSetPosX(double value) {
			double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
			double x = -Math.sin(yaw) * value;
			return x;
		}

		public double getPosForSetPosZ(double value) {
			double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
			double z = Math.cos(yaw) * value;
			return z;
		}
		

		
}
