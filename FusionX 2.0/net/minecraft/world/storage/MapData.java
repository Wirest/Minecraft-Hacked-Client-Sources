package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec4b;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;

    /** colours */
    public byte[] colors = new byte[16384];

    /**
     * Holds a reference to the MapInfo of the players who own a copy of the map
     */
    public List playersArrayList = Lists.newArrayList();

    /**
     * Holds a reference to the players who own a copy of the map and a reference to their MapInfo
     */
    private Map playersHashMap = Maps.newHashMap();
    public Map playersVisibleOnMap = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00000577";

    public MapData(String p_i2140_1_)
    {
        super(p_i2140_1_);
    }

    public void func_176054_a(double p_176054_1_, double p_176054_3_, int p_176054_5_)
    {
        int var6 = 128 * (1 << p_176054_5_);
        int var7 = MathHelper.floor_double((p_176054_1_ + 64.0D) / (double)var6);
        int var8 = MathHelper.floor_double((p_176054_3_ + 64.0D) / (double)var6);
        this.xCenter = var7 * var6 + var6 / 2 - 64;
        this.zCenter = var8 * var6 + var6 / 2 - 64;
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
        this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
        short var2 = nbt.getShort("width");
        short var3 = nbt.getShort("height");

        if (var2 == 128 && var3 == 128)
        {
            this.colors = nbt.getByteArray("colors");
        }
        else
        {
            byte[] var4 = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - var2) / 2;
            int var6 = (128 - var3) / 2;

            for (int var7 = 0; var7 < var3; ++var7)
            {
                int var8 = var7 + var6;

                if (var8 >= 0 || var8 < 128)
                {
                    for (int var9 = 0; var9 < var2; ++var9)
                    {
                        int var10 = var9 + var5;

                        if (var10 >= 0 || var10 < 128)
                        {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("dimension", this.dimension);
        nbt.setInteger("xCenter", this.xCenter);
        nbt.setInteger("zCenter", this.zCenter);
        nbt.setByte("scale", this.scale);
        nbt.setShort("width", (short)128);
        nbt.setShort("height", (short)128);
        nbt.setByteArray("colors", this.colors);
    }

    /**
     * Adds the player passed to the list of visible players and checks to see which players are visible
     */
    public void updateVisiblePlayers(EntityPlayer p_76191_1_, ItemStack p_76191_2_)
    {
        if (!this.playersHashMap.containsKey(p_76191_1_))
        {
            MapData.MapInfo var3 = new MapData.MapInfo(p_76191_1_);
            this.playersHashMap.put(p_76191_1_, var3);
            this.playersArrayList.add(var3);
        }

        if (!p_76191_1_.inventory.hasItemStack(p_76191_2_))
        {
            this.playersVisibleOnMap.remove(p_76191_1_.getName());
        }

        for (int var6 = 0; var6 < this.playersArrayList.size(); ++var6)
        {
            MapData.MapInfo var4 = (MapData.MapInfo)this.playersArrayList.get(var6);

            if (!var4.entityplayerObj.isDead && (var4.entityplayerObj.inventory.hasItemStack(p_76191_2_) || p_76191_2_.isOnItemFrame()))
            {
                if (!p_76191_2_.isOnItemFrame() && var4.entityplayerObj.dimension == this.dimension)
                {
                    this.func_82567_a(0, var4.entityplayerObj.worldObj, var4.entityplayerObj.getName(), var4.entityplayerObj.posX, var4.entityplayerObj.posZ, (double)var4.entityplayerObj.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(var4.entityplayerObj);
                this.playersArrayList.remove(var4);
            }
        }

        if (p_76191_2_.isOnItemFrame())
        {
            EntityItemFrame var7 = p_76191_2_.getItemFrame();
            BlockPos var9 = var7.func_174857_n();
            this.func_82567_a(1, p_76191_1_.worldObj, "frame-" + var7.getEntityId(), (double)var9.getX(), (double)var9.getZ(), (double)(var7.field_174860_b.getHorizontalIndex() * 90));
        }

        if (p_76191_2_.hasTagCompound() && p_76191_2_.getTagCompound().hasKey("Decorations", 9))
        {
            NBTTagList var8 = p_76191_2_.getTagCompound().getTagList("Decorations", 10);

            for (int var10 = 0; var10 < var8.tagCount(); ++var10)
            {
                NBTTagCompound var5 = var8.getCompoundTagAt(var10);

                if (!this.playersVisibleOnMap.containsKey(var5.getString("id")))
                {
                    this.func_82567_a(var5.getByte("type"), p_76191_1_.worldObj, var5.getString("id"), var5.getDouble("x"), var5.getDouble("z"), var5.getDouble("rot"));
                }
            }
        }
    }

    private void func_82567_a(int p_82567_1_, World worldIn, String p_82567_3_, double p_82567_4_, double p_82567_6_, double p_82567_8_)
    {
        int var10 = 1 << this.scale;
        float var11 = (float)(p_82567_4_ - (double)this.xCenter) / (float)var10;
        float var12 = (float)(p_82567_6_ - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5D));
        byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5D));
        byte var16 = 63;
        byte var15;

        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16)
        {
            p_82567_8_ += p_82567_8_ < 0.0D ? -8.0D : 8.0D;
            var15 = (byte)((int)(p_82567_8_ * 16.0D / 360.0D));

            if (this.dimension < 0)
            {
                int var17 = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
            }
        }
        else
        {
            if (Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F)
            {
                this.playersVisibleOnMap.remove(p_82567_3_);
                return;
            }

            p_82567_1_ = 6;
            var15 = 0;

            if (var11 <= (float)(-var16))
            {
                var13 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var12 <= (float)(-var16))
            {
                var14 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var11 >= (float)var16)
            {
                var13 = (byte)(var16 * 2 + 1);
            }

            if (var12 >= (float)var16)
            {
                var14 = (byte)(var16 * 2 + 1);
            }
        }

        this.playersVisibleOnMap.put(p_82567_3_, new Vec4b((byte)p_82567_1_, var13, var14, var15));
    }

    public Packet func_176052_a(ItemStack p_176052_1_, World worldIn, EntityPlayer p_176052_3_)
    {
        MapData.MapInfo var4 = (MapData.MapInfo)this.playersHashMap.get(p_176052_3_);
        return var4 == null ? null : var4.func_176101_a(p_176052_1_);
    }

    public void func_176053_a(int p_176053_1_, int p_176053_2_)
    {
        super.markDirty();
        Iterator var3 = this.playersArrayList.iterator();

        while (var3.hasNext())
        {
            MapData.MapInfo var4 = (MapData.MapInfo)var3.next();
            var4.func_176102_a(p_176053_1_, p_176053_2_);
        }
    }

    public MapData.MapInfo func_82568_a(EntityPlayer p_82568_1_)
    {
        MapData.MapInfo var2 = (MapData.MapInfo)this.playersHashMap.get(p_82568_1_);

        if (var2 == null)
        {
            var2 = new MapData.MapInfo(p_82568_1_);
            this.playersHashMap.put(p_82568_1_, var2);
            this.playersArrayList.add(var2);
        }

        return var2;
    }

    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        private boolean field_176105_d = true;
        private int field_176106_e = 0;
        private int field_176103_f = 0;
        private int field_176104_g = 127;
        private int field_176108_h = 127;
        private int field_176109_i;
        public int field_82569_d;
        private static final String __OBFID = "CL_00000578";

        public MapInfo(EntityPlayer p_i2138_2_)
        {
            this.entityplayerObj = p_i2138_2_;
        }

        public Packet func_176101_a(ItemStack p_176101_1_)
        {
            if (this.field_176105_d)
            {
                this.field_176105_d = false;
                return new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, this.field_176106_e, this.field_176103_f, this.field_176104_g + 1 - this.field_176106_e, this.field_176108_h + 1 - this.field_176103_f);
            }
            else
            {
                return this.field_176109_i++ % 5 == 0 ? new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, 0, 0, 0, 0) : null;
            }
        }

        public void func_176102_a(int p_176102_1_, int p_176102_2_)
        {
            if (this.field_176105_d)
            {
                this.field_176106_e = Math.min(this.field_176106_e, p_176102_1_);
                this.field_176103_f = Math.min(this.field_176103_f, p_176102_2_);
                this.field_176104_g = Math.max(this.field_176104_g, p_176102_1_);
                this.field_176108_h = Math.max(this.field_176108_h, p_176102_2_);
            }
            else
            {
                this.field_176105_d = true;
                this.field_176106_e = p_176102_1_;
                this.field_176103_f = p_176102_2_;
                this.field_176104_g = p_176102_1_;
                this.field_176108_h = p_176102_2_;
            }
        }
    }
}
