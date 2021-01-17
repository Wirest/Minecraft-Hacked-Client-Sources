// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSetSpawnpoint extends CommandBase
{
    @Override
    public String getCommandName() {
        return "spawnpoint";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 1 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        final EntityPlayerMP entityplayermp = (args.length > 0) ? CommandBase.getPlayer(sender, args[0]) : CommandBase.getCommandSenderAsPlayer(sender);
        final BlockPos blockpos = (args.length > 3) ? CommandBase.parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();
        if (entityplayermp.worldObj != null) {
            entityplayermp.setSpawnPoint(blockpos, true);
            CommandBase.notifyOperators(sender, this, "commands.spawnpoint.success", entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
