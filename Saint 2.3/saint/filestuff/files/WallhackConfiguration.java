package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.block.Block;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.modstuff.modules.Wallhack;

public class WallhackConfiguration extends BasicFile {
   public WallhackConfiguration() {
      super("wallhackconfiguration");
   }

   public void loadFile() {
      try {
         Wallhack wh = (Wallhack)Saint.getModuleManager().getModuleUsingName("wallhack");
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
            } while(arguments.length != 2);

            Iterator var6 = Block.blockRegistry.iterator();

            while(var6.hasNext()) {
               Object o = var6.next();
               Block bl = (Block)o;
               if (arguments[0].equalsIgnoreCase(String.valueOf(Block.getIdFromBlock(bl)))) {
                  wh.getBlocks().add(bl);
               }
            }
         }
      } catch (FileNotFoundException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         Wallhack wh = (Wallhack)Saint.getModuleManager().getModuleUsingName("wallhack");
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var4 = wh.getBlocks().iterator();

         while(var4.hasNext()) {
            Block block = (Block)var4.next();
            if (block != null) {
               writer.write(String.valueOf(Block.getIdFromBlock(block)) + ":" + "kewl");
               writer.newLine();
            }
         }

         writer.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }
}
