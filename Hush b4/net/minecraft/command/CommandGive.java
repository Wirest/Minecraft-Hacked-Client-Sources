// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;

public class CommandGive extends CommandBase
{
    @Override
    public String getCommandName() {
        return "give";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.give.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(sender, args[0]);
        final Item item = CommandBase.getItemByText(sender, args[1]);
        final int i = (args.length >= 3) ? CommandBase.parseInt(args[2], 1, 64) : 1;
        final int j = (args.length >= 4) ? CommandBase.parseInt(args[3]) : 0;
        final ItemStack itemstack = new ItemStack(item, i, j);
        if (args.length >= 5) {
            final String s = CommandBase.getChatComponentFromNthArg(sender, args, 4).getUnformattedText();
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.give.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);
        if (flag) {
            entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.pop", 0.2f, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityplayer.inventoryContainer.detectAndSendChanges();
        }
        if (flag && itemstack.stackSize <= 0) {
            itemstack.stackSize = 1;
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
            final EntityItem entityitem1 = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
            if (entityitem1 != null) {
                entityitem1.func_174870_v();
            }
        }
        else {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.stackSize);
            final EntityItem entityitem2 = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
            if (entityitem2 != null) {
                entityitem2.setNoPickupDelay();
                entityitem2.setOwner(entityplayer.getName());
            }
        }
        CommandBase.notifyOperators(sender, this, "commands.give.success", itemstack.getChatComponent(), i, entityplayer.getName());
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getPlayers()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
    }
    
    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
