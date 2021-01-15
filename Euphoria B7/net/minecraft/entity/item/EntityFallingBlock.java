package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity
{
    private IBlockState field_175132_d;
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean field_145808_f;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;
    private static final String __OBFID = "CL_00001668";

    public EntityFallingBlock(World worldIn)
    {
        super(worldIn);
    }

    public EntityFallingBlock(World worldIn, double p_i45848_2_, double p_i45848_4_, double p_i45848_6_, IBlockState p_i45848_8_)
    {
        super(worldIn);
        this.field_175132_d = p_i45848_8_;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.setPosition(p_i45848_2_, p_i45848_4_, p_i45848_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i45848_2_;
        this.prevPosY = p_i45848_4_;
        this.prevPosZ = p_i45848_6_;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        Block var1 = this.field_175132_d.getBlock();

        if (var1.getMaterial() == Material.air)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            BlockPos var2;

            if (this.fallTime++ == 0)
            {
                var2 = new BlockPos(this);

                if (this.worldObj.getBlockState(var2).getBlock() == var1)
                {
                    this.worldObj.setBlockToAir(var2);
                }
                else if (!this.worldObj.isRemote)
                {
                    this.setDead();
                    return;
                }
            }

            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (!this.worldObj.isRemote)
            {
                var2 = new BlockPos(this);

                if (this.onGround)
                {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlockState(var2).getBlock() != Blocks.piston_extension)
                    {
                        this.setDead();

                        if (!this.field_145808_f && this.worldObj.canBlockBePlaced(var1, var2, true, EnumFacing.UP, (Entity)null, (ItemStack)null) && !BlockFalling.canFallInto(this.worldObj, var2.offsetDown()) && this.worldObj.setBlockState(var2, this.field_175132_d, 3))
                        {
                            if (var1 instanceof BlockFalling)
                            {
                                ((BlockFalling)var1).onEndFalling(this.worldObj, var2);
                            }

                            if (this.tileEntityData != null && var1 instanceof ITileEntityProvider)
                            {
                                TileEntity var3 = this.worldObj.getTileEntity(var2);

                                if (var3 != null)
                                {
                                    NBTTagCompound var4 = new NBTTagCompound();
                                    var3.writeToNBT(var4);
                                    Iterator var5 = this.tileEntityData.getKeySet().iterator();

                                    while (var5.hasNext())
                                    {
                                        String var6 = (String)var5.next();
                                        NBTBase var7 = this.tileEntityData.getTag(var6);

                                        if (!var6.equals("x") && !var6.equals("y") && !var6.equals("z"))
                                        {
                                            var4.setTag(var6, var7.copy());
                                        }
                                    }

                                    var3.readFromNBT(var4);
                                    var3.markDirty();
                                }
                            }
                        }
                        else if (this.shouldDropItem && !this.field_145808_f && this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops"))
                        {
                            this.entityDropItem(new ItemStack(var1, 1, var1.damageDropped(this.field_175132_d)), 0.0F);
                        }
                    }
                }
                else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2.getY() < 1 || var2.getY() > 256) || this.fallTime > 600)
                {
                    if (this.shouldDropItem && this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops"))
                    {
                        this.entityDropItem(new ItemStack(var1, 1, var1.damageDropped(this.field_175132_d)), 0.0F);
                    }

                    this.setDead();
                }
            }
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
        Block var3 = this.field_175132_d.getBlock();

        if (this.hurtEntities)
        {
            int var4 = MathHelper.ceiling_float_int(distance - 1.0F);

            if (var4 > 0)
            {
                ArrayList var5 = Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                boolean var6 = var3 == Blocks.anvil;
                DamageSource var7 = var6 ? DamageSource.anvil : DamageSource.fallingBlock;
                Iterator var8 = var5.iterator();

                while (var8.hasNext())
                {
                    Entity var9 = (Entity)var8.next();
                    var9.attackEntityFrom(var7, (float)Math.min(MathHelper.floor_float((float)var4 * this.fallHurtAmount), this.fallHurtMax));
                }

                if (var6 && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)var4 * 0.05D)
                {
                    int var10 = ((Integer)this.field_175132_d.getValue(BlockAnvil.DAMAGE)).intValue();
                    ++var10;

                    if (var10 > 2)
                    {
                        this.field_145808_f = true;
                    }
                    else
                    {
                        this.field_175132_d = this.field_175132_d.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(var10));
                    }
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        Block var2 = this.field_175132_d != null ? this.field_175132_d.getBlock() : Blocks.air;
        ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2);
        tagCompound.setString("Block", var3 == null ? "" : var3.toString());
        tagCompound.setByte("Data", (byte)var2.getMetaFromState(this.field_175132_d));
        tagCompound.setByte("Time", (byte)this.fallTime);
        tagCompound.setBoolean("DropItem", this.shouldDropItem);
        tagCompound.setBoolean("HurtEntities", this.hurtEntities);
        tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        tagCompound.setInteger("FallHurtMax", this.fallHurtMax);

        if (this.tileEntityData != null)
        {
            tagCompound.setTag("TileEntityData", this.tileEntityData);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        int var2 = tagCompund.getByte("Data") & 255;

        if (tagCompund.hasKey("Block", 8))
        {
            this.field_175132_d = Block.getBlockFromName(tagCompund.getString("Block")).getStateFromMeta(var2);
        }
        else if (tagCompund.hasKey("TileID", 99))
        {
            this.field_175132_d = Block.getBlockById(tagCompund.getInteger("TileID")).getStateFromMeta(var2);
        }
        else
        {
            this.field_175132_d = Block.getBlockById(tagCompund.getByte("Tile") & 255).getStateFromMeta(var2);
        }

        this.fallTime = tagCompund.getByte("Time") & 255;
        Block var3 = this.field_175132_d.getBlock();

        if (tagCompund.hasKey("HurtEntities", 99))
        {
            this.hurtEntities = tagCompund.getBoolean("HurtEntities");
            this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
            this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
        }
        else if (var3 == Blocks.anvil)
        {
            this.hurtEntities = true;
        }

        if (tagCompund.hasKey("DropItem", 99))
        {
            this.shouldDropItem = tagCompund.getBoolean("DropItem");
        }

        if (tagCompund.hasKey("TileEntityData", 10))
        {
            this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
        }

        if (var3 == null || var3.getMaterial() == Material.air)
        {
            this.field_175132_d = Blocks.sand.getDefaultState();
        }
    }

    public World getWorldObj()
    {
        return this.worldObj;
    }

    public void setHurtEntities(boolean p_145806_1_)
    {
        this.hurtEntities = p_145806_1_;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        super.addEntityCrashInfo(category);

        if (this.field_175132_d != null)
        {
            Block var2 = this.field_175132_d.getBlock();
            category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(var2)));
            category.addCrashSection("Immitating block data", Integer.valueOf(var2.getMetaFromState(this.field_175132_d)));
        }
    }

    public IBlockState getBlock()
    {
        return this.field_175132_d;
    }
}
