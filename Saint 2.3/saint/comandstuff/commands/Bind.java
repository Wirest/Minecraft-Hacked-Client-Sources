package saint.comandstuff.commands;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import saint.Saint;
import saint.comandstuff.Command;
import saint.modstuff.Module;
import saint.utilities.Logger;

public class Bind extends Command {
   public Bind() {
      super("bind", "<mod name> <key>");
   }

   public void run(String message) {
      Module mod = Saint.getModuleManager().getModuleUsingName(message.split(" ")[1]);
      if (mod == null) {
         try {
            Minecraft.getMinecraft().thePlayer.playSound("random.anvil_land", 1.0F, 1.0F);
         } catch (Exception var5) {
         }

         Logger.writeChat("Module \"" + message.split(" ")[1] + "\" wasn't found!");
      } else {
         mod.setKeybind(Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()));

         try {
            Minecraft.getMinecraft().thePlayer.playSound("random.anvil_use", 1.0F, 1.0F);
         } catch (Exception var4) {
         }

         Logger.writeChat("Module \"" + mod.getName() + "\" bound to: " + Keyboard.getKeyName(mod.getKeybind()));
      }

      if (Saint.getFileManager().getFileUsingName("moduleconfiguration") != null) {
         Saint.getFileManager().getFileUsingName("moduleconfiguration").saveFile();
      }

   }
}
