// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import java.util.Iterator;
import java.lang.reflect.Array;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.block.state.BlockWorldState;
import com.google.common.base.Predicate;
import java.util.Map;
import java.util.List;
import com.google.common.base.Joiner;

public class FactoryBlockPattern
{
    private static final Joiner COMMA_JOIN;
    private final List<String[]> depth;
    private final Map<Character, Predicate<BlockWorldState>> symbolMap;
    private int aisleHeight;
    private int rowWidth;
    
    static {
        COMMA_JOIN = Joiner.on(",");
    }
    
    private FactoryBlockPattern() {
        this.depth = (List<String[]>)Lists.newArrayList();
        (this.symbolMap = (Map<Character, Predicate<BlockWorldState>>)Maps.newHashMap()).put(' ', Predicates.alwaysTrue());
    }
    
    public FactoryBlockPattern aisle(final String... aisle) {
        if (ArrayUtils.isEmpty(aisle) || StringUtils.isEmpty(aisle[0])) {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
        if (this.depth.isEmpty()) {
            this.aisleHeight = aisle.length;
            this.rowWidth = aisle[0].length();
        }
        if (aisle.length != this.aisleHeight) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
        }
        for (final String s : aisle) {
            if (s.length() != this.rowWidth) {
                throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
            }
            char[] charArray;
            for (int length2 = (charArray = s.toCharArray()).length, j = 0; j < length2; ++j) {
                final char c0 = charArray[j];
                if (!this.symbolMap.containsKey(c0)) {
                    this.symbolMap.put(c0, null);
                }
            }
        }
        this.depth.add(aisle);
        return this;
    }
    
    public static FactoryBlockPattern start() {
        return new FactoryBlockPattern();
    }
    
    public FactoryBlockPattern where(final char symbol, final Predicate<BlockWorldState> blockMatcher) {
        this.symbolMap.put(symbol, blockMatcher);
        return this;
    }
    
    public BlockPattern build() {
        return new BlockPattern(this.makePredicateArray());
    }
    
    private Predicate<BlockWorldState>[][][] makePredicateArray() {
        this.checkMissingPredicates();
        final Predicate[][][] predicate = (Predicate[][][])Array.newInstance(Predicate.class, this.depth.size(), this.aisleHeight, this.rowWidth);
        for (int i = 0; i < this.depth.size(); ++i) {
            for (int j = 0; j < this.aisleHeight; ++j) {
                for (int k = 0; k < this.rowWidth; ++k) {
                    predicate[i][j][k] = this.symbolMap.get(this.depth.get(i)[j].charAt(k));
                }
            }
        }
        return (Predicate<BlockWorldState>[][][])predicate;
    }
    
    private void checkMissingPredicates() {
        final List<Character> list = (List<Character>)Lists.newArrayList();
        for (final Map.Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet()) {
            if (entry.getValue() == null) {
                list.add(entry.getKey());
            }
        }
        if (!list.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + FactoryBlockPattern.COMMA_JOIN.join(list) + " are missing");
        }
    }
}
