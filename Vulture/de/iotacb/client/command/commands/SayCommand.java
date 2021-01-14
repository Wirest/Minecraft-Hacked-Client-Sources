package de.iotacb.client.command.commands;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import net.minecraft.client.Minecraft;

@CommandInfo(names = {"Say"}, description = "Say something in chat (So you can send the command prefix)", usage = "#say MESSAGE")
public class SayCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		final StringBuilder message = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			message.append(args[i].concat(" "));
		}
		Minecraft.getMinecraft().thePlayer.sendChatMessage(message.toString());
	}

}
