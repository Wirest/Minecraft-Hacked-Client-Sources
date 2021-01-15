package nivia.commands.commands;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.gui.altmanager.AltManager;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

public class Stuff extends Command {
	private Collator collator = Collator.getInstance(Locale.US);
	public Stuff() {
		super("Stuff", "Logs the available commands or modules.", Logger.LogExecutionFail("argument! Nivia [mods, commands, reload, save]"), false, "list", "mods", "cmds", "modules", Pandora.getClientName(), "clientt");
	}

	@Override
	public void execute(String commandName, String[] arguments) {		
		ArrayList<Module> mods = new ArrayList<Module>();
		ArrayList<Command> cmds = new ArrayList<Command>();
		String message = "";
        try{ message = arguments[1]; } catch (Exception e){}
		switch(message.toLowerCase()){
		case "mods": case "modules":
		case "Mods": case "Modules":
			logModules(mods);
			break;
		case "cmds": case "commands":
		case "Cmds": case "Commands":
			logCommands(cmds);
			break;
            case "save":
                Pandora.getFileManager().saveFiles();
                Logger.logChat("Saved.");
                break;
            case "reload":
            	AltManager.registry.clear();
                Pandora.getFileManager().loadFiles();
                break;
            default:
                Logger.logChat(this.getError());
                break;
		}
	}
	private void logModules(ArrayList<Module> mods){
		for(Module m : Pandora.getModManager().mods){
			if(!mods.contains(m)) mods.add(m);
		}
		mods.sort((mod1, mod2) -> {						
		     return collator.compare(mod1.getName(), mod2.getName());
		});
		StringBuilder listmods = new StringBuilder("\247bMods ("+ mods.size() + "): ");
        for (Module mod : mods) 
        	listmods.append(mod.getState() ? "\247b" : "\2477")
            .append(mod.getName()).append(" \2478- ");

        Logger.logChat(listmods.toString().substring(0, listmods.toString().length() - 2));
	}
	private void logCommands(ArrayList<Command> cmds){
		for(Command cmd : Pandora.getCommandManager().cmds){
			if(!cmds.contains(cmd)) cmds.add(cmd);
		}
		cmds.sort((cmd1, cmd2) -> {						
		     return collator.compare(cmd1.getName(), cmd2.getName());
		});		       
		final StringBuilder listcmds = new StringBuilder("\247bCommands: ");
		final StringBuilder modcmds = new StringBuilder("\247bModule Commands: ");
        for (final Command cmd : cmds){
        	if(!cmd.isModuleCommand())
        	listcmds.append("\2477")
            .append(cmd.getName()).append("\2478 - ");
        	else {
        	modcmds.append("\2477").append(cmd.getName()).append(" \2478- ");
        	}
        }
        Logger.logChat(modcmds.toString().substring(0, modcmds.toString().length() - 2));
        Logger.logChat(listcmds.toString().substring(0, listcmds.toString().length() - 2));
	}
}
