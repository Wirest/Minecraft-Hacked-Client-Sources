package net.minecraft.client.renderer.block.statemap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class StateMap extends StateMapperBase
{
    private final IProperty property;
    private final String suffix;
    private final List listProperties;

    private StateMap(IProperty propertyIn, String suffixIn, List listPropertiesIn)
    {
        this.property = propertyIn;
        this.suffix = suffixIn;
        this.listProperties = listPropertiesIn;
    }

    @Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        LinkedHashMap var2 = Maps.newLinkedHashMap(state.getProperties());
        String var3;

        if (this.property == null)
        {
            var3 = ((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock())).toString();
        }
        else
        {
            var3 = this.property.getName((Comparable)var2.remove(this.property));
        }

        if (this.suffix != null)
        {
            var3 = var3 + this.suffix;
        }

        Iterator var4 = this.listProperties.iterator();

        while (var4.hasNext())
        {
            IProperty var5 = (IProperty)var4.next();
            var2.remove(var5);
        }

        return new ModelResourceLocation(var3, this.getPropertyString(var2));
    }

    StateMap(IProperty p_i46211_1_, String p_i46211_2_, List p_i46211_3_, Object p_i46211_4_)
    {
        this(p_i46211_1_, p_i46211_2_, p_i46211_3_);
    }

    public static class Builder
    {
        private IProperty builderProperty;
        private String builderSuffix;
        private final List builderListProperties = Lists.newArrayList();

        public StateMap.Builder setProperty(IProperty builderPropertyIn)
        {
            this.builderProperty = builderPropertyIn;
            return this;
        }

        public StateMap.Builder setBuilderSuffix(String builderSuffixIn)
        {
            this.builderSuffix = builderSuffixIn;
            return this;
        }

        public StateMap.Builder addPropertiesToIgnore(IProperty ... p_178442_1_)
        {
            Collections.addAll(this.builderListProperties, p_178442_1_);
            return this;
        }

        public StateMap build()
        {
            return new StateMap(this.builderProperty, this.builderSuffix, this.builderListProperties, null);
        }
    }
}
