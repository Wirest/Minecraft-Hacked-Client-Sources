package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.PreMotionEvent;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class Flight extends Module {

	int wait = 6;
	double MACvelY = 0.02;
	double startingHeight;
	double fallSpeed = 0.05;
	double maxY;
	boolean damaging = false;
	private WaitTimer timer = new WaitTimer();
	public double flyHeight;
	private boolean viewBobbing;
	private boolean flyBoost;
	private boolean flyBoostEnabled = false;
	private boolean aac;
	private double aad;
	boolean Up = false;
	boolean Start = false;
	private WaitTimer cubeTimer = new WaitTimer();
	private boolean cubecraftEnabled = false;
	public static boolean cubecraftOverrideTpaurathingidkwhattonameit = false;

	public Flight() {
		super("Flight", Keyboard.KEY_F, Category.MOVEMENT, "Allows you to fly.");
	}

	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		if(ClientSettings.BlinkFly) {
			Xatz.getModuleByName("Blink").onDisable();;
		}
		super.onDisable();
		mc.timer.timerSpeed = 1f;
		//if(ClientSettings.flyBoost == false && this.flyBoostEnabled == true) {
			//ClientSettings.flyBoost = true;
			//this.flyBoostEnabled = false;
		//}
	}

	public void onPreMotion(PreMotionEvent event) {
		if (this.currentMode.equals("AAC") || currentMode.equals("AAC2")) {
			mc.thePlayer.setSprinting(false);
			if ((mc.thePlayer.fallDistance >= 4.0F) && (!this.aac)) {
				this.aac = true;
				this.aad = (mc.thePlayer.posY + 3.0D);
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
			mc.thePlayer.capabilities.isFlying = false;
			if (this.aac) {
				if (mc.thePlayer.onGround) {
					this.aac = false;
				}
				if (mc.thePlayer.posY < this.aad) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, true));
					if (mc.gameSettings.keyBindSneak.pressed) {
						this.aad -= 2.0D;
					} else if ((mc.gameSettings.keyBindSneak.pressed) && (mc.thePlayer.posY < this.aad + 0.8D)) {
						this.aad += 2.0D;
					} else {
						mc.thePlayer.motionY = 0.7D;
						if(currentMode.equals("AAC2")) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
									mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true));
						}
						gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(0.8f);
					}
				}
				else {
					if(currentMode.equals("AAC2")) {
						if(mc.thePlayer.motionY <= 0) {
							event.y = 0.01;
						}
					}
				}
			} else {
				mc.thePlayer.capabilities.isFlying = false;
			}
		}
		if (this.currentMode.equals("AAC3") && Start) {
			if (!Up) {
				//event.y = 0.01;
				mc.thePlayer.motionY = 1;
				Up = true;
			}
			else {
				event.y = -0.05;
				event.x *= 3;
				event.z *= 3;
				Up = false;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
		}
		if(currentMode.equals("Cubecraft")) {
			if(!cubecraftEnabled) {
				return;
			}
			mc.thePlayer.motionY = 0;
			event.y = -0.003;
			if(ClientSettings.flightCubecraftKillAnticheat) {
				event.y = -0.0784000015258789;
			}
			if(mc.gameSettings.keyBindJump.pressed) {
				event.y = 0.8;
			}
			if(mc.gameSettings.keyBindSneak.pressed) {
				event.y = -0.8;
			}
			double moveSpeed = Math.min(ClientSettings.FlightdefaultSpeed, 0.2873D * Math.pow(((double)(toggleTime) / 10.0), 2.5) + 0.2873D);
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				event.x = 0.0D;
				event.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			event.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
			event.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
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

	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Flight Speed", ClientSettings.FlightdefaultSpeed, 0, 10, 0,
//				ValueDisplay.DECIMAL);
//		SliderListener listener1 = new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.FlightdefaultSpeed = slider.getValue();
//			}
//		};
//		slider1.addSliderListener(listener1);
//		final BasicCheckButton box1 = new BasicCheckButton("Default Smooth Flight");
//		box1.setSelected(ClientSettings.Flightsmooth);
//		ButtonListener listener2 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.Flightsmooth = box1.isSelected();
//			}
//		};
//		box1.addButtonListener(listener2);
//		final BasicCheckButton box2 = new BasicCheckButton("Flight Kick Bypass");
//		box2.setSelected(ClientSettings.flightkick);
//		ButtonListener listener3 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.flightkick = box2.isSelected();
//			}
//		};
//		box2.addButtonListener(listener3);
//		
//		final BasicCheckButton box3 = new BasicCheckButton("Glide Damage");
//		box3.setSelected(ClientSettings.glideDmg);
//		ButtonListener listener4 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.glideDmg = box3.isSelected();
//			}
//		};
//		box3.addButtonListener(listener4);
//		
//		final BasicCheckButton box4 = new BasicCheckButton("onGround Spoof");
//		box4.setSelected(ClientSettings.onGroundSpoofFlight);
//		ButtonListener listener5 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.onGroundSpoofFlight = box4.isSelected();
//			}
//		};
//		box3.addButtonListener(listener5);
		SliderSetting<Number> airAgilitySpeedSlider = new SliderSetting<Number>("Fly Speed", ClientSettings.VanillaspeedFactor, 0.1, 9, 0.0, ValueFormat.DECIMAL);
		CheckBtnSetting box2 = new CheckBtnSetting("Smooth Flight", "Flightsmooth");
		CheckBtnSetting box1 = new CheckBtnSetting("Vanilla Kick Bypass", "flightkick");
		CheckBtnSetting box3 = new CheckBtnSetting("Glide Mode - Damage", "glideDmg");
		CheckBtnSetting box4 = new CheckBtnSetting("onGround Spoof", "onGroundSpoofFlight");
		CheckBtnSetting box5 = new CheckBtnSetting("Cubecraft Less Damage", "flightCubecraftKillAnticheat");
		CheckBtnSetting box6 = new CheckBtnSetting("View Bobbing", "viewBobbing");
		CheckBtnSetting box8 = new CheckBtnSetting("Blink Fly", "BlinkFly");
	    CheckBtnSetting box7 = new CheckBtnSetting("Timer Boost", "flyBoost");
		return new ModSetting[] { airAgilitySpeedSlider, box2, box1, box3, box4, box5, box6, box7, box8 };
	}

	@Override
	public void onEnable() {
		//if(ClientSettings.flyBoost == true) {
		//	Xatz.sendChatMessage(".damage");
		//}
		
		cubecraftEnabled = false;
		timer.reset();
		if (currentMode.equals("AirWalk")) {
			maxY = mc.thePlayer.posY + 0.01d;
			damaging = true;
			// Xatz.sendChatMessage(".damage");
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01d, mc.thePlayer.posZ);
		}
		if (currentMode.equals("Glide")) {
			this.startingHeight = mc.thePlayer.posY + 1000;
			if(ClientSettings.glideDmg) {
				Xatz.sendChatMessage(".damage");
			}
		}
		if(currentMode.equals("Cubecraft")) {
			cubeTimer.reset();
			
		}
		if(currentMode.equals("Redesky")) {
			mc.thePlayer.jump();
			if(!ClientSettings.BlinkFly) {
				Xatz.getNotificationManager().addNotification(new Notification(Level.WARNING, 
						"Please enable blinkfly from the settings"));
				this.toggle();
				}
			
		}
		if (currentMode.equalsIgnoreCase("MAC")) {
			wait = 6;
			mc.thePlayer.motionY = 0.25;
		}
		if(currentMode.equals("AAC3")) {
			Up = true;
			Start = false;
		}
		if(ClientSettings.BlinkFly) {
			Xatz.getModuleByName("Blink").onEnable();
		}
		super.onEnable();
	}
	
	
	public void updateFlyHeight() {
		double h = 1;
		AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
		for (flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);

			if (mc.theWorld.checkBlockCollision(nextBox)) {
				if (h < 0.0625)
					break;

				flyHeight -= h;
				h /= 2;
			}
		}
	}

	public void goToGround() {
		if (flyHeight > 320)
			return;

		double minY = mc.thePlayer.posY - flyHeight;

		if (minY <= 0)
			return;

		for (double y = mc.thePlayer.posY; y > minY;) {
			y -= 9.9;
			if (y < minY)
				y = minY;

			C04PacketPlayerPosition packet = new C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		for (double y = minY; y < mc.thePlayer.posY;) {
			y += 9.9;
			if (y > mc.thePlayer.posY)
				y = mc.thePlayer.posY;

			C04PacketPlayerPosition packet = new C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);	
		
		if(ClientSettings.viewBobbing == true) {
			mc.thePlayer.cameraYaw = 0.105f;
		}
		
		/*if(ClientSettings.flyBoost == true && mc.thePlayer.onGround == true) {

			mc.timer.timerSpeed = 1.5f;
			if(this.timer.getTime() == 400 || mc.thePlayer.isCollidedHorizontally) {
				mc.timer.timerSpeed = 1f;
				ClientSettings.flyBoost = false;
				this.flyBoostEnabled = true;
			}
		} else {
			if(ClientSettings.flyBoost == true) {
				if(this.timer.getTime() == 400) {
					ClientSettings.flyBoost = false;
					this.flyBoostEnabled = true;
				}
			}
		}*/
		
		if(currentMode.equals("AAC3")) {
			event.onGround = true;
			Start = true;
		}
		if (currentMode.equalsIgnoreCase("Default")) {
			if (!ClientSettings.Flightsmooth) {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionY = 0;
			if (ClientSettings.Flightsmooth) {
				mc.thePlayer.jumpMovementFactor = (float) (ClientSettings.VanillaspeedFactor / 10);
			} else {
				mc.thePlayer.jumpMovementFactor = (float) (ClientSettings.VanillaspeedFactor);
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY += ClientSettings.VanillaspeedFactor / 2;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY += -ClientSettings.VanillaspeedFactor / 2;
			}
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
			//Double sspeed = HaxxerClient.instance.settingsManager.getSettingByName("Speed").getValDouble();
        	//float ssspeed = sspeed.floatValue();
			/*float sssspeed = (float)ClientSettings.VanillaspeedFactor;
        	if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
        	mc.thePlayer.setSpeed((float)sssspeed * 0.191111f);
        	//mc.thePlayer.cameraYaw = 0.105f;
        	} else {
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        	}
        	/*if(!mc.gameSettings.keyBindForward.isKeyDown() || !mc.gameSettings.keyBindBack.isKeyDown() || !mc.gameSettings.keyBindLeft.isKeyDown() || !mc.gameSettings.keyBindRight.isKeyDown()) {
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        	}*/
        	/*mc.thePlayer.motionY = 0;
        	if(mc.gameSettings.keyBindJump.isKeyDown()) {
        		mc.thePlayer.motionY = sssspeed * 0.6 * 0.191111; 
        	}
        	if(mc.gameSettings.keyBindSneak.isKeyDown()) {
        		mc.thePlayer.motionY = -sssspeed * 0.6 * 0.191111;
        	}*/
        	//mc.thePlayer.onGround = true;
        	/*mc.thePlayer.motionY = 0;
        	if(mc.gameSettings.keyBindJump.isKeyDown()) {
        		mc.thePlayer.motionY = 0.1;
        	}
        	if(mc.gameSettings.keyBindSneak.isKeyDown()) {
        		mc.thePlayer.motionY = -0.1;
        	}
        	if(mc.gameSettings.keyBindForward.isKeyDown()) {
        		mc.thePlayer.movementInput.moveForward = ssspeed;
        }*/
        	
		}
		if (currentMode.equalsIgnoreCase("MAC")) {
			if (wait < 6) {
				wait++;
				if (mc.thePlayer.motionY < 0// If falling
						&& !mc.thePlayer.onGround) {
					mc.thePlayer.motionY = -MACvelY;
				}
				return;
			}
			if (mc.thePlayer.motionY < 0// If falling
					&& !mc.thePlayer.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ);
					wait = 3;
					return;
				} else if (mc.gameSettings.keyBindSneak.pressed) {
					mc.thePlayer.motionY = -0.4;
					return;
				}
				mc.thePlayer.motionY = -MACvelY;

			}
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equalsIgnoreCase("Creative")) {
			mc.thePlayer.capabilities.isFlying = true;
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equalsIgnoreCase("Redesky")) {
			if(ClientSettings.viewBobbing == true) {
				mc.thePlayer.cameraYaw = 0.105f;
			}
			mc.thePlayer.capabilities.isFlying = true;
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equalsIgnoreCase("FastJetpack")) {
			mc.thePlayer.jump();
		}
		if (currentMode.equals("Glide")) {
			// if (mc.thePlayer.onGround) {
			// this.startingHeight = mc.thePlayer.posY;
			// }
			if (!mc.thePlayer.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.posY + 0.5 < startingHeight) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ);
				} else if (mc.gameSettings.keyBindSneak.pressed) {
					mc.thePlayer.motionY = -0.2;
					return;
				}
				mc.thePlayer.motionY = -fallSpeed;

			}
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if(currentMode.equals("Motion")) {
			//Double sspeed = HaxxerClient.instance.settingsManager.getSettingByName("Speed").getValDouble();
        	//float ssspeed = sspeed.floatValue();
			double sssspeed = ClientSettings.VanillaspeedFactor;
        	if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
        	mc.thePlayer.setSpeed((float)sssspeed * 0.191111f);
        	//mc.thePlayer.cameraYaw = 0.105f;
        	} else {
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        	}
        	/*if(!mc.gameSettings.keyBindForward.isKeyDown() || !mc.gameSettings.keyBindBack.isKeyDown() || !mc.gameSettings.keyBindLeft.isKeyDown() || !mc.gameSettings.keyBindRight.isKeyDown()) {
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        	}*/
        	mc.thePlayer.motionY = 0;
        	if(mc.gameSettings.keyBindJump.isKeyDown()) {
        		mc.thePlayer.motionY = sssspeed * 0.6 * 0.191111; 
        	}
        	if(mc.gameSettings.keyBindSneak.isKeyDown()) {
        		mc.thePlayer.motionY = -sssspeed * 0.6 * 0.191111;
        	}
        	//mc.thePlayer.onGround = true;
        	/*mc.thePlayer.motionY = 0;
        	if(mc.gameSettings.keyBindJump.isKeyDown()) {
        		mc.thePlayer.motionY = 0.1;
        	}
        	if(mc.gameSettings.keyBindSneak.isKeyDown()) {
        		mc.thePlayer.motionY = -0.1;
        	}
        	if(mc.gameSettings.keyBindForward.isKeyDown()) {
        		mc.thePlayer.movementInput.moveForward = ssspeed;
        }*/
        	
		}
		if (currentMode.equals("AirWalk")) {
			if (mc.thePlayer.posY <= maxY) {
				mc.thePlayer.motionY = 0;
			}
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if(ClientSettings.onGroundSpoofFlight) {
			event.onGround = true;
		}
		if(currentMode.equals("Cubecraft")) {
			event.onGround = true;
			if(!cubecraftEnabled) {
				if(mc.thePlayer.fallDistance > 1.2) {
					cubecraftEnabled = true;
					Xatz.sendChatMessage(".damage");
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
				}
			}
			else {
				if(mc.thePlayer.onGround) {
					cubecraftEnabled = false;
				}
				if(Xatz.getModuleByName("TpAura").isToggled() || cubecraftOverrideTpaurathingidkwhattonameit) {
					if(cubeTimer.hasTimeElapsed(2000, true)) {
						if(!ClientSettings.flightCubecraftKillAnticheat) {
							Xatz.sendChatMessage(".damage");
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.4, mc.thePlayer.posZ);
						}
						cubecraftOverrideTpaurathingidkwhattonameit = false;
					}
				}
				else {
					if(cubeTimer.hasTimeElapsed(4000, true)) {
						if(!ClientSettings.flightCubecraftKillAnticheat) {
							Xatz.sendChatMessage(".damage");
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.4, mc.thePlayer.posZ);
						}
					}
				}
			}
		}
	}

	@Override
	public void onBasicUpdates() {
		if (damaging) {
			damaging = false;
		}

		if (currentMode.equals("AirWalk")) {
			if (mc.thePlayer.posY <= maxY) {
				mc.thePlayer.onGround = true;
				// mc.thePlayer.motionY = 0;
			} else {
				mc.thePlayer.onGround = false;
			}
		}
		super.onBasicUpdates();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Default", "Creative", "Glide", "AirWalk", "AAC", "AAC2", "AAC3", "Motion", "FastJetpack", "Redesky" };
	}
	public String getModeName() {
		return "Mode: ";
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}

}