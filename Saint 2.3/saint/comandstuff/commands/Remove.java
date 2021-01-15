package saint.comandstuff.commands;

import saint.Saint;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Remove extends Command {
   public Remove() {
      super("friendremove", "<name>", "fremove", "fr");
   }

   public void run(String message) {
      String name = message.split(" ")[1];
      Saint.getFriendManager().removeFriend(name);
      Logger.writeChat("Friend " + name + " has been removed.");
      Saint.getFileManager().getFileUsingName("friendfile").saveFile();
   }
}
