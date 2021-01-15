package nivia.modules.ghost;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Logger;

public class ThrowPot extends Module {

	public DoubleProperty delay = new DoubleProperty(this, "Delay", 350L, 1, 2000);
	public DoubleProperty health = new DoubleProperty(this, "Health", 10, 1, 50);
	public Property<Boolean> getPot = new Property<Boolean>(this, "GetPot", false);

	public ThrowPot() {
		super("ThrowPot", 0, 0xE6B800, Category.GHOST, "Throws a pot.",
				new String[] { "throwp", "tpot", "throw" }, true);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		Pandora.getCommandManager().getCommand(nivia.commands.commands.ThrowPot.class).execute("ThrowPot",new String[0]);
		this.Toggle();
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("ThrowPot", "Manages Throwpot Stuff.",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Health", "Delay", "getPot", "Values" }),
				"throwpot", "tpot", "throw") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message) {
				case "pot":
				case "get":
				case "getpot":
					getPot.value = !getPot.value;
					Logger.logToggleMessage("GetPot", getPot.value);
					break;
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
							Logger.logSetMessage("ThrowPot", "Health", health);
							break;
						default:
							int nHP = Integer.parseInt(message2);
							health.setValue(nHP);
							Logger.logSetMessage("ThrowPot", "Health", health);
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
						Logger.logSetMessage("ThrowPot", "delay", delay);
						break;
					default:
						Long nD = Long.parseLong(message21);
						delay.setValue(nD);
						Logger.logSetMessage("ThrowPot", "delay", delay);
						break;
					}
					break;
				case "values":
				case "actual":
					logValues();
					break;
				default:
					Logger.LogExecutionFail("Option, Options:",
							new String[] { "Health", "Delay", "GetPots", "Values", "" });
					break;

				}
			}
		});
	}
}