package saint.modstuff.modules;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Spammer extends Module {
   public Value delay = new Value("spammer_delay", 2000L);
   public Value filemode = new Value("spammer_file", false);
   public Value message = new Value("spammer_message", "");
   public List messages = new CopyOnWriteArrayList();
   public TimeHelper time = new TimeHelper();
   public Random rand = new Random();

   public Spammer() {
      super("Spammer", -8092269, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("spam", "<message/file/delay>", new String[]{"s"}) {
         public void run(String message) {
            String[] arguments = message.split(" ");
            String arg = arguments[1];
            if (arg.equalsIgnoreCase("message")) {
               Spammer.this.message.setValueState(message.substring((".spam " + arg + " ").length()));
               Logger.writeChat("Spammer Message set to: \"" + (String)Spammer.this.message.getValueState() + "\"");
            } else if (arg.equalsIgnoreCase("file")) {
               Spammer.this.filemode.setValueState(!(Boolean)Spammer.this.filemode.getValueState());
               Logger.writeChat("Spammer will " + ((Boolean)Spammer.this.filemode.getValueState() ? "now" : "no longer") + " use messages in the \"spam.txt\" file.");
            } else if (arg.equalsIgnoreCase("delay")) {
               if (message.split(" ")[2].equalsIgnoreCase("-d")) {
                  Spammer.this.delay.setValueState((Long)Spammer.this.delay.getDefaultValue());
               } else {
                  Spammer.this.delay.setValueState(Long.parseLong(arguments[2]));
               }

               if ((Long)Spammer.this.delay.getValueState() < 500L) {
                  Spammer.this.delay.setValueState(500L);
               }

               Logger.writeChat("Spammer Delay set to: " + Spammer.this.delay.getValueState());
            } else {
               Logger.writeChat("Invalid option! Valid options: message, file, delay");
            }

         }
      });
   }

   public List getMessages() {
      return this.messages;
   }

   public void onEnabled() {
      super.onEnabled();
      if (this.messages.isEmpty() && Saint.getFileManager().getFileUsingName("spam") != null) {
         Saint.getFileManager().getFileUsingName("spam").loadFile();
      }

   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick) {
         if (mc.thePlayer == null || mc.theWorld == null) {
            return;
         }

         if (this.time.hasReached((Long)this.delay.getValueState())) {
            Random randomNum = new Random();

            for(int idx = 1; idx <= 10; ++idx) {
               int var4 = randomNum.nextInt(100);
            }

            String msg = "" + randomNum;
            if ((Boolean)this.filemode.getValueState()) {
               if (this.messages.isEmpty()) {
                  Saint.getFileManager().getFileUsingName("spam").loadFile();
                  return;
               }

               if (!this.messages.isEmpty() && Saint.getFileManager().getFileUsingName("spam") != null) {
                  msg = (String)this.messages.get(0);
                  this.messages.remove(this.messages.get(0));
               }
            } else {
               if (((String)this.message.getValueState()).equals("")) {
                  Logger.writeChat("You need to set a spam message! Type \".spam message <message>\"");
                  this.toggle();
                  return;
               }

               msg = (String)this.message.getValueState();
            }

            mc.thePlayer.sendChatMessage(msg + ((Boolean)this.filemode.getValueState() ? "" : this.rand.nextInt(9999)));
            this.time.reset();
         }
      }

   }
}
