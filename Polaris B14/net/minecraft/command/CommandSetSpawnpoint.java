/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandSetSpawnpoint
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 15 */     return "spawnpoint";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 23 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 31 */     return "commands.spawnpoint.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 39 */     if ((args.length > 1) && (args.length < 4))
/*    */     {
/* 41 */       throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 45 */     EntityPlayerMP entityplayermp = args.length > 0 ? getPlayer(sender, args[0]) : getCommandSenderAsPlayer(sender);
/* 46 */     BlockPos blockpos = args.length > 3 ? parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();
/*    */     
/* 48 */     if (entityplayermp.worldObj != null)
/*    */     {
/* 50 */       entityplayermp.setSpawnPoint(blockpos, true);
/* 51 */       notifyOperators(sender, this, "commands.spawnpoint.success", new Object[] { entityplayermp.getName(), Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 58 */     return (args.length > 1) && (args.length <= 4) ? func_175771_a(args, 1, pos) : args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsernameIndex(String[] args, int index)
/*    */   {
/* 66 */     return index == 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandSetSpawnpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */