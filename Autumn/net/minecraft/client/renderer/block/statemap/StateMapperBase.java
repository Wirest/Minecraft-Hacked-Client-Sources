package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
   protected Map mapStateModelLocations = Maps.newLinkedHashMap();

   public String getPropertyString(Map p_178131_1_) {
      StringBuilder stringbuilder = new StringBuilder();
      Iterator var3 = p_178131_1_.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         if (stringbuilder.length() != 0) {
            stringbuilder.append(",");
         }

         IProperty iproperty = (IProperty)entry.getKey();
         Comparable comparable = (Comparable)entry.getValue();
         stringbuilder.append(iproperty.getName());
         stringbuilder.append("=");
         stringbuilder.append(iproperty.getName(comparable));
      }

      if (stringbuilder.length() == 0) {
         stringbuilder.append("normal");
      }

      return stringbuilder.toString();
   }

   public Map putStateModelLocations(Block blockIn) {
      UnmodifiableIterator var2 = blockIn.getBlockState().getValidStates().iterator();

      while(var2.hasNext()) {
         IBlockState iblockstate = (IBlockState)var2.next();
         this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
      }

      return this.mapStateModelLocations;
   }

   protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);
}
