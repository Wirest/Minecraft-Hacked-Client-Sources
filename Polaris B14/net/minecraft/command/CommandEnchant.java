/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ 
/*     */ public class CommandEnchant
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  18 */     return "enchant";
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
/*  34 */     return "commands.enchant.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  42 */     if (args.length < 2)
/*     */     {
/*  44 */       throw new WrongUsageException("commands.enchant.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  48 */     EntityPlayer entityplayer = getPlayer(sender, args[0]);
/*  49 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/*     */     
/*     */     int i;
/*     */     try
/*     */     {
/*  54 */       i = parseInt(args[1], 0);
/*     */     }
/*     */     catch (NumberInvalidException numberinvalidexception) {
/*     */       int i;
/*  58 */       Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
/*     */       
/*  60 */       if (enchantment == null)
/*     */       {
/*  62 */         throw numberinvalidexception;
/*     */       }
/*     */       
/*  65 */       i = enchantment.effectId;
/*     */     }
/*     */     
/*  68 */     int j = 1;
/*  69 */     ItemStack itemstack = entityplayer.getCurrentEquippedItem();
/*     */     
/*  71 */     if (itemstack == null)
/*     */     {
/*  73 */       throw new CommandException("commands.enchant.noItem", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  77 */     Enchantment enchantment1 = Enchantment.getEnchantmentById(i);
/*     */     
/*  79 */     if (enchantment1 == null)
/*     */     {
/*  81 */       throw new NumberInvalidException("commands.enchant.notFound", new Object[] { Integer.valueOf(i) });
/*     */     }
/*  83 */     if (!enchantment1.canApply(itemstack))
/*     */     {
/*  85 */       throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  89 */     if (args.length >= 3)
/*     */     {
/*  91 */       j = parseInt(args[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
/*     */     }
/*     */     
/*  94 */     if (itemstack.hasTagCompound())
/*     */     {
/*  96 */       NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
/*     */       
/*  98 */       if (nbttaglist != null)
/*     */       {
/* 100 */         for (int k = 0; k < nbttaglist.tagCount(); k++)
/*     */         {
/* 102 */           int l = nbttaglist.getCompoundTagAt(k).getShort("id");
/*     */           
/* 104 */           if (Enchantment.getEnchantmentById(l) != null)
/*     */           {
/* 106 */             Enchantment enchantment2 = Enchantment.getEnchantmentById(l);
/*     */             
/* 108 */             if (!enchantment2.canApplyTogether(enchantment1))
/*     */             {
/* 110 */               throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment1.getTranslatedName(j), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(k).getShort("lvl")) });
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 117 */     itemstack.addEnchantment(enchantment1, j);
/* 118 */     notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
/* 119 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 127 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, Enchantment.func_181077_c()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, getListOfPlayers()) : null;
/*     */   }
/*     */   
/*     */   protected String[] getListOfPlayers()
/*     */   {
/* 132 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 140 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandEnchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */