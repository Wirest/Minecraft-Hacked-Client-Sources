package net.minecraft.command;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandExecuteAt extends CommandBase
{

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "execute";
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
        return "commands.execute.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    @Override
	public void processCommand(final ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 5)
        {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        else
        {
            final Entity var3 = getEntity(sender, args[0], Entity.class);
            final double var4 = func_175761_b(var3.posX, args[1], false);
            final double var6 = func_175761_b(var3.posY, args[2], false);
            final double var8 = func_175761_b(var3.posZ, args[3], false);
            final BlockPos var10 = new BlockPos(var4, var6, var8);
            byte var11 = 4;

            if ("detect".equals(args[4]) && args.length > 10)
            {
                World var12 = sender.getEntityWorld();
                double var13 = func_175761_b(var4, args[5], false);
                double var15 = func_175761_b(var6, args[6], false);
                double var17 = func_175761_b(var8, args[7], false);
                Block var19 = getBlockByText(sender, args[8]);
                int var20 = parseInt(args[9], -1, 15);
                BlockPos var21 = new BlockPos(var13, var15, var17);
                IBlockState var22 = var12.getBlockState(var21);

                if (var22.getBlock() != var19 || var20 >= 0 && var22.getBlock().getMetaFromState(var22) != var20)
                {
                    throw new CommandException("commands.execute.failed", new Object[] {"detect", var3.getCommandSenderName()});
                }

                var11 = 10;
            }

            String var24 = buildString(args, var11);
            ICommandSender var14 = new ICommandSender()
            {
                @Override
				public String getCommandSenderName()
                {
                    return var3.getCommandSenderName();
                }
                @Override
				public IChatComponent getDisplayName()
                {
                    return var3.getDisplayName();
                }
                @Override
				public void addChatMessage(IChatComponent component)
                {
                    sender.addChatMessage(component);
                }
                @Override
				public boolean canCommandSenderUseCommand(int permLevel, String commandName)
                {
                    return sender.canCommandSenderUseCommand(permLevel, commandName);
                }
                @Override
				public BlockPos getPosition()
                {
                    return var10;
                }
                @Override
				public Vec3 getPositionVector()
                {
                    return new Vec3(var4, var6, var8);
                }
                @Override
				public World getEntityWorld()
                {
                    return var3.worldObj;
                }
                @Override
				public Entity getCommandSenderEntity()
                {
                    return var3;
                }
                @Override
				public boolean sendCommandFeedback()
                {
                    MinecraftServer var1 = MinecraftServer.getServer();
                    return var1 == null || var1.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
                }
                @Override
				public void setCommandStat(CommandResultStats.Type type, int amount)
                {
                    var3.setCommandStat(type, amount);
                }
            };
            ICommandManager var25 = MinecraftServer.getServer().getCommandManager();

            try
            {
                int var16 = var25.executeCommand(var14, var24);

                if (var16 < 1)
                {
                    throw new CommandException("commands.execute.allInvocationsFailed", new Object[] {var24});
                }
            }
            catch (Throwable var23)
            {
                throw new CommandException("commands.execute.failed", new Object[] {var24, var3.getCommandSenderName()});
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : (args.length > 5 && args.length <= 8 && "detect".equals(args[4]) ? func_175771_a(args, 5, pos) : (args.length == 9 && "detect".equals(args[4]) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)));
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
