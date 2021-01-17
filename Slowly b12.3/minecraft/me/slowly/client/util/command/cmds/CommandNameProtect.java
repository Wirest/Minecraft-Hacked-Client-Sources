package me.slowly.client.util.command.cmds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import me.slowly.client.mod.mods.misc.NameProtect;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.command.Command;
import net.minecraft.client.Minecraft;

public class CommandNameProtect extends Command {
   private static String fileDir;

   static {
      fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "Slowly";
   }

   public CommandNameProtect(String[] commands) {
      super(commands);
      this.setArgs(".nameprotect <Name>");
   }

   public void onCmd(String[] args) {
      if (args.length != 2) {
         ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
      } else {
         args[1] = args[1].replace("¡ì", "¡ì");
         NameProtect.name = args[1];
         saveName();
         ClientUtil.sendClientMessage("Changed to " + args[1], ClientNotification.Type.SUCCESS);
         super.onCmd(args);
      }
   }

   public static void saveName() {
      File f = new File(fileDir + "/nameprotect.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         pw.print(NameProtect.name);
         pw.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void loadName() throws IOException {
      File f = new File(fileDir + "/nameprotect.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               String name = String.valueOf(line);
               NameProtect.name = name;
            } catch (Exception var4) {
               ;
            }
         }
      }

   }
}
