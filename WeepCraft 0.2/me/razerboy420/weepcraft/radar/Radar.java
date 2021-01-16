/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.radar;

import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;

public class Radar {
    public void draw(int x, int y) {
        Wrapper.drawBorderRect(x, y, x + 90, y + 90, ColorUtil.getHexColor(Weepcraft.borderColor), ColorUtil.getHexColor(Weepcraft.panlColor) & -2130706433, 1.0f);
        Gui.drawRect(x + 44, y + 1, x + 45, y + 89, ColorUtil.getHexColor(Weepcraft.borderColor));
        Gui.drawRect(x + 1, y + 44, x + 89, y + 45, ColorUtil.getHexColor(Weepcraft.borderColor));
        try {
            for (Object o : Wrapper.getWorld().loadedEntityList) {
                EntityLivingBase e;
                if (!(o instanceof EntityLivingBase) || (e = (EntityLivingBase)o) == Wrapper.getPlayer() || e instanceof EntityArmorStand) continue;
                float xpos = x;
                float ypos = y;
                xpos = (float)(e.posX - Wrapper.getPlayer().posX + 44.0 + (double)x);
                ypos = (float)(e.posZ - Wrapper.getPlayer().posZ + 44.0 + (double)y);
                if (xpos >= (float)(x + 88) || ypos >= (float)(y + 88) || ypos <= (float)(y + 2) || xpos <= (float)(x + 2)) continue;
                Gui.drawRect(xpos, ypos, xpos + 1.0f, ypos + 1.0f, e instanceof EntityPlayer ? (FriendManager.isFriend(e.getName()) ? -16711681 : -16711936) : -65536);
            }
        }
        catch (Exception o) {
            // empty catch block
        }
        Gui.drawRect(x + 44, y + 44, x + 45, y + 45, -1);
        Gui.drawCenteredString(Wrapper.fr(), "N", x + 45, y + 2, -1);
        Gui.drawCenteredString(Wrapper.fr(), "S", x + 45, y + 80, -1);
        Gui.drawCenteredString(Wrapper.fr(), "E", x + 90 - 5, y + 45 - 5, -1);
        Gui.drawCenteredString(Wrapper.fr(), "W", x + 5, y + 45 - 5, -1);
    }
}

