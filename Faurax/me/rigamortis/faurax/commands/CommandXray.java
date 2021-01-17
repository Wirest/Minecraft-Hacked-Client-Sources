package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.render.*;
import net.minecraft.block.*;
import me.rigamortis.faurax.*;
import net.minecraft.client.*;

public class CommandXray extends Command
{
    public CommandXray() {
        super("xray", new String[] { "x" });
    }
    
    @Argument
    protected String xrayHelp() {
        final String help = "Add <BlockID>, Remove <BlockID>, Clear";
        return help;
    }
    
    @Argument(handles = { "add", "a" })
    protected String xrayAdd(final int id) {
        if (Xray.contains(id)) {
            return "Xray already contains §6" + Block.getBlockById(id).getLocalizedName() + "§f";
        }
        Xray.add(id);
        Client.getConfig().saveXray();
        if (Xray.enabled) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return "Xray §badded §6" + Block.getBlockById(id).getLocalizedName() + "§f";
    }
    
    @Argument(handles = { "remove", "r", "rem" })
    protected String xrayRemove(final int id) {
        if (!Xray.contains(id)) {
            return "Xray §bdoesn't contain §6" + Block.getBlockById(id).getLocalizedName() + "§f";
        }
        if (Xray.contains(id)) {
            Xray.remove(id);
            Client.getConfig().saveXray();
        }
        if (Xray.enabled) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return "Xray §bremoved §6" + Block.getBlockById(id).getLocalizedName() + "§f";
    }
    
    @Argument(handles = { "clear", "c" })
    protected String xrayClear() {
        Xray.clear();
        Client.getConfig().saveXray();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        return "Xray cleared all blocks§f";
    }
}
