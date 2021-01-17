package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command;

import java.util.List;

public class WrappedCommand extends Command {

	private CommandListener listener;
	
	public WrappedCommand(String name, String[] aliases, String description, CommandListener listener) {
		super(name, aliases, description);
		this.listener = listener;
	}
	
	@Override
	public void onCommand(List<String> args) {
		listener.onCommandRan(args);
	}

}
