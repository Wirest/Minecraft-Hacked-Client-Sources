package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandGive extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "give";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.give.usage";
    }

    /**
     * Callback when the command is invoked
     *  
     * @param sender The {@link ICommandSender sender} who executed the command
     * @param args The arguments that were passed with the command
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.give.usage");
        }
        else
        {
            EntityPlayer entityplayer = getPlayer(sender, args[0]);
            Item item = getItemByText(sender, args[1]);
            int i = args.length >= 3 ? parseInt(args[2], 1, 64) : 1;
            int j = args.length >= 4 ? parseInt(args[3]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);

            if (args.length >= 5)
            {
                String s = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();

                try
                {
                    itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.give.tagError", nbtexception.getMessage());
                }
            }

            boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

            if (flag)
            {
                entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.pop", 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.inventoryContainer.detectAndSendChanges();
            }

            if (flag && itemstack.stackSize <= 0)
            {
                itemstack.stackSize = 1;
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
                EntityItem entityitem1 = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);

                if (entityitem1 != null)
                {
                    entityitem1.func_174870_v();
                }
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.stackSize);
                EntityItem entityitem = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);

                if (entityitem != null)
                {
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(entityplayer.getCommandSenderName());
                }
            }

            notifyOperators(sender, this, "commands.give.success", itemstack.getChatComponent(), i, entityplayer.getCommandSenderName());
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *  
     * @param args The arguments that were given
     * @param index The argument index that we are checking
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
