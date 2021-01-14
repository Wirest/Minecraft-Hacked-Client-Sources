package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase
{
    /**
     * Returns true if the given command sender is allowed to use this command.
     *  
     * @param sender The CommandSender
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(sender);
    }

    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "seed";
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
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.seed.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        World world = sender instanceof EntityPlayer ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
        sender.addChatMessage(new ChatComponentTranslation("commands.seed.success", world.getSeed()));
    }
}
