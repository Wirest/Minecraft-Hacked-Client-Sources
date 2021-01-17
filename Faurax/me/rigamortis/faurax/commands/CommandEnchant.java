package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.nbt.*;
import net.minecraft.item.*;
import me.cupboard.command.argument.*;

public class CommandEnchant extends Command
{
    public CommandEnchant() {
        super("enchant", new String[] { "ench" });
    }
    
    @Argument
    protected String enchant() {
        final ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        Enchantment[] arrayOfEnchantment;
        for (int j = (arrayOfEnchantment = Enchantment.enchantmentsList).length, i = 0; i < j; ++i) {
            final Enchantment e = arrayOfEnchantment[i];
            if (e != null && e != null && e != Enchantment.silkTouch && item != null) {
                if (item.stackTagCompound == null) {
                    item.setTagCompound(new NBTTagCompound());
                }
                if (!item.stackTagCompound.hasKey("ench", 9)) {
                    item.stackTagCompound.setTag("ench", new NBTTagList());
                }
                final NBTTagList var3 = item.stackTagCompound.getTagList("ench", 10);
                final NBTTagCompound var2 = new NBTTagCompound();
                var2.setShort("id", (short)e.effectId);
                var2.setShort("lvl", (short)32767);
                var3.appendTag(var2);
            }
        }
        return "Done.";
    }
}
