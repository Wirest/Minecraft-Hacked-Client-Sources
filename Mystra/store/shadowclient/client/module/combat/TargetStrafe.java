package store.shadowclient.client.module.combat;

import java.awt.Color;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.Event3D;
import store.shadowclient.client.event.events.EventSpeed;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class TargetStrafe extends Module {
	
	public static int direction = 1;
    public static boolean canMove;
    public double movespeed2;
    
	public TargetStrafe() {
		super("TargetStrafe", 0, Category.COMBAT);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Target Distance", this, 1.90f, 1.0f, 6.0f, false));
		Shadow.instance.settingsManager.rSetting(new Setting("Speed Only", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("Allow Fly", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("CircleESP", this, false));
	}
	@EventTarget
    public void onMove(EventSpeed event) {
    	if(isToggled()) {
    		if(Shadow.instance.moduleManager.getModuleByName("Aura").isToggled()) {
    			if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null) {
    				return;
    			}
    			float rotations[] = getRotationsEntity(Aura.ThisIsTheEntityThatThePlayerIsHittingTo);
    			movespeed2 = getBaseMoveSpeed();
	    		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getDistanceToEntity(mc.thePlayer) < Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble()) {
	    			if(!Shadow.instance.settingsManager.getSettingByName("Allow Fly").getValBoolean()) {
	    				if(Shadow.instance.moduleManager.getModuleByName("Fly").isToggled()) {
	    					return;
	    				} else {
	    					if(mc.gameSettings.keyBindRight.isPressed()) {
	    	    		    	this.direction = -1;
	    	    		    }
	    	    			if(mc.gameSettings.keyBindLeft.isPressed()) {
	    	    		    	this.direction = 1;
	    	    		    }
	    	    			canMove = true;
	    				}
	    			}
	    			
	    			/*
	    			 * Speed Only Value
	    			 */
	    			
	    			if(Shadow.instance.settingsManager.getSettingByName("Speed Only").getValBoolean()) {
	    				if(Shadow.instance.moduleManager.getModuleByName("Speed").isToggled()) {
	    					if(mc.gameSettings.keyBindRight.isPressed()) {
	    	    		    	this.direction = -1;
	    	    		    }
	    	    			if(mc.gameSettings.keyBindLeft.isPressed()) {
	    	    		    	this.direction = 1;
	    	    		    }
	    	    			canMove = true;
	    	    			if(Shadow.instance.moduleManager.getModuleByName("Speed").isToggled()) {
	    	    				if(direction == -1 || direction == 1) {
	    	    					mc.gameSettings.keyBindForward.pressed = true;
	    	    				}
	    	    			}
	    				} else {
	    					return;
	    				}
	    			}
	    			
	    			/*
	    			 * Normal
	    			 */
	    			
	    			if(mc.gameSettings.keyBindRight.isPressed()) {
	    		    	this.direction = -1;
	    		    }
	    			if(mc.gameSettings.keyBindLeft.isPressed()) {
	    		    	this.direction = 1;
	    		    }
	    			
	    			canMove = true;
	    			if(Shadow.instance.moduleManager.getModuleByName("Speed").isToggled()) {
	    				if(direction == -1 || direction == 1) {
	    					mc.gameSettings.keyBindForward.pressed = true;
	    				}
	    			}
	    			
	    			if(canMove == true) {
	    				this.strafe(event, movespeed2, rotations[0], direction, 0.0D);
	    			}
    			} else {
    				if(canMove == true) {
	    				this.strafe(event, movespeed2, rotations[0], direction, 1.0D);
	    			}
    				canMove = false;
    			}
    		} else {
    			canMove = false;
    		}
    	}
    }
	
	@EventTarget
	public void onRender3D(Event3D event) {
		if(this.isToggled()) {
			if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null) {
				return;
			}
			if(Shadow.instance.settingsManager.getSettingByName("CircleESP").getValBoolean()) {
				if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getDistanceToEntity(mc.thePlayer) < Shadow.instance.settingsManager.getSettingByName("KillRange").getValDouble()) {
					final int[] counter = {1};
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.002, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.004, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.006, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.008, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.010, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.012, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.014, novoline(counter[0] * 300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble() + 0.016, novoline(300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("Target Distance").getValDouble(), novoline(counter[0] * 300));
					counter[0]++;
				}
			}
		}
	}
	
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
    public static void strafe(EventSpeed event, double movespeed, float yaw, double strafe, double forward) {
    	double fow = forward;
    	double stra = strafe;
    	float ya = yaw;
    	if (fow != 0.0D) {
            if (strafe > 0.0D) {
            	ya += ((fow > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
            	ya += ((fow > 0.0D) ? 45 : -45);
            }
            stra = 0.0D;
            if (fow > 0.0D) {
            	fow = 1.0D;
            } else if (fow < 0.0D) {
            	fow = -1.0D;
            }
        }
        if (stra > 1.0D) {
        	stra = 1.0D;
        } else if (stra < 0.0D) {
        	stra = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((ya + 90.0F)));
        double mz = Math.sin(Math.toRadians((ya + 90.0F)));
        event.setX(fow * movespeed * mx + stra * movespeed * mz);
        event.setZ(fow * movespeed * mz - stra * movespeed * mx);
    }
    public static float[] getRotationsEntity(EntityLivingBase entity) {
        return getRotations(entity.posX + randomNumber(0.03D, -0.03D), entity.posY + entity.getEyeHeight() - 0.4D + randomNumber(0.07D, -0.07D), entity.posZ + randomNumber(0.03D, -0.03D));
    }
    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
    public static float[] getRotations(double posX, double posY, double posZ) {
        EntityLivingBase player = mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - player.posY + player.getEyeHeight();
        double z = posZ - player.posZ;
        double dist = Math.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float)-(Math.atan2(y, dist) * 180.0D / Math.PI);
        return new float[] { yaw, pitch };
    }
    
    public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}
    
    public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
    
    @Override
    public void onEnable() {
    	EventManager.register(this);
    	super.onEnable();
    }
    @Override
    public void onDisable() {
    	EventManager.unregister(this);
    	super.onDisable();
    }
}