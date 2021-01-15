/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandBase.CoordinateArg;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class CommandTeleport
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  24 */     return "tp";
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
/*  40 */     return "commands.tp.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  48 */     if (args.length < 1)
/*     */     {
/*  50 */       throw new WrongUsageException("commands.tp.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  54 */     int i = 0;
/*     */     Entity entity;
/*     */     Entity entity;
/*  57 */     if ((args.length != 2) && (args.length != 4) && (args.length != 6))
/*     */     {
/*  59 */       entity = getCommandSenderAsPlayer(sender);
/*     */     }
/*     */     else
/*     */     {
/*  63 */       entity = func_175768_b(sender, args[0]);
/*  64 */       i = 1;
/*     */     }
/*     */     
/*  67 */     if ((args.length != 1) && (args.length != 2))
/*     */     {
/*  69 */       if (args.length < i + 3)
/*     */       {
/*  71 */         throw new WrongUsageException("commands.tp.usage", new Object[0]);
/*     */       }
/*  73 */       if (entity.worldObj != null)
/*     */       {
/*  75 */         int lvt_5_2_ = i + 1;
/*  76 */         CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(entity.posX, args[i], true);
/*  77 */         CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(entity.posY, args[(lvt_5_2_++)], 0, 0, false);
/*  78 */         CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(entity.posZ, args[(lvt_5_2_++)], true);
/*  79 */         CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate(entity.rotationYaw, args.length > lvt_5_2_ ? args[(lvt_5_2_++)] : "~", false);
/*  80 */         CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(entity.rotationPitch, args.length > lvt_5_2_ ? args[lvt_5_2_] : "~", false);
/*     */         
/*  82 */         if ((entity instanceof EntityPlayerMP))
/*     */         {
/*  84 */           Set<S08PacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
/*     */           
/*  86 */           if (commandbase$coordinatearg.func_179630_c())
/*     */           {
/*  88 */             set.add(S08PacketPlayerPosLook.EnumFlags.X);
/*     */           }
/*     */           
/*  91 */           if (commandbase$coordinatearg1.func_179630_c())
/*     */           {
/*  93 */             set.add(S08PacketPlayerPosLook.EnumFlags.Y);
/*     */           }
/*     */           
/*  96 */           if (commandbase$coordinatearg2.func_179630_c())
/*     */           {
/*  98 */             set.add(S08PacketPlayerPosLook.EnumFlags.Z);
/*     */           }
/*     */           
/* 101 */           if (commandbase$coordinatearg4.func_179630_c())
/*     */           {
/* 103 */             set.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
/*     */           }
/*     */           
/* 106 */           if (commandbase$coordinatearg3.func_179630_c())
/*     */           {
/* 108 */             set.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
/*     */           }
/*     */           
/* 111 */           float f = (float)commandbase$coordinatearg3.func_179629_b();
/*     */           
/* 113 */           if (!commandbase$coordinatearg3.func_179630_c())
/*     */           {
/* 115 */             f = MathHelper.wrapAngleTo180_float(f);
/*     */           }
/*     */           
/* 118 */           float f1 = (float)commandbase$coordinatearg4.func_179629_b();
/*     */           
/* 120 */           if (!commandbase$coordinatearg4.func_179630_c())
/*     */           {
/* 122 */             f1 = MathHelper.wrapAngleTo180_float(f1);
/*     */           }
/*     */           
/* 125 */           if ((f1 > 90.0F) || (f1 < -90.0F))
/*     */           {
/* 127 */             f1 = MathHelper.wrapAngleTo180_float(180.0F - f1);
/* 128 */             f = MathHelper.wrapAngleTo180_float(f + 180.0F);
/*     */           }
/*     */           
/* 131 */           entity.mountEntity(null);
/* 132 */           ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg1.func_179629_b(), commandbase$coordinatearg2.func_179629_b(), f, f1, set);
/* 133 */           entity.setRotationYawHead(f);
/*     */         }
/*     */         else
/*     */         {
/* 137 */           float f2 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg3.func_179628_a());
/* 138 */           float f3 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg4.func_179628_a());
/*     */           
/* 140 */           if ((f3 > 90.0F) || (f3 < -90.0F))
/*     */           {
/* 142 */             f3 = MathHelper.wrapAngleTo180_float(180.0F - f3);
/* 143 */             f2 = MathHelper.wrapAngleTo180_float(f2 + 180.0F);
/*     */           }
/*     */           
/* 146 */           entity.setLocationAndAngles(commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), f2, f3);
/* 147 */           entity.setRotationYawHead(f2);
/*     */         }
/*     */         
/* 150 */         notifyOperators(sender, this, "commands.tp.success.coordinates", new Object[] { entity.getName(), Double.valueOf(commandbase$coordinatearg.func_179628_a()), Double.valueOf(commandbase$coordinatearg1.func_179628_a()), Double.valueOf(commandbase$coordinatearg2.func_179628_a()) });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 155 */       Entity entity1 = func_175768_b(sender, args[(args.length - 1)]);
/*     */       
/* 157 */       if (entity1.worldObj != entity.worldObj)
/*     */       {
/* 159 */         throw new CommandException("commands.tp.notSameDimension", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 163 */       entity.mountEntity(null);
/*     */       
/* 165 */       if ((entity instanceof EntityPlayerMP))
/*     */       {
/* 167 */         ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
/*     */       }
/*     */       else
/*     */       {
/* 171 */         entity.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
/*     */       }
/*     */       
/* 174 */       notifyOperators(sender, this, "commands.tp.success", new Object[] { entity.getName(), entity1.getName() });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 182 */     return (args.length != 1) && (args.length != 2) ? null : getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 190 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandTeleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */