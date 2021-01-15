package net.minecraft.client.renderer.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_)
    {
        this(p_i46201_1_, (RegionRenderCacheBuilder)null);
    }

    public ChunkRenderWorker(ChunkRenderDispatcher chunkRenderDispatcherIn, RegionRenderCacheBuilder regionRenderCacheBuilderIn)
    {
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }

    @Override
	public void run()
    {
        try
        {
            while (true)
            {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
        }
        catch (InterruptedException var3)
        {
            LOGGER.debug("Stopping due to interrupt");
        }
        catch (Throwable var4)
        {
            CrashReport var2 = CrashReport.makeCrashReport(var4, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(var2));
        }
    }

    protected void processTask(final ChunkCompileTaskGenerator generator) throws InterruptedException
    {
        generator.getLock().lock();
        label235:
        {
            try
            {
                if (generator.getStatus() == ChunkCompileTaskGenerator.Status.PENDING)
                {
                    generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
                    break label235;
                }

                if (!generator.isFinished())
                {
                    LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be pending; ignoring task");
                }
            }
            finally
            {
                generator.getLock().unlock();
            }

            return;
        }
        Entity var2 = Minecraft.getMinecraft().getRenderViewEntity();

        if (var2 == null)
        {
            generator.finish();
        }
        else
        {
            generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            float var3 = (float)var2.posX;
            float var4 = (float)var2.posY + var2.getEyeHeight();
            float var5 = (float)var2.posZ;
            ChunkCompileTaskGenerator.Type var6 = generator.getType();

            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                generator.getRenderChunk().rebuildChunk(var3, var4, var5, generator);
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                generator.getRenderChunk().resortTransparency(var3, var4, var5, generator);
            }

            generator.getLock().lock();
            label223:
            {
                try
                {
                    if (generator.getStatus() == ChunkCompileTaskGenerator.Status.COMPILING)
                    {
                        generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
                        break label223;
                    }

                    if (!generator.isFinished())
                    {
                        LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be compiling; aborting task");
                    }

                    this.freeRenderBuilder(generator);
                }
                finally
                {
                    generator.getLock().unlock();
                }

                return;
            }
            final CompiledChunk var7 = generator.getCompiledChunk();
            ArrayList var8 = Lists.newArrayList();

            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                EnumWorldBlockLayer[] var9 = EnumWorldBlockLayer.values();
                int var10 = var9.length;

                for (int var11 = 0; var11 < var10; ++var11)
                {
                    EnumWorldBlockLayer var12 = var9[var11];

                    if (var7.isLayerStarted(var12))
                    {
                        var8.add(this.chunkRenderDispatcher.uploadChunk(var12, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(var12), generator.getRenderChunk(), var7));
                    }
                }
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                var8.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), generator.getRenderChunk(), var7));
            }

            final ListenableFuture var19 = Futures.allAsList(var8);
            generator.addFinishRunnable(new Runnable()
            {
                @Override
				public void run()
                {
                    var19.cancel(false);
                }
            });
            Futures.addCallback(var19, new FutureCallback()
            {
                public void onSuccessList(List p_178481_1_)
                {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    generator.getLock().lock();

                    try
                    {
                        if (generator.getStatus() != ChunkCompileTaskGenerator.Status.UPLOADING)
                        {
                            if (!generator.isFinished())
                            {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be uploading; aborting task");
                            }

                            return;
                        }

                        generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                    }
                    finally
                    {
                        generator.getLock().unlock();
                    }

                    generator.getRenderChunk().setCompiledChunk(var7);
                }
                @Override
				public void onFailure(Throwable p_onFailure_1_)
                {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);

                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException))
                    {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
                @Override
				public void onSuccess(Object p_onSuccess_1_)
                {
                    this.onSuccessList((List)p_onSuccess_1_);
                }
            });
        }
    }

    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException
    {
        return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }

    private void freeRenderBuilder(ChunkCompileTaskGenerator taskGenerator)
    {
        if (this.regionRenderCacheBuilder == null)
        {
            this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
        }
    }
}
