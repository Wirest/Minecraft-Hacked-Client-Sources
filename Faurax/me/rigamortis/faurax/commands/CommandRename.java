package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import me.cupboard.command.argument.*;

public class CommandRename extends Command
{
    public CommandRename() {
        super("rename", new String[] { "rn" });
    }
    
    @Argument
    protected String rename(final String name) {
        final ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        name.replaceAll("&1", "§1");
        item.setStackDisplayName(name);
        return "Done.";
    }
}
