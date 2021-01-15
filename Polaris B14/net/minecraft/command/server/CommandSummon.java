/*     */ package net.minecraft.command.server;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.command.WrongUsageException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandSummon
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  28 */     return "summon";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  36 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  44 */     return "commands.summon.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  52 */     if (args.length < 1)
/*     */     {
/*  54 */       throw new WrongUsageException("commands.summon.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  58 */     String s = args[0];
/*  59 */     BlockPos blockpos = sender.getPosition();
/*  60 */     Vec3 vec3 = sender.getPositionVector();
/*  61 */     double d0 = vec3.xCoord;
/*  62 */     double d1 = vec3.yCoord;
/*  63 */     double d2 = vec3.zCoord;
/*     */     
/*  65 */     if (args.length >= 4)
/*     */     {
/*  67 */       d0 = parseDouble(d0, args[1], true);
/*  68 */       d1 = parseDouble(d1, args[2], false);
/*  69 */       d2 = parseDouble(d2, args[3], true);
/*  70 */       blockpos = new BlockPos(d0, d1, d2);
/*     */     }
/*     */     
/*  73 */     World world = sender.getEntityWorld();
/*     */     
/*  75 */     if (!world.isBlockLoaded(blockpos))
/*     */     {
/*  77 */       throw new CommandException("commands.summon.outOfWorld", new Object[0]);
/*     */     }
/*  79 */     if ("LightningBolt".equals(s))
/*     */     {
/*  81 */       world.addWeatherEffect(new EntityLightningBolt(world, d0, d1, d2));
/*  82 */       notifyOperators(sender, this, "commands.summon.success", new Object[0]);
/*     */     }
/*     */     else
/*     */     {
/*  86 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  87 */       boolean flag = false;
/*     */       
/*  89 */       if (args.length >= 5)
/*     */       {
/*  91 */         IChatComponent ichatcomponent = getChatComponentFromNthArg(sender, args, 4);
/*     */         
/*     */         try
/*     */         {
/*  95 */           nbttagcompound = JsonToNBT.getTagFromJson(ichatcomponent.getUnformattedText());
/*  96 */           flag = true;
/*     */         }
/*     */         catch (NBTException nbtexception)
/*     */         {
/* 100 */           throw new CommandException("commands.summon.tagError", new Object[] { nbtexception.getMessage() });
/*     */         }
/*     */       }
/*     */       
/* 104 */       nbttagcompound.setString("id", s);
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 109 */         entity2 = EntityList.createEntityFromNBT(nbttagcompound, world);
/*     */       }
/*     */       catch (RuntimeException var19) {
/*     */         Entity entity2;
/* 113 */         throw new CommandException("commands.summon.failed", new Object[0]);
/*     */       }
/*     */       Entity entity2;
/* 116 */       if (entity2 == null)
/*     */       {
/* 118 */         throw new CommandException("commands.summon.failed", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 122 */       entity2.setLocationAndAngles(d0, d1, d2, entity2.rotationYaw, entity2.rotationPitch);
/*     */       
/* 124 */       if ((!flag) && ((entity2 instanceof EntityLiving)))
/*     */       {
/* 126 */         ((EntityLiving)entity2).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity2)), null);
/*     */       }
/*     */       
/* 129 */       world.spawnEntityInWorld(entity2);
/* 130 */       Entity entity = entity2;
/*     */       
/* 132 */       for (NBTTagCompound nbttagcompound1 = nbttagcompound; (entity != null) && (nbttagcompound1.hasKey("Riding", 10)); nbttagcompound1 = nbttagcompound1.getCompoundTag("Riding"))
/*     */       {
/* 134 */         Entity entity1 = EntityList.createEntityFromNBT(nbttagcompound1.getCompoundTag("Riding"), world);
/*     */         
/* 136 */         if (entity1 != null)
/*     */         {
/* 138 */           entity1.setLocationAndAngles(d0, d1, d2, entity1.rotationYaw, entity1.rotationPitch);
/* 139 */           world.spawnEntityInWorld(entity1);
/* 140 */           entity.mountEntity(entity1);
/*     */         }
/*     */         
/* 143 */         entity = entity1;
/*     */       }
/*     */       
/* 146 */       notifyOperators(sender, this, "commands.summon.success", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 154 */     return (args.length > 1) && (args.length <= 4) ? func_175771_a(args, 1, pos) : args.length == 1 ? getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList()) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\server\CommandSummon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */