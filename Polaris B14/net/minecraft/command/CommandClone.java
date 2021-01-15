/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.world.NextTickListEntry;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class CommandClone
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  24 */     return "clone";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  32 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  40 */     return "commands.clone.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  48 */     if (args.length < 9)
/*     */     {
/*  50 */       throw new WrongUsageException("commands.clone.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  54 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  55 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  56 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  57 */     BlockPos blockpos2 = parseBlockPos(sender, args, 6, false);
/*  58 */     StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
/*  59 */     StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
/*  60 */     int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
/*     */     
/*  62 */     if (i > 32768)
/*     */     {
/*  64 */       throw new CommandException("commands.clone.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(32768) });
/*     */     }
/*     */     
/*     */ 
/*  68 */     boolean flag = false;
/*  69 */     Block block = null;
/*  70 */     int j = -1;
/*     */     
/*  72 */     if (((args.length < 11) || ((!args[10].equals("force")) && (!args[10].equals("move")))) && (structureboundingbox.intersectsWith(structureboundingbox1)))
/*     */     {
/*  74 */       throw new CommandException("commands.clone.noOverlap", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  78 */     if ((args.length >= 11) && (args[10].equals("move")))
/*     */     {
/*  80 */       flag = true;
/*     */     }
/*     */     
/*  83 */     if ((structureboundingbox.minY >= 0) && (structureboundingbox.maxY < 256) && (structureboundingbox1.minY >= 0) && (structureboundingbox1.maxY < 256))
/*     */     {
/*  85 */       World world = sender.getEntityWorld();
/*     */       
/*  87 */       if ((world.isAreaLoaded(structureboundingbox)) && (world.isAreaLoaded(structureboundingbox1)))
/*     */       {
/*  89 */         boolean flag1 = false;
/*     */         
/*  91 */         if (args.length >= 10)
/*     */         {
/*  93 */           if (args[9].equals("masked"))
/*     */           {
/*  95 */             flag1 = true;
/*     */           }
/*  97 */           else if (args[9].equals("filtered"))
/*     */           {
/*  99 */             if (args.length < 12)
/*     */             {
/* 101 */               throw new WrongUsageException("commands.clone.usage", new Object[0]);
/*     */             }
/*     */             
/* 104 */             block = getBlockByText(sender, args[11]);
/*     */             
/* 106 */             if (args.length >= 13)
/*     */             {
/* 108 */               j = parseInt(args[12], 0, 15);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 113 */         List<StaticCloneData> list = Lists.newArrayList();
/* 114 */         List<StaticCloneData> list1 = Lists.newArrayList();
/* 115 */         List<StaticCloneData> list2 = Lists.newArrayList();
/* 116 */         LinkedList<BlockPos> linkedlist = Lists.newLinkedList();
/* 117 */         BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
/*     */         int l;
/* 119 */         BlockPos blockpos4; for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; k++)
/*     */         {
/* 121 */           for (l = structureboundingbox.minY; l <= structureboundingbox.maxY; l++)
/*     */           {
/* 123 */             for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; i1++)
/*     */             {
/* 125 */               blockpos4 = new BlockPos(i1, l, k);
/* 126 */               BlockPos blockpos5 = blockpos4.add(blockpos3);
/* 127 */               IBlockState iblockstate = world.getBlockState(blockpos4);
/*     */               
/* 129 */               if (((!flag1) || (iblockstate.getBlock() != Blocks.air)) && ((block == null) || ((iblockstate.getBlock() == block) && ((j < 0) || (iblockstate.getBlock().getMetaFromState(iblockstate) == j)))))
/*     */               {
/* 131 */                 TileEntity tileentity = world.getTileEntity(blockpos4);
/*     */                 
/* 133 */                 if (tileentity != null)
/*     */                 {
/* 135 */                   NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 136 */                   tileentity.writeToNBT(nbttagcompound);
/* 137 */                   list1.add(new StaticCloneData(blockpos5, iblockstate, nbttagcompound));
/* 138 */                   linkedlist.addLast(blockpos4);
/*     */                 }
/* 140 */                 else if ((!iblockstate.getBlock().isFullBlock()) && (!iblockstate.getBlock().isFullCube()))
/*     */                 {
/* 142 */                   list2.add(new StaticCloneData(blockpos5, iblockstate, null));
/* 143 */                   linkedlist.addFirst(blockpos4);
/*     */                 }
/*     */                 else
/*     */                 {
/* 147 */                   list.add(new StaticCloneData(blockpos5, iblockstate, null));
/* 148 */                   linkedlist.addLast(blockpos4);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 155 */         if (flag)
/*     */         {
/* 157 */           for (BlockPos blockpos6 : linkedlist)
/*     */           {
/* 159 */             TileEntity tileentity1 = world.getTileEntity(blockpos6);
/*     */             
/* 161 */             if ((tileentity1 instanceof IInventory))
/*     */             {
/* 163 */               ((IInventory)tileentity1).clear();
/*     */             }
/*     */             
/* 166 */             world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2);
/*     */           }
/*     */           
/* 169 */           for (BlockPos blockpos7 : linkedlist)
/*     */           {
/* 171 */             world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
/*     */           }
/*     */         }
/*     */         
/* 175 */         List<StaticCloneData> list3 = Lists.newArrayList();
/* 176 */         list3.addAll(list);
/* 177 */         list3.addAll(list1);
/* 178 */         list3.addAll(list2);
/* 179 */         List<StaticCloneData> list4 = Lists.reverse(list3);
/*     */         
/* 181 */         for (StaticCloneData commandclone$staticclonedata : list4)
/*     */         {
/* 183 */           TileEntity tileentity2 = world.getTileEntity(commandclone$staticclonedata.field_179537_a);
/*     */           
/* 185 */           if ((tileentity2 instanceof IInventory))
/*     */           {
/* 187 */             ((IInventory)tileentity2).clear();
/*     */           }
/*     */           
/* 190 */           world.setBlockState(commandclone$staticclonedata.field_179537_a, Blocks.barrier.getDefaultState(), 2);
/*     */         }
/*     */         
/* 193 */         i = 0;
/*     */         
/* 195 */         for (StaticCloneData commandclone$staticclonedata1 : list3)
/*     */         {
/* 197 */           if (world.setBlockState(commandclone$staticclonedata1.field_179537_a, commandclone$staticclonedata1.blockState, 2))
/*     */           {
/* 199 */             i++;
/*     */           }
/*     */         }
/*     */         TileEntity tileentity3;
/* 203 */         for (StaticCloneData commandclone$staticclonedata2 : list1)
/*     */         {
/* 205 */           tileentity3 = world.getTileEntity(commandclone$staticclonedata2.field_179537_a);
/*     */           
/* 207 */           if ((commandclone$staticclonedata2.field_179536_c != null) && (tileentity3 != null))
/*     */           {
/* 209 */             commandclone$staticclonedata2.field_179536_c.setInteger("x", commandclone$staticclonedata2.field_179537_a.getX());
/* 210 */             commandclone$staticclonedata2.field_179536_c.setInteger("y", commandclone$staticclonedata2.field_179537_a.getY());
/* 211 */             commandclone$staticclonedata2.field_179536_c.setInteger("z", commandclone$staticclonedata2.field_179537_a.getZ());
/* 212 */             tileentity3.readFromNBT(commandclone$staticclonedata2.field_179536_c);
/* 213 */             tileentity3.markDirty();
/*     */           }
/*     */           
/* 216 */           world.setBlockState(commandclone$staticclonedata2.field_179537_a, commandclone$staticclonedata2.blockState, 2);
/*     */         }
/*     */         
/* 219 */         for (StaticCloneData commandclone$staticclonedata3 : list4)
/*     */         {
/* 221 */           world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState.getBlock());
/*     */         }
/*     */         
/* 224 */         List<NextTickListEntry> list5 = world.func_175712_a(structureboundingbox, false);
/*     */         
/* 226 */         if (list5 != null)
/*     */         {
/* 228 */           for (NextTickListEntry nextticklistentry : list5)
/*     */           {
/* 230 */             if (structureboundingbox.isVecInside(nextticklistentry.position))
/*     */             {
/* 232 */               BlockPos blockpos8 = nextticklistentry.position.add(blockpos3);
/* 233 */               world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 238 */         if (i <= 0)
/*     */         {
/* 240 */           throw new CommandException("commands.clone.failed", new Object[0]);
/*     */         }
/*     */         
/*     */ 
/* 244 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
/* 245 */         notifyOperators(sender, this, "commands.clone.success", new Object[] { Integer.valueOf(i) });
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 250 */         throw new CommandException("commands.clone.outOfWorld", new Object[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 255 */       throw new CommandException("commands.clone.outOfWorld", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 264 */     return (args.length == 12) && ("filtered".equals(args[9])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : args.length == 11 ? getListOfStringsMatchingLastWord(args, new String[] { "normal", "force", "move" }) : args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "masked", "filtered" }) : (args.length > 6) && (args.length <= 9) ? func_175771_a(args, 6, pos) : (args.length > 3) && (args.length <= 6) ? func_175771_a(args, 3, pos) : (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */   
/*     */   static class StaticCloneData
/*     */   {
/*     */     public final BlockPos field_179537_a;
/*     */     public final IBlockState blockState;
/*     */     public final NBTTagCompound field_179536_c;
/*     */     
/*     */     public StaticCloneData(BlockPos p_i46037_1_, IBlockState p_i46037_2_, NBTTagCompound p_i46037_3_)
/*     */     {
/* 275 */       this.field_179537_a = p_i46037_1_;
/* 276 */       this.blockState = p_i46037_2_;
/* 277 */       this.field_179536_c = p_i46037_3_;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandClone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */