/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class CommandBlockData
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  18 */     return "blockdata";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  26 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  34 */     return "commands.blockdata.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  42 */     if (args.length < 4)
/*     */     {
/*  44 */       throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  48 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
/*  49 */     BlockPos blockpos = parseBlockPos(sender, args, 0, false);
/*  50 */     World world = sender.getEntityWorld();
/*     */     
/*  52 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  54 */       throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  58 */     TileEntity tileentity = world.getTileEntity(blockpos);
/*     */     
/*  60 */     if (tileentity == null)
/*     */     {
/*  62 */       throw new CommandException("commands.blockdata.notValid", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  66 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  67 */     tileentity.writeToNBT(nbttagcompound);
/*  68 */     NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  73 */       nbttagcompound2 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 3).getUnformattedText());
/*     */     }
/*     */     catch (NBTException nbtexception) {
/*     */       NBTTagCompound nbttagcompound2;
/*  77 */       throw new CommandException("commands.blockdata.tagError", new Object[] { nbtexception.getMessage() });
/*     */     }
/*     */     NBTTagCompound nbttagcompound2;
/*  80 */     nbttagcompound.merge(nbttagcompound2);
/*  81 */     nbttagcompound.setInteger("x", blockpos.getX());
/*  82 */     nbttagcompound.setInteger("y", blockpos.getY());
/*  83 */     nbttagcompound.setInteger("z", blockpos.getZ());
/*     */     
/*  85 */     if (nbttagcompound.equals(nbttagcompound1))
/*     */     {
/*  87 */       throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString() });
/*     */     }
/*     */     
/*     */ 
/*  91 */     tileentity.readFromNBT(nbttagcompound);
/*  92 */     tileentity.markDirty();
/*  93 */     world.markBlockForUpdate(blockpos);
/*  94 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
/*  95 */     notifyOperators(sender, this, "commands.blockdata.success", new Object[] { nbttagcompound.toString() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 104 */     return (args.length > 0) && (args.length <= 3) ? func_175771_a(args, 0, pos) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandBlockData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */