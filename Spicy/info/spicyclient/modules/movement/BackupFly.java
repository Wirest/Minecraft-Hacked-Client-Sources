package info.spicyclient.modules.movement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.ibm.icu.math.BigDecimal;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventRenderGUI;
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
import info.spicyclient.util.PlayerUtils;
import info.spicyclient.util.RotationUtils;
import info.spicyclient.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BackupFly extends Module {
	
	public NumberSetting speed = new NumberSetting("Speed", 0.1, 0.01, 2, 0.1);
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Hypixel", "HypixelFast1");
	
	public BooleanSetting hypixelBlink = new BooleanSetting("Blink", true);
	public BooleanSetting hypixelTimerBoost = new BooleanSetting("Hypixel timer boost", true);
	public NumberSetting hypixelSpeed = new NumberSetting("Speed", 0.18, 0.05, 0.2, 0.005);
	public NumberSetting hypixelBoostSpeed = new NumberSetting("Fall speed boost", 2.2, 1.0, 10, 0.1);
	
	public NumberSetting hypixelFastFly1Speed = new NumberSetting("Speed", 0.2675, 0.01, 1.0, 0.0025);
	public BooleanSetting hypixelFastFly1StopOnDisable = new BooleanSetting("Stop on disable", true);
	public BooleanSetting hypixelFastFly1Blink = new BooleanSetting("Blink", false);
	public NumberSetting hypixelFastFly1Decay = new NumberSetting("Decay", 18, 2, 35, 1);
	
	public static ArrayList<Packet> hypixelPackets = new ArrayList<Packet>();
	public static ArrayList<Packet> hypixelFastFly1Packets = new ArrayList<Packet>();
	
	public BackupFly() {
		super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}

	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(speed, mode, hypixelTimerBoost, hypixelSpeed, hypixelBoostSpeed, hypixelFastFly1Speed, hypixelFastFly1StopOnDisable, hypixelFastFly1Decay);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
		if (e.setting.getSetting() == mode) {
			
			if (this.settings.contains(hypixelBlink)) {
				this.settings.remove(hypixelBlink);
			}
			
			if (this.settings.contains(hypixelTimerBoost)) {
				this.settings.remove(hypixelTimerBoost);
			}
			
			if (this.settings.contains(hypixelSpeed)) {
				this.settings.remove(hypixelSpeed);
			}
			
			if (this.settings.contains(hypixelBoostSpeed)) {
				this.settings.remove(hypixelBoostSpeed);
			}
			
			if (this.settings.contains(hypixelFastFly1Speed)) {
				this.settings.remove(hypixelFastFly1Speed);
			}
			
			if (this.settings.contains(hypixelFastFly1StopOnDisable)) {
				this.settings.remove(hypixelFastFly1StopOnDisable);
			}
			
			if (this.settings.contains(speed)) {
				this.settings.remove(speed);
			}
			
			if (this.settings.contains(hypixelFastFly1Blink)) {
				this.settings.remove(hypixelFastFly1Blink);
			}
			
			if (this.settings.contains(hypixelFastFly1Decay)) {
				this.settings.remove(hypixelFastFly1Decay);
			}
			
			reorderSettings();
			
			if (mode.is("Hypixel") || mode.getMode() == "Hypixel") {
				
				if (!this.settings.contains(hypixelBlink)) {
					this.settings.add(hypixelBlink);
				}
				
				if (!this.settings.contains(hypixelTimerBoost)) {
					this.settings.add(hypixelTimerBoost);
				}
				
				if (!this.settings.contains(hypixelSpeed)) {
					this.settings.add(hypixelSpeed);
				}
				
				if (!this.settings.contains(hypixelBoostSpeed)) {
					this.settings.add(hypixelBoostSpeed);
				}
				
				if (this.settings.contains(hypixelFastFly1Speed)) {
					this.settings.remove(hypixelFastFly1Speed);
				}
				
				if (this.settings.contains(hypixelFastFly1StopOnDisable)) {
					this.settings.remove(hypixelFastFly1StopOnDisable);
				}
				
				reorderSettings();
			}
			else if (mode.is("HypixelFast1") || mode.getMode() == "HypixelFast1") {
				
				if (!this.settings.contains(hypixelFastFly1Speed)) {
					this.settings.add(hypixelFastFly1Speed);
				}
				
				if (!this.settings.contains(hypixelFastFly1StopOnDisable)) {
					this.settings.add(hypixelFastFly1StopOnDisable);
				}
				
				if (!this.settings.contains(hypixelFastFly1Blink)) {
					this.settings.add(hypixelFastFly1Blink);
				}
				
				if (!this.settings.contains(hypixelFastFly1Decay)) {
					this.settings.add(hypixelFastFly1Decay);
				}
				
			}else {
				if (!this.settings.contains(speed)) {
					this.settings.add(speed);
				}
			}
			
			reorderSettings();
			
		}
		
	}
	
	public static int fly_keybind = Keyboard.KEY_F;

	public static transient int hypixelStage = 0, verusStage = 0;
	public static transient boolean hypixelDamaged = false;
	public static transient float lastPlayerHealth;
	
	public void onEnable() {
		
		hypixelDamaged = false;
		if (mode.getMode().equals("Vanilla")) {
			original_fly_speed = mc.thePlayer.capabilities.getFlySpeed();
			
			if (MovementUtils.isOnGround(0.0001)) {
				mc.thePlayer.posY += 0.5;
			}
			
		} else if (mode.getMode().equals("Hypixel")) {
			
			if (mc.isSingleplayer()) {
				//Command.sendPrivateChatMessage("You cannot use hypixel fly in singleplayer!");
				NotificationManager.getNotificationManager().createNotification("Don't use hypixel fly in singleplayer!", "", true, 5000, Type.WARNING, Color.RED);
				this.toggle();
			}
			
			if (SpicyClient.config.bhop.isEnabled()) {
				SpicyClient.config.bhop.toggle();
				NotificationManager.getNotificationManager().createNotification("Don't use bhop while fly is enabled!", "", true, 5000, Type.WARNING, Color.RED);
			}
			
			hypixelStartTime = (long) (System.currentTimeMillis());
			
			if (!SpicyClient.config.blink.isEnabled()) {
				//SpicyClient.config.blink.toggle();
			}
			
			//damage();
			if (MovementUtils.isOnGround(0.0001)) {
				//damage();
				//mc.thePlayer.onGround = false;
				//MovementUtils.setMotion(0);
				//mc.thePlayer.jumpMovementFactor = 0;
				
				mc.thePlayer.jump();
			}
			
			mc.thePlayer.stepHeight = 0;
			
			if (!hypixelBlink.isEnabled()) {
				
				lastPlayerHealth = mc.thePlayer.getHealth();
				//damage();
				hypixelDamaged = true;
				
	            PlayerCapabilities playerCapabilities = new PlayerCapabilities();
	            playerCapabilities.isFlying = true;
	            playerCapabilities.allowFlying = true;
	            //playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
	            playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
	            playerCapabilities.isCreativeMode = true;
	            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
	            
			}else {
				hypixelDamaged = true;
			}
			
		}
		else if (mode.is("HypixelFast1") || mode.getMode() == "HypixelFast1") {
			
			onEnableHypixelFastfly1();
			
		}
		
	}
	
	public void onDisable() {
		
		hypixelDamaged = false;
		mc.thePlayer.stepHeight = 0.6f;
		
		if (mode.getMode().equals("Vanilla")) {
			mc.thePlayer.capabilities.setFlySpeed(original_fly_speed);
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.capabilities.allowFlying = false;
		} else if (mode.getMode().equals("Hypixel")) {
			
			if (MovementUtils.isOnGround(0.0001)) {
				mc.thePlayer.jump();
			}
			
			for (Packet p : hypixelPackets) {
				
				if (mc.isSingleplayer()) {
					
				}else {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
				}
				
			}
			hypixelPackets.clear();
			
			if (SpicyClient.config.blink.isEnabled()) {
				//SpicyClient.config.blink.toggle();
			}

			mc.timer.ticksPerSecond = 20f;

		}
		else if (mode.is("HypixelFast1") || mode.getMode() == "HypixelFast1") {
			
			onDisablehypixelFastFly1();
			
		}
		
	}
	
	private static float original_fly_speed;
	private static transient int viewBobbing = 0, hypixelLagback = 0;

	private transient long hypixelStartTime = System.currentTimeMillis();

	private transient Timer timer = new Timer();

	public void onEvent(Event e) {
		
		if (mode.is("HypixelFast1") || mode.getMode() == "HypixelFast1") {
			
			onEventHypixelFastfly1(e);
			
		}
		
		if (e instanceof EventPacket && ((EventPacket) e).packet instanceof S08PacketPlayerPosLook && mode.getMode().equals("Hypixel")) {
			
			if (hypixelLagback >= 1) {
				//toggle();
				//NotificationManager.getNotificationManager().createNotification(this.name + " has been disabled to prevent flags", "", true, 1000, Type.WARNING, Color.RED);
			}else {
				hypixelLagback++;
			}
			
		}
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			if (mode.is("Hypixel") || mode.getMode() == "Hypixel") {
				this.additionalInformation = mode.getMode() + " : " + (hypixelBlink.isEnabled() ? "Blink" : "Non Blink");
			}else {
				this.additionalInformation = mode.getMode();
			}
			
		}
		
		// For the viewbobbing
		if (e instanceof EventMotion && e.isPre() && (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())) {
			
			switch (viewBobbing) {
			case 0:
				mc.thePlayer.cameraYaw = 0.105F;
				mc.thePlayer.cameraPitch = 0.105F;
				viewBobbing++;
				break;
			case 1:
				viewBobbing++;
				break;
			case 2:
				viewBobbing = 0;
				break;
			}
			
		}
		
		// For the viewbobbing
		
		if (e instanceof EventSendPacket && mode.getMode().equals("Hypixel") && hypixelDamaged) {
			
			if (e.isPre()) {
				
				if (((EventSendPacket)e).packet instanceof C00PacketKeepAlive || ((EventSendPacket)e).packet instanceof C00Handshake || ((EventSendPacket)e).packet instanceof C00PacketLoginStart) {
					return;
				}
				
				EventSendPacket sendPacket = (EventSendPacket) e;
				
				if (sendPacket.packet instanceof C03PacketPlayer) {
					((C03PacketPlayer)sendPacket.packet).setIsOnGround(false);
					((C03PacketPlayer)sendPacket.packet).setMoving(false);
				}
				
				if (hypixelBlink.isEnabled() && mc.thePlayer.fallDistance < 3) {
					hypixelPackets.add(sendPacket.packet);
					sendPacket.setCanceled(true);
				}
				
			}
			
		}
		
		if (e instanceof EventUpdate && e.isPre()) {

			if (timer.hasTimeElapsed(2000 + new Random().nextInt(500), true)) {
				// SpicyClient.config.blink.toggle();
			}

		}
		
		if (mode.getMode().equals("Vanilla")) {
			try {
				mc.thePlayer.capabilities.setFlySpeed(original_fly_speed);
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
			try {
				mc.thePlayer.capabilities.isFlying = false;
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
			try {
				mc.thePlayer.capabilities.allowFlying = false;
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
		}
		
		if (mode.getMode().equals("Vanilla")) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed((float) speed.getValue());
		}
		
		if (e instanceof EventMotion) {

			EventMotion event = (EventMotion) e;

			if (e.isPost()) {
				
				//this.additionalInformation = mode.getMode();
				
				if (lastPlayerHealth > mc.thePlayer.getHealth()) {
					hypixelDamaged = true;
				}
				
				if (mode.getMode().equals("Hypixel") && hypixelDamaged) {
					
					//mc.thePlayer.capabilities.isFlying = true;
					
					mc.thePlayer.onGround = true;
					mc.thePlayer.motionY = 0.0;
					//mc.thePlayer.motionY = -0.0784000015258789;
					
					//double offset = 9.947598300641403E-14D;
					//double offset = 9.947599900641403E-14D;
					//double offset = 9.274936900641403E-14D;
					double offset1 = 0.00000000824934;
					//double offset2 = 0.002248000625918 / 5;
					// 4.496001251836E-4
					//double offset2 = 4.496001251836E-4;
					
					//double offset2 = 4.496001251836E-43;
					double offset2 = 9.274936900641403E-14D;
					//offset2 += ((float)new Random().nextInt(99999)) / 1000000000000000000000000000000000000000000000000d; 
					
					//MovementUtils.setMotion(0.2);
					//MovementUtils.strafe(0.195f);
					//MovementUtils.setMotion((float) ((Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)) / (2)) + ((float)hypixelSpeed.getValue()));
					
					if (mc.thePlayer.fallDistance >= 3) {
						MovementUtils.setMotion(hypixelBoostSpeed.getValue());
						this.additionalInformation = "MEGA SPEED BOOST!!!";
						//mc.thePlayer.motionY = -0.005;
						//mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
						hypixelLagback = 0;
					}else {
						MovementUtils.setMotion(hypixelSpeed.getValue());
						//offset2 = 4.496001251836E-5;
						//offset2 += ((float)new Random().nextInt(99999)) / 100000000000f; 
					}
					
					//int time = (int) ((System.currentTimeMillis() - hypixelStartTime) / 1000);
					
					if (hypixelTimerBoost.isEnabled()) {
						mc.timer.ticksPerSecond = 27f;
					}
					
					//Command.sendPrivateChatMessage(offset2);
					//offset1 += ((float)new Random().nextInt(99999)) / 10000000000000000f; 
					//offset2 += ((float)new Random().nextInt(99999)) / 10000000000000000f; 
					//Command.sendPrivateChatMessage(new DecimalFormat("#.####################################################").format(offset2));
					
					switch (hypixelStage) {
					case 0:
						event.setY(mc.thePlayer.posY);
						//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY,
								mc.thePlayer.posZ);
						hypixelStage++;
						break;
					case 1:
						// mc.thePlayer.posY = mc.thePlayer.posY + 9.947598300641403E-14;
						// mc.thePlayer.posY = mc.thePlayer.lastTickPosY + 0.0002000000000066393;
						//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0002000000000066393,
								//mc.thePlayer.posZ);
						if (!MovementUtils.isOnGround(0.0001)) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -offset2, mc.thePlayer.posZ);
						}
						
						//event.setY(mc.thePlayer.posY);
						hypixelStage++;
						break;
					case 2:
						event.setY(mc.thePlayer.posY);
						//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (offset2),
								mc.thePlayer.posZ);
						hypixelStage = 0;
						break;
					}
					//Command.sendPrivateChatMessage(mc.thePlayer.posY);
					DecimalFormat dec = new DecimalFormat("#.##########################################");
					
					
				}

			}
			
			if (mode.getMode().equals("Hypixel") && hypixelDamaged) {
				
				//mc.thePlayer.capabilities.isFlying = true;
				
				mc.thePlayer.onGround = true;
				mc.thePlayer.motionY = 0.0;
				
				//double offset = 9.947598300641403E-14D;
				//double offset = 9.947599900641403E-14D;
				//double offset = 9.274936900641403E-14D;
				double offset1 = 0.00000000824934;
				//double offset2 = 0.002248000625918 / 5;
				// 4.496001251836E-4
				//double offset2 = 4.496001251836E-4;
				
				double offset2 = 4.496001251836E-43;
				offset2 += ((float)new Random().nextInt(99999)) / 1000000000000000000000000000000000000000000000000d; 
				
				//MovementUtils.setMotion(0.2);
				//MovementUtils.strafe(0.195f);
				//MovementUtils.setMotion((float) ((Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)) / (2)) + ((float)hypixelSpeed.getValue()));
				
				if (mc.thePlayer.fallDistance >= 3) {
					MovementUtils.setMotion(hypixelBoostSpeed.getValue());
					this.additionalInformation = "MEGA SPEED BOOST!!!";
					mc.thePlayer.motionY = -0.005;
					//mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
					hypixelLagback = 0;
				}else {
					MovementUtils.setMotion(hypixelSpeed.getValue());
					offset2 = 4.496001251836E-4;
					//offset2 += ((float)new Random().nextInt(99999)) / 100000000000f; 
				}
				
				//int time = (int) ((System.currentTimeMillis() - hypixelStartTime) / 1000);
				
				if (hypixelTimerBoost.isEnabled()) {
					mc.timer.ticksPerSecond = 27f;
				}
				
				//Command.sendPrivateChatMessage(offset2);
				//offset1 += ((float)new Random().nextInt(99999)) / 10000000000000000f; 
				//offset2 += ((float)new Random().nextInt(99999)) / 10000000000000000f; 
				//Command.sendPrivateChatMessage(new DecimalFormat("#.####################################################").format(offset2));
				
				switch (hypixelStage) {
				case 0:
					event.setY(mc.thePlayer.posY);
					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY,
							mc.thePlayer.posZ);
					hypixelStage++;
					break;
				case 1:
					// mc.thePlayer.posY = mc.thePlayer.posY + 9.947598300641403E-14;
					// mc.thePlayer.posY = mc.thePlayer.lastTickPosY + 0.0002000000000066393;
					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0002000000000066393,
							//mc.thePlayer.posZ);
					if (!MovementUtils.isOnGround(0.0001)) {
						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -offset2, mc.thePlayer.posZ);
					}
					
					//event.setY(mc.thePlayer.posY);
					hypixelStage++;
					break;
				case 2:
					event.setY(mc.thePlayer.posY);
					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (offset2),
							mc.thePlayer.posZ);
					hypixelStage = 0;
					break;
				}
				//Command.sendPrivateChatMessage(mc.thePlayer.posY);
				DecimalFormat dec = new DecimalFormat("#.##########################################");
				
				
			}
			
		}

	}
	
	@Override
	public void onEventWhenDisabled(Event e) {
		
		if (e instanceof EventRenderGUI && e.isPre() && this.keycode.getKeycode() != Keyboard.KEY_NONE && mc.thePlayer.fallDistance >= 3 && mode.getMode().equals("Hypixel")) {
			
			ScaledResolution sr = new ScaledResolution(mc);
			
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.2, 1.2, 1.2);
			Gui.drawString(mc.fontRendererObj, "Toggle fly for a mega speed boost", ((sr.getScaledWidth()/2) - ((mc.fontRendererObj.getStringWidth("Toggle fly for a mega speed boost") / 2) * 1.2)) / 1.2, ((sr.getScaledHeight() - (sr.getScaledHeight()/3))) / 1.2, 0xffff0000);
			GlStateManager.popMatrix();
			
		}
		
	}
	
	// Found on github
    public void damage(){
    	
    	int damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
    	
    	/*
    	for (int i = 0; i < 10; i++) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }

        float fallDistance = 3.0125f; //does half a heart of damage
    	//float fallDistance = 8.0125f;

        while (fallDistance > 0) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
            
            fallDistance -= 0.0624986421f;
        }

        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        mc.thePlayer.jump();

        mc.thePlayer.posY += 0.42f;
        
    	/*
        double fallDistance = 0;
        double offset = 0.41999998688698;
        while (fallDistance < 6)
        {
            sendPacket(offset,false);
            sendPacket(0, fallDistance + offset >= 4);
            fallDistance += offset;
        }
        */
        hypixelDamaged = true;
    }
    void sendPacket(double addY,boolean ground){
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX,mc.thePlayer.posY+addY,mc.thePlayer.posZ,ground
        ));
    }
    
    
    // Hypixel fast fly code
    
	public int hypixelFastFlyStatus = 0, hypixelFastFly1 = 0;
	public double speedAndStuff = 0;
	public static transient boolean hypixelFastFly1Damaged = false;
	
	public void onEnableHypixelFastfly1() {
		
		hypixelFastFlyStatus = 0;
		hypixelFastFly1 = 0;
		speedAndStuff = 0;
		hypixelFastFly1Damaged = false;
		
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = true;
        playerCapabilities.allowFlying = true;
        //playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
        playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
        playerCapabilities.isCreativeMode = true;
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
		
		SpicyClient.config.fly.damage();
		mc.thePlayer.onGround = false;
		MovementUtils.setMotion(0);
		mc.thePlayer.jumpMovementFactor = 0;
		
		hypixelFastFly1Packets.clear();
		
	}
	
	public void onDisablehypixelFastFly1() {
		
		if (hypixelFastFly1StopOnDisable.isEnabled()) {
			
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			
		}
		
		if (hypixelFastFly1Blink.isEnabled()) {
			
			for (Packet p : hypixelFastFly1Packets) {
				
				if (mc.isSingleplayer()) {
					
				}else {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
				}
				
			}
			hypixelFastFly1Packets.clear();
			
		}
		
	}
	
    public void onEventHypixelFastfly1(Event e) {
    	
    	
		if (e instanceof EventSendPacket) {
			
			Block block = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.2, mc.thePlayer.posZ)).getBlock();
			
			if (e.isPre() && !MovementUtils.isOnGround(0.0000001) && !block.isFullBlock() && !(block instanceof BlockGlass) && hypixelFastFly1Damaged) {
				
				if (((EventSendPacket)e).packet instanceof C00PacketKeepAlive || ((EventSendPacket)e).packet instanceof C00Handshake || ((EventSendPacket)e).packet instanceof C00PacketLoginStart) {
					return;
				}
				
				EventSendPacket sendPacket = (EventSendPacket) e;
				
				if (hypixelFastFly1Blink.isEnabled()) {
					hypixelFastFly1Packets.add(sendPacket.packet);
					sendPacket.setCanceled(true);
				}
				
			}
			
		}
    	
		if (e instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) e;
            
            //double speed = Math.max(hypixelFastFly1Speed.getValue(), 0.2873D);
            double speed = hypixelFastFly1Speed.getValue();
            
            if (true) {
            	if(!em.isPre())
            		return;
            	hypixelFastFly1++;
                if (true) {
                    if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MovementUtils.isOnGround(0.01)) {
                        
                    	if(mc.thePlayer.hurtResistantTime == 19){
                    		
                    		MovementUtils.setMotion(0.3 + 0 * 0.05f);
                    		mc.thePlayer.motionY = 0.41999998688698f + 0*0.1;
                    		hypixelFastFly1 = 25;
                    		speedAndStuff = 13;
                    		hypixelFastFly1Damaged = true;
                    	}else if(hypixelFastFly1 < 25){
                    		mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                            mc.thePlayer.jumpMovementFactor = 0;
                            mc.thePlayer.onGround = false;
                    	}
                    	
                    }
                }
                Block block = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.2, mc.thePlayer.posZ)).getBlock();
                if (!MovementUtils.isOnGround(0.0000001) && !block.isFullBlock() && !(block instanceof BlockGlass)) {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    float speedf = (float) (hypixelFastFly1Speed.getValue() + 0 * 0.06f);
                    if (speedAndStuff > 0) {
                        if ((mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) || mc.thePlayer.isCollidedHorizontally)
                            speedAndStuff = 0;
                        
                        //speedf += speedAndStuff / 18;
                        speedf += speedAndStuff / hypixelFastFly1Decay.getValue();
                        
                        //dub-= 0.175 + 0*0.006; //0.152
                        
                        speedAndStuff-= 0.155 + 0*0.006;
                        
                        /*
                        if(((Options)settings.get("dubMODE").getValue()).getSelected().equalsIgnoreCase("OldFast")){
                        	dub-= 1.3;
                        }else if(((Options)settings.get("dubMODE").getValue()).getSelected().equalsIgnoreCase("Fast3")){
                        	dub-= 0.175 + 0*0.006; //0.152
                        }else{
                        	dub-= 0.155 + 0*0.006; //0.152
                        }
                        */
                        
                    }
                    
                    //setSpecialMotion(speedf);
                    
                    double forward = mc.thePlayer.movementInput.moveForward;
                    double strafe = mc.thePlayer.movementInput.moveStrafe;
                    float yaw = mc.thePlayer.rotationYaw;
                    if ((forward == 0.0D) && (strafe == 0.0D)) {
                    	mc.thePlayer.motionX = 0;
                    	mc.thePlayer.motionZ = 0;
                    } else {
                        if (forward != 0.0D) {
                        	if(speedAndStuff <= 0)
                        	 if (strafe > 0.0D) {
                                 yaw += (forward > 0.0D ? -45 : 45);
                             } else if (strafe < 0.0D) {
                                 yaw += (forward > 0.0D ? 45 : -45);
                             }
                             strafe = 0.0D;
                            if (forward > 0.0D) {
                                forward = 1;
                            } else if (forward < 0.0D) {
                                forward = -1;
                            }
                        }
                        mc.thePlayer.motionX = forward * speedf * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speedf * Math.sin(Math.toRadians(yaw + 90.0F));
                        mc.thePlayer.motionZ = forward * speedf * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speedf * Math.cos(Math.toRadians(yaw + 90.0F));
                    }
                    
                   // MovementUtils.setMotion(speedf);
                    
                    mc.thePlayer.jumpMovementFactor = 0;
                    mc.thePlayer.onGround = false;
                    if (mc.gameSettings.keyBindJump.pressed) {
                        mc.thePlayer.motionY = 0.4;
                    }
                    //status++;
                    //mc.thePlayer.lastReportedPosY = 0;
                    
                    //double offset2 = 4.496001251836E-5;
                    double offset2 = 9.274936900641403E-14D;
                    
                    switch (hypixelFastFlyStatus) {
                    
    				case 0:
    					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY,
    							mc.thePlayer.posZ);
    					hypixelFastFlyStatus++;
    					break;
    				case 1:
    					// mc.thePlayer.posY = mc.thePlayer.posY + 9.947598300641403E-14;
    					// mc.thePlayer.posY = mc.thePlayer.lastTickPosY + 0.0002000000000066393;
    					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0002000000000066393,
    							//mc.thePlayer.posZ);
    					if (!MovementUtils.isOnGround(0.0001)) {
    						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -offset2, mc.thePlayer.posZ);
    					}
    					
    					//event.setY(mc.thePlayer.posY);
    					hypixelFastFlyStatus++;
    					break;
    				case 2:;
    					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (offset2),
    							mc.thePlayer.posZ);
    					hypixelFastFlyStatus = 0;
    					break;
                    
					}
                    
                }

            }
            
		}
		
	}
    
}
