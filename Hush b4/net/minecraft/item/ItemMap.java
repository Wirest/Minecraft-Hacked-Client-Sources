// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import com.google.common.collect.Multiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import com.google.common.collect.HashMultiset;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;

public class ItemMap extends ItemMapBase
{
    protected ItemMap() {
        this.setHasSubtypes(true);
    }
    
    public static MapData loadMapData(final int mapId, final World worldIn) {
        final String s = "map_" + mapId;
        MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);
        if (mapdata == null) {
            mapdata = new MapData(s);
            worldIn.setItemData(s, mapdata);
        }
        return mapdata;
    }
    
    public MapData getMapData(final ItemStack stack, final World worldIn) {
        String s = "map_" + stack.getMetadata();
        MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);
        if (mapdata == null && !worldIn.isRemote) {
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            s = "map_" + stack.getMetadata();
            mapdata = new MapData(s);
            mapdata.scale = 3;
            mapdata.calculateMapCenter(worldIn.getWorldInfo().getSpawnX(), worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
            mapdata.dimension = (byte)worldIn.provider.getDimensionId();
            mapdata.markDirty();
            worldIn.setItemData(s, mapdata);
        }
        return mapdata;
    }
    
    public void updateMapData(final World worldIn, final Entity viewer, final MapData data) {
        if (worldIn.provider.getDimensionId() == data.dimension && viewer instanceof EntityPlayer) {
            final int i = 1 << data.scale;
            final int j = data.xCenter;
            final int k = data.zCenter;
            final int l = MathHelper.floor_double(viewer.posX - j) / i + 64;
            final int i2 = MathHelper.floor_double(viewer.posZ - k) / i + 64;
            int j2 = 128 / i;
            if (worldIn.provider.getHasNoSky()) {
                j2 /= 2;
            }
            final MapData.MapInfo mapInfo;
            final MapData.MapInfo mapdata$mapinfo = mapInfo = data.getMapInfo((EntityPlayer)viewer);
            ++mapInfo.field_82569_d;
            boolean flag = false;
            for (int k2 = l - j2 + 1; k2 < l + j2; ++k2) {
                if ((k2 & 0xF) == (mapdata$mapinfo.field_82569_d & 0xF) || flag) {
                    flag = false;
                    double d0 = 0.0;
                    for (int l2 = i2 - j2 - 1; l2 < i2 + j2; ++l2) {
                        if (k2 >= 0 && l2 >= -1 && k2 < 128 && l2 < 128) {
                            final int i3 = k2 - l;
                            final int j3 = l2 - i2;
                            final boolean flag2 = i3 * i3 + j3 * j3 > (j2 - 2) * (j2 - 2);
                            final int k3 = (j / i + k2 - 64) * i;
                            final int l3 = (k / i + l2 - 64) * i;
                            final Multiset<MapColor> multiset = (Multiset<MapColor>)HashMultiset.create();
                            final Chunk chunk = worldIn.getChunkFromBlockCoords(new BlockPos(k3, 0, l3));
                            if (!chunk.isEmpty()) {
                                final int i4 = k3 & 0xF;
                                final int j4 = l3 & 0xF;
                                int k4 = 0;
                                double d2 = 0.0;
                                if (worldIn.provider.getHasNoSky()) {
                                    int l4 = k3 + l3 * 231871;
                                    l4 = l4 * l4 * 31287121 + l4 * 11;
                                    if ((l4 >> 20 & 0x1) == 0x0) {
                                        multiset.add(Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT)), 10);
                                    }
                                    else {
                                        multiset.add(Blocks.stone.getMapColor(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)), 100);
                                    }
                                    d2 = 100.0;
                                }
                                else {
                                    final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                                    for (int i5 = 0; i5 < i; ++i5) {
                                        for (int j5 = 0; j5 < i; ++j5) {
                                            int k5 = chunk.getHeightValue(i5 + i4, j5 + j4) + 1;
                                            IBlockState iblockstate = Blocks.air.getDefaultState();
                                            if (k5 > 1) {
                                                do {
                                                    --k5;
                                                    iblockstate = chunk.getBlockState(blockpos$mutableblockpos.func_181079_c(i5 + i4, k5, j5 + j4));
                                                } while (iblockstate.getBlock().getMapColor(iblockstate) == MapColor.airColor && k5 > 0);
                                                if (k5 > 0 && iblockstate.getBlock().getMaterial().isLiquid()) {
                                                    int l5 = k5 - 1;
                                                    Block block;
                                                    do {
                                                        block = chunk.getBlock(i5 + i4, l5--, j5 + j4);
                                                        ++k4;
                                                    } while (l5 > 0 && block.getMaterial().isLiquid());
                                                }
                                            }
                                            d2 += k5 / (double)(i * i);
                                            multiset.add(iblockstate.getBlock().getMapColor(iblockstate));
                                        }
                                    }
                                }
                                k4 /= i * i;
                                double d3 = (d2 - d0) * 4.0 / (i + 4) + ((k2 + l2 & 0x1) - 0.5) * 0.4;
                                int i6 = 1;
                                if (d3 > 0.6) {
                                    i6 = 2;
                                }
                                if (d3 < -0.6) {
                                    i6 = 0;
                                }
                                final MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.airColor);
                                if (mapcolor == MapColor.waterColor) {
                                    d3 = k4 * 0.1 + (k2 + l2 & 0x1) * 0.2;
                                    i6 = 1;
                                    if (d3 < 0.5) {
                                        i6 = 2;
                                    }
                                    if (d3 > 0.9) {
                                        i6 = 0;
                                    }
                                }
                                d0 = d2;
                                if (l2 >= 0 && i3 * i3 + j3 * j3 < j2 * j2 && (!flag2 || (k2 + l2 & 0x1) != 0x0)) {
                                    final byte b0 = data.colors[k2 + l2 * 128];
                                    final byte b2 = (byte)(mapcolor.colorIndex * 4 + i6);
                                    if (b0 != b2) {
                                        data.colors[k2 + l2 * 128] = b2;
                                        data.updateMapData(k2, l2);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onUpdate(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        if (!worldIn.isRemote) {
            final MapData mapdata = this.getMapData(stack, worldIn);
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                mapdata.updateVisiblePlayers(entityplayer, stack);
            }
            if (isSelected) {
                this.updateMapData(worldIn, entityIn, mapdata);
            }
        }
    }
    
    @Override
    public Packet createMapDataPacket(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        return this.getMapData(stack, worldIn).getMapPacket(stack, worldIn, player);
    }
    
    @Override
    public void onCreated(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("map_is_scaling")) {
            final MapData mapdata = Items.filled_map.getMapData(stack, worldIn);
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            final MapData mapdata2 = new MapData("map_" + stack.getMetadata());
            mapdata2.scale = (byte)(mapdata.scale + 1);
            if (mapdata2.scale > 4) {
                mapdata2.scale = 4;
            }
            mapdata2.calculateMapCenter(mapdata.xCenter, mapdata.zCenter, mapdata2.scale);
            mapdata2.dimension = mapdata.dimension;
            mapdata2.markDirty();
            worldIn.setItemData("map_" + stack.getMetadata(), mapdata2);
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        final MapData mapdata = this.getMapData(stack, playerIn.worldObj);
        if (advanced) {
            if (mapdata == null) {
                tooltip.add("Unknown map");
            }
            else {
                tooltip.add("Scaling at 1:" + (1 << mapdata.scale));
                tooltip.add("(Level " + mapdata.scale + "/" + 4 + ")");
            }
        }
    }
}
