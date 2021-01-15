/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandGive extends CommandBase
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/*  20 */     return "give";
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
/*  36 */     return "commands.give.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  44 */     if (args.length < 2)
/*     */     {
/*  46 */       throw new WrongUsageException("commands.give.usage", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  50 */     EntityPlayer entityplayer = getPlayer(sender, args[0]);
/*  51 */     Item item = getItemByText(sender, args[1]);
/*  52 */     int i = args.length >= 3 ? parseInt(args[2], 1, 64) : 1;
/*  53 */     int j = args.length >= 4 ? parseInt(args[3]) : 0;
/*  54 */     ItemStack itemstack = new ItemStack(item, i, j);
/*     */     
/*  56 */     if (args.length >= 5)
/*     */     {
/*  58 */       String s = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();
/*     */       
/*     */       try
/*     */       {
/*  62 */         itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
/*     */       }
/*     */       catch (NBTException nbtexception)
/*     */       {
/*  66 */         throw new CommandException("commands.give.tagError", new Object[] { nbtexception.getMessage() });
/*     */       }
/*     */     }
/*     */     
/*  70 */     boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);
/*     */     
/*  72 */     if (flag)
/*     */     {
/*  74 */       entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.pop", 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*  75 */       entityplayer.inventoryContainer.detectAndSendChanges();
/*     */     }
/*     */     
/*  78 */     if ((flag) && (itemstack.stackSize <= 0))
/*     */     {
/*  80 */       itemstack.stackSize = 1;
/*  81 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
/*  82 */       EntityItem entityitem1 = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       
/*  84 */       if (entityitem1 != null)
/*     */       {
/*  86 */         entityitem1.func_174870_v();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  91 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.stackSize);
/*  92 */       EntityItem entityitem = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       
/*  94 */       if (entityitem != null)
/*     */       {
/*  96 */         entityitem.setNoPickupDelay();
/*  97 */         entityitem.setOwner(entityplayer.getName());
/*     */       }
/*     */     }
/*     */     
/* 101 */     notifyOperators(sender, this, "commands.give.success", new Object[] { itemstack.getChatComponent(), Integer.valueOf(i), entityplayer.getName() });
/*     */   }
/*     */   
/*     */ 
/*     */   public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 107 */     return args.length == 2 ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, getPlayers()) : null;
/*     */   }
/*     */   
/*     */   protected String[] getPlayers()
/*     */   {
/* 112 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 120 */     return index == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandGive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */