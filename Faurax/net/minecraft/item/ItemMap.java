package net.minecraft.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

public class ItemMap extends ItemMapBase
{
    private static final String __OBFID = "CL_00000047";

    protected ItemMap()
    {
        this.setHasSubtypes(true);
    }

    public static MapData loadMapData(int p_150912_0_, World worldIn)
    {
        String var2 = "map_" + p_150912_0_;
        MapData var3 = (MapData)worldIn.loadItemData(MapData.class, var2);

        if (var3 == null)
        {
            var3 = new MapData(var2);
            worldIn.setItemData(var2, var3);
        }

        return var3;
    }

    public MapData getMapData(ItemStack p_77873_1_, World worldIn)
    {
        String var3 = "map_" + p_77873_1_.getMetadata();
        MapData var4 = (MapData)worldIn.loadItemData(MapData.class, var3);

        if (var4 == null && !worldIn.isRemote)
        {
            p_77873_1_.setItemDamage(worldIn.getUniqueDataId("map"));
            var3 = "map_" + p_77873_1_.getMetadata();
            var4 = new MapData(var3);
            var4.scale = 3;
            var4.func_176054_a((double)worldIn.getWorldInfo().getSpawnX(), (double)worldIn.getWorldInfo().getSpawnZ(), var4.scale);
            var4.dimension = (byte)worldIn.provider.getDimensionId();
            var4.markDirty();
            worldIn.setItemData(var3, var4);
        }

        return var4;
    }

