package net.minecraft.client.renderer.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;

public class ChunkRenderDispatcher
{
    private static final Logger logger = LogManager.getLogger();
    private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final List listThreadedWorkers = Lists.newArrayList();
    private final BlockingQueue queueChunkUpdates = Queues.newArrayBlockingQueue(100);
    private final BlockingQueue queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
    private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
    private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
    private final Queue queueChunkUploads = Queues.newArrayDeque();
    private final ChunkRenderWorker renderWorker;

    public ChunkRenderDispatcher()
    {
        int var1;

        for (var1 = 0; var1 < 2; ++var1)
        {
            ChunkRenderWorker var2 = new ChunkRenderWorker(this);
            Thread var3 = threadFactory.newThread(var2);
            var3.start();
            this.listThreadedWorkers.add(var2);
        }

        for (var1 = 0; var1 < 5; ++var1)
        {
            this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
        }

        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public String getDebugInfo()
    {
        return String.format("pC: %03d, pU: %1d, aB: %1d", new Object[] {Integer.valueOf(this.queueChunkUpdates.size()), Integer.valueOf(this.queueChunkUploads.size()), Integer.valueOf(this.queueFreeRenderBuilders.size())});
    }

    public boolean runChunkUploads(long p_178516_1_)
    {
        boolean var3 = false;
        long var8;

        do
        {
            boolean var4 = false;
            synchronized (this.queueChunkUploads)
            {
                if (!this.queueChunkUploads.isEmpty())
                {
                    ((ListenableFutureTask)this.queueChunkUploads.poll()).run();
                    var4 = true;
                    var3 = true;
                }
            }

            if (p_178516_1_ == 0L || !var4)
            {
                break;
            }

            var8 = p_178516_1_ - System.nanoTime();
        }
        while (var8 >= 0L && var8 <= 1000000000L);

        return var3;
    }

    public boolean updateChunkLater(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean var4;

        try
        {
            final ChunkCompileTaskGenerator var2 = chunkRenderer.makeCompileTaskChunk();
            var2.addFinishRunnable(new Runnable()
            {
                @Override
				public void run()
                {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(var2);
                }
            });
            boolean var3 = this.queueChunkUpdates.offer(var2);

            if (!var3)
            {
                var2.finish();
            }

            var4 = var3;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return var4;
    }

    public boolean updateChunkNow(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean var3;

        try
        {
            ChunkCompileTaskGenerator var2 = chunkRenderer.makeCompileTaskChunk();

            try
            {
                this.renderWorker.processTask(var2);
            }
            catch (InterruptedException var8)
            {
                ;
            }

            var3 = true;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return var3;
    }

    public void stopChunkUpdates()
    {
        this.clearChunkUpdates();

        while (this.runChunkUploads(0L))
        {
            ;
        }

        ArrayList var1 = Lists.newArrayList();

        while (var1.size() != 5)
        {
            try
            {
                var1.add(this.allocateRenderBuilder());
            }
            catch (InterruptedException var3)
            {
                ;
            }
        }

        this.queueFreeRenderBuilders.addAll(var1);
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_)
    {
        this.queueFreeRenderBuilders.add(p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException, InterruptedException
    {
        return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException, InterruptedException
    {
        return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
    }

    public boolean updateTransparencyLater(RenderChunk chunkRenderer)
    {
        chunkRenderer.getLockCompileTask().lock();
        boolean var4;

        try
        {
            final ChunkCompileTaskGenerator var2 = chunkRenderer.makeCompileTaskTransparency();
            boolean var3;

            if (var2 == null)
            {
                var3 = true;
                return var3;
            }

            var2.addFinishRunnable(new Runnable()
            {
                @Override
				public void run()
                {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(var2);
                }
            });
            var3 = this.queueChunkUpdates.offer(var2);
            var4 = var3;
        }
        finally
        {
            chunkRenderer.getLockCompileTask().unlock();
        }

        return var4;
    }

    public ListenableFuture uploadChunk(final EnumWorldBlockLayer player, final WorldRenderer p_178503_2_, final RenderChunk chunkRenderer, final CompiledChunk compiledChunkIn)
    {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread())
        {
            if (OpenGlHelper.useVbo())
            {
                this.uploadVertexBuffer(p_178503_2_, chunkRenderer.getVertexBufferByLayer(player.ordinal()));
            }
            else
            {
                this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)chunkRenderer).getDisplayList(player, compiledChunkIn), chunkRenderer);
            }

            p_178503_2_.setTranslation(0.0D, 0.0D, 0.0D);
            return Futures.immediateFuture((Object)null);
        }
        else
        {
            ListenableFutureTask var5 = ListenableFutureTask.create(new Runnable()
            {
                @Override
				public void run()
                {
                    ChunkRenderDispatcher.this.uploadChunk(player, p_178503_2_, chunkRenderer, compiledChunkIn);
                }
            }, (Object)null);
            synchronized (this.queueChunkUploads)
            {
                this.queueChunkUploads.add(var5);
                return var5;
            }
        }
    }

    private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk chunkRenderer)
    {
        GL11.glNewList(p_178510_2_, GL11.GL_COMPILE);
        GlStateManager.pushMatrix();
        chunkRenderer.multModelviewMatrix();
        this.worldVertexUploader.draw(p_178510_1_, p_178510_1_.getByteIndex());
        GlStateManager.popMatrix();
        GL11.glEndList();
    }

    private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer vertexBufferIn)
    {
        this.vertexUploader.setVertexBuffer(vertexBufferIn);
        this.vertexUploader.draw(p_178506_1_, p_178506_1_.getByteIndex());
    }

    public void clearChunkUpdates()
    {
        while (!this.queueChunkUpdates.isEmpty())
        {
            ChunkCompileTaskGenerator task = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();

            if (task != null)
            {
                task.finish();
            }
        }
    }
}
