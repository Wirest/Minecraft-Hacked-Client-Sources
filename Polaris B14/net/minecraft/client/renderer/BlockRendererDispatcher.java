/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.client.resources.model.SimpleBakedModel.Builder;
/*     */ import net.minecraft.client.resources.model.WeightedBakedModel;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ public class BlockRendererDispatcher implements IResourceManagerReloadListener
/*     */ {
/*     */   private BlockModelShapes blockModelShapes;
/*     */   private final GameSettings gameSettings;
/*  24 */   private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
/*  25 */   private final ChestRenderer chestRenderer = new ChestRenderer();
/*  26 */   private final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();
/*     */   
/*     */   public BlockRendererDispatcher(BlockModelShapes blockModelShapesIn, GameSettings gameSettingsIn)
/*     */   {
/*  30 */     this.blockModelShapes = blockModelShapesIn;
/*  31 */     this.gameSettings = gameSettingsIn;
/*     */   }
/*     */   
/*     */   public BlockModelShapes getBlockModelShapes()
/*     */   {
/*  36 */     return this.blockModelShapes;
/*     */   }
/*     */   
/*     */   public void renderBlockDamage(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess)
/*     */   {
/*  41 */     Block block = state.getBlock();
/*  42 */     int i = block.getRenderType();
/*     */     
/*  44 */     if (i == 3)
/*     */     {
/*  46 */       state = block.getActualState(state, blockAccess, pos);
/*  47 */       IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(state);
/*  48 */       IBakedModel ibakedmodel1 = new SimpleBakedModel.Builder(ibakedmodel, texture).makeBakedModel();
/*  49 */       this.blockModelRenderer.renderModel(blockAccess, ibakedmodel1, state, pos, Tessellator.getInstance().getWorldRenderer());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       int i = state.getBlock().getRenderType();
/*     */       
/*  59 */       if (i == -1)
/*     */       {
/*  61 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  65 */       switch (i)
/*     */       {
/*     */       case 1: 
/*  68 */         return this.fluidRenderer.renderFluid(blockAccess, state, pos, worldRendererIn);
/*     */       
/*     */       case 2: 
/*  71 */         return false;
/*     */       
/*     */       case 3: 
/*  74 */         IBakedModel ibakedmodel = getModelFromBlockState(state, blockAccess, pos);
/*  75 */         return this.blockModelRenderer.renderModel(blockAccess, ibakedmodel, state, pos, worldRendererIn);
/*     */       }
/*     */       
/*  78 */       return false;
/*     */ 
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*     */ 
/*  84 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
/*  85 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
/*  86 */       CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
/*  87 */       throw new ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */   
/*     */   public BlockModelRenderer getBlockModelRenderer()
/*     */   {
/*  93 */     return this.blockModelRenderer;
/*     */   }
/*     */   
/*     */   private IBakedModel getBakedModel(IBlockState state, BlockPos pos)
/*     */   {
/*  98 */     IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(state);
/*     */     
/* 100 */     if ((pos != null) && (this.gameSettings.allowBlockAlternatives) && ((ibakedmodel instanceof WeightedBakedModel)))
/*     */     {
/* 102 */       ibakedmodel = ((WeightedBakedModel)ibakedmodel).getAlternativeModel(MathHelper.getPositionRandom(pos));
/*     */     }
/*     */     
/* 105 */     return ibakedmodel;
/*     */   }
/*     */   
/*     */   public IBakedModel getModelFromBlockState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 110 */     Block block = state.getBlock();
/*     */     
/* 112 */     if (worldIn.getWorldType() != WorldType.DEBUG_WORLD)
/*     */     {
/*     */       try
/*     */       {
/* 116 */         state = block.getActualState(state, worldIn, pos);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 124 */     IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(state);
/*     */     
/* 126 */     if ((pos != null) && (this.gameSettings.allowBlockAlternatives) && ((ibakedmodel instanceof WeightedBakedModel)))
/*     */     {
/* 128 */       ibakedmodel = ((WeightedBakedModel)ibakedmodel).getAlternativeModel(MathHelper.getPositionRandom(pos));
/*     */     }
/*     */     
/* 131 */     return ibakedmodel;
/*     */   }
/*     */   
/*     */   public void renderBlockBrightness(IBlockState state, float brightness)
/*     */   {
/* 136 */     int i = state.getBlock().getRenderType();
/*     */     
/* 138 */     if (i != -1)
/*     */     {
/* 140 */       switch (i)
/*     */       {
/*     */       case 1: 
/*     */       default: 
/*     */         break;
/*     */       
/*     */       case 2: 
/* 147 */         this.chestRenderer.renderChestBrightness(state.getBlock(), brightness);
/* 148 */         break;
/*     */       
/*     */       case 3: 
/* 151 */         IBakedModel ibakedmodel = getBakedModel(state, null);
/* 152 */         this.blockModelRenderer.renderModelBrightness(ibakedmodel, state, brightness, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isRenderTypeChest(Block p_175021_1_, int p_175021_2_)
/*     */   {
/* 159 */     if (p_175021_1_ == null)
/*     */     {
/* 161 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 165 */     int i = p_175021_1_.getRenderType();
/* 166 */     return i != 3;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onResourceManagerReload(IResourceManager resourceManager)
/*     */   {
/* 172 */     this.fluidRenderer.initAtlasSprites();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\BlockRendererDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */