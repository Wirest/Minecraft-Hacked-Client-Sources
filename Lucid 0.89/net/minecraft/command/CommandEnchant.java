package net.minecraft.command;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEnchant extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "enchant";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "commands.enchant.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    @Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            int var4;

            try
            {
                var4 = parseInt(args[1], 0);
            }
            catch (NumberInvalidException var12)
            {
                Enchantment var6 = Enchantment.getEnchantmentByLocation(args[1]);

                if (var6 == null)
                {
                    throw var12;
                }

                var4 = var6.effectId;
            }

            int var5 = 1;
            ItemStack var13 = var3.getCurrentEquippedItem();

            if (var13 == null)
            {
                throw new CommandException("commands.enchant.noItem", new Object[0]);
            }
            else
            {
                Enchantment var7 = Enchantment.getEnchantmentById(var4);

                if (var7 == null)
                {
                    throw new NumberInvalidException("commands.enchant.notFound", new Object[] {Integer.valueOf(var4)});
                }
                else if (!var7.canApply(var13))
                {
                    throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
                }
                else
                {
                    if (args.length >= 3)
                    {
                        var5 = parseInt(args[2], var7.getMinLevel(), var7.getMaxLevel());
                    }

                    if (var13.hasTagCompound())
                    {
                        NBTTagList var8 = var13.getEnchantmentTagList();

                        if (var8 != null)
                        {
                            for (int var9 = 0; var9 < var8.tagCount(); ++var9)
                            {
                                short var10 = var8.getCompoundTagAt(var9).getShort("id");

                                if (Enchantment.getEnchantmentById(var10) != null)
                                {
                                    Enchantment var11 = Enchantment.getEnchantmentById(var10);

                                    if (!var11.canApplyTogether(var7))
                                    {
                                        throw new CommandException("commands.enchant.cantCombine", new Object[] {var7.getTranslatedName(var5), var11.getTranslatedName(var8.getCompoundTagAt(var9).getShort("lvl"))});
                                    }
                                }
                            }
                        }
                    }

                    var13.addEnchantment(var7, var5);
                    notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
        }
    }

    /**
     * Return a list of options when the user types TAB
     *  
     * @param sender The {@link ICommandSender sender} who pressed TAB
     * @param args The arguments that were present when TAB was pressed
     * @param pos The block that the player is targeting, <b>May be {@code null}</b>
     */
    @Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, Enchantment.getLocationNames()) : null);
    }

    protected String[] getListOfPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments that were given
     * @param index The argument index that we are checking
     */
    @Override
	public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
