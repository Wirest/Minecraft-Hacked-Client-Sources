package nivia.modules.ghost;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.MathUtils;
import nivia.utils.utils.Timer;

import java.awt.*;

public class TriggerBot extends Module {
	
	public DoubleProperty cps = new DoubleProperty(this, "CPS", 15, 1, 20);
	public DoubleProperty random = new DoubleProperty(this, "Random CPS", 5, 0, 20);
	public DoubleProperty range = new DoubleProperty(this, "Range", 4.0, 0, 10, 1);
	public DoubleProperty ticks = new DoubleProperty(this, "Ticks existed", 30, 0, 100);
	public DoubleProperty swingfov = new DoubleProperty(this, "Swing FOV", 90, 28, 360);
	public DoubleProperty afov = new DoubleProperty(this, "Aim FOV", 120, 0, 360);
	public Property<Boolean> attack = new Property<Boolean>(this, "Attack", true);
	public Property<Boolean> aim = new Property<Boolean>(this, "Smooth Aim", true);
	public DoubleProperty hspeed = new DoubleProperty(this, "Horizontal Speed", 3, 0, 50);
	public DoubleProperty vspeed = new DoubleProperty(this, "Vertical Speed", 0, 0, 50);

	public Property<Boolean> sword = new Property<Boolean>(this, "Swords/Axes", true);
	public Property<Boolean> block = new Property<Boolean>(this, "Block", false);
	public Property<TrigMode> tmode = new Property<TrigMode>(this, "Mode", TrigMode.Legit);
	
	private Timer timer = new Timer();


	public TriggerBot() {
		super("TriggerBot", 0, 0xE6B800, Category.GHOST, "Clicks Automatically.", new String[] { "trig", "tbot", "tb" }, true);
	}

	public enum TrigMode {
        Legit, Fake;
    }
	
