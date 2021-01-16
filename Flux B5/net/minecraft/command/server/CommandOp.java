package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandOp extends CommandBase
{
    private static final String __OBFID = "CL_00000694";

    public String getCommandName()
    {
        return "op";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.op.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1 && args[0].length() > 0)
        {
            MinecraftServer var3 = MinecraftServer.getServer();
            GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(args[0]);

            if (var4 == null)
            {
                throw new CommandException("commands.op.failed", new Object[] {args[0]});
            }
            else
            {
                var3.getConfigurationManager().addOp(var4);
                notifyOperators(sender, this, "commands.op.success", new Object[] {args[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            String var4 = args[args.length - 1];
            ArrayList var5 = Lists.newArrayList();
            GameProfile[] var6 = MinecraftServer.getServer().getGameProfiles();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                GameProfile var9 = var6[var8];

                if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(var9) && doesStringStartWith(var4, var9.getName()))
                {
                    var5.add(var9.getName());
                }
            }

            return var5;
        }
        else
        {
            return null;
        }
    }
}
