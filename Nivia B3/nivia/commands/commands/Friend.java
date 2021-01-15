package nivia.commands.commands;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Hooked;
import nivia.utils.Logger;
public class Friend extends Command{
	public Friend(){
		super("Friend", "Add delete or clear your friends.", Logger.LogExecutionFail("Argument", new String[]{"Add", "Delete", "Clear", "Safe"}), false , "fr", "friends");
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		String message = arguments[1], name = "", alias = "";
		try { name = arguments[2]; } catch (Exception e){}
		try { alias = arguments[3]; } catch (Exception e){}		
		switch(message.toLowerCase()){
		case "add":
		case "a":
			if(alias.isEmpty()) alias = name;
			if (!Pandora.getFriendManager().isFriend(name) && !name.isEmpty()){
                Pandora.getFriendManager().addFriend(name, alias);
                Logger.logChat("You have added \247b" + name.replaceFirst(name.substring(1), "\247b" + name.substring(1)) + " \2477to your friend list as \247b" + alias);
            } else 
            	Logger.logChat("That player is already a friend or isn't valid!");           
				break;
		case "remove": case "d":
		case "del": case "delete":
			if (Pandora.getFriendManager().isFriend(name)) {
                Pandora.getFriendManager().deleteFriend(name);
                Logger.logChat("You have removed \247b" + name.replaceFirst(name.substring(1), "\247b" + name.substring(1)) + "\2477 from your friends list."  );
            } else 
            	Logger.logChat(name + " is not a friend!");          
			break;
		case "clear":case "emtpy": case "e":
			Logger.logChat("Cleared Friends!");
            Pandora.getFriendManager().friends.clear();
            break;
            case "safe":case "s":
                Hooked.safe.value = !Hooked.safe.value;
                Logger.logSetMessage("Safe Friends", Hooked.safe.value.toString());
                break;
        default: Logger.logChat(this.getError());;
        break;
			}
		}
	}

