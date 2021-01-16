package me.razerboy420.weepcraft.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.util.FileUtils;

public class GuiFile {

   public static void load() {
      List file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Gui.weep");
      Iterator var2 = file.iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         if(!s.startsWith("#")) {
            String name = s.split(":")[0];
            Window w = null;
            Weepcraft.getClick();
            Iterator y = Click.windows.iterator();

            while(y.hasNext()) {
               Window x = (Window)y.next();
               if(x.getTitle().equalsIgnoreCase(name)) {
                  w = x;
               }
            }

            if(w != null) {
               Integer x1 = Integer.valueOf(s.split(":")[1]);
               Integer y1 = Integer.valueOf(s.split(":")[2]);
               boolean open = Boolean.valueOf(s.split(":")[3]).booleanValue();
               boolean pinned = Boolean.valueOf(s.split(":")[4]).booleanValue();
               boolean extended = Boolean.valueOf(s.split(":")[5]).booleanValue();
               w.dragX = x1.intValue();
               w.dragY = y1.intValue();
               w.setOpen(open);
               w.setPinned(pinned);
               w.setExtended(extended);
            }
         }
      }

   }

   public static void save() {
      List file = FileUtils.readFile(Weepcraft.configDir + File.separator + "Gui.weep");
      ArrayList newfile = new ArrayList();
      newfile.add(FileUtils.getDateString());
      Weepcraft.getClick();
      Iterator var3 = Click.windows.iterator();

      while(var3.hasNext()) {
         Window w = (Window)var3.next();
         newfile.add(w.getTitle() + ":" + w.dragX + ":" + w.dragY + ":" + w.isOpen() + ":" + w.isPinned() + ":" + w.isExtended());
      }

      FileUtils.writeFile(Weepcraft.configDir + File.separator + "Gui.weep", newfile);
   }
}
