package net.minecraft.client.renderer.chunk;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.optifine.BlockPosM;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class RenderChunk {
    private World field_178588_d;
    private final RenderGlobal field_178589_e;
    public static int field_178592_a;
    private BlockPos field_178586_f;
    public CompiledChunk field_178590_b;
    private final ReentrantLock field_178587_g;
    private final ReentrantLock field_178598_h;
    private ChunkCompileTaskGenerator field_178599_i;
    private final int field_178596_j;
    private final FloatBuffer field_178597_k;
    private final VertexBuffer[] field_178594_l;
    public AxisAlignedBB field_178591_c;
    private int field_178595_m;
    private boolean field_178593_n;
    private static final String __OBFID = "CL_00002452";
    private BlockPos[] positionOffsets16;

    public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn) {
        this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
        this.field_178590_b = CompiledChunk.field_178502_a;
        this.field_178587_g = new ReentrantLock();
        this.field_178598_h = new ReentrantLock();
        this.field_178599_i = null;
        this.field_178597_k = GLAllocation.createDirectFloatBuffer(16);
        this.field_178594_l = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.field_178595_m = -1;
        this.field_178593_n = true;
        this.field_178588_d = worldIn;
        this.field_178589_e = renderGlobalIn;
        this.field_178596_j = indexIn;

        if (!blockPosIn.equals(this.func_178568_j())) {
            this.func_178576_a(blockPosIn);
        }

        if (OpenGlHelper.func_176075_f()) {
            for (int var5 = 0; var5 < EnumWorldBlockLayer.values().length; ++var5) {
                this.field_178594_l[var5] = new VertexBuffer(DefaultVertexFormats.field_176600_a);
            }
        }
    }

    public boolean func_178577_a(int frameIndexIn) {
        if (this.field_178595_m == frameIndexIn) {
            return false;
        } else {
            this.field_178595_m = frameIndexIn;
            return true;
        }
    }

    public VertexBuffer func_178565_b(int p_178565_1_) {
        return this.field_178594_l[p_178565_1_];
    }

    public void func_178576_a(BlockPos p_178576_1_) {
        this.func_178585_h();
        this.field_178586_f = p_178576_1_;
        this.field_178591_c = new AxisAlignedBB(p_178576_1_, p_178576_1_.add(16, 16, 16));
        this.func_178567_n();

        for (int i = 0; i < this.positionOffsets16.length; ++i) {
            this.positionOffsets16[i] = null;
        }
    }

    public void func_178570_a(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_) {
        CompiledChunk var5 = p_178570_4_.func_178544_c();

        if (var5.func_178487_c() != null && !var5.func_178491_b(EnumWorldBlockLayer.TRANSLUCENT)) {
            WorldRenderer worldRenderer = p_178570_4_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT);
            this.func_178573_a(worldRenderer, this.field_178586_f);
            worldRenderer.setVertexState(var5.func_178487_c());
            this.func_178584_a(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, worldRenderer, var5);
        }
    }

    public void func_178581_b(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_) {
        CompiledChunk var5 = new CompiledChunk();
        boolean var6 = true;
        BlockPos var7 = this.field_178586_f;
        BlockPos var8 = var7.add(15, 15, 15);
        p_178581_4_.func_178540_f().lock();
        RegionRenderCache var9;

        try {
            if (p_178581_4_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }

            if (this.field_178588_d == null) {
                return;
            }

            var9 = new RegionRenderCache(this.field_178588_d, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
            p_178581_4_.func_178543_a(var5);
        } finally {
            p_178581_4_.func_178540_f().unlock();
        }

        VisGraph var10 = new VisGraph();

        if (!var9.extendedLevelsInChunkCache()) {
            ++field_178592_a;
            Iterator var11 = BlockPosM.getAllInBoxMutable(var7, var8).iterator();
            EnumWorldBlockLayer var231;

            while (var11.hasNext()) {
                BlockPosM var20 = (BlockPosM) var11.next();
                IBlockState var21 = var9.getBlockState(var20);
                Block var22 = var21.getBlock();

                if (var22.isOpaqueCube()) {
                    var10.func_178606_a(var20);
                }

                if (var22.hasTileEntity()) {
                    TileEntity var23 = var9.getTileEntity(new BlockPos(var20));

                    if (var23 != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(var23)) {
                        var5.func_178490_a(var23);
                    }
                }

                var231 = var22.getBlockLayer();
                int var16 = var231.ordinal();

                if (var22.getRenderType() != -1) {
                    WorldRenderer var17 = p_178581_4_.func_178545_d().func_179039_a(var16);

                    if (!var5.func_178492_d(var231)) {
                        var5.func_178493_c(var231);
                        this.func_178573_a(var17, var7);
                    }

                    if (Minecraft.getMinecraft().getBlockRendererDispatcher().func_175018_a(var21, var20, var9, var17)) {
                        var5.func_178486_a(var231);
                    }
                }
            }

            EnumWorldBlockLayer[] var201 = EnumWorldBlockLayer.values();
            int var211 = var201.length;

            for (int var221 = 0; var221 < var211; ++var221) {
                var231 = var201[var221];

                if (var5.func_178492_d(var231)) {
                    this.func_178584_a(var231, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.func_178545_d().func_179038_a(var231), var5);
                }
            }
        }

        var5.func_178488_a(var10.func_178607_a());
    }

    protected void func_178578_b() {
        this.field_178587_g.lock();

        try {
            if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
                this.field_178599_i.func_178542_e();
                this.field_178599_i = null;
            }
        } finally {
            this.field_178587_g.unlock();
        }
    }

    public ReentrantLock func_178579_c() {
        return this.field_178587_g;
    }

    public ChunkCompileTaskGenerator func_178574_d() {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator var1;

        try {
            this.func_178578_b();
            this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            var1 = this.field_178599_i;
        } finally {
            this.field_178587_g.unlock();
        }

        return var1;
    }

    public ChunkCompileTaskGenerator func_178582_e() {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator var1;

        try {
            if (this.field_178599_i == null || this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
                    this.field_178599_i.func_178542_e();
                    this.field_178599_i = null;
                }

                this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
                this.field_178599_i.func_178543_a(this.field_178590_b);
                var1 = this.field_178599_i;
                ChunkCompileTaskGenerator var2 = var1;
                return var2;
            }

            var1 = null;
        } finally {
            this.field_178587_g.unlock();
        }

        return var1;
    }

    private void func_178573_a(WorldRenderer p_178573_1_, BlockPos p_178573_2_) {
        p_178573_1_.startDrawing(7);
        p_178573_1_.setVertexFormat(DefaultVertexFormats.field_176600_a);
        p_178573_1_.setTranslation((double) (-p_178573_2_.getX()), (double) (-p_178573_2_.getY()), (double) (-p_178573_2_.getZ()));
    }

    private void func_178584_a(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_) {
        if (p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT && !p_178584_6_.func_178491_b(p_178584_1_)) {
            p_178584_6_.func_178494_a(p_178584_5_.getVertexState(p_178584_2_, p_178584_3_, p_178584_4_));
        }

        p_178584_5_.draw();
    }

    private void func_178567_n() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float var1 = 1.000001F;
        GlStateManager.translate(-8.0F, -8.0F, -8.0F);
        GlStateManager.scale(var1, var1, var1);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.getFloat(2982, this.field_178597_k);
        GlStateManager.popMatrix();
    }

    public void func_178572_f() {
        GlStateManager.multMatrix(this.field_178597_k);
    }

    public CompiledChunk func_178571_g() {
        return this.field_178590_b;
    }

    public void func_178580_a(CompiledChunk p_178580_1_) {
        this.field_178598_h.lock();

        try {
            this.field_178590_b = p_178580_1_;
        } finally {
            this.field_178598_h.unlock();
        }
    }

    public void func_178585_h() {
        this.func_178578_b();
        this.field_178590_b = CompiledChunk.field_178502_a;
    }

    public void func_178566_a() {
        this.func_178585_h();
        this.field_178588_d = null;

        for (int var1 = 0; var1 < EnumWorldBlockLayer.values().length; ++var1) {
            if (this.field_178594_l[var1] != null) {
                this.field_178594_l[var1].func_177362_c();
            }
        }
    }

    public BlockPos func_178568_j() {
        return this.field_178586_f;
    }

    public boolean func_178583_l() {
        this.field_178587_g.lock();
        boolean var1;

        try {
            var1 = this.field_178599_i == null || this.field_178599_i.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING;
        } finally {
            this.field_178587_g.unlock();
        }

        return var1;
    }

    public void func_178575_a(boolean p_178575_1_) {
        this.field_178593_n = p_178575_1_;
    }

    public boolean func_178569_m() {
        return this.field_178593_n;
    }

    public BlockPos getPositionOffset16(EnumFacing facing) {
        int index = facing.getIndex();
        BlockPos posOffset = this.positionOffsets16[index];

        if (posOffset == null) {
            posOffset = this.func_178568_j().offset(facing, 16);
            this.positionOffsets16[index] = posOffset;
        }

        return posOffset;
    }
}
