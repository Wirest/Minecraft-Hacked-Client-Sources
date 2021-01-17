// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import java.util.Iterator;
import com.google.common.base.Objects;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.block.Block;
import java.util.Map;

public class BlockStateMapper
{
    private Map<Block, IStateMapper> blockStateMap;
    private Set<Block> setBuiltInBlocks;
    
    public BlockStateMapper() {
        this.blockStateMap = (Map<Block, IStateMapper>)Maps.newIdentityHashMap();
        this.setBuiltInBlocks = Sets.newIdentityHashSet();
    }
    
    public void registerBlockStateMapper(final Block p_178447_1_, final IStateMapper p_178447_2_) {
        this.blockStateMap.put(p_178447_1_, p_178447_2_);
    }
    
    public void registerBuiltInBlocks(final Block... p_178448_1_) {
        Collections.addAll(this.setBuiltInBlocks, p_178448_1_);
    }
    
    public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
        final Map<IBlockState, ModelResourceLocation> map = (Map<IBlockState, ModelResourceLocation>)Maps.newIdentityHashMap();
        for (final Block block : Block.blockRegistry) {
            if (!this.setBuiltInBlocks.contains(block)) {
                map.putAll(Objects.firstNonNull(this.blockStateMap.get(block), new DefaultStateMapper()).putStateModelLocations(block));
            }
        }
        return map;
    }
}
