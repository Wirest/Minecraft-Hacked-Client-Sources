/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreenResourcePacks;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.resources.data.PackMetadataSection;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ResourcePackListEntryDefault extends ResourcePackListEntry
/*    */ {
/* 16 */   private static final Logger logger = ;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 23 */   private final IResourcePack field_148320_d = this.mc.getResourcePackRepository().rprDefaultResourcePack;
/*    */   private final net.minecraft.util.ResourceLocation resourcePackIcon;
/*    */   
/*    */   public ResourcePackListEntryDefault(GuiScreenResourcePacks resourcePacksGUIIn)
/*    */   {
/* 22 */     super(resourcePacksGUIIn);
/*    */     
/*    */     DynamicTexture dynamictexture;
/*    */     
/*    */     try
/*    */     {
/* 28 */       dynamictexture = new DynamicTexture(this.field_148320_d.getPackImage());
/*    */     }
/*    */     catch (IOException var4) {
/*    */       DynamicTexture dynamictexture;
/* 32 */       dynamictexture = net.minecraft.client.renderer.texture.TextureUtil.missingTexture;
/*    */     }
/*    */     
/* 35 */     this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
/*    */   }
/*    */   
/*    */   protected int func_183019_a()
/*    */   {
/* 40 */     return 1;
/*    */   }
/*    */   
/*    */   protected String func_148311_a()
/*    */   {
/*    */     try
/*    */     {
/* 47 */       PackMetadataSection packmetadatasection = (PackMetadataSection)this.field_148320_d.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");
/*    */       
/* 49 */       if (packmetadatasection != null)
/*    */       {
/* 51 */         return packmetadatasection.getPackDescription().getFormattedText();
/*    */       }
/*    */     }
/*    */     catch (JsonParseException jsonparseexception)
/*    */     {
/* 56 */       logger.error("Couldn't load metadata info", jsonparseexception);
/*    */     }
/*    */     catch (IOException ioexception)
/*    */     {
/* 60 */       logger.error("Couldn't load metadata info", ioexception);
/*    */     }
/*    */     
/* 63 */     return EnumChatFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
/*    */   }
/*    */   
/*    */   protected boolean func_148309_e()
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean func_148308_f()
/*    */   {
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean func_148314_g()
/*    */   {
/* 78 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean func_148307_h()
/*    */   {
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   protected String func_148312_b()
/*    */   {
/* 88 */     return "Default";
/*    */   }
/*    */   
/*    */   protected void func_148313_c()
/*    */   {
/* 93 */     this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
/*    */   }
/*    */   
/*    */   protected boolean func_148310_d()
/*    */   {
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\ResourcePackListEntryDefault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */