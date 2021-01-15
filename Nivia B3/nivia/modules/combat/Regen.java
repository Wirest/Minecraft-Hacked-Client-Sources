package nivia.modules.combat;

import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Logger;
import nivia.utils.utils.Timer;

public class Regen extends Module {
	public DoubleProperty health = new DoubleProperty(this, "Health", 19, 1, 20);
	public DoubleProperty packets = new DoubleProperty(this, "Packets", 50, 1, 5000);
	private Timer timer = new Timer();

	public Regen() {
		super("Regen", 0, 0xFFFFFF, Category.COMBAT, "Regenerates faster, duh.", new String[] { "reg" }, true);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdates e) {
		for (int index = 0; index < packets.getValue(); index++) {
			if (mc.thePlayer.onGround && mc.thePlayer.getHealth() <= health.getValue() && mc.thePlayer.isEntityAlive()
					&& mc.thePlayer.getFoodStats().getFoodLevel() > 10) {
				if (mc.thePlayer.onGround)
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
				else
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
			}
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Regen", "Manages Regen stuff",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Health", "Packets", "Values" }), "rg") {
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
							Logger.logSetMessage("Regen", "Health", health);
							break;
						default:
							int nHP = Integer.parseInt(message2);
							health.setValue(nHP);
							Logger.logSetMessage("Regen", "Health", health);
							break;
						}
						break;
					} catch (Exception e) {
					}

				case "Packets":
				case "packets":
				case "p":
					String message21 = arguments[2];
					switch (message21) {
					case "actual":
					case "value":
						logValue(packets);
						break;
					case "reset":
						packets.reset();
						Logger.logSetMessage("Regen", "Packets", packets);
						break;
					default:
						Integer nD = Integer.parseInt(message21);
						packets.setValue(nD);
						Logger.logSetMessage("Regen", "Packets", packets);
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
