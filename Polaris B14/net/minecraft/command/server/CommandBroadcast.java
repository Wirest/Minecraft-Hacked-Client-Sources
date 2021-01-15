/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class CommandBroadcast
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 20 */     return "say";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 28 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 36 */     return "commands.say.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 44 */     if ((args.length > 0) && (args[0].length() > 0))
/*    */     {
/* 46 */       IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 0, true);
/* 47 */       MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.announcement", new Object[] { sender.getDisplayName(), ichatcomponent }));
/*    */     }
/*    */     else
/*    */     {
/* 51 */       throw new WrongUsageException("commands.say.usage", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 57 */     return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandBroadcast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */