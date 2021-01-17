package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class StateMap extends StateMapperBase
{
    private final IProperty field_178142_a;
    private final String field_178141_c;
    private final List field_178140_d;
    private static final String __OBFID = "CL_00002476";

    private StateMap(IProperty p_i46210_1_, String p_i46210_2_, List p_i46210_3_)
    {
        this.field_178142_a = p_i46210_1_;
        this.field_178141_c = p_i46210_2_;
        this.field_178140_d = p_i46210_3_;
    }

    protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
    {
        LinkedHashMap var2 = Maps.newLinkedHashMap(p_178132_1_.getProperties());
        String var3;

        if (this.field_178142_a == null)
        {
            var3 = ((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock())).toString();
        }
        else
        {
            var3 = this.field_178142_a.getName((Comparable)var2.remove(this.field_178142_a));
        }

        if (this.field_178141_c != null)
        {
            var3 = var3 + this.field_178141_c;
        }

        Iterator var4 = this.field_178140_d.iterator();

        while (var4.hasNext())
        {
            IProperty var5 = (IProperty)var4.next();
            var2.remove(var5);
        }

        return new ModelResourceLocation(var3, this.func_178131_a(var2));
    }

    StateMap(IProperty p_i46211_1_, String p_i46211_2_, List p_i46211_3_, Object p_i46211_4_)
    {
        this(p_i46211_1_, p_i46211_2_, p_i46211_3_);
    }

    public static class Builder
    {
        private IProperty field_178445_a;
        private String field_178443_b;
        private final List field_178444_c = Lists.newArrayList();
        private static final String __OBFID = "CL_00002474";

        public StateMap.Builder func_178440_a(IProperty p_178440_1_)
        {
            this.field_178445_a = p_178440_1_;
            return this;
        }

        public StateMap.Builder func_178439_a(String p_178439_1_)
        {
            this.field_178443_b = p_178439_1_;
            return this;
        }

        public StateMap.Builder func_178442_a(IProperty ... p_178442_1_)
        {
            Collections.addAll(this.field_178444_c, p_178442_1_);
            return this;
        }

        public StateMap build()
        {
            return new StateMap(this.field_178445_a, this.field_178443_b, this.field_178444_c, null);
        }
    }
}
