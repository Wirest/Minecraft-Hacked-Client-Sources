/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.CommandResultStats.Type;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ 
/*    */ 
/*    */ public class CommandListPlayers
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 18 */     return "list";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 26 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 34 */     return "commands.players.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 42 */     int i = MinecraftServer.getServer().getCurrentPlayerCount();
/* 43 */     sender.addChatMessage(new ChatComponentTranslation("commands.players.list", new Object[] { Integer.valueOf(i), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers()) }));
/* 44 */     sender.addChatMessage(new ChatComponentText(MinecraftServer.getServer().getConfigurationManager().func_181058_b((args.length > 0) && ("uuids".equalsIgnoreCase(args[0])))));
/* 45 */     sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandListPlayers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */