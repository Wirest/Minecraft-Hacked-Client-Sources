package nivia.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.modules.movement.Speed;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.InventoryUtils;
import nivia.utils.utils.Timer;

public class AutoPot extends Module {

	public DoubleProperty delay = new DoubleProperty(this, "Delay", 350L, 1, 2000);
	public DoubleProperty health = new DoubleProperty(this, "Health", 10, 1, 20);
	private float oldYaw, oldPitch;
	public static int pots;
	private final Timer timer = new Timer(), jumptimer = new Timer();
	public static boolean doPot;

	public Property<Boolean> jump = new Property<>(this, "Jump Pot", false);

	public AutoPot() {
		super("AutoPotioner", 0, 0xFFAA00CC, Category.COMBAT, "Throws potions automatically",
				new String[] { "apn", "autopotn", "ap", "autopot"}, true);
	}

	@EventTarget(Priority.HIGHEST)
	public void onPreMotion(EventPreMotionUpdates event) {
		this.setSuffix(pots + "");
		if(health.max < mc.thePlayer.getMaxHealth()) health.max = mc.thePlayer.getMaxHealth();
		boolean checkground = !Helper.playerUtils().MovementInput() && mc.thePlayer.isCollidedVertically;
		pots = Helper.inventoryUtils().countPotion(Potion.heal, true);
		if(pots == 0) return;
		if (timer.hasTimeElapsed((long) delay.getValue()) && mc.thePlayer.getHealth() <= health.getValue()) {
			float pitch = jump.value && mc.thePlayer.isCollidedVertically ? -90 : getPitch();
			event.setPitch(pitch);
			doPot = true;
			if(mc.thePlayer.isCollidedVertically && jump.value) {
				mc.thePlayer.motionZ *= mc.thePlayer.motionX *= 0;
				mc.thePlayer.jump();
			}
		}
	}

	@EventTarget
	public void onTick(EventPostMotionUpdates event) {
		if (doPot) {
			if(jumptimer.hasTimeElapsed(25L, true)) {
				if (InventoryUtils.hotbarHasPotion(Potion.heal, true)) {
					InventoryUtils.useFirstPotionSilent(Potion.heal, true);
				} else {
					InventoryUtils.getPotion(Potion.heal, true);
					InventoryUtils.useFirstPotionSilent(Potion.heal, true);
				}
			}
			timer.reset();
			doPot = false;
		}
	}

	public int getCount() {
		int counter = 0;
		for (int i = 0; i < 36; ++i) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (item instanceof ItemPotion) {
					ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (Object o : potion.getEffects(is)) {
							PotionEffect effect = (PotionEffect) o;
							if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
								++counter;
							}
						}
					}
				}
			}
		}
		return counter;
	}

	public float getPitch() {
		boolean checksprintfwd = mc.gameSettings.keyBindForward.getIsKeyPressed() && !mc.gameSettings.keyBindBack.getIsKeyPressed() && mc.thePlayer.isSprinting() && Helper.playerUtils().MovementInput() && mc.thePlayer.moveForward != 0;
		boolean checksprintbwd = mc.gameSettings.keyBindBack.getIsKeyPressed() && !mc.gameSettings.keyBindForward.getIsKeyPressed() && mc.thePlayer.isSprinting() && Helper.playerUtils().MovementInput() && mc.thePlayer.moveForward == 0;
		boolean checkfwd = mc.gameSettings.keyBindForward.getIsKeyPressed() && !mc.gameSettings.keyBindBack.getIsKeyPressed() && Helper.playerUtils().MovementInput();
		boolean checkbwd = mc.gameSettings.keyBindBack.getIsKeyPressed() && !mc.gameSettings.keyBindForward.getIsKeyPressed() && Helper.playerUtils().MovementInput();
		Speed s = (Speed) Pandora.getModManager().getModule(Speed.class);
		boolean bhop = Pandora.getModManager().getModState("Speed") && !mc.thePlayer.isCollidedVertically && mc.thePlayer.moveForward != 0 && s.mode.value.equals(Speed.speedMode.HOP) ;
		boolean air = !mc.thePlayer.isCollidedVertically && mc.thePlayer.moveForward != 0;
		return checksprintfwd ? 50 : checkfwd ? 65 : checksprintbwd && !bhop ? 175 : checkbwd && !bhop ? 170 : bhop ? 90 : air ? 65 : 105;
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Autopot", "Manages Autopot stuff",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Health", "Delay", "Values" }), "ap", "apot",
				"autop") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message) {
				case "health":
				case "hp":
				case "Health":
				case "h":
					try {
						String message2 = arguments[2];
						switch (message2) {
						case "actual":
						case "value":
							logValue(health);
							break;
						case "reset":
							health.reset();
							Logger.logSetMessage("AutoPot", "Health", health);
							break;
						default:
							int nHP = Integer.parseInt(message2);
							health.setValue(nHP);
							Logger.logSetMessage("AutoPot", "Health", health);
							break;
						}
						break;
					} catch (Exception e) {
					}

				case "Delay":
				case "delay":
				case "d":
					String message21 = arguments[2];
					switch (message21) {
					case "actual":
					case "value":
						logValue(delay);
						break;
					case "reset":
						delay.reset();
						Logger.logSetMessage("AutoPot", "delay", delay);
						break;
					default:
						Long nD = Long.parseLong(message21);
						delay.setValue(nD);
						Logger.logSetMessage("AutoPot", "delay", delay);
						break;
					}
					break;
				case "values":
				case "actual":
					logValues();
					break;
				default:
					Logger.LogExecutionFail("Option, Options:", new String[] { "Health", "Delay", "Values" });
					break;

				}
			}
		});
	}
}