// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTTagList;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList<MerchantRecipe>
{
    public MerchantRecipeList() {
    }
    
    public MerchantRecipeList(final NBTTagCompound compound) {
        this.readRecipiesFromTags(compound);
    }
    
    public MerchantRecipe canRecipeBeUsed(final ItemStack p_77203_1_, final ItemStack p_77203_2_, final int p_77203_3_) {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size()) {
            final MerchantRecipe merchantrecipe1 = this.get(p_77203_3_);
            return (!this.func_181078_a(p_77203_1_, merchantrecipe1.getItemToBuy()) || ((p_77203_2_ != null || merchantrecipe1.hasSecondItemToBuy()) && (!merchantrecipe1.hasSecondItemToBuy() || !this.func_181078_a(p_77203_2_, merchantrecipe1.getSecondItemToBuy()))) || p_77203_1_.stackSize < merchantrecipe1.getItemToBuy().stackSize || (merchantrecipe1.hasSecondItemToBuy() && p_77203_2_.stackSize < merchantrecipe1.getSecondItemToBuy().stackSize)) ? null : merchantrecipe1;
        }
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe2 = this.get(i);
            if (this.func_181078_a(p_77203_1_, merchantrecipe2.getItemToBuy()) && p_77203_1_.stackSize >= merchantrecipe2.getItemToBuy().stackSize && ((!merchantrecipe2.hasSecondItemToBuy() && p_77203_2_ == null) || (merchantrecipe2.hasSecondItemToBuy() && this.func_181078_a(p_77203_2_, merchantrecipe2.getSecondItemToBuy()) && p_77203_2_.stackSize >= merchantrecipe2.getSecondItemToBuy().stackSize))) {
                return merchantrecipe2;
            }
        }
        return null;
    }
    
    private boolean func_181078_a(final ItemStack p_181078_1_, final ItemStack p_181078_2_) {
        return ItemStack.areItemsEqual(p_181078_1_, p_181078_2_) && (!p_181078_2_.hasTagCompound() || (p_181078_1_.hasTagCompound() && NBTUtil.func_181123_a(p_181078_2_.getTagCompound(), p_181078_1_.getTagCompound(), false)));
    }
    
    public void writeToBuf(final PacketBuffer buffer) {
        buffer.writeByte((byte)(this.size() & 0xFF));
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
            final ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            buffer.writeBoolean(itemstack != null);
            if (itemstack != null) {
                buffer.writeItemStackToBuffer(itemstack);
            }
            buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
            buffer.writeInt(merchantrecipe.getToolUses());
            buffer.writeInt(merchantrecipe.getMaxTradeUses());
        }
    }
    
    public static MerchantRecipeList readFromBuf(final PacketBuffer buffer) throws IOException {
        final MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        for (int i = buffer.readByte() & 0xFF, j = 0; j < i; ++j) {
            final ItemStack itemstack = buffer.readItemStackFromBuffer();
            final ItemStack itemstack2 = buffer.readItemStackFromBuffer();
            ItemStack itemstack3 = null;
            if (buffer.readBoolean()) {
                itemstack3 = buffer.readItemStackFromBuffer();
            }
            final boolean flag = buffer.readBoolean();
            final int k = buffer.readInt();
            final int l = buffer.readInt();
            final MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack3, itemstack2, k, l);
            if (flag) {
                merchantrecipe.compensateToolUses();
            }
            merchantrecipelist.add(merchantrecipe);
        }
        return merchantrecipelist;
    }
    
    public void readRecipiesFromTags(final NBTTagCompound compound) {
        final NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            this.add(new MerchantRecipe(nbttagcompound));
        }
    }
    
    public NBTTagCompound getRecipiesAsTags() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            nbttaglist.appendTag(merchantrecipe.writeToTags());
        }
        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
