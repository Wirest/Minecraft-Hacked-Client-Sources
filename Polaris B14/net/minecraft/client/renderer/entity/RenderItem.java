/*      */ package net.minecraft.client.renderer.entity;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDirt.DirtType;
/*      */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*      */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*      */ import net.minecraft.block.BlockHugeMushroom.EnumType;
/*      */ import net.minecraft.block.BlockPlanks.EnumType;
/*      */ import net.minecraft.block.BlockPrismarine.EnumType;
/*      */ import net.minecraft.block.BlockQuartz.EnumType;
/*      */ import net.minecraft.block.BlockRedSandstone.EnumType;
/*      */ import net.minecraft.block.BlockSand.EnumType;
/*      */ import net.minecraft.block.BlockSandStone.EnumType;
/*      */ import net.minecraft.block.BlockSilverfish.EnumType;
/*      */ import net.minecraft.block.BlockStone.EnumType;
/*      */ import net.minecraft.block.BlockStoneBrick.EnumType;
/*      */ import net.minecraft.block.BlockStoneSlab.EnumType;
/*      */ import net.minecraft.block.BlockStoneSlabNew.EnumType;
/*      */ import net.minecraft.block.BlockTallGrass.EnumType;
/*      */ import net.minecraft.block.BlockWall.EnumType;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.ItemMeshDefinition;
/*      */ import net.minecraft.client.renderer.ItemModelMesher;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*      */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*      */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*      */ import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
/*      */ import net.minecraft.client.renderer.texture.ITextureObject;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.resources.model.IBakedModel;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.EnumDyeColor;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemFishFood.FishType;
/*      */ import net.minecraft.item.ItemPotion;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3i;
/*      */ import org.lwjgl.util.vector.Vector3f;
/*      */ 
/*      */ public class RenderItem implements IResourceManagerReloadListener
/*      */ {
/*   63 */   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
/*   64 */   private boolean field_175058_l = true;
/*      */   
/*      */   public float zLevel;
/*      */   
/*      */   private final ItemModelMesher itemModelMesher;
/*      */   private final TextureManager textureManager;
/*      */   
/*      */   public RenderItem(TextureManager textureManager, ModelManager modelManager)
/*      */   {
/*   73 */     this.textureManager = textureManager;
/*   74 */     this.itemModelMesher = new ItemModelMesher(modelManager);
/*   75 */     registerItems();
/*      */   }
/*      */   
/*      */   public void func_175039_a(boolean p_175039_1_)
/*      */   {
/*   80 */     this.field_175058_l = p_175039_1_;
/*      */   }
/*      */   
/*      */   public ItemModelMesher getItemModelMesher()
/*      */   {
/*   85 */     return this.itemModelMesher;
/*      */   }
/*      */   
/*      */   protected void registerItem(Item itm, int subType, String identifier)
/*      */   {
/*   90 */     this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
/*      */   }
/*      */   
/*      */   protected void registerBlock(Block blk, int subType, String identifier)
/*      */   {
/*   95 */     registerItem(Item.getItemFromBlock(blk), subType, identifier);
/*      */   }
/*      */   
/*      */   private void registerBlock(Block blk, String identifier)
/*      */   {
/*  100 */     registerBlock(blk, 0, identifier);
/*      */   }
/*      */   
/*      */   private void registerItem(Item itm, String identifier)
/*      */   {
/*  105 */     registerItem(itm, 0, identifier);
/*      */   }
/*      */   
/*      */   private void renderModel(IBakedModel model, ItemStack stack)
/*      */   {
/*  110 */     renderModel(model, -1, stack);
/*      */   }
/*      */   
/*      */   private void renderModel(IBakedModel model, int color)
/*      */   {
/*  115 */     renderModel(model, color, null);
/*      */   }
/*      */   
/*      */   private void renderModel(IBakedModel model, int color, ItemStack stack)
/*      */   {
/*  120 */     Tessellator tessellator = Tessellator.getInstance();
/*  121 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  122 */     worldrenderer.begin(7, DefaultVertexFormats.ITEM);
/*      */     EnumFacing[] arrayOfEnumFacing;
/*  124 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*      */       
/*  126 */       renderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
/*      */     }
/*      */     
/*  129 */     renderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
/*  130 */     tessellator.draw();
/*      */   }
/*      */   
/*      */   public void renderItem(ItemStack stack, IBakedModel model)
/*      */   {
/*  135 */     if (stack != null)
/*      */     {
/*  137 */       GlStateManager.pushMatrix();
/*  138 */       GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*      */       
/*  140 */       if (model.isBuiltInRenderer())
/*      */       {
/*  142 */         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  143 */         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*  144 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  145 */         GlStateManager.enableRescaleNormal();
/*  146 */         TileEntityItemStackRenderer.instance.renderByItem(stack);
/*      */       }
/*      */       else
/*      */       {
/*  150 */         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*  151 */         renderModel(model, stack);
/*      */         
/*  153 */         if (stack.hasEffect())
/*      */         {
/*  155 */           renderEffect(model);
/*      */         }
/*      */       }
/*      */       
/*  159 */       GlStateManager.popMatrix();
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderEffect(IBakedModel model)
/*      */   {
/*  165 */     GlStateManager.depthMask(false);
/*  166 */     GlStateManager.depthFunc(514);
/*  167 */     GlStateManager.disableLighting();
/*  168 */     GlStateManager.blendFunc(768, 1);
/*  169 */     this.textureManager.bindTexture(RES_ITEM_GLINT);
/*  170 */     GlStateManager.matrixMode(5890);
/*  171 */     GlStateManager.pushMatrix();
/*  172 */     GlStateManager.scale(8.0F, 8.0F, 8.0F);
/*  173 */     float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
/*  174 */     GlStateManager.translate(f, 0.0F, 0.0F);
/*  175 */     GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
/*  176 */     renderModel(model, -8372020);
/*  177 */     GlStateManager.popMatrix();
/*  178 */     GlStateManager.pushMatrix();
/*  179 */     GlStateManager.scale(8.0F, 8.0F, 8.0F);
/*  180 */     float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
/*  181 */     GlStateManager.translate(-f1, 0.0F, 0.0F);
/*  182 */     GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
/*  183 */     renderModel(model, -8372020);
/*  184 */     GlStateManager.popMatrix();
/*  185 */     GlStateManager.matrixMode(5888);
/*  186 */     GlStateManager.blendFunc(770, 771);
/*  187 */     GlStateManager.enableLighting();
/*  188 */     GlStateManager.depthFunc(515);
/*  189 */     GlStateManager.depthMask(true);
/*  190 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*      */   }
/*      */   
/*      */   private void putQuadNormal(WorldRenderer renderer, BakedQuad quad)
/*      */   {
/*  195 */     Vec3i vec3i = quad.getFace().getDirectionVec();
/*  196 */     renderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/*      */   }
/*      */   
/*      */   private void renderQuad(WorldRenderer renderer, BakedQuad quad, int color)
/*      */   {
/*  201 */     renderer.addVertexData(quad.getVertexData());
/*  202 */     renderer.putColor4(color);
/*  203 */     putQuadNormal(renderer, quad);
/*      */   }
/*      */   
/*      */   private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack)
/*      */   {
/*  208 */     boolean flag = (color == -1) && (stack != null);
/*  209 */     int i = 0;
/*      */     
/*  211 */     for (int j = quads.size(); i < j; i++)
/*      */     {
/*  213 */       BakedQuad bakedquad = (BakedQuad)quads.get(i);
/*  214 */       int k = color;
/*      */       
/*  216 */       if ((flag) && (bakedquad.hasTintIndex()))
/*      */       {
/*  218 */         k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());
/*      */         
/*  220 */         if (net.minecraft.client.renderer.EntityRenderer.anaglyphEnable)
/*      */         {
/*  222 */           k = net.minecraft.client.renderer.texture.TextureUtil.anaglyphColor(k);
/*      */         }
/*      */         
/*  225 */         k |= 0xFF000000;
/*      */       }
/*      */       
/*  228 */       renderQuad(renderer, bakedquad, k);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean shouldRenderItemIn3D(ItemStack stack)
/*      */   {
/*  234 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  235 */     return ibakedmodel == null ? false : ibakedmodel.isGui3d();
/*      */   }
/*      */   
/*      */   private void preTransform(ItemStack stack)
/*      */   {
/*  240 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  241 */     Item item = stack.getItem();
/*      */     
/*  243 */     if (item != null)
/*      */     {
/*  245 */       boolean flag = ibakedmodel.isGui3d();
/*      */       
/*  247 */       if (!flag)
/*      */       {
/*  249 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*      */       }
/*      */       
/*  252 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_181564_a(ItemStack p_181564_1_, ItemCameraTransforms.TransformType p_181564_2_)
/*      */   {
/*  258 */     if (p_181564_1_ != null)
/*      */     {
/*  260 */       IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(p_181564_1_);
/*  261 */       renderItemModelTransform(p_181564_1_, ibakedmodel, p_181564_2_);
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderItemModelForEntity(ItemStack stack, EntityLivingBase entityToRenderFor, ItemCameraTransforms.TransformType cameraTransformType)
/*      */   {
/*  267 */     if ((stack != null) && (entityToRenderFor != null))
/*      */     {
/*  269 */       IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*      */       
/*  271 */       if ((entityToRenderFor instanceof EntityPlayer))
/*      */       {
/*  273 */         EntityPlayer entityplayer = (EntityPlayer)entityToRenderFor;
/*  274 */         Item item = stack.getItem();
/*  275 */         ModelResourceLocation modelresourcelocation = null;
/*      */         
/*  277 */         if ((item == Items.fishing_rod) && (entityplayer.fishEntity != null))
/*      */         {
/*  279 */           modelresourcelocation = new ModelResourceLocation("fishing_rod_cast", "inventory");
/*      */         }
/*  281 */         else if ((item == Items.bow) && (entityplayer.getItemInUse() != null))
/*      */         {
/*  283 */           int i = stack.getMaxItemUseDuration() - entityplayer.getItemInUseCount();
/*      */           
/*  285 */           if (i >= 18)
/*      */           {
/*  287 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_2", "inventory");
/*      */           }
/*  289 */           else if (i > 13)
/*      */           {
/*  291 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_1", "inventory");
/*      */           }
/*  293 */           else if (i > 0)
/*      */           {
/*  295 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_0", "inventory");
/*      */           }
/*      */         }
/*      */         
/*  299 */         if (modelresourcelocation != null)
/*      */         {
/*  301 */           ibakedmodel = this.itemModelMesher.getModelManager().getModel(modelresourcelocation);
/*      */         }
/*      */       }
/*      */       
/*  305 */       renderItemModelTransform(stack, ibakedmodel, cameraTransformType);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void renderItemModelTransform(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType)
/*      */   {
/*  311 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  312 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/*  313 */     preTransform(stack);
/*  314 */     GlStateManager.enableRescaleNormal();
/*  315 */     GlStateManager.alphaFunc(516, 0.1F);
/*  316 */     GlStateManager.enableBlend();
/*  317 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  318 */     GlStateManager.pushMatrix();
/*  319 */     ItemCameraTransforms itemcameratransforms = model.getItemCameraTransforms();
/*  320 */     itemcameratransforms.applyTransform(cameraTransformType);
/*      */     
/*  322 */     if (func_183005_a(itemcameratransforms.getTransform(cameraTransformType)))
/*      */     {
/*  324 */       GlStateManager.cullFace(1028);
/*      */     }
/*      */     
/*  327 */     renderItem(stack, model);
/*  328 */     GlStateManager.cullFace(1029);
/*  329 */     GlStateManager.popMatrix();
/*  330 */     GlStateManager.disableRescaleNormal();
/*  331 */     GlStateManager.disableBlend();
/*  332 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  333 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/*      */   }
/*      */   
/*      */   private boolean func_183005_a(ItemTransformVec3f p_183005_1_)
/*      */   {
/*  338 */     return (p_183005_1_.scale.x < 0.0F ? 1 : 0) ^ (p_183005_1_.scale.y < 0.0F ? 1 : 0) ^ (p_183005_1_.scale.z < 0.0F ? 1 : 0);
/*      */   }
/*      */   
/*      */   public void renderItemIntoGUI(ItemStack stack, int x, int y)
/*      */   {
/*  343 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  344 */     GlStateManager.pushMatrix();
/*  345 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  346 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/*  347 */     GlStateManager.enableRescaleNormal();
/*  348 */     GlStateManager.enableAlpha();
/*  349 */     GlStateManager.alphaFunc(516, 0.1F);
/*  350 */     GlStateManager.enableBlend();
/*  351 */     GlStateManager.blendFunc(770, 771);
/*  352 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  353 */     setupGuiTransform(x, y, ibakedmodel.isGui3d());
/*  354 */     ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
/*  355 */     renderItem(stack, ibakedmodel);
/*  356 */     GlStateManager.disableAlpha();
/*  357 */     GlStateManager.disableRescaleNormal();
/*  358 */     GlStateManager.disableLighting();
/*  359 */     GlStateManager.popMatrix();
/*  360 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  361 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/*      */   }
/*      */   
/*      */   private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
/*      */   {
/*  366 */     GlStateManager.translate(xPosition, yPosition, 100.0F + this.zLevel);
/*  367 */     GlStateManager.translate(8.0F, 8.0F, 0.0F);
/*  368 */     GlStateManager.scale(1.0F, 1.0F, -1.0F);
/*  369 */     GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*      */     
/*  371 */     if (isGui3d)
/*      */     {
/*  373 */       GlStateManager.scale(40.0F, 40.0F, 40.0F);
/*  374 */       GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
/*  375 */       GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/*  376 */       GlStateManager.enableLighting();
/*      */     }
/*      */     else
/*      */     {
/*  380 */       GlStateManager.scale(64.0F, 64.0F, 64.0F);
/*  381 */       GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/*  382 */       GlStateManager.disableLighting();
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderItemAndEffectIntoGUI(final ItemStack stack, int xPosition, int yPosition)
/*      */   {
/*  388 */     if ((stack != null) && (stack.getItem() != null))
/*      */     {
/*  390 */       this.zLevel += 50.0F;
/*      */       
/*      */       try
/*      */       {
/*  394 */         renderItemIntoGUI(stack, xPosition, yPosition);
/*      */       }
/*      */       catch (Throwable throwable)
/*      */       {
/*  398 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
/*  399 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
/*  400 */         crashreportcategory.addCrashSectionCallable("Item Type", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  404 */             return String.valueOf(stack.getItem());
/*      */           }
/*  406 */         });
/*  407 */         crashreportcategory.addCrashSectionCallable("Item Aux", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  411 */             return String.valueOf(stack.getMetadata());
/*      */           }
/*  413 */         });
/*  414 */         crashreportcategory.addCrashSectionCallable("Item NBT", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  418 */             return String.valueOf(stack.getTagCompound());
/*      */           }
/*  420 */         });
/*  421 */         crashreportcategory.addCrashSectionCallable("Item Foil", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  425 */             return String.valueOf(stack.hasEffect());
/*      */           }
/*  427 */         });
/*  428 */         throw new net.minecraft.util.ReportedException(crashreport);
/*      */       }
/*      */       
/*  431 */       this.zLevel -= 50.0F;
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition)
/*      */   {
/*  437 */     renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text)
/*      */   {
/*  445 */     if (stack != null)
/*      */     {
/*  447 */       if ((stack.stackSize != 1) || (text != null))
/*      */       {
/*  449 */         String s = text == null ? String.valueOf(stack.stackSize) : text;
/*      */         
/*  451 */         if ((text == null) && (stack.stackSize < 1))
/*      */         {
/*  453 */           s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
/*      */         }
/*      */         
/*  456 */         GlStateManager.disableLighting();
/*  457 */         GlStateManager.disableDepth();
/*  458 */         GlStateManager.disableBlend();
/*  459 */         fr.drawStringWithShadow(s, xPosition + 19 - 2 - fr.getStringWidth(s), yPosition + 6 + 3, 16777215);
/*  460 */         GlStateManager.enableLighting();
/*  461 */         GlStateManager.enableDepth();
/*      */       }
/*      */       
/*  464 */       if (stack.isItemDamaged())
/*      */       {
/*  466 */         int j = (int)Math.round(13.0D - stack.getItemDamage() * 13.0D / stack.getMaxDamage());
/*  467 */         int i = (int)Math.round(255.0D - stack.getItemDamage() * 255.0D / stack.getMaxDamage());
/*  468 */         GlStateManager.disableLighting();
/*  469 */         GlStateManager.disableDepth();
/*  470 */         GlStateManager.disableTexture2D();
/*  471 */         GlStateManager.disableAlpha();
/*  472 */         GlStateManager.disableBlend();
/*  473 */         Tessellator tessellator = Tessellator.getInstance();
/*  474 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  475 */         func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
/*  476 */         func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
/*  477 */         func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, j, 1, 255 - i, i, 0, 255);
/*  478 */         GlStateManager.enableBlend();
/*  479 */         GlStateManager.enableAlpha();
/*  480 */         GlStateManager.enableTexture2D();
/*  481 */         GlStateManager.enableLighting();
/*  482 */         GlStateManager.enableDepth();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void func_181565_a(WorldRenderer p_181565_1_, int p_181565_2_, int p_181565_3_, int p_181565_4_, int p_181565_5_, int p_181565_6_, int p_181565_7_, int p_181565_8_, int p_181565_9_)
/*      */   {
/*  489 */     p_181565_1_.begin(7, DefaultVertexFormats.POSITION_COLOR);
/*  490 */     p_181565_1_.pos(p_181565_2_ + 0, p_181565_3_ + 0, 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
/*  491 */     p_181565_1_.pos(p_181565_2_ + 0, p_181565_3_ + p_181565_5_, 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
/*  492 */     p_181565_1_.pos(p_181565_2_ + p_181565_4_, p_181565_3_ + p_181565_5_, 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
/*  493 */     p_181565_1_.pos(p_181565_2_ + p_181565_4_, p_181565_3_ + 0, 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
/*  494 */     Tessellator.getInstance().draw();
/*      */   }
/*      */   
/*      */   private void registerItems()
/*      */   {
/*  499 */     registerBlock(Blocks.anvil, "anvil_intact");
/*  500 */     registerBlock(Blocks.anvil, 1, "anvil_slightly_damaged");
/*  501 */     registerBlock(Blocks.anvil, 2, "anvil_very_damaged");
/*  502 */     registerBlock(Blocks.carpet, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
/*  503 */     registerBlock(Blocks.carpet, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
/*  504 */     registerBlock(Blocks.carpet, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
/*  505 */     registerBlock(Blocks.carpet, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
/*  506 */     registerBlock(Blocks.carpet, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
/*  507 */     registerBlock(Blocks.carpet, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
/*  508 */     registerBlock(Blocks.carpet, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
/*  509 */     registerBlock(Blocks.carpet, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
/*  510 */     registerBlock(Blocks.carpet, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
/*  511 */     registerBlock(Blocks.carpet, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
/*  512 */     registerBlock(Blocks.carpet, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
/*  513 */     registerBlock(Blocks.carpet, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
/*  514 */     registerBlock(Blocks.carpet, EnumDyeColor.RED.getMetadata(), "red_carpet");
/*  515 */     registerBlock(Blocks.carpet, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
/*  516 */     registerBlock(Blocks.carpet, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
/*  517 */     registerBlock(Blocks.carpet, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
/*  518 */     registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
/*  519 */     registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
/*  520 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
/*  521 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
/*  522 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
/*  523 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
/*  524 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
/*  525 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
/*  526 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
/*  527 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
/*  528 */     registerBlock(Blocks.double_plant, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
/*  529 */     registerBlock(Blocks.leaves, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
/*  530 */     registerBlock(Blocks.leaves, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
/*  531 */     registerBlock(Blocks.leaves, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
/*  532 */     registerBlock(Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
/*  533 */     registerBlock(Blocks.leaves2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
/*  534 */     registerBlock(Blocks.leaves2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
/*  535 */     registerBlock(Blocks.log, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
/*  536 */     registerBlock(Blocks.log, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
/*  537 */     registerBlock(Blocks.log, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
/*  538 */     registerBlock(Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
/*  539 */     registerBlock(Blocks.log2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
/*  540 */     registerBlock(Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
/*  541 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
/*  542 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
/*  543 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
/*  544 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
/*  545 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
/*  546 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
/*  547 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
/*  548 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
/*  549 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
/*  550 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
/*  551 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
/*  552 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
/*  553 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
/*  554 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
/*  555 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
/*  556 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
/*  557 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
/*  558 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
/*  559 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
/*  560 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
/*  561 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
/*  562 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
/*  563 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
/*  564 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
/*  565 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
/*  566 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
/*  567 */     registerBlock(Blocks.red_flower, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
/*  568 */     registerBlock(Blocks.sand, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
/*  569 */     registerBlock(Blocks.sand, BlockSand.EnumType.SAND.getMetadata(), "sand");
/*  570 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
/*  571 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
/*  572 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
/*  573 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
/*  574 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
/*  575 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
/*  576 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
/*  577 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
/*  578 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
/*  579 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
/*  580 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
/*  581 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
/*  582 */     registerBlock(Blocks.sponge, 0, "sponge");
/*  583 */     registerBlock(Blocks.sponge, 1, "sponge_wet");
/*  584 */     registerBlock(Blocks.stained_glass, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
/*  585 */     registerBlock(Blocks.stained_glass, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
/*  586 */     registerBlock(Blocks.stained_glass, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
/*  587 */     registerBlock(Blocks.stained_glass, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
/*  588 */     registerBlock(Blocks.stained_glass, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
/*  589 */     registerBlock(Blocks.stained_glass, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
/*  590 */     registerBlock(Blocks.stained_glass, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
/*  591 */     registerBlock(Blocks.stained_glass, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
/*  592 */     registerBlock(Blocks.stained_glass, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
/*  593 */     registerBlock(Blocks.stained_glass, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
/*  594 */     registerBlock(Blocks.stained_glass, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
/*  595 */     registerBlock(Blocks.stained_glass, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
/*  596 */     registerBlock(Blocks.stained_glass, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
/*  597 */     registerBlock(Blocks.stained_glass, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
/*  598 */     registerBlock(Blocks.stained_glass, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
/*  599 */     registerBlock(Blocks.stained_glass, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
/*  600 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
/*  601 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
/*  602 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
/*  603 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
/*  604 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
/*  605 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
/*  606 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
/*  607 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
/*  608 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
/*  609 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
/*  610 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
/*  611 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
/*  612 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
/*  613 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
/*  614 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
/*  615 */     registerBlock(Blocks.stained_glass_pane, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
/*  616 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
/*  617 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
/*  618 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
/*  619 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
/*  620 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
/*  621 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
/*  622 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
/*  623 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
/*  624 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
/*  625 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
/*  626 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
/*  627 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
/*  628 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
/*  629 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
/*  630 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
/*  631 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
/*  632 */     registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
/*  633 */     registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
/*  634 */     registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
/*  635 */     registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
/*  636 */     registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
/*  637 */     registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
/*  638 */     registerBlock(Blocks.stone, BlockStone.EnumType.STONE.getMetadata(), "stone");
/*  639 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
/*  640 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
/*  641 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
/*  642 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
/*  643 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
/*  644 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
/*  645 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
/*  646 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
/*  647 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
/*  648 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
/*  649 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
/*  650 */     registerBlock(Blocks.stone_slab, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
/*  651 */     registerBlock(Blocks.stone_slab2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
/*  652 */     registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
/*  653 */     registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
/*  654 */     registerBlock(Blocks.tallgrass, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
/*  655 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
/*  656 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
/*  657 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
/*  658 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
/*  659 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
/*  660 */     registerBlock(Blocks.wooden_slab, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
/*  661 */     registerBlock(Blocks.wool, EnumDyeColor.BLACK.getMetadata(), "black_wool");
/*  662 */     registerBlock(Blocks.wool, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
/*  663 */     registerBlock(Blocks.wool, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
/*  664 */     registerBlock(Blocks.wool, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
/*  665 */     registerBlock(Blocks.wool, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
/*  666 */     registerBlock(Blocks.wool, EnumDyeColor.GREEN.getMetadata(), "green_wool");
/*  667 */     registerBlock(Blocks.wool, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
/*  668 */     registerBlock(Blocks.wool, EnumDyeColor.LIME.getMetadata(), "lime_wool");
/*  669 */     registerBlock(Blocks.wool, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
/*  670 */     registerBlock(Blocks.wool, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
/*  671 */     registerBlock(Blocks.wool, EnumDyeColor.PINK.getMetadata(), "pink_wool");
/*  672 */     registerBlock(Blocks.wool, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
/*  673 */     registerBlock(Blocks.wool, EnumDyeColor.RED.getMetadata(), "red_wool");
/*  674 */     registerBlock(Blocks.wool, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
/*  675 */     registerBlock(Blocks.wool, EnumDyeColor.WHITE.getMetadata(), "white_wool");
/*  676 */     registerBlock(Blocks.wool, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
/*  677 */     registerBlock(Blocks.acacia_stairs, "acacia_stairs");
/*  678 */     registerBlock(Blocks.activator_rail, "activator_rail");
/*  679 */     registerBlock(Blocks.beacon, "beacon");
/*  680 */     registerBlock(Blocks.bedrock, "bedrock");
/*  681 */     registerBlock(Blocks.birch_stairs, "birch_stairs");
/*  682 */     registerBlock(Blocks.bookshelf, "bookshelf");
/*  683 */     registerBlock(Blocks.brick_block, "brick_block");
/*  684 */     registerBlock(Blocks.brick_block, "brick_block");
/*  685 */     registerBlock(Blocks.brick_stairs, "brick_stairs");
/*  686 */     registerBlock(Blocks.brown_mushroom, "brown_mushroom");
/*  687 */     registerBlock(Blocks.cactus, "cactus");
/*  688 */     registerBlock(Blocks.clay, "clay");
/*  689 */     registerBlock(Blocks.coal_block, "coal_block");
/*  690 */     registerBlock(Blocks.coal_ore, "coal_ore");
/*  691 */     registerBlock(Blocks.cobblestone, "cobblestone");
/*  692 */     registerBlock(Blocks.crafting_table, "crafting_table");
/*  693 */     registerBlock(Blocks.dark_oak_stairs, "dark_oak_stairs");
/*  694 */     registerBlock(Blocks.daylight_detector, "daylight_detector");
/*  695 */     registerBlock(Blocks.deadbush, "dead_bush");
/*  696 */     registerBlock(Blocks.detector_rail, "detector_rail");
/*  697 */     registerBlock(Blocks.diamond_block, "diamond_block");
/*  698 */     registerBlock(Blocks.diamond_ore, "diamond_ore");
/*  699 */     registerBlock(Blocks.dispenser, "dispenser");
/*  700 */     registerBlock(Blocks.dropper, "dropper");
/*  701 */     registerBlock(Blocks.emerald_block, "emerald_block");
/*  702 */     registerBlock(Blocks.emerald_ore, "emerald_ore");
/*  703 */     registerBlock(Blocks.enchanting_table, "enchanting_table");
/*  704 */     registerBlock(Blocks.end_portal_frame, "end_portal_frame");
/*  705 */     registerBlock(Blocks.end_stone, "end_stone");
/*  706 */     registerBlock(Blocks.oak_fence, "oak_fence");
/*  707 */     registerBlock(Blocks.spruce_fence, "spruce_fence");
/*  708 */     registerBlock(Blocks.birch_fence, "birch_fence");
/*  709 */     registerBlock(Blocks.jungle_fence, "jungle_fence");
/*  710 */     registerBlock(Blocks.dark_oak_fence, "dark_oak_fence");
/*  711 */     registerBlock(Blocks.acacia_fence, "acacia_fence");
/*  712 */     registerBlock(Blocks.oak_fence_gate, "oak_fence_gate");
/*  713 */     registerBlock(Blocks.spruce_fence_gate, "spruce_fence_gate");
/*  714 */     registerBlock(Blocks.birch_fence_gate, "birch_fence_gate");
/*  715 */     registerBlock(Blocks.jungle_fence_gate, "jungle_fence_gate");
/*  716 */     registerBlock(Blocks.dark_oak_fence_gate, "dark_oak_fence_gate");
/*  717 */     registerBlock(Blocks.acacia_fence_gate, "acacia_fence_gate");
/*  718 */     registerBlock(Blocks.furnace, "furnace");
/*  719 */     registerBlock(Blocks.glass, "glass");
/*  720 */     registerBlock(Blocks.glass_pane, "glass_pane");
/*  721 */     registerBlock(Blocks.glowstone, "glowstone");
/*  722 */     registerBlock(Blocks.golden_rail, "golden_rail");
/*  723 */     registerBlock(Blocks.gold_block, "gold_block");
/*  724 */     registerBlock(Blocks.gold_ore, "gold_ore");
/*  725 */     registerBlock(Blocks.grass, "grass");
/*  726 */     registerBlock(Blocks.gravel, "gravel");
/*  727 */     registerBlock(Blocks.hardened_clay, "hardened_clay");
/*  728 */     registerBlock(Blocks.hay_block, "hay_block");
/*  729 */     registerBlock(Blocks.heavy_weighted_pressure_plate, "heavy_weighted_pressure_plate");
/*  730 */     registerBlock(Blocks.hopper, "hopper");
/*  731 */     registerBlock(Blocks.ice, "ice");
/*  732 */     registerBlock(Blocks.iron_bars, "iron_bars");
/*  733 */     registerBlock(Blocks.iron_block, "iron_block");
/*  734 */     registerBlock(Blocks.iron_ore, "iron_ore");
/*  735 */     registerBlock(Blocks.iron_trapdoor, "iron_trapdoor");
/*  736 */     registerBlock(Blocks.jukebox, "jukebox");
/*  737 */     registerBlock(Blocks.jungle_stairs, "jungle_stairs");
/*  738 */     registerBlock(Blocks.ladder, "ladder");
/*  739 */     registerBlock(Blocks.lapis_block, "lapis_block");
/*  740 */     registerBlock(Blocks.lapis_ore, "lapis_ore");
/*  741 */     registerBlock(Blocks.lever, "lever");
/*  742 */     registerBlock(Blocks.light_weighted_pressure_plate, "light_weighted_pressure_plate");
/*  743 */     registerBlock(Blocks.lit_pumpkin, "lit_pumpkin");
/*  744 */     registerBlock(Blocks.melon_block, "melon_block");
/*  745 */     registerBlock(Blocks.mossy_cobblestone, "mossy_cobblestone");
/*  746 */     registerBlock(Blocks.mycelium, "mycelium");
/*  747 */     registerBlock(Blocks.netherrack, "netherrack");
/*  748 */     registerBlock(Blocks.nether_brick, "nether_brick");
/*  749 */     registerBlock(Blocks.nether_brick_fence, "nether_brick_fence");
/*  750 */     registerBlock(Blocks.nether_brick_stairs, "nether_brick_stairs");
/*  751 */     registerBlock(Blocks.noteblock, "noteblock");
/*  752 */     registerBlock(Blocks.oak_stairs, "oak_stairs");
/*  753 */     registerBlock(Blocks.obsidian, "obsidian");
/*  754 */     registerBlock(Blocks.packed_ice, "packed_ice");
/*  755 */     registerBlock(Blocks.piston, "piston");
/*  756 */     registerBlock(Blocks.pumpkin, "pumpkin");
/*  757 */     registerBlock(Blocks.quartz_ore, "quartz_ore");
/*  758 */     registerBlock(Blocks.quartz_stairs, "quartz_stairs");
/*  759 */     registerBlock(Blocks.rail, "rail");
/*  760 */     registerBlock(Blocks.redstone_block, "redstone_block");
/*  761 */     registerBlock(Blocks.redstone_lamp, "redstone_lamp");
/*  762 */     registerBlock(Blocks.redstone_ore, "redstone_ore");
/*  763 */     registerBlock(Blocks.redstone_torch, "redstone_torch");
/*  764 */     registerBlock(Blocks.red_mushroom, "red_mushroom");
/*  765 */     registerBlock(Blocks.sandstone_stairs, "sandstone_stairs");
/*  766 */     registerBlock(Blocks.red_sandstone_stairs, "red_sandstone_stairs");
/*  767 */     registerBlock(Blocks.sea_lantern, "sea_lantern");
/*  768 */     registerBlock(Blocks.slime_block, "slime");
/*  769 */     registerBlock(Blocks.snow, "snow");
/*  770 */     registerBlock(Blocks.snow_layer, "snow_layer");
/*  771 */     registerBlock(Blocks.soul_sand, "soul_sand");
/*  772 */     registerBlock(Blocks.spruce_stairs, "spruce_stairs");
/*  773 */     registerBlock(Blocks.sticky_piston, "sticky_piston");
/*  774 */     registerBlock(Blocks.stone_brick_stairs, "stone_brick_stairs");
/*  775 */     registerBlock(Blocks.stone_button, "stone_button");
/*  776 */     registerBlock(Blocks.stone_pressure_plate, "stone_pressure_plate");
/*  777 */     registerBlock(Blocks.stone_stairs, "stone_stairs");
/*  778 */     registerBlock(Blocks.tnt, "tnt");
/*  779 */     registerBlock(Blocks.torch, "torch");
/*  780 */     registerBlock(Blocks.trapdoor, "trapdoor");
/*  781 */     registerBlock(Blocks.tripwire_hook, "tripwire_hook");
/*  782 */     registerBlock(Blocks.vine, "vine");
/*  783 */     registerBlock(Blocks.waterlily, "waterlily");
/*  784 */     registerBlock(Blocks.web, "web");
/*  785 */     registerBlock(Blocks.wooden_button, "wooden_button");
/*  786 */     registerBlock(Blocks.wooden_pressure_plate, "wooden_pressure_plate");
/*  787 */     registerBlock(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
/*  788 */     registerBlock(Blocks.chest, "chest");
/*  789 */     registerBlock(Blocks.trapped_chest, "trapped_chest");
/*  790 */     registerBlock(Blocks.ender_chest, "ender_chest");
/*  791 */     registerItem(Items.iron_shovel, "iron_shovel");
/*  792 */     registerItem(Items.iron_pickaxe, "iron_pickaxe");
/*  793 */     registerItem(Items.iron_axe, "iron_axe");
/*  794 */     registerItem(Items.flint_and_steel, "flint_and_steel");
/*  795 */     registerItem(Items.apple, "apple");
/*  796 */     registerItem(Items.bow, 0, "bow");
/*  797 */     registerItem(Items.bow, 1, "bow_pulling_0");
/*  798 */     registerItem(Items.bow, 2, "bow_pulling_1");
/*  799 */     registerItem(Items.bow, 3, "bow_pulling_2");
/*  800 */     registerItem(Items.arrow, "arrow");
/*  801 */     registerItem(Items.coal, 0, "coal");
/*  802 */     registerItem(Items.coal, 1, "charcoal");
/*  803 */     registerItem(Items.diamond, "diamond");
/*  804 */     registerItem(Items.iron_ingot, "iron_ingot");
/*  805 */     registerItem(Items.gold_ingot, "gold_ingot");
/*  806 */     registerItem(Items.iron_sword, "iron_sword");
/*  807 */     registerItem(Items.wooden_sword, "wooden_sword");
/*  808 */     registerItem(Items.wooden_shovel, "wooden_shovel");
/*  809 */     registerItem(Items.wooden_pickaxe, "wooden_pickaxe");
/*  810 */     registerItem(Items.wooden_axe, "wooden_axe");
/*  811 */     registerItem(Items.stone_sword, "stone_sword");
/*  812 */     registerItem(Items.stone_shovel, "stone_shovel");
/*  813 */     registerItem(Items.stone_pickaxe, "stone_pickaxe");
/*  814 */     registerItem(Items.stone_axe, "stone_axe");
/*  815 */     registerItem(Items.diamond_sword, "diamond_sword");
/*  816 */     registerItem(Items.diamond_shovel, "diamond_shovel");
/*  817 */     registerItem(Items.diamond_pickaxe, "diamond_pickaxe");
/*  818 */     registerItem(Items.diamond_axe, "diamond_axe");
/*  819 */     registerItem(Items.stick, "stick");
/*  820 */     registerItem(Items.bowl, "bowl");
/*  821 */     registerItem(Items.mushroom_stew, "mushroom_stew");
/*  822 */     registerItem(Items.golden_sword, "golden_sword");
/*  823 */     registerItem(Items.golden_shovel, "golden_shovel");
/*  824 */     registerItem(Items.golden_pickaxe, "golden_pickaxe");
/*  825 */     registerItem(Items.golden_axe, "golden_axe");
/*  826 */     registerItem(Items.string, "string");
/*  827 */     registerItem(Items.feather, "feather");
/*  828 */     registerItem(Items.gunpowder, "gunpowder");
/*  829 */     registerItem(Items.wooden_hoe, "wooden_hoe");
/*  830 */     registerItem(Items.stone_hoe, "stone_hoe");
/*  831 */     registerItem(Items.iron_hoe, "iron_hoe");
/*  832 */     registerItem(Items.diamond_hoe, "diamond_hoe");
/*  833 */     registerItem(Items.golden_hoe, "golden_hoe");
/*  834 */     registerItem(Items.wheat_seeds, "wheat_seeds");
/*  835 */     registerItem(Items.wheat, "wheat");
/*  836 */     registerItem(Items.bread, "bread");
/*  837 */     registerItem(Items.leather_helmet, "leather_helmet");
/*  838 */     registerItem(Items.leather_chestplate, "leather_chestplate");
/*  839 */     registerItem(Items.leather_leggings, "leather_leggings");
/*  840 */     registerItem(Items.leather_boots, "leather_boots");
/*  841 */     registerItem(Items.chainmail_helmet, "chainmail_helmet");
/*  842 */     registerItem(Items.chainmail_chestplate, "chainmail_chestplate");
/*  843 */     registerItem(Items.chainmail_leggings, "chainmail_leggings");
/*  844 */     registerItem(Items.chainmail_boots, "chainmail_boots");
/*  845 */     registerItem(Items.iron_helmet, "iron_helmet");
/*  846 */     registerItem(Items.iron_chestplate, "iron_chestplate");
/*  847 */     registerItem(Items.iron_leggings, "iron_leggings");
/*  848 */     registerItem(Items.iron_boots, "iron_boots");
/*  849 */     registerItem(Items.diamond_helmet, "diamond_helmet");
/*  850 */     registerItem(Items.diamond_chestplate, "diamond_chestplate");
/*  851 */     registerItem(Items.diamond_leggings, "diamond_leggings");
/*  852 */     registerItem(Items.diamond_boots, "diamond_boots");
/*  853 */     registerItem(Items.golden_helmet, "golden_helmet");
/*  854 */     registerItem(Items.golden_chestplate, "golden_chestplate");
/*  855 */     registerItem(Items.golden_leggings, "golden_leggings");
/*  856 */     registerItem(Items.golden_boots, "golden_boots");
/*  857 */     registerItem(Items.flint, "flint");
/*  858 */     registerItem(Items.porkchop, "porkchop");
/*  859 */     registerItem(Items.cooked_porkchop, "cooked_porkchop");
/*  860 */     registerItem(Items.painting, "painting");
/*  861 */     registerItem(Items.golden_apple, "golden_apple");
/*  862 */     registerItem(Items.golden_apple, 1, "golden_apple");
/*  863 */     registerItem(Items.sign, "sign");
/*  864 */     registerItem(Items.oak_door, "oak_door");
/*  865 */     registerItem(Items.spruce_door, "spruce_door");
/*  866 */     registerItem(Items.birch_door, "birch_door");
/*  867 */     registerItem(Items.jungle_door, "jungle_door");
/*  868 */     registerItem(Items.acacia_door, "acacia_door");
/*  869 */     registerItem(Items.dark_oak_door, "dark_oak_door");
/*  870 */     registerItem(Items.bucket, "bucket");
/*  871 */     registerItem(Items.water_bucket, "water_bucket");
/*  872 */     registerItem(Items.lava_bucket, "lava_bucket");
/*  873 */     registerItem(Items.minecart, "minecart");
/*  874 */     registerItem(Items.saddle, "saddle");
/*  875 */     registerItem(Items.iron_door, "iron_door");
/*  876 */     registerItem(Items.redstone, "redstone");
/*  877 */     registerItem(Items.snowball, "snowball");
/*  878 */     registerItem(Items.boat, "boat");
/*  879 */     registerItem(Items.leather, "leather");
/*  880 */     registerItem(Items.milk_bucket, "milk_bucket");
/*  881 */     registerItem(Items.brick, "brick");
/*  882 */     registerItem(Items.clay_ball, "clay_ball");
/*  883 */     registerItem(Items.reeds, "reeds");
/*  884 */     registerItem(Items.paper, "paper");
/*  885 */     registerItem(Items.book, "book");
/*  886 */     registerItem(Items.slime_ball, "slime_ball");
/*  887 */     registerItem(Items.chest_minecart, "chest_minecart");
/*  888 */     registerItem(Items.furnace_minecart, "furnace_minecart");
/*  889 */     registerItem(Items.egg, "egg");
/*  890 */     registerItem(Items.compass, "compass");
/*  891 */     registerItem(Items.fishing_rod, "fishing_rod");
/*  892 */     registerItem(Items.fishing_rod, 1, "fishing_rod_cast");
/*  893 */     registerItem(Items.clock, "clock");
/*  894 */     registerItem(Items.glowstone_dust, "glowstone_dust");
/*  895 */     registerItem(Items.fish, ItemFishFood.FishType.COD.getMetadata(), "cod");
/*  896 */     registerItem(Items.fish, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
/*  897 */     registerItem(Items.fish, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
/*  898 */     registerItem(Items.fish, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
/*  899 */     registerItem(Items.cooked_fish, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
/*  900 */     registerItem(Items.cooked_fish, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
/*  901 */     registerItem(Items.dye, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
/*  902 */     registerItem(Items.dye, EnumDyeColor.RED.getDyeDamage(), "dye_red");
/*  903 */     registerItem(Items.dye, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
/*  904 */     registerItem(Items.dye, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
/*  905 */     registerItem(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
/*  906 */     registerItem(Items.dye, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
/*  907 */     registerItem(Items.dye, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
/*  908 */     registerItem(Items.dye, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
/*  909 */     registerItem(Items.dye, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
/*  910 */     registerItem(Items.dye, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
/*  911 */     registerItem(Items.dye, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
/*  912 */     registerItem(Items.dye, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
/*  913 */     registerItem(Items.dye, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
/*  914 */     registerItem(Items.dye, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
/*  915 */     registerItem(Items.dye, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
/*  916 */     registerItem(Items.dye, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
/*  917 */     registerItem(Items.bone, "bone");
/*  918 */     registerItem(Items.sugar, "sugar");
/*  919 */     registerItem(Items.cake, "cake");
/*  920 */     registerItem(Items.bed, "bed");
/*  921 */     registerItem(Items.repeater, "repeater");
/*  922 */     registerItem(Items.cookie, "cookie");
/*  923 */     registerItem(Items.shears, "shears");
/*  924 */     registerItem(Items.melon, "melon");
/*  925 */     registerItem(Items.pumpkin_seeds, "pumpkin_seeds");
/*  926 */     registerItem(Items.melon_seeds, "melon_seeds");
/*  927 */     registerItem(Items.beef, "beef");
/*  928 */     registerItem(Items.cooked_beef, "cooked_beef");
/*  929 */     registerItem(Items.chicken, "chicken");
/*  930 */     registerItem(Items.cooked_chicken, "cooked_chicken");
/*  931 */     registerItem(Items.rabbit, "rabbit");
/*  932 */     registerItem(Items.cooked_rabbit, "cooked_rabbit");
/*  933 */     registerItem(Items.mutton, "mutton");
/*  934 */     registerItem(Items.cooked_mutton, "cooked_mutton");
/*  935 */     registerItem(Items.rabbit_foot, "rabbit_foot");
/*  936 */     registerItem(Items.rabbit_hide, "rabbit_hide");
/*  937 */     registerItem(Items.rabbit_stew, "rabbit_stew");
/*  938 */     registerItem(Items.rotten_flesh, "rotten_flesh");
/*  939 */     registerItem(Items.ender_pearl, "ender_pearl");
/*  940 */     registerItem(Items.blaze_rod, "blaze_rod");
/*  941 */     registerItem(Items.ghast_tear, "ghast_tear");
/*  942 */     registerItem(Items.gold_nugget, "gold_nugget");
/*  943 */     registerItem(Items.nether_wart, "nether_wart");
/*  944 */     this.itemModelMesher.register(Items.potionitem, new ItemMeshDefinition()
/*      */     {
/*      */       public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */       {
/*  948 */         return ItemPotion.isSplash(stack.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
/*      */       }
/*  950 */     });
/*  951 */     registerItem(Items.glass_bottle, "glass_bottle");
/*  952 */     registerItem(Items.spider_eye, "spider_eye");
/*  953 */     registerItem(Items.fermented_spider_eye, "fermented_spider_eye");
/*  954 */     registerItem(Items.blaze_powder, "blaze_powder");
/*  955 */     registerItem(Items.magma_cream, "magma_cream");
/*  956 */     registerItem(Items.brewing_stand, "brewing_stand");
/*  957 */     registerItem(Items.cauldron, "cauldron");
/*  958 */     registerItem(Items.ender_eye, "ender_eye");
/*  959 */     registerItem(Items.speckled_melon, "speckled_melon");
/*  960 */     this.itemModelMesher.register(Items.spawn_egg, new ItemMeshDefinition()
/*      */     {
/*      */       public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */       {
/*  964 */         return new ModelResourceLocation("spawn_egg", "inventory");
/*      */       }
/*  966 */     });
/*  967 */     registerItem(Items.experience_bottle, "experience_bottle");
/*  968 */     registerItem(Items.fire_charge, "fire_charge");
/*  969 */     registerItem(Items.writable_book, "writable_book");
/*  970 */     registerItem(Items.emerald, "emerald");
/*  971 */     registerItem(Items.item_frame, "item_frame");
/*  972 */     registerItem(Items.flower_pot, "flower_pot");
/*  973 */     registerItem(Items.carrot, "carrot");
/*  974 */     registerItem(Items.potato, "potato");
/*  975 */     registerItem(Items.baked_potato, "baked_potato");
/*  976 */     registerItem(Items.poisonous_potato, "poisonous_potato");
/*  977 */     registerItem(Items.map, "map");
/*  978 */     registerItem(Items.golden_carrot, "golden_carrot");
/*  979 */     registerItem(Items.skull, 0, "skull_skeleton");
/*  980 */     registerItem(Items.skull, 1, "skull_wither");
/*  981 */     registerItem(Items.skull, 2, "skull_zombie");
/*  982 */     registerItem(Items.skull, 3, "skull_char");
/*  983 */     registerItem(Items.skull, 4, "skull_creeper");
/*  984 */     registerItem(Items.carrot_on_a_stick, "carrot_on_a_stick");
/*  985 */     registerItem(Items.nether_star, "nether_star");
/*  986 */     registerItem(Items.pumpkin_pie, "pumpkin_pie");
/*  987 */     registerItem(Items.firework_charge, "firework_charge");
/*  988 */     registerItem(Items.comparator, "comparator");
/*  989 */     registerItem(Items.netherbrick, "netherbrick");
/*  990 */     registerItem(Items.quartz, "quartz");
/*  991 */     registerItem(Items.tnt_minecart, "tnt_minecart");
/*  992 */     registerItem(Items.hopper_minecart, "hopper_minecart");
/*  993 */     registerItem(Items.armor_stand, "armor_stand");
/*  994 */     registerItem(Items.iron_horse_armor, "iron_horse_armor");
/*  995 */     registerItem(Items.golden_horse_armor, "golden_horse_armor");
/*  996 */     registerItem(Items.diamond_horse_armor, "diamond_horse_armor");
/*  997 */     registerItem(Items.lead, "lead");
/*  998 */     registerItem(Items.name_tag, "name_tag");
/*  999 */     this.itemModelMesher.register(Items.banner, new ItemMeshDefinition()
/*      */     {
/*      */       public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */       {
/* 1003 */         return new ModelResourceLocation("banner", "inventory");
/*      */       }
/* 1005 */     });
/* 1006 */     registerItem(Items.record_13, "record_13");
/* 1007 */     registerItem(Items.record_cat, "record_cat");
/* 1008 */     registerItem(Items.record_blocks, "record_blocks");
/* 1009 */     registerItem(Items.record_chirp, "record_chirp");
/* 1010 */     registerItem(Items.record_far, "record_far");
/* 1011 */     registerItem(Items.record_mall, "record_mall");
/* 1012 */     registerItem(Items.record_mellohi, "record_mellohi");
/* 1013 */     registerItem(Items.record_stal, "record_stal");
/* 1014 */     registerItem(Items.record_strad, "record_strad");
/* 1015 */     registerItem(Items.record_ward, "record_ward");
/* 1016 */     registerItem(Items.record_11, "record_11");
/* 1017 */     registerItem(Items.record_wait, "record_wait");
/* 1018 */     registerItem(Items.prismarine_shard, "prismarine_shard");
/* 1019 */     registerItem(Items.prismarine_crystals, "prismarine_crystals");
/* 1020 */     this.itemModelMesher.register(Items.enchanted_book, new ItemMeshDefinition()
/*      */     {
/*      */       public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */       {
/* 1024 */         return new ModelResourceLocation("enchanted_book", "inventory");
/*      */       }
/* 1026 */     });
/* 1027 */     this.itemModelMesher.register(Items.filled_map, new ItemMeshDefinition()
/*      */     {
/*      */       public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */       {
/* 1031 */         return new ModelResourceLocation("filled_map", "inventory");
/*      */       }
/* 1033 */     });
/* 1034 */     registerBlock(Blocks.command_block, "command_block");
/* 1035 */     registerItem(Items.fireworks, "fireworks");
/* 1036 */     registerItem(Items.command_block_minecart, "command_block_minecart");
/* 1037 */     registerBlock(Blocks.barrier, "barrier");
/* 1038 */     registerBlock(Blocks.mob_spawner, "mob_spawner");
/* 1039 */     registerItem(Items.written_book, "written_book");
/* 1040 */     registerBlock(Blocks.brown_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
/* 1041 */     registerBlock(Blocks.red_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
/* 1042 */     registerBlock(Blocks.dragon_egg, "dragon_egg");
/*      */   }
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager)
/*      */   {
/* 1047 */     this.itemModelMesher.rebuildCache();
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */