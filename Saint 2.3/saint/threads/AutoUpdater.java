package saint.threads;

import java.io.File;
import net.minecraft.client.Minecraft;
import saint.screens.GuiUpdating;
import saint.utilities.FileHelper;

public class AutoUpdater extends Thread {
   private String url;
   private String percentage = "";

   public AutoUpdater(String url) {
      this.url = url;
   }

   public void run() {
      GuiUpdating.isDeleting = true;
      File file = new File(Minecraft.getMinecraft().mcDataDir + "/versions" + "/Saint" + "/Saint.jar");
      if (file.exists()) {
         file.delete();
      }

      File directory = new File(Minecraft.getMinecraft().mcDataDir + "/versions", "Saint");
      if (!directory.exists()) {
         directory.mkdir();
      }

      GuiUpdating.isDownloading = true;
      FileHelper.download(this.url, Minecraft.getMinecraft().mcDataDir + "/versions/Saint/Saint.jar");
      this.percentage = FileHelper.percentage;
      GuiUpdating.isDone = true;
   }

   public String getPercentage() {
      return this.percentage;
   }
}
