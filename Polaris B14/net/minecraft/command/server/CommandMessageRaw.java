/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.SyntaxErrorException;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentProcessor;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IChatComponent.Serializer;
/*    */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*    */ 
/*    */ 
/*    */ public class CommandMessageRaw
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 24 */     return "tellraw";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 32 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 40 */     return "commands.tellraw.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 48 */     if (args.length < 2)
/*    */     {
/* 50 */       throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 54 */     EntityPlayer entityplayer = getPlayer(sender, args[0]);
/* 55 */     String s = buildString(args, 1);
/*    */     
/*    */     try
/*    */     {
/* 59 */       IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/* 60 */       entityplayer.addChatMessage(ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayer));
/*    */     }
/*    */     catch (JsonParseException jsonparseexception)
/*    */     {
/* 64 */       Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
/* 65 */       throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { throwable == null ? "" : throwable.getMessage() });
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 72 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsernameIndex(String[] args, int index)
/*    */   {
/* 80 */     return index == 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandMessageRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */