// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTException;
import net.minecraft.command.CommandException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTestFor extends CommandBase
{
    @Override
    public String getCommandName() {
        return "testfor";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.testfor.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        final Entity entity = CommandBase.func_175768_b(sender, args[0]);
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 2) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 1));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.testfor.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (nbttagcompound != null) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            entity.writeToNBT(nbttagcompound2);
            if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound2, true)) {
                throw new CommandException("commands.testfor.failure", new Object[] { entity.getName() });
            }
        }
        CommandBase.notifyOperators(sender, this, "commands.testfor.success", entity.getName());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
