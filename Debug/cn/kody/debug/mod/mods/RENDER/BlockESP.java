package cn.kody.debug.mod.mods.RENDER;

import java.util.List;
import java.util.function.Predicate;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderManager;
import java.util.Arrays;
import java.util.Iterator;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventBlock;
import cn.kody.debug.events.EventRender;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;

import java.util.ArrayList;

public class BlockESP extends Mod
{
    public static ArrayList<BlockPos> toRender;
    public Value<Boolean> dia;
    public Value<Boolean> gold;
    public Value<Boolean> iron;
    public Value<Boolean> lapis;
    public Value<Boolean> emerald;
    public Value<Boolean> coal;
    public Value<Boolean> redstone;
    public Value<Boolean> GhostChunkTest;
    public Value<Boolean> bypass;
    public Value<Double> depth;
    public Value<Double> limit;
    
    public BlockESP() {
        super("BlockESP", "Block ESP", Category.RENDER);
        this.dia = new Value<Boolean>("BlockESP_Diamond", true);
        this.gold = new Value<Boolean>("BlockESP_Gold", true);
        this.iron = new Value<Boolean>("BlockESP_Iron", true);
        this.lapis = new Value<Boolean>("BlockESP_Lapis", true);
        this.emerald = new Value<Boolean>("BlockESP_Emerald", true);
        this.coal = new Value<Boolean>("BlockESP_Coal", true);
        this.redstone = new Value<Boolean>("BlockESP_RedStone", true);
        this.GhostChunkTest = new Value<Boolean>("BlockESP_GhostChunkTest", true);
        this.bypass = new Value<Boolean>("BlockESP_TouchingAirOrLiquidTest", true);
        this.depth = new Value<Double>("BlockESP_TestDepth", 2.0, 1.0, 5.0, 1.0);
        this.limit = new Value<Double>("BlockESP_RenderLimit", 10.0, 5.0, 100.0, 5.0);
    }
    
