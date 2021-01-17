// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import me.nico.hush.commands.Command;

public class Ign extends Command
{
    Minecraft mc;
    
    public Ign() {
        super("ign", "to copy your ingamename.");
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 1) {
            try {
                GuiScreen.setClipboardString(this.mc.session.getUsername());
                Command.messageWithPrefix("§aYour ign has been copied.");
            }
            catch (Exception localException) {
                Command.messageWithPrefix("§fError");
            }
        }
        else {
            Command.messageWithPrefix("§cPlease only use §f.ign§c!");
        }
    }
}
