package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum
{
    protected final RenderGlobal field_178169_a;
    protected final World field_178167_b;
    protected int field_178168_c;
    protected int field_178165_d;
    protected int field_178166_e;
    public RenderChunk[] field_178164_f;
    public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal p_i46246_3_, IRenderChunkFactory p_i46246_4_)
    {
        this.field_178169_a = p_i46246_3_;
        this.field_178167_b = worldIn;
        this.func_178159_a(renderDistanceChunks);
        this.func_178158_a(p_i46246_4_);
    }

    protected void func_178158_a(IRenderChunkFactory p_178158_1_)
    {
        int var2 = this.field_178165_d * this.field_178168_c * this.field_178166_e;
        this.field_178164_f = new RenderChunk[var2];
        int var3 = 0;

        for (int var4 = 0; var4 < this.field_178165_d; ++var4)
        {
            for (int var5 = 0; var5 < this.field_178168_c; ++var5)
            {
                for (int var6 = 0; var6 < this.field_178166_e; ++var6)
                {
                    int var7 = (var6 * this.field_178168_c + var5) * this.field_178165_d + var4;
                    BlockPos var8 = new BlockPos(var4 * 16, var5 * 16, var6 * 16);
                    this.field_178164_f[var7] = p_178158_1_.func_178602_a(this.field_178167_b, this.field_178169_a, var8, var3++);
                }
            }
        }
    }

    public void func_178160_a()
    {
        RenderChunk[] var1 = this.field_178164_f;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            RenderChunk var4 = var1[var3];
            var4.func_178566_a();
        }
    }

    protected void func_178159_a(int renderDistanceChunks)
    {
        int var2 = renderDistanceChunks * 2 + 1;
        this.field_178165_d = var2;
        this.field_178168_c = 16;
        this.field_178166_e = var2;
    }

    public void func_178163_a(double viewEntityX, double viewEntityZ)
    {
        int var5 = MathHelper.floor_double(viewEntityX) - 8;
        int var6 = MathHelper.floor_double(viewEntityZ) - 8;
        int var7 = this.field_178165_d * 16;

        for (int var8 = 0; var8 < this.field_178165_d; ++var8)
        {
            int var9 = this.func_178157_a(var5, var7, var8);

            for (int var10 = 0; var10 < this.field_178166_e; ++var10)
            {
                int var11 = this.func_178157_a(var6, var7, var10);

                for (int var12 = 0; var12 < this.field_178168_c; ++var12)
                {
                    int var13 = var12 * 16;
                    RenderChunk var14 = this.field_178164_f[(var10 * this.field_178168_c + var12) * this.field_178165_d + var8];
                    BlockPos posChunk = var14.func_178568_j();

                    if (posChunk.getX() != var9 || posChunk.getY() != var13 || posChunk.getZ() != var11)
                    {
                        BlockPos var15 = new BlockPos(var9, var13, var11);

                        if (!var15.equals(var14.func_178568_j()))
                        {
                            var14.func_178576_a(var15);
                        }
                    }
                }
            }
        }
    }

    private int func_178157_a(int p_178157_1_, int p_178157_2_, int p_178157_3_)
    {
        int var4 = p_178157_3_ * 16;
        int var5 = var4 - p_178157_1_ + p_178157_2_ / 2;

        if (var5 < 0)
        {
            var5 -= p_178157_2_ - 1;
        }

        return var4 - var5 / p_178157_2_ * p_178157_2_;
    }

    public void func_178162_a(int p_178162_1_, int p_178162_2_, int p_178162_3_, int p_178162_4_, int p_178162_5_, int p_178162_6_)
    {
        int var7 = MathHelper.bucketInt(p_178162_1_, 16);
        int var8 = MathHelper.bucketInt(p_178162_2_, 16);
        int var9 = MathHelper.bucketInt(p_178162_3_, 16);
        int var10 = MathHelper.bucketInt(p_178162_4_, 16);
        int var11 = MathHelper.bucketInt(p_178162_5_, 16);
        int var12 = MathHelper.bucketInt(p_178162_6_, 16);

        for (int var13 = var7; var13 <= var10; ++var13)
        {
            int var14 = var13 % this.field_178165_d;

            if (var14 < 0)
            {
                var14 += this.field_178165_d;
            }

            for (int var15 = var8; var15 <= var11; ++var15)
            {
                int var16 = var15 % this.field_178168_c;

                if (var16 < 0)
                {
                    var16 += this.field_178168_c;
                }

                for (int var17 = var9; var17 <= var12; ++var17)
                {
                    int var18 = var17 % this.field_178166_e;

                    if (var18 < 0)
                    {
                        var18 += this.field_178166_e;
                    }

                    int var19 = (var18 * this.field_178168_c + var16) * this.field_178165_d + var14;
                    RenderChunk var20 = this.field_178164_f[var19];
                    var20.func_178575_a(true);
                }
            }
        }
    }

    protected RenderChunk func_178161_a(BlockPos p_178161_1_)
    {
        int var2 = MathHelper.bucketInt(p_178161_1_.getX(), 16);
        int var3 = MathHelper.bucketInt(p_178161_1_.getY(), 16);
        int var4 = MathHelper.bucketInt(p_178161_1_.getZ(), 16);

        if (var3 >= 0 && var3 < this.field_178168_c)
        {
            var2 %= this.field_178165_d;

            if (var2 < 0)
            {
                var2 += this.field_178165_d;
            }

            var4 %= this.field_178166_e;

            if (var4 < 0)
            {
                var4 += this.field_178166_e;
            }

            int var5 = (var4 * this.field_178168_c + var3) * this.field_178165_d + var2;
            return this.field_178164_f[var5];
        }
        else
        {
            return null;
        }
    }
}
