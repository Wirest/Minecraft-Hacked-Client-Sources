package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    private static final Logger logger = LogManager.getLogger();

    /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot;

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots;
    private World theWorld;
    private BlockPos field_178156_j;

    /** The maximum cost of repairing/renaming in the anvil. */
    public int maximumCost;

    /** determined by damage of input item and stackSize of repair materials */
    private int materialCost;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;
    private static final String __OBFID = "CL_00001732";

    public ContainerRepair(InventoryPlayer p_i45806_1_, World worldIn, EntityPlayer p_i45806_3_)
    {
        this(p_i45806_1_, worldIn, BlockPos.ORIGIN, p_i45806_3_);
    }

    public ContainerRepair(InventoryPlayer p_i45807_1_, final World worldIn, final BlockPos p_i45807_3_, EntityPlayer p_i45807_4_)
    {
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Repair", true, 2)
        {
            private static final String __OBFID = "CL_00001733";
            public void markDirty()
            {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.field_178156_j = p_i45807_3_;
        this.theWorld = worldIn;
        this.thePlayer = p_i45807_4_;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47)
        {
            private static final String __OBFID = "CL_00001734";
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
            public boolean canTakeStack(EntityPlayer p_82869_1_)
            {
                return (p_82869_1_.capabilities.isCreativeMode || p_82869_1_.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
            {
                if (!playerIn.capabilities.isCreativeMode)
                {
                    playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }

                ContainerRepair.this.inputSlots.setInventorySlotContents(0, (ItemStack)null);

                if (ContainerRepair.this.materialCost > 0)
                {
                    ItemStack var3 = ContainerRepair.this.inputSlots.getStackInSlot(1);

                    if (var3 != null && var3.stackSize > ContainerRepair.this.materialCost)
                    {
                        var3.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, var3);
                    }
                    else
                    {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
                    }
                }
                else
                {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
                }

                ContainerRepair.this.maximumCost = 0;
                IBlockState var5 = worldIn.getBlockState(p_i45807_3_);

                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && var5.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12F)
                {
                    int var4 = ((Integer)var5.getValue(BlockAnvil.DAMAGE)).intValue();
                    ++var4;

                    if (var4 > 2)
                    {
                        worldIn.setBlockToAir(p_i45807_3_);
                        worldIn.playAuxSFX(1020, p_i45807_3_, 0);
                    }
                    else
                    {
                        worldIn.setBlockState(p_i45807_3_, var5.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(var4)), 2);
                        worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                    }
                }
                else if (!worldIn.isRemote)
                {
                    worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                }
            }
        });
        int var5;

        for (var5 = 0; var5 < 3; ++var5)
        {
            for (int var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(p_i45807_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, 84 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 9; ++var5)
        {
            this.addSlotToContainer(new Slot(p_i45807_1_, var5, 8 + var5 * 18, 142));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory p_75130_1_)
    {
        super.onCraftMatrixChanged(p_75130_1_);

        if (p_75130_1_ == this.inputSlots)
        {
            this.updateRepairOutput();
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput()
    {
        boolean var1 = false;
        boolean var2 = true;
        boolean var3 = true;
        boolean var4 = true;
        boolean var5 = true;
        boolean var6 = true;
        boolean var7 = true;
        ItemStack var8 = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int var9 = 0;
        byte var10 = 0;
        byte var11 = 0;

        if (var8 == null)
        {
            this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
            this.maximumCost = 0;
        }
        else
        {
            ItemStack var12 = var8.copy();
            ItemStack var13 = this.inputSlots.getStackInSlot(1);
            Map var14 = EnchantmentHelper.getEnchantments(var12);
            boolean var15 = false;
            int var25 = var10 + var8.getRepairCost() + (var13 == null ? 0 : var13.getRepairCost());
            this.materialCost = 0;
            int var16;

            if (var13 != null)
            {
                var15 = var13.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(var13).tagCount() > 0;
                int var17;
                int var18;

                if (var12.isItemStackDamageable() && var12.getItem().getIsRepairable(var8, var13))
                {
                    var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);

                    if (var16 <= 0)
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    for (var17 = 0; var16 > 0 && var17 < var13.stackSize; ++var17)
                    {
                        var18 = var12.getItemDamage() - var16;
                        var12.setItemDamage(var18);
                        ++var9;
                        var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
                    }

                    this.materialCost = var17;
                }
                else
                {
                    if (!var15 && (var12.getItem() != var13.getItem() || !var12.isItemStackDamageable()))
                    {
                        this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                        this.maximumCost = 0;
                        return;
                    }

                    int var20;

                    if (var12.isItemStackDamageable() && !var15)
                    {
                        var16 = var8.getMaxDamage() - var8.getItemDamage();
                        var17 = var13.getMaxDamage() - var13.getItemDamage();
                        var18 = var17 + var12.getMaxDamage() * 12 / 100;
                        int var19 = var16 + var18;
                        var20 = var12.getMaxDamage() - var19;

                        if (var20 < 0)
                        {
                            var20 = 0;
                        }

                        if (var20 < var12.getMetadata())
                        {
                            var12.setItemDamage(var20);
                            var9 += 2;
                        }
                    }

                    Map var26 = EnchantmentHelper.getEnchantments(var13);
                    Iterator var27 = var26.keySet().iterator();

                    while (var27.hasNext())
                    {
                        var18 = ((Integer)var27.next()).intValue();
                        Enchantment var28 = Enchantment.func_180306_c(var18);

                        if (var28 != null)
                        {
                            var20 = var14.containsKey(Integer.valueOf(var18)) ? ((Integer)var14.get(Integer.valueOf(var18))).intValue() : 0;
                            int var21 = ((Integer)var26.get(Integer.valueOf(var18))).intValue();
                            int var10000;

                            if (var20 == var21)
                            {
                                ++var21;
                                var10000 = var21;
                            }
                            else
                            {
                                var10000 = Math.max(var21, var20);
                            }

                            var21 = var10000;
                            boolean var22 = var28.canApply(var8);

                            if (this.thePlayer.capabilities.isCreativeMode || var8.getItem() == Items.enchanted_book)
                            {
                                var22 = true;
                            }

                            Iterator var23 = var14.keySet().iterator();

                            while (var23.hasNext())
                            {
                                int var24 = ((Integer)var23.next()).intValue();

                                if (var24 != var18 && !var28.canApplyTogether(Enchantment.func_180306_c(var24)))
                                {
                                    var22 = false;
                                    ++var9;
                                }
                            }

                            if (var22)
                            {
                                if (var21 > var28.getMaxLevel())
                                {
                                    var21 = var28.getMaxLevel();
                                }

                                var14.put(Integer.valueOf(var18), Integer.valueOf(var21));
                                int var29 = 0;

                                switch (var28.getWeight())
                                {
                                    case 1:
                                        var29 = 8;
                                        break;

                                    case 2:
                                        var29 = 4;

                                    case 3:
                                    case 4:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    default:
                                        break;

                                    case 5:
                                        var29 = 2;
                                        break;

                                    case 10:
                                        var29 = 1;
                                }

                                if (var15)
                                {
                                    var29 = Math.max(1, var29 / 2);
                                }

                                var9 += var29 * var21;
                            }
                        }
                    }
                }
            }

            if (StringUtils.isBlank(this.repairedItemName))
            {
                if (var8.hasDisplayName())
                {
                    var11 = 1;
                    var9 += var11;
                    var12.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(var8.getDisplayName()))
            {
                var11 = 1;
                var9 += var11;
                var12.setStackDisplayName(this.repairedItemName);
            }

            this.maximumCost = var25 + var9;

            if (var9 <= 0)
            {
                var12 = null;
            }

            if (var11 == var9 && var11 > 0 && this.maximumCost >= 40)
            {
                this.maximumCost = 39;
            }

            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode)
            {
                var12 = null;
            }

            if (var12 != null)
            {
                var16 = var12.getRepairCost();

                if (var13 != null && var16 < var13.getRepairCost())
                {
                    var16 = var13.getRepairCost();
                }

                var16 = var16 * 2 + 1;
                var12.setRepairCost(var16);
                EnchantmentHelper.setEnchantments(var14, var12);
            }

            this.outputSlot.setInventorySlotContents(0, var12);
            this.detectAndSendChanges();
        }
    }

    public void onCraftGuiOpened(ICrafting p_75132_1_)
    {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
        if (p_75137_1_ == 0)
        {
            this.maximumCost = p_75137_2_;
        }
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);

        if (!this.theWorld.isRemote)
        {
            for (int var2 = 0; var2 < this.inputSlots.getSizeInventory(); ++var2)
            {
                ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);

                if (var3 != null)
                {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.theWorld.getBlockState(this.field_178156_j).getBlock() != Blocks.anvil ? false : playerIn.getDistanceSq((double)this.field_178156_j.getX() + 0.5D, (double)this.field_178156_j.getY() + 0.5D, (double)this.field_178156_j.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(var5, 3, 39, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (index != 0 && index != 1)
            {
                if (index >= 3 && index < 39 && !this.mergeItemStack(var5, 0, 2, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 3, 39, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(playerIn, var5);
        }

        return var3;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String p_82850_1_)
    {
        this.repairedItemName = p_82850_1_;

        if (this.getSlot(2).getHasStack())
        {
            ItemStack var2 = this.getSlot(2).getStack();

            if (StringUtils.isBlank(p_82850_1_))
            {
                var2.clearCustomName();
            }
            else
            {
                var2.setStackDisplayName(this.repairedItemName);
            }
        }

        this.updateRepairOutput();
    }
}
