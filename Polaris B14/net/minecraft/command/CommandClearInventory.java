/*     */ package net.minecraft.command;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ 
/*     */ public class CommandClearInventory extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  20 */     return "clear";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  28 */     return "commands.clear.usage";
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
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  44 */     EntityPlayerMP entityplayermp = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[0]);
/*  45 */     Item item = args.length >= 2 ? getItemByText(sender, args[1]) : null;
/*  46 */     int i = args.length >= 3 ? parseInt(args[2], -1) : -1;
/*  47 */     int j = args.length >= 4 ? parseInt(args[3], -1) : -1;
/*  48 */     NBTTagCompound nbttagcompound = null;
/*     */     
/*  50 */     if (args.length >= 5)
/*     */     {
/*     */       try
/*     */       {
/*  54 */         nbttagcompound = JsonToNBT.getTagFromJson(buildString(args, 4));
/*     */       }
/*     */       catch (NBTException nbtexception)
/*     */       {
/*  58 */         throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
/*     */       }
/*     */     }
/*     */     
/*  62 */     if ((args.length >= 2) && (item == null))
/*     */     {
/*  64 */       throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
/*     */     }
/*     */     
/*     */ 
/*  68 */     int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
/*  69 */     entityplayermp.inventoryContainer.detectAndSendChanges();
/*     */     
/*  71 */     if (!entityplayermp.capabilities.isCreativeMode)
/*     */     {
/*  73 */       entityplayermp.updateHeldItem();
/*     */     }
/*     */     
/*  76 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
/*     */     
/*  78 */     if (k == 0)
/*     */     {
/*  80 */       throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
/*     */     }
/*     */     
/*     */ 
/*  84 */     if (j == 0)
/*     */     {
/*  86 */       sender.addChatMessage(new ChatComponentTranslation("commands.clear.testing", new Object[] { entityplayermp.getName(), Integer.valueOf(k) }));
/*     */     }
/*     */     else
/*     */     {
/*  90 */       notifyOperators(sender, this, "commands.clear.success", new Object[] { entityplayermp.getName(), Integer.valueOf(k) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/*  98 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, func_147209_d()) : null;
/*     */   }
/*     */   
/*     */   protected String[] func_147209_d()
/*     */   {
/* 103 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 111 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandClearInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */