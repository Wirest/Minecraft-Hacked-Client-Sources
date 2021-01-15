/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ public class CommandPlaySound
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  17 */     return "playsound";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  25 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  33 */     return "commands.playsound.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  41 */     if (args.length < 2)
/*     */     {
/*  43 */       throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  47 */     int i = 0;
/*  48 */     String s = args[(i++)];
/*  49 */     EntityPlayerMP entityplayermp = getPlayer(sender, args[(i++)]);
/*  50 */     Vec3 vec3 = sender.getPositionVector();
/*  51 */     double d0 = vec3.xCoord;
/*     */     
/*  53 */     if (args.length > i)
/*     */     {
/*  55 */       d0 = parseDouble(d0, args[(i++)], true);
/*     */     }
/*     */     
/*  58 */     double d1 = vec3.yCoord;
/*     */     
/*  60 */     if (args.length > i)
/*     */     {
/*  62 */       d1 = parseDouble(d1, args[(i++)], 0, 0, false);
/*     */     }
/*     */     
/*  65 */     double d2 = vec3.zCoord;
/*     */     
/*  67 */     if (args.length > i)
/*     */     {
/*  69 */       d2 = parseDouble(d2, args[(i++)], true);
/*     */     }
/*     */     
/*  72 */     double d3 = 1.0D;
/*     */     
/*  74 */     if (args.length > i)
/*     */     {
/*  76 */       d3 = parseDouble(args[(i++)], 0.0D, 3.4028234663852886E38D);
/*     */     }
/*     */     
/*  79 */     double d4 = 1.0D;
/*     */     
/*  81 */     if (args.length > i)
/*     */     {
/*  83 */       d4 = parseDouble(args[(i++)], 0.0D, 2.0D);
/*     */     }
/*     */     
/*  86 */     double d5 = 0.0D;
/*     */     
/*  88 */     if (args.length > i)
/*     */     {
/*  90 */       d5 = parseDouble(args[i], 0.0D, 1.0D);
/*     */     }
/*     */     
/*  93 */     double d6 = d3 > 1.0D ? d3 * 16.0D : 16.0D;
/*  94 */     double d7 = entityplayermp.getDistance(d0, d1, d2);
/*     */     
/*  96 */     if (d7 > d6)
/*     */     {
/*  98 */       if (d5 <= 0.0D)
/*     */       {
/* 100 */         throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayermp.getName() });
/*     */       }
/*     */       
/* 103 */       double d8 = d0 - entityplayermp.posX;
/* 104 */       double d9 = d1 - entityplayermp.posY;
/* 105 */       double d10 = d2 - entityplayermp.posZ;
/* 106 */       double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
/*     */       
/* 108 */       if (d11 > 0.0D)
/*     */       {
/* 110 */         d0 = entityplayermp.posX + d8 / d11 * 2.0D;
/* 111 */         d1 = entityplayermp.posY + d9 / d11 * 2.0D;
/* 112 */         d2 = entityplayermp.posZ + d10 / d11 * 2.0D;
/*     */       }
/*     */       
/* 115 */       d3 = d5;
/*     */     }
/*     */     
/* 118 */     entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(s, d0, d1, d2, (float)d3, (float)d4));
/* 119 */     notifyOperators(sender, this, "commands.playsound.success", new Object[] { s, entityplayermp.getName() });
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 125 */     return (args.length > 2) && (args.length <= 5) ? func_175771_a(args, 2, pos) : args.length == 2 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 133 */     return index == 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandPlaySound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */