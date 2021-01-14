package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern {
   private static final Joiner COMMA_JOIN = Joiner.on(",");
   private final List depth = Lists.newArrayList();
   private final Map symbolMap = Maps.newHashMap();
   private int aisleHeight;
   private int rowWidth;

   private FactoryBlockPattern() {
      this.symbolMap.put(' ', Predicates.alwaysTrue());
   }

   public FactoryBlockPattern aisle(String... aisle) {
      if (!ArrayUtils.isEmpty((Object[])aisle) && !StringUtils.isEmpty(aisle[0])) {
         if (this.depth.isEmpty()) {
            this.aisleHeight = aisle.length;
            this.rowWidth = aisle[0].length();
         }

         if (aisle.length != this.aisleHeight) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
         } else {
            String[] var2 = aisle;
            int var3 = aisle.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String s = var2[var4];
               if (s.length() != this.rowWidth) {
                  throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
               }

               char[] var6 = s.toCharArray();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  char c0 = var6[var8];
                  if (!this.symbolMap.containsKey(c0)) {
                     this.symbolMap.put(c0, (Predicate)null);
                  }
               }
            }

            this.depth.add(aisle);
            return this;
         }
      } else {
         throw new IllegalArgumentException("Empty pattern for aisle");
      }
   }

   public static FactoryBlockPattern start() {
      return new FactoryBlockPattern();
   }

   public FactoryBlockPattern where(char symbol, Predicate blockMatcher) {
      this.symbolMap.put(symbol, blockMatcher);
      return this;
   }

   public BlockPattern build() {
      return new BlockPattern(this.makePredicateArray());
   }

   private Predicate[][][] makePredicateArray() {
      this.checkMissingPredicates();
      Predicate[][][] predicate = (Predicate[][][])((Predicate[][][])((Predicate[][][])Array.newInstance(Predicate.class, new int[]{this.depth.size(), this.aisleHeight, this.rowWidth})));

      for(int i = 0; i < this.depth.size(); ++i) {
         for(int j = 0; j < this.aisleHeight; ++j) {
            for(int k = 0; k < this.rowWidth; ++k) {
               predicate[i][j][k] = (Predicate)this.symbolMap.get(((String[])((String[])this.depth.get(i)))[j].charAt(k));
            }
         }
      }

      return predicate;
   }

   private void checkMissingPredicates() {
      List list = Lists.newArrayList();
      Iterator var2 = this.symbolMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         if (entry.getValue() == null) {
            list.add(entry.getKey());
         }
      }

      if (!list.isEmpty()) {
         throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(list) + " are missing");
      }
   }
}
