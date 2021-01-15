/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.SyntaxErrorException;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.BanList;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class CommandPardonIp extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 20 */     return "pardon-ip";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 28 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender)
/*    */   {
/* 36 */     return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer()) && (super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 44 */     return "commands.unbanip.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 52 */     if ((args.length == 1) && (args[0].length() > 1))
/*    */     {
/* 54 */       Matcher matcher = CommandBanIp.field_147211_a.matcher(args[0]);
/*    */       
/* 56 */       if (matcher.matches())
/*    */       {
/* 58 */         MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(args[0]);
/* 59 */         notifyOperators(sender, this, "commands.unbanip.success", new Object[] { args[0] });
/*    */       }
/*    */       else
/*    */       {
/* 63 */         throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 68 */       throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 74 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandPardonIp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */