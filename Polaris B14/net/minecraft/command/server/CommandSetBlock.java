/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.CommandResultStats.Type;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
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
/*     */ public class CommandSetBlock
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  27 */     return "setblock";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  35 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  43 */     return "commands.setblock.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  51 */     if (args.length < 4)
/*     */     {
/*  53 */       throw new WrongUsageException("commands.setblock.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  57 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  58 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  59 */     Block block = CommandBase.getBlockByText(sender, args[3]);
/*  60 */     int i = 0;
/*     */     
/*  62 */     if (args.length >= 5)
/*     */     {
/*  64 */       i = parseInt(args[4], 0, 15);
/*     */     }
/*     */     
/*  67 */     World world = sender.getEntityWorld();
/*     */     
/*  69 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  71 */       throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  75 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  76 */     boolean flag = false;
/*     */     
/*  78 */     if ((args.length >= 7) && (block.hasTileEntity()))
/*     */     {
/*  80 */       String s = getChatComponentFromNthArg(sender, args, 6).getUnformattedText();
/*     */       
/*     */       try
/*     */       {
/*  84 */         nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  85 */         flag = true;
/*     */       }
/*     */       catch (NBTException nbtexception)
/*     */       {
/*  89 */         throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
/*     */       }
/*     */     }
/*     */     
/*  93 */     if (args.length >= 6)
/*     */     {
/*  95 */       if (args[5].equals("destroy"))
/*     */       {
/*  97 */         world.destroyBlock(blockpos, true);
/*     */         
/*  99 */         if (block == Blocks.air)
/*     */         {
/* 101 */           notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
/*     */         }
/*     */         
/*     */       }
/* 105 */       else if ((args[5].equals("keep")) && (!world.isAirBlock(blockpos)))
/*     */       {
/* 107 */         throw new CommandException("commands.setblock.noChange", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/* 111 */     TileEntity tileentity1 = world.getTileEntity(blockpos);
/*     */     
/* 113 */     if (tileentity1 != null)
/*     */     {
/* 115 */       if ((tileentity1 instanceof IInventory))
/*     */       {
/* 117 */         ((IInventory)tileentity1).clear();
/*     */       }
/*     */       
/* 120 */       world.setBlockState(blockpos, Blocks.air.getDefaultState(), block == Blocks.air ? 2 : 4);
/*     */     }
/*     */     
/* 123 */     IBlockState iblockstate = block.getStateFromMeta(i);
/*     */     
/* 125 */     if (!world.setBlockState(blockpos, iblockstate, 2))
/*     */     {
/* 127 */       throw new CommandException("commands.setblock.noChange", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 131 */     if (flag)
/*     */     {
/* 133 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 135 */       if (tileentity != null)
/*     */       {
/* 137 */         nbttagcompound.setInteger("x", blockpos.getX());
/* 138 */         nbttagcompound.setInteger("y", blockpos.getY());
/* 139 */         nbttagcompound.setInteger("z", blockpos.getZ());
/* 140 */         tileentity.readFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 144 */     world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock());
/* 145 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/* 146 */     notifyOperators(sender, this, "commands.setblock.success", new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 154 */     return args.length == 6 ? getListOfStringsMatchingLastWord(args, new String[] { "replace", "destroy", "keep" }) : args.length == 4 ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandSetBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */