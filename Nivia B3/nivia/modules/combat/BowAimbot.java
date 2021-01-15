package nivia.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

import java.util.ArrayList;

public class BowAimbot extends Module {
	public DoubleProperty reach = new DoubleProperty(this, "Reach", 32, 0, 256);
	public Property<Boolean> lockView = new Property<Boolean>(this, "LockView", true);
	public Property<Boolean> monsters = new Property<Boolean>(this, "Monsters", false);
	public Property<Boolean> players = new Property<Boolean>(this, "Players", true);
	public Property<Boolean> animals = new Property<Boolean>(this, "Animals", false);
	public Property<Boolean> invisibles = new Property<Boolean>(this, "Invisibles", false);
	private Property<TargetingMode> tMode = new Property<TargetingMode>(this, "Target", TargetingMode.Health);
	public static ArrayList<Entity> attackList = new ArrayList<>();
	public static ArrayList<Entity> targets = new ArrayList<>();
	public static int currentTarget;

	public BowAimbot() {
		super("BowAimbot", Keyboard.KEY_NONE, 0xA37547, Category.COMBAT, "Aims bows for you!", new String[] { "baim", "bow", "baimbot" }, true);
	}

	private enum TargetingMode {
		Distance, Health;
	}

	public boolean isValidTarget(final Entity entity) {
		boolean valid = false;
		if (entity == mc.thePlayer.ridingEntity)
			return false;

		if (entity.isInvisible())
			valid = true;

		if (FriendManager.isFriend(entity.getName()) && entity instanceof EntityPlayer || !mc.thePlayer.canEntityBeSeen(entity))
			return false;

		if (entity instanceof EntityPlayer && players.value) {
			valid = (entity != null && mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue() && entity != mc.thePlayer && entity.isEntityAlive() && !FriendManager.isFriend(entity.getName()));
			if (entity.isInvisible() && !invisibles.value)
				valid = false;
		} else if (entity instanceof IMob && monsters.value) {
			valid = (entity != null && mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue() && entity.isEntityAlive());
		} else if (entity instanceof IAnimals && !(entity instanceof IMob) && animals.value) {
			valid = (entity != null && mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue() && entity.isEntityAlive());
		}
		return valid;
	}

	@EventTarget(Priority.HIGHEST)
	public void onPre(EventPreMotionUpdates pre) {
		for (Object o : Helper.mc().theWorld.loadedEntityList) {
			Entity e = (Entity) o;
			if ((animals.value) && ((e instanceof EntityAnimal)) && (!targets.contains(e))) {
				targets.add(e);
			}
			if ((players.value) && ((e instanceof EntityPlayer)) && (!targets.contains(e))) {
				targets.add(e);
			}
			if ((monsters.value) && ((e instanceof EntityMob)) && (!targets.contains(e))) {
				targets.add(e);
			}
			if ((!animals.value) && (targets.contains(e)) && ((e instanceof EntityAnimal))) {
				targets.remove(e);
			}
			if ((!players.value) && (targets.contains(e)) && ((e instanceof EntityPlayer))) {
				targets.remove(e);
			}
			if ((!monsters.value) && (targets.contains(e)) && ((e instanceof EntityMob))) {
				targets.remove(e);
			}
		}
		if (currentTarget >= attackList.size())
			currentTarget = 0;

		for (Object o : mc.theWorld.loadedEntityList) {
			Entity e = (Entity) o;
			if ((isValidTarget(e)) && (!attackList.contains(e)))
				attackList.add(e);
		}

		for (Object o : attackList) {
			Entity e = (Entity) o;
			if (!isValidTarget(e) && attackList.contains(e))
				attackList.remove(e);
		}

		sortTargets();
		if (mc.thePlayer != null && attackList.get(currentTarget) != null && isValidTarget(attackList.get(currentTarget)) && mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
			int bowCurrentCharge = mc.thePlayer.getItemInUseDuration();
			float bowVelocity = (bowCurrentCharge / 20.0f);
			bowVelocity = (bowVelocity * bowVelocity + bowVelocity * 2.0f) / 3.0f;
			bowVelocity = MathHelper.clamp_float(bowVelocity, 0.0F, 1.0F);

			double v = bowVelocity * 3.0F;
			double g = 0.05000000074505806D;

			if (bowVelocity < 0.1)
				return;
		
			if (bowVelocity > 1.0f)
				bowVelocity = 1.0f;

			final double xDistance = attackList.get(currentTarget).posX - mc.thePlayer.posX + (attackList.get(currentTarget).posX - attackList.get(currentTarget).lastTickPosX) * ((float) (bowVelocity) * 10.0f);
			final double zDistance = attackList.get(currentTarget).posZ - mc.thePlayer.posZ + (attackList.get(currentTarget).posZ - attackList.get(currentTarget).lastTickPosZ) * ((float) (bowVelocity) * 10.0f);
			final double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
			final float trajectoryTheta90 = (float) (Math.atan2(zDistance, xDistance) * 180.0 / 3.141592653589793) - 90.0f;
			final float bowTrajectory = (float) -Math.toDegrees(getLaunchAngle((EntityLivingBase) attackList.get(currentTarget), v, g)) - 3;

			if (!AutoPot.doPot) {
				if (trajectoryTheta90 <= 360 && bowTrajectory <= 360) {
					
					if(lockView.value){
						mc.thePlayer.rotationYaw = trajectoryTheta90;
						mc.thePlayer.rotationPitch = bowTrajectory;
					} else {
						pre.setYaw(trajectoryTheta90);
						pre.setPitch(bowTrajectory);
					}
				}
			}
		}
	}

