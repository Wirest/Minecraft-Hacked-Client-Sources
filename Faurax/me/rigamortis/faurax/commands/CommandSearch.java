package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.module.modules.render.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import me.cupboard.command.argument.*;

public class CommandSearch extends Command
{
    public CommandSearch() {
        super("search", new String[0]);
    }
    
    @Argument(handles = { "add", "a" })
    protected String searchAdd(final int id) {
        if (Search.contains(id)) {
            return "Search already contains §6" + Block.getBlockById(id).getLocalizedName() + "§f";
        }
        Search.add(id);
        Search.blocks.clear();
        if (Search.enabled) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return "Search §badded §6" + Block.getBlockById(id).getLocalizedName() + "§f";
    }
    
    @Argument(handles = { "remove", "r", "rem" })
    protected String xrayRemove(final int id) {
        if (!Search.contains(id)) {
            return "Search §bdoesn't contain §6" + Block.getBlockById(id).getLocalizedName() + "§f";
        }
        Search.remove(id);
        Search.blocks.clear();
        if (Search.enabled) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return "Search §bremoved §6" + Block.getBlockById(id).getLocalizedName() + "§f";
    }
    
    @Argument(handles = { "clear", "c" })
    protected String searchClear() {
        Search.clear();
        Search.blocks.clear();
        if (Search.enabled) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return "Search cleared all blocks§f";
    }
}
