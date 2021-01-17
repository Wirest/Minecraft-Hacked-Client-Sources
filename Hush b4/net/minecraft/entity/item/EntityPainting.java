// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityHanging;

public class EntityPainting extends EntityHanging
{
    public EnumArt art;
    
    public EntityPainting(final World worldIn) {
        super(worldIn);
    }
    
    public EntityPainting(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        super(worldIn, pos);
        final List<EnumArt> list = (List<EnumArt>)Lists.newArrayList();
        EnumArt[] values;
        for (int length = (values = EnumArt.values()).length, i = 0; i < length; ++i) {
            final EnumArt entitypainting$enumart = values[i];
            this.art = entitypainting$enumart;
            this.updateFacingWithBoundingBox(facing);
            if (this.onValidSurface()) {
                list.add(entitypainting$enumart);
            }
        }
        if (!list.isEmpty()) {
            this.art = list.get(this.rand.nextInt(list.size()));
        }
        this.updateFacingWithBoundingBox(facing);
    }
    
    public EntityPainting(final World worldIn, final BlockPos pos, final EnumFacing facing, final String title) {
        this(worldIn, pos, facing);
        EnumArt[] values;
        for (int length = (values = EnumArt.values()).length, i = 0; i < length; ++i) {
            final EnumArt entitypainting$enumart = values[i];
            if (entitypainting$enumart.title.equals(title)) {
                this.art = entitypainting$enumart;
                break;
            }
        }
        this.updateFacingWithBoundingBox(facing);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setString("Motive", this.art.title);
        super.writeEntityToNBT(tagCompound);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        final String s = tagCompund.getString("Motive");
        EnumArt[] values;
        for (int length = (values = EnumArt.values()).length, i = 0; i < length; ++i) {
            final EnumArt entitypainting$enumart = values[i];
            if (entitypainting$enumart.title.equals(s)) {
                this.art = entitypainting$enumart;
            }
        }
        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }
        super.readEntityFromNBT(tagCompund);
    }
    
    @Override
    public int getWidthPixels() {
        return this.art.sizeX;
    }
    
    @Override
    public int getHeightPixels() {
        return this.art.sizeY;
    }
    
    @Override
    public void onBroken(final Entity brokenEntity) {
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            if (brokenEntity instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)brokenEntity;
                if (entityplayer.capabilities.isCreativeMode) {
                    return;
                }
            }
            this.entityDropItem(new ItemStack(Items.painting), 0.0f);
        }
    }
    
    @Override
    public void setLocationAndAngles(final double x, final double y, final double z, final float yaw, final float pitch) {
        final BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
        this.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        final BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
        this.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }
    
    public enum EnumArt
    {
        KEBAB("KEBAB", 0, "Kebab", 16, 16, 0, 0), 
        AZTEC("AZTEC", 1, "Aztec", 16, 16, 16, 0), 
        ALBAN("ALBAN", 2, "Alban", 16, 16, 32, 0), 
        AZTEC_2("AZTEC_2", 3, "Aztec2", 16, 16, 48, 0), 
        BOMB("BOMB", 4, "Bomb", 16, 16, 64, 0), 
        PLANT("PLANT", 5, "Plant", 16, 16, 80, 0), 
        WASTELAND("WASTELAND", 6, "Wasteland", 16, 16, 96, 0), 
        POOL("POOL", 7, "Pool", 32, 16, 0, 32), 
        COURBET("COURBET", 8, "Courbet", 32, 16, 32, 32), 
        SEA("SEA", 9, "Sea", 32, 16, 64, 32), 
        SUNSET("SUNSET", 10, "Sunset", 32, 16, 96, 32), 
        CREEBET("CREEBET", 11, "Creebet", 32, 16, 128, 32), 
        WANDERER("WANDERER", 12, "Wanderer", 16, 32, 0, 64), 
        GRAHAM("GRAHAM", 13, "Graham", 16, 32, 16, 64), 
        MATCH("MATCH", 14, "Match", 32, 32, 0, 128), 
        BUST("BUST", 15, "Bust", 32, 32, 32, 128), 
        STAGE("STAGE", 16, "Stage", 32, 32, 64, 128), 
        VOID("VOID", 17, "Void", 32, 32, 96, 128), 
        SKULL_AND_ROSES("SKULL_AND_ROSES", 18, "SkullAndRoses", 32, 32, 128, 128), 
        WITHER("WITHER", 19, "Wither", 32, 32, 160, 128), 
        FIGHTERS("FIGHTERS", 20, "Fighters", 64, 32, 0, 96), 
        POINTER("POINTER", 21, "Pointer", 64, 64, 0, 192), 
        PIGSCENE("PIGSCENE", 22, "Pigscene", 64, 64, 64, 192), 
        BURNING_SKULL("BURNING_SKULL", 23, "BurningSkull", 64, 64, 128, 192), 
        SKELETON("SKELETON", 24, "Skeleton", 64, 48, 192, 64), 
        DONKEY_KONG("DONKEY_KONG", 25, "DonkeyKong", 64, 48, 192, 112);
        
        public static final int field_180001_A;
        public final String title;
        public final int sizeX;
        public final int sizeY;
        public final int offsetX;
        public final int offsetY;
        
        static {
            field_180001_A = "SkullAndRoses".length();
        }
        
        private EnumArt(final String name, final int ordinal, final String titleIn, final int width, final int height, final int textureU, final int textureV) {
            this.title = titleIn;
            this.sizeX = width;
            this.sizeY = height;
            this.offsetX = textureU;
            this.offsetY = textureV;
        }
    }
}
