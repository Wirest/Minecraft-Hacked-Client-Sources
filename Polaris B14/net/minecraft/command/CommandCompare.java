/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*     */ 
/*     */ 
/*     */ public class CommandCompare
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  19 */     return "testforblocks";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  27 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  35 */     return "commands.compare.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  43 */     if (args.length < 9)
/*     */     {
/*  45 */       throw new WrongUsageException("commands.compare.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  49 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  50 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  51 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  52 */     BlockPos blockpos2 = parseBlockPos(sender, args, 6, false);
/*  53 */     StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
/*  54 */     StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
/*  55 */     int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
/*     */     
/*  57 */     if (i > 524288)
/*     */     {
/*  59 */       throw new CommandException("commands.compare.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(524288) });
/*     */     }
/*  61 */     if ((structureboundingbox.minY >= 0) && (structureboundingbox.maxY < 256) && (structureboundingbox1.minY >= 0) && (structureboundingbox1.maxY < 256))
/*     */     {
/*  63 */       World world = sender.getEntityWorld();
/*     */       
/*  65 */       if ((world.isAreaLoaded(structureboundingbox)) && (world.isAreaLoaded(structureboundingbox1)))
/*     */       {
/*  67 */         boolean flag = false;
/*     */         
/*  69 */         if ((args.length > 9) && (args[9].equals("masked")))
/*     */         {
/*  71 */           flag = true;
/*     */         }
/*     */         
/*  74 */         i = 0;
/*  75 */         BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
/*  76 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*  77 */         BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
/*     */         
/*  79 */         for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; j++)
/*     */         {
/*  81 */           for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; k++)
/*     */           {
/*  83 */             for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; l++)
/*     */             {
/*  85 */               blockpos$mutableblockpos.func_181079_c(l, k, j);
/*  86 */               blockpos$mutableblockpos1.func_181079_c(l + blockpos3.getX(), k + blockpos3.getY(), j + blockpos3.getZ());
/*  87 */               boolean flag1 = false;
/*  88 */               IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);
/*     */               
/*  90 */               if ((!flag) || (iblockstate.getBlock() != Blocks.air))
/*     */               {
/*  92 */                 if (iblockstate == world.getBlockState(blockpos$mutableblockpos1))
/*     */                 {
/*  94 */                   TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);
/*  95 */                   TileEntity tileentity1 = world.getTileEntity(blockpos$mutableblockpos1);
/*     */                   
/*  97 */                   if ((tileentity != null) && (tileentity1 != null))
/*     */                   {
/*  99 */                     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 100 */                     tileentity.writeToNBT(nbttagcompound);
/* 101 */                     nbttagcompound.removeTag("x");
/* 102 */                     nbttagcompound.removeTag("y");
/* 103 */                     nbttagcompound.removeTag("z");
/* 104 */                     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 105 */                     tileentity1.writeToNBT(nbttagcompound1);
/* 106 */                     nbttagcompound1.removeTag("x");
/* 107 */                     nbttagcompound1.removeTag("y");
/* 108 */                     nbttagcompound1.removeTag("z");
/*     */                     
/* 110 */                     if (!nbttagcompound.equals(nbttagcompound1))
/*     */                     {
/* 112 */                       flag1 = true;
/*     */                     }
/*     */                   }
/* 115 */                   else if (tileentity != null)
/*     */                   {
/* 117 */                     flag1 = true;
/*     */                   }
/*     */                 }
/*     */                 else
/*     */                 {
/* 122 */                   flag1 = true;
/*     */                 }
/*     */                 
/* 125 */                 i++;
/*     */                 
/* 127 */                 if (flag1)
/*     */                 {
/* 129 */                   throw new CommandException("commands.compare.failed", new Object[0]);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 136 */         sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
/* 137 */         notifyOperators(sender, this, "commands.compare.success", new Object[] { Integer.valueOf(i) });
/*     */       }
/*     */       else
/*     */       {
/* 141 */         throw new CommandException("commands.compare.outOfWorld", new Object[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 146 */       throw new CommandException("commands.compare.outOfWorld", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 153 */     return args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[] { "masked", "all" }) : (args.length > 6) && (args.length <= 9) ? func_175771_a(args, 6, pos) : (args.length > 3) && (args.length <= 6) ? func_175771_a(args, 3, pos) : (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandCompare.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */