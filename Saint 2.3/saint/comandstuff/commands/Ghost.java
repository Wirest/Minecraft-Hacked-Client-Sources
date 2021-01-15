package saint.comandstuff.commands;

import saint.Saint;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Ghost extends Command {
   public static boolean shouldGhost = false;

   public Ghost() {
      super("screensharemode", "<boolean>", "ghostmode", "ssm");
   }

   public void run(String message) {
      boolean b = Boolean.parseBoolean(message.split(" ")[1]);
      shouldGhost = b;
      if (b) {
         Logger.writeChat("Screen Share Mode activated! \nYour command prefix has been changed to '---'!");
      } else if (!b) {
         Logger.writeChat("Screen Share Mode deactivated! \nYour command prefix has been changed to '.'!");
      }

      Saint.getFileManager().getFileUsingName("ghostmodefile").saveFile();
   }
}
