package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkRenderDispatcher field_178477_b;
    private final RegionRenderCacheBuilder field_178478_c;
    private static final String __OBFID = "CL_00002459";

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_)
    {
        this(p_i46201_1_, (RegionRenderCacheBuilder)null);
    }

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46202_1_, RegionRenderCacheBuilder p_i46202_2_)
    {
        this.field_178477_b = p_i46202_1_;
        this.field_178478_c = p_i46202_2_;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                this.func_178474_a(this.field_178477_b.func_178511_d());
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

    protected void func_178474_a(final ChunkCompileTaskGenerator p_178474_1_) throws InterruptedException
    {
        p_178474_1_.func_178540_f().lock();
        label242:
        {
            try
            {
                if (p_178474_1_.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING)
                {
                    p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.COMPILING);
                    break label242;
                }

                if (!p_178474_1_.func_178537_h())
                {
                    LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be pending; ignoring task");
                }
            }
            finally
            {
                p_178474_1_.func_178540_f().unlock();
            }

            return;
        }
        Entity var2 = Minecraft.getMinecraft().func_175606_aa();

        if (var2 == null)
        {
            p_178474_1_.func_178542_e();
        }
        else
        {
            p_178474_1_.func_178541_a(this.func_178475_b());
            float var3 = (float)var2.posX;
            float var4 = (float)var2.posY + var2.getEyeHeight();
            float var5 = (float)var2.posZ;
            ChunkCompileTaskGenerator.Type var6 = p_178474_1_.func_178538_g();

            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                p_178474_1_.func_178536_b().func_178581_b(var3, var4, var5, p_178474_1_);
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                p_178474_1_.func_178536_b().func_178570_a(var3, var4, var5, p_178474_1_);
            }

            p_178474_1_.func_178540_f().lock();

            try
            {
                if (p_178474_1_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING)
                {
                    if (!p_178474_1_.func_178537_h())
                    {
                        LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be compiling; aborting task");
                    }

                    this.func_178473_b(p_178474_1_);
                    return;
                }

                p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally
            {
                p_178474_1_.func_178540_f().unlock();
            }

            final CompiledChunk var7 = p_178474_1_.func_178544_c();
            ArrayList var8 = Lists.newArrayList();

            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                EnumWorldBlockLayer[] var9 = EnumWorldBlockLayer.values();
                int var10 = var9.length;

                for (int var11 = 0; var11 < var10; ++var11)
                {
                    EnumWorldBlockLayer var12 = var9[var11];

                    if (var7.func_178492_d(var12))
                    {
                        var8.add(this.field_178477_b.func_178503_a(var12, p_178474_1_.func_178545_d().func_179038_a(var12), p_178474_1_.func_178536_b(), var7));
                    }
                }
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                var8.add(this.field_178477_b.func_178503_a(EnumWorldBlockLayer.TRANSLUCENT, p_178474_1_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT), p_178474_1_.func_178536_b(), var7));
            }

            final ListenableFuture var19 = Futures.allAsList(var8);
            p_178474_1_.func_178539_a(new Runnable()
            {
                private static final String __OBFID = "CL_00002458";
                public void run()
                {
                    var19.cancel(false);
                }
            });
            Futures.addCallback(var19, new FutureCallback()
            {
                private static final String __OBFID = "CL_00002457";
                public void func_178481_a(List p_178481_1_)
                {
                    ChunkRenderWorker.this.func_178473_b(p_178474_1_);
                    p_178474_1_.func_178540_f().lock();

                    try
                    {
                        if (p_178474_1_.func_178546_a() != ChunkCompileTaskGenerator.Status.UPLOADING)
                        {
                            if (!p_178474_1_.func_178537_h())
                            {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be uploading; aborting task");
                            }

                            return;
                        }

                        p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.DONE);
                    }
                    finally
                    {
                        p_178474_1_.func_178540_f().unlock();
                    }

                    p_178474_1_.func_178536_b().func_178580_a(var7);
                }
                public void onFailure(Throwable p_onFailure_1_)
                {
                    ChunkRenderWorker.this.func_178473_b(p_178474_1_);

                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException))
                    {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
                public void onSuccess(Object p_onSuccess_1_)
                {
                    this.func_178481_a((List)p_onSuccess_1_);
                }
            });
        }
    }

    private RegionRenderCacheBuilder func_178475_b() throws InterruptedException
    {
        return this.field_178478_c != null ? this.field_178478_c : this.field_178477_b.func_178515_c();
    }

    private void func_178473_b(ChunkCompileTaskGenerator p_178473_1_)
    {
        if (this.field_178478_c == null)
        {
            this.field_178477_b.func_178512_a(p_178473_1_.func_178545_d());
        }
    }
}
