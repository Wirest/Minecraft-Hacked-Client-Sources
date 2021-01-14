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

public class CommandGive extends CommandBase {
    private static final String __OBFID = "CL_00000502";

    @Override
    public String getCommandName() {
        return "give";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.give.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        } else {
            EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
            Item var4 = CommandBase.getItemByText(sender, args[1]);
            int var5 = args.length >= 3 ? CommandBase.parseInt(args[2], 1, 64) : 1;
            int var6 = args.length >= 4 ? CommandBase.parseInt(args[3]) : 0;
            ItemStack var7 = new ItemStack(var4, var5, var6);

            if (args.length >= 5) {
                String var8 = CommandBase.getChatComponentFromNthArg(sender, args, 4).getUnformattedText();

                try {
                    var7.setTagCompound(JsonToNBT.func_180713_a(var8));
                } catch (NBTException var10) {
                    throw new CommandException("commands.give.tagError", new Object[]{var10.getMessage()});
                }
            }

            boolean var11 = var3.inventory.addItemStackToInventory(var7);

            if (var11) {
                var3.worldObj.playSoundAtEntity(var3, "random.pop", 0.2F, ((var3.getRNG().nextFloat() - var3.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                var3.inventoryContainer.detectAndSendChanges();
            }

            EntityItem var9;

            if (var11 && var7.stackSize <= 0) {
                var7.stackSize = 1;
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var5);
                var9 = var3.dropPlayerItemWithRandomChoice(var7, false);

                if (var9 != null) {
                    var9.func_174870_v();
                }
            } else {
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var5 - var7.stackSize);
                var9 = var3.dropPlayerItemWithRandomChoice(var7, false);

                if (var9 != null) {
                    var9.setNoPickupDelay();
                    var9.setOwner(var3.getName());
                }
            }

            CommandBase.notifyOperators(sender, this, "commands.give.success", new Object[]{var7.getChatComponent(), Integer.valueOf(var5), var3.getName()});
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, getPlayers()) : (args.length == 2 ? CommandBase.func_175762_a(args, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username
     * parameter.
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
