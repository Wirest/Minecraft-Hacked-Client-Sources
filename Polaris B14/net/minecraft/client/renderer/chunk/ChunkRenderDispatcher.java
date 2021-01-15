/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFutureTask;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RegionRenderCacheBuilder;
/*     */ import net.minecraft.client.renderer.VertexBufferUploader;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.WorldVertexBufferUploader;
/*     */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class ChunkRenderDispatcher
/*     */ {
/*  28 */   private static final Logger logger = ;
/*  29 */   private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
/*  30 */   private final List<ChunkRenderWorker> listThreadedWorkers = Lists.newArrayList();
/*  31 */   private final BlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates = Queues.newArrayBlockingQueue(100);
/*  32 */   private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
/*  33 */   private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
/*  34 */   private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
/*  35 */   private final Queue<ListenableFutureTask<?>> queueChunkUploads = Queues.newArrayDeque();
/*     */   private final ChunkRenderWorker renderWorker;
/*     */   
/*     */   public ChunkRenderDispatcher()
/*     */   {
/*  40 */     for (int i = 0; i < 2; i++)
/*     */     {
/*  42 */       ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
/*  43 */       Thread thread = threadFactory.newThread(chunkrenderworker);
/*  44 */       thread.start();
/*  45 */       this.listThreadedWorkers.add(chunkrenderworker);
/*     */     }
/*     */     
/*  48 */     for (int j = 0; j < 5; j++)
/*     */     {
/*  50 */       this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
/*     */     }
/*     */     
/*  53 */     this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
/*     */   }
/*     */   
/*     */   public String getDebugInfo()
/*     */   {
/*  58 */     return String.format("pC: %03d, pU: %1d, aB: %1d", new Object[] { Integer.valueOf(this.queueChunkUpdates.size()), Integer.valueOf(this.queueChunkUploads.size()), Integer.valueOf(this.queueFreeRenderBuilders.size()) });
/*     */   }
/*     */   
/*     */   public boolean runChunkUploads(long p_178516_1_)
/*     */   {
/*  63 */     boolean flag = false;
/*     */     long i;
/*     */     do
/*     */     {
/*  67 */       boolean flag1 = false;
/*     */       
/*  69 */       synchronized (this.queueChunkUploads)
/*     */       {
/*  71 */         if (!this.queueChunkUploads.isEmpty())
/*     */         {
/*  73 */           ((ListenableFutureTask)this.queueChunkUploads.poll()).run();
/*  74 */           flag1 = true;
/*  75 */           flag = true;
/*     */         }
/*     */       }
/*     */       
/*  79 */       if ((p_178516_1_ == 0L) || (!flag1)) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*  84 */       i = p_178516_1_ - System.nanoTime();
/*     */     }
/*  86 */     while (i >= 0L);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     return flag;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean updateChunkLater(RenderChunk chunkRenderer)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 203	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_1
/*     */     //   8: invokevirtual 207	net/minecraft/client/renderer/chunk/RenderChunk:makeCompileTaskChunk	()Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   11: astore_3
/*     */     //   12: aload_3
/*     */     //   13: new 7	net/minecraft/client/renderer/chunk/ChunkRenderDispatcher$1
/*     */     //   16: dup
/*     */     //   17: aload_0
/*     */     //   18: aload_3
/*     */     //   19: invokespecial 210	net/minecraft/client/renderer/chunk/ChunkRenderDispatcher$1:<init>	(Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;)V
/*     */     //   22: invokevirtual 216	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:addFinishRunnable	(Ljava/lang/Runnable;)V
/*     */     //   25: aload_0
/*     */     //   26: getfield 80	net/minecraft/client/renderer/chunk/ChunkRenderDispatcher:queueChunkUpdates	Ljava/util/concurrent/BlockingQueue;
/*     */     //   29: aload_3
/*     */     //   30: invokeinterface 219 2 0
/*     */     //   35: istore 4
/*     */     //   37: iload 4
/*     */     //   39: ifne +7 -> 46
/*     */     //   42: aload_3
/*     */     //   43: invokevirtual 222	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:finish	()V
/*     */     //   46: iload 4
/*     */     //   48: istore_2
/*     */     //   49: goto +15 -> 64
/*     */     //   52: astore 5
/*     */     //   54: aload_1
/*     */     //   55: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   58: invokevirtual 225	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   61: aload 5
/*     */     //   63: athrow
/*     */     //   64: aload_1
/*     */     //   65: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   68: invokevirtual 225	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   71: iload_2
/*     */     //   72: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #97	-> byte code offset #0
/*     */     //   Java source line #102	-> byte code offset #7
/*     */     //   Java source line #103	-> byte code offset #12
/*     */     //   Java source line #110	-> byte code offset #25
/*     */     //   Java source line #112	-> byte code offset #37
/*     */     //   Java source line #114	-> byte code offset #42
/*     */     //   Java source line #117	-> byte code offset #46
/*     */     //   Java source line #118	-> byte code offset #49
/*     */     //   Java source line #120	-> byte code offset #52
/*     */     //   Java source line #121	-> byte code offset #54
/*     */     //   Java source line #122	-> byte code offset #61
/*     */     //   Java source line #121	-> byte code offset #64
/*     */     //   Java source line #124	-> byte code offset #71
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	73	0	this	ChunkRenderDispatcher
/*     */     //   0	73	1	chunkRenderer	RenderChunk
/*     */     //   48	2	2	flag1	boolean
/*     */     //   64	8	2	flag1	boolean
/*     */     //   11	32	3	chunkcompiletaskgenerator	ChunkCompileTaskGenerator
/*     */     //   35	12	4	flag	boolean
/*     */     //   52	10	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	52	52	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean updateChunkNow(RenderChunk chunkRenderer)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 203	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_1
/*     */     //   8: invokevirtual 207	net/minecraft/client/renderer/chunk/RenderChunk:makeCompileTaskChunk	()Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
/*     */     //   11: astore_3
/*     */     //   12: aload_0
/*     */     //   13: getfield 131	net/minecraft/client/renderer/chunk/ChunkRenderDispatcher:renderWorker	Lnet/minecraft/client/renderer/chunk/ChunkRenderWorker;
/*     */     //   16: aload_3
/*     */     //   17: invokevirtual 236	net/minecraft/client/renderer/chunk/ChunkRenderWorker:processTask	(Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;)V
/*     */     //   20: goto +5 -> 25
/*     */     //   23: astore 4
/*     */     //   25: iconst_1
/*     */     //   26: istore_2
/*     */     //   27: goto +15 -> 42
/*     */     //   30: astore 5
/*     */     //   32: aload_1
/*     */     //   33: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   36: invokevirtual 225	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   39: aload 5
/*     */     //   41: athrow
/*     */     //   42: aload_1
/*     */     //   43: invokevirtual 198	net/minecraft/client/renderer/chunk/RenderChunk:getLockCompileTask	()Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   46: invokevirtual 225	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   49: iload_2
/*     */     //   50: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #129	-> byte code offset #0
/*     */     //   Java source line #134	-> byte code offset #7
/*     */     //   Java source line #138	-> byte code offset #12
/*     */     //   Java source line #139	-> byte code offset #20
/*     */     //   Java source line #140	-> byte code offset #23
/*     */     //   Java source line #145	-> byte code offset #25
/*     */     //   Java source line #146	-> byte code offset #27
/*     */     //   Java source line #148	-> byte code offset #30
/*     */     //   Java source line #149	-> byte code offset #32
/*     */     //   Java source line #150	-> byte code offset #39
/*     */     //   Java source line #149	-> byte code offset #42
/*     */     //   Java source line #152	-> byte code offset #49
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	51	0	this	ChunkRenderDispatcher
/*     */     //   0	51	1	chunkRenderer	RenderChunk
/*     */     //   26	2	2	flag	boolean
/*     */     //   42	8	2	flag	boolean
/*     */     //   11	6	3	chunkcompiletaskgenerator	ChunkCompileTaskGenerator
/*     */     //   23	1	4	localInterruptedException	InterruptedException
/*     */     //   30	10	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   12	20	23	java/lang/InterruptedException
/*     */     //   7	30	30	finally
/*     */   }
/*     */   
/*     */   public void stopChunkUpdates()
/*     */   {
/* 157 */     clearChunkUpdates();
/*     */     
/* 159 */     while (runChunkUploads(0L)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 164 */     List<RegionRenderCacheBuilder> list = Lists.newArrayList();
/*     */     
/* 166 */     while (list.size() != 5)
/*     */     {
/*     */       try
/*     */       {
/* 170 */         list.add(allocateRenderBuilder());
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 178 */     this.queueFreeRenderBuilders.addAll(list);
/*     */   }
/*     */   
/*     */   public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_)
/*     */   {
/* 183 */     this.queueFreeRenderBuilders.add(p_178512_1_);
/*     */   }
/*     */   
/*     */   public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException
/*     */   {
/* 188 */     return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
/*     */   }
/*     */   
/*     */   public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException
/*     */   {
/* 193 */     return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
/*     */   }
/*     */   
/*     */   public boolean updateTransparencyLater(RenderChunk chunkRenderer)
/*     */   {
/* 198 */     chunkRenderer.getLockCompileTask().lock();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 203 */       final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
/*     */       
/* 205 */       if (chunkcompiletaskgenerator == null)
/*     */       {
/* 207 */         boolean flag = true;
/* 208 */         return flag;
/*     */       }
/*     */       
/* 211 */       chunkcompiletaskgenerator.addFinishRunnable(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 215 */           ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
/*     */         }
/* 217 */       });
/* 218 */       flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
/*     */     }
/*     */     finally {
/*     */       boolean flag;
/* 222 */       chunkRenderer.getLockCompileTask().unlock(); } boolean flag; chunkRenderer.getLockCompileTask().unlock();
/*     */     
/*     */ 
/* 225 */     return flag;
/*     */   }
/*     */   
/*     */   public com.google.common.util.concurrent.ListenableFuture<Object> uploadChunk(final EnumWorldBlockLayer player, final WorldRenderer p_178503_2_, final RenderChunk chunkRenderer, final CompiledChunk compiledChunkIn)
/*     */   {
/* 230 */     if (Minecraft.getMinecraft().isCallingFromMinecraftThread())
/*     */     {
/* 232 */       if (OpenGlHelper.useVbo())
/*     */       {
/* 234 */         uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
/*     */       }
/*     */       else
/*     */       {
/* 238 */         uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
/*     */       }
/*     */       
/* 241 */       p_178503_2_.setTranslation(0.0D, 0.0D, 0.0D);
/* 242 */       return Futures.immediateFuture(null);
/*     */     }
/*     */     
/*     */ 
/* 246 */     ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 250 */         ChunkRenderDispatcher.this.uploadChunk(player, p_178503_2_, chunkRenderer, compiledChunkIn);
/*     */       }
/* 252 */     }, null);
/*     */     
/* 254 */     synchronized (this.queueChunkUploads)
/*     */     {
/* 256 */       this.queueChunkUploads.add(listenablefuturetask);
/* 257 */       return listenablefuturetask;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer)
/*     */   {
/* 264 */     GL11.glNewList(p_178510_2_, 4864);
/* 265 */     GlStateManager.pushMatrix();
/* 266 */     chunkRenderer.multModelviewMatrix();
/* 267 */     this.worldVertexUploader.func_181679_a(p_178510_1_);
/* 268 */     GlStateManager.popMatrix();
/* 269 */     GL11.glEndList();
/*     */   }
/*     */   
/*     */   private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer vertexBufferIn)
/*     */   {
/* 274 */     this.vertexUploader.setVertexBuffer(vertexBufferIn);
/* 275 */     this.vertexUploader.func_181679_a(p_178506_1_);
/*     */   }
/*     */   
/*     */   public void clearChunkUpdates()
/*     */   {
/* 280 */     while (!this.queueChunkUpdates.isEmpty())
/*     */     {
/* 282 */       ChunkCompileTaskGenerator chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
/*     */       
/* 284 */       if (chunkcompiletaskgenerator != null)
/*     */       {
/* 286 */         chunkcompiletaskgenerator.finish();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\ChunkRenderDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */