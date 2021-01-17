// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.init.Blocks;
import net.minecraft.util.IChatComponent;
import net.minecraft.stats.StatList;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockWorkbench extends Block
{
    protected BlockWorkbench() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        playerIn.displayGui(new InterfaceCraftingTable(worldIn, pos));
        playerIn.triggerAchievement(StatList.field_181742_Z);
        return true;
    }
    
    public static class InterfaceCraftingTable implements IInteractionObject
    {
        private final World world;
        private final BlockPos position;
        
        public InterfaceCraftingTable(final World worldIn, final BlockPos pos) {
            this.world = worldIn;
            this.position = pos;
        }
        
        @Override
        public String getName() {
            return null;
        }
        
        @Override
        public boolean hasCustomName() {
            return false;
        }
        
        @Override
        public IChatComponent getDisplayName() {
            return new ChatComponentTranslation(String.valueOf(Blocks.crafting_table.getUnlocalizedName()) + ".name", new Object[0]);
        }
        
        @Override
        public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
            return new ContainerWorkbench(playerInventory, this.world, this.position);
        }
        
        @Override
        public String getGuiID() {
            return "minecraft:crafting_table";
        }
    }
}
