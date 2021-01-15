package saint.eventstuff.events;

import java.util.Iterator;
import saint.Saint;
import saint.comandstuff.Command;
import saint.comandstuff.commands.Ghost;
import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;
import saint.utilities.Logger;

public final class EventChatSent extends Event implements Cancellable {
   private boolean cancel;
   private String message;

   public EventChatSent(String message) {
      this.message = message;
   }

   public void checkForCommands() {
      if (this.message.startsWith(Ghost.shouldGhost ? "---" : ".")) {
         Iterator var2 = Saint.getCommandManager().getContentList().iterator();

         while(true) {
            while(var2.hasNext()) {
               Command command = (Command)var2.next();
               if (this.message.split(" ")[0].equalsIgnoreCase((Ghost.shouldGhost ? "---" : ".") + command.getCommand())) {
                  try {
                     command.run(this.message);
                  } catch (Exception var9) {
                     Logger.writeChat("Wrong arguments! " + command.getCommand() + " " + command.getArguments());
                  }

                  this.cancel = true;
               } else {
                  String[] var6;
                  int var5 = (var6 = command.getAliases()).length;

                  for(int var4 = 0; var4 < var5; ++var4) {
                     String alias = var6[var4];
                     if (this.message.split(" ")[0].equalsIgnoreCase((Ghost.shouldGhost ? "---" : ".") + alias)) {
                        try {
                           command.run(this.message);
                        } catch (Exception var8) {
                           Logger.writeChat("Wrong arguments! " + alias + " " + command.getArguments());
                        }

                        this.cancel = true;
                     }
                  }
               }
            }

            if (!this.cancel) {
               Logger.writeChat("Command \"" + this.message + "\" was not found!");
               this.cancel = true;
            }
            break;
         }
      }

   }

   public String getMessage() {
      return this.message;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
