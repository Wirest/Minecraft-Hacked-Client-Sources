// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.network.play.server.S34PacketMaps;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.util.Vec4b;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import java.util.List;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;
    public byte[] colors;
    public List<MapInfo> playersArrayList;
    private Map<EntityPlayer, MapInfo> playersHashMap;
    public Map<String, Vec4b> mapDecorations;
    
    public MapData(final String mapname) {
        super(mapname);
        this.colors = new byte[16384];
        this.playersArrayList = (List<MapInfo>)Lists.newArrayList();
        this.playersHashMap = (Map<EntityPlayer, MapInfo>)Maps.newHashMap();
        this.mapDecorations = (Map<String, Vec4b>)Maps.newLinkedHashMap();
    }
    
    public void calculateMapCenter(final double x, final double z, final int mapScale) {
        final int i = 128 * (1 << mapScale);
        final int j = MathHelper.floor_double((x + 64.0) / i);
        final int k = MathHelper.floor_double((z + 64.0) / i);
        this.xCenter = j * i + i / 2 - 64;
        this.zCenter = k * i + i / 2 - 64;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.dimension = nbt.getByte("dimension");
        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
        final int i = nbt.getShort("width");
        final int j = nbt.getShort("height");
        if (i == 128 && j == 128) {
            this.colors = nbt.getByteArray("colors");
        }
        else {
            final byte[] abyte = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            final int k = (128 - i) / 2;
            final int l = (128 - j) / 2;
            for (int i2 = 0; i2 < j; ++i2) {
                final int j2 = i2 + l;
                if (j2 >= 0 || j2 < 128) {
                    for (int k2 = 0; k2 < i; ++k2) {
                        final int l2 = k2 + k;
                        if (l2 >= 0 || l2 < 128) {
                            this.colors[l2 + j2 * 128] = abyte[k2 + i2 * i];
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setByte("dimension", this.dimension);
        nbt.setInteger("xCenter", this.xCenter);
        nbt.setInteger("zCenter", this.zCenter);
        nbt.setByte("scale", this.scale);
        nbt.setShort("width", (short)128);
        nbt.setShort("height", (short)128);
        nbt.setByteArray("colors", this.colors);
    }
    
    public void updateVisiblePlayers(final EntityPlayer player, final ItemStack mapStack) {
        if (!this.playersHashMap.containsKey(player)) {
            final MapInfo mapdata$mapinfo = new MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }
        if (!player.inventory.hasItemStack(mapStack)) {
            this.mapDecorations.remove(player.getName());
        }
        for (int i = 0; i < this.playersArrayList.size(); ++i) {
            final MapInfo mapdata$mapinfo2 = this.playersArrayList.get(i);
            if (!mapdata$mapinfo2.entityplayerObj.isDead && (mapdata$mapinfo2.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame())) {
                if (!mapStack.isOnItemFrame() && mapdata$mapinfo2.entityplayerObj.dimension == this.dimension) {
                    this.updateDecorations(0, mapdata$mapinfo2.entityplayerObj.worldObj, mapdata$mapinfo2.entityplayerObj.getName(), mapdata$mapinfo2.entityplayerObj.posX, mapdata$mapinfo2.entityplayerObj.posZ, mapdata$mapinfo2.entityplayerObj.rotationYaw);
                }
            }
            else {
                this.playersHashMap.remove(mapdata$mapinfo2.entityplayerObj);
                this.playersArrayList.remove(mapdata$mapinfo2);
            }
        }
        if (mapStack.isOnItemFrame()) {
            final EntityItemFrame entityitemframe = mapStack.getItemFrame();
            final BlockPos blockpos = entityitemframe.getHangingPosition();
            this.updateDecorations(1, player.worldObj, "frame-" + entityitemframe.getEntityId(), blockpos.getX(), blockpos.getZ(), entityitemframe.facingDirection.getHorizontalIndex() * 90);
        }
        if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9)) {
            final NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                if (!this.mapDecorations.containsKey(nbttagcompound.getString("id"))) {
                    this.updateDecorations(nbttagcompound.getByte("type"), player.worldObj, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                }
            }
        }
    }
    
    private void updateDecorations(int type, final World worldIn, final String entityIdentifier, final double worldX, final double worldZ, double rotation) {
        final int i = 1 << this.scale;
        final float f = (float)(worldX - this.xCenter) / i;
        final float f2 = (float)(worldZ - this.zCenter) / i;
        byte b0 = (byte)(f * 2.0f + 0.5);
        byte b2 = (byte)(f2 * 2.0f + 0.5);
        final int j = 63;
        byte b3;
        if (f >= -j && f2 >= -j && f <= j && f2 <= j) {
            rotation += ((rotation < 0.0) ? -8.0 : 8.0);
            b3 = (byte)(rotation * 16.0 / 360.0);
            if (this.dimension < 0) {
                final int k = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
                b3 = (byte)(k * k * 34187121 + k * 121 >> 15 & 0xF);
            }
        }
        else {
            if (Math.abs(f) >= 320.0f || Math.abs(f2) >= 320.0f) {
                this.mapDecorations.remove(entityIdentifier);
                return;
            }
            type = 6;
            b3 = 0;
            if (f <= -j) {
                b0 = (byte)(j * 2 + 2.5);
            }
            if (f2 <= -j) {
                b2 = (byte)(j * 2 + 2.5);
            }
            if (f >= j) {
                b0 = (byte)(j * 2 + 1);
            }
            if (f2 >= j) {
                b2 = (byte)(j * 2 + 1);
            }
        }
        this.mapDecorations.put(entityIdentifier, new Vec4b((byte)type, b0, b2, b3));
    }
    
    public Packet getMapPacket(final ItemStack mapStack, final World worldIn, final EntityPlayer player) {
        final MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        return (mapdata$mapinfo == null) ? null : mapdata$mapinfo.getPacket(mapStack);
    }
    
    public void updateMapData(final int x, final int y) {
        super.markDirty();
        for (final MapInfo mapdata$mapinfo : this.playersArrayList) {
            mapdata$mapinfo.update(x, y);
        }
    }
    
    public MapInfo getMapInfo(final EntityPlayer player) {
        MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        if (mapdata$mapinfo == null) {
            mapdata$mapinfo = new MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }
        return mapdata$mapinfo;
    }
    
    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        private boolean field_176105_d;
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;
        private int field_176109_i;
        public int field_82569_d;
        
        public MapInfo(final EntityPlayer player) {
            this.field_176105_d = true;
            this.minX = 0;
            this.minY = 0;
            this.maxX = 127;
            this.maxY = 127;
            this.entityplayerObj = player;
        }
        
        public Packet getPacket(final ItemStack stack) {
            if (this.field_176105_d) {
                this.field_176105_d = false;
                return new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
            }
            return (this.field_176109_i++ % 5 == 0) ? new S34PacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
        }
        
        public void update(final int x, final int y) {
            if (this.field_176105_d) {
                this.minX = Math.min(this.minX, x);
                this.minY = Math.min(this.minY, y);
                this.maxX = Math.max(this.maxX, x);
                this.maxY = Math.max(this.maxY, y);
            }
            else {
                this.field_176105_d = true;
                this.minX = x;
                this.minY = y;
                this.maxX = x;
                this.maxY = y;
            }
        }
    }
}
