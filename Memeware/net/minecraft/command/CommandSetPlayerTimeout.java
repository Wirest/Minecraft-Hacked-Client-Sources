package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase {
    private static final String __OBFID = "CL_00000999";

    public String getCommandName() {
        return "setidletimeout";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.setidletimeout.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        } else {
            int var3 = parseInt(args[0], 0);
            MinecraftServer.getServer().setPlayerIdleTimeout(var3);
            notifyOperators(sender, this, "commands.setidletimeout.success", new Object[]{Integer.valueOf(var3)});
        }
    }
}
