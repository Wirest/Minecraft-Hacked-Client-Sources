// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands.cmds;

import me.nico.hush.utils.MathUtils;
import net.minecraft.client.Minecraft;
import me.nico.hush.commands.Command;

public class Clip extends Command
{
    Minecraft mc;
    
    public Clip() {
        super("clip", "to glitch through a block");
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            Command.messageWithPrefix("§cPlease only use §f.clip <height>§c!");
        }
        else if (args.length == 1) {
            if (MathUtils.isDouble(args[0])) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Double.parseDouble(args[0]), Minecraft.thePlayer.posZ);
            }
            else {
                Command.messageWithPrefix("§7The §fheight §7must be a valid integer.");
            }
        }
        else {
            Command.messageWithPrefix(".clip §f<height>");
        }
    }
}
