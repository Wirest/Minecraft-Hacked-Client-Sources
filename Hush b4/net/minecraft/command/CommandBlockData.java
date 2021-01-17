// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public class CommandBlockData extends CommandBase
{
    @Override
    public String getCommandName() {
        return "blockdata";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.blockdata.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
        }
        final TileEntity tileentity = world.getTileEntity(blockpos);
        if (tileentity == null) {
            throw new CommandException("commands.blockdata.notValid", new Object[0]);
        }
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        tileentity.writeToNBT(nbttagcompound);
        final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttagcompound.copy();
        NBTTagCompound nbttagcompound3;
        try {
            nbttagcompound3 = JsonToNBT.getTagFromJson(CommandBase.getChatComponentFromNthArg(sender, args, 3).getUnformattedText());
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.blockdata.tagError", new Object[] { nbtexception.getMessage() });
        }
        nbttagcompound.merge(nbttagcompound3);
        nbttagcompound.setInteger("x", blockpos.getX());
        nbttagcompound.setInteger("y", blockpos.getY());
        nbttagcompound.setInteger("z", blockpos.getZ());
        if (nbttagcompound.equals(nbttagcompound2)) {
            throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString() });
        }
        tileentity.readFromNBT(nbttagcompound);
        tileentity.markDirty();
        world.markBlockForUpdate(blockpos);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyOperators(sender, this, "commands.blockdata.success", nbttagcompound.toString());
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : null;
    }
}
