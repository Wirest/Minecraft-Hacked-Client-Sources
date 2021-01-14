package store.shadowclient.client.module.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventPreMotionUpdate;
import store.shadowclient.client.event.events.EventReceivePacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Speed extends Module {
	
	public static TimeHelper lastCheck = new TimeHelper();
	
	public static TimeHelper timer;
	
	//NCP
	private int level = 1;
    private double moveSpeed = 0.2873;
    public static double moveSpeed2;
    private static double lastDist;
    private int timerDelay;
	
	//AAC
	private int airMoves;
	double speed;
	double Motion;
	
	//HYPIXEL
	private boolean boosted;

	//INT
	int spoofSlot;
	private int state;
	private int stage;
	private static int stage1 = 1;
	
	//PIKANETWORK

	public Speed() {
        super("Speed", Keyboard.KEY_V, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        
        //HYPIXEL
        options.add("HypixelBHop");
        
        //OTHER
        options.add("HiveHop");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "HiveHop", options));
        Shadow.instance.settingsManager.rSetting(new Setting("Speed LagBack", this, false));
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("Speed Mode").getValString();
    	this.setDisplayName("Speed §7| " + mode);
    }
    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
    	float forward1 = MovementInput.moveForward;
        float strafe1 = MovementInput.moveStrafe;
        float yaw1 = mc.thePlayer.rotationYaw;
    	String mode = Shadow.instance.settingsManager.getSettingByName("Speed Mode").getValString();
    	

        if(mode.equalsIgnoreCase("HypixelBHop")) {
        	this.setDisplayName("Speed §7| " + "HypixelBhop");
        	if (this.mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || this.mc.gameSettings.keyBindRight.pressed || this.mc.gameSettings.keyBindLeft.pressed) {

				this.mc.gameSettings.keyBindSprint.pressed = true;

				if (this.mc.thePlayer.onGround) {

					this.mc.gameSettings.keyBindJump.pressed = false;
					this.mc.thePlayer.jump();
					this.boosted = false;

				} else {
					speed = 1.0044D;
                    Motion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
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
        
        	if(mode.equalsIgnoreCase("HiveHop")) {
        		this.setDisplayName("Speed §7| " + "HiveHop");
        		mc.gameSettings.keyBindJump.pressed = false;
	                if (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed) {
	                   if (mc.thePlayer.onGround) {
	                      mc.thePlayer.jump();
	                      mc.thePlayer.motionY = 0.42D;
	                      mc.timer.timerSpeed = 1.0F;
	                   } else {
	                      speed = 1.0044D;
	                      Motion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
	                      if (mc.thePlayer.hurtTime < 5) {
	                         mc.thePlayer.motionX = -Math.sin((double)getDirection()) * speed * Motion;
	                         mc.thePlayer.motionZ = Math.cos((double)getDirection()) * speed * Motion;
	                      }
	                   }
	                }
	        	}
    		}
        	
    	
      
    
    
            private double getBaseMoveSpeed() {
                double baseSpeed = 0.2873;
                if(mc.thePlayer.isPotionActive(Potion.moveSpeed))
                    baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
                return baseSpeed;
            }

            private double round(double value) {
                BigDecimal bigDecimal = new BigDecimal(value);
                bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
                return bigDecimal.doubleValue();
            }
            
            public double round2(final double value, final int places) {
                if (places < 0) {
                    throw new IllegalArgumentException();
                }
                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(places, RoundingMode.HALF_UP);
                return bd.doubleValue();
            }
            
            public void onMotion() {
                double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            }
            
    private boolean isOnLiquid() {
        boolean onLiquid = false;
        final int y = (int)(mc.thePlayer.boundingBox.minY - .01);
        for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if(block != null && !(block instanceof BlockAir)) {
                    if(!(block instanceof BlockLiquid))
                        return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
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
	
	@EventTarget
    public void receivePacket(EventReceivePacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook)
            if (Shadow.instance.settingsManager.getSettingByName("Speed LagBack").getValBoolean()) {
            	Shadow.addChatMessage("Disabled Speed (Lagback Checks)");
            	//NotificationPublisher.queue("Flag", "Disabled Flight due to flag.", NotificationType.INFO);
            	//NotificationManager.show(new Notification(NotificationType.INFO, " " + EnumChatFormatting.RED + "Disabled" + EnumChatFormatting.WHITE + getName() + "due to lagback", 2));
                mc.thePlayer.onGround = false;
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                mc.thePlayer.jumpMovementFactor = 0;
                this.toggle();
            } else if (lastCheck.delay(300)) {
                S08PacketPlayerPosLook.yaw = mc.thePlayer.rotationYaw;
                S08PacketPlayerPosLook.pitch = mc.thePlayer.rotationPitch;
            }
    }
	
	private void MineplexSpoof() {
        try {
            int i = 36;
            while (i < 45) {
                final int theSlot = i - 36;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                    if (mc.thePlayer.inventory.currentItem != theSlot) {
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(theSlot));
                        mc.playerController.updateController();
                        this.spoofSlot = theSlot;
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        catch (Exception ex) {}
    }
	
	private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
	
	@Override
    public void onDisable() {
    	this.mc.timer.timerSpeed = 1.0f;
    	super.onDisable();
    }
	
	@Override
	public void onEnable() {
		level = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.isCollidedVertically ? 1 : 4;
		super.onEnable();
	}
}
