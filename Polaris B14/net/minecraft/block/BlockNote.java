/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityNote;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockNote extends BlockContainer
/*     */ {
/*  19 */   private static final List<String> INSTRUMENTS = Lists.newArrayList(new String[] { "harp", "bd", "snare", "hat", "bassattack" });
/*     */   
/*     */   public BlockNote()
/*     */   {
/*  23 */     super(Material.wood);
/*  24 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  32 */     boolean flag = worldIn.isBlockPowered(pos);
/*  33 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  35 */     if ((tileentity instanceof TileEntityNote))
/*     */     {
/*  37 */       TileEntityNote tileentitynote = (TileEntityNote)tileentity;
/*     */       
/*  39 */       if (tileentitynote.previousRedstoneState != flag)
/*     */       {
/*  41 */         if (flag)
/*     */         {
/*  43 */           tileentitynote.triggerNote(worldIn, pos);
/*     */         }
/*     */         
/*  46 */         tileentitynote.previousRedstoneState = flag;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  53 */     if (worldIn.isRemote)
/*     */     {
/*  55 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  59 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  61 */     if ((tileentity instanceof TileEntityNote))
/*     */     {
/*  63 */       TileEntityNote tileentitynote = (TileEntityNote)tileentity;
/*  64 */       tileentitynote.changePitch();
/*  65 */       tileentitynote.triggerNote(worldIn, pos);
/*  66 */       playerIn.triggerAchievement(StatList.field_181735_S);
/*     */     }
/*     */     
/*  69 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
/*     */   {
/*  75 */     if (!worldIn.isRemote)
/*     */     {
/*  77 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  79 */       if ((tileentity instanceof TileEntityNote))
/*     */       {
/*  81 */         ((TileEntityNote)tileentity).triggerNote(worldIn, pos);
/*  82 */         playerIn.triggerAchievement(StatList.field_181734_R);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  92 */     return new TileEntityNote();
/*     */   }
/*     */   
/*     */   private String getInstrument(int id)
/*     */   {
/*  97 */     if ((id < 0) || (id >= INSTRUMENTS.size()))
/*     */     {
/*  99 */       id = 0;
/*     */     }
/*     */     
/* 102 */     return (String)INSTRUMENTS.get(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
/*     */   {
/* 110 */     float f = (float)Math.pow(2.0D, (eventParam - 12) / 12.0D);
/* 111 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "note." + getInstrument(eventID), 3.0F, f);
/* 112 */     worldIn.spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, eventParam / 24.0D, 0.0D, 0.0D, new int[0]);
/* 113 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 121 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */