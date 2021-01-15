/*    */ package net.minecraft.command.server;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.PlayerNotFoundException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.ChatStyle;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class CommandMessage extends CommandBase
/*    */ {
/*    */   public List<String> getCommandAliases()
/*    */   {
/* 21 */     return Arrays.asList(new String[] { "w", "msg" });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandName()
/*    */   {
/* 29 */     return "tell";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 37 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 45 */     return "commands.message.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 53 */     if (args.length < 2)
/*    */     {
/* 55 */       throw new net.minecraft.command.WrongUsageException("commands.message.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 59 */     EntityPlayer entityplayer = getPlayer(sender, args[0]);
/*    */     
/* 61 */     if (entityplayer == sender)
/*    */     {
/* 63 */       throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 67 */     IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
/* 68 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.message.display.incoming", new Object[] { sender.getDisplayName(), ichatcomponent.createCopy() });
/* 69 */     ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] { entityplayer.getDisplayName(), ichatcomponent.createCopy() });
/* 70 */     chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
/* 71 */     chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
/* 72 */     entityplayer.addChatMessage(chatcomponenttranslation);
/* 73 */     sender.addChatMessage(chatcomponenttranslation1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 80 */     return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUsernameIndex(String[] args, int index)
/*    */   {
/* 88 */     return index == 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */