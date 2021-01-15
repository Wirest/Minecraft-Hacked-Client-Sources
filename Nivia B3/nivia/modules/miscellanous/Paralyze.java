package nivia.modules.miscellanous;

import net.minecraft.network.play.client.C03PacketPlayer;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

public class Paralyze extends Module {
	public DoubleProperty packets = new DoubleProperty(this, "Packets", 250, 1, 10000);

	public Paralyze() {
		super("Paralyze", 0, 0, Category.MISCELLANEOUS, "Attempts to make players unable to move.",
				new String[] { "paralyse" }, true);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdates pre) {
		if (!this.mc.theWorld
				.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox())
				.isEmpty()) {
			for (int i = 1; i < packets.getValue(); ++i)
				Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, false));
			Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
					mc.thePlayer.posZ, false));
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Paralyze", "Manages Paralyze stuff",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Packets", "Values" }), "paralyse") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
				case "packets":
				case "p":
					String message2 = arguments[2];
					switch (message2) {
					case "actual":
					case "value":
						logValue(packets);
						break;
					case "reset":
						packets.reset();
						Logger.logSetMessage("Paralyze", "Packets", packets);
						break;
					default:
						Integer nD = Integer.parseInt(message2);
						packets.setValue(nD);
						Logger.logSetMessage("Paralyze", "Packets", packets);
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