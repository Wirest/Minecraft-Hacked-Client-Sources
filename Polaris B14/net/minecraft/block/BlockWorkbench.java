/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerWorkbench;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.IInteractionObject;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockWorkbench extends Block
/*    */ {
/*    */   protected BlockWorkbench()
/*    */   {
/* 23 */     super(Material.wood);
/* 24 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 29 */     if (worldIn.isRemote)
/*    */     {
/* 31 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 35 */     playerIn.displayGui(new InterfaceCraftingTable(worldIn, pos));
/* 36 */     playerIn.triggerAchievement(StatList.field_181742_Z);
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class InterfaceCraftingTable
/*    */     implements IInteractionObject
/*    */   {
/*    */     private final World world;
/*    */     private final BlockPos position;
/*    */     
/*    */     public InterfaceCraftingTable(World worldIn, BlockPos pos)
/*    */     {
/* 48 */       this.world = worldIn;
/* 49 */       this.position = pos;
/*    */     }
/*    */     
/*    */     public String getName()
/*    */     {
/* 54 */       return null;
/*    */     }
/*    */     
/*    */     public boolean hasCustomName()
/*    */     {
/* 59 */       return false;
/*    */     }
/*    */     
/*    */     public IChatComponent getDisplayName()
/*    */     {
/* 64 */       return new ChatComponentTranslation(Blocks.crafting_table.getUnlocalizedName() + ".name", new Object[0]);
/*    */     }
/*    */     
/*    */     public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*    */     {
/* 69 */       return new ContainerWorkbench(playerInventory, this.world, this.position);
/*    */     }
/*    */     
/*    */     public String getGuiID()
/*    */     {
/* 74 */       return "minecraft:crafting_table";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockWorkbench.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */