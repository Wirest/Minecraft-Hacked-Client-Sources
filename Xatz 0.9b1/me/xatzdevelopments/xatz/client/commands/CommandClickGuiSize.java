package me.xatzdevelopments.xatz.client.commands;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;

public class CommandClickGuiSize extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length == 1 || commands.length > 2) {
			Xatz.chatMessage("§cEnter one numer!");
			return;
		}
		int size;
		try {
			size = Integer.parseInt(commands[1]);
		} catch (NumberFormatException e) {
			Xatz.chatMessage("§cEnter a valid number!");
			return;
		}
//		((SimpleTheme) Xatz.getGUIMananger().getTheme()).fontRenderer = new UnicodeFontRenderer(
//				new Font("Segue UI", Font.PLAIN, size));
		Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, "Fontsize was set to: " + size));
		ClientSettings.clickGuiFontSize = size;
		//Xatz.getGUIMananger().reload();
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
