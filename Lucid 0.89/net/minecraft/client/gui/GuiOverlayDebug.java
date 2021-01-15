package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.Lagometer;
import net.minecraft.src.Reflector;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;

public class GuiOverlayDebug extends Gui
{
    private final Minecraft mc;
    private final FontRenderer fontRenderer;

    public GuiOverlayDebug(Minecraft mc)
    {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }

    public void renderDebugInfo(ScaledResolution scaledResolutionIn)
    {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        Lagometer.showLagometer(scaledResolutionIn);
        this.mc.mcProfiler.endSection();
    }

    private boolean isReducedDebug()
    {
        return this.mc.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo;
    }

    protected void renderDebugInfoLeft()
    {
        List var1 = this.call();

        for (int var2 = 0; var2 < var1.size(); ++var2)
        {
            String var3 = (String)var1.get(var2);

            if (!Strings.isNullOrEmpty(var3))
            {
                int var4 = this.fontRenderer.FONT_HEIGHT;
                int var5 = this.fontRenderer.getStringWidth(var3);
                int var7 = 2 + var4 * var2;
                drawRect(1, var7 - 1, 2 + var5 + 1, var7 + var4 - 1, -1873784752);
                this.fontRenderer.drawString(var3, 2, var7, 14737632);
            }
        }
    }

    protected void renderDebugInfoRight(ScaledResolution p_175239_1_)
    {
        List var2 = this.getDebugInfoRight();

        for (int var3 = 0; var3 < var2.size(); ++var3)
        {
            String var4 = (String)var2.get(var3);

            if (!Strings.isNullOrEmpty(var4))
            {
                int var5 = this.fontRenderer.FONT_HEIGHT;
                int var6 = this.fontRenderer.getStringWidth(var4);
                int var7 = p_175239_1_.getScaledWidth() - 2 - var6;
                int var8 = 2 + var5 * var3;
                drawRect(var7 - 1, var8 - 1, var7 + var6 + 1, var8 + var5 - 1, -1873784752);
                this.fontRenderer.drawString(var4, var7, var8, 14737632);
            }
        }
    }

    protected List call()
    {
        BlockPos var1 = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);

