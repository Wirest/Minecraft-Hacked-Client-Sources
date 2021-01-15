/*     */ package optfine;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class PlayerConfigurationParser
/*     */ {
/*  16 */   private String player = null;
/*     */   public static final String CONFIG_ITEMS = "items";
/*     */   public static final String ITEM_TYPE = "type";
/*     */   public static final String ITEM_ACTIVE = "active";
/*     */   
/*     */   public PlayerConfigurationParser(String p_i47_1_)
/*     */   {
/*  23 */     this.player = p_i47_1_;
/*     */   }
/*     */   
/*     */   public PlayerConfiguration parsePlayerConfiguration(JsonElement p_parsePlayerConfiguration_1_)
/*     */   {
/*  28 */     if (p_parsePlayerConfiguration_1_ == null)
/*     */     {
/*  30 */       throw new JsonParseException("JSON object is null, player: " + this.player);
/*     */     }
/*     */     
/*     */ 
/*  34 */     JsonObject jsonobject = (JsonObject)p_parsePlayerConfiguration_1_;
/*  35 */     PlayerConfiguration playerconfiguration = new PlayerConfiguration();
/*  36 */     JsonArray jsonarray = (JsonArray)jsonobject.get("items");
/*     */     
/*  38 */     if (jsonarray != null)
/*     */     {
/*  40 */       for (int i = 0; i < jsonarray.size(); i++)
/*     */       {
/*  42 */         JsonObject jsonobject1 = (JsonObject)jsonarray.get(i);
/*  43 */         boolean flag = Json.getBoolean(jsonobject1, "active", true);
/*     */         
/*  45 */         if (flag)
/*     */         {
/*  47 */           String s = Json.getString(jsonobject1, "type");
/*     */           
/*  49 */           if (s == null)
/*     */           {
/*  51 */             Config.warn("Item type is null, player: " + this.player);
/*     */           }
/*     */           else
/*     */           {
/*  55 */             String s1 = Json.getString(jsonobject1, "model");
/*     */             
/*  57 */             if (s1 == null)
/*     */             {
/*  59 */               s1 = "items/" + s + "/model.cfg";
/*     */             }
/*     */             
/*  62 */             PlayerItemModel playeritemmodel = downloadModel(s1);
/*     */             
/*  64 */             if (playeritemmodel != null)
/*     */             {
/*  66 */               if (!playeritemmodel.isUsePlayerTexture())
/*     */               {
/*  68 */                 String s2 = Json.getString(jsonobject1, "texture");
/*     */                 
/*  70 */                 if (s2 == null)
/*     */                 {
/*  72 */                   s2 = "items/" + s + "/users/" + this.player + ".png";
/*     */                 }
/*     */                 
/*  75 */                 BufferedImage bufferedimage = downloadTextureImage(s2);
/*     */                 
/*  77 */                 if (bufferedimage != null)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*  82 */                   playeritemmodel.setTextureImage(bufferedimage);
/*  83 */                   ResourceLocation resourcelocation = new ResourceLocation("optifine.net", s2);
/*  84 */                   playeritemmodel.setTextureLocation(resourcelocation);
/*     */                 }
/*     */               } else {
/*  87 */                 playerconfiguration.addPlayerItemModel(playeritemmodel);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return playerconfiguration;
/*     */   }
/*     */   
/*     */ 
/*     */   private BufferedImage downloadTextureImage(String p_downloadTextureImage_1_)
/*     */   {
/* 100 */     String s = "http://s.optifine.net/" + p_downloadTextureImage_1_;
/*     */     
/*     */     try
/*     */     {
/* 104 */       return ImageIO.read(new URL(s));
/*     */ 
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 109 */       Config.warn("Error loading item texture " + p_downloadTextureImage_1_ + ": " + ioexception.getClass().getName() + ": " + ioexception.getMessage()); }
/* 110 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private PlayerItemModel downloadModel(String p_downloadModel_1_)
/*     */   {
/* 116 */     String s = "http://s.optifine.net/" + p_downloadModel_1_;
/*     */     
/*     */     try
/*     */     {
/* 120 */       byte[] abyte = HttpUtils.get(s);
/* 121 */       String s1 = new String(abyte, "ASCII");
/* 122 */       JsonParser jsonparser = new JsonParser();
/* 123 */       JsonObject jsonobject = (JsonObject)jsonparser.parse(s1);
/* 124 */       PlayerItemParser playeritemparser = new PlayerItemParser();
/* 125 */       return PlayerItemParser.parseItemModel(jsonobject);
/*     */ 
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 130 */       Config.warn("Error loading item model " + p_downloadModel_1_ + ": " + exception.getClass().getName() + ": " + exception.getMessage()); }
/* 131 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerConfigurationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */