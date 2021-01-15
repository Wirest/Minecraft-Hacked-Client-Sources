package nivia.modules.ghost;

import net.minecraft.potion.Potion;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Logger;
import nivia.utils.utils.InventoryUtils;
import nivia.utils.utils.Timer;

public class Refill extends Module {

	public DoubleProperty delay = new DoubleProperty(this, "Delay", 100, 0, 1000);
	private Timer timer = new Timer();

	public Refill() {
		super("Refill", 0, 0, Category.GHOST, "Refills your potions.", new String[] { "rfill", "rf", "fill", "refil" },
				false);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdates pre) {
		for (int i = 0; i < 9; ++i) {
			if (!InventoryUtils.hotbarIsFull() && this.timer.hasTimeElapsed((long) this.delay.getValue(), false)
					&& InventoryUtils.inventoryHasPotion(Potion.heal, true)) {
				InventoryUtils.shiftClickPotion(Potion.heal, true);
				timer.reset();
				if (!InventoryUtils.inventoryHasPotion(Potion.heal, true))
					this.Toggle();
			}

			if (InventoryUtils.hotbarIsFull())
				this.Toggle();

			if (!InventoryUtils.inventoryHasPotion(Potion.heal, true))
				this.Toggle();
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Refill", "Manages Refill stuff.",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Delay", "Values" }), "rfill", "rf", "fill",
				"refil") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
				case "delay":
				case "d":
				case "D":
				case "Delay":
					String message2 = arguments[2];
					switch (message2) {
					case "actual":
					case "value":
						logValue(delay);
						break;
					case "reset":
						delay.reset();
						Logger.logSetMessage("Refill", "Delay", delay);
						break;
					default:
						Long nD = Long.parseLong(message2);
						delay.setValue(nD);
						Logger.logSetMessage("Refill", "Delay", delay);
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