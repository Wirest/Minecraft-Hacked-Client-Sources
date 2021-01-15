package net.minecraft.block.state.pattern;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FactoryBlockPattern
{
    private static final Joiner COMMA_JOIN = Joiner.on(",");
    private final List depth = Lists.newArrayList();
    private final Map symbolMap = Maps.newHashMap();
    private int aisleHeight;
    private int rowWidth;

    private FactoryBlockPattern()
    {
        this.symbolMap.put(' ', Predicates.alwaysTrue());
    }

    public FactoryBlockPattern aisle(String ... aisle)
    {
        if (!ArrayUtils.isEmpty(aisle) && !StringUtils.isEmpty(aisle[0]))
        {
            if (this.depth.isEmpty())
            {
                this.aisleHeight = aisle.length;
                this.rowWidth = aisle[0].length();
            }

            if (aisle.length != this.aisleHeight)
            {
                throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
            }
            else
            {
                String[] var2 = aisle;
                int var3 = aisle.length;

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    String var5 = var2[var4];

                    if (var5.length() != this.rowWidth)
                    {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + var5.length() + ")");
                    }

                    char[] var6 = var5.toCharArray();
                    int var7 = var6.length;

                    for (int var8 = 0; var8 < var7; ++var8)
                    {
                        char var9 = var6[var8];

                        if (!this.symbolMap.containsKey(Character.valueOf(var9)))
                        {
                            this.symbolMap.put(Character.valueOf(var9), (Object)null);
                        }
                    }
                }

                this.depth.add(aisle);
                return this;
            }
        }
        else
        {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static FactoryBlockPattern start()
    {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern where(char symbol, Predicate blockMatcher)
    {
        this.symbolMap.put(Character.valueOf(symbol), blockMatcher);
        return this;
    }

    public BlockPattern build()
    {
        return new BlockPattern(this.makePredicateArray());
    }

    private Predicate[][][] makePredicateArray()
    {
        this.checkMissingPredicates();
        Predicate[][][] var1 = ((Predicate[][][])Array.newInstance(Predicate.class, new int[] {this.depth.size(), this.aisleHeight, this.rowWidth}));

        for (int var2 = 0; var2 < this.depth.size(); ++var2)
        {
            for (int var3 = 0; var3 < this.aisleHeight; ++var3)
            {
                for (int var4 = 0; var4 < this.rowWidth; ++var4)
                {
                    var1[var2][var3][var4] = (Predicate)this.symbolMap.get(Character.valueOf(((String[])this.depth.get(var2))[var3].charAt(var4)));
                }
            }
        }

        return var1;
    }

    private void checkMissingPredicates()
    {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.symbolMap.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (var3.getValue() == null)
            {
                var1.add(var3.getKey());
            }
        }

        if (!var1.isEmpty())
        {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(var1) + " are missing");
        }
    }
}
