/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandParticle
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  17 */     return "particle";
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
/*  33 */     return "commands.particle.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  41 */     if (args.length < 8)
/*     */     {
/*  43 */       throw new WrongUsageException("commands.particle.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  47 */     boolean flag = false;
/*  48 */     EnumParticleTypes enumparticletypes = null;
/*     */     EnumParticleTypes[] arrayOfEnumParticleTypes;
/*  50 */     int j = (arrayOfEnumParticleTypes = EnumParticleTypes.values()).length; for (int i = 0; i < j; i++) { EnumParticleTypes enumparticletypes1 = arrayOfEnumParticleTypes[i];
/*     */       
/*  52 */       if (enumparticletypes1.hasArguments())
/*     */       {
/*  54 */         if (args[0].startsWith(enumparticletypes1.getParticleName()))
/*     */         {
/*  56 */           flag = true;
/*  57 */           enumparticletypes = enumparticletypes1;
/*  58 */           break;
/*     */         }
/*     */       }
/*  61 */       else if (args[0].equals(enumparticletypes1.getParticleName()))
/*     */       {
/*  63 */         flag = true;
/*  64 */         enumparticletypes = enumparticletypes1;
/*  65 */         break;
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (!flag)
/*     */     {
/*  71 */       throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
/*     */     }
/*     */     
/*     */ 
/*  75 */     String s = args[0];
/*  76 */     Vec3 vec3 = sender.getPositionVector();
/*  77 */     double d6 = (float)parseDouble(vec3.xCoord, args[1], true);
/*  78 */     double d0 = (float)parseDouble(vec3.yCoord, args[2], true);
/*  79 */     double d1 = (float)parseDouble(vec3.zCoord, args[3], true);
/*  80 */     double d2 = (float)parseDouble(args[4]);
/*  81 */     double d3 = (float)parseDouble(args[5]);
/*  82 */     double d4 = (float)parseDouble(args[6]);
/*  83 */     double d5 = (float)parseDouble(args[7]);
/*  84 */     int i = 0;
/*     */     
/*  86 */     if (args.length > 8)
/*     */     {
/*  88 */       i = parseInt(args[8], 0);
/*     */     }
/*     */     
/*  91 */     boolean flag1 = false;
/*     */     
/*  93 */     if ((args.length > 9) && ("force".equals(args[9])))
/*     */     {
/*  95 */       flag1 = true;
/*     */     }
/*     */     
/*  98 */     World world = sender.getEntityWorld();
/*     */     
/* 100 */     if ((world instanceof WorldServer))
/*     */     {
/* 102 */       WorldServer worldserver = (WorldServer)world;
/* 103 */       int[] aint = new int[enumparticletypes.getArgumentCount()];
/*     */       
/* 105 */       if (enumparticletypes.hasArguments())
/*     */       {
/* 107 */         String[] astring = args[0].split("_", 3);
/*     */         
/* 109 */         for (int j = 1; j < astring.length; j++)
/*     */         {
/*     */           try
/*     */           {
/* 113 */             aint[(j - 1)] = Integer.parseInt(astring[j]);
/*     */           }
/*     */           catch (NumberFormatException var29)
/*     */           {
/* 117 */             throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 122 */       worldserver.spawnParticle(enumparticletypes, flag1, d6, d0, d1, i, d2, d3, d4, d5, aint);
/* 123 */       notifyOperators(sender, this, "commands.particle.success", new Object[] { s, Integer.valueOf(Math.max(i, 1)) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 131 */     return args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[] { "normal", "force" }) : (args.length > 1) && (args.length <= 4) ? func_175771_a(args, 1, pos) : args.length == 1 ? getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames()) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */