package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.Xray;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;

public class Wallhack extends Module {
   private final List blocks = new ArrayList();

   public Wallhack() {
      super("Wallhack", -9868951, ModManager.Category.WORLD);
      Saint.getCommandManager().getContentList().add(new Command("wallhack", "<block name/id>", new String[]{"whack", "wh"}) {
         public boolean isInteger(String string) {
            try {
               Integer.parseInt(string);
               return true;
            } catch (Exception var3) {
               return false;
            }
         }

         public void run(String message) {
            String[] arguments = message.split(" ");
            if (arguments.length == 1) {
               Wallhack.this.blocks.clear();
               Logger.writeChat("Block list cleared.");
            } else {
               String input = message.substring((arguments[0] + " ").length());
               Block block = null;
               if (this.isInteger(input)) {
                  block = Block.getBlockById(Integer.parseInt(input));
               } else {
                  Iterator var6 = Block.blockRegistry.iterator();

                  while(var6.hasNext()) {
                     Object o = var6.next();
                     Block blockk = (Block)o;
                     String name = blockk.getLocalizedName().replaceAll("tile.", "").replaceAll(".name", "");
                     if (name.toLowerCase().startsWith(input.toLowerCase())) {
                        block = blockk;
                        break;
                     }
                  }
               }

               if (block == null) {
                  Logger.writeChat("Invalid block.");
               } else {
                  if (Wallhack.this.blocks.contains(block)) {
                     Logger.writeChat("No longer wallhacking for [" + block.getLocalizedName() + "]");
                     Wallhack.this.blocks.remove(Wallhack.this.blocks.indexOf(block));
                  } else {
                     Logger.writeChat("Wallhacking for [" + block.getLocalizedName() + "]");
                     Wallhack.this.blocks.add(block);
                  }

                  mc.renderGlobal.loadRenderers();
                  Saint.getFileManager().getFileUsingName("wallhackconfiguration").saveFile();
               }
            }
         }
      });
   }

   public List getBlocks() {
      return this.blocks;
   }

   public void onEnabled() {
      super.onEnabled();
      mc.renderGlobal.loadRenderers();
   }

   public void onDisabled() {
      super.onDisabled();
      mc.renderGlobal.loadRenderers();
   }

   public void onEvent(Event event) {
      if (event instanceof Xray) {
         ((Xray)event).setRay(true);
      }

   }
}
