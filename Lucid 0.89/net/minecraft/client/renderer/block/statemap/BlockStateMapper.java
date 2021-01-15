package net.minecraft.client.renderer.block.statemap;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;

public class BlockStateMapper
{
    private Map blockStateMap = Maps.newIdentityHashMap();
    private Set setBuiltInBlocks = Sets.newIdentityHashSet();

    public void registerBlockStateMapper(Block p_178447_1_, IStateMapper p_178447_2_)
    {
        this.blockStateMap.put(p_178447_1_, p_178447_2_);
    }

    public void registerBuiltInBlocks(Block ... p_178448_1_)
    {
        Collections.addAll(this.setBuiltInBlocks, p_178448_1_);
    }

    public Map putAllStateModelLocations()
    {
        IdentityHashMap var1 = Maps.newIdentityHashMap();
        Iterator var2 = Block.blockRegistry.iterator();

        while (var2.hasNext())
        {
            Block var3 = (Block)var2.next();

            if (!this.setBuiltInBlocks.contains(var3))
            {
                var1.putAll(((IStateMapper)Objects.firstNonNull(this.blockStateMap.get(var3), new DefaultStateMapper())).putStateModelLocations(var3));
            }
        }

        return var1;
    }
}
