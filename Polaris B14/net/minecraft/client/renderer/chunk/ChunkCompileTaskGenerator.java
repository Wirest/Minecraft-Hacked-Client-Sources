/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.renderer.RegionRenderCacheBuilder;
/*     */ 
/*     */ public class ChunkCompileTaskGenerator
/*     */ {
/*     */   private final RenderChunk renderChunk;
/*  11 */   private final ReentrantLock lock = new ReentrantLock();
/*  12 */   private final List<Runnable> listFinishRunnables = Lists.newArrayList();
/*     */   private final Type type;
/*     */   private RegionRenderCacheBuilder regionRenderCacheBuilder;
/*     */   private CompiledChunk compiledChunk;
/*  16 */   private Status status = Status.PENDING;
/*     */   private boolean finished;
/*     */   
/*     */   public ChunkCompileTaskGenerator(RenderChunk renderChunkIn, Type typeIn)
/*     */   {
/*  21 */     this.renderChunk = renderChunkIn;
/*  22 */     this.type = typeIn;
/*     */   }
/*     */   
/*     */   public Status getStatus()
/*     */   {
/*  27 */     return this.status;
/*     */   }
/*     */   
/*     */   public RenderChunk getRenderChunk()
/*     */   {
/*  32 */     return this.renderChunk;
/*     */   }
/*     */   
/*     */   public CompiledChunk getCompiledChunk()
/*     */   {
/*  37 */     return this.compiledChunk;
/*     */   }
/*     */   
/*     */   public void setCompiledChunk(CompiledChunk compiledChunkIn)
/*     */   {
/*  42 */     this.compiledChunk = compiledChunkIn;
/*     */   }
/*     */   
/*     */   public RegionRenderCacheBuilder getRegionRenderCacheBuilder()
/*     */   {
/*  47 */     return this.regionRenderCacheBuilder;
/*     */   }
/*     */   
/*     */   public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilderIn)
/*     */   {
/*  52 */     this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setStatus(Status statusIn)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 81	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: aload_1
/*     */     //   9: putfield 51	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:status	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   12: goto +13 -> 25
/*     */     //   15: astore_2
/*     */     //   16: aload_0
/*     */     //   17: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   20: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   23: aload_2
/*     */     //   24: athrow
/*     */     //   25: aload_0
/*     */     //   26: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   29: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   32: return
/*     */     // Line number table:
/*     */     //   Java source line #57	-> byte code offset #0
/*     */     //   Java source line #61	-> byte code offset #7
/*     */     //   Java source line #62	-> byte code offset #12
/*     */     //   Java source line #64	-> byte code offset #15
/*     */     //   Java source line #65	-> byte code offset #16
/*     */     //   Java source line #66	-> byte code offset #23
/*     */     //   Java source line #65	-> byte code offset #25
/*     */     //   Java source line #67	-> byte code offset #32
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	33	0	this	ChunkCompileTaskGenerator
/*     */     //   0	33	1	statusIn	Status
/*     */     //   15	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	15	15	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void finish()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 81	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 55	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:type	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
/*     */     //   11: getstatic 91	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type:REBUILD_CHUNK	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
/*     */     //   14: if_acmpne +21 -> 35
/*     */     //   17: aload_0
/*     */     //   18: getfield 51	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:status	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   21: getstatic 94	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   24: if_acmpeq +11 -> 35
/*     */     //   27: aload_0
/*     */     //   28: getfield 53	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:renderChunk	Lnet/minecraft/client/renderer/chunk/RenderChunk;
/*     */     //   31: iconst_1
/*     */     //   32: invokevirtual 100	net/minecraft/client/renderer/chunk/RenderChunk:setNeedsUpdate	(Z)V
/*     */     //   35: aload_0
/*     */     //   36: iconst_1
/*     */     //   37: putfield 102	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:finished	Z
/*     */     //   40: aload_0
/*     */     //   41: getstatic 94	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   44: putfield 51	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:status	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
/*     */     //   47: aload_0
/*     */     //   48: getfield 46	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:listFinishRunnables	Ljava/util/List;
/*     */     //   51: invokeinterface 108 1 0
/*     */     //   56: astore_2
/*     */     //   57: goto +19 -> 76
/*     */     //   60: aload_2
/*     */     //   61: invokeinterface 114 1 0
/*     */     //   66: checkcast 116	java/lang/Runnable
/*     */     //   69: astore_1
/*     */     //   70: aload_1
/*     */     //   71: invokeinterface 119 1 0
/*     */     //   76: aload_2
/*     */     //   77: invokeinterface 123 1 0
/*     */     //   82: ifne -22 -> 60
/*     */     //   85: goto +13 -> 98
/*     */     //   88: astore_3
/*     */     //   89: aload_0
/*     */     //   90: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   93: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   96: aload_3
/*     */     //   97: athrow
/*     */     //   98: aload_0
/*     */     //   99: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   102: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   105: return
/*     */     // Line number table:
/*     */     //   Java source line #71	-> byte code offset #0
/*     */     //   Java source line #75	-> byte code offset #7
/*     */     //   Java source line #77	-> byte code offset #27
/*     */     //   Java source line #80	-> byte code offset #35
/*     */     //   Java source line #81	-> byte code offset #40
/*     */     //   Java source line #83	-> byte code offset #47
/*     */     //   Java source line #85	-> byte code offset #70
/*     */     //   Java source line #83	-> byte code offset #76
/*     */     //   Java source line #87	-> byte code offset #85
/*     */     //   Java source line #89	-> byte code offset #88
/*     */     //   Java source line #90	-> byte code offset #89
/*     */     //   Java source line #91	-> byte code offset #96
/*     */     //   Java source line #90	-> byte code offset #98
/*     */     //   Java source line #92	-> byte code offset #105
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	106	0	this	ChunkCompileTaskGenerator
/*     */     //   69	2	1	runnable	Runnable
/*     */     //   56	21	2	localIterator	java.util.Iterator
/*     */     //   88	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	88	88	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void addFinishRunnable(Runnable p_178539_1_)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   4: invokevirtual 81	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 46	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:listFinishRunnables	Ljava/util/List;
/*     */     //   11: aload_1
/*     */     //   12: invokeinterface 131 2 0
/*     */     //   17: pop
/*     */     //   18: aload_0
/*     */     //   19: getfield 102	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:finished	Z
/*     */     //   22: ifeq +22 -> 44
/*     */     //   25: aload_1
/*     */     //   26: invokeinterface 119 1 0
/*     */     //   31: goto +13 -> 44
/*     */     //   34: astore_2
/*     */     //   35: aload_0
/*     */     //   36: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   39: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   42: aload_2
/*     */     //   43: athrow
/*     */     //   44: aload_0
/*     */     //   45: getfield 38	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   48: invokevirtual 86	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */     //   51: return
/*     */     // Line number table:
/*     */     //   Java source line #96	-> byte code offset #0
/*     */     //   Java source line #100	-> byte code offset #7
/*     */     //   Java source line #102	-> byte code offset #18
/*     */     //   Java source line #104	-> byte code offset #25
/*     */     //   Java source line #106	-> byte code offset #31
/*     */     //   Java source line #108	-> byte code offset #34
/*     */     //   Java source line #109	-> byte code offset #35
/*     */     //   Java source line #110	-> byte code offset #42
/*     */     //   Java source line #109	-> byte code offset #44
/*     */     //   Java source line #111	-> byte code offset #51
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	52	0	this	ChunkCompileTaskGenerator
/*     */     //   0	52	1	p_178539_1_	Runnable
/*     */     //   34	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	34	34	finally
/*     */   }
/*     */   
/*     */   public ReentrantLock getLock()
/*     */   {
/* 115 */     return this.lock;
/*     */   }
/*     */   
/*     */   public Type getType()
/*     */   {
/* 120 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/* 125 */     return this.finished;
/*     */   }
/*     */   
/*     */   public static enum Status
/*     */   {
/* 130 */     PENDING, 
/* 131 */     COMPILING, 
/* 132 */     UPLOADING, 
/* 133 */     DONE;
/*     */   }
/*     */   
/*     */   public static enum Type
/*     */   {
/* 138 */     REBUILD_CHUNK, 
/* 139 */     RESORT_TRANSPARENCY;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\ChunkCompileTaskGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */