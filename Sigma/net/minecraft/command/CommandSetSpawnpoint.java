package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetSpawnpoint extends CommandBase {
    private static final String __OBFID = "CL_00001026";

    @Override
    public String getCommandName() {
        return "spawnpoint";
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
        return "commands.spawnpoint.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        } else {
            EntityPlayerMP var3 = args.length > 0 ? CommandBase.getPlayer(sender, args[0]) : CommandBase.getCommandSenderAsPlayer(sender);
            BlockPos var4 = args.length > 3 ? CommandBase.func_175757_a(sender, args, 1, true) : var3.getPosition();

            if (var3.worldObj != null) {
                var3.func_180473_a(var4, true);
                CommandBase.notifyOperators(sender, this, "commands.spawnpoint.success", new Object[]{var3.getName(), Integer.valueOf(var4.getX()), Integer.valueOf(var4.getY()), Integer.valueOf(var4.getZ())});
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? CommandBase.func_175771_a(args, 1, pos) : null);
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