	private boolean isValidTarget(Entity target) {
		if (target.isEntityAlive() && target.ticksExisted >= ticks.getValue() && target.isEntityAlive() && mc.thePlayer.getDistanceToEntity(target) <= (range.getValue()) && (target != mc.thePlayer)) {
			if (!FriendManager.isFriend(target.getName())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private boolean canAttack() {
		if (mc.thePlayer.isDead || !mc.inGameHasFocus || !mc.thePlayer.isEntityAlive())
			return false;

		boolean canSwing = sword.value ? mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword || mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe : true;
		
		if (sword.value && !canSwing)
			return false;
		
		return true;
	}

	@EventTarget(Priority.HIGHEST)
	private void onEvent(EventPreMotionUpdates event) {
		if (aim.value && canAttack()) {
			Entity target = Helper.entityUtils().findClosestToCross(range.getValue());

				if (isValidTarget(target) && mc.inGameHasFocus && isValidTarget(target)) {
					if (Helper.entityUtils().isWithingFOV(target, (float) afov.getValue())) {
						float[] face = Helper.combatUtils().faceTarget(target, (int) hspeed.getValue(), (int) vspeed.getValue(), false);
						
						mc.thePlayer.rotationYaw = face[0];
						mc.thePlayer.rotationPitch = face[1];
				}
			}
		}
	}

	@EventTarget(Priority.HIGH)
	private void onUpdate(EventPostMotionUpdates event) throws AWTException {
		Entity target = Helper.entityUtils().findClosestToCross(range.getValue());
		
		if (!(target instanceof EntityPlayer)) return;
		
		boolean time = timer.hasTimeElapsed((long) (1000 / cps.getValue() + MathUtils.getRandom((int) 0, (int) random.getValue())) , true);
		
		if (canAttack() && attack.value && tmode.value == TrigMode.Fake ? Helper.entityUtils().isWithingFOV(target, 28) : Helper.entityUtils().isWithingFOV(target, (float) swingfov.getValue()) && mc.inGameHasFocus && isValidTarget(target)) {
			if (time) {
				switch (tmode.value) {
				case Fake:
					if (mc.thePlayer.isBlocking() && block.value) 
						Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
					
					mc.thePlayer.swingItem();
					Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C02PacketUseEntity(Helper.combatUtils().rayTrace(1.0F, mc.thePlayer.getDistance(target.posX, target.getEyeHeight(), target.posZ)), C02PacketUseEntity.Action.ATTACK));
					break;
				case Legit:
					Helper.playerUtils().doRealClick();
					break;
				}
			}	
		}
	}
	
	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Triggerbot", "Manages Triggerbot stuff", Logger.LogExecutionFail("Option, Options:", new String[]
				{"Fake", "Legit", "Swing FOV", "Aim FOV", "Attack", "Horizontal Speed", "Vertical Speed", "Smooth Aim", "Swords/Axes", "Block", "CPS", "Reach", "Ticks", "Random", "Values"}), "trig", "tbot", "tb") {
					@Override
					public void execute(String commandName, String[] arguments) {
						String message = arguments[1];
						switch (message) {
						case "speed":
						case "sp":
						case "Speed":
						case "cps":
						case "aps":
							try {
								String message2 = arguments[2];
								switch (message2) {
								case "actual":
								case "value":
									logValue(cps);
									break;
								case "reset":
									cps.reset();
									Logger.logSetMessage("Triggerbot", "Cps", cps);
									break;
								default:
									Integer nSP = Integer.parseInt(message2);
									cps.setValue(nSP);
									Logger.logSetMessage("Triggerbot", "Cps", cps);
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
								Logger.logSetMessage("Triggerbot", "Attack Reach", range);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						 case "legit": case "leg": case "lgit": case "kohi":
		                	   tmode.value = TrigMode.Legit;
		                       Logger.logSetMessage("Triggerbot", "Mode", tmode);
		                       break;
						 case "fake": case "fak": case "sketchy": case "sketch":
		                	   tmode.value = TrigMode.Fake;
		                       Logger.logSetMessage("Triggerbot", "Mode", tmode);
		                       break;
							case "sword":
			                case "onlysword":
			                case "axe":
			                case "onlyaxe":
			                case "swd":	
			                    sword.value = !sword.value;
			                    Logger.logToggleMessage("Swords/Axes", sword.value);                    
			                    break;
			            
			                case "block":
			                case "blck":
			                case "autoblock":
			                case "ab":
			                case "blok":	
			                    block.value = !block.value;
			                    Logger.logToggleMessage("Auto Block", block.value);                    
			                    break;
			                case "swing":
			                case "attack":
			                case "attck":
			                case "atk":
			                case "attak":	
			                    attack.value = !attack.value;
			                    Logger.logToggleMessage("Attack", attack.value);                    
			                    break;
			                case "smooth":
			                case "saim":
			                case "smoothaim":
			                case "aim":
			                case "aimassist":	
			                    aim.value = !aim.value;
			                    Logger.logToggleMessage("Smooth Aim", aim.value);                    
			                    break;
						case "swingfov":
						case "sfov":
						case "swingangle":
						case "swing fov":
							try {
								String message2 = arguments[2];
								Integer RNG = Integer.parseInt(message2);
								swingfov.setValue(RNG);
								Logger.logSetMessage("Triggerbot", "Swing FOV", swingfov);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						case "hspeed":
						case "hs":
						case "hsp":
							try {
								String message2 = arguments[2];
								Double RNG = Double.parseDouble(message2);
								hspeed.setValue(RNG);
								Logger.logSetMessage("Triggerbot", "Horizontal Speed", hspeed);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						case "vspeed":
						case "vs":
						case "vsp":
							try {
								String message2 = arguments[2];
								Double RNG = Double.parseDouble(message2);
								vspeed.setValue(RNG);
								Logger.logSetMessage("Triggerbot", "Vertical Speed", vspeed);
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
								Logger.logSetMessage("Triggerbot", "Random", random);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						case "aimfov":
						case "afov":
						case "aimangle":
						case "aim fov":
							try {
								String message2 = arguments[2];
								Integer RND = Integer.parseInt(message2);
								afov.setValue(RND);
								Logger.logSetMessage("Triggerbot", "Smooth Aim FOV", afov);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
							}
							break;
						case "ticks":
						case "tcks":
						case "ticksexisted":
							try {
								String message2 = arguments[2];
								Integer TCK = Integer.parseInt(message2);
								ticks.setValue(TCK);
								Logger.logSetMessage("Triggerbot", "Ticks Existed", ticks);
							} catch (Exception e) {
								Logger.LogExecutionFail("Value!");
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
