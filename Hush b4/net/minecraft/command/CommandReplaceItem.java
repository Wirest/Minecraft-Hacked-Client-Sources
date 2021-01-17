// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import java.util.Map;

public class CommandReplaceItem extends CommandBase
{
    private static final Map<String, Integer> SHORTCUTS;
    
    static {
        SHORTCUTS = Maps.newHashMap();
        for (int i = 0; i < 54; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.container." + i, i);
        }
        for (int j = 0; j < 9; ++j) {
            CommandReplaceItem.SHORTCUTS.put("slot.hotbar." + j, j);
        }
        for (int k = 0; k < 27; ++k) {
            CommandReplaceItem.SHORTCUTS.put("slot.inventory." + k, 9 + k);
        }
        for (int l = 0; l < 27; ++l) {
            CommandReplaceItem.SHORTCUTS.put("slot.enderchest." + l, 200 + l);
        }
        for (int i2 = 0; i2 < 8; ++i2) {
            CommandReplaceItem.SHORTCUTS.put("slot.villager." + i2, 300 + i2);
        }
        for (int j2 = 0; j2 < 15; ++j2) {
            CommandReplaceItem.SHORTCUTS.put("slot.horse." + j2, 500 + j2);
        }
        CommandReplaceItem.SHORTCUTS.put("slot.weapon", 99);
        CommandReplaceItem.SHORTCUTS.put("slot.armor.head", 103);
        CommandReplaceItem.SHORTCUTS.put("slot.armor.chest", 102);
        CommandReplaceItem.SHORTCUTS.put("slot.armor.legs", 101);
        CommandReplaceItem.SHORTCUTS.put("slot.armor.feet", 100);
        CommandReplaceItem.SHORTCUTS.put("slot.horse.saddle", 400);
        CommandReplaceItem.SHORTCUTS.put("slot.horse.armor", 401);
        CommandReplaceItem.SHORTCUTS.put("slot.horse.chest", 499);
    }
    
    @Override
    public String getCommandName() {
        return "replaceitem";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.replaceitem.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        boolean flag;
        if (args[0].equals("entity")) {
            flag = false;
        }
        else {
            if (!args[0].equals("block")) {
                throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }
            flag = true;
        }
        int i;
        if (flag) {
            if (args.length < 6) {
                throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }
            i = 4;
        }
        else {
            if (args.length < 4) {
                throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }
            i = 2;
        }
        final int j = this.getSlotForShortcut(args[i++]);
        Item item;
        try {
            item = CommandBase.getItemByText(sender, args[i]);
        }
        catch (NumberInvalidException numberinvalidexception) {
            if (Block.getBlockFromName(args[i]) != Blocks.air) {
                throw numberinvalidexception;
            }
            item = null;
        }
        ++i;
        final int k = (args.length > i) ? CommandBase.parseInt(args[i++], 1, 64) : 1;
        final int l = (args.length > i) ? CommandBase.parseInt(args[i++]) : 0;
        ItemStack itemstack = new ItemStack(item, k, l);
        if (args.length > i) {
            final String s = CommandBase.getChatComponentFromNthArg(sender, args, i).getUnformattedText();
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.replaceitem.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (itemstack.getItem() == null) {
            itemstack = null;
        }
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
            final World world = sender.getEntityWorld();
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null || !(tileentity instanceof IInventory)) {
                throw new CommandException("commands.replaceitem.noContainer", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            final IInventory iinventory = (IInventory)tileentity;
            if (j >= 0 && j < iinventory.getSizeInventory()) {
                iinventory.setInventorySlotContents(j, itemstack);
            }
        }
        else {
            final Entity entity = CommandBase.func_175768_b(sender, args[1]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
            if (!entity.replaceItemInInventory(j, itemstack)) {
                throw new CommandException("commands.replaceitem.failed", new Object[] { j, k, (itemstack == null) ? "Air" : itemstack.getChatComponent() });
            }
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
        CommandBase.notifyOperators(sender, this, "commands.replaceitem.success", j, k, (itemstack == null) ? "Air" : itemstack.getChatComponent());
    }
    
    private int getSlotForShortcut(final String shortcut) throws CommandException {
        if (!CommandReplaceItem.SHORTCUTS.containsKey(shortcut)) {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] { shortcut });
        }
        return CommandReplaceItem.SHORTCUTS.get(shortcut);
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block") : ((args.length == 2 && args[0].equals("entity")) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getUsernames()) : ((args.length >= 2 && args.length <= 4 && args[0].equals("block")) ? CommandBase.func_175771_a(args, 1, pos) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? null : CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys())) : CommandBase.getListOfStringsMatchingLastWord(args, CommandReplaceItem.SHORTCUTS.keySet()))));
    }
    
    protected String[] getUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
}
