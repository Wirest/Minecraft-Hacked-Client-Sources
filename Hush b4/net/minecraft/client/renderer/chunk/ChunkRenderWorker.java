// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.concurrent.CancellationException;
import java.util.List;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import net.minecraft.util.EnumWorldBlockLayer;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER;
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher p_i46201_1_) {
        this(p_i46201_1_, null);
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher chunkRenderDispatcherIn, final RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
        }
        catch (InterruptedException var3) {
            ChunkRenderWorker.LOGGER.debug("Stopping due to interrupt");
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
        }
    }
    
    protected void processTask(final ChunkCompileTaskGenerator generator) throws InterruptedException {
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (!generator.isFinished()) {
                    ChunkRenderWorker.LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be pending; ignoring task");
                }
                return;
            }
            generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
        }
        finally {
            generator.getLock().unlock();
        }
        generator.getLock().unlock();
        final Entity lvt_2_1_ = Minecraft.getMinecraft().getRenderViewEntity();
        if (lvt_2_1_ == null) {
            generator.finish();
        }
        else {
            generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            final float f = (float)lvt_2_1_.posX;
            final float f2 = (float)lvt_2_1_.posY + lvt_2_1_.getEyeHeight();
            final float f3 = (float)lvt_2_1_.posZ;
            final ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                generator.getRenderChunk().rebuildChunk(f, f2, f3, generator);
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                generator.getRenderChunk().resortTransparency(f, f2, f3, generator);
            }
            generator.getLock().lock();
            try {
                if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                    if (!generator.isFinished()) {
                        ChunkRenderWorker.LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be compiling; aborting task");
                    }
                    this.freeRenderBuilder(generator);
                    return;
                }
                generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally {
                generator.getLock().unlock();
            }
            generator.getLock().unlock();
            final CompiledChunk lvt_7_1_ = generator.getCompiledChunk();
            final ArrayList lvt_8_1_ = Lists.newArrayList();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                EnumWorldBlockLayer[] values;
                for (int length = (values = EnumWorldBlockLayer.values()).length, i = 0; i < length; ++i) {
                    final EnumWorldBlockLayer enumworldblocklayer = values[i];
                    if (lvt_7_1_.isLayerStarted(enumworldblocklayer)) {
                        lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), generator.getRenderChunk(), lvt_7_1_));
                    }
                }
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                lvt_8_1_.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), generator.getRenderChunk(), lvt_7_1_));
            }
            final ListenableFuture<List<Object>> listenablefuture = Futures.allAsList((Iterable<? extends ListenableFuture<?>>)lvt_8_1_);
            generator.addFinishRunnable(new Runnable() {
                @Override
                public void run() {
                    listenablefuture.cancel(false);
                }
            });
            Futures.addCallback(listenablefuture, new FutureCallback<List<Object>>() {
                @Override
                public void onSuccess(final List<Object> p_onSuccess_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    generator.getLock().lock();
                    Label_0129: {
                        try {
                            if (generator.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                                generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                                break Label_0129;
                            }
                            if (!generator.isFinished()) {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was " + generator.getStatus() + " when I expected it to be uploading; aborting task");
                            }
                        }
                        finally {
                            generator.getLock().unlock();
                        }
                        generator.getLock().unlock();
                        return;
                    }
                    generator.getRenderChunk().setCompiledChunk(lvt_7_1_);
                }
                
                @Override
                public void onFailure(final Throwable p_onFailure_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException)) {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
            });
        }
    }
    
    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
        return (this.regionRenderCacheBuilder != null) ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }
    
    private void freeRenderBuilder(final ChunkCompileTaskGenerator taskGenerator) {
        if (this.regionRenderCacheBuilder == null) {
            this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
        }
    }
}
