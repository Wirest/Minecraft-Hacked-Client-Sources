package nivia.commands.commands;

import com.google.common.base.Objects;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Module;
import nivia.modules.Module.Category;
import nivia.utils.Logger;

import java.util.ArrayList;

public class Legit extends Command {
	private ArrayList<Module> disabledMods = new ArrayList();
	public Legit() {
		super("Legit", "Disables all non-render modules", null, false, "l");
		
		Pandora.getCommandManager().cmds.add(new Command
	 			("BackOn", "Enables the previously disabled by LEGIT modules", null, false, "bo") {
					@Override
					public void execute(String commandName, String[] arguments) {
						if(disabledMods.isEmpty()){
							Logger.logChat("No modules to turn back on.");
							return;
						}
						for(Module m : disabledMods)
							if(!m.getState()) 
								m.Toggle();						
						Logger.logChat("Toggled modules back on.");
					}								
		});
		
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		for(Module m : Pandora.getModManager().enabledModules()){
			if(!Objects.equal(m.getCategory(), Category.RENDER)){
				if(m.getState()){ 
					m.Toggle();
					disabledMods.add(m);
			}}
		}
	}

}
