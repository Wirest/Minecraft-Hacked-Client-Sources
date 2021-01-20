package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBase implements IBlockState {
   private static final Joiner field_177234_a = Joiner.on(',');
   private static final Function field_177233_b = new Function() {
      private static final String __OBFID = "CL_00002031";

      public String apply(Entry p_apply_1_) {
         if(p_apply_1_ == null) {
            return "<NULL>";
         } else {
            IProperty iproperty = (IProperty)p_apply_1_.getKey();
            return iproperty.func_177701_a() + "=" + iproperty.func_177702_a((Comparable)p_apply_1_.getValue());
         }
      }

      public Object apply(Object p_apply_1_) {
         return this.apply((Entry)p_apply_1_);
      }
   };
   private static final String __OBFID = "CL_00002032";
   private int blockId = -1;
   private int blockStateId = -1;
   private int metadata = -1;
   private ResourceLocation blockLocation = null;

   public int getBlockId() {
      if(this.blockId < 0) {
         this.blockId = Block.func_149682_b(this.func_177230_c());
      }

      return this.blockId;
   }

   public int getBlockStateId() {
      if(this.blockStateId < 0) {
         this.blockStateId = Block.func_176210_f(this);
      }

      return this.blockStateId;
   }

   public int getMetadata() {
      if(this.metadata < 0) {
         this.metadata = this.func_177230_c().func_176201_c(this);
      }

      return this.metadata;
   }

   public ResourceLocation getBlockLocation() {
      if(this.blockLocation == null) {
         this.blockLocation = (ResourceLocation)Block.field_149771_c.func_177774_c(this.func_177230_c());
      }

      return this.blockLocation;
   }

   public IBlockState func_177231_a(IProperty p_177231_1_) {
      return this.func_177226_a(p_177231_1_, (Comparable)func_177232_a(p_177231_1_.func_177700_c(), this.func_177229_b(p_177231_1_)));
   }

   protected static Object func_177232_a(Collection p_177232_0_, Object p_177232_1_) {
      Iterator iterator = p_177232_0_.iterator();

      while(iterator.hasNext()) {
         if(iterator.next().equals(p_177232_1_)) {
            if(iterator.hasNext()) {
               return iterator.next();
            }

            return p_177232_0_.iterator().next();
         }
      }

      return iterator.next();
   }

   public String toString() {
      StringBuilder stringbuilder = new StringBuilder();
      stringbuilder.append(Block.field_149771_c.func_177774_c(this.func_177230_c()));
      if(!this.func_177228_b().isEmpty()) {
         stringbuilder.append("[");
         field_177234_a.appendTo(stringbuilder, Iterables.transform(this.func_177228_b().entrySet(), field_177233_b));
         stringbuilder.append("]");
      }

      return stringbuilder.toString();
   }

   public ImmutableTable<IProperty, Comparable, IBlockState> getPropertyValueTable() {
      return null;
   }
}
