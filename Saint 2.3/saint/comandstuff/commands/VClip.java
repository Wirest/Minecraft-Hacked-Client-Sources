package saint.comandstuff.commands;

import net.minecraft.client.Minecraft;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class VClip extends Command {
   private final Minecraft mc = Minecraft.getMinecraft();

   public VClip() {
      super("vclip", "<blocks>", "vc");
   }

   public void run(String message) {
      double blocks = Double.parseDouble(message.split(" ")[1]);
      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + blocks, this.mc.thePlayer.posZ);
      Logger.writeChat("Teleported \"" + blocks + "\" blocks.");
   }
}
