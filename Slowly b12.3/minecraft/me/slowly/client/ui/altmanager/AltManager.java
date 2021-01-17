package me.slowly.client.ui.altmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

public class AltManager {
   public static ArrayList altList = new ArrayList();
   public static ArrayList guiSlotList = new ArrayList();
   public static final File altFile;

   static {
      altFile = new File(Minecraft.getMinecraft().mcDataDir + "/" + "Slowly" + "/alts.txt");
   }

   public static void saveAltsToFile() {
      try {
         PrintWriter writer = new PrintWriter(altFile);
         Iterator var2 = guiSlotList.iterator();

         while(var2.hasNext()) {
            GuiAltSlot slot = (GuiAltSlot)var2.next();
            writer.write(slot.getUsername() + ":" + slot.getPassword() + "\n");
         }

         writer.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void loadAltsFromFile() {
      guiSlotList.clear();

      try {
         if (!altFile.exists()) {
            altFile.createNewFile();
         } else {
            @SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(new FileReader(altFile));

            String line;
            while((line = bufferedReader.readLine()) != null) {
               String[] alt = line.split(":");
               if (alt.length >= 2) {
                  String username = alt[0];
                  String password = alt[1];
                  guiSlotList.add(new GuiAltSlot(username, password));
               }
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
