package nivia.modules.movement;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.commands.commands.Damage;
import nivia.events.EventTarget;
import nivia.events.events.EventMove;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Logger;

public class Fly extends Module {
	public Fly() {
		super("Fly", 0, 0x75FF47, Category.MOVEMENT, "Fly bird",
				new String[] { "f", "fly" }, true);
	}

	public Property<Boolean> damage = new Property<Boolean>(this, "Damage", true);
	public DoubleProperty speed = new DoubleProperty(this, "Speed", 1D, 0, 10, 1);
	public Property<Boolean> tight = new Property<Boolean>(this, "Tight", true);
	private double maxPosY = 0;


	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {
		if(tight.value) return;
		mc.thePlayer.capabilities.isFlying = true;
		mc.thePlayer.capabilities.setFlySpeed((float) speed.getValue() * 0.1F);
	}	

	@EventTarget
	public void onMove(EventMove event) {
		if (Pandora.getModManager().getModState("Speed") || !tight.value)
			return;
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		if (mc.thePlayer.isSneaking())
			event.y = mc.thePlayer.motionY = -0.4;
		else if (mc.gameSettings.keyBindJump.getIsKeyPressed())
			event.y = mc.thePlayer.motionY = 0.4; 
		else
			event.y = mc.thePlayer.motionY = 0;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			event.x = (0.0);
			if (mc.thePlayer.isSneaking())
				event.y = mc.thePlayer.motionY = -0.4;
			else if (mc.gameSettings.keyBindJump.getIsKeyPressed())
				event.y = mc.thePlayer.motionY = 0.4; 
			else
				event.y = mc.thePlayer.motionY = 0;
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
			
			event.x = (forward * speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0f))
					+ strafe * speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0f)));
			event.z = (forward * speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0f))
					- strafe * speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0f)));
		}
	}
	@Override
	public void onEnable() {
		super.onEnable();
		maxPosY = mc.thePlayer.posY;
		if (damage.value)
			Pandora.getCommandManager().getCommand(Damage.class).execute("Damage", new String[] { "", "0" });
	}
	@Override
	public void onDisable() {
		super.onDisable();
		mc.thePlayer.capabilities.isFlying = false;
	}
	protected void addCommand() {
		Pandora.getCommandManager().cmds
				.add(new Command("Fly", "Manages fly", Logger.LogExecutionFail("Option, Options:",
						new String[] { "Tight", "Speed" }), "flight") {
					@Override
					public void execute(String commandName, String[] arguments) {
		                String message = arguments[1];
		                switch (message.toLowerCase()) {
		                    case "speed":
		                    case "s":
		                        try {
		                            String message2 = arguments[2];
		                            Double d = Double.parseDouble(message2);
		                            speed.setValue(d);
		                            Logger.logSetMessage("Fly", "Speed", speed);
		                        } catch (Exception e) {
		                            Logger.LogExecutionFail("Value!");
		                        }
		                        break;
		                    case "damage":
							case "dmg":
								damage.value = !damage.value;
								Logger.logSetMessage("Glide", "Damage", damage);
								break;
		                    case "tight":
		                    case "t":
		                    tight.value = !tight.value;
							Logger.logSetMessage("Fly", "Tight", tight);
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
