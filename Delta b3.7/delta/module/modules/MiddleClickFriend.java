/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.player.EventClick
 *  me.xtrm.delta.api.event.events.player.EventClick$ClickType
 *  me.xtrm.delta.api.event.events.player.EventClick$MouseButton
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.module.modules;

import delta.client.DeltaClient;
import delta.friends.FriendsManager;
import delta.utils.PlayerUtils;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.player.EventClick;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class MiddleClickFriend
extends Module {
    @EventTarget
    public void onClick(EventClick eventClick) {
        if (eventClick.getMouseButton() != EventClick.MouseButton.MIDDLE || eventClick.getClickType() != EventClick.ClickType.RELEASE) {
            return;
        }
        if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityPlayer) {
            FriendsManager friendsManager = DeltaClient.instance.managers.friendsManager;
            EntityPlayer entityPlayer = (EntityPlayer)this.mc.pointedEntity;
            String string = entityPlayer.getCommandSenderName();
            if (friendsManager.contains(string)) {
                PlayerUtils.addChatMessage("Le joueur " + (Object)EnumChatFormatting.DARK_PURPLE + string + (Object)EnumChatFormatting.GRAY + " a \u00e9t\u00e9 " + (Object)EnumChatFormatting.RED + "enlev\u00e9" + (Object)EnumChatFormatting.GRAY + " de votre liste d'amis");
                friendsManager.removeFriend(string);
            } else {
                PlayerUtils.addChatMessage("Le joueur " + (Object)EnumChatFormatting.DARK_PURPLE + string + (Object)EnumChatFormatting.GRAY + " a \u00e9t\u00e9 " + (Object)EnumChatFormatting.GREEN + "ajout\u00e9" + (Object)EnumChatFormatting.GRAY + " dans votre liste d'amis");
                friendsManager.addFriend(string);
            }
        }
    }

    public MiddleClickFriend() {
        super("MCF", Category.Misc);
        this.setDescription("\"MiddleClickFriend\", ajoute en ami le joueur vis\u00e9 au click molette");
    }
}