        if (this.isReducedDebug())
        {
            return Lists.newArrayList(new String[] {"Minecraft 1.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", new Object[]{Integer.valueOf(var1.getX() & 15), Integer.valueOf(var1.getY() & 15), Integer.valueOf(var1.getZ() & 15)})});
        }
        else
        {
            Entity var2 = this.mc.getRenderViewEntity();
            EnumFacing var3 = var2.getHorizontalFacing();
            String var4 = "Invalid";

            switch (GuiOverlayDebug.SwitchEnumFacing.field_178907_a[var3.ordinal()])
            {
                case 1:
                    var4 = "Towards negative Z";
                    break;

                case 2:
                    var4 = "Towards positive Z";
                    break;

                case 3:
                    var4 = "Towards negative X";
                    break;

                case 4:
                    var4 = "Towards positive X";
            }

            ArrayList var5 = Lists.newArrayList(new String[] {"Minecraft 1.8 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", new Object[]{Double.valueOf(this.mc.getRenderViewEntity().posX), Double.valueOf(this.mc.getRenderViewEntity().getEntityBoundingBox().minY), Double.valueOf(this.mc.getRenderViewEntity().posZ)}), String.format("Block: %d %d %d", new Object[]{Integer.valueOf(var1.getX()), Integer.valueOf(var1.getY()), Integer.valueOf(var1.getZ())}), String.format("Chunk: %d %d %d in %d %d %d", new Object[]{Integer.valueOf(var1.getX() & 15), Integer.valueOf(var1.getY() & 15), Integer.valueOf(var1.getZ() & 15), Integer.valueOf(var1.getX() >> 4), Integer.valueOf(var1.getY() >> 4), Integer.valueOf(var1.getZ() >> 4)}), String.format("Facing: %s (%s) (%.1f / %.1f)", new Object[]{var3, var4, Float.valueOf(MathHelper.wrapAngleTo180_float(var2.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(var2.rotationPitch))})});

            if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1))
            {
                Chunk var9 = this.mc.theWorld.getChunkFromBlockCoords(var1);
                var5.add("Biome: " + var9.getBiome(var1, this.mc.theWorld.getWorldChunkManager()).biomeName);
                var5.add("Light: " + var9.getLightSubtracted(var1, 0) + " (" + var9.getLightFor(EnumSkyBlock.SKY, var1) + " sky, " + var9.getLightFor(EnumSkyBlock.BLOCK, var1) + " block)");
                DifficultyInstance var7 = this.mc.theWorld.getDifficultyForLocation(var1);

                if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null)
                {
                    EntityPlayerMP var8 = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(this.mc.thePlayer.getUniqueID());

                    if (var8 != null)
                    {
                        var7 = var8.worldObj.getDifficultyForLocation(new BlockPos(var8));
                    }
                }

                var5.add(String.format("Local Difficulty: %.2f (Day %d)", new Object[] {Float.valueOf(var7.getAdditionalDifficulty()), Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L)}));
            }

            if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive())
            {
                var5.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
            }

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null)
            {
                BlockPos var91 = this.mc.objectMouseOver.getBlockPos();
                var5.add(String.format("Looking at: %d %d %d", new Object[] {Integer.valueOf(var91.getX()), Integer.valueOf(var91.getY()), Integer.valueOf(var91.getZ())}));
            }

            return var5;
        }
    }

    protected List getDebugInfoRight()
    {
        long var1 = Runtime.getRuntime().maxMemory();
        long var3 = Runtime.getRuntime().totalMemory();
        long var5 = Runtime.getRuntime().freeMemory();
        long var7 = var3 - var5;
        ArrayList var9 = Lists.newArrayList(new String[] {String.format("Java: %s %dbit", new Object[]{System.getProperty("java.version"), Integer.valueOf(this.mc.isJava64bit() ? 64 : 32)}), String.format("Mem: % 2d%% %03d/%03dMB", new Object[]{Long.valueOf(var7 * 100L / var1), Long.valueOf(bytesToMb(var7)), Long.valueOf(bytesToMb(var1))}), String.format("Allocated: % 2d%% %03dMB", new Object[]{Long.valueOf(var3 * 100L / var1), Long.valueOf(bytesToMb(var3))}), "", String.format("Display: %dx%d (%s)", new Object[]{Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GL11.glGetString(GL11.GL_VENDOR)}), GL11.glGetString(GL11.GL_RENDERER), GL11.glGetString(GL11.GL_VERSION)});

        if (Reflector.FMLCommonHandler_getBrandings.exists())
        {
            Object var10 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            var9.add("");
            var9.addAll((Collection)Reflector.call(var10, Reflector.FMLCommonHandler_getBrandings, new Object[] {Boolean.valueOf(false)}));
        }

        if (this.isReducedDebug())
        {
            return var9;
        }
        else
        {
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null)
            {
                BlockPos var101 = this.mc.objectMouseOver.getBlockPos();
                IBlockState var11 = this.mc.theWorld.getBlockState(var101);

                if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD)
                {
                    var11 = var11.getBlock().getActualState(var11, this.mc.theWorld, var101);
                }

                var9.add("");
                var9.add(String.valueOf(Block.blockRegistry.getNameForObject(var11.getBlock())));
                Entry var13;
                String var14;

                for (UnmodifiableIterator var12 = var11.getProperties().entrySet().iterator(); var12.hasNext(); var9.add(((IProperty)var13.getKey()).getName() + ": " + var14))
                {
                    var13 = (Entry)var12.next();
                    var14 = ((Comparable)var13.getValue()).toString();

                    if (var13.getValue() == Boolean.TRUE)
                    {
                        var14 = EnumChatFormatting.GREEN + var14;
                    }
                    else if (var13.getValue() == Boolean.FALSE)
                    {
                        var14 = EnumChatFormatting.RED + var14;
                    }
                }
            }

            return var9;
        }
    }

    private static long bytesToMb(long bytes)
    {
        return bytes / 1024L / 1024L;
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_178907_a = new int[EnumFacing.values().length];

        static
        {
            try
            {
                field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_178907_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_178907_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