    @Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
        BlockESP.toRender.clear();
        super.onEnable();
    }
    
    @EventTarget
    public void onRenderBlock(EventBlock class1631) {
        BlockPos blockPos = new BlockPos(class1631.x, class1631.y, class1631.z);
        if (!BlockESP.toRender.contains(blockPos) && this.isTarget(blockPos) && BlockESP.toRender.size() < this.limit.getValueState()) {
            if (this.bypass.getValueState()) {
                if (this.oreTest(blockPos, this.depth.getValueState())) {
                    if (this.GhostChunkTest.getValueState()) {
                        if (this.noSurroundingGhostChunks(blockPos)) {
                            BlockESP.toRender.add(blockPos);
                        }
                    }
                    else {
                        BlockESP.toRender.add(blockPos);
                    }
                }
            }
            else {
                BlockESP.toRender.add(blockPos);
            }
        }
        int i = 0;
        while (i < BlockESP.toRender.size()) {
            BlockPos blockPos2 = BlockESP.toRender.get(i);
            if (!this.isTarget(blockPos2)) {
                BlockESP.toRender.remove(i);
            }
            if (this.bypass.getValueState() && !this.oreTest(blockPos2, this.depth.getValueState())) {
                BlockESP.toRender.remove(i);
            }
            if (this.GhostChunkTest.getValueState() && !this.noSurroundingGhostChunks(blockPos2)) {
                BlockESP.toRender.remove(i);
            }
            ++i;
        }
    }
    
    @EventTarget
    public void onRender(EventRender class1170) {
        Iterator<BlockPos> iterator = BlockESP.toRender.iterator();
        while (iterator.hasNext()) {
            this.renderBlock(iterator.next());
        }
    }
    
    private boolean noSurroundingGhostChunks(BlockPos p_getChunkFromBlockCoords_1_) {
        Chunk chunkFromBlockCoords = this.mc.theWorld.getChunkFromBlockCoords(p_getChunkFromBlockCoords_1_);
        return Arrays.asList(new Chunk(this.mc.theWorld, chunkFromBlockCoords.xPosition, chunkFromBlockCoords.zPosition + 1), new Chunk(this.mc.theWorld, chunkFromBlockCoords.xPosition, chunkFromBlockCoords.zPosition - 1), new Chunk(this.mc.theWorld, chunkFromBlockCoords.xPosition + 1, chunkFromBlockCoords.zPosition), new Chunk(this.mc.theWorld, chunkFromBlockCoords.xPosition - 1, chunkFromBlockCoords.zPosition)).stream().allMatch(Chunk::isLoaded);
    }
    
    private void renderBlock(BlockPos blockPos) {
        double n = blockPos.getX() - RenderManager.renderPosX;
        double n2 = blockPos.getY() - RenderManager.renderPosY;
        double n3 = blockPos.getZ() - RenderManager.renderPosZ;
        float[] array = this.getColor(blockPos);
        drawOutlinedBlockESP(n, n2, n3, array[0], array[1], array[2], 0.25f, 2.5f);
    }
    
    private boolean isTarget(BlockPos p_getBlockState_1_) {
        Block block = this.mc.theWorld.getBlockState(p_getBlockState_1_).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return this.dia.getValueState();
        }
        if (Blocks.lapis_ore.equals(block)) {
            return this.lapis.getValueState();
        }
        if (Blocks.iron_ore.equals(block)) {
            return this.iron.getValueState();
        }
        if (Blocks.gold_ore.equals(block)) {
            return this.gold.getValueState();
        }
        if (Blocks.coal_ore.equals(block)) {
            return this.coal.getValueState();
        }
        if (Blocks.emerald_ore.equals(block)) {
            return this.emerald.getValueState();
        }
        return (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) && this.redstone.getValueState();
    }
    
    private float[] getColor(BlockPos p_getBlockState_1_) {
        Block block = this.mc.theWorld.getBlockState(p_getBlockState_1_).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return new float[] { 0.0f, 1.0f, 1.0f };
        }
        if (Blocks.lapis_ore.equals(block)) {
            return new float[] { 0.0f, 0.0f, 1.0f };
        }
        if (Blocks.iron_ore.equals(block)) {
            return new float[] { 1.0f, 1.0f, 1.0f };
        }
        if (Blocks.gold_ore.equals(block)) {
            return new float[] { 1.0f, 1.0f, 0.0f };
        }
        if (Blocks.coal_ore.equals(block)) {
            return new float[] { 0.0f, 0.0f, 0.0f };
        }
        if (Blocks.emerald_ore.equals(block)) {
            return new float[] { 0.0f, 1.0f, 0.0f };
        }
        if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return new float[] { 1.0f, 0.0f, 0.0f };
        }
        return new float[] { 0.0f, 0.0f, 0.0f };
    }
    
    public static void drawOutlinedBlockESP(double p_i485_1_, double p_i485_3_, double p_i485_5_, float n, float n2, float n3, float n4, float n5) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(n5);
        GL11.glColor4f(n, n2, n3, n4);
        drawOutlinedBoundingBox(new AxisAlignedBB(p_i485_1_, p_i485_3_, p_i485_5_, p_i485_1_ + 1.0, p_i485_3_ + 1.0, p_i485_5_ + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBoundingBox(AxisAlignedBB class1593) {
        Tessellator instance = Tessellator.getInstance();
        WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(class1593.minX, class1593.minY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.minY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.minY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.minY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.minY, class1593.minZ).endVertex();
        instance.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(class1593.minX, class1593.maxY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.maxY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.maxY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.maxY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.maxY, class1593.minZ).endVertex();
        instance.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(class1593.minX, class1593.minY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.maxY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.minY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.maxY, class1593.minZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.minY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.maxX, class1593.maxY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.minY, class1593.maxZ).endVertex();
        worldRenderer.pos(class1593.minX, class1593.maxY, class1593.maxZ).endVertex();
        instance.draw();
    }
    
    private Boolean oreTest(final BlockPos blockPos, final Double n) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        ArrayList<BlockPos> list2 = new ArrayList<BlockPos>(Collections.singletonList(blockPos));
        final ArrayList list3 = new ArrayList();
        int n2 = 0;
        while (n2 < n) {
            for (final BlockPos blockPos2 : list2) {
                list.add(blockPos2.up());
                list.add(blockPos2.down());
                list.add(blockPos2.north());
                list.add(blockPos2.south());
                list.add(blockPos2.west());
                list.add(blockPos2.east());
            }
            for (final BlockPos blockPos3 : list) {
                if (list2.contains(blockPos3)) {
                    list.remove(blockPos3);
                }
            }
            list2 = list;
            list3.addAll(list);
            list = new ArrayList<BlockPos>();
            ++n2;
        }
        List list4 = Arrays.asList((Object[])new Block[]{Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air});
        return list3.stream().anyMatch(arg_0 -> this.lambda$oreTest$0(list4, (BlockPos) arg_0));    
      }
    
    private boolean lambda$oreTest$0(final List list, final BlockPos blockPos) {
        return list.contains(this.mc.theWorld.getBlockState(blockPos).getBlock());
    }
    
    static {
        BlockESP.toRender = new ArrayList<BlockPos>();
    }
}
