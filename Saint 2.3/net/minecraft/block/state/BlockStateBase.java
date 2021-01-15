package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract class BlockStateBase implements IBlockState {
   private static final Joiner COMMA_JOINER = Joiner.on(',');
   private static final Function field_177233_b = new Function() {
      private static final String __OBFID = "CL_00002031";

      public String func_177225_a(Entry p_177225_1_) {
         if (p_177225_1_ == null) {
            return "<NULL>";
         } else {
            IProperty var2 = (IProperty)p_177225_1_.getKey();
            return var2.getName() + "=" + var2.getName((Comparable)p_177225_1_.getValue());
         }
      }

      public Object apply(Object p_apply_1_) {
         return this.func_177225_a((Entry)p_apply_1_);
      }
   };
   private static final String __OBFID = "CL_00002032";

   public IBlockState cycleProperty(IProperty property) {
      return this.withProperty(property, (Comparable)cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
   }

   protected static Object cyclePropertyValue(Collection values, Object currentValue) {
      Iterator var2 = values.iterator();

      while(var2.hasNext()) {
         if (var2.next().equals(currentValue)) {
            if (var2.hasNext()) {
               return var2.next();
            }

            return values.iterator().next();
         }
      }

      return var2.next();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(Block.blockRegistry.getNameForObject(this.getBlock()));
      if (!this.getProperties().isEmpty()) {
         var1.append("[");
         COMMA_JOINER.appendTo(var1, Iterables.transform(this.getProperties().entrySet(), field_177233_b));
         var1.append("]");
      }

      return var1.toString();
   }
}
