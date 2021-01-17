// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;

public class CommandEntityData extends CommandBase
{
    @Override
    public String getCommandName() {
        return "entitydata";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.entitydata.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        final Entity entity = CommandBase.func_175768_b(sender, args[0]);
        if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.getDisplayName() });
        }
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        entity.writeToNBT(nbttagcompound);
        final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttagcompound.copy();
        NBTTagCompound nbttagcompound3;
        try {
            nbttagcompound3 = JsonToNBT.getTagFromJson(CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.entitydata.tagError", new Object[] { nbtexception.getMessage() });
        }
        nbttagcompound3.removeTag("UUIDMost");
        nbttagcompound3.removeTag("UUIDLeast");
        nbttagcompound.merge(nbttagcompound3);
        if (nbttagcompound.equals(nbttagcompound2)) {
            throw new CommandException("commands.entitydata.failed", new Object[] { nbttagcompound.toString() });
        }
        entity.readFromNBT(nbttagcompound);
        CommandBase.notifyOperators(sender, this, "commands.entitydata.success", nbttagcompound.toString());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
