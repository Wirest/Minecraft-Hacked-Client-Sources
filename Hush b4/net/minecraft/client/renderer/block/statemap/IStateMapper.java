// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.block.Block;

public interface IStateMapper
{
    Map<IBlockState, ModelResourceLocation> putStateModelLocations(final Block p0);
}
