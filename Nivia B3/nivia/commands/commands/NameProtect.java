package nivia.commands.commands;

import nivia.commands.Command;
import nivia.utils.Logger;



public class NameProtect extends Command {
	public static String prot = "";
    public NameProtect() {
        super("NameProtect", "Hide your name XD", Logger.LogExecutionFail("Name", new String[]{"Name", "Clear"}), false, "nprot", "nameprot", "nprotect", "np");
    }
    @Override
    public void execute(String commandName, String[] arguments){
    	if(arguments[1].equalsIgnoreCase("clear")){
    		prot = "";
    		Logger.logChat("Cleared nameprotect.");
    		return;
    	}
        prot = arguments[1];
        Logger.logSetMessage("Hidden name", prot);
    }
}
