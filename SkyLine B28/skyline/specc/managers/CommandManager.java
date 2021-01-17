package skyline.specc.managers;

import java.util.ArrayList;
import java.util.Set;

import com.google.common.reflect.ClassPath;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.BindCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.FriendCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.SpamCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.ThemeCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.ToggleCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.VClipCommand;
import skyline.specc.SkyLine;
import skyline.specc.managers.File.Manager;

public class CommandManager extends Manager<Command> {

	public CommandManager(){
		addContent(new BindCommand());
		addContent(new FriendCommand());
		addContent(new ToggleCommand());
		addContent(new VClipCommand());
		addContent(new ThemeCommand());
		addContent(new SpamCommand());

	}

	public Command getCommandFromName(String name){
		for(Command m : getContents()){
			if(m.getName().equalsIgnoreCase(name))
				return m;
		}

		return null;
	}

	public Command getCommandFromClass(Class clas){
		for(Command m : getContents()){
			if(m.getClass() == clas){
				return m;
			}
		}
		return null;
	}

	private ArrayList<Class<?>> getClasses(final String packageName){
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		try{
			final ClassLoader loader = Thread.currentThread()
					.getContextClassLoader();
			for(final ClassPath.ClassInfo info : ClassPath.from(loader)
					.getAllClasses()){
				if(info.getName().startsWith(packageName)){
					final Class<?> clazz = info.load();
					classes.add(clazz);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return classes;
	}

	public boolean hasCommand(Command m){

		for(Command command : getContents()){
			if(command == m)
				return true;
		}

		return false;
	}
	
}
