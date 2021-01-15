/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandFill
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  23 */     return "fill";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  31 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  39 */     return "commands.fill.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  47 */     if (args.length < 7)
/*     */     {
/*  49 */       throw new WrongUsageException("commands.fill.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  53 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  54 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  55 */     BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
/*  56 */     Block block = CommandBase.getBlockByText(sender, args[6]);
/*  57 */     int i = 0;
/*     */     
/*  59 */     if (args.length >= 8)
/*     */     {
/*  61 */       i = parseInt(args[7], 0, 15);
/*     */     }
/*     */     
/*  64 */     BlockPos blockpos2 = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
/*  65 */     BlockPos blockpos3 = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
/*  66 */     int j = (blockpos3.getX() - blockpos2.getX() + 1) * (blockpos3.getY() - blockpos2.getY() + 1) * (blockpos3.getZ() - blockpos2.getZ() + 1);
/*     */     
/*  68 */     if (j > 32768)
/*     */     {
/*  70 */       throw new CommandException("commands.fill.tooManyBlocks", new Object[] { Integer.valueOf(j), Integer.valueOf(32768) });
/*     */     }
/*  72 */     if ((blockpos2.getY() >= 0) && (blockpos3.getY() < 256))
/*     */     {
/*  74 */       World world = sender.getEntityWorld();
/*     */       
/*  76 */       for (int k = blockpos2.getZ(); k < blockpos3.getZ() + 16; k += 16)
/*     */       {
/*  78 */         for (int l = blockpos2.getX(); l < blockpos3.getX() + 16; l += 16)
/*     */         {
/*  80 */           if (!world.isBlockLoaded(new BlockPos(l, blockpos3.getY() - blockpos2.getY(), k)))
/*     */           {
/*  82 */             throw new CommandException("commands.fill.outOfWorld", new Object[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  87 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  88 */       boolean flag = false;
/*     */       
/*  90 */       if ((args.length >= 10) && (block.hasTileEntity()))
/*     */       {
/*  92 */         String s = getChatComponentFromNthArg(sender, args, 9).getUnformattedText();
/*     */         
/*     */         try
/*     */         {
/*  96 */           nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  97 */           flag = true;
/*     */         }
/*     */         catch (NBTException nbtexception)
/*     */         {
/* 101 */           throw new CommandException("commands.fill.tagError", new Object[] { nbtexception.getMessage() });
/*     */         }
/*     */       }
/*     */       
/* 105 */       List<BlockPos> list = Lists.newArrayList();
/* 106 */       j = 0;
/*     */       int j1;
/* 108 */       for (int i1 = blockpos2.getZ(); i1 <= blockpos3.getZ(); i1++)
/*     */       {
/* 110 */         for (j1 = blockpos2.getY(); j1 <= blockpos3.getY(); j1++)
/*     */         {
/* 112 */           for (int k1 = blockpos2.getX(); k1 <= blockpos3.getX(); k1++)
/*     */           {
/* 114 */             BlockPos blockpos4 = new BlockPos(k1, j1, i1);
/*     */             
/* 116 */             if (args.length >= 9)
/*     */             {
/* 118 */               if ((!args[8].equals("outline")) && (!args[8].equals("hollow")))
/*     */               {
/* 120 */                 if (args[8].equals("destroy"))
/*     */                 {
/* 122 */                   world.destroyBlock(blockpos4, true);
/*     */                 }
/* 124 */                 else if (args[8].equals("keep"))
/*     */                 {
/* 126 */                   if (!world.isAirBlock(blockpos4)) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                 }
/* 131 */                 else if ((args[8].equals("replace")) && (!block.hasTileEntity()))
/*     */                 {
/* 133 */                   if (args.length > 9)
/*     */                   {
/* 135 */                     Block block1 = CommandBase.getBlockByText(sender, args[9]);
/*     */                     
/* 137 */                     if (world.getBlockState(blockpos4).getBlock() != block1) {
/*     */                       continue;
/*     */                     }
/*     */                   }
/*     */                   
/*     */ 
/* 143 */                   if (args.length > 10)
/*     */                   {
/* 145 */                     int l1 = CommandBase.parseInt(args[10]);
/* 146 */                     IBlockState iblockstate = world.getBlockState(blockpos4);
/*     */                     
/* 148 */                     if (iblockstate.getBlock().getMetaFromState(iblockstate) != l1) {
/*     */                       continue;
/*     */                     }
/*     */                     
/*     */                   }
/*     */                 }
/*     */               }
/* 155 */               else if ((k1 != blockpos2.getX()) && (k1 != blockpos3.getX()) && (j1 != blockpos2.getY()) && (j1 != blockpos3.getY()) && (i1 != blockpos2.getZ()) && (i1 != blockpos3.getZ()))
/*     */               {
/* 157 */                 if (!args[8].equals("hollow"))
/*     */                   continue;
/* 159 */                 world.setBlockState(blockpos4, Blocks.air.getDefaultState(), 2);
/* 160 */                 list.add(blockpos4);
/*     */                 
/*     */ 
/* 163 */                 continue;
/*     */               }
/*     */             }
/*     */             
/* 167 */             TileEntity tileentity1 = world.getTileEntity(blockpos4);
/*     */             
/* 169 */             if (tileentity1 != null)
/*     */             {
/* 171 */               if ((tileentity1 instanceof IInventory))
/*     */               {
/* 173 */                 ((IInventory)tileentity1).clear();
/*     */               }
/*     */               
/* 176 */               world.setBlockState(blockpos4, Blocks.barrier.getDefaultState(), block == Blocks.barrier ? 2 : 4);
/*     */             }
/*     */             
/* 179 */             IBlockState iblockstate1 = block.getStateFromMeta(i);
/*     */             
/* 181 */             if (world.setBlockState(blockpos4, iblockstate1, 2))
/*     */             {
/* 183 */               list.add(blockpos4);
/* 184 */               j++;
/*     */               
/* 186 */               if (flag)
/*     */               {
/* 188 */                 TileEntity tileentity = world.getTileEntity(blockpos4);
/*     */                 
/* 190 */                 if (tileentity != null)
/*     */                 {
/* 192 */                   nbttagcompound.setInteger("x", blockpos4.getX());
/* 193 */                   nbttagcompound.setInteger("y", blockpos4.getY());
/* 194 */                   nbttagcompound.setInteger("z", blockpos4.getZ());
/* 195 */                   tileentity.readFromNBT(nbttagcompound);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 203 */       for (BlockPos blockpos5 : list)
/*     */       {
/* 205 */         Block block2 = world.getBlockState(blockpos5).getBlock();
/* 206 */         world.notifyNeighborsRespectDebug(blockpos5, block2);
/*     */       }
/*     */       
/* 209 */       if (j <= 0)
/*     */       {
/* 211 */         throw new CommandException("commands.fill.failed", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 215 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
/* 216 */       notifyOperators(sender, this, "commands.fill.success", new Object[] { Integer.valueOf(j) });
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 221 */       throw new CommandException("commands.fill.outOfWorld", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 228 */     return (args.length == 10) && ("replace".equals(args[8])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : args.length == 9 ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "destroy", "keep", "hollow", "outline" }) : args.length == 7 ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length > 3) && (args.length <= 6) ? func_175771_a(args, 3, pos) : (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandFill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */