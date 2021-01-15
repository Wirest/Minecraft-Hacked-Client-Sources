package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import saint.Saint;
import saint.clickgui.element.panel.Panel;
import saint.filestuff.BasicFile;

public class GuiConfiguration extends BasicFile {
   public GuiConfiguration() {
      super("guiconfiguration");
   }

   public void loadFile() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         while(true) {
            String[] arguments;
            do {
               String line;
               if ((line = reader.readLine()) == null) {
                  reader.close();
                  return;
               }

               arguments = line.split(":");
            } while(arguments.length != 4);

            Iterator var5 = Saint.getClickGui().getPanels().iterator();

            while(var5.hasNext()) {
               Panel panel = (Panel)var5.next();
               if (panel != null && arguments[0].equalsIgnoreCase(panel.getTitle())) {
                  panel.setX(Integer.parseInt(arguments[1]));
                  panel.setY(Integer.parseInt(arguments[2]));
                  panel.setOpen(Boolean.parseBoolean(arguments[3]));
               }
            }
         }
      } catch (FileNotFoundException var6) {
         var6.printStackTrace();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var3 = Saint.getClickGui().getPanels().iterator();

         while(var3.hasNext()) {
            Panel panel = (Panel)var3.next();
            if (panel != null) {
               writer.write(panel.getTitle() + ":" + panel.getX() + ":" + panel.getY() + ":" + panel.getOpen());
               writer.newLine();
            }
         }

         writer.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
