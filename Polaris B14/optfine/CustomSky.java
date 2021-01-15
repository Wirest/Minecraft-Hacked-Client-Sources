/*     */ package optfine;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ 
/*     */ public class CustomSky
/*     */ {
/*  16 */   private static CustomSkyLayer[][] worldSkyLayers = null;
/*     */   
/*     */   public static void reset()
/*     */   {
/*  20 */     worldSkyLayers = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void update()
/*     */   {
/*     */     
/*  27 */     if (Config.isCustomSky())
/*     */     {
/*  29 */       worldSkyLayers = readCustomSkies();
/*     */     }
/*     */   }
/*     */   
/*     */   private static CustomSkyLayer[][] readCustomSkies()
/*     */   {
/*  35 */     CustomSkyLayer[][] acustomskylayer = new CustomSkyLayer[10][0];
/*  36 */     String s = "mcpatcher/sky/world";
/*  37 */     int i = -1;
/*     */     
/*  39 */     for (int j = 0; j < acustomskylayer.length; j++)
/*     */     {
/*  41 */       String s1 = s + j + "/sky";
/*  42 */       List list = new java.util.ArrayList();
/*     */       
/*  44 */       for (int k = 1; k < 1000; k++)
/*     */       {
/*  46 */         String s2 = s1 + k + ".properties";
/*     */         
/*     */         try
/*     */         {
/*  50 */           ResourceLocation resourcelocation = new ResourceLocation(s2);
/*  51 */           InputStream inputstream = Config.getResourceStream(resourcelocation);
/*     */           
/*  53 */           if (inputstream == null) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*  58 */           Properties properties = new Properties();
/*  59 */           properties.load(inputstream);
/*  60 */           Config.dbg("CustomSky properties: " + s2);
/*  61 */           String s3 = s1 + k + ".png";
/*  62 */           CustomSkyLayer customskylayer = new CustomSkyLayer(properties, s3);
/*     */           
/*  64 */           if (customskylayer.isValid(s2))
/*     */           {
/*  66 */             ResourceLocation resourcelocation1 = new ResourceLocation(customskylayer.source);
/*  67 */             ITextureObject itextureobject = TextureUtils.getTexture(resourcelocation1);
/*     */             
/*  69 */             if (itextureobject == null)
/*     */             {
/*  71 */               Config.log("CustomSky: Texture not found: " + resourcelocation1);
/*     */             }
/*     */             else
/*     */             {
/*  75 */               customskylayer.textureId = itextureobject.getGlTextureId();
/*  76 */               list.add(customskylayer);
/*  77 */               inputstream.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (FileNotFoundException var15)
/*     */         {
/*     */           break;
/*     */         }
/*     */         catch (IOException ioexception)
/*     */         {
/*  87 */           ioexception.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*  91 */       if (list.size() > 0)
/*     */       {
/*  93 */         CustomSkyLayer[] acustomskylayer2 = (CustomSkyLayer[])list.toArray(new CustomSkyLayer[list.size()]);
/*  94 */         acustomskylayer[j] = acustomskylayer2;
/*  95 */         i = j;
/*     */       }
/*     */     }
/*     */     
/*  99 */     if (i < 0)
/*     */     {
/* 101 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 105 */     int l = i + 1;
/* 106 */     CustomSkyLayer[][] acustomskylayer1 = new CustomSkyLayer[l][0];
/*     */     
/* 108 */     for (int i1 = 0; i1 < acustomskylayer1.length; i1++)
/*     */     {
/* 110 */       acustomskylayer1[i1] = acustomskylayer[i1];
/*     */     }
/*     */     
/* 113 */     return acustomskylayer1;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void renderSky(World p_renderSky_0_, net.minecraft.client.renderer.texture.TextureManager p_renderSky_1_, float p_renderSky_2_, float p_renderSky_3_)
/*     */   {
/* 119 */     if (worldSkyLayers != null)
/*     */     {
/* 121 */       if (Config.getGameSettings().renderDistanceChunks >= 8)
/*     */       {
/* 123 */         int i = p_renderSky_0_.provider.getDimensionId();
/*     */         
/* 125 */         if ((i >= 0) && (i < worldSkyLayers.length))
/*     */         {
/* 127 */           CustomSkyLayer[] acustomskylayer = worldSkyLayers[i];
/*     */           
/* 129 */           if (acustomskylayer != null)
/*     */           {
/* 131 */             long j = p_renderSky_0_.getWorldTime();
/* 132 */             int k = (int)(j % 24000L);
/*     */             
/* 134 */             for (int l = 0; l < acustomskylayer.length; l++)
/*     */             {
/* 136 */               CustomSkyLayer customskylayer = acustomskylayer[l];
/*     */               
/* 138 */               if (customskylayer.isActive(k))
/*     */               {
/* 140 */                 customskylayer.render(k, p_renderSky_2_, p_renderSky_3_);
/*     */               }
/*     */             }
/*     */             
/* 144 */             Blender.clearBlend(p_renderSky_3_);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean hasSkyLayers(World p_hasSkyLayers_0_)
/*     */   {
/* 153 */     if (worldSkyLayers == null)
/*     */     {
/* 155 */       return false;
/*     */     }
/* 157 */     if (Config.getGameSettings().renderDistanceChunks < 8)
/*     */     {
/* 159 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 163 */     int i = p_hasSkyLayers_0_.provider.getDimensionId();
/*     */     
/* 165 */     if ((i >= 0) && (i < worldSkyLayers.length))
/*     */     {
/* 167 */       CustomSkyLayer[] acustomskylayer = worldSkyLayers[i];
/* 168 */       return acustomskylayer != null;
/*     */     }
/*     */     
/*     */ 
/* 172 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CustomSky.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */