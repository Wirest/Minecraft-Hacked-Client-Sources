/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.CommandResultStats.Type;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.NumberInvalidException;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandTestForBlock
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  27 */     return "testforblock";
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
/*  43 */     return "commands.testforblock.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  51 */     if (args.length < 4)
/*     */     {
/*  53 */       throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  57 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  58 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  59 */     Block block = Block.getBlockFromName(args[3]);
/*     */     
/*  61 */     if (block == null)
/*     */     {
/*  63 */       throw new NumberInvalidException("commands.setblock.notFound", new Object[] { args[3] });
/*     */     }
/*     */     
/*     */ 
/*  67 */     int i = -1;
/*     */     
/*  69 */     if (args.length >= 5)
/*     */     {
/*  71 */       i = parseInt(args[4], -1, 15);
/*     */     }
/*     */     
/*  74 */     World world = sender.getEntityWorld();
/*     */     
/*  76 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  78 */       throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  82 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  83 */     boolean flag = false;
/*     */     
/*  85 */     if ((args.length >= 6) && (block.hasTileEntity()))
/*     */     {
/*  87 */       String s = getChatComponentFromNthArg(sender, args, 5).getUnformattedText();
/*     */       
/*     */       try
/*     */       {
/*  91 */         nbttagcompound = JsonToNBT.getTagFromJson(s);
/*  92 */         flag = true;
/*     */       }
/*     */       catch (NBTException nbtexception)
/*     */       {
/*  96 */         throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
/*     */       }
/*     */     }
/*     */     
/* 100 */     IBlockState iblockstate = world.getBlockState(blockpos);
/* 101 */     Block block1 = iblockstate.getBlock();
/*     */     
/* 103 */     if (block1 != block)
/*     */     {
/* 105 */       throw new CommandException("commands.testforblock.failed.tile", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), block1.getLocalizedName(), block.getLocalizedName() });
/*     */     }
/*     */     
/*     */ 
/* 109 */     if (i > -1)
/*     */     {
/* 111 */       int j = iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */       
/* 113 */       if (j != i)
/*     */       {
/* 115 */         throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), Integer.valueOf(j), Integer.valueOf(i) });
/*     */       }
/*     */     }
/*     */     
/* 119 */     if (flag)
/*     */     {
/* 121 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 123 */       if (tileentity == null)
/*     */       {
/* 125 */         throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       
/* 128 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 129 */       tileentity.writeToNBT(nbttagcompound1);
/*     */       
/* 131 */       if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true))
/*     */       {
/* 133 */         throw new CommandException("commands.testforblock.failed.nbt", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */     }
/*     */     
/* 137 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/* 138 */     notifyOperators(sender, this, "commands.testforblock.success", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 147 */     return args.length == 4 ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandTestForBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */