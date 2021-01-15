package net.minecraft.command.common;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase
{
    private static final Map SHORTCUTS = Maps.newHashMap();

    /**
     * Gets the name of the command
     */
    @Override
	public String getCommandName()
    {
        return "replaceitem";
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
        return "commands.replaceitem.usage";
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
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        else
        {
            boolean var3;

            if (args[0].equals("entity"))
            {
                var3 = false;
            }
            else
            {
                if (!args[0].equals("block"))
                {
                    throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
                }

                var3 = true;
            }

            byte var4;

            if (var3)
            {
                if (args.length < 6)
                {
                    throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
                }

                var4 = 4;
            }
            else
            {
                if (args.length < 4)
                {
                    throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
                }

                var4 = 2;
            }

            int var16 = var4 + 1;
            int var5 = this.getSlotForShortcut(args[var4]);
            Item var6;

            try
            {
                var6 = getItemByText(sender, args[var16]);
            }
            catch (NumberInvalidException var15)
            {
                if (Block.getBlockFromName(args[var16]) != Blocks.air)
                {
                    throw var15;
                }

                var6 = null;
            }

            ++var16;
            int var7 = args.length > var16 ? parseInt(args[var16++], 1, 64) : 1;
            int var8 = args.length > var16 ? parseInt(args[var16++]) : 0;
            ItemStack var9 = new ItemStack(var6, var7, var8);

            if (args.length > var16)
            {
                String var10 = getChatComponentFromNthArg(sender, args, var16).getUnformattedText();

                try
                {
                    var9.setTagCompound(JsonToNBT.getTagFromJson(var10));
                }
                catch (NBTException var14)
                {
                    throw new CommandException("commands.replaceitem.tagError", new Object[] {var14.getMessage()});
                }
            }

            if (var9.getItem() == null)
            {
                var9 = null;
            }

            if (var3)
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos var17 = parseBlockPos(sender, args, 1, false);
                World var11 = sender.getEntityWorld();
                TileEntity var12 = var11.getTileEntity(var17);

                if (var12 == null || !(var12 instanceof IInventory))
                {
                    throw new CommandException("commands.replaceitem.noContainer", new Object[] {Integer.valueOf(var17.getX()), Integer.valueOf(var17.getY()), Integer.valueOf(var17.getZ())});
                }

                IInventory var13 = (IInventory)var12;

                if (var5 >= 0 && var5 < var13.getSizeInventory())
                {
                    var13.setInventorySlotContents(var5, var9);
                }
            }
            else
            {
                Entity var18 = func_175768_b(sender, args[1]);
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

                if (var18 instanceof EntityPlayer)
                {
                    ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
                }

                if (!var18.replaceItemInInventory(var5, var9))
                {
                    throw new CommandException("commands.replaceitem.failed", new Object[] {Integer.valueOf(var5), Integer.valueOf(var7), var9 == null ? "Air" : var9.getChatComponent()});
                }

                if (var18 instanceof EntityPlayer)
                {
                    ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
                }
            }

            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, var7);
            notifyOperators(sender, this, "commands.replaceitem.success", new Object[] {Integer.valueOf(var5), Integer.valueOf(var7), var9 == null ? "Air" : var9.getChatComponent()});
        }
    }

    private int getSlotForShortcut(String shortcut) throws CommandException
    {
        if (!SHORTCUTS.containsKey(shortcut))
        {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] {shortcut});
        }
        else
        {
            return ((Integer)SHORTCUTS.get(shortcut)).intValue();
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"entity", "block"}): (args.length == 2 && args[0].equals("entity") ? getListOfStringsMatchingLastWord(args, this.getUsernames()) : ((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block")) ? ((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block")) ? null : getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys())) : getListOfStringsMatchingLastWord(args, SHORTCUTS.keySet())));
    }

    protected String[] getUsernames()
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
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }

    static
    {
        int var0;

        for (var0 = 0; var0 < 54; ++var0)
        {
            SHORTCUTS.put("slot.container." + var0, Integer.valueOf(var0));
        }

        for (var0 = 0; var0 < 9; ++var0)
        {
            SHORTCUTS.put("slot.hotbar." + var0, Integer.valueOf(var0));
        }

        for (var0 = 0; var0 < 27; ++var0)
        {
            SHORTCUTS.put("slot.inventory." + var0, Integer.valueOf(9 + var0));
        }

        for (var0 = 0; var0 < 27; ++var0)
        {
            SHORTCUTS.put("slot.enderchest." + var0, Integer.valueOf(200 + var0));
        }

        for (var0 = 0; var0 < 8; ++var0)
        {
            SHORTCUTS.put("slot.villager." + var0, Integer.valueOf(300 + var0));
        }

        for (var0 = 0; var0 < 15; ++var0)
        {
            SHORTCUTS.put("slot.horse." + var0, Integer.valueOf(500 + var0));
        }

        SHORTCUTS.put("slot.weapon", Integer.valueOf(99));
        SHORTCUTS.put("slot.armor.head", Integer.valueOf(103));
        SHORTCUTS.put("slot.armor.chest", Integer.valueOf(102));
        SHORTCUTS.put("slot.armor.legs", Integer.valueOf(101));
        SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100));
        SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
        SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
        SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
    }
}
