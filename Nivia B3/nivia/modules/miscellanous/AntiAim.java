package nivia.modules.miscellanous;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Logger;
import nivia.utils.TimeHelper;
import nivia.utils.Wrapper;

import java.util.Random;

public class AntiAim extends Module {
	Property<Yaw> yawMode = new Property<>(this, "Yaw", Yaw.BACKWARDS);
	Property<Pitch> pitchMode = new Property<>(this, "Pitch", Pitch.DOWN);
	TimeHelper spinTimer = new TimeHelper();
	TimeHelper emotionTimer = new TimeHelper();
	Random random = new Random();
	boolean emotion;
	float yaw;
	float pitch;
	public AntiAim() {
		super("AntiAim", 0, 0x005C00, Category.MISCELLANEOUS, "look like a retard", new String[] { "aa", "derp", "retard" }, true);
	}
	
	@EventTarget
	private void onPre(EventPreMotionUpdates event) {
		if (Wrapper.getPlayer().isSwingInProgress || Wrapper.getMinecraft().playerController.isHittingBlock) return;
		
		switch(yawMode.value) {
		case BACKWARDS:
			yaw = event.getYaw() + 180;
			event.setYaw(yaw);
			break;
		case LEFT:
			yaw = event.getYaw() + 90;
			event.setYaw(yaw);
			break;
		case RANDOM:
			if (spinTimer.isDelayComplete(200)) {
				yaw = event.getYaw() + random.nextInt(360);
				spinTimer.setLastMS();
			}
			event.setYaw(yaw);
			break;
		case RIGHT:
			yaw = event.getYaw() + 90;
			event.setYaw(yaw);
			break;
		case SLOWSPIN:
			if (spinTimer.isDelayComplete(100)) {
				yaw += 23;
				spinTimer.setLastMS();
			}
			event.setYaw(yaw);
			break;
		case SPIN:
			if (spinTimer.isDelayComplete(100)) {
				yaw += 50;
				spinTimer.setLastMS();
			}
			event.setYaw(yaw);
			break;
		case OFF:
			break;
		}
		
		switch(pitchMode.value) {
		case DOWN:
			pitch = 89;
			event.setPitch(89);
			break;
		case EMOTION:
			if (emotionTimer.isDelayComplete(20)) {
					emotion = !emotion;
					emotionTimer.setLastMS();
			}
			pitch = emotion ? 40 : -40;
			event.setPitch(pitch);
			break;
		case LISP:
			pitch = -179;
			event.setPitch(pitch);
			break;
		case OFF:
			break;
		}
		
	}
	
	public enum Yaw {
		SPIN, SLOWSPIN, RANDOM, BACKWARDS, RIGHT, LEFT, OFF;
	}
	
	public enum Pitch {
		DOWN, EMOTION, LISP, OFF;
	}
	
	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("AntiAim", "Manages AntiAim modes", Logger.LogExecutionFail("Option, Options:", new String[] { "Spin", "Slowspin", "Random", "BackWards", "Right", "left", "off", "Values" }), "derp", "retard") {
			@Override
			public void execute(String commandName, String[] arguments) {
			
				String message = arguments[1];
				switch (message.toLowerCase()) {
				case "spin":
					yawMode.value = Yaw.SPIN;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "slowspin":
					yawMode.value = Yaw.SLOWSPIN;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "random":
					yawMode.value = Yaw.RANDOM;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "backwards":
					yawMode.value = Yaw.BACKWARDS;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "right":
					yawMode.value = Yaw.RIGHT;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "left":
					yawMode.value = Yaw.LEFT;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
					break;
				case "off":
					yawMode.value = Yaw.OFF;
					Logger.logSetMessage("Nofall", "Mode", yawMode);
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
