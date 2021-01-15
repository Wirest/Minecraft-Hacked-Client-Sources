package saint.wallhackstuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.block.Block;
import saint.utilities.ListManager;

public class WallhackManager extends ListManager {
   public Block getBlockUsingName(String name) {
      if (this.getContentList() == null) {
         return null;
      } else {
         Iterator var3 = this.getContentList().iterator();

         while(var3.hasNext()) {
            Block block = (Block)var3.next();
            if (Block.blockRegistry.getNameForObject(block).toString().equalsIgnoreCase(name)) {
               return block;
            }
         }

         return null;
      }
   }

   public Block getBlockUsingBlock(Block block) {
      if (this.getContentList() == null) {
         return null;
      } else {
         Iterator var3 = this.getContentList().iterator();

         while(var3.hasNext()) {
            Block bl = (Block)var3.next();
            if (bl == block) {
               return block;
            }
         }

         return null;
      }
   }

   public boolean doesListContain(Block block) {
      return this.getBlockUsingBlock(block) != null;
   }

   public void setup() {
      this.contents = new ArrayList();
      Collections.sort(this.contents, new Comparator() {
         public int compare(Block block1, Block block2) {
            return block1.getLocalizedName().compareTo(block2.getLocalizedName());
         }
      });
   }
}
