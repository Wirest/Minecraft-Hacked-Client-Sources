package saint.comandstuff.commands;

import saint.Saint;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Add extends Command {
   public Add() {
      super("friendadd", "<name alias>", "fadd", "add");
   }

   public void run(String message) {
      String name = message.split(" ")[1];
      String alias = message.substring((message.split(" ")[0] + " " + name + " ").length());
      Saint.getFriendManager().addFriend(name, alias);
      Logger.writeChat("Friend " + name + " added with the alias of " + alias + ".");
      if (Saint.getFileManager().getFileUsingName("friendfile") != null) {
         Saint.getFileManager().getFileUsingName("friendfile").saveFile();
      }

   }
}
