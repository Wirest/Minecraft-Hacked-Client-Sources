package info.spicyclient.modules.movement;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventMove;
import info.spicyclient.events.listeners.EventOnLadder;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import optifine.MathUtils;

public class Bhop extends Module {
	
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "PvpLands", "Hypixel", "Test", "Test 2", "Test 3");
	
	public NumberSetting hypixelSpeed = new NumberSetting("Speed", 0.01, 0.0001, 0.03, 0.0001);
	
	private static double lastY;
	private static float rotate = 180;
	
	private static int lagbackCheck = 0;
	private static long lastLagback = System.currentTimeMillis();
	
	private transient boolean toggle = true;
	
	public Bhop() {
		super("Bhop", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode, hypixelSpeed);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		if (e.setting.getSetting() == mode) {
			
			if (mode.is("Hypixel") || mode.getMode() == "Hypixel") {
				
				if (!this.settings.contains(hypixelSpeed)) {
					this.settings.add(hypixelSpeed);
				}
				reorderSettings();
				
			}else {
				
				if (this.settings.contains(hypixelSpeed)) {
					this.settings.remove(hypixelSpeed);
				}
				reorderSettings();
				
			}
			
		}
	}
	
	public void onEnable() {
		this.speed = 0.0D;
		if (SpicyClient.config.fly.isEnabled()) {
			toggle();
			NotificationManager.getNotificationManager().createNotification("Don't use bhop while fly is enabled!", "", true, 5000, Type.WARNING, Color.RED);
		}
		
		lastY = mc.thePlayer.posY;
	}
	
	public void onDisable() {
		mc.timer.ticksPerSecond = 20f;
		this.mc.timer.timerSpeed = 1.00f;
		status = 0;
	}
	
	private int status = 0;
	private boolean boosted = false;
	
    private double speed;
    private double lastDist;
    public static int stage;
    
    private transient Timer timer = new Timer();
    
	public void onEvent(Event e) {
		
		if (e instanceof EventPacket) {
			
			if (e.isPre()) {
				
				EventPacket packetEvent = (EventPacket) e;
				
				if (packetEvent.packet instanceof S08PacketPlayerPosLook) {
					
					if (lagbackCheck >= 3) {
						
						lagbackCheck = 0;
						lastLagback = System.currentTimeMillis() - (5*1000);
						this.toggle();
						NotificationManager.getNotificationManager().createNotification(this.name + " has been disabled to prevent flags", "", true, 1000, Type.WARNING, Color.RED);
						
					}else {
						
						lastLagback = System.currentTimeMillis();
						lagbackCheck++;
						
					}
					
				}
				
			}
			
		}
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				if (lastLagback + (5*1000) < System.currentTimeMillis()) {
					
					lagbackCheck = 0;
					lastLagback = System.currentTimeMillis() + (10*1000);
					
				}
				
			}
			
		}
		
		if (e instanceof EventUpdate) {
			if (e.isPre()) {
				this.additionalInformation = mode.getMode();
				
				if (mode.getMode().equalsIgnoreCase("Vanilla") && mc.gameSettings.keyBindForward.pressed) {
					if (mc.thePlayer.onGround) {
						mc.gameSettings.keyBindJump.pressed = false;
						mc.thePlayer.jump();
						mc.thePlayer.setSprinting(true);
					}
				}
			}
		}
		
		if (e instanceof EventMotion) {
			
			if (e.isBeforePost()) {
				
				EventMotion event = (EventMotion) e;
				
				if (mode.getMode().equalsIgnoreCase("Pvplands") && !mc.thePlayer.isInWater()) {
					
					if (mc.thePlayer.onGround && mc.gameSettings.keyBindForward.pressed) {
						mc.thePlayer.setSprinting(true);
						mc.gameSettings.keyBindJump.pressed = false;
			            mc.thePlayer.jump();
			            mc.thePlayer.jump();
			            mc.thePlayer.setSprinting(true);
			            //mc.thePlayer.motionY = 0.1f;
					}
					else if (mc.gameSettings.keyBindForward.pressed) {
			            float f = mc.thePlayer.rotationYaw * 0.017453292F;
			            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.035F);
			            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.035F);
					}
					
				}
				else if (mode.getMode().equalsIgnoreCase("Pvplands") && mc.thePlayer.isInWater()) {
					if (mc.thePlayer.onGround) {
						mc.gameSettings.keyBindJump.pressed = false;
						mc.thePlayer.jump();
						mc.thePlayer.setSprinting(true);
					}
				}
				else if (mode.is("Hypixel") && (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && (!SpicyClient.config.blockFly.isEnabled() || (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null) || true)) {
					
					mc.gameSettings.keyBindJump.pressed = false;
					
					if (!mc.thePlayer.isInWater()) {
						
						mc.thePlayer.noClip = true;
						
						if (MovementUtils.isOnGround(0.00004)) {
							
							//MovementUtils.setMotion((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + ((float)hypixelSpeed.getValue() * 15));
							mc.thePlayer.jump();
							
							e.setCanceled(true);
							
						}
						
						if (MovementUtils.isOnGround(1)) {
							mc.timer.ticksPerSecond = 20;
						}else if (mc.thePlayer.fallDistance < 1.5) {
							mc.timer.ticksPerSecond = 26.5f + new Random().nextFloat();
						}
						
						mc.thePlayer.setSprinting(true);
						//MovementUtils.strafe((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + 0.01f);
						MovementUtils.setMotion((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + ((float)hypixelSpeed.getValue()));
						
					}else {
						
					}
					
				}
				else if (mode.is("Hypixel") && (!!SpicyClient.config.blockFly.isEnabled() || (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null))) {
					
					mc.timer.ticksPerSecond = 20;
					
				}
				else if (mode.is("Test") && (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed)) {
					
					mc.gameSettings.keyBindJump.pressed = false;
					
					if (!mc.thePlayer.isInWater()) {
						
						mc.thePlayer.noClip = true;
						
						if (!MovementUtils.isOnGround(1)) {
							
							mc.thePlayer.motionY = -0.01 + (new Random().nextDouble() / 100);
							//e.setCanceled(true);
							
						}
						else if (MovementUtils.isOnGround(0.000000000004)) {
							mc.thePlayer.jump();
						}
						
						mc.thePlayer.setSprinting(true);
						//MovementUtils.strafe((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + 0.01f);
						MovementUtils.setMotion((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + ((float)hypixelSpeed.getValue()));
						
					}else {
						
					}
					
				}
				else if (mode.is("Test 3")) {
					
					if ((!SpicyClient.config.blockFly.isEnabled() || (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null))) {
						
						if (MovementUtils.isOnGround(1)) {
							mc.timer.ticksPerSecond = 20;
						}else if (mc.thePlayer.fallDistance < 3) {
							mc.timer.ticksPerSecond = 26.5f + new Random().nextFloat();
						}
						
					}else {
						
						mc.timer.ticksPerSecond = 20;
						
					}
					
				}
				
			}
			
		}
		
		if (e instanceof EventMotion && mc.thePlayer.onGround && mode.is("Test 3")) {
            ((EventMotion)e).setY(((EventMotion)e).getY() + (double)RandomUtils.nextFloat(1.0E-5F, 9.9E-4F));
        }

        if (e instanceof EventMove && MovementUtils.isMoving() && !mc.thePlayer.isInWater() && e.isBeforePre() && mode.is("Test 3") && (!SpicyClient.config.blockFly.isEnabled() || (SpicyClient.config.killaura.isEnabled() && SpicyClient.config.killaura.target != null))) {
        	forward =  0.2873D;
            if (mc.thePlayer.onGround) {
                ((EventMove)e).setY(mc.thePlayer.motionY = 0.41999998688697815D);
                this.speed = forward * 2.1500000953674316D;
                this.lastDistanceReset = true;
            } else if (!this.lastDistanceReset && !mc.thePlayer.isCollidedHorizontally) {
                //this.speed -= this.speed / 159.0D;
            	this.speed -= this.speed / 165.0D;
            } else {
                this.speed -= 0.66D * (this.speed - forward);
                this.lastDistanceReset = false;
            }

            MovementUtils.setMotion(Math.max(this.speed, forward));
            ((EventMove)e).setSpeed(Math.max(this.speed, speed));
        }
		
	}
	
	public static transient float Speed = 0;
	public static transient boolean lastDistanceReset = false;
	
	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
	}
    double forward;
    double strafe;
}
