package saint.comandstuff.commands;

import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Timer extends Command {
   public Timer() {
      super("timer", "<timerspeed>");
   }

   public void run(String message) {
      float speed = Float.parseFloat(message.split(" ")[1]);
      if (speed >= 1.0F) {
         mc.timer.timerSpeed = speed;
         Logger.writeChat("Minecraft Timer Speed set to: " + speed + ".");
      } else if (speed < 1.0F) {
         mc.timer.timerSpeed = 1.0F;
         Logger.writeChat("Minecraft Timer Speed set to: 1.0.");
      }

   }
}
