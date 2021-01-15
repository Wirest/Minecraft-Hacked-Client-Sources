package saint.protection;

import net.minecraft.client.Minecraft;
import saint.Saint;
import saint.screens.GuiSaintLogin;

public class Protection {
   public static void setup() {
      Minecraft.getMinecraft().displayGuiScreen(new GuiSaintLogin(Saint.getAccount()));
   }
}
