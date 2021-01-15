package nivia.modules.movement;

import com.stringer.annotations.HideAccess;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.commands.commands.Damage;
import nivia.events.EventTarget;
import nivia.events.events.EventMove;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

@HideAccess
public class Glide extends Module {
	public Glide() {
		super("Glide", 0, 0x75FF47, Category.MOVEMENT, "Glide down the specified speed.",
				new String[] { "glide", "gl" }, true);
	}

	public DoubleProperty glideSpeed = new DoubleProperty(this, "Speed", 0.03145, 0, 10, 1);
	public DoubleProperty verticalSpeed = new DoubleProperty(this, "Vertical Speed", 0.4, 0, 10, 1);
	public DoubleProperty horizontalSpeed = new DoubleProperty(this, "Horizontal Speed", 0.8D, 0, 10, 1);
	public Property<Boolean> lock = new Property<Boolean>(this, "Lock", true);
	public Property<Boolean> damage = new Property<Boolean>(this, "Damage", true);
	public Property<Boolean> lemon = new Property<Boolean>(this, "Lemon", true);
	private double maxPosY = 0;

	@Override
	public void onEnable() {
		super.onEnable();
		maxPosY = mc.thePlayer.posY;
		if (damage.value)
			Pandora.getCommandManager().getCommand(Damage.class).execute("Damage", new String[] { "", "0" });
	}
	
	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {
		if (!mc.thePlayer.isEntityAlive())
			return;
		boolean shouldBlock = (!((mc.thePlayer.posY + 0.1) < maxPosY) && mc.gameSettings.keyBindJump.getIsKeyPressed())
				&& lock.value;
		boolean isGliding = !mc.thePlayer.onGround && !mc.thePlayer.isCollidedVertically;
		if (mc.thePlayer.isSneaking())
			mc.thePlayer.motionY = -verticalSpeed.getValue();
		else if (mc.gameSettings.keyBindJump.getIsKeyPressed() && !shouldBlock)
			mc.thePlayer.motionY = verticalSpeed.getValue();
		else {
			double speed = glideSpeed.getValue();
			double x = 0;
			x++;
			if (Helper.blockUtils().isInsideBlock())
				speed = 0;
	
			mc.thePlayer.motionY = -speed;
			if(lemon.value){
				if(mc.thePlayer.ticksExisted % 3 == 0) {
					pre.setY(mc.thePlayer.posY - 0.000000001);
					pre.setGround(true);
				} else {
					pre.setY(mc.thePlayer.posY);
					pre.setGround(false);
				}
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.000000001, mc.thePlayer.posZ);
			}
		}
	
	}

	@EventTarget
	public void onMove(EventMove event) {
		if (Pandora.getModManager().getModState("Speed"))
			return;
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			event.x = (0.0);
			event.z = (0.0);
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0)
					yaw += ((forward > 0.0) ? -45 : 45);
				else if (strafe < 0.0)
					yaw += ((forward > 0.0) ? 45 : -45);

				strafe = 0.0;

				if (forward > 0.0)
					forward = 1.0;
				else if (forward < 0.0)
					forward = -1.0;
			}
			event.x = (forward * horizontalSpeed.getValue() * Math.cos(Math.toRadians(yaw + 90.0f))
					+ strafe * horizontalSpeed.getValue() * Math.sin(Math.toRadians(yaw + 90.0f)));
			event.z = (forward * horizontalSpeed.getValue() * Math.sin(Math.toRadians(yaw + 90.0f))
					- strafe * horizontalSpeed.getValue() * Math.cos(Math.toRadians(yaw + 90.0f)));
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds
				.add(new Command("Glide", "Manages glide", Logger.LogExecutionFail("Option, Options:",
						new String[] { "Vertical Speed", "Glide Speed", "Lock", "Values" }), "gl") {
					@Override
					public void execute(String commandName, String[] arguments) {
						String message = arguments[1], message2 = "";
						try {
							message2 = arguments[2];
						} catch (Exception e) {
						}
						switch (message.toLowerCase()) {
						case "lock":
						case "l":
							lock.value = !lock.value;
							if (lock.value)
								maxPosY = mc.thePlayer.posY;
							Logger.logSetMessage("Glide", "Lock", lock);
							break;
						case "damage":
						case "dmg":
							damage.value = !damage.value;
							Logger.logSetMessage("Glide", "Damage", damage);
							break;
						case "vspeed":
						case "verticalspeed":
						case "vs":
							switch (message2) {
							case "actual":
								logValue(verticalSpeed);
								break;
							case "reset":
								verticalSpeed.reset();
								break;
							default:
								verticalSpeed.setValue(Double.parseDouble(message2));
								Logger.logSetMessage("Glide", "Vertical Speed", verticalSpeed);
								break;
							}
							break;
						case "hspeed":
						case "horizontalspeed":
						case "hs":
							switch (message2) {
							case "actual":
								logValue(horizontalSpeed);
								break;
							case "reset":
								horizontalSpeed.reset();
								break;
							default:
								horizontalSpeed.setValue(Double.parseDouble(message2));
								;
								Logger.logSetMessage("Glide", "Horizontal Speed", horizontalSpeed);
								break;
							}
							break;
						case "gspeed":
						case "gs":
						case "glidespeed":
							switch (message2) {
							case "actual":
								logValue(glideSpeed);
								break;
							case "reset":
								glideSpeed.reset();
								break;
							default:
								glideSpeed.setValue(Double.parseDouble(message2));
								Logger.logSetMessage("Glide", "Glide Speed", glideSpeed);
								break;
							}
							break;
						case "values":
						case "actual":
							logValues();
							break;
						default:
							Logger.logChat(this.getError());
							break;
						}
					}
				});
	}
}
