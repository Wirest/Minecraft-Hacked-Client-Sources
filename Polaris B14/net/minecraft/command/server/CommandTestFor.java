/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.nbt.JsonToNBT;
/*    */ import net.minecraft.nbt.NBTException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTUtil;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandTestFor
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 23 */     return "testfor";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 31 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 39 */     return "commands.testfor.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 47 */     if (args.length < 1)
/*    */     {
/* 49 */       throw new WrongUsageException("commands.testfor.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 53 */     Entity entity = func_175768_b(sender, args[0]);
/* 54 */     NBTTagCompound nbttagcompound = null;
/*    */     
/* 56 */     if (args.length >= 2)
/*    */     {
/*    */       try
/*    */       {
/* 60 */         nbttagcompound = JsonToNBT.getTagFromJson(buildString(args, 1));
/*    */       }
/*    */       catch (NBTException nbtexception)
/*    */       {
/* 64 */         throw new CommandException("commands.testfor.tagError", new Object[] { nbtexception.getMessage() });
/*    */       }
/*    */     }
/*    */     
/* 68 */     if (nbttagcompound != null)
/*    */     {
/* 70 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 71 */       entity.writeToNBT(nbttagcompound1);
/*    */       
/* 73 */       if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true))
/*    */       {
/* 75 */         throw new CommandException("commands.testfor.failure", new Object[] { entity.getName() });
/*    */       }
/*    */     }
/*    */     
/* 79 */     notifyOperators(sender, this, "commands.testfor.success", new Object[] { entity.getName() });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsernameIndex(String[] args, int index)
/*    */   {
/* 88 */     return index == 0;
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 93 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandTestFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */