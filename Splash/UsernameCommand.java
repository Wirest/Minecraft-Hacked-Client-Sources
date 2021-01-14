package splash.client.commands;

import java.awt.datatransfer.Clipboard;

import com.mojang.realmsclient.dto.RealmsServer.McoServerComparator;

import net.minecraft.client.Minecraft;
import splash.api.command.Command;
import splash.utilities.clipboard.ClipboardUtils;
import splash.utilities.system.ClientLogger;

public class UsernameCommand extends Command {

	public UsernameCommand() {
		super("username");
	}

	@Override
	public String usage() {
		return "username";
	}

	@Override
	public void executeCommand(String[] commandArguments) {
		if(Minecraft.getMinecraft().theWorld != null) {
			ClipboardUtils.clip(Minecraft.getMinecraft().thePlayer.getName());
			ClientLogger.printToMinecraft("Copied name!");
		}
	}

}
