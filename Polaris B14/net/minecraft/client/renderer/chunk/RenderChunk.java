/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockRedstoneWire;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RegionRenderCache;
/*     */ import net.minecraft.client.renderer.RegionRenderCacheBuilder;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.World;
/*     */ import optfine.BlockPosM;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ 
/*     */ public class RenderChunk
/*     */ {
/*     */   private World world;
/*     */   private final RenderGlobal renderGlobal;
/*     */   public static int renderChunksUpdated;
/*     */   private BlockPos position;
/*  41 */   public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
/*  42 */   private final ReentrantLock lockCompileTask = new ReentrantLock();
/*  43 */   private final ReentrantLock lockCompiledChunk = new ReentrantLock();
/*  44 */   private ChunkCompileTaskGenerator compileTask = null;
/*  45 */   private final Set field_181056_j = Sets.newHashSet();
/*     */   private final int index;
/*  47 */   private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
/*  48 */   private final VertexBuffer[] vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
/*     */   public AxisAlignedBB boundingBox;
/*  50 */   private int frameIndex = -1;
/*  51 */   private boolean needsUpdate = true;
/*     */   private EnumMap field_181702_p;
/*     */   private static final String __OBFID = "CL_00002452";
/*  54 */   private BlockPos[] positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
/*  55 */   private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = ;
/*  56 */   private EnumWorldBlockLayer[] blockLayersSingle = new EnumWorldBlockLayer[1];
/*     */   
/*     */   public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn)
/*     */   {
/*  60 */     this.world = worldIn;
/*  61 */     this.renderGlobal = renderGlobalIn;
/*  62 */     this.index = indexIn;
/*     */     
/*  64 */     if (!blockPosIn.equals(getPosition()))
/*     */     {
/*  66 */       setPosition(blockPosIn);
/*     */     }
/*     */     
/*  69 */     if (OpenGlHelper.useVbo())
/*     */     {
/*  71 */       for (int i = 0; i < EnumWorldBlockLayer.values().length; i++)
/*     */       {
/*  73 */         this.vertexBuffers[i] = new VertexBuffer(DefaultVertexFormats.BLOCK);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean setFrameIndex(int frameIndexIn)
/*     */   {
/*  80 */     if (this.frameIndex == frameIndexIn)
/*     */     {
/*  82 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  86 */     this.frameIndex = frameIndexIn;
/*  87 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public VertexBuffer getVertexBufferByLayer(int layer)
/*     */   {
/*  93 */     return this.vertexBuffers[layer];
/*     */   }
/*     */   
/*     */   public void setPosition(BlockPos pos)
/*     */   {
/*  98 */     stopCompileTask();
/*  99 */     this.position = pos;
/* 100 */     this.boundingBox = new AxisAlignedBB(pos, pos.add(16, 16, 16));
/* 101 */     EnumFacing[] aenumfacing = EnumFacing.values();
/* 102 */     int i = aenumfacing.length;
/* 103 */     initModelviewMatrix();
/*     */     
/* 105 */     for (int j = 0; j < this.positionOffsets16.length; j++)
/*     */     {
/* 107 */       this.positionOffsets16[j] = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator)
/*     */   {
/* 113 */     CompiledChunk compiledchunk = generator.getCompiledChunk();
/*     */     
/* 115 */     if ((compiledchunk.getState() != null) && (!compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)))
/*     */     {
/* 117 */       WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
/* 118 */       preRenderBlocks(worldrenderer, this.position);
/* 119 */       worldrenderer.setVertexState(compiledchunk.getState());
/* 120 */       postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, x, y, z, worldrenderer, compiledchunk);
/*     */     }
/*     */   }
/*     */   
/*     */   public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator)
/*     */   {
/* 126 */     CompiledChunk compiledchunk = new CompiledChunk();
/* 127 */     boolean flag = true;
/* 128 */     BlockPos blockpos = this.position;
/* 129 */     BlockPos blockpos1 = blockpos.add(15, 15, 15);
/* 130 */     generator.getLock().lock();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 135 */       if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
/*     */       {
/* 137 */         return;
/*     */       }
/*     */       
/* 140 */       if (this.world == null)
/*     */       {
/* 142 */         return;
/*     */       }
/*     */       
/* 145 */       RegionRenderCache regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
/* 146 */       generator.setCompiledChunk(compiledchunk);
/*     */     }
/*     */     finally
/*     */     {
/* 150 */       generator.getLock().unlock(); } RegionRenderCache regionrendercache; generator.getLock().unlock();
/*     */     
/*     */ 
/* 153 */     VisGraph var10 = new VisGraph();
/* 154 */     HashSet var11 = Sets.newHashSet();
/*     */     
/* 156 */     if (!regionrendercache.extendedLevelsInChunkCache())
/*     */     {
/* 158 */       renderChunksUpdated += 1;
/* 159 */       boolean[] aboolean = new boolean[EnumWorldBlockLayer.values().length];
/* 160 */       BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 161 */       iterator = BlockPosM.getAllInBoxMutable(blockpos, blockpos1).iterator();
/* 162 */       boolean flag1 = Reflector.ForgeBlock_hasTileEntity.exists();
/* 163 */       boolean flag2 = Reflector.ForgeBlock_canRenderInLayer.exists();
/* 164 */       boolean flag3 = Reflector.ForgeHooksClient_setRenderLayer.exists();
/*     */       boolean flag4;
/* 166 */       EnumWorldBlockLayer[] aenumworldblocklayer; int i; for (; iterator.hasNext(); 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */           i < aenumworldblocklayer.length)
/*     */       {
/* 168 */         BlockPosM blockposm = (BlockPosM)iterator.next();
/* 169 */         iblockstate = regionrendercache.getBlockState(blockposm);
/* 170 */         block = iblockstate.getBlock();
/*     */         
/* 172 */         if (block.isOpaqueCube())
/*     */         {
/* 174 */           var10.func_178606_a(blockposm);
/*     */         }
/*     */         
/*     */         boolean flag4;
/*     */         
/* 179 */         if (flag1)
/*     */         {
/* 181 */           flag4 = Reflector.callBoolean(iterator, Reflector.ForgeBlock_hasTileEntity, new Object[] { blockrendererdispatcher });
/*     */         }
/*     */         else
/*     */         {
/* 185 */           flag4 = block.hasTileEntity();
/*     */         }
/*     */         
/* 188 */         if (flag4)
/*     */         {
/* 190 */           net.minecraft.tileentity.TileEntity tileentity = regionrendercache.getTileEntity(new BlockPos(blockposm));
/* 191 */           TileEntitySpecialRenderer tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);
/*     */           
/* 193 */           if ((tileentity != null) && (tileentityspecialrenderer != null))
/*     */           {
/* 195 */             compiledchunk.addTileEntity(tileentity);
/*     */             
/* 197 */             if (tileentityspecialrenderer.func_181055_a())
/*     */             {
/* 199 */               var11.add(tileentity);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */         EnumWorldBlockLayer[] aenumworldblocklayer;
/*     */         
/* 206 */         if (flag2)
/*     */         {
/* 208 */           aenumworldblocklayer = ENUM_WORLD_BLOCK_LAYERS;
/*     */         }
/*     */         else
/*     */         {
/* 212 */           aenumworldblocklayer = this.blockLayersSingle;
/* 213 */           aenumworldblocklayer[0] = block.getBlockLayer();
/*     */         }
/*     */         
/* 216 */         i = 0; continue;
/*     */         
/* 218 */         EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
/*     */         
/* 220 */         if (flag2)
/*     */         {
/* 222 */           boolean flag5 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, new Object[] { enumworldblocklayer });
/*     */           
/* 224 */           if (!flag5) {}
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 230 */           enumworldblocklayer = fixBlockLayer(block, enumworldblocklayer);
/*     */           
/* 232 */           if (flag3)
/*     */           {
/* 234 */             Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[] { enumworldblocklayer });
/*     */           }
/*     */           
/* 237 */           int j = enumworldblocklayer.ordinal();
/*     */           
/* 239 */           if (block.getRenderType() != -1)
/*     */           {
/* 241 */             WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
/* 242 */             worldrenderer.setBlockLayer(enumworldblocklayer);
/*     */             
/* 244 */             if (!compiledchunk.isLayerStarted(enumworldblocklayer))
/*     */             {
/* 246 */               compiledchunk.setLayerStarted(enumworldblocklayer);
/* 247 */               preRenderBlocks(worldrenderer, blockpos);
/*     */             }
/*     */             
/* 250 */             aboolean[j] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm, regionrendercache, worldrenderer);
/*     */           }
/*     */         }
/* 216 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */       Block block = (flag4 = EnumWorldBlockLayer.values()).length; for (IBlockState iblockstate = 0; iblockstate < block; iblockstate++) { EnumWorldBlockLayer enumworldblocklayer1 = flag4[iblockstate];
/*     */         
/* 257 */         if (aboolean[enumworldblocklayer1.ordinal()] != 0)
/*     */         {
/* 259 */           compiledchunk.setLayerUsed(enumworldblocklayer1);
/*     */         }
/*     */         
/* 262 */         if (compiledchunk.isLayerStarted(enumworldblocklayer1))
/*     */         {
/* 264 */           postRenderBlocks(enumworldblocklayer1, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer1), compiledchunk);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 269 */     compiledchunk.setVisibility(var10.computeVisibility());
/* 270 */     this.lockCompileTask.lock();
/*     */     
/*     */     try
/*     */     {
/* 274 */       HashSet hashset1 = Sets.newHashSet(var11);
/* 275 */       HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
/* 276 */       hashset1.removeAll(this.field_181056_j);
/* 277 */       hashset2.removeAll(var11);
/* 278 */       this.field_181056_j.clear();
/* 279 */       this.field_181056_j.addAll(var11);
/* 280 */       this.renderGlobal.func_181023_a(hashset2, hashset1);
/*     */     }
/*     */     finally
/*     */     {
/* 284 */       this.lockCompileTask.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void finishCompileTask()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 255	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   11: ifnull +41 -> 52
/*     */     //   14: aload_0
/*     */     //   15: getfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   18: invokevirtual 259	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:getStatus	()Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   21: getstatic 497	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   24: if_acmpeq +28 -> 52
/*     */     //   27: aload_0
/*     */     //   28: getfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   31: invokevirtual 500	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:finish	()V
/*     */     //   34: aload_0
/*     */     //   35: aconst_null
/*     */     //   36: putfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   39: goto +13 -> 52
/*     */     //   42: astore_1
/*     */     //   43: aload_0
/*     */     //   44: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   47: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   50: aload_1
/*     */     //   51: athrow
/*     */     //   52: aload_0
/*     */     //   53: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   56: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   59: return
/*     */     // Line number table:
/*     */     //   Java source line #290	-> byte code offset #0
/*     */     //   Java source line #294	-> byte code offset #7
/*     */     //   Java source line #296	-> byte code offset #27
/*     */     //   Java source line #297	-> byte code offset #34
/*     */     //   Java source line #299	-> byte code offset #39
/*     */     //   Java source line #301	-> byte code offset #42
/*     */     //   Java source line #302	-> byte code offset #43
/*     */     //   Java source line #303	-> byte code offset #50
/*     */     //   Java source line #302	-> byte code offset #52
/*     */     //   Java source line #304	-> byte code offset #59
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	60	0	this	RenderChunk
/*     */     //   42	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	42	42	finally
/*     */   }
/*     */   
/*     */   public ReentrantLock getLockCompileTask()
/*     */   {
/* 308 */     return this.lockCompileTask;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public ChunkCompileTaskGenerator makeCompileTaskChunk()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 255	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 505	net/minecraft/client/renderer/chunk/RenderChunk:finishCompileTask	()V
/*     */     //   11: aload_0
/*     */     //   12: new 14	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator
/*     */     //   15: dup
/*     */     //   16: aload_0
/*     */     //   17: getstatic 509	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type:REBUILD_CHUNK	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
/*     */     //   20: invokespecial 512	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:<init>	(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;)V
/*     */     //   23: putfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   26: aload_0
/*     */     //   27: getfield 86	net/minecraft/client/renderer/chunk/RenderChunk:compileTask	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   30: astore_1
/*     */     //   31: goto +13 -> 44
/*     */     //   34: astore_2
/*     */     //   35: aload_0
/*     */     //   36: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   39: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   42: aload_2
/*     */     //   43: athrow
/*     */     //   44: aload_0
/*     */     //   45: getfield 82	net/minecraft/client/renderer/chunk/RenderChunk:lockCompileTask	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   48: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   51: aload_1
/*     */     //   52: areturn
/*     */     // Line number table:
/*     */     //   Java source line #313	-> byte code offset #0
/*     */     //   Java source line #318	-> byte code offset #7
/*     */     //   Java source line #319	-> byte code offset #11
/*     */     //   Java source line #320	-> byte code offset #26
/*     */     //   Java source line #321	-> byte code offset #31
/*     */     //   Java source line #323	-> byte code offset #34
/*     */     //   Java source line #324	-> byte code offset #35
/*     */     //   Java source line #325	-> byte code offset #42
/*     */     //   Java source line #324	-> byte code offset #44
/*     */     //   Java source line #327	-> byte code offset #51
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	53	0	this	RenderChunk
/*     */     //   30	2	1	chunkcompiletaskgenerator	ChunkCompileTaskGenerator
/*     */     //   44	8	1	chunkcompiletaskgenerator	ChunkCompileTaskGenerator
/*     */     //   34	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	34	34	finally
/*     */   }
/*     */   
/*     */   public ChunkCompileTaskGenerator makeCompileTaskTransparency()
/*     */   {
/* 332 */     this.lockCompileTask.lock();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 337 */       if ((this.compileTask != null) && (this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING))
/*     */       {
/* 339 */         ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
/* 340 */         return chunkcompiletaskgenerator2;
/*     */       }
/*     */       
/* 343 */       if ((this.compileTask != null) && (this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE))
/*     */       {
/* 345 */         this.compileTask.finish();
/* 346 */         this.compileTask = null;
/*     */       }
/*     */       
/* 349 */       this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
/* 350 */       this.compileTask.setCompiledChunk(this.compiledChunk);
/* 351 */       ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.compileTask;
/* 352 */       chunkcompiletaskgenerator1 = chunkcompiletaskgenerator;
/*     */     }
/*     */     finally {
/*     */       ChunkCompileTaskGenerator chunkcompiletaskgenerator1;
/* 356 */       this.lockCompileTask.unlock(); } ChunkCompileTaskGenerator chunkcompiletaskgenerator1; this.lockCompileTask.unlock();
/*     */     
/*     */ 
/* 359 */     return chunkcompiletaskgenerator1;
/*     */   }
/*     */   
/*     */   private void preRenderBlocks(WorldRenderer worldRendererIn, BlockPos pos)
/*     */   {
/* 364 */     worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
/* 365 */     worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
/*     */   }
/*     */   
/*     */   private void postRenderBlocks(EnumWorldBlockLayer layer, float x, float y, float z, WorldRenderer worldRendererIn, CompiledChunk compiledChunkIn)
/*     */   {
/* 370 */     if ((layer == EnumWorldBlockLayer.TRANSLUCENT) && (!compiledChunkIn.isLayerEmpty(layer)))
/*     */     {
/* 372 */       worldRendererIn.func_181674_a(x, y, z);
/* 373 */       compiledChunkIn.setState(worldRendererIn.func_181672_a());
/*     */     }
/*     */     
/* 376 */     worldRendererIn.finishDrawing();
/*     */   }
/*     */   
/*     */   private void initModelviewMatrix()
/*     */   {
/* 381 */     GlStateManager.pushMatrix();
/* 382 */     GlStateManager.loadIdentity();
/* 383 */     float f = 1.000001F;
/* 384 */     GlStateManager.translate(-8.0F, -8.0F, -8.0F);
/* 385 */     GlStateManager.scale(f, f, f);
/* 386 */     GlStateManager.translate(8.0F, 8.0F, 8.0F);
/* 387 */     GlStateManager.getFloat(2982, this.modelviewMatrix);
/* 388 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   public void multModelviewMatrix()
/*     */   {
/* 393 */     GlStateManager.multMatrix(this.modelviewMatrix);
/*     */   }
/*     */   
/*     */   public CompiledChunk getCompiledChunk()
/*     */   {
/* 398 */     return this.compiledChunk;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setCompiledChunk(CompiledChunk compiledChunkIn)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 84	net/minecraft/client/renderer/chunk/RenderChunk:lockCompiledChunk	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 255	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: aload_1
/*     */     //   9: putfield 77	net/minecraft/client/renderer/chunk/RenderChunk:compiledChunk	Lnet/minecraft/client/renderer/chunk/CompiledChunk;
/*     */     //   12: goto +13 -> 25
/*     */     //   15: astore_2
/*     */     //   16: aload_0
/*     */     //   17: getfield 84	net/minecraft/client/renderer/chunk/RenderChunk:lockCompiledChunk	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   20: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   23: aload_2
/*     */     //   24: athrow
/*     */     //   25: aload_0
/*     */     //   26: getfield 84	net/minecraft/client/renderer/chunk/RenderChunk:lockCompiledChunk	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   29: invokevirtual 266	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   32: return
/*     */     // Line number table:
/*     */     //   Java source line #403	-> byte code offset #0
/*     */     //   Java source line #407	-> byte code offset #7
/*     */     //   Java source line #408	-> byte code offset #12
/*     */     //   Java source line #410	-> byte code offset #15
/*     */     //   Java source line #411	-> byte code offset #16
/*     */     //   Java source line #412	-> byte code offset #23
/*     */     //   Java source line #411	-> byte code offset #25
/*     */     //   Java source line #413	-> byte code offset #32
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	33	0	this	RenderChunk
/*     */     //   0	33	1	compiledChunkIn	CompiledChunk
/*     */     //   15	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	15	15	finally
/*     */   }
/*     */   
/*     */   public void stopCompileTask()
/*     */   {
/* 417 */     finishCompileTask();
/* 418 */     this.compiledChunk = CompiledChunk.DUMMY;
/*     */   }
/*     */   
/*     */   public void deleteGlResources()
/*     */   {
/* 423 */     stopCompileTask();
/* 424 */     this.world = null;
/*     */     
/* 426 */     for (int i = 0; i < EnumWorldBlockLayer.values().length; i++)
/*     */     {
/* 428 */       if (this.vertexBuffers[i] != null)
/*     */       {
/* 430 */         this.vertexBuffers[i].deleteGlBuffers();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public BlockPos getPosition()
/*     */   {
/* 437 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setNeedsUpdate(boolean needsUpdateIn)
/*     */   {
/* 442 */     this.needsUpdate = needsUpdateIn;
/*     */   }
/*     */   
/*     */   public boolean isNeedsUpdate()
/*     */   {
/* 447 */     return this.needsUpdate;
/*     */   }
/*     */   
/*     */   public BlockPos func_181701_a(EnumFacing p_181701_1_)
/*     */   {
/* 452 */     return getPositionOffset16(p_181701_1_);
/*     */   }
/*     */   
/*     */   public BlockPos getPositionOffset16(EnumFacing p_getPositionOffset16_1_)
/*     */   {
/* 457 */     int i = p_getPositionOffset16_1_.getIndex();
/* 458 */     BlockPos blockpos = this.positionOffsets16[i];
/*     */     
/* 460 */     if (blockpos == null)
/*     */     {
/* 462 */       blockpos = getPosition().offset(p_getPositionOffset16_1_, 16);
/* 463 */       this.positionOffsets16[i] = blockpos;
/*     */     }
/*     */     
/* 466 */     return blockpos;
/*     */   }
/*     */   
/*     */   private EnumWorldBlockLayer fixBlockLayer(Block p_fixBlockLayer_1_, EnumWorldBlockLayer p_fixBlockLayer_2_)
/*     */   {
/* 471 */     return p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT ? EnumWorldBlockLayer.CUTOUT_MIPPED : (p_fixBlockLayer_1_ instanceof net.minecraft.block.BlockCactus) ? p_fixBlockLayer_2_ : (p_fixBlockLayer_1_ instanceof BlockRedstoneWire) ? p_fixBlockLayer_2_ : p_fixBlockLayer_2_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\RenderChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */