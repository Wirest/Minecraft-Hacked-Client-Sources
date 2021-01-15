package info.spicyclient.chatCommands.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import info.spicyclient.chatCommands.Command;

public class Date extends Command {

	public Date() {
		super("date", "date", 0);
	}
	
	@Override
	public void commandAction(String message) {
		
		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		sendPrivateChatMessage(dtf.format(now));
		
	}
	
	@Override
	public void incorrectParameters() {
		sendPrivateChatMessage("Please use .time");
	}
	
}
