// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collection;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;

public class CommandExecuteAt extends CommandBase
{
    @Override
    public String getCommandName() {
        return "execute";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.execute.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(sender, args[0], (Class<? extends Entity>)Entity.class);
        final double d0 = CommandBase.parseDouble(entity.posX, args[1], false);
        final double d2 = CommandBase.parseDouble(entity.posY, args[2], false);
        final double d3 = CommandBase.parseDouble(entity.posZ, args[3], false);
        final BlockPos blockpos = new BlockPos(d0, d2, d3);
        int i = 4;
        if ("detect".equals(args[4]) && args.length > 10) {
            final World world = entity.getEntityWorld();
            final double d4 = CommandBase.parseDouble(d0, args[5], false);
            final double d5 = CommandBase.parseDouble(d2, args[6], false);
            final double d6 = CommandBase.parseDouble(d3, args[7], false);
            final Block block = CommandBase.getBlockByText(sender, args[8]);
            final int k = CommandBase.parseInt(args[9], -1, 15);
            final BlockPos blockpos2 = new BlockPos(d4, d5, d6);
            final IBlockState iblockstate = world.getBlockState(blockpos2);
            if (iblockstate.getBlock() != block || (k >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != k)) {
                throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
            }
            i = 10;
        }
        final String s = CommandBase.buildString(args, i);
        final ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
                return entity.getName();
            }
            
            @Override
            public IChatComponent getDisplayName() {
                return entity.getDisplayName();
            }
            
            @Override
            public void addChatMessage(final IChatComponent component) {
                sender.addChatMessage(component);
            }
            
            @Override
            public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                return sender.canCommandSenderUseCommand(permLevel, commandName);
            }
            
            @Override
            public BlockPos getPosition() {
                return blockpos;
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(d0, d2, d3);
            }
            
            @Override
            public World getEntityWorld() {
                return entity.worldObj;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return entity;
            }
            
            @Override
            public boolean sendCommandFeedback() {
                final MinecraftServer minecraftserver = MinecraftServer.getServer();
                return minecraftserver == null || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
            }
            
            @Override
            public void setCommandStat(final CommandResultStats.Type type, final int amount) {
                entity.setCommandStat(type, amount);
            }
        };
        final ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();
        try {
            final int j = icommandmanager.executeCommand(icommandsender, s);
            if (j < 1) {
                throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s });
            }
        }
        catch (Throwable var23) {
            throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName() });
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : ((args.length > 5 && args.length <= 8 && "detect".equals(args[4])) ? CommandBase.func_175771_a(args, 5, pos) : ((args.length == 9 && "detect".equals(args[4])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)));
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
