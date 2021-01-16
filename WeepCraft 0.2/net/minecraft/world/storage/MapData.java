package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public boolean trackingPosition;
    public boolean field_191096_f;
    public byte scale;

    /** colours */
    public byte[] colors = new byte[16384];
    public List<MapData.MapInfo> playersArrayList = Lists.<MapData.MapInfo>newArrayList();
    private final Map<EntityPlayer, MapData.MapInfo> playersHashMap = Maps.<EntityPlayer, MapData.MapInfo>newHashMap();
    public Map<String, MapDecoration> mapDecorations = Maps.<String, MapDecoration>newLinkedHashMap();

    public MapData(String mapname)
    {
        super(mapname);
    }

    public void calculateMapCenter(double x, double z, int mapScale)
    {
        int i = 128 * (1 << mapScale);
        int j = MathHelper.floor((x + 64.0D) / (double)i);
        int k = MathHelper.floor((z + 64.0D) / (double)i);
        this.xCenter = j * i + i / 2 - 64;
        this.zCenter = k * i + i / 2 - 64;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.dimension = nbt.getByte("dimension");
        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp(this.scale, 0, 4);

        if (nbt.hasKey("trackingPosition", 1))
        {
            this.trackingPosition = nbt.getBoolean("trackingPosition");
        }
        else
        {
            this.trackingPosition = true;
        }

        this.field_191096_f = nbt.getBoolean("unlimitedTracking");
        int i = nbt.getShort("width");
        int j = nbt.getShort("height");

        if (i == 128 && j == 128)
        {
            this.colors = nbt.getByteArray("colors");
        }
        else
        {
            byte[] abyte = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            int k = (128 - i) / 2;
            int l = (128 - j) / 2;

            for (int i1 = 0; i1 < j; ++i1)
            {
                int j1 = i1 + l;

                if (j1 >= 0 || j1 < 128)
                {
                    for (int k1 = 0; k1 < i; ++k1)
                    {
                        int l1 = k1 + k;

                        if (l1 >= 0 || l1 < 128)
                        {
                            this.colors[l1 + j1 * 128] = abyte[k1 + i1 * i];
                        }
                    }
                }
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setByte("dimension", this.dimension);
        compound.setInteger("xCenter", this.xCenter);
        compound.setInteger("zCenter", this.zCenter);
        compound.setByte("scale", this.scale);
        compound.setShort("width", (short)128);
        compound.setShort("height", (short)128);
        compound.setByteArray("colors", this.colors);
        compound.setBoolean("trackingPosition", this.trackingPosition);
        compound.setBoolean("unlimitedTracking", this.field_191096_f);
        return compound;
    }

    /**
     * Adds the player passed to the list of visible players and checks to see which players are visible
     */
    public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack)
    {
        if (!this.playersHashMap.containsKey(player))
        {
            MapData.MapInfo mapdata$mapinfo = new MapData.MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }

        if (!player.inventory.hasItemStack(mapStack))
        {
            this.mapDecorations.remove(player.getName());
        }

        for (int i = 0; i < this.playersArrayList.size(); ++i)
        {
            MapData.MapInfo mapdata$mapinfo1 = this.playersArrayList.get(i);

            if (!mapdata$mapinfo1.entityplayerObj.isDead && (mapdata$mapinfo1.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame()))
            {
                if (!mapStack.isOnItemFrame() && mapdata$mapinfo1.entityplayerObj.dimension == this.dimension && this.trackingPosition)
                {
                    this.func_191095_a(MapDecoration.Type.PLAYER, mapdata$mapinfo1.entityplayerObj.world, mapdata$mapinfo1.entityplayerObj.getName(), mapdata$mapinfo1.entityplayerObj.posX, mapdata$mapinfo1.entityplayerObj.posZ, (double)mapdata$mapinfo1.entityplayerObj.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(mapdata$mapinfo1.entityplayerObj);
                this.playersArrayList.remove(mapdata$mapinfo1);
            }
        }

        if (mapStack.isOnItemFrame() && this.trackingPosition)
        {
            EntityItemFrame entityitemframe = mapStack.getItemFrame();
            BlockPos blockpos = entityitemframe.getHangingPosition();
            this.func_191095_a(MapDecoration.Type.FRAME, player.world, "frame-" + entityitemframe.getEntityId(), (double)blockpos.getX(), (double)blockpos.getZ(), (double)(entityitemframe.facingDirection.getHorizontalIndex() * 90));
        }

        if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9))
        {
            NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);

            for (int j = 0; j < nbttaglist.tagCount(); ++j)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);

                if (!this.mapDecorations.containsKey(nbttagcompound.getString("id")))
                {
                    this.func_191095_a(MapDecoration.Type.func_191159_a(nbttagcompound.getByte("type")), player.world, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                }
            }
        }
    }

    public static void func_191094_a(ItemStack p_191094_0_, BlockPos p_191094_1_, String p_191094_2_, MapDecoration.Type p_191094_3_)
    {
        NBTTagList nbttaglist;

        if (p_191094_0_.hasTagCompound() && p_191094_0_.getTagCompound().hasKey("Decorations", 9))
        {
            nbttaglist = p_191094_0_.getTagCompound().getTagList("Decorations", 10);
        }
        else
        {
            nbttaglist = new NBTTagList();
            p_191094_0_.setTagInfo("Decorations", nbttaglist);
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("type", p_191094_3_.func_191163_a());
        nbttagcompound.setString("id", p_191094_2_);
        nbttagcompound.setDouble("x", (double)p_191094_1_.getX());
        nbttagcompound.setDouble("z", (double)p_191094_1_.getZ());
        nbttagcompound.setDouble("rot", 180.0D);
        nbttaglist.appendTag(nbttagcompound);

        if (p_191094_3_.func_191162_c())
        {
            NBTTagCompound nbttagcompound1 = p_191094_0_.func_190925_c("display");
            nbttagcompound1.setInteger("MapColor", p_191094_3_.func_191161_d());
        }
    }

    private void func_191095_a(MapDecoration.Type p_191095_1_, World p_191095_2_, String p_191095_3_, double p_191095_4_, double p_191095_6_, double p_191095_8_)
    {
        int i = 1 << this.scale;
        float f = (float)(p_191095_4_ - (double)this.xCenter) / (float)i;
        float f1 = (float)(p_191095_6_ - (double)this.zCenter) / (float)i;
        byte b0 = (byte)((int)((double)(f * 2.0F) + 0.5D));
        byte b1 = (byte)((int)((double)(f1 * 2.0F) + 0.5D));
        int j = 63;
        byte b2;

        if (f >= -63.0F && f1 >= -63.0F && f <= 63.0F && f1 <= 63.0F)
        {
            p_191095_8_ = p_191095_8_ + (p_191095_8_ < 0.0D ? -8.0D : 8.0D);
            b2 = (byte)((int)(p_191095_8_ * 16.0D / 360.0D));

            if (this.dimension < 0)
            {
                int l = (int)(p_191095_2_.getWorldInfo().getWorldTime() / 10L);
                b2 = (byte)(l * l * 34187121 + l * 121 >> 15 & 15);
            }
        }
        else
        {
            if (p_191095_1_ != MapDecoration.Type.PLAYER)
            {
                this.mapDecorations.remove(p_191095_3_);
                return;
            }

            int k = 320;

            if (Math.abs(f) < 320.0F && Math.abs(f1) < 320.0F)
            {
                p_191095_1_ = MapDecoration.Type.PLAYER_OFF_MAP;
            }
            else
            {
                if (!this.field_191096_f)
                {
                    this.mapDecorations.remove(p_191095_3_);
                    return;
                }

                p_191095_1_ = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }

            b2 = 0;

            if (f <= -63.0F)
            {
                b0 = -128;
            }

            if (f1 <= -63.0F)
            {
                b1 = -128;
            }

            if (f >= 63.0F)
            {
                b0 = 127;
            }

            if (f1 >= 63.0F)
            {
                b1 = 127;
            }
        }

        this.mapDecorations.put(p_191095_3_, new MapDecoration(p_191095_1_, b0, b1, b2));
    }

    @Nullable
    public Packet<?> getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player)
    {
        MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        return mapdata$mapinfo == null ? null : mapdata$mapinfo.getPacket(mapStack);
    }

    public void updateMapData(int x, int y)
    {
        super.markDirty();

        for (MapData.MapInfo mapdata$mapinfo : this.playersArrayList)
        {
            mapdata$mapinfo.update(x, y);
        }
    }

    public MapData.MapInfo getMapInfo(EntityPlayer player)
    {
        MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);

        if (mapdata$mapinfo == null)
        {
            mapdata$mapinfo = new MapData.MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }

        return mapdata$mapinfo;
    }

    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        private boolean isDirty = true;
        private int minX;
        private int minY;
        private int maxX = 127;
        private int maxY = 127;
        private int tick;
        public int step;

        public MapInfo(EntityPlayer player)
        {
            this.entityplayerObj = player;
        }

        @Nullable
        public Packet<?> getPacket(ItemStack stack)
        {
            if (this.isDirty)
            {
                this.isDirty = false;
                return new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
            }
            else
            {
                return this.tick++ % 5 == 0 ? new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
            }
        }

        public void update(int x, int y)
        {
            if (this.isDirty)
            {
                this.minX = Math.min(this.minX, x);
                this.minY = Math.min(this.minY, y);
                this.maxX = Math.max(this.maxX, x);
                this.maxY = Math.max(this.maxY, y);
            }
            else
            {
                this.isDirty = true;
                this.minX = x;
                this.minY = y;
                this.maxX = x;
                this.maxY = y;
            }
        }
    }
}
