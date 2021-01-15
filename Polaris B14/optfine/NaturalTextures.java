/*     */ package optfine;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class NaturalTextures
/*     */ {
/*  15 */   private static NaturalProperties[] propertiesByIndex = new NaturalProperties[0];
/*     */   
/*     */   public static void update()
/*     */   {
/*  19 */     propertiesByIndex = new NaturalProperties[0];
/*     */     
/*  21 */     if (Config.isNaturalTextures())
/*     */     {
/*  23 */       String s = "optifine/natural.properties";
/*     */       
/*     */       try
/*     */       {
/*  27 */         ResourceLocation resourcelocation = new ResourceLocation(s);
/*     */         
/*  29 */         if (!Config.hasResource(resourcelocation))
/*     */         {
/*  31 */           Config.dbg("NaturalTextures: configuration \"" + s + "\" not found");
/*  32 */           propertiesByIndex = makeDefaultProperties();
/*  33 */           return;
/*     */         }
/*     */         
/*  36 */         InputStream inputstream = Config.getResourceStream(resourcelocation);
/*  37 */         ArrayList arraylist = new ArrayList(256);
/*  38 */         String s1 = Config.readInputStream(inputstream);
/*  39 */         inputstream.close();
/*  40 */         String[] astring = Config.tokenize(s1, "\n\r");
/*  41 */         Config.dbg("Natural Textures: Parsing configuration \"" + s + "\"");
/*  42 */         TextureMap texturemap = TextureUtils.getTextureMapBlocks();
/*     */         
/*  44 */         for (int i = 0; i < astring.length; i++)
/*     */         {
/*  46 */           String s2 = astring[i].trim();
/*     */           
/*  48 */           if (!s2.startsWith("#"))
/*     */           {
/*  50 */             String[] astring1 = Config.tokenize(s2, "=");
/*     */             
/*  52 */             if (astring1.length != 2)
/*     */             {
/*  54 */               Config.warn("Natural Textures: Invalid \"" + s + "\" line: " + s2);
/*     */             }
/*     */             else
/*     */             {
/*  58 */               String s3 = astring1[0].trim();
/*  59 */               String s4 = astring1[1].trim();
/*  60 */               TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe("minecraft:blocks/" + s3);
/*     */               
/*  62 */               if (textureatlassprite == null)
/*     */               {
/*  64 */                 Config.warn("Natural Textures: Texture not found: \"" + s + "\" line: " + s2);
/*     */               }
/*     */               else
/*     */               {
/*  68 */                 int j = textureatlassprite.getIndexInMap();
/*     */                 
/*  70 */                 if (j < 0)
/*     */                 {
/*  72 */                   Config.warn("Natural Textures: Invalid \"" + s + "\" line: " + s2);
/*     */                 }
/*     */                 else
/*     */                 {
/*  76 */                   NaturalProperties naturalproperties = new NaturalProperties(s4);
/*     */                   
/*  78 */                   if (naturalproperties.isValid())
/*     */                   {
/*  80 */                     while (arraylist.size() <= j)
/*     */                     {
/*  82 */                       arraylist.add(null);
/*     */                     }
/*     */                     
/*  85 */                     arraylist.set(j, naturalproperties);
/*  86 */                     Config.dbg("NaturalTextures: " + s3 + " = " + s4);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  94 */         propertiesByIndex = (NaturalProperties[])arraylist.toArray(new NaturalProperties[arraylist.size()]);
/*     */       }
/*     */       catch (FileNotFoundException var16)
/*     */       {
/*  98 */         Config.warn("NaturalTextures: configuration \"" + s + "\" not found");
/*  99 */         propertiesByIndex = makeDefaultProperties();
/* 100 */         return;
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 104 */         exception.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static BakedQuad getNaturalTexture(BlockPos p_getNaturalTexture_0_, BakedQuad p_getNaturalTexture_1_)
/*     */   {
/* 111 */     TextureAtlasSprite textureatlassprite = p_getNaturalTexture_1_.getSprite();
/*     */     
/* 113 */     if (textureatlassprite == null)
/*     */     {
/* 115 */       return p_getNaturalTexture_1_;
/*     */     }
/*     */     
/*     */ 
/* 119 */     NaturalProperties naturalproperties = getNaturalProperties(textureatlassprite);
/*     */     
/* 121 */     if (naturalproperties == null)
/*     */     {
/* 123 */       return p_getNaturalTexture_1_;
/*     */     }
/*     */     
/*     */ 
/* 127 */     int i = ConnectedTextures.getSide(p_getNaturalTexture_1_.getFace());
/* 128 */     int j = Config.getRandom(p_getNaturalTexture_0_, i);
/* 129 */     int k = 0;
/* 130 */     boolean flag = false;
/*     */     
/* 132 */     if (naturalproperties.rotation > 1)
/*     */     {
/* 134 */       k = j & 0x3;
/*     */     }
/*     */     
/* 137 */     if (naturalproperties.rotation == 2)
/*     */     {
/* 139 */       k = k / 2 * 2;
/*     */     }
/*     */     
/* 142 */     if (naturalproperties.flip)
/*     */     {
/* 144 */       flag = (j & 0x4) != 0;
/*     */     }
/*     */     
/* 147 */     return naturalproperties.getQuad(p_getNaturalTexture_1_, k, flag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static NaturalProperties getNaturalProperties(TextureAtlasSprite p_getNaturalProperties_0_)
/*     */   {
/* 154 */     if (!(p_getNaturalProperties_0_ instanceof TextureAtlasSprite))
/*     */     {
/* 156 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 160 */     int i = p_getNaturalProperties_0_.getIndexInMap();
/*     */     
/* 162 */     if ((i >= 0) && (i < propertiesByIndex.length))
/*     */     {
/* 164 */       NaturalProperties naturalproperties = propertiesByIndex[i];
/* 165 */       return naturalproperties;
/*     */     }
/*     */     
/*     */ 
/* 169 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static NaturalProperties[] makeDefaultProperties()
/*     */   {
/* 176 */     Config.dbg("NaturalTextures: Creating default configuration.");
/* 177 */     List list = new ArrayList();
/* 178 */     setIconProperties(list, "coarse_dirt", "4F");
/* 179 */     setIconProperties(list, "grass_side", "F");
/* 180 */     setIconProperties(list, "grass_side_overlay", "F");
/* 181 */     setIconProperties(list, "stone_slab_top", "F");
/* 182 */     setIconProperties(list, "gravel", "2");
/* 183 */     setIconProperties(list, "log_oak", "2F");
/* 184 */     setIconProperties(list, "log_spruce", "2F");
/* 185 */     setIconProperties(list, "log_birch", "F");
/* 186 */     setIconProperties(list, "log_jungle", "2F");
/* 187 */     setIconProperties(list, "log_acacia", "2F");
/* 188 */     setIconProperties(list, "log_big_oak", "2F");
/* 189 */     setIconProperties(list, "log_oak_top", "4F");
/* 190 */     setIconProperties(list, "log_spruce_top", "4F");
/* 191 */     setIconProperties(list, "log_birch_top", "4F");
/* 192 */     setIconProperties(list, "log_jungle_top", "4F");
/* 193 */     setIconProperties(list, "log_acacia_top", "4F");
/* 194 */     setIconProperties(list, "log_big_oak_top", "4F");
/* 195 */     setIconProperties(list, "leaves_oak", "2F");
/* 196 */     setIconProperties(list, "leaves_spruce", "2F");
/* 197 */     setIconProperties(list, "leaves_birch", "2F");
/* 198 */     setIconProperties(list, "leaves_jungle", "2");
/* 199 */     setIconProperties(list, "leaves_big_oak", "2F");
/* 200 */     setIconProperties(list, "leaves_acacia", "2F");
/* 201 */     setIconProperties(list, "gold_ore", "2F");
/* 202 */     setIconProperties(list, "iron_ore", "2F");
/* 203 */     setIconProperties(list, "coal_ore", "2F");
/* 204 */     setIconProperties(list, "diamond_ore", "2F");
/* 205 */     setIconProperties(list, "redstone_ore", "2F");
/* 206 */     setIconProperties(list, "lapis_ore", "2F");
/* 207 */     setIconProperties(list, "obsidian", "4F");
/* 208 */     setIconProperties(list, "snow", "4F");
/* 209 */     setIconProperties(list, "grass_side_snowed", "F");
/* 210 */     setIconProperties(list, "cactus_side", "2F");
/* 211 */     setIconProperties(list, "clay", "4F");
/* 212 */     setIconProperties(list, "mycelium_side", "F");
/* 213 */     setIconProperties(list, "mycelium_top", "4F");
/* 214 */     setIconProperties(list, "farmland_wet", "2F");
/* 215 */     setIconProperties(list, "farmland_dry", "2F");
/* 216 */     setIconProperties(list, "netherrack", "4F");
/* 217 */     setIconProperties(list, "soul_sand", "4F");
/* 218 */     setIconProperties(list, "glowstone", "4");
/* 219 */     setIconProperties(list, "end_stone", "4");
/* 220 */     setIconProperties(list, "sandstone_top", "4");
/* 221 */     setIconProperties(list, "sandstone_bottom", "4F");
/* 222 */     setIconProperties(list, "redstone_lamp_on", "4F");
/* 223 */     setIconProperties(list, "redstone_lamp_off", "4F");
/* 224 */     NaturalProperties[] anaturalproperties = (NaturalProperties[])list.toArray(new NaturalProperties[list.size()]);
/* 225 */     return anaturalproperties;
/*     */   }
/*     */   
/*     */   private static void setIconProperties(List p_setIconProperties_0_, String p_setIconProperties_1_, String p_setIconProperties_2_)
/*     */   {
/* 230 */     TextureMap texturemap = TextureUtils.getTextureMapBlocks();
/* 231 */     TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe("minecraft:blocks/" + p_setIconProperties_1_);
/*     */     
/* 233 */     if (textureatlassprite == null)
/*     */     {
/* 235 */       Config.warn("*** NaturalProperties: Icon not found: " + p_setIconProperties_1_ + " ***");
/*     */     }
/* 237 */     else if (!(textureatlassprite instanceof TextureAtlasSprite))
/*     */     {
/* 239 */       Config.warn("*** NaturalProperties: Icon is not IconStitched: " + p_setIconProperties_1_ + ": " + textureatlassprite.getClass().getName() + " ***");
/*     */     }
/*     */     else
/*     */     {
/* 243 */       int i = textureatlassprite.getIndexInMap();
/*     */       
/* 245 */       if (i < 0)
/*     */       {
/* 247 */         Config.warn("*** NaturalProperties: Invalid index for icon: " + p_setIconProperties_1_ + ": " + i + " ***");
/*     */       }
/* 249 */       else if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + p_setIconProperties_1_ + ".png")))
/*     */       {
/* 251 */         while (i >= p_setIconProperties_0_.size())
/*     */         {
/* 253 */           p_setIconProperties_0_.add(null);
/*     */         }
/*     */         
/* 256 */         NaturalProperties naturalproperties = new NaturalProperties(p_setIconProperties_2_);
/* 257 */         p_setIconProperties_0_.set(i, naturalproperties);
/* 258 */         Config.dbg("NaturalTextures: " + p_setIconProperties_1_ + " = " + p_setIconProperties_2_);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\NaturalTextures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */