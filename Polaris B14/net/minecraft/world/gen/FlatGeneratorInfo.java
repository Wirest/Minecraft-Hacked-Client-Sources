/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class FlatGeneratorInfo
/*     */ {
/*  15 */   private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
/*  16 */   private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
/*     */   
/*     */ 
/*     */   private int biomeToUse;
/*     */   
/*     */ 
/*     */   public int getBiome()
/*     */   {
/*  24 */     return this.biomeToUse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBiome(int p_82647_1_)
/*     */   {
/*  32 */     this.biomeToUse = p_82647_1_;
/*     */   }
/*     */   
/*     */   public Map<String, Map<String, String>> getWorldFeatures()
/*     */   {
/*  37 */     return this.worldFeatures;
/*     */   }
/*     */   
/*     */   public List<FlatLayerInfo> getFlatLayers()
/*     */   {
/*  42 */     return this.flatLayers;
/*     */   }
/*     */   
/*     */   public void func_82645_d()
/*     */   {
/*  47 */     int i = 0;
/*     */     
/*  49 */     for (FlatLayerInfo flatlayerinfo : this.flatLayers)
/*     */     {
/*  51 */       flatlayerinfo.setMinY(i);
/*  52 */       i += flatlayerinfo.getLayerCount();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  58 */     StringBuilder stringbuilder = new StringBuilder();
/*  59 */     stringbuilder.append(3);
/*  60 */     stringbuilder.append(";");
/*     */     
/*  62 */     for (int i = 0; i < this.flatLayers.size(); i++)
/*     */     {
/*  64 */       if (i > 0)
/*     */       {
/*  66 */         stringbuilder.append(",");
/*     */       }
/*     */       
/*  69 */       stringbuilder.append(((FlatLayerInfo)this.flatLayers.get(i)).toString());
/*     */     }
/*     */     
/*  72 */     stringbuilder.append(";");
/*  73 */     stringbuilder.append(this.biomeToUse);
/*     */     
/*  75 */     if (!this.worldFeatures.isEmpty())
/*     */     {
/*  77 */       stringbuilder.append(";");
/*  78 */       int k = 0;
/*     */       
/*  80 */       for (Map.Entry<String, Map<String, String>> entry : this.worldFeatures.entrySet())
/*     */       {
/*  82 */         if (k++ > 0)
/*     */         {
/*  84 */           stringbuilder.append(",");
/*     */         }
/*     */         
/*  87 */         stringbuilder.append(((String)entry.getKey()).toLowerCase());
/*  88 */         Map<String, String> map = (Map)entry.getValue();
/*     */         
/*  90 */         if (!map.isEmpty())
/*     */         {
/*  92 */           stringbuilder.append("(");
/*  93 */           int j = 0;
/*     */           
/*  95 */           for (Map.Entry<String, String> entry1 : map.entrySet())
/*     */           {
/*  97 */             if (j++ > 0)
/*     */             {
/*  99 */               stringbuilder.append(" ");
/*     */             }
/*     */             
/* 102 */             stringbuilder.append((String)entry1.getKey());
/* 103 */             stringbuilder.append("=");
/* 104 */             stringbuilder.append((String)entry1.getValue());
/*     */           }
/*     */           
/* 107 */           stringbuilder.append(")");
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 113 */       stringbuilder.append(";");
/*     */     }
/*     */     
/* 116 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   private static FlatLayerInfo func_180715_a(int p_180715_0_, String p_180715_1_, int p_180715_2_)
/*     */   {
/* 121 */     String[] astring = p_180715_0_ >= 3 ? p_180715_1_.split("\\*", 2) : p_180715_1_.split("x", 2);
/* 122 */     int i = 1;
/* 123 */     int j = 0;
/*     */     
/* 125 */     if (astring.length == 2)
/*     */     {
/*     */       try
/*     */       {
/* 129 */         i = Integer.parseInt(astring[0]);
/*     */         
/* 131 */         if (p_180715_2_ + i >= 256)
/*     */         {
/* 133 */           i = 256 - p_180715_2_;
/*     */         }
/*     */         
/* 136 */         if (i < 0)
/*     */         {
/* 138 */           i = 0;
/*     */         }
/*     */       }
/*     */       catch (Throwable var8)
/*     */       {
/* 143 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 147 */     Block block = null;
/*     */     
/*     */     try
/*     */     {
/* 151 */       String s = astring[(astring.length - 1)];
/*     */       
/* 153 */       if (p_180715_0_ < 3)
/*     */       {
/* 155 */         astring = s.split(":", 2);
/*     */         
/* 157 */         if (astring.length > 1)
/*     */         {
/* 159 */           j = Integer.parseInt(astring[1]);
/*     */         }
/*     */         
/* 162 */         block = Block.getBlockById(Integer.parseInt(astring[0]));
/*     */       }
/*     */       else
/*     */       {
/* 166 */         astring = s.split(":", 3);
/* 167 */         block = astring.length > 1 ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;
/*     */         
/* 169 */         if (block != null)
/*     */         {
/* 171 */           j = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
/*     */         }
/*     */         else
/*     */         {
/* 175 */           block = Block.getBlockFromName(astring[0]);
/*     */           
/* 177 */           if (block != null)
/*     */           {
/* 179 */             j = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
/*     */           }
/*     */         }
/*     */         
/* 183 */         if (block == null)
/*     */         {
/* 185 */           return null;
/*     */         }
/*     */       }
/*     */       
/* 189 */       if (block == Blocks.air)
/*     */       {
/* 191 */         j = 0;
/*     */       }
/*     */       
/* 194 */       if ((j < 0) || (j > 15))
/*     */       {
/* 196 */         j = 0;
/*     */       }
/*     */     }
/*     */     catch (Throwable var9)
/*     */     {
/* 201 */       return null;
/*     */     }
/*     */     
/* 204 */     FlatLayerInfo flatlayerinfo = new FlatLayerInfo(p_180715_0_, i, block, j);
/* 205 */     flatlayerinfo.setMinY(p_180715_2_);
/* 206 */     return flatlayerinfo;
/*     */   }
/*     */   
/*     */   private static List<FlatLayerInfo> func_180716_a(int p_180716_0_, String p_180716_1_)
/*     */   {
/* 211 */     if ((p_180716_1_ != null) && (p_180716_1_.length() >= 1))
/*     */     {
/* 213 */       List<FlatLayerInfo> list = Lists.newArrayList();
/* 214 */       String[] astring = p_180716_1_.split(",");
/* 215 */       int i = 0;
/*     */       String[] arrayOfString1;
/* 217 */       int j = (arrayOfString1 = astring).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*     */         
/* 219 */         FlatLayerInfo flatlayerinfo = func_180715_a(p_180716_0_, s, i);
/*     */         
/* 221 */         if (flatlayerinfo == null)
/*     */         {
/* 223 */           return null;
/*     */         }
/*     */         
/* 226 */         list.add(flatlayerinfo);
/* 227 */         i += flatlayerinfo.getLayerCount();
/*     */       }
/*     */       
/* 230 */       return list;
/*     */     }
/*     */     
/*     */ 
/* 234 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static FlatGeneratorInfo createFlatGeneratorFromString(String p_82651_0_)
/*     */   {
/* 240 */     if (p_82651_0_ == null)
/*     */     {
/* 242 */       return getDefaultFlatGenerator();
/*     */     }
/*     */     
/*     */ 
/* 246 */     String[] astring = p_82651_0_.split(";", -1);
/* 247 */     int i = astring.length == 1 ? 0 : MathHelper.parseIntWithDefault(astring[0], 0);
/*     */     
/* 249 */     if ((i >= 0) && (i <= 3))
/*     */     {
/* 251 */       FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/* 252 */       int j = astring.length == 1 ? 0 : 1;
/* 253 */       List<FlatLayerInfo> list = func_180716_a(i, astring[(j++)]);
/*     */       
/* 255 */       if ((list != null) && (!list.isEmpty()))
/*     */       {
/* 257 */         flatgeneratorinfo.getFlatLayers().addAll(list);
/* 258 */         flatgeneratorinfo.func_82645_d();
/* 259 */         int k = BiomeGenBase.plains.biomeID;
/*     */         
/* 261 */         if ((i > 0) && (astring.length > j))
/*     */         {
/* 263 */           k = MathHelper.parseIntWithDefault(astring[(j++)], k);
/*     */         }
/*     */         
/* 266 */         flatgeneratorinfo.setBiome(k);
/*     */         
/* 268 */         if ((i > 0) && (astring.length > j))
/*     */         {
/* 270 */           String[] astring1 = astring[(j++)].toLowerCase().split(",");
/*     */           String[] arrayOfString1;
/* 272 */           int j = (arrayOfString1 = astring1).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*     */             
/* 274 */             String[] astring2 = s.split("\\(", 2);
/* 275 */             Map<String, String> map = Maps.newHashMap();
/*     */             
/* 277 */             if (astring2[0].length() > 0)
/*     */             {
/* 279 */               flatgeneratorinfo.getWorldFeatures().put(astring2[0], map);
/*     */               
/* 281 */               if ((astring2.length > 1) && (astring2[1].endsWith(")")) && (astring2[1].length() > 1))
/*     */               {
/* 283 */                 String[] astring3 = astring2[1].substring(0, astring2[1].length() - 1).split(" ");
/*     */                 
/* 285 */                 for (int l = 0; l < astring3.length; l++)
/*     */                 {
/* 287 */                   String[] astring4 = astring3[l].split("=", 2);
/*     */                   
/* 289 */                   if (astring4.length == 2)
/*     */                   {
/* 291 */                     map.put(astring4[0], astring4[1]);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 300 */           flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
/*     */         }
/*     */         
/* 303 */         return flatgeneratorinfo;
/*     */       }
/*     */       
/*     */ 
/* 307 */       return getDefaultFlatGenerator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 312 */     return getDefaultFlatGenerator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static FlatGeneratorInfo getDefaultFlatGenerator()
/*     */   {
/* 319 */     FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/* 320 */     flatgeneratorinfo.setBiome(BiomeGenBase.plains.biomeID);
/* 321 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
/* 322 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
/* 323 */     flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
/* 324 */     flatgeneratorinfo.func_82645_d();
/* 325 */     flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
/* 326 */     return flatgeneratorinfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\FlatGeneratorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */