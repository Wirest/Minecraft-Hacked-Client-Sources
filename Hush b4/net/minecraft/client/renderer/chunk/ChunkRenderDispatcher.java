// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.google.common.util.concurrent.Futures;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Collection;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.Queue;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;

public class ChunkRenderDispatcher
{
    private static final Logger logger;
    private static final ThreadFactory threadFactory;
    private final List<ChunkRenderWorker> listThreadedWorkers;
    private final BlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates;
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;
    private final WorldVertexBufferUploader worldVertexUploader;
    private final VertexBufferUploader vertexUploader;
    private final Queue<ListenableFutureTask<?>> queueChunkUploads;
    private final ChunkRenderWorker renderWorker;
    
    static {
        logger = LogManager.getLogger();
        threadFactory = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    }
    
    public ChunkRenderDispatcher() {
        this.listThreadedWorkers = (List<ChunkRenderWorker>)Lists.newArrayList();
        this.queueChunkUpdates = (BlockingQueue<ChunkCompileTaskGenerator>)Queues.newArrayBlockingQueue(100);
        this.queueFreeRenderBuilders = (BlockingQueue<RegionRenderCacheBuilder>)Queues.newArrayBlockingQueue(5);
        this.worldVertexUploader = new WorldVertexBufferUploader();
        this.vertexUploader = new VertexBufferUploader();
        this.queueChunkUploads = (Queue<ListenableFutureTask<?>>)Queues.newArrayDeque();
        for (int i = 0; i < 2; ++i) {
            final ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
            final Thread thread = ChunkRenderDispatcher.threadFactory.newThread(chunkrenderworker);
            thread.start();
            this.listThreadedWorkers.add(chunkrenderworker);
        }
        for (int j = 0; j < 5; ++j) {
            this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
        }
        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }
    
    public String getDebugInfo() {
        return String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
    }
    
    public boolean runChunkUploads(final long p_178516_1_) {
        boolean flag = false;
        long i;
        do {
            boolean flag2 = false;
            synchronized (this.queueChunkUploads) {
                if (!this.queueChunkUploads.isEmpty()) {
                    this.queueChunkUploads.poll().run();
                    flag2 = true;
                    flag = true;
                }
            }
            // monitorexit(this.queueChunkUploads)
            if (p_178516_1_ == 0L) {
                break;
            }
            if (!flag2) {
                break;
            }
            i = p_178516_1_ - System.nanoTime();
        } while (i >= 0L);
        return flag;
    }
    
    public boolean updateChunkLater(final RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag2;
        try {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
                @Override
                public void run() {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            final boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
            if (!flag) {
                chunkcompiletaskgenerator.finish();
            }
            flag2 = flag;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        chunkRenderer.getLockCompileTask().unlock();
        return flag2;
    }
    
    public boolean updateChunkNow(final RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag;
        try {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            try {
                this.renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException ex) {}
            flag = true;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        chunkRenderer.getLockCompileTask().unlock();
        return flag;
    }
    
    public void stopChunkUpdates() {
        this.clearChunkUpdates();
        while (this.runChunkUploads(0L)) {}
        final List<RegionRenderCacheBuilder> list = (List<RegionRenderCacheBuilder>)Lists.newArrayList();
        while (list.size() != 5) {
            try {
                list.add(this.allocateRenderBuilder());
            }
            catch (InterruptedException ex) {}
        }
        this.queueFreeRenderBuilders.addAll((Collection<?>)list);
    }
    
    public void freeRenderBuilder(final RegionRenderCacheBuilder p_178512_1_) {
        this.queueFreeRenderBuilders.add(p_178512_1_);
    }
    
    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
        return this.queueFreeRenderBuilders.take();
    }
    
    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
        return this.queueChunkUpdates.take();
    }
    
    public boolean updateTransparencyLater(final RenderChunk chunkRenderer) {
        chunkRenderer.getLockCompileTask().lock();
        boolean flag;
        try {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
            if (chunkcompiletaskgenerator == null) {
                flag = true;
                return flag;
            }
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
                @Override
                public void run() {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        chunkRenderer.getLockCompileTask().unlock();
        return flag;
    }
    
    public ListenableFuture<Object> uploadChunk(final EnumWorldBlockLayer player, final WorldRenderer p_178503_2_, final RenderChunk chunkRenderer, final CompiledChunk compiledChunkIn) {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            if (OpenGlHelper.useVbo()) {
                this.uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
            }
            else {
                this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
            }
            p_178503_2_.setTranslation(0.0, 0.0, 0.0);
            return Futures.immediateFuture((Object)null);
        }
        final ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new Runnable() {
            @Override
            public void run() {
                ChunkRenderDispatcher.this.uploadChunk(player, p_178503_2_, chunkRenderer, compiledChunkIn);
            }
        }, (Object)null);
        synchronized (this.queueChunkUploads) {
            this.queueChunkUploads.add(listenablefuturetask);
            // monitorexit(this.queueChunkUploads)
            return listenablefuturetask;
        }
    }
    
    private void uploadDisplayList(final WorldRenderer p_178510_1_, final int p_178510_2_, final RenderChunk chunkRenderer) {
        GL11.glNewList(p_178510_2_, 4864);
        GlStateManager.pushMatrix();
        chunkRenderer.multModelviewMatrix();
        this.worldVertexUploader.func_181679_a(p_178510_1_);
        GlStateManager.popMatrix();
        GL11.glEndList();
    }
    
    private void uploadVertexBuffer(final WorldRenderer p_178506_1_, final VertexBuffer vertexBufferIn) {
        this.vertexUploader.setVertexBuffer(vertexBufferIn);
        this.vertexUploader.func_181679_a(p_178506_1_);
    }
    
    public void clearChunkUpdates() {
        while (!this.queueChunkUpdates.isEmpty()) {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.queueChunkUpdates.poll();
            if (chunkcompiletaskgenerator != null) {
                chunkcompiletaskgenerator.finish();
            }
        }
    }
}
