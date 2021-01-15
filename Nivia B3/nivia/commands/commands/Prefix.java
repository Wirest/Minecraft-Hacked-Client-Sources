package nivia.commands.commands;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.files.Misc;
import nivia.utils.Logger;

import java.io.IOException;

public class Prefix extends Command {
	 public Prefix() {
		 	super("Prefix", "Changes the client's prefix", "Invalid prefix.", false);
	    } 
	 
	 @Override
		public void execute(String commandName, String[] arguments) {
			String newPrefix = arguments[1];
			if(newPrefix.length() > 1){
				Logger.logChat(this.getError());
				return;
			}
			Pandora.prefix = newPrefix;
			Logger.logSetMessage("Client's prefix" , Pandora.prefix);
         try {
             Pandora.getFileManager().getFile(Misc.class).saveFile();
         } catch (IOException e) {}
     }
	 
}
