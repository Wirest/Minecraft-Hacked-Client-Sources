package moonx.ohare.client.module.impl.player;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.input.MouseEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 9/27/2019
 **/
public class MCF extends Module {
    public MCF() {
        super("MCF", Category.PLAYER, new Color(200,200,0).getRGB());
    }
    @Handler
    public void onMouse(MouseEvent event) {
        if (event.getButton() == 2 && getMc().objectMouseOver != null && getMc().objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) getMc().objectMouseOver.entityHit;
            String name = player.getName();
            if (Moonx.INSTANCE.getFriendManager().isFriend(name)) {
                Moonx.INSTANCE.getFriendManager().removeFriend(name);
                Printer.print("Removed " + name + " as a friend!");
            } else {
                Moonx.INSTANCE.getFriendManager().addFriend(name);
                Printer.print("Added " + name + " as a friend!");
            }
        }
    }
}
