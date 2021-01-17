// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;

public class CommandClearInventory extends CommandBase
{
    @Override
    public String getCommandName() {
        return "clear";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.clear.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final EntityPlayerMP entityplayermp = (args.length == 0) ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(sender, args[0]);
        final Item item = (args.length >= 2) ? CommandBase.getItemByText(sender, args[1]) : null;
        final int i = (args.length >= 3) ? CommandBase.parseInt(args[2], -1) : -1;
        final int j = (args.length >= 4) ? CommandBase.parseInt(args[3], -1) : -1;
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 5) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 4));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (args.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
        }
        final int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
        entityplayermp.inventoryContainer.detectAndSendChanges();
        if (!entityplayermp.capabilities.isCreativeMode) {
            entityplayermp.updateHeldItem();
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
        if (k == 0) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
        }
        if (j == 0) {
            sender.addChatMessage(new ChatComponentTranslation("commands.clear.testing", new Object[] { entityplayermp.getName(), k }));
        }
        else {
            CommandBase.notifyOperators(sender, this, "commands.clear.success", entityplayermp.getName(), k);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.func_147209_d()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
    }
    
    protected String[] func_147209_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
