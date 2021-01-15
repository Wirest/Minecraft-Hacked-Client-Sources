/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ 
/*     */ 
/*     */ public class CommandEffect
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  18 */     return "effect";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  26 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  34 */     return "commands.effect.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  42 */     if (args.length < 2)
/*     */     {
/*  44 */       throw new WrongUsageException("commands.effect.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  48 */     EntityLivingBase entitylivingbase = (EntityLivingBase)getEntity(sender, args[0], EntityLivingBase.class);
/*     */     
/*  50 */     if (args[1].equals("clear"))
/*     */     {
/*  52 */       if (entitylivingbase.getActivePotionEffects().isEmpty())
/*     */       {
/*  54 */         throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entitylivingbase.getName() });
/*     */       }
/*     */       
/*     */ 
/*  58 */       entitylivingbase.clearActivePotions();
/*  59 */       notifyOperators(sender, this, "commands.effect.success.removed.all", new Object[] { entitylivingbase.getName() });
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/*     */       
/*     */ 
/*     */       try
/*     */       {
/*  68 */         i = parseInt(args[1], 1);
/*     */       }
/*     */       catch (NumberInvalidException numberinvalidexception) {
/*     */         int i;
/*  72 */         Potion potion = Potion.getPotionFromResourceLocation(args[1]);
/*     */         
/*  74 */         if (potion == null)
/*     */         {
/*  76 */           throw numberinvalidexception;
/*     */         }
/*     */         
/*  79 */         i = potion.id;
/*     */       }
/*     */       
/*  82 */       int j = 600;
/*  83 */       int l = 30;
/*  84 */       int k = 0;
/*     */       
/*  86 */       if ((i >= 0) && (i < Potion.potionTypes.length) && (Potion.potionTypes[i] != null))
/*     */       {
/*  88 */         Potion potion1 = Potion.potionTypes[i];
/*     */         
/*  90 */         if (args.length >= 3)
/*     */         {
/*  92 */           l = parseInt(args[2], 0, 1000000);
/*     */           
/*  94 */           if (potion1.isInstant())
/*     */           {
/*  96 */             j = l;
/*     */           }
/*     */           else
/*     */           {
/* 100 */             j = l * 20;
/*     */           }
/*     */         }
/* 103 */         else if (potion1.isInstant())
/*     */         {
/* 105 */           j = 1;
/*     */         }
/*     */         
/* 108 */         if (args.length >= 4)
/*     */         {
/* 110 */           k = parseInt(args[3], 0, 255);
/*     */         }
/*     */         
/* 113 */         boolean flag = true;
/*     */         
/* 115 */         if ((args.length >= 5) && ("true".equalsIgnoreCase(args[4])))
/*     */         {
/* 117 */           flag = false;
/*     */         }
/*     */         
/* 120 */         if (l > 0)
/*     */         {
/* 122 */           PotionEffect potioneffect = new PotionEffect(i, j, k, false, flag);
/* 123 */           entitylivingbase.addPotionEffect(potioneffect);
/* 124 */           notifyOperators(sender, this, "commands.effect.success", new Object[] { new ChatComponentTranslation(potioneffect.getEffectName(), new Object[0]), Integer.valueOf(i), Integer.valueOf(k), entitylivingbase.getName(), Integer.valueOf(l) });
/*     */         }
/* 126 */         else if (entitylivingbase.isPotionActive(i))
/*     */         {
/* 128 */           entitylivingbase.removePotionEffect(i);
/* 129 */           notifyOperators(sender, this, "commands.effect.success.removed", new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName() });
/*     */         }
/*     */         else
/*     */         {
/* 133 */           throw new CommandException("commands.effect.failure.notActive", new Object[] { new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName() });
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 138 */         throw new NumberInvalidException("commands.effect.notFound", new Object[] { Integer.valueOf(i) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 146 */     return args.length == 5 ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" }) : args.length == 2 ? getListOfStringsMatchingLastWord(args, Potion.func_181168_c()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, getAllUsernames()) : null;
/*     */   }
/*     */   
/*     */   protected String[] getAllUsernames()
/*     */   {
/* 151 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 159 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */