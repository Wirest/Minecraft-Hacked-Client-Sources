// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import me.CheerioFX.FusionX.utils.EntityUtils2;

public abstract class Command
{
    public abstract String getAlias();
    
    public abstract String getDescription();
    
    public abstract String getSyntax();
    
    public abstract void onCommand(final String p0, final String[] p1) throws Exception;
    
    protected final int[] argsToPos(final EntityUtils2.TargetSettings targetSettings, final String... args) {
        int[] pos = new int[3];
        if (args.length == 3) {
            final int[] playerPos = { (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ };
            for (int i = 0; i < args.length; ++i) {
                if (MiscUtils.isInteger(args[i])) {
                    pos[i] = Integer.parseInt(args[i]);
                }
                else if (args[i].startsWith("~")) {
                    if (args[i].equals("~")) {
                        pos[i] = playerPos[i];
                    }
                    else if (MiscUtils.isInteger(args[i].substring(1))) {
                        pos[i] = playerPos[i] + Integer.parseInt(args[i].substring(1));
                    }
                    else {
                        FusionX.addChatMessage("Invalid coordinates.");
                    }
                }
                else {
                    FusionX.addChatMessage("Invalid coordinates.");
                }
            }
        }
        else if (args.length == 1) {
            final Entity entity = EntityUtils2.getEntityWithName(args[0], targetSettings);
            if (entity == null) {
                FusionX.addChatMessage("Entity \"" + args[0] + "\" could not be found.");
            }
            final BlockPos blockPos = new BlockPos(entity);
            pos = new int[] { blockPos.getX(), blockPos.getY(), blockPos.getZ() };
        }
        else {
            FusionX.addChatMessage("Invalid coordinates.");
        }
        return pos;
    }
}
