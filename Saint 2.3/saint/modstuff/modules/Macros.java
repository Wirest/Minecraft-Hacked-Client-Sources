package saint.modstuff.modules;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.input.Keyboard;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.EventChatSent;
import saint.eventstuff.events.PressedKey;
import saint.modstuff.Module;
import saint.modstuff.modules.addons.Macro;
import saint.utilities.Logger;

public class Macros extends Module {
   private final List macros = new CopyOnWriteArrayList();

   public Macros() {
      super("Macros");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("macroadd", "<key> <command>", new String[]{"madd", "ma"}) {
         public void run(String message) {
            String[] arguments = message.split(" ");
            int key = Keyboard.getKeyIndex(arguments[1].toUpperCase());
            if (key == 0) {
               Logger.writeChat("You can't macro the key \"NONE\".");
            } else {
               String command = message.substring((arguments[0] + " " + arguments[1] + " ").length());
               if (command.startsWith(".")) {
                  command = command.substring(1);
               }

               Iterator var6 = Macros.this.macros.iterator();

               while(var6.hasNext()) {
                  Macro macro = (Macro)var6.next();
                  if (macro.getKey() == key) {
                     Macros.this.macros.remove(macro);
                  }
               }

               Macros.this.macros.add(new Macro(command, key));
               Saint.getFileManager().getFileUsingName("macros").saveFile();
               Logger.writeChat("Macro \"" + Keyboard.getKeyName(key) + "\" added with command \"" + command + "\".");
            }
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("macrodel", "<key>", new String[]{"mdel", "md"}) {
         public void run(String message) {
            int key = Keyboard.getKeyIndex(message.split(" ")[1].toUpperCase());
            if (key == 0) {
               Logger.writeChat("You can't macro the key \"NONE\".");
            } else {
               boolean found = false;
               Iterator var5 = Macros.this.macros.iterator();

               while(var5.hasNext()) {
                  Macro macro = (Macro)var5.next();
                  if (key == macro.getKey()) {
                     Macros.this.macros.remove(macro);
                     Logger.writeChat("Macro \"" + Keyboard.getKeyName(key) + "\" removed.");
                     Saint.getFileManager().getFileUsingName("macros").saveFile();
                     found = true;
                     break;
                  }
               }

               if (!found) {
                  Logger.writeChat("Macro \"" + Keyboard.getKeyName(key) + "\" not found.");
               }

            }
         }
      });
   }

   public final List getMacros() {
      return this.macros;
   }

   public void onEvent(Event event) {
      if (event instanceof PressedKey) {
         PressedKey pressed = (PressedKey)event;
         Iterator var4 = this.macros.iterator();

         while(var4.hasNext()) {
            Macro macro = (Macro)var4.next();
            if (pressed.getKey() == macro.getKey()) {
               EventChatSent sent = new EventChatSent("." + macro.getCommand());
               Saint.getEventManager().hook(sent);
               sent.checkForCommands();
            }
         }
      }

   }
}
