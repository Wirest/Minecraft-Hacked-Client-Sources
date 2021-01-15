package net.minecraft.client.renderer.block.statemap;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper
{
    protected Map mapStateModelLocations = Maps.newLinkedHashMap();

    public String getPropertyString(Map p_178131_1_)
    {
        StringBuilder var2 = new StringBuilder();
        Iterator var3 = p_178131_1_.entrySet().iterator();

        while (var3.hasNext())
        {
            Entry var4 = (Entry)var3.next();

            if (var2.length() != 0)
            {
                var2.append(",");
            }

            IProperty var5 = (IProperty)var4.getKey();
            Comparable var6 = (Comparable)var4.getValue();
            var2.append(var5.getName());
            var2.append("=");
            var2.append(var5.getName(var6));
        }

        if (var2.length() == 0)
        {
            var2.append("normal");
        }

        return var2.toString();
    }

    @Override
	public Map putStateModelLocations(Block p_178130_1_)
    {
        Iterator var2 = p_178130_1_.getBlockState().getValidStates().iterator();

        while (var2.hasNext())
        {
            IBlockState var3 = (IBlockState)var2.next();
            this.mapStateModelLocations.put(var3, this.getModelResourceLocation(var3));
        }

        return this.mapStateModelLocations;
    }

    protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);
}