    public void updateMapData(World worldIn, Entity p_77872_2_, MapData p_77872_3_)
    {
        if (worldIn.provider.getDimensionId() == p_77872_3_.dimension && p_77872_2_ instanceof EntityPlayer)
        {
            int var4 = 1 << p_77872_3_.scale;
            int var5 = p_77872_3_.xCenter;
            int var6 = p_77872_3_.zCenter;
            int var7 = MathHelper.floor_double(p_77872_2_.posX - (double)var5) / var4 + 64;
            int var8 = MathHelper.floor_double(p_77872_2_.posZ - (double)var6) / var4 + 64;
            int var9 = 128 / var4;

            if (worldIn.provider.getHasNoSky())
            {
                var9 /= 2;
            }

            MapData.MapInfo var10 = p_77872_3_.func_82568_a((EntityPlayer)p_77872_2_);
            ++var10.field_82569_d;
            boolean var11 = false;

            for (int var12 = var7 - var9 + 1; var12 < var7 + var9; ++var12)
            {
                if ((var12 & 15) == (var10.field_82569_d & 15) || var11)
                {
                    var11 = false;
                    double var13 = 0.0D;

                    for (int var15 = var8 - var9 - 1; var15 < var8 + var9; ++var15)
                    {
                        if (var12 >= 0 && var15 >= -1 && var12 < 128 && var15 < 128)
                        {
                            int var16 = var12 - var7;
                            int var17 = var15 - var8;
                            boolean var18 = var16 * var16 + var17 * var17 > (var9 - 2) * (var9 - 2);
                            int var19 = (var5 / var4 + var12 - 64) * var4;
                            int var20 = (var6 / var4 + var15 - 64) * var4;
                            HashMultiset var21 = HashMultiset.create();
                            Chunk var22 = worldIn.getChunkFromBlockCoords(new BlockPos(var19, 0, var20));

                            if (!var22.isEmpty())
                            {
                                int var23 = var19 & 15;
                                int var24 = var20 & 15;
                                int var25 = 0;
                                double var26 = 0.0D;
                                int var28;

                                if (worldIn.provider.getHasNoSky())
                                {
                                    var28 = var19 + var20 * 231871;
                                    var28 = var28 * var28 * 31287121 + var28 * 11;

                                    if ((var28 >> 20 & 1) == 0)
                                    {
                                        var21.add(Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
                                    }
                                    else
                                    {
                                        var21.add(Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT_PROP, BlockStone.EnumType.STONE)), 100);
                                    }

                                    var26 = 100.0D;
                                }
                                else
                                {
                                    for (var28 = 0; var28 < var4; ++var28)
                                    {
                                        for (int var29 = 0; var29 < var4; ++var29)
                                        {
                                            int var30 = var22.getHeight(var28 + var23, var29 + var24) + 1;
                                            IBlockState var31 = Blocks.air.getDefaultState();

                                            if (var30 > 1)
                                            {
                                                do
                                                {
                                                    --var30;
                                                    var31 = var22.getBlockState(new BlockPos(var28 + var23, var30, var29 + var24));
                                                }
                                                while (var31.getBlock().getMapColor(var31) == MapColor.airColor && var30 > 0);

                                                if (var30 > 0 && var31.getBlock().getMaterial().isLiquid())
                                                {
                                                    int var32 = var30 - 1;
                                                    Block var33;

                                                    do
                                                    {
                                                        var33 = var22.getBlock(var28 + var23, var32--, var29 + var24);
                                                        ++var25;
                                                    }
                                                    while (var32 > 0 && var33.getMaterial().isLiquid());
                                                }
                                            }

                                            var26 += (double)var30 / (double)(var4 * var4);
                                            var21.add(var31.getBlock().getMapColor(var31));
                                        }
                                    }
                                }

                                var25 /= var4 * var4;
                                double var34 = (var26 - var13) * 4.0D / (double)(var4 + 4) + ((double)(var12 + var15 & 1) - 0.5D) * 0.4D;
                                byte var35 = 1;

                                if (var34 > 0.6D)
                                {
                                    var35 = 2;
                                }

                                if (var34 < -0.6D)
                                {
                                    var35 = 0;
                                }

                                MapColor var36 = (MapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(var21), MapColor.airColor);

                                if (var36 == MapColor.waterColor)
                                {
                                    var34 = (double)var25 * 0.1D + (double)(var12 + var15 & 1) * 0.2D;
                                    var35 = 1;

                                    if (var34 < 0.5D)
                                    {
                                        var35 = 2;
                                    }

                                    if (var34 > 0.9D)
                                    {
                                        var35 = 0;
                                    }
                                }

                                var13 = var26;

                                if (var15 >= 0 && var16 * var16 + var17 * var17 < var9 * var9 && (!var18 || (var12 + var15 & 1) != 0))
                                {
                                    byte var37 = p_77872_3_.colors[var12 + var15 * 128];
                                    byte var38 = (byte)(var36.colorIndex * 4 + var35);

                                    if (var37 != var38)
                                    {
                                        p_77872_3_.colors[var12 + var15 * 128] = var38;
                                        p_77872_3_.func_176053_a(var12, var15);
                                        var11 = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            MapData var6 = this.getMapData(stack, worldIn);

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer var7 = (EntityPlayer)entityIn;
                var6.updateVisiblePlayers(var7, stack);
            }

            if (isSelected)
            {
                this.updateMapData(worldIn, entityIn, var6);
            }
        }
    }

    public Packet createMapDataPacket(ItemStack p_150911_1_, World worldIn, EntityPlayer p_150911_3_)
    {
        return this.getMapData(p_150911_1_, worldIn).func_176052_a(p_150911_1_, worldIn, p_150911_3_);
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("map_is_scaling"))
        {
            MapData var4 = Items.filled_map.getMapData(stack, worldIn);
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            MapData var5 = new MapData("map_" + stack.getMetadata());
            var5.scale = (byte)(var4.scale + 1);

            if (var5.scale > 4)
            {
                var5.scale = 4;
            }

            var5.func_176054_a((double)var4.xCenter, (double)var4.zCenter, var5.scale);
            var5.dimension = var4.dimension;
            var5.markDirty();
            worldIn.setItemData("map_" + stack.getMetadata(), var5);
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        MapData var5 = this.getMapData(stack, playerIn.worldObj);

        if (advanced)
        {
            if (var5 == null)
            {
                tooltip.add("Unknown map");
            }
            else
            {
                tooltip.add("Scaling at 1:" + (1 << var5.scale));
                tooltip.add("(Level " + var5.scale + "/" + 4 + ")");
            }
        }
    }
}
