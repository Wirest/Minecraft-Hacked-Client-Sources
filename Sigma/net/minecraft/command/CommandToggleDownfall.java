package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
    private static final String __OBFID = "CL_00001184";

    @Override
    public String getCommandName() {
        return "toggledownfall";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.downfall.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        toggleDownfall();
        CommandBase.notifyOperators(sender, this, "commands.downfall.success", new Object[0]);
    }

    /**
     * Toggle rain and enable thundering.
     */
    protected void toggleDownfall() {
        WorldInfo var1 = MinecraftServer.getServer().worldServers[0].getWorldInfo();
        var1.setRaining(!var1.isRaining());
    }
}
