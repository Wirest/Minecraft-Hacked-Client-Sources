/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureCompass;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class RenderItemFrame extends Render<EntityItemFrame>
/*     */ {
/*  35 */   private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
/*  36 */   private final Minecraft mc = Minecraft.getMinecraft();
/*  37 */   private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
/*  38 */   private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
/*     */   private RenderItem itemRenderer;
/*     */   
/*     */   public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn)
/*     */   {
/*  43 */     super(renderManagerIn);
/*  44 */     this.itemRenderer = itemRendererIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  55 */     GlStateManager.pushMatrix();
/*  56 */     BlockPos blockpos = entity.getHangingPosition();
/*  57 */     double d0 = blockpos.getX() - entity.posX + x;
/*  58 */     double d1 = blockpos.getY() - entity.posY + y;
/*  59 */     double d2 = blockpos.getZ() - entity.posZ + z;
/*  60 */     GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
/*  61 */     GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
/*  62 */     this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/*  63 */     BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
/*  64 */     ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
/*     */     IBakedModel ibakedmodel;
/*     */     IBakedModel ibakedmodel;
/*  67 */     if ((entity.getDisplayedItem() != null) && (entity.getDisplayedItem().getItem() == Items.filled_map))
/*     */     {
/*  69 */       ibakedmodel = modelmanager.getModel(this.mapModel);
/*     */     }
/*     */     else
/*     */     {
/*  73 */       ibakedmodel = modelmanager.getModel(this.itemFrameModel);
/*     */     }
/*     */     
/*  76 */     GlStateManager.pushMatrix();
/*  77 */     GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*  78 */     blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
/*  79 */     GlStateManager.popMatrix();
/*  80 */     GlStateManager.translate(0.0F, 0.0F, 0.4375F);
/*  81 */     renderItem(entity);
/*  82 */     GlStateManager.popMatrix();
/*  83 */     renderName(entity, x + entity.facingDirection.getFrontOffsetX() * 0.3F, y - 0.25D, z + entity.facingDirection.getFrontOffsetZ() * 0.3F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceLocation getEntityTexture(EntityItemFrame entity)
/*     */   {
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   private void renderItem(EntityItemFrame itemFrame)
/*     */   {
/*  96 */     ItemStack itemstack = itemFrame.getDisplayedItem();
/*     */     
/*  98 */     if (itemstack != null)
/*     */     {
/* 100 */       EntityItem entityitem = new EntityItem(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
/* 101 */       Item item = entityitem.getEntityItem().getItem();
/* 102 */       entityitem.getEntityItem().stackSize = 1;
/* 103 */       entityitem.hoverStart = 0.0F;
/* 104 */       GlStateManager.pushMatrix();
/* 105 */       GlStateManager.disableLighting();
/* 106 */       int i = itemFrame.getRotation();
/*     */       
/* 108 */       if (item == Items.filled_map)
/*     */       {
/* 110 */         i = i % 4 * 2;
/*     */       }
/*     */       
/* 113 */       GlStateManager.rotate(i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
/*     */       
/* 115 */       if (item == Items.filled_map)
/*     */       {
/* 117 */         this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
/* 118 */         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 119 */         float f = 0.0078125F;
/* 120 */         GlStateManager.scale(f, f, f);
/* 121 */         GlStateManager.translate(-64.0F, -64.0F, 0.0F);
/* 122 */         net.minecraft.world.storage.MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
/* 123 */         GlStateManager.translate(0.0F, 0.0F, -1.0F);
/*     */         
/* 125 */         if (mapdata != null)
/*     */         {
/* 127 */           this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 132 */         TextureAtlasSprite textureatlassprite = null;
/*     */         
/* 134 */         if (item == Items.compass)
/*     */         {
/* 136 */           textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
/* 137 */           this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*     */           
/* 139 */           if ((textureatlassprite instanceof TextureCompass))
/*     */           {
/* 141 */             TextureCompass texturecompass = (TextureCompass)textureatlassprite;
/* 142 */             double d0 = texturecompass.currentAngle;
/* 143 */             double d1 = texturecompass.angleDelta;
/* 144 */             texturecompass.currentAngle = 0.0D;
/* 145 */             texturecompass.angleDelta = 0.0D;
/* 146 */             texturecompass.updateCompass(itemFrame.worldObj, itemFrame.posX, itemFrame.posZ, MathHelper.wrapAngleTo180_float(180 + itemFrame.facingDirection.getHorizontalIndex() * 90), false, true);
/* 147 */             texturecompass.currentAngle = d0;
/* 148 */             texturecompass.angleDelta = d1;
/*     */           }
/*     */           else
/*     */           {
/* 152 */             textureatlassprite = null;
/*     */           }
/*     */         }
/*     */         
/* 156 */         GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*     */         
/* 158 */         if ((!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem())) || ((item instanceof net.minecraft.item.ItemSkull)))
/*     */         {
/* 160 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 163 */         GlStateManager.pushAttrib();
/* 164 */         RenderHelper.enableStandardItemLighting();
/* 165 */         this.itemRenderer.func_181564_a(entityitem.getEntityItem(), net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED);
/* 166 */         RenderHelper.disableStandardItemLighting();
/* 167 */         GlStateManager.popAttrib();
/*     */         
/* 169 */         if ((textureatlassprite != null) && (textureatlassprite.getFrameCount() > 0))
/*     */         {
/* 171 */           textureatlassprite.updateAnimation();
/*     */         }
/*     */       }
/*     */       
/* 175 */       GlStateManager.enableLighting();
/* 176 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void renderName(EntityItemFrame entity, double x, double y, double z)
/*     */   {
/* 182 */     if ((Minecraft.isGuiEnabled()) && (entity.getDisplayedItem() != null) && (entity.getDisplayedItem().hasDisplayName()) && (this.renderManager.pointedEntity == entity))
/*     */     {
/* 184 */       float f = 1.6F;
/* 185 */       float f1 = 0.016666668F * f;
/* 186 */       double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
/* 187 */       float f2 = entity.isSneaking() ? 32.0F : 64.0F;
/*     */       
/* 189 */       if (d0 < f2 * f2)
/*     */       {
/* 191 */         String s = entity.getDisplayedItem().getDisplayName();
/*     */         
/* 193 */         if (entity.isSneaking())
/*     */         {
/* 195 */           FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 196 */           GlStateManager.pushMatrix();
/* 197 */           GlStateManager.translate((float)x + 0.0F, (float)y + entity.height + 0.5F, (float)z);
/* 198 */           GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 199 */           GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 200 */           GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 201 */           GlStateManager.scale(-f1, -f1, f1);
/* 202 */           GlStateManager.disableLighting();
/* 203 */           GlStateManager.translate(0.0F, 0.25F / f1, 0.0F);
/* 204 */           GlStateManager.depthMask(false);
/* 205 */           GlStateManager.enableBlend();
/* 206 */           GlStateManager.blendFunc(770, 771);
/* 207 */           Tessellator tessellator = Tessellator.getInstance();
/* 208 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 209 */           int i = fontrenderer.getStringWidth(s) / 2;
/* 210 */           GlStateManager.disableTexture2D();
/* 211 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 212 */           worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 213 */           worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 214 */           worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 215 */           worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 216 */           tessellator.draw();
/* 217 */           GlStateManager.enableTexture2D();
/* 218 */           GlStateManager.depthMask(true);
/* 219 */           fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0.0D, 553648127);
/* 220 */           GlStateManager.enableLighting();
/* 221 */           GlStateManager.disableBlend();
/* 222 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 223 */           GlStateManager.popMatrix();
/*     */         }
/*     */         else
/*     */         {
/* 227 */           renderLivingLabel(entity, s, x, y, z, 64);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\RenderItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */