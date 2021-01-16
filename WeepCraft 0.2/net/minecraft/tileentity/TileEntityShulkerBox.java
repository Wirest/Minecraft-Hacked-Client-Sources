package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private static final int[] field_190595_a = new int[27];
    private NonNullList<ItemStack> field_190596_f;
    private boolean field_190597_g;
    private int field_190598_h;
    private TileEntityShulkerBox.AnimationStatus field_190599_i;
    private float field_190600_j;
    private float field_190601_k;
    private EnumDyeColor field_190602_l;
    private boolean field_190594_p;

    public TileEntityShulkerBox()
    {
        this((EnumDyeColor)null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor p_i47242_1_)
    {
        this.field_190596_f = NonNullList.<ItemStack>func_191197_a(27, ItemStack.field_190927_a);
        this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
        this.field_190602_l = p_i47242_1_;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        this.func_190583_o();

        if (this.field_190599_i == TileEntityShulkerBox.AnimationStatus.OPENING || this.field_190599_i == TileEntityShulkerBox.AnimationStatus.CLOSING)
        {
            this.func_190589_G();
        }
    }

    protected void func_190583_o()
    {
        this.field_190601_k = this.field_190600_j;

        switch (this.field_190599_i)
        {
            case CLOSED:
                this.field_190600_j = 0.0F;
                break;

            case OPENING:
                this.field_190600_j += 0.1F;

                if (this.field_190600_j >= 1.0F)
                {
                    this.func_190589_G();
                    this.field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENED;
                    this.field_190600_j = 1.0F;
                }

                break;

            case CLOSING:
                this.field_190600_j -= 0.1F;

                if (this.field_190600_j <= 0.0F)
                {
                    this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
                    this.field_190600_j = 0.0F;
                }

                break;

            case OPENED:
                this.field_190600_j = 1.0F;
        }
    }

    public TileEntityShulkerBox.AnimationStatus func_190591_p()
    {
        return this.field_190599_i;
    }

    public AxisAlignedBB func_190584_a(IBlockState p_190584_1_)
    {
        return this.func_190587_b((EnumFacing)p_190584_1_.getValue(BlockShulkerBox.field_190957_a));
    }

    public AxisAlignedBB func_190587_b(EnumFacing p_190587_1_)
    {
        return Block.FULL_BLOCK_AABB.addCoord((double)(0.5F * this.func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetX()), (double)(0.5F * this.func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetY()), (double)(0.5F * this.func_190585_a(1.0F) * (float)p_190587_1_.getFrontOffsetZ()));
    }

    private AxisAlignedBB func_190588_c(EnumFacing p_190588_1_)
    {
        EnumFacing enumfacing = p_190588_1_.getOpposite();
        return this.func_190587_b(p_190588_1_).func_191195_a((double)enumfacing.getFrontOffsetX(), (double)enumfacing.getFrontOffsetY(), (double)enumfacing.getFrontOffsetZ());
    }

    private void func_190589_G()
    {
        IBlockState iblockstate = this.world.getBlockState(this.getPos());

        if (iblockstate.getBlock() instanceof BlockShulkerBox)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(BlockShulkerBox.field_190957_a);
            AxisAlignedBB axisalignedbb = this.func_190588_c(enumfacing).offset(this.pos);
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);

            if (!list.isEmpty())
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE)
                    {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();

                        switch (enumfacing.getAxis())
                        {
                            case X:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                                }
                                else
                                {
                                    d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                                }

                                d0 = d0 + 0.01D;
                                break;

                            case Y:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                                }
                                else
                                {
                                    d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                                }

                                d1 = d1 + 0.01D;
                                break;

                            case Z:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                                }
                                else
                                {
                                    d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                                }

                                d2 = d2 + 0.01D;
                        }

                        entity.moveEntity(MoverType.SHULKER_BOX, d0 * (double)enumfacing.getFrontOffsetX(), d1 * (double)enumfacing.getFrontOffsetY(), d2 * (double)enumfacing.getFrontOffsetZ());
                    }
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.field_190596_f.size();
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.field_190598_h = type;

            if (type == 0)
            {
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSING;
            }

            if (type == 1)
            {
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENING;
            }

            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.field_190598_h < 0)
            {
                this.field_190598_h = 0;
            }

            ++this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);

            if (this.field_190598_h == 1)
            {
                this.world.playSound((EntityPlayer)null, this.pos, SoundEvents.field_191262_fB, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            --this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);

            if (this.field_190598_h <= 0)
            {
                this.world.playSound((EntityPlayer)null, this.pos, SoundEvents.field_191261_fA, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerShulkerBox(playerInventory, this, playerIn);
    }

    public String getGuiID()
    {
        return "minecraft:shulker_box";
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.field_190577_o : "container.shulkerBox";
    }

    public static void func_190593_a(DataFixer p_190593_0_)
    {
        p_190593_0_.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.func_190586_e(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        return this.func_190580_f(compound);
    }

    public void func_190586_e(NBTTagCompound p_190586_1_)
    {
        this.field_190596_f = NonNullList.<ItemStack>func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);

        if (!this.checkLootAndRead(p_190586_1_) && p_190586_1_.hasKey("Items", 9))
        {
            ItemStackHelper.func_191283_b(p_190586_1_, this.field_190596_f);
        }

        if (p_190586_1_.hasKey("CustomName", 8))
        {
            this.field_190577_o = p_190586_1_.getString("CustomName");
        }
    }

    public NBTTagCompound func_190580_f(NBTTagCompound p_190580_1_)
    {
        if (!this.checkLootAndWrite(p_190580_1_))
        {
            ItemStackHelper.func_191281_a(p_190580_1_, this.field_190596_f, false);
        }

        if (this.hasCustomName())
        {
            p_190580_1_.setString("CustomName", this.field_190577_o);
        }

        if (!p_190580_1_.hasKey("Lock") && this.isLocked())
        {
            this.getLockCode().toNBT(p_190580_1_);
        }

        return p_190580_1_;
    }

    protected NonNullList<ItemStack> func_190576_q()
    {
        return this.field_190596_f;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : this.field_190596_f)
        {
            if (!itemstack.func_190926_b())
            {
                return false;
            }
        }

        return true;
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return field_190595_a;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    public void clear()
    {
        this.field_190597_g = true;
        super.clear();
    }

    public boolean func_190590_r()
    {
        return this.field_190597_g;
    }

    public float func_190585_a(float p_190585_1_)
    {
        return this.field_190601_k + (this.field_190600_j - this.field_190601_k) * p_190585_1_;
    }

    public EnumDyeColor func_190592_s()
    {
        if (this.field_190602_l == null)
        {
            this.field_190602_l = BlockShulkerBox.func_190954_c(this.getBlockType());
        }

        return this.field_190602_l;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }

    public boolean func_190581_E()
    {
        return this.field_190594_p;
    }

    public void func_190579_a(boolean p_190579_1_)
    {
        this.field_190594_p = p_190579_1_;
    }

    public boolean func_190582_F()
    {
        return !this.func_190581_E() || !this.func_191420_l() || this.hasCustomName() || this.lootTable != null;
    }

    static
    {
        for (int i = 0; i < field_190595_a.length; field_190595_a[i] = i++)
        {
            ;
        }
    }

    public static enum AnimationStatus
    {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;
    }
}
