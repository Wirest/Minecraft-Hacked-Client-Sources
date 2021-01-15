/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class CommandExecuteAt extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  20 */     return "execute";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  28 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  36 */     return "commands.execute.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(final ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  44 */     if (args.length < 5)
/*     */     {
/*  46 */       throw new WrongUsageException("commands.execute.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  50 */     final Entity entity = getEntity(sender, args[0], Entity.class);
/*  51 */     final double d0 = parseDouble(entity.posX, args[1], false);
/*  52 */     double d1 = parseDouble(entity.posY, args[2], false);
/*  53 */     final double d2 = parseDouble(entity.posZ, args[3], false);
/*  54 */     final BlockPos blockpos = new BlockPos(d0, d1, d2);
/*  55 */     int i = 4;
/*     */     
/*  57 */     if (("detect".equals(args[4])) && (args.length > 10))
/*     */     {
/*  59 */       World world = entity.getEntityWorld();
/*  60 */       double d3 = parseDouble(d0, args[5], false);
/*  61 */       double d4 = parseDouble(d1, args[6], false);
/*  62 */       double d5 = parseDouble(d2, args[7], false);
/*  63 */       Block block = getBlockByText(sender, args[8]);
/*  64 */       int k = parseInt(args[9], -1, 15);
/*  65 */       BlockPos blockpos1 = new BlockPos(d3, d4, d5);
/*  66 */       IBlockState iblockstate = world.getBlockState(blockpos1);
/*     */       
/*  68 */       if ((iblockstate.getBlock() != block) || ((k >= 0) && (iblockstate.getBlock().getMetaFromState(iblockstate) != k)))
/*     */       {
/*  70 */         throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
/*     */       }
/*     */       
/*  73 */       i = 10;
/*     */     }
/*     */     
/*  76 */     String s = buildString(args, i);
/*  77 */     ICommandSender icommandsender = new ICommandSender()
/*     */     {
/*     */       public String getName()
/*     */       {
/*  81 */         return entity.getName();
/*     */       }
/*     */       
/*     */       public IChatComponent getDisplayName() {
/*  85 */         return entity.getDisplayName();
/*     */       }
/*     */       
/*     */       public void addChatMessage(IChatComponent component) {
/*  89 */         sender.addChatMessage(component);
/*     */       }
/*     */       
/*     */       public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  93 */         return sender.canCommandSenderUseCommand(permLevel, commandName);
/*     */       }
/*     */       
/*     */       public BlockPos getPosition() {
/*  97 */         return blockpos;
/*     */       }
/*     */       
/*     */       public Vec3 getPositionVector() {
/* 101 */         return new Vec3(d0, d2, this.val$d2);
/*     */       }
/*     */       
/*     */       public World getEntityWorld() {
/* 105 */         return entity.worldObj;
/*     */       }
/*     */       
/*     */       public Entity getCommandSenderEntity() {
/* 109 */         return entity;
/*     */       }
/*     */       
/*     */       public boolean sendCommandFeedback() {
/* 113 */         MinecraftServer minecraftserver = MinecraftServer.getServer();
/* 114 */         return (minecraftserver == null) || (minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
/*     */       }
/*     */       
/*     */       public void setCommandStat(CommandResultStats.Type type, int amount) {
/* 118 */         entity.setCommandStat(type, amount);
/*     */       }
/* 120 */     };
/* 121 */     ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();
/*     */     
/*     */     try
/*     */     {
/* 125 */       int j = icommandmanager.executeCommand(icommandsender, s);
/*     */       
/* 127 */       if (j < 1)
/*     */       {
/* 129 */         throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s });
/*     */       }
/*     */     }
/*     */     catch (Throwable var23)
/*     */     {
/* 134 */       throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName() });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 141 */     return (args.length == 9) && ("detect".equals(args[4])) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : (args.length > 5) && (args.length <= 8) && ("detect".equals(args[4])) ? func_175771_a(args, 5, pos) : (args.length > 1) && (args.length <= 4) ? func_175771_a(args, 1, pos) : args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 149 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandExecuteAt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */