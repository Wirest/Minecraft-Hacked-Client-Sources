package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CommandFunction extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "function";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.function.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 1 && args.length != 3)
        {
            throw new WrongUsageException("commands.function.usage", new Object[0]);
        }
        else
        {
            ResourceLocation resourcelocation = new ResourceLocation(args[0]);
            FunctionObject functionobject = server.func_193030_aL().func_193058_a(resourcelocation);

            if (functionobject == null)
            {
                throw new CommandException("commands.function.unknown", new Object[] {resourcelocation});
            }
            else
            {
                if (args.length == 3)
                {
                    String s = args[1];
                    boolean flag;

                    if ("if".equals(s))
                    {
                        flag = true;
                    }
                    else
                    {
                        if (!"unless".equals(s))
                        {
                            throw new WrongUsageException("commands.function.usage", new Object[0]);
                        }

                        flag = false;
                    }

                    boolean flag1 = false;

                    try
                    {
                        flag1 = !getEntityList(server, sender, args[2]).isEmpty();
                    }
                    catch (EntityNotFoundException var10)
                    {
                        ;
                    }

                    if (flag != flag1)
                    {
                        throw new CommandException("commands.function.skipped", new Object[] {resourcelocation});
                    }
                }

                int i = server.func_193030_aL().func_194019_a(functionobject, CommandSenderWrapper.func_193998_a(sender).func_194000_i().func_193999_a(2).func_194001_a(false));
                notifyCommandListener(sender, this, "commands.function.success", new Object[] {resourcelocation, i});
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.func_193030_aL().func_193066_d().keySet());
        }
        else if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"if", "unless"});
        }
        else
        {
            return args.length == 3 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
        }
    }
}
