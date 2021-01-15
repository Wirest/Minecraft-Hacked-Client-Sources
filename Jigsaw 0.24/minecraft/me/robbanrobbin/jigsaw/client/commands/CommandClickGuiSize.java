package me.robbanrobbin.jigsaw.client.commands;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;

public class CommandClickGuiSize extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length == 1 || commands.length > 2) {
			Jigsaw.chatMessage("§cEnter one numer!");
			return;
		}
		int size;
		try {
			size = Integer.parseInt(commands[1]);
		} catch (NumberFormatException e) {
			Jigsaw.chatMessage("§cEnter a valid number!");
			return;
		}
//		((SimpleTheme) Jigsaw.getGUIMananger().getTheme()).fontRenderer = new UnicodeFontRenderer(
//				new Font("Segue UI", Font.PLAIN, size));
		Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Fontsize was set to: " + size));
		ClientSettings.clickGuiFontSize = size;
		//Jigsaw.getGUIMananger().reload();
	}

	@Override
	public String getActivator() {
		return ".fontsize";
	}

	@Override
	public String getSyntax() {
		return ".fontsize <size>";
	}

	@Override
	public String getDesc() {
		return "Sets the font size for the ClickGUI. Default is 13.";
	}
}
