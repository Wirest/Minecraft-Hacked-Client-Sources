package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandEffect extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "effect";
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
        return "commands.effect.usage";
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
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        else
        {
            EntityLivingBase var3 = (EntityLivingBase)getEntity(sender, args[0], EntityLivingBase.class);

            if (args[1].equals("clear"))
            {
                if (var3.getActivePotionEffects().isEmpty())
                {
                    throw new CommandException("commands.effect.failure.notActive.all", new Object[] {var3.getCommandSenderName()});
                }
                else
                {
                    var3.clearActivePotions();
                    notifyOperators(sender, this, "commands.effect.success.removed.all", new Object[] {var3.getCommandSenderName()});
                }
            }
            else
            {
                int var4;

                try
                {
                    var4 = parseInt(args[1], 1);
                }
                catch (NumberInvalidException var11)
                {
                    Potion var6 = Potion.getPotionFromResourceLocation(args[1]);

                    if (var6 == null)
                    {
                        throw var11;
                    }

                    var4 = var6.id;
                }

                int var5 = 600;
                int var12 = 30;
                int var7 = 0;

                if (var4 >= 0 && var4 < Potion.potionTypes.length && Potion.potionTypes[var4] != null)
                {
                    Potion var8 = Potion.potionTypes[var4];

                    if (args.length >= 3)
                    {
                        var12 = parseInt(args[2], 0, 1000000);

                        if (var8.isInstant())
                        {
                            var5 = var12;
                        }
                        else
                        {
                            var5 = var12 * 20;
                        }
                    }
                    else if (var8.isInstant())
                    {
                        var5 = 1;
                    }

                    if (args.length >= 4)
                    {
                        var7 = parseInt(args[3], 0, 255);
                    }

                    boolean var9 = true;

                    if (args.length >= 5 && "true".equalsIgnoreCase(args[4]))
                    {
                        var9 = false;
                    }

                    if (var12 > 0)
                    {
                        PotionEffect var10 = new PotionEffect(var4, var5, var7, false, var9);
                        var3.addPotionEffect(var10);
                        notifyOperators(sender, this, "commands.effect.success", new Object[] {new ChatComponentTranslation(var10.getEffectName(), new Object[0]), Integer.valueOf(var4), Integer.valueOf(var7), var3.getCommandSenderName(), Integer.valueOf(var12)});
                    }
                    else if (var3.isPotionActive(var4))
                    {
                        var3.removePotionEffect(var4);
                        notifyOperators(sender, this, "commands.effect.success.removed", new Object[] {new ChatComponentTranslation(var8.getName(), new Object[0]), var3.getCommandSenderName()});
                    }
                    else
                    {
                        throw new CommandException("commands.effect.failure.notActive", new Object[] {new ChatComponentTranslation(var8.getName(), new Object[0]), var3.getCommandSenderName()});
                    }
                }
                else
                {
                    throw new NumberInvalidException("commands.effect.notFound", new Object[] {Integer.valueOf(var4)});
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, Potion.getPotionMapAsArray()) : (args.length == 5 ? getListOfStringsMatchingLastWord(args, new String[] {"true", "false"}): null));
    }

    protected String[] getAllUsernames()
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
