package nivia.modules.ghost;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.modules.combat.AutoPot;
import nivia.utils.Helper;
import nivia.utils.Logger;

public class SmoothAim extends Module {

	public DoubleProperty horizontalspeed = new DoubleProperty(this, "HorizontalSpeed", 2, 0, 50);
	public DoubleProperty verticalspeed = new DoubleProperty(this, "VerticalSpeed", 0, 0, 50);
	public DoubleProperty random = new DoubleProperty(this, "Random", 0, 0, 5);
	public DoubleProperty fov = new DoubleProperty(this, "FOV", 90, 1, 360);
	public DoubleProperty range = new DoubleProperty(this, "Range", 3.8, 0, 10, 1);


	public SmoothAim() {
		super("SmoothAim", 0, 0xE6B800, Category.GHOST, "Aims for you smoothly!",
				new String[] { "smooth", "aim", "aimasssist", "saim" }, true);
	}

	private boolean isValidTarget(Entity target) {
		if (!target.isDead && target.isEntityAlive() && this.mc.thePlayer.getDistanceToEntity(target) <= (this.range.getValue()) && target != this.mc.thePlayer) {
			if (!FriendManager.isFriend(target.getName()))
				return true;
			else
				return false;
		}
		return false;
	}

	@EventTarget(Priority.HIGHEST)
	public void onEvent(EventPreMotionUpdates event) {
		if (!AutoPot.doPot && !mc.thePlayer.isDead) {
			Entity target = Helper.entityUtils().findClosestToCross(range.getValue());

				if (isValidTarget(target) && mc.inGameHasFocus) {
					if (Helper.entityUtils().isWithingFOV(target, (float) fov.getValue()) && mc.thePlayer.canEntityBeSeen(target) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword|| mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe && mc.inGameHasFocus) {
						float[] face = Helper.combatUtils().faceTarget(target, (int) horizontalspeed.getValue(), (int) verticalspeed.getValue(), false);
						
						mc.thePlayer.rotationYaw = face[0];
						mc.thePlayer.rotationPitch = face[1];
				}
			}
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("SmoothAim", "Manages Smoothaim stuff.", Logger.LogExecutionFail("Option, Options:", new String[] { "FOV", "Horizontal Speed", "Range", "Vertical Speed", "Random", "Values" }), "smooth", "aim", "aimasssist", "saim") {
					@Override
					public void execute(String commandName, String[] arguments) {
						String message = arguments[1];
						switch (message.toLowerCase()) {
						case "hspeed":
						case "hsp":
						case "hs":
							try {
								String message2 = arguments[2];
								switch (message2) {
								case "actual":
								case "value":
									logValue(horizontalspeed);
									break;
								case "reset":
									horizontalspeed.reset();
									Logger.logSetMessage("SmoothAim", "HorizontalSpeed", horizontalspeed);
									break;
								default:
									int hSP = Integer.parseInt(message2);
									horizontalspeed.setValue(hSP);
									Logger.logSetMessage("SmoothAim", "HorizontalSpeed", horizontalspeed);
									break;
								}

								break;
							} catch (Exception e) {
							}
						case "range":
						case "rng":
						case "reach":
							try {
								String message2 = arguments[2];
								Double RNG = Double.parseDouble(message2);
								range.setValue(RNG);
								Logger.logSetMessage("SmoothAim", "Range", range);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;

						case "vspeed":
						case "vsp":
						case "vs":
							try {
								String message2 = arguments[2];
								Integer vSP = Integer.parseInt(message2);
								verticalspeed.setValue(vSP);
								Logger.logSetMessage("SmoothAim", "VerticalSpeed", verticalspeed);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;

						case "random":
						case "rand":
						case "randomization":
							try {
								String message2 = arguments[2];
								Integer RND = Integer.parseInt(message2);
								random.setValue(RND);
								Logger.logSetMessage("SmoothAim", "Random", random);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						case "angle":
						case "fov":
						case "f":
							String message21 = arguments[2];
							switch (message21) {
							case "actual":
							case "value":
								logValue(fov);
								break;
							case "reset":
								fov.reset();
								Logger.logSetMessage("SmoothAim", "FOV", fov);
								break;
							default:
								Integer nD = Integer.parseInt(message21);
								fov.setValue(nD);
								Logger.logSetMessage("SmoothAim", "FOV", fov);
								break;
							}

							break;
						case "values":
						case "actual":
							logValues();
							break;
						default:
							Logger.logChat(getError());
							break;
						}
					}
				});
	}
}