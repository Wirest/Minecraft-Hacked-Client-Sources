/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.RegionRenderCacheBuilder;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkRenderWorker implements Runnable
/*     */ {
/*  20 */   private static final Logger LOGGER = ;
/*     */   private final ChunkRenderDispatcher chunkRenderDispatcher;
/*     */   private final RegionRenderCacheBuilder regionRenderCacheBuilder;
/*     */   
/*     */   public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_)
/*     */   {
/*  26 */     this(p_i46201_1_, null);
/*     */   }
/*     */   
/*     */   public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcherIn, RegionRenderCacheBuilder regionRenderCacheBuilderIn)
/*     */   {
/*  31 */     this.chunkRenderDispatcher = chunkRenderDispatcherIn;
/*  32 */     this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/*  41 */         processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       CrashReport crashreport;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  52 */       return;
/*     */     }
/*     */     catch (InterruptedException var3)
/*     */     {
/*  45 */       LOGGER.debug("Stopping due to interrupt");
/*  46 */       return;
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*  50 */       crashreport = CrashReport.makeCrashReport(throwable, "Batching chunks");
/*  51 */       Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void processTask(final ChunkCompileTaskGenerator generator)
/*     */     throws InterruptedException
/*     */   {
/*  59 */     generator.getLock().lock();
/*     */     
/*     */     try
/*     */     {
/*  63 */       if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING)
/*     */       {
/*  65 */         if (!generator.isFinished())
/*     */         {
/*  67 */           LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be pending; ignoring task");
/*     */         }
/*     */         
/*  70 */         return;
/*     */       }
/*     */       
/*  73 */       generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
/*     */     }
/*     */     finally
/*     */     {
/*  77 */       generator.getLock().unlock(); } generator.getLock().unlock();
/*     */     
/*     */ 
/*  80 */     Entity lvt_2_1_ = Minecraft.getMinecraft().getRenderViewEntity();
/*     */     
/*  82 */     if (lvt_2_1_ == null)
/*     */     {
/*  84 */       generator.finish();
/*     */     }
/*     */     else
/*     */     {
/*  88 */       generator.setRegionRenderCacheBuilder(getRegionRenderCacheBuilder());
/*  89 */       float f = (float)lvt_2_1_.posX;
/*  90 */       float f1 = (float)lvt_2_1_.posY + lvt_2_1_.getEyeHeight();
/*  91 */       float f2 = (float)lvt_2_1_.posZ;
/*  92 */       ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
/*     */       
/*  94 */       if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
/*     */       {
/*  96 */         generator.getRenderChunk().rebuildChunk(f, f1, f2, generator);
/*     */       }
/*  98 */       else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
/*     */       {
/* 100 */         generator.getRenderChunk().resortTransparency(f, f1, f2, generator);
/*     */       }
/*     */       
/* 103 */       generator.getLock().lock();
/*     */       
/*     */       try
/*     */       {
/* 107 */         if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
/*     */         {
/* 109 */           if (!generator.isFinished())
/*     */           {
/* 111 */             LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be compiling; aborting task");
/*     */           }
/*     */           
/* 114 */           freeRenderBuilder(generator);
/* 115 */           return;
/*     */         }
/*     */         
/* 118 */         generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
/*     */       }
/*     */       finally
/*     */       {
/* 122 */         generator.getLock().unlock(); } generator.getLock().unlock();
/*     */       
/*     */ 
/* 125 */       final CompiledChunk lvt_7_1_ = generator.getCompiledChunk();
/* 126 */       ArrayList lvt_8_1_ = Lists.newArrayList();
/*     */       
/* 128 */       if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
/*     */         EnumWorldBlockLayer[] arrayOfEnumWorldBlockLayer;
/* 130 */         int j = (arrayOfEnumWorldBlockLayer = EnumWorldBlockLayer.values()).length; for (int i = 0; i < j; i++) { EnumWorldBlockLayer enumworldblocklayer = arrayOfEnumWorldBlockLayer[i];
/*     */           
/* 132 */           if (lvt_7_1_.isLayerStarted(enumworldblocklayer))
/*     */           {
/* 134 */             lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), generator.getRenderChunk(), lvt_7_1_));
/*     */           }
/*     */         }
/*     */       }
/* 138 */       else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
/*     */       {
/* 140 */         lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), generator.getRenderChunk(), lvt_7_1_));
/*     */       }
/*     */       
/* 143 */       final ListenableFuture<List<Object>> listenablefuture = Futures.allAsList(lvt_8_1_);
/* 144 */       generator.addFinishRunnable(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 148 */           listenablefuture.cancel(false);
/*     */         }
/* 150 */       });
/* 151 */       Futures.addCallback(listenablefuture, new com.google.common.util.concurrent.FutureCallback()
/*     */       {
/*     */         public void onSuccess(List<Object> p_onSuccess_1_)
/*     */         {
/* 155 */           ChunkRenderWorker.this.freeRenderBuilder(generator);
/* 156 */           generator.getLock().lock();
/*     */           
/*     */ 
/*     */           try
/*     */           {
/* 161 */             if (generator.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING)
/*     */             {
/* 163 */               generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
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
/* 174 */               generator.getLock().unlock();
/*     */               break label129;
/*     */             }
/* 167 */             if (!generator.isFinished())
/*     */             {
/* 169 */               ChunkRenderWorker.LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be uploading; aborting task");
/*     */             }
/*     */           }
/*     */           finally
/*     */           {
/* 174 */             generator.getLock().unlock(); } generator.getLock().unlock();
/*     */           
/*     */ 
/* 177 */           return;
/*     */           label129:
/* 179 */           generator.getRenderChunk().setCompiledChunk(lvt_7_1_);
/*     */         }
/*     */         
/*     */         public void onFailure(Throwable p_onFailure_1_) {
/* 183 */           ChunkRenderWorker.this.freeRenderBuilder(generator);
/*     */           
/* 185 */           if ((!(p_onFailure_1_ instanceof CancellationException)) && (!(p_onFailure_1_ instanceof InterruptedException)))
/*     */           {
/* 187 */             Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException
/*     */   {
/* 196 */     return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
/*     */   }
/*     */   
/*     */   private void freeRenderBuilder(ChunkCompileTaskGenerator taskGenerator)
/*     */   {
/* 201 */     if (this.regionRenderCacheBuilder == null)
/*     */     {
/* 203 */       this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\ChunkRenderWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */