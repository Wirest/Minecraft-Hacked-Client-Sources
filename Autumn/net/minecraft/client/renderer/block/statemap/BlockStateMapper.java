package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;

public class BlockStateMapper {
   private Map blockStateMap = Maps.newIdentityHashMap();
   private Set setBuiltInBlocks = Sets.newIdentityHashSet();

   public void registerBlockStateMapper(Block p_178447_1_, IStateMapper p_178447_2_) {
      this.blockStateMap.put(p_178447_1_, p_178447_2_);
   }

   public void registerBuiltInBlocks(Block... p_178448_1_) {
      Collections.addAll(this.setBuiltInBlocks, p_178448_1_);
   }

   public Map putAllStateModelLocations() {
      Map map = Maps.newIdentityHashMap();
      Iterator var2 = Block.blockRegistry.iterator();

      while(var2.hasNext()) {
         Block block = (Block)var2.next();
         if (!this.setBuiltInBlocks.contains(block)) {
            map.putAll(((IStateMapper)Objects.firstNonNull(this.blockStateMap.get(block), new DefaultStateMapper())).putStateModelLocations(block));
         }
      }

      return map;
   }
}
