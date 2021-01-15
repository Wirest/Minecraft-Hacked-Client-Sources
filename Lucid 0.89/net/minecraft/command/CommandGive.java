package net.minecraft.command;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
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
    @Override
	public String getCommandName()
    {
        return "give";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     *  
     * @param sender The {@link ICommandSender} who is requesting usage details.
     */
    @Override
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
    @Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(sender, args[0]);
            Item var4 = getItemByText(sender, args[1]);
            int var5 = args.length >= 3 ? parseInt(args[2], 1, 64) : 1;
            int var6 = args.length >= 4 ? parseInt(args[3]) : 0;
            ItemStack var7 = new ItemStack(var4, var5, var6);

            if (args.length >= 5)
            {
                String var8 = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();

                try
                {
                    var7.setTagCompound(JsonToNBT.getTagFromJson(var8));
                }
                catch (NBTException var10)
                {
                    throw new CommandException("commands.give.tagError", new Object[] {var10.getMessage()});
                }
            }

            boolean var11 = var3.inventory.addItemStackToInventory(var7);

            if (var11)
            {
                var3.worldObj.playSoundAtEntity(var3, "random.pop", 0.2F, ((var3.getRNG().nextFloat() - var3.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                var3.inventoryContainer.detectAndSendChanges();
            }

            EntityItem var9;

            if (var11 && var7.stackSize <= 0)
            {
                var7.stackSize = 1;
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, var5);
                var9 = var3.dropPlayerItemWithRandomChoice(var7, false);

                if (var9 != null)
                {
                    var9.func_174870_v();
                }
            }
            else
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, var5 - var7.stackSize);
                var9 = var3.dropPlayerItemWithRandomChoice(var7, false);

                if (var9 != null)
                {
                    var9.setNoPickupDelay();
                    var9.setOwner(var3.getCommandSenderName());
                }
            }

            notifyOperators(sender, this, "commands.give.success", new Object[] {var7.getChatComponent(), Integer.valueOf(var5), var3.getCommandSenderName()});
        }
    }

    /**
     * Return a list of options when the user types TAB
     *  
     * @param sender The {@link ICommandSender sender} who pressed TAB
     * @param args The arguments that were present when TAB was pressed
     * @param pos The block that the player is targeting, <b>May be {@code null}</b>
     */
    @Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
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
    @Override
	public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
