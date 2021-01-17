// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.util.DamageSource;
import com.google.common.collect.Lists;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockFalling;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public class EntityFallingBlock extends Entity
{
    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem;
    private boolean canSetAsBlock;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;
    
    public EntityFallingBlock(final World worldIn) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
    }
    
    public EntityFallingBlock(final World worldIn, final double x, final double y, final double z, final IBlockState fallingBlockState) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
        this.fallTile = fallingBlockState;
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate() {
        final Block block = this.fallTile.getBlock();
        if (block.getMaterial() == Material.air) {
            this.setDead();
        }
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.fallTime++ == 0) {
                final BlockPos blockpos = new BlockPos(this);
                if (this.worldObj.getBlockState(blockpos).getBlock() == block) {
                    this.worldObj.setBlockToAir(blockpos);
                }
                else if (!this.worldObj.isRemote) {
                    this.setDead();
                    return;
                }
            }
            this.motionY -= 0.03999999910593033;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= 0.9800000190734863;
            if (!this.worldObj.isRemote) {
                final BlockPos blockpos2 = new BlockPos(this);
                if (this.onGround) {
                    this.motionX *= 0.699999988079071;
                    this.motionZ *= 0.699999988079071;
                    this.motionY *= -0.5;
                    if (this.worldObj.getBlockState(blockpos2).getBlock() != Blocks.piston_extension) {
                        this.setDead();
                        if (!this.canSetAsBlock) {
                            if (this.worldObj.canBlockBePlaced(block, blockpos2, true, EnumFacing.UP, null, null) && !BlockFalling.canFallInto(this.worldObj, blockpos2.down()) && this.worldObj.setBlockState(blockpos2, this.fallTile, 3)) {
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling)block).onEndFalling(this.worldObj, blockpos2);
                                }
                                if (this.tileEntityData != null && block instanceof ITileEntityProvider) {
                                    final TileEntity tileentity = this.worldObj.getTileEntity(blockpos2);
                                    if (tileentity != null) {
                                        final NBTTagCompound nbttagcompound = new NBTTagCompound();
                                        tileentity.writeToNBT(nbttagcompound);
                                        for (final String s : this.tileEntityData.getKeySet()) {
                                            final NBTBase nbtbase = this.tileEntityData.getTag(s);
                                            if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }
                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                            }
                        }
                    }
                }
                else if ((this.fallTime > 100 && !this.worldObj.isRemote && (blockpos2.getY() < 1 || blockpos2.getY() > 256)) || this.fallTime > 600) {
                    if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                        this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                    }
                    this.setDead();
                }
            }
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        final Block block = this.fallTile.getBlock();
        if (this.hurtEntities) {
            final int i = MathHelper.ceiling_float_int(distance - 1.0f);
            if (i > 0) {
                final List<Entity> list = (List<Entity>)Lists.newArrayList((Iterable<?>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                final boolean flag = block == Blocks.anvil;
                final DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
                for (final Entity entity : list) {
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor_float(i * this.fallHurtAmount), this.fallHurtMax));
                }
                if (flag && this.rand.nextFloat() < 0.05000000074505806 + i * 0.05) {
                    int j = this.fallTile.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
                    if (++j > 2) {
                        this.canSetAsBlock = true;
                    }
                    else {
                        this.fallTile = this.fallTile.withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, j);
                    }
                }
            }
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        final Block block = (this.fallTile != null) ? this.fallTile.getBlock() : Blocks.air;
        final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(block);
        tagCompound.setString("Block", (resourcelocation == null) ? "" : resourcelocation.toString());
        tagCompound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
        tagCompound.setByte("Time", (byte)this.fallTime);
        tagCompound.setBoolean("DropItem", this.shouldDropItem);
        tagCompound.setBoolean("HurtEntities", this.hurtEntities);
        tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            tagCompound.setTag("TileEntityData", this.tileEntityData);
        }
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        final int i = tagCompund.getByte("Data") & 0xFF;
        if (tagCompund.hasKey("Block", 8)) {
            this.fallTile = Block.getBlockFromName(tagCompund.getString("Block")).getStateFromMeta(i);
        }
        else if (tagCompund.hasKey("TileID", 99)) {
            this.fallTile = Block.getBlockById(tagCompund.getInteger("TileID")).getStateFromMeta(i);
        }
        else {
            this.fallTile = Block.getBlockById(tagCompund.getByte("Tile") & 0xFF).getStateFromMeta(i);
        }
        this.fallTime = (tagCompund.getByte("Time") & 0xFF);
        final Block block = this.fallTile.getBlock();
        if (tagCompund.hasKey("HurtEntities", 99)) {
            this.hurtEntities = tagCompund.getBoolean("HurtEntities");
            this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
            this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
        }
        else if (block == Blocks.anvil) {
            this.hurtEntities = true;
        }
        if (tagCompund.hasKey("DropItem", 99)) {
            this.shouldDropItem = tagCompund.getBoolean("DropItem");
        }
        if (tagCompund.hasKey("TileEntityData", 10)) {
            this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
        }
        if (block == null || block.getMaterial() == Material.air) {
            this.fallTile = Blocks.sand.getDefaultState();
        }
    }
    
    public World getWorldObj() {
        return this.worldObj;
    }
    
    public void setHurtEntities(final boolean p_145806_1_) {
        this.hurtEntities = p_145806_1_;
    }
    
    @Override
    public boolean canRenderOnFire() {
        return false;
    }
    
    @Override
    public void addEntityCrashInfo(final CrashReportCategory category) {
        super.addEntityCrashInfo(category);
        if (this.fallTile != null) {
            final Block block = this.fallTile.getBlock();
            category.addCrashSection("Immitating block ID", Block.getIdFromBlock(block));
            category.addCrashSection("Immitating block data", block.getMetaFromState(this.fallTile));
        }
    }
    
    public IBlockState getBlock() {
        return this.fallTile;
    }
}
