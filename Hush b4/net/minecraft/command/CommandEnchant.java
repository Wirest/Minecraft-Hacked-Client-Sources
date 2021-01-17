// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.Enchantment;

public class CommandEnchant extends CommandBase
{
    @Override
    public String getCommandName() {
        return "enchant";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.enchant.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(sender, args[0]);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        int i;
        try {
            i = CommandBase.parseInt(args[1], 0);
        }
        catch (NumberInvalidException numberinvalidexception) {
            final Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
            if (enchantment == null) {
                throw numberinvalidexception;
            }
            i = enchantment.effectId;
        }
        int j = 1;
        final ItemStack itemstack = entityplayer.getCurrentEquippedItem();
        if (itemstack == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        final Enchantment enchantment2 = Enchantment.getEnchantmentById(i);
        if (enchantment2 == null) {
            throw new NumberInvalidException("commands.enchant.notFound", new Object[] { i });
        }
        if (!enchantment2.canApply(itemstack)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (args.length >= 3) {
            j = CommandBase.parseInt(args[2], enchantment2.getMinLevel(), enchantment2.getMaxLevel());
        }
        if (itemstack.hasTagCompound()) {
            final NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
            if (nbttaglist != null) {
                for (int k = 0; k < nbttaglist.tagCount(); ++k) {
                    final int l = nbttaglist.getCompoundTagAt(k).getShort("id");
                    if (Enchantment.getEnchantmentById(l) != null) {
                        final Enchantment enchantment3 = Enchantment.getEnchantmentById(l);
                        if (!enchantment3.canApplyTogether(enchantment2)) {
                            throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment2.getTranslatedName(j), enchantment3.getTranslatedName(nbttaglist.getCompoundTagAt(k).getShort("lvl")) });
                        }
                    }
                }
            }
        }
        itemstack.addEnchantment(enchantment2, j);
        CommandBase.notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Enchantment.func_181077_c()) : null);
    }
    
    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
