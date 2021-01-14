package store.shadowclient.client.module.movement;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.MathUtils;
import store.shadowclient.client.utils.MovementUtils;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Fly extends Module {
	
	ArrayList<Packet> packetListZ = new ArrayList<>();
	
    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("CrystalAC");
        options.add("Hypixel");
        options.add("Hypixel2");
        options.add("HypixelBoost");
        options.add("Vanilla");
        options.add("Spartan");
        options.add("AAC 3.0.5");
        options.add("AAC");
        options.add("Fast2");
        options.add("NCP");
        options.add("CubeCraft");
        options.add("MCCentral");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Hypixel", options));
        
        Shadow.instance.settingsManager.rSetting(new Setting("Timer Boost", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("FlyBlink", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("View Bobbing", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("Timer Speed", this, 2.4F, 1F, 6F, false));
    }

    //FINALS
    private final TimeHelper timer = new TimeHelper();
    
    //DOUBLES
    private double moveSpeed, lastDistance;
    public static double hypixel;
    private double flycrit;
    private double lastDist;
    private double startY;
    private double aad;
    double roofY = -1.0D;
    
    //BOOLENS
    private boolean failedStart = false;
    private boolean boosted;
    private boolean aac;
    private boolean canFly;
    private Fly flyModule;
    boolean wasOnGround = true;
    private EntityOtherPlayerMP player;
    private ArrayList<Packet> packets;

    //FLOATS
    private float flytimer;
    
    //INTS
    private int xd;
    private int count;
    private int stage;
    private int state;
    private int counter;
    private int boostHypixelState = 1;
    private int delay = 0;
    public static int fastFlew;
    
    //LONGS
    private long deltaTime;
    private long startTime;
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	
    	String mode = Shadow.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        this.setDisplayName("Fly §7| " + mode); // Displays flight mode in array list.

        
        if(mode.equalsIgnoreCase("HypixelBoost")) { // Hypixel Boost
        if(mc.thePlayer.onGround) {
        	return;
        }

        for (int i = 0; i < 10; i++) //Imagine flagging to NCP.
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        
        double fallDistance = 3.0125; //add 0.0125 to ensure we get the fall dmg
        while (fallDistance > 0) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
            fallDistance -= 0.0624986421;
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        
        mc.thePlayer.jump();
        boostHypixelState = 1;
        moveSpeed = 0.1D;
        lastDistance = 0D;
        failedStart = false;
        }
        
        if(mode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = true;
        }

        if(mode.equalsIgnoreCase("Spartan")) {
        	this.mc.thePlayer.motionY = 0;
    		this.state += 1;
    		switch (this.state) {
    		case 1:
    			mc.timer.timerSpeed = 1.4F;
    	    	mc.thePlayer.motionY = 0.0;
    	    	mc.thePlayer.onGround = true;
    			break;
    		case 2:
    			mc.timer.timerSpeed = 1.5F;
    	    	mc.thePlayer.motionY = 0.0;
    	    	mc.thePlayer.onGround = true;
    			break;
    		case 3:
    			mc.timer.timerSpeed = 1.3F;
    	    	mc.thePlayer.motionY = 0.0;
    	    	mc.thePlayer.onGround = true;
    			this.state = 0;
    			break;
        	}
        }
        
        if(mode.equalsIgnoreCase("AAC 3.0.5")) {
            if(this.delay == 0) {
               this.mc.timer.timerSpeed = 1.1F;
            }

            if(this.delay == 2) {
               this.mc.thePlayer.motionX *= 1.1D;
               this.mc.thePlayer.motionZ *= 1.1D;
               this.mc.thePlayer.motionY = 0.1D;
            } else if(this.delay > 2) {
               this.mc.timer.timerSpeed = 1.0F;
               this.delay = 0;
            }

            ++this.delay;
        }
        if (mode.equalsIgnoreCase("AAC")) {
			mc.thePlayer.setSprinting(false);
			if ((mc.thePlayer.fallDistance >= 4.0F) && (!this.aac)) {
				this.aac = true;
				this.aad = (mc.thePlayer.posY + 3.0D);
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
			mc.thePlayer.capabilities.isFlying = false;
			if (this.aac) {
				if (mc.thePlayer.onGround) {
					this.aac = false;
				}
				if (mc.thePlayer.posY < this.aad) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
					if (mc.gameSettings.keyBindSneak.pressed) {
						this.aad -= 2.0D;
					} else if ((mc.gameSettings.keyBindSneak.pressed) && (mc.thePlayer.posY < this.aad + 0.8D)) {
						this.aad += 2.0D;
					} else {
						mc.thePlayer.motionY = 0.7D;

						gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(0.8f);
					}
				}
			} else {
				mc.thePlayer.capabilities.isFlying = false;
			}
		}
        
        if(mode.equalsIgnoreCase("CubeCraft")) {
        	if (MovementUtils.isMoving()) {
				if (mc.thePlayer.ticksExisted % 3 == 0) {
					mc.thePlayer.motionY = .2;
					mc.timer.timerSpeed = 1F;
					MovementUtils.doStrafe(3);
				} else {
					mc.thePlayer.motionY = -.1;
					mc.timer.timerSpeed = .5F;
					MovementUtils.doStrafe(.2);
				}
			} else {
				stop(true);
				mc.timer.timerSpeed = 1F;
			}
        }

        if(mode.equalsIgnoreCase("MCCentral")) {
        	if(mc.thePlayer.onGround) {
        		mc.timer.timerSpeed = 1F;
        	}else {
        		mc.thePlayer.motionY = -0.001;
        		mc.timer.timerSpeed = 0.8F;
        	}
        }

        if(mode.equalsIgnoreCase("Hypixel")) {
        	if(Shadow.instance.settingsManager.getSettingByName("Timer Boost").getValBoolean()) {
        		mc.timer.timerSpeed = (float) Shadow.instance.settingsManager.getSettingByName("Timer Speed").getValDouble();
        	}
        	if(Shadow.instance.settingsManager.getSettingByName("View Bobbing").getValBoolean()) {
        		getMc().thePlayer.cameraYaw = 0.089f;
        	}
            //mc.timer.timerSpeed = 2.4F;
	        if (this.mc.thePlayer.onGround) {
	        	//damage();
	            //this.mc.thePlayer.jump();
	         } else {
	             this.mc.thePlayer.motionY = 0;
	             mc.thePlayer.onGround = true;
	             this.state += 1;
	             switch (this.state) {
	             case 1:
	                 this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12D, this.mc.thePlayer.posZ);
	                 break;
	             case 2:
	                 this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-12D,
	                         this.mc.thePlayer.posZ);
	                 break;
	             case 3:
	                 this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12D,
	                         this.mc.thePlayer.posZ);
	                 this.state = 1;
	                 break;
	             default:
	                 break;
	             }
	        }
        }
        
        if(mode.equalsIgnoreCase("Hypixel2")) {
            double y;
            double y1;
            mc.thePlayer.motionY = 0;
            if(mc.thePlayer.ticksExisted % 3 == 0) {
                y = mc.thePlayer.posY - 1.0E-10D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
            }
            mc.thePlayer.onGround = true;
            y1 = mc.thePlayer.posY + 1.0E-10D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
        }

        if(mode.equalsIgnoreCase("Vanilla"))
            mc.thePlayer.capabilities.isFlying = true;
        
        if(mode.equalsIgnoreCase("CrystalAC")) {
        	if (getMc().gameSettings.keyBindJump.isKeyDown()) {
                getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + 0.15, getMc().thePlayer.posZ);
                getMc().thePlayer.motionY = 0.15;
            } else if (getMc().gameSettings.keyBindSneak.isKeyDown()) {
                getMc().thePlayer.setPosition(getMc().thePlayer.posX, getMc().thePlayer.posY - 0.15, getMc().thePlayer.posZ);
                getMc().thePlayer.motionY = -0.15;
            } else getMc().thePlayer.motionY = 0;
            if (getMc().getCurrentServerData() != null && getMc().getCurrentServerData().serverIP != null && getMc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                event.setOnGround(true);
            }
            double result = 0.00000000334947 + MathUtils.getRandomInRange(0.00000000014947, 0.00000000064947);
            if (getMc().thePlayer.ticksExisted % 3 == 0) {
                event.setY(getMc().thePlayer.posY + result);
                event.setOnGround(false);
            }
            /*if (getMc().thePlayer.ticksExisted % 3 == 0) {
                ascension += value;
            }
            if (ascension > value * 7) {
                event.setOnGround(false);
                event.setY(getMc().thePlayer.posY + ascension);
                ascension /= 1.125F;
            }*/
           // Printer.print(""+event.getY());
        }
        
        if(mode.equalsIgnoreCase("NCP")) {
        	if (!Minecraft.getMinecraft().thePlayer.onGround && this.wasOnGround) {
                this.roofY = Minecraft.getMinecraft().thePlayer.posY + 0.0D;
             }

             if (Minecraft.getMinecraft().thePlayer.onGround) {
                this.roofY = -1.0D;
             }

             this.wasOnGround = Minecraft.getMinecraft().thePlayer.onGround;
             if (this.isToggled()) {
                if (!Minecraft.getMinecraft().thePlayer.isOnLadder() && !Minecraft.getMinecraft().thePlayer.onGround) {
                   if (Keyboard.isKeyDown(57)) {
                      if (this.roofY != -1.0D) {
                         if (Minecraft.getMinecraft().thePlayer.posY + 0.4D > this.roofY) {
                            Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
                         } else {
                            Minecraft.getMinecraft().thePlayer.motionY = 0.4D;
                         }
                      }
                   } else if (Keyboard.isKeyDown(42)) {
                      Minecraft.getMinecraft().thePlayer.motionY = -0.4D;
                   } else {
                      Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
                      mc.thePlayer.onGround = true;
                   }
                }

                if (this.roofY != -1.0D && Minecraft.getMinecraft().thePlayer.posY > this.roofY) {
                   Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, this.roofY, Minecraft.getMinecraft().thePlayer.posZ);
                }

             }
        }
    }
    
    public void gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(float speed) {
		mc.thePlayer.motionX = (-(Math.sin(aan()) * speed));
		mc.thePlayer.motionZ = (Math.cos(aan()) * speed);
	}
    
    public float aan() {
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
    
    /*
     * AAC 2 FLIGHT UTILS
     */
    
    public void setSpeed(final float speed) {
        double yaw = mc.thePlayer.rotationYaw;
        final boolean isMoving = mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
        final boolean isMovingForward = mc.thePlayer.moveForward > 0.0f;
        final boolean isMovingBackward = mc.thePlayer.moveForward < 0.0f;
        final boolean isMovingRight = mc.thePlayer.moveStrafing > 0.0f;
        final boolean isMovingLeft = mc.thePlayer.moveStrafing < 0.0f;
        final boolean isMovingSideways = isMovingLeft || isMovingRight;
        final boolean isMovingStraight = isMovingForward || isMovingBackward;
        if (isMoving) {
            if (isMovingForward && !isMovingSideways) {
                yaw += 0.0;
            }
            else if (isMovingBackward && !isMovingSideways) {
                yaw += 180.0;
            }
            else if (isMovingForward && isMovingLeft) {
                yaw += 45.0;
            }
            else if (isMovingForward) {
                yaw -= 45.0;
            }
            else if (!isMovingStraight && isMovingLeft) {
                yaw += 90.0;
            }
            else if (!isMovingStraight && isMovingRight) {
                yaw -= 90.0;
            }
            else if (isMovingBackward && isMovingLeft) {
                yaw += 135.0;
            }
            else if (isMovingBackward) {
                yaw -= 135.0;
            }
            yaw = Math.toRadians(yaw);
            mc.thePlayer.motionX = -Math.sin(yaw) * speed;
            mc.thePlayer.motionZ = Math.cos(yaw) * speed;
        }
    }
    
    public void drawBox(BlockPos pos, int multiplier, Color color) {
        final RenderManager renderManager = mc.getRenderManager();
        final net.minecraft.util.Timer timer = mc.timer;
        
        final double x = pos.getX() - renderManager.renderPosX;
        final double y = pos.getY() - renderManager.renderPosY;
        final double z = pos.getZ() - renderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        final Block block = mc.theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, pos)
                    .expand(2 * multiplier, .002, 2 * multiplier)
                    .offset(-posX, -posY, -posZ);
            
            Shadow.RENDER3D.drawAxisAlignedBBFilled(axisAlignedBB, color, true);
        }
    }
    
    @Override
    public void onDisable() {
    	String mode = Shadow.instance.settingsManager.getSettingByName("Fly Mode").getValString();
    	mc.timer.timerSpeed = 1F;
    	if(Shadow.instance.settingsManager.getSettingByName("FlyBlink").getValBoolean()) {
    		for(final Packet packet : packetListZ){
    			mc.getNetHandler().addToSendQueue(packet);
    		}
    		packetListZ.clear();
    	}
        if(mode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = false;
        }
        
        super.onDisable();
    }
    
    public final void getYaw(boolean strafing) {
        float rotationYaw = strafing ? mc.thePlayer.rotationYawHead : mc.thePlayer.rotationYaw;
        float forward = 1F;

        final double moveForward = mc.thePlayer.movementInput.moveForward;
        final double moveStrafing = mc.thePlayer.movementInput.moveStrafe;
        final float yaw = mc.thePlayer.rotationYaw;
        
        if (moveForward < 0) {
        	rotationYaw += 180F;
        }

        if (moveForward < 0) {
        	forward = -0.5F;
        } else if(moveForward > 0) {
        	forward = 0.5F;
        }

        if (moveStrafing > 0) {
        	rotationYaw -= 90F * forward;
        } else if(moveStrafing < 0) {
        	rotationYaw += 90F * forward;
        }
    }

    public final void stop(boolean y) {
    	mc.thePlayer.motionX = 0;
    	mc.thePlayer.motionZ = 0;
    	if (y) mc.thePlayer.motionY = 0;
    }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("Fly Mode").getValString();
    	Packet packet = event.getPacket();
    	if(Shadow.instance.moduleManager.getModuleByName("Fly").isToggled()) {
	    	if(Shadow.instance.settingsManager.getSettingByName("FlyBlink").getValBoolean()) {
	    	 if (packet instanceof C03PacketPlayer) {
	             packetListZ.add(packet);
	             event.setCancelled(true);
	             System.out.println("Packet cancelled");
	           }
	    	} else {
	    		return;
	    	}
    	}
    }
    
    @Override
    public void onEnable() {
    	damage();
    	super.onEnable();
    }
    
    public float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return (float)(Minecraft.getMinecraft().thePlayer.getMaxFallHeight() + f);
     }
    
    public void damage() {
        double offset = 0.060100000351667404D;
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        for(int i = 0; (double)i < (double)getMaxFallDist() / 0.05510000046342611D + 1.0D; ++i) {
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404D, z, false));
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4D, z, false));
           netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
        
        }
        netHandler.addToSendQueue(new C03PacketPlayer(true));
     }
}
