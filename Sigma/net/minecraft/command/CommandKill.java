package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill extends CommandBase {
    private static final String __OBFID = "CL_00000570";

    @Override
    public String getCommandName() {
        return "kill";
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
        return "commands.kill.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayerMP var4 = CommandBase.getCommandSenderAsPlayer(sender);
            var4.func_174812_G();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", new Object[]{var4.getDisplayName()});
        } else {
            Entity var3 = CommandBase.func_175768_b(sender, args[0]);
            var3.func_174812_G();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", new Object[]{var3.getDisplayName()});
        }
    }

    /**
     * Return whether the specified command parameter index is a username
     * parameter.
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
