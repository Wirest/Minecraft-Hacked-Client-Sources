package me.xatzdevelopments.xatz.client.commands;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.modules.NameProtect;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.module.Module;

public class CommandBind extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length < 3) {
			Xatz.chatMessage("Invalid Arguments: Use \".bind <module> <keybind>\"");
			return;
		}
		
		Module module = Xatz.getModuleByName(commands[1]);
		if (module == null) {
			Xatz.getNotificationManager()
			.addNotification(new Notification(Level.ERROR, "Could not find module \"" + commands[1] + "\"!"));
			return;
		}
		
		int key = Keyboard.getKeyIndex(commands[2].toUpperCase());
		
	    Xatz.getModuleByName(commands[1]).setKeyCode(key);
	    mc.thePlayer.playSound("random.anvil_use", 1f, 1f);
		return;
	}
	
	
	

	@Override
	public String getActivator() {
		return ".bind";
	}

	
	@Override
	public String getSyntax() {
		return ".bind <module> <keybind>";
	}

	@Override
	public String getDesc() {
		return "Binds a module to a keybind";
	}
}
