package modification.commands;

import modification.extenders.Command;
import modification.main.Modification;
import modification.managers.FriendManager;

public final class Friend
        extends Command {
    public Friend(String paramString1, String paramString2) {
        super(paramString1, paramString2);
    }

    public void execute(String paramString, String[] paramArrayOfString) {
        if (paramArrayOfString.length > 2) {
            String str2;
            switch (paramArrayOfString[1].toLowerCase()) {
                case "add":
                    if (!paramArrayOfString[2].isEmpty()) {
                        str2 = paramString.substring((paramArrayOfString[0] + paramArrayOfString[1] + paramArrayOfString[2] + "   ").length());
                        if (!str2.isEmpty()) {
                            Modification.FRIEND_MANAGER.add(paramArrayOfString[2], str2);
                            Modification.LOG_UTIL.sendChatMessage("Added §f".concat(paramArrayOfString[2]).concat(" §7with alias §f").concat(str2));
                        }
                    }
                    break;
                case "remove":
                    if (!paramArrayOfString[2].isEmpty()) {
                        str2 = Modification.FRIEND_MANAGER.checkFriendForAlias(paramArrayOfString[2]);
                        if (str2 != null) {
                            Modification.FRIEND_MANAGER.remove(str2);
                            Modification.LOG_UTIL.sendChatMessage("Removed §f".concat(paramArrayOfString[2]).concat(" §7successfully"));
                        }
                    }
                    break;
                case "clearlist":
                    FriendManager.FRIENDS.clear();
                    Modification.LOG_UTIL.sendChatMessage("Cleared friend list successfully!");
            }
            return;
        }
        sendUsage();
    }
}




