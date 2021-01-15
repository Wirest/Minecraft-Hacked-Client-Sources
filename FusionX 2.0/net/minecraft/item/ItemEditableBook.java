package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemEditableBook extends Item
{
    private static final String __OBFID = "CL_00000077";

    public ItemEditableBook()
    {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound p_77828_0_)
    {
        if (!ItemWritableBook.validBookPageTagContents(p_77828_0_))
        {
            return false;
        }
        else if (!p_77828_0_.hasKey("title", 8))
        {
            return false;
        }
        else
        {
            String var1 = p_77828_0_.getString("title");
            return var1 != null && var1.length() <= 32 ? p_77828_0_.hasKey("author", 8) : false;
        }
    }

    public static int func_179230_h(ItemStack p_179230_0_)
    {
        return p_179230_0_.getTagCompound().getInteger("generation");
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound var2 = stack.getTagCompound();
            String var3 = var2.getString("title");

            if (!StringUtils.isNullOrEmpty(var3))
            {
                return var3;
            }
        }

        return super.getItemStackDisplayName(stack);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound var5 = stack.getTagCompound();
            String var6 = var5.getString("author");

            if (!StringUtils.isNullOrEmpty(var6))
            {
                tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {var6}));
            }

            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("book.generation." + var5.getInteger("generation")));
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            this.func_179229_a(itemStackIn, playerIn);
        }

        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    private void func_179229_a(ItemStack p_179229_1_, EntityPlayer p_179229_2_)
    {
        if (p_179229_1_ != null && p_179229_1_.getTagCompound() != null)
        {
            NBTTagCompound var3 = p_179229_1_.getTagCompound();

            if (!var3.getBoolean("resolved"))
            {
                var3.setBoolean("resolved", true);

                if (validBookTagContents(var3))
                {
                    NBTTagList var4 = var3.getTagList("pages", 8);

                    for (int var5 = 0; var5 < var4.tagCount(); ++var5)
                    {
                        String var6 = var4.getStringTagAt(var5);
                        Object var7;

                        try
                        {
                            IChatComponent var11 = IChatComponent.Serializer.jsonToComponent(var6);
                            var7 = ChatComponentProcessor.func_179985_a(p_179229_2_, var11, p_179229_2_);
                        }
                        catch (Exception var9)
                        {
                            var7 = new ChatComponentText(var6);
                        }

                        var4.set(var5, new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)var7)));
                    }

                    var3.setTag("pages", var4);

                    if (p_179229_2_ instanceof EntityPlayerMP && p_179229_2_.getCurrentEquippedItem() == p_179229_1_)
                    {
                        Slot var10 = p_179229_2_.openContainer.getSlotFromInventory(p_179229_2_.inventory, p_179229_2_.inventory.currentItem);
                        ((EntityPlayerMP)p_179229_2_).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, var10.slotNumber, p_179229_1_));
                    }
                }
            }
        }
    }

    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
