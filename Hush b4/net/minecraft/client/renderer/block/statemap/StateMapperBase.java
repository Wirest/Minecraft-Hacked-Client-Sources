// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.Map;

public abstract class StateMapperBase implements IStateMapper
{
    protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations;
    
    public StateMapperBase() {
        this.mapStateModelLocations = (Map<IBlockState, ModelResourceLocation>)Maps.newLinkedHashMap();
    }
    
    public String getPropertyString(final Map<IProperty, Comparable> p_178131_1_) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final Map.Entry<IProperty, Comparable> entry : p_178131_1_.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }
            final IProperty iproperty = entry.getKey();
            final Comparable comparable = entry.getValue();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(iproperty.getName(comparable));
        }
        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }
        return stringbuilder.toString();
    }
    
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(final Block blockIn) {
        for (final IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
            this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
        }
        return this.mapStateModelLocations;
    }
    
    protected abstract ModelResourceLocation getModelResourceLocation(final IBlockState p0);
}
