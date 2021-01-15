/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.gson.JsonParseException;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S45PacketTitle;
/*     */ import net.minecraft.network.play.server.S45PacketTitle.Type;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentProcessor;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandTitle extends CommandBase
/*     */ {
/*  17 */   private static final Logger LOGGER = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/*  24 */     return "title";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  32 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  40 */     return "commands.title.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  48 */     if (args.length < 2)
/*     */     {
/*  50 */       throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  54 */     if (args.length < 3)
/*     */     {
/*  56 */       if (("title".equals(args[1])) || ("subtitle".equals(args[1])))
/*     */       {
/*  58 */         throw new WrongUsageException("commands.title.usage.title", new Object[0]);
/*     */       }
/*     */       
/*  61 */       if ("times".equals(args[1]))
/*     */       {
/*  63 */         throw new WrongUsageException("commands.title.usage.times", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*  67 */     EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);
/*  68 */     S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName(args[1]);
/*     */     
/*  70 */     if ((s45packettitle$type != S45PacketTitle.Type.CLEAR) && (s45packettitle$type != S45PacketTitle.Type.RESET))
/*     */     {
/*  72 */       if (s45packettitle$type == S45PacketTitle.Type.TIMES)
/*     */       {
/*  74 */         if (args.length != 5)
/*     */         {
/*  76 */           throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */         }
/*     */         
/*     */ 
/*  80 */         int i = parseInt(args[2]);
/*  81 */         int j = parseInt(args[3]);
/*  82 */         int k = parseInt(args[4]);
/*  83 */         S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
/*  84 */         entityplayermp.playerNetServerHandler.sendPacket(s45packettitle2);
/*  85 */         notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */       }
/*     */       else {
/*  88 */         if (args.length < 3)
/*     */         {
/*  90 */           throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */         }
/*     */         
/*     */ 
/*  94 */         String s = buildString(args, 2);
/*     */         
/*     */ 
/*     */         try
/*     */         {
/*  99 */           ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/*     */         }
/*     */         catch (JsonParseException jsonparseexception) {
/*     */           IChatComponent ichatcomponent;
/* 103 */           Throwable throwable = org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(jsonparseexception);
/* 104 */           throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { throwable == null ? "" : throwable.getMessage() });
/*     */         }
/*     */         IChatComponent ichatcomponent;
/* 107 */         S45PacketTitle s45packettitle1 = new S45PacketTitle(s45packettitle$type, ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayermp));
/* 108 */         entityplayermp.playerNetServerHandler.sendPacket(s45packettitle1);
/* 109 */         notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */       }
/*     */     } else {
/* 112 */       if (args.length != 2)
/*     */       {
/* 114 */         throw new WrongUsageException("commands.title.usage", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 118 */       S45PacketTitle s45packettitle = new S45PacketTitle(s45packettitle$type, null);
/* 119 */       entityplayermp.playerNetServerHandler.sendPacket(s45packettitle);
/* 120 */       notifyOperators(sender, this, "commands.title.success", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 127 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 135 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */