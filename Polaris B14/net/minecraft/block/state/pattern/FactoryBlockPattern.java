/*     */ package net.minecraft.block.state.pattern;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.state.BlockWorldState;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class FactoryBlockPattern
/*     */ {
/*  18 */   private static final Joiner COMMA_JOIN = Joiner.on(",");
/*  19 */   private final List<String[]> depth = Lists.newArrayList();
/*  20 */   private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
/*     */   private int aisleHeight;
/*     */   private int rowWidth;
/*     */   
/*     */   private FactoryBlockPattern()
/*     */   {
/*  26 */     this.symbolMap.put(Character.valueOf(' '), Predicates.alwaysTrue());
/*     */   }
/*     */   
/*     */   public FactoryBlockPattern aisle(String... aisle)
/*     */   {
/*  31 */     if ((!ArrayUtils.isEmpty(aisle)) && (!StringUtils.isEmpty(aisle[0])))
/*     */     {
/*  33 */       if (this.depth.isEmpty())
/*     */       {
/*  35 */         this.aisleHeight = aisle.length;
/*  36 */         this.rowWidth = aisle[0].length();
/*     */       }
/*     */       
/*  39 */       if (aisle.length != this.aisleHeight)
/*     */       {
/*  41 */         throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
/*     */       }
/*     */       
/*     */       String[] arrayOfString;
/*  45 */       int j = (arrayOfString = aisle).length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
/*     */         
/*  47 */         if (s.length() != this.rowWidth)
/*     */         {
/*  49 */           throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
/*     */         }
/*     */         char[] arrayOfChar;
/*  52 */         int m = (arrayOfChar = s.toCharArray()).length; for (int k = 0; k < m; k++) { char c0 = arrayOfChar[k];
/*     */           
/*  54 */           if (!this.symbolMap.containsKey(Character.valueOf(c0)))
/*     */           {
/*  56 */             this.symbolMap.put(Character.valueOf(c0), null);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  61 */       this.depth.add(aisle);
/*  62 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  67 */     throw new IllegalArgumentException("Empty pattern for aisle");
/*     */   }
/*     */   
/*     */ 
/*     */   public static FactoryBlockPattern start()
/*     */   {
/*  73 */     return new FactoryBlockPattern();
/*     */   }
/*     */   
/*     */   public FactoryBlockPattern where(char symbol, Predicate<BlockWorldState> blockMatcher)
/*     */   {
/*  78 */     this.symbolMap.put(Character.valueOf(symbol), blockMatcher);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public BlockPattern build()
/*     */   {
/*  84 */     return new BlockPattern(makePredicateArray());
/*     */   }
/*     */   
/*     */   private Predicate<BlockWorldState>[][][] makePredicateArray()
/*     */   {
/*  89 */     checkMissingPredicates();
/*  90 */     Predicate[][][] predicate = (Predicate[][][])Array.newInstance(Predicate.class, new int[] { this.depth.size(), this.aisleHeight, this.rowWidth });
/*     */     
/*  92 */     for (int i = 0; i < this.depth.size(); i++)
/*     */     {
/*  94 */       for (int j = 0; j < this.aisleHeight; j++)
/*     */       {
/*  96 */         for (int k = 0; k < this.rowWidth; k++)
/*     */         {
/*  98 */           predicate[i][j][k] = ((Predicate)this.symbolMap.get(Character.valueOf(((String[])this.depth.get(i))[j].charAt(k))));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 103 */     return predicate;
/*     */   }
/*     */   
/*     */   private void checkMissingPredicates()
/*     */   {
/* 108 */     List<Character> list = Lists.newArrayList();
/*     */     
/* 110 */     for (Map.Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet())
/*     */     {
/* 112 */       if (entry.getValue() == null)
/*     */       {
/* 114 */         list.add((Character)entry.getKey());
/*     */       }
/*     */     }
/*     */     
/* 118 */     if (!list.isEmpty())
/*     */     {
/* 120 */       throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(list) + " are missing");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\pattern\FactoryBlockPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */