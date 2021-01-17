package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands;

import java.util.List;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.specc.SkyLine;
import skyline.specc.managers.FriendManager.Friend;


public class FriendCommand extends Command {

	public FriendCommand() {
		super("Friend", new String[]{}, "Manage friends.");
	}

	@Override
	public void onCommand(List<String> args) {
		if(args.size() >= 2){
			if(args.get(0).equalsIgnoreCase("add")){
				String name = args.get(1);

                if(SkyLine.getManagers().getFriendManager().hasFriend(name)){
					for(int i = 0; i <= SkyLine.getManagers().getFriendManager().getContents().size() - 1; i++){
						Friend friend = SkyLine.getManagers().getFriendManager().getContents().get(i);

						if(friend.getName().equalsIgnoreCase(name) || friend.getNickname().equalsIgnoreCase(name)){
							SkyLine.getManagers().getFriendManager().removeContent(friend);
							SkyLine.getManagers().getFriendManager().save();
						}
					}
				}
				
				String nickname;

				if(args.size() == 3){
					nickname = args.get(2).replaceAll(" ", "");
				}else{
					nickname = name;
				}

				SkyLine.getManagers().getFriendManager().addContent(new Friend(name, nickname));
				SkyLine.getManagers().getFriendManager().save();
				addChat("Added friend.");
			}else if(args.get(0).equalsIgnoreCase("remove")){
				String name = args.get(1);

				if(SkyLine.getManagers().getFriendManager().hasFriend(name)){
					for(int i = 0; i <= SkyLine.getManagers().getFriendManager().getContents().size() - 1; i++){
						Friend friend = SkyLine.getManagers().getFriendManager().getContents().get(i);

						if(friend.getName().equalsIgnoreCase(name) || friend.getNickname().equalsIgnoreCase(name)){
							SkyLine.getManagers().getFriendManager().removeContent(friend);
							SkyLine.getManagers().getFriendManager().save();
							addChat("Removed friend.");
						}
					}
				}else{
					error("Friend not found!");
				}
			}
		}else{
			error("Invalid args! Usage : 'Friend add [name] [nickname]' or 'Friend remove [name]'");
		}
	}


}
