package net.minecraft.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandStats extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "stats";
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
        return "commands.stats.usage";
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
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        else
        {
            boolean var3;

            if (args[0].equals("entity"))
            {
                var3 = false;
            }
            else
            {
                if (!args[0].equals("block"))
                {
                    throw new WrongUsageException("commands.stats.usage", new Object[0]);
                }

                var3 = true;
            }

            byte var4;

            if (var3)
            {
                if (args.length < 5)
                {
                    throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
                }

                var4 = 4;
            }
            else
            {
                if (args.length < 3)
                {
                    throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
                }

                var4 = 2;
            }

            int var11 = var4 + 1;
            String var5 = args[var4];

            if ("set".equals(var5))
            {
                if (args.length < var11 + 3)
                {
                    if (var11 == 5)
                    {
                        throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                    }

                    throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
                }
            }
            else
            {
                if (!"clear".equals(var5))
                {
                    throw new WrongUsageException("commands.stats.usage", new Object[0]);
                }

                if (args.length < var11 + 1)
                {
                    if (var11 == 5)
                    {
                        throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                    }

                    throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
                }
            }

            CommandResultStats.Type var6 = CommandResultStats.Type.getTypeByName(args[var11++]);

            if (var6 == null)
            {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            else
            {
                World var7 = sender.getEntityWorld();
                CommandResultStats var8;
                BlockPos var9;
                TileEntity var10;

                if (var3)
                {
                    var9 = parseBlockPos(sender, args, 1, false);
                    var10 = var7.getTileEntity(var9);

                    if (var10 == null)
                    {
                        throw new CommandException("commands.stats.noCompatibleBlock", new Object[] {Integer.valueOf(var9.getX()), Integer.valueOf(var9.getY()), Integer.valueOf(var9.getZ())});
                    }

                    if (var10 instanceof TileEntityCommandBlock)
                    {
                        var8 = ((TileEntityCommandBlock)var10).getCommandResultStats();
                    }
                    else
                    {
                        if (!(var10 instanceof TileEntitySign))
                        {
                            throw new CommandException("commands.stats.noCompatibleBlock", new Object[] {Integer.valueOf(var9.getX()), Integer.valueOf(var9.getY()), Integer.valueOf(var9.getZ())});
                        }

                        var8 = ((TileEntitySign)var10).func_174880_d();
                    }
                }
                else
                {
                    Entity var12 = func_175768_b(sender, args[1]);
                    var8 = var12.getCommandStats();
                }

                if ("set".equals(var5))
                {
                    String var13 = args[var11++];
                    String var14 = args[var11];

                    if (var13.length() == 0 || var14.length() == 0)
                    {
                        throw new CommandException("commands.stats.failed", new Object[0]);
                    }

                    CommandResultStats.func_179667_a(var8, var6, var13, var14);
                    notifyOperators(sender, this, "commands.stats.success", new Object[] {var6.getTypeName(), var14, var13});
                }
                else if ("clear".equals(var5))
                {
                    CommandResultStats.func_179667_a(var8, var6, (String)null, (String)null);
                    notifyOperators(sender, this, "commands.stats.cleared", new Object[] {var6.getTypeName()});
                }

                if (var3)
                {
                    var9 = parseBlockPos(sender, args, 1, false);
                    var10 = var7.getTileEntity(var9);
                    var10.markDirty();
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"entity", "block"}): (args.length == 2 && args[0].equals("entity") ? getListOfStringsMatchingLastWord(args, this.func_175776_d()) : ((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block")) ? ((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block")) ? ((args.length != 6 || !args[0].equals("entity")) && (args.length != 8 || !args[0].equals("block")) ? null : getListOfStringsMatchingLastWord(args, this.func_175777_e())) : getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames())) : getListOfStringsMatchingLastWord(args, new String[] {"set", "clear"})));
    }

    protected String[] func_175776_d()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected List func_175777_e()
    {
        Collection var1 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            ScoreObjective var4 = (ScoreObjective)var3.next();

            if (!var4.getCriteria().isReadOnly())
            {
                var2.add(var4.getName());
            }
        }

        return var2;
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
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
}
