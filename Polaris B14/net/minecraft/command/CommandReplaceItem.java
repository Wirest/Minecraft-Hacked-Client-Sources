/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandReplaceItem extends CommandBase
/*     */ {
/*  22 */   private static final Map<String, Integer> SHORTCUTS = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/*  29 */     return "replaceitem";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRequiredPermissionLevel()
/*     */   {
/*  37 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommandUsage(ICommandSender sender)
/*     */   {
/*  45 */     return "commands.replaceitem.usage";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processCommand(ICommandSender sender, String[] args)
/*     */     throws CommandException
/*     */   {
/*  53 */     if (args.length < 1)
/*     */     {
/*  55 */       throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
/*     */     }
/*     */     
/*     */     boolean flag;
/*     */     
/*     */     boolean flag;
/*  61 */     if (args[0].equals("entity"))
/*     */     {
/*  63 */       flag = false;
/*     */     }
/*     */     else
/*     */     {
/*  67 */       if (!args[0].equals("block"))
/*     */       {
/*  69 */         throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
/*     */       }
/*     */       
/*  72 */       flag = true;
/*     */     }
/*     */     
/*     */     int i;
/*     */     int i;
/*  77 */     if (flag)
/*     */     {
/*  79 */       if (args.length < 6)
/*     */       {
/*  81 */         throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
/*     */       }
/*     */       
/*  84 */       i = 4;
/*     */     }
/*     */     else
/*     */     {
/*  88 */       if (args.length < 4)
/*     */       {
/*  90 */         throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
/*     */       }
/*     */       
/*  93 */       i = 2;
/*     */     }
/*     */     
/*  96 */     int j = getSlotForShortcut(args[(i++)]);
/*     */     
/*     */     Item item;
/*     */     try
/*     */     {
/* 101 */       item = getItemByText(sender, args[i]);
/*     */     }
/*     */     catch (NumberInvalidException numberinvalidexception) {
/*     */       Item item;
/* 105 */       if (Block.getBlockFromName(args[i]) != net.minecraft.init.Blocks.air)
/*     */       {
/* 107 */         throw numberinvalidexception;
/*     */       }
/*     */       
/* 110 */       item = null;
/*     */     }
/*     */     
/* 113 */     i++;
/* 114 */     int k = args.length > i ? parseInt(args[(i++)], 1, 64) : 1;
/* 115 */     int l = args.length > i ? parseInt(args[(i++)]) : 0;
/* 116 */     ItemStack itemstack = new ItemStack(item, k, l);
/*     */     
/* 118 */     if (args.length > i)
/*     */     {
/* 120 */       String s = getChatComponentFromNthArg(sender, args, i).getUnformattedText();
/*     */       
/*     */       try
/*     */       {
/* 124 */         itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
/*     */       }
/*     */       catch (NBTException nbtexception)
/*     */       {
/* 128 */         throw new CommandException("commands.replaceitem.tagError", new Object[] { nbtexception.getMessage() });
/*     */       }
/*     */     }
/*     */     
/* 132 */     if (itemstack.getItem() == null)
/*     */     {
/* 134 */       itemstack = null;
/*     */     }
/*     */     
/* 137 */     if (flag)
/*     */     {
/* 139 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/* 140 */       BlockPos blockpos = parseBlockPos(sender, args, 1, false);
/* 141 */       World world = sender.getEntityWorld();
/* 142 */       net.minecraft.tileentity.TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 144 */       if ((tileentity == null) || (!(tileentity instanceof IInventory)))
/*     */       {
/* 146 */         throw new CommandException("commands.replaceitem.noContainer", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       
/* 149 */       IInventory iinventory = (IInventory)tileentity;
/*     */       
/* 151 */       if ((j >= 0) && (j < iinventory.getSizeInventory()))
/*     */       {
/* 153 */         iinventory.setInventorySlotContents(j, itemstack);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 158 */       Entity entity = func_175768_b(sender, args[1]);
/* 159 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/*     */       
/* 161 */       if ((entity instanceof EntityPlayer))
/*     */       {
/* 163 */         ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
/*     */       }
/*     */       
/* 166 */       if (!entity.replaceItemInInventory(j, itemstack))
/*     */       {
/* 168 */         throw new CommandException("commands.replaceitem.failed", new Object[] { Integer.valueOf(j), Integer.valueOf(k), itemstack == null ? "Air" : itemstack.getChatComponent() });
/*     */       }
/*     */       
/* 171 */       if ((entity instanceof EntityPlayer))
/*     */       {
/* 173 */         ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
/*     */       }
/*     */     }
/*     */     
/* 177 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
/* 178 */     notifyOperators(sender, this, "commands.replaceitem.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k), itemstack == null ? "Air" : itemstack.getChatComponent() });
/*     */   }
/*     */   
/*     */   private int getSlotForShortcut(String shortcut)
/*     */     throws CommandException
/*     */   {
/* 184 */     if (!SHORTCUTS.containsKey(shortcut))
/*     */     {
/* 186 */       throw new CommandException("commands.generic.parameter.invalid", new Object[] { shortcut });
/*     */     }
/*     */     
/*     */ 
/* 190 */     return ((Integer)SHORTCUTS.get(shortcut)).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*     */   {
/* 196 */     return ((args.length != 3) || (!args[0].equals("entity"))) && ((args.length != 5) || (!args[0].equals("block"))) ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : ((args.length != 4) || (!args[0].equals("entity"))) && ((args.length != 6) || (!args[0].equals("block"))) ? null : (args.length >= 2) && (args.length <= 4) && (args[0].equals("block")) ? func_175771_a(args, 1, pos) : (args.length == 2) && (args[0].equals("entity")) ? getListOfStringsMatchingLastWord(args, getUsernames()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "entity", "block" }) : getListOfStringsMatchingLastWord(args, SHORTCUTS.keySet());
/*     */   }
/*     */   
/*     */   protected String[] getUsernames()
/*     */   {
/* 201 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUsernameIndex(String[] args, int index)
/*     */   {
/* 209 */     return (args.length > 0) && (args[0].equals("entity")) && (index == 1);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 214 */     for (int i = 0; i < 54; i++)
/*     */     {
/* 216 */       SHORTCUTS.put("slot.container." + i, Integer.valueOf(i));
/*     */     }
/*     */     
/* 219 */     for (int j = 0; j < 9; j++)
/*     */     {
/* 221 */       SHORTCUTS.put("slot.hotbar." + j, Integer.valueOf(j));
/*     */     }
/*     */     
/* 224 */     for (int k = 0; k < 27; k++)
/*     */     {
/* 226 */       SHORTCUTS.put("slot.inventory." + k, Integer.valueOf(9 + k));
/*     */     }
/*     */     
/* 229 */     for (int l = 0; l < 27; l++)
/*     */     {
/* 231 */       SHORTCUTS.put("slot.enderchest." + l, Integer.valueOf(200 + l));
/*     */     }
/*     */     
/* 234 */     for (int i1 = 0; i1 < 8; i1++)
/*     */     {
/* 236 */       SHORTCUTS.put("slot.villager." + i1, Integer.valueOf(300 + i1));
/*     */     }
/*     */     
/* 239 */     for (int j1 = 0; j1 < 15; j1++)
/*     */     {
/* 241 */       SHORTCUTS.put("slot.horse." + j1, Integer.valueOf(500 + j1));
/*     */     }
/*     */     
/* 244 */     SHORTCUTS.put("slot.weapon", Integer.valueOf(99));
/* 245 */     SHORTCUTS.put("slot.armor.head", Integer.valueOf(103));
/* 246 */     SHORTCUTS.put("slot.armor.chest", Integer.valueOf(102));
/* 247 */     SHORTCUTS.put("slot.armor.legs", Integer.valueOf(101));
/* 248 */     SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100));
/* 249 */     SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
/* 250 */     SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
/* 251 */     SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandReplaceItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */