package moonx.ohare.client.command.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.utils.Printer;
import org.apache.commons.lang3.text.WordUtils;

public class Help extends Command {

	public Help() {
		super("Help", new String[]{"h", "help"});
	}

	@Override
	public void onRun(final String[] s) {
		Moonx.INSTANCE.getCommandManager().getCommandMap().values().forEach(command -> {
			Printer.print(WordUtils.capitalizeFully(command.getLabel()));
		});
	}
}
