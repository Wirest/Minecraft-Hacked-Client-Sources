// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.util.EnumChatFormatting;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEditableBook extends Item
{
    public ItemEditableBook() {
        this.setMaxStackSize(1);
    }
    
    public static boolean validBookTagContents(final NBTTagCompound nbt) {
        if (!ItemWritableBook.isNBTValid(nbt)) {
            return false;
        }
        if (!nbt.hasKey("title", 8)) {
            return false;
        }
        final String s = nbt.getString("title");
        return s != null && s.length() <= 32 && nbt.hasKey("author", 8);
    }
    
    public static int getGeneration(final ItemStack book) {
        return book.getTagCompound().getInteger("generation");
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String s = nbttagcompound.getString("title");
            if (!StringUtils.isNullOrEmpty(s)) {
                return s;
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String s = nbttagcompound.getString("author");
            if (!StringUtils.isNullOrEmpty(s)) {
                tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", s));
            }
            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
        }
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            this.resolveContents(itemStackIn, playerIn);
        }
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
    
    private void resolveContents(final ItemStack stack, final EntityPlayer player) {
        if (stack != null && stack.getTagCompound() != null) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (!nbttagcompound.getBoolean("resolved")) {
                nbttagcompound.setBoolean("resolved", true);
                if (validBookTagContents(nbttagcompound)) {
                    final NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        final String s = nbttaglist.getStringTagAt(i);
                        IChatComponent lvt_7_1_;
                        try {
                            lvt_7_1_ = IChatComponent.Serializer.jsonToComponent(s);
                            lvt_7_1_ = ChatComponentProcessor.processComponent(player, lvt_7_1_, player);
                        }
                        catch (Exception var9) {
                            lvt_7_1_ = new ChatComponentText(s);
                        }
                        nbttaglist.set(i, new NBTTagString(IChatComponent.Serializer.componentToJson(lvt_7_1_)));
                    }
                    nbttagcompound.setTag("pages", nbttaglist);
                    if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
                        final Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
                        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, stack));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
}
