package moonx.ohare.client.command.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.utils.Printer;
import net.minecraft.client.Minecraft;

public class Vclip extends Command {

	public Vclip() {
		super("Vclip", new String[]{"v", "vclip"});
	}

	@Override
	public void onRun(final String[] args) {
		if (args.length == 1) {
			Printer.print(".vclip <value>");
			return;
		}
		final double distance = Double.parseDouble(args[1]);
		Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0, distance, 0);
		Moonx.INSTANCE.getNotificationManager().addNotification("Vcliped " + args[1] + "!", 2000);
		Printer.print("Vcliped " + args[1] + "!");
	}
}
