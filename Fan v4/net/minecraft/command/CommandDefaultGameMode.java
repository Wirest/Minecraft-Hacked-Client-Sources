package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode extends CommandGameMode
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "defaultgamemode";
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.defaultgamemode.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.defaultgamemode.usage");
        }
        else
        {
            WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
            this.setGameType(worldsettings$gametype);
            notifyOperators(sender, this, "commands.defaultgamemode.success", new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName()));
        }
    }

    protected void setGameType(WorldSettings.GameType p_71541_1_)
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        minecraftserver.setGameType(p_71541_1_);

        if (minecraftserver.getForceGamemode())
        {
            for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().func_181057_v())
            {
                entityplayermp.setGameType(p_71541_1_);
                entityplayermp.fallDistance = 0.0F;
            }
        }
    }
}