	public void sortTargets() {
		switch (tMode.value) {
		case Distance:
			attackList.sort((ent1, ent2) -> {
				double d1 = Helper.player().getDistanceToEntity(ent1);
				double d2 = Helper.player().getDistanceToEntity(ent2);
				return (d1 < d2) ? -1 : (d1 == d2) ? 0 : 1;
			});
			;
			break;
		case Health:
			attackList.sort((ent1, ent2) -> {
				double h1 = ((EntityLivingBase) ent1).getHealth();
				double h2 = ((EntityLivingBase) ent2).getHealth();
				return (h1 < h2) ? -1 : (h1 == h2) ? 0 : 1;
			});
			;
			break;
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		targets.clear();
		attackList.clear();
		currentTarget = 0;
	}

	private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
		double yDif = targetEntity.posY + targetEntity.getEyeHeight() / 2.0F - (Helper.player().posY + Helper.player().getEyeHeight());
		double xDif = targetEntity.posX - Helper.player().posX;
		double zDif = targetEntity.posZ - Helper.player().posZ;

		double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);

		return theta(v + 2, g, xCoord, yDif);
	}

	private float theta(double v, double g, double x, double y) {
		double yv = 2.0D * y * (v * v);
		double gx = g * (x * x);
		double g2 = g * (gx + yv);
		double insqrt = v * v * v * v - g2;
		double sqrt = Math.sqrt(insqrt);

		double numerator = v * v + sqrt;
		double numerator2 = v * v - sqrt;

		double atan1 = Math.atan2(numerator, g * x);
		double atan2 = Math.atan2(numerator2, g * x);

		return (float) Math.min(atan1, atan2);
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("BowAimbot", "Manages BowAimbot", Logger.LogExecutionFail("Option, Options:", new String[] { "players", "monsters", "animals", "targeting", "invisible" }), "baim", "bow", "baimbot") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1], message2 = "";
				try {
					message2 = arguments[2];
				} catch (Exception e) {
				}
				switch (message) {
				case "reach":
				case "range":
				case "Reach":
				case "Range":
					switch (message2) {
					case "actual":
						logValue(reach);
						break;
					case "reset":
						reach.reset();
						break;
					default:
						Double RCH = Double.parseDouble(message2);
						reach.setValue(RCH);
						Logger.logSetMessage("BowAimbot", "Reach", reach);
						break;
					}

				case "animals":
				case "Animals":
				case "a":
					animals.value = !animals.value;
					Logger.logSetMessage("BowAimbot", "Animals", animals);
					break;
				case "monsters":
				case "Monsters":
				case "m":
				case "mobs":
				case "Mobs":
				case "Monster":
				case "monster":
					monsters.value = !monsters.value;
					Logger.logSetMessage("BowAimbot", "Monsters", monsters);
					break;
				case "players":
				case "Players":
				case "p":
					players.value = !players.value;
					Logger.logSetMessage("BowAimbot", "Players", players);
					break;
				case "invisibles":
				case "invis":
				case "i":
				case "invisible":
					players.value = !players.value;
					Logger.logSetMessage("BowAimbot", "Players", players);
					break;
				case "ta":
				case "targeting":
				case "tmode":
				case "sorting":
					String[] Modes = new String[] { "Distance", "Health" };
					try {
						String message21 = arguments[2];
						switch (message21.toLowerCase()) {
						case "Distance":
						case "distance":
						case "d":
							tMode.value = TargetingMode.Distance;
							Logger.logSetMessage("Bow Aimbot", "Targeting Mode", tMode);
							break;
						case "Health":
						case "health":
						case "hp":
							tMode.value = TargetingMode.Health;
							Logger.logSetMessage("Bow Aimbot", "Targeting Mode", tMode);
							break;
						default:
							Logger.LogExecutionFail("Mode!", Modes);
							break;
						}
					} catch (Exception e) {
						Logger.LogExecutionFail("Mode!", Modes);
					}
					break;
				case "values":
					logValues();
					break;
				default:
					Logger.logChat(getError());
				}
			}
		});
	}
}