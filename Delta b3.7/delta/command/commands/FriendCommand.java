/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command.commands;

import delta.client.DeltaClient;
import delta.friends.FriendsManager;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.util.EnumChatFormatting;

public class FriendCommand
implements ICommand {
    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 1) {
            FriendsManager friendsManager = DeltaClient.instance.managers.friendsManager;
            if (friendsManager.contains(arrstring[0])) {
                friendsManager.removeFriend(arrstring[0]);
                this.printMessage(iCommandListener, "Le joueur " + (Object)EnumChatFormatting.DARK_PURPLE + arrstring[0] + (Object)EnumChatFormatting.GRAY + " a \u00e9t\u00e9 " + (Object)EnumChatFormatting.RED + "enlev\u00e9" + (Object)EnumChatFormatting.GRAY + " de votre liste d'amis");
            } else {
                friendsManager.addFriend(arrstring[0]);
                this.printMessage(iCommandListener, "Le joueur " + (Object)EnumChatFormatting.DARK_PURPLE + arrstring[0] + (Object)EnumChatFormatting.GRAY + " a \u00e9t\u00e9 " + (Object)EnumChatFormatting.GREEN + "ajout\u00e9" + (Object)EnumChatFormatting.GRAY + " dans votre liste d'amis");
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return true;
    }

    public String getHelp() {
        return "friend <Joueur>";
    }

    public String[] getAliases() {
        String[] arrstring = new String[5];
        arrstring[0] = "f";
        arrstring[1] = "friends";
        arrstring[2] = "ami";
        arrstring[3] = "amis";
        return arrstring;
    }

    public String getDescription() {
        return "Ajoute ou enl\u00e8ve un joueur de votre liste d'amis";
    }

    public String getName() {
        return "friend";
    }
}

