/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.util.ChatComponentProcessor;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemEditableBook extends Item
/*     */ {
/*     */   public ItemEditableBook()
/*     */   {
/*  24 */     setMaxStackSize(1);
/*     */   }
/*     */   
/*     */   public static boolean validBookTagContents(NBTTagCompound nbt)
/*     */   {
/*  29 */     if (!ItemWritableBook.isNBTValid(nbt))
/*     */     {
/*  31 */       return false;
/*     */     }
/*  33 */     if (!nbt.hasKey("title", 8))
/*     */     {
/*  35 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  39 */     String s = nbt.getString("title");
/*  40 */     return (s != null) && (s.length() <= 32) ? nbt.hasKey("author", 8) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getGeneration(ItemStack book)
/*     */   {
/*  49 */     return book.getTagCompound().getInteger("generation");
/*     */   }
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack)
/*     */   {
/*  54 */     if (stack.hasTagCompound())
/*     */     {
/*  56 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  57 */       String s = nbttagcompound.getString("title");
/*     */       
/*  59 */       if (!StringUtils.isNullOrEmpty(s))
/*     */       {
/*  61 */         return s;
/*     */       }
/*     */     }
/*     */     
/*  65 */     return super.getItemStackDisplayName(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  73 */     if (stack.hasTagCompound())
/*     */     {
/*  75 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  76 */       String s = nbttagcompound.getString("author");
/*     */       
/*  78 */       if (!StringUtils.isNullOrEmpty(s))
/*     */       {
/*  80 */         tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { s }));
/*     */       }
/*     */       
/*  83 */       tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(new StringBuilder("book.generation.").append(nbttagcompound.getInteger("generation")).toString()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  92 */     if (!worldIn.isRemote)
/*     */     {
/*  94 */       resolveContents(itemStackIn, playerIn);
/*     */     }
/*     */     
/*  97 */     playerIn.displayGUIBook(itemStackIn);
/*  98 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  99 */     return itemStackIn;
/*     */   }
/*     */   
/*     */   private void resolveContents(ItemStack stack, EntityPlayer player)
/*     */   {
/* 104 */     if ((stack != null) && (stack.getTagCompound() != null))
/*     */     {
/* 106 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */       
/* 108 */       if (!nbttagcompound.getBoolean("resolved"))
/*     */       {
/* 110 */         nbttagcompound.setBoolean("resolved", true);
/*     */         
/* 112 */         if (validBookTagContents(nbttagcompound))
/*     */         {
/* 114 */           NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
/*     */           
/* 116 */           for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */           {
/* 118 */             String s = nbttaglist.getStringTagAt(i);
/*     */             
/*     */             IChatComponent lvt_7_1_;
/*     */             try
/*     */             {
/* 123 */               IChatComponent lvt_7_1_ = IChatComponent.Serializer.jsonToComponent(s);
/* 124 */               lvt_7_1_ = ChatComponentProcessor.processComponent(player, lvt_7_1_, player);
/*     */             }
/*     */             catch (Exception var9)
/*     */             {
/* 128 */               lvt_7_1_ = new net.minecraft.util.ChatComponentText(s);
/*     */             }
/*     */             
/* 131 */             nbttaglist.set(i, new NBTTagString(IChatComponent.Serializer.componentToJson(lvt_7_1_)));
/*     */           }
/*     */           
/* 134 */           nbttagcompound.setTag("pages", nbttaglist);
/*     */           
/* 136 */           if (((player instanceof EntityPlayerMP)) && (player.getCurrentEquippedItem() == stack))
/*     */           {
/* 138 */             Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
/* 139 */             ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2FPacketSetSlot(0, slot.slotNumber, stack));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasEffect(ItemStack stack)
/*     */   {
/* 148 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemEditableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */