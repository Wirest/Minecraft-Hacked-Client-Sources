package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command;

import java.util.ArrayList;
import java.util.List;

import skyline.specc.SkyLine;

public class CommandBuilder {

	private String name;
	private String desc;
	private List<String> aliases = new ArrayList<String>();
	private CommandListener listener;
	
	public CommandBuilder withName(String name){
		this.name = name;
		return this;
	}
	
	public CommandBuilder withDesc(String desc){
		this.desc = desc;
		return this;
	}
	
	public CommandBuilder addAlias(String alias){
		aliases.add(alias);
		return this;
	}
	
	public CommandBuilder withListener(CommandListener listener){
		this.listener = listener;
		return this;
	}
	
	public void build(){
		String[] stockArr = new String[aliases.size()];
		stockArr = aliases.toArray(stockArr);
		WrappedCommand command = new WrappedCommand(this.name, stockArr, this.desc, this.listener);
		SkyLine.getManagers().getCommandManager().addContent(command);
	}
	
}
