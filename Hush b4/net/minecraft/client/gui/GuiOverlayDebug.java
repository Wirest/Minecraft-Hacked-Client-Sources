// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.FrameTimer;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumChatFormatting;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import java.util.Collection;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.util.BlockPos;
import java.util.List;
import com.google.common.base.Strings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiOverlayDebug extends Gui
{
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private static final String __OBFID = "CL_00001956";
    
    public GuiOverlayDebug(final Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }
    
    public void renderDebugInfo(final ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        this.mc.mcProfiler.endSection();
    }
    
    private boolean isReducedDebug() {
        return Minecraft.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo;
    }
    
    protected void renderDebugInfoLeft() {
        final List list = this.call();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final boolean flag = true;
                final int l = 2 + j * i;
                Gui.drawRect(1.0, l - 1, 2 + k + 1, l + j - 1, -1873784752);
                this.fontRenderer.drawString(s, 2.0, l, 14737632);
            }
        }
    }
    
    protected void renderDebugInfoRight(final ScaledResolution p_175239_1_) {
        final List list = this.getDebugInfoRight();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final int l = p_175239_1_.getScaledWidth() - 2 - k;
                final int i2 = 2 + j * i;
                Gui.drawRect(l - 1, i2 - 1, l + k + 1, i2 + j - 1, -1873784752);
                this.fontRenderer.drawString(s, l, i2, 14737632);
            }
        }
    }
    
    protected List call() {
        final BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.isReducedDebug()) {
            return Lists.newArrayList("Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF));
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";
        switch (GuiOverlayDebug$1.field_178907_a[enumfacing.ordinal()]) {
            case 1: {
                s = "Towards negative Z";
                break;
            }
            case 2: {
                s = "Towards positive Z";
                break;
            }
            case 3: {
                s = "Towards negative X";
                break;
            }
            case 4: {
                s = "Towards positive X";
                break;
            }
        }
        final ArrayList arraylist = Lists.newArrayList("Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ), String.format("Block: %d %d %d", blockpos.getX(), blockpos.getY(), blockpos.getZ()), String.format("Chunk: %d %d %d in %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF, blockpos.getX() >> 4, blockpos.getY() >> 4, blockpos.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", enumfacing, s, MathHelper.wrapAngleTo180_float(entity.rotationYaw), MathHelper.wrapAngleTo180_float(entity.rotationPitch)));
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(blockpos)) {
            final Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);
            arraylist.add("Biome: " + chunk.getBiome(blockpos, this.mc.theWorld.getWorldChunkManager()).biomeName);
            arraylist.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
            DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);
            if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                final EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(Minecraft.thePlayer.getUniqueID());
                if (entityplayermp != null) {
                    difficultyinstance = entityplayermp.worldObj.getDifficultyForLocation(new BlockPos(entityplayermp));
                }
            }
            arraylist.add(String.format("Local Difficulty: %.2f (Day %d)", difficultyinstance.getAdditionalDifficulty(), this.mc.theWorld.getWorldTime() / 24000L));
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            arraylist.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos2 = this.mc.objectMouseOver.getBlockPos();
            arraylist.add(String.format("Looking at: %d %d %d", blockpos2.getX(), blockpos2.getY(), blockpos2.getZ()));
        }
        return arraylist;
    }
    
    protected List getDebugInfoRight() {
        final long i = Runtime.getRuntime().maxMemory();
        final long j = Runtime.getRuntime().totalMemory();
        final long k = Runtime.getRuntime().freeMemory();
        final long l = j - k;
        final ArrayList arraylist = Lists.newArrayList(String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, bytesToMb(l), bytesToMb(i)), String.format("Allocated: % 2d%% %03dMB", j * 100L / i, bytesToMb(j)), "", String.format("CPU: %s", OpenGlHelper.func_183029_j()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString(7936)), GL11.glGetString(7937), GL11.glGetString(7938));
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            arraylist.add("");
            arraylist.addAll((Collection)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, false));
        }
        if (this.isReducedDebug()) {
            return arraylist;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
            if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                iblockstate = iblockstate.getBlock().getActualState(iblockstate, this.mc.theWorld, blockpos);
            }
            arraylist.add("");
            arraylist.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));
            for (final Map.Entry entry : iblockstate.getProperties().entrySet()) {
                String s = entry.getValue().toString();
                if (entry.getValue() == Boolean.TRUE) {
                    s = EnumChatFormatting.GREEN + s;
                }
                else if (entry.getValue() == Boolean.FALSE) {
                    s = EnumChatFormatting.RED + s;
                }
                arraylist.add(String.valueOf(entry.getKey().getName()) + ": " + s);
            }
        }
        return arraylist;
    }
    
    private void func_181554_e() {
        GlStateManager.disableDepth();
        final FrameTimer frametimer = this.mc.func_181539_aj();
        final int i = frametimer.func_181749_a();
        final int j = frametimer.func_181750_b();
        final long[] along = frametimer.func_181746_c();
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int k = i;
        int l = 0;
        Gui.drawRect(0.0, scaledresolution.getScaledHeight() - 60, 240.0, scaledresolution.getScaledHeight(), -1873784752);
        while (k != j) {
            final int i2 = frametimer.func_181748_a(along[k], 30);
            final int j2 = this.func_181552_c(MathHelper.clamp_int(i2, 0, 60), 0, 30, 60);
            this.drawVerticalLine(l, scaledresolution.getScaledHeight(), scaledresolution.getScaledHeight() - i2, j2);
            ++l;
            k = frametimer.func_181751_b(k + 1);
        }
        Gui.drawRect(1.0, scaledresolution.getScaledHeight() - 30 + 1, 14.0, scaledresolution.getScaledHeight() - 30 + 10, -1873784752);
        this.fontRenderer.drawString("60", 2.0, scaledresolution.getScaledHeight() - 30 + 2, 14737632);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 30, -1);
        Gui.drawRect(1.0, scaledresolution.getScaledHeight() - 60 + 1, 14.0, scaledresolution.getScaledHeight() - 60 + 10, -1873784752);
        this.fontRenderer.drawString("30", 2.0, scaledresolution.getScaledHeight() - 60 + 2, 14737632);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60, -1);
        this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 1, -1);
        this.drawVerticalLine(0, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        this.drawVerticalLine(239, scaledresolution.getScaledHeight() - 60, scaledresolution.getScaledHeight(), -1);
        if (this.mc.gameSettings.limitFramerate <= 120) {
            this.drawHorizontalLine(0, 239, scaledresolution.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
        }
        GlStateManager.enableDepth();
    }
    
    private int func_181552_c(final int p_181552_1_, final int p_181552_2_, final int p_181552_3_, final int p_181552_4_) {
        return (p_181552_1_ < p_181552_3_) ? this.func_181553_a(-16711936, -256, p_181552_1_ / (float)p_181552_3_) : this.func_181553_a(-256, -65536, (p_181552_1_ - p_181552_3_) / (float)(p_181552_4_ - p_181552_3_));
    }
    
    private int func_181553_a(final int p_181553_1_, final int p_181553_2_, final float p_181553_3_) {
        final int i = p_181553_1_ >> 24 & 0xFF;
        final int j = p_181553_1_ >> 16 & 0xFF;
        final int k = p_181553_1_ >> 8 & 0xFF;
        final int l = p_181553_1_ & 0xFF;
        final int i2 = p_181553_2_ >> 24 & 0xFF;
        final int j2 = p_181553_2_ >> 16 & 0xFF;
        final int k2 = p_181553_2_ >> 8 & 0xFF;
        final int l2 = p_181553_2_ & 0xFF;
        final int i3 = MathHelper.clamp_int((int)(i + (i2 - i) * p_181553_3_), 0, 255);
        final int j3 = MathHelper.clamp_int((int)(j + (j2 - j) * p_181553_3_), 0, 255);
        final int k3 = MathHelper.clamp_int((int)(k + (k2 - k) * p_181553_3_), 0, 255);
        final int l3 = MathHelper.clamp_int((int)(l + (l2 - l) * p_181553_3_), 0, 255);
        return i3 << 24 | j3 << 16 | k3 << 8 | l3;
    }
    
    private static long bytesToMb(final long bytes) {
        return bytes / 1024L / 1024L;
    }
    
    static final class GuiOverlayDebug$1
    {
        static final int[] field_178907_a;
        private static final String __OBFID = "CL_00001955";
        
        static {
            field_178907_a = new int[EnumFacing.values().length];
            try {
                GuiOverlayDebug$1.field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                GuiOverlayDebug$1.field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                GuiOverlayDebug$1.field_178907_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                GuiOverlayDebug$1.field_178907_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
