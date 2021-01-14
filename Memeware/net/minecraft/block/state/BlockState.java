package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.Cartesian;
import net.minecraft.util.MapPopulator;

public class BlockState {
    private static final Joiner COMMA_JOINER = Joiner.on(", ");

    /**
     * Function that converts a Property into it's name.
     */
    private static final Function GET_NAME_FUNC = new Function() {
        private static final String __OBFID = "CL_00002029";

        public String apply(IProperty property) {
            return property == null ? "<NULL>" : property.getName();
        }

        public Object apply(Object p_apply_1_) {
            return this.apply((IProperty) p_apply_1_);
        }
    };
    private final Block block;
    private final ImmutableList properties;
    private final ImmutableList validStates;
    private static final String __OBFID = "CL_00002030";

    public BlockState(Block blockIn, IProperty... properties) {
        this.block = blockIn;
        Arrays.sort(properties, new Comparator() {
            private static final String __OBFID = "CL_00002028";

            public int compare(IProperty left, IProperty right) {
                return left.getName().compareTo(right.getName());
            }

            public int compare(Object p_compare_1_, Object p_compare_2_) {
                return this.compare((IProperty) p_compare_1_, (IProperty) p_compare_2_);
            }
        });
        this.properties = ImmutableList.copyOf(properties);
        LinkedHashMap var3 = Maps.newLinkedHashMap();
        ArrayList var4 = Lists.newArrayList();
        Iterable var5 = Cartesian.cartesianProduct(this.getAllowedValues());
        Iterator var6 = var5.iterator();

        while (var6.hasNext()) {
            List var7 = (List) var6.next();
            Map var8 = MapPopulator.createMap(this.properties, var7);
            BlockState.StateImplemenation var9 = new BlockState.StateImplemenation(blockIn, ImmutableMap.copyOf(var8), null);
            var3.put(var8, var9);
            var4.add(var9);
        }

        var6 = var4.iterator();

        while (var6.hasNext()) {
            BlockState.StateImplemenation var10 = (BlockState.StateImplemenation) var6.next();
            var10.buildPropertyValueTable(var3);
        }

        this.validStates = ImmutableList.copyOf(var4);
    }

    public ImmutableList getValidStates() {
        return this.validStates;
    }

    private List getAllowedValues() {
        ArrayList var1 = Lists.newArrayList();

        for (int var2 = 0; var2 < this.properties.size(); ++var2) {
            var1.add(((IProperty) this.properties.get(var2)).getAllowedValues());
        }

        return var1;
    }

    public IBlockState getBaseState() {
        return (IBlockState) this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection getProperties() {
        return this.properties;
    }

    public String toString() {
        return Objects.toStringHelper(this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
    }

    static class StateImplemenation extends BlockStateBase {
        private final Block block;
        private final ImmutableMap properties;
        private ImmutableTable propertyValueTable;
        private static final String __OBFID = "CL_00002027";

        private StateImplemenation(Block p_i45660_1_, ImmutableMap p_i45660_2_) {
            this.block = p_i45660_1_;
            this.properties = p_i45660_2_;
        }

        public Collection getPropertyNames() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        public Comparable getValue(IProperty property) {
            if (!this.properties.containsKey(property)) {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            } else {
                return (Comparable) property.getValueClass().cast(this.properties.get(property));
            }
        }

        public IBlockState withProperty(IProperty property, Comparable value) {
            if (!this.properties.containsKey(property)) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            } else if (!property.getAllowedValues().contains(value)) {
                throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
            } else {
                return (IBlockState) (this.properties.get(property) == value ? this : (IBlockState) this.propertyValueTable.get(property, value));
            }
        }

        public ImmutableMap getProperties() {
            return this.properties;
        }

        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object p_equals_1_) {
            return this == p_equals_1_;
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            } else {
                HashBasedTable var2 = HashBasedTable.create();
                Iterator var3 = this.properties.keySet().iterator();

                while (var3.hasNext()) {
                    IProperty var4 = (IProperty) var3.next();
                    Iterator var5 = var4.getAllowedValues().iterator();

                    while (var5.hasNext()) {
                        Comparable var6 = (Comparable) var5.next();

                        if (var6 != this.properties.get(var4)) {
                            var2.put(var4, var6, map.get(this.setPropertyValue(var4, var6)));
                        }
                    }
                }

                this.propertyValueTable = ImmutableTable.copyOf(var2);
            }
        }

        private Map setPropertyValue(IProperty property, Comparable value) {
            HashMap var3 = Maps.newHashMap(this.properties);
            var3.put(property, value);
            return var3;
        }

        StateImplemenation(Block p_i45661_1_, ImmutableMap p_i45661_2_, Object p_i45661_3_) {
            this(p_i45661_1_, p_i45661_2_);
        }
    }
}
