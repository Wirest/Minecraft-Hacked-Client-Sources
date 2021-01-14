package de.iotacb.client.command.commands;

import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import net.minecraft.client.Minecraft;

@CommandInfo(names = {"Clear"}, description = "Clear the chat", usage = "#clear")
public class ClearCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
	}

}
