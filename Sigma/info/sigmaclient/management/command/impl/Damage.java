/**
 * Time: 10:14:08 PM
 * Date: Jan 4, 2017
 * Creator: cool1
 */
package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author cool1
 */
public class Damage extends Command {

    static Minecraft mc = Minecraft.getMinecraft();

    /**
     * @param names
     * @param description
     */
    public Damage(String[] names, String description) {
        super(names, description);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Fireable#fire(java.lang.String[])
     */
    @Override
    public void fire(String[] args) {
        damagePlayer();
    }

    public static void damagePlayer() {
        for (int index = 0; index < 70; index++) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06D, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1D, mc.thePlayer.posZ, false));
    }

    /* (non-Javadoc)
     * @see me.arithmo.command.Command#getUsage()
     */
    @Override
    public String getUsage() {
        // TODO Auto-generated method stub
        return "damage";
    }

    @Override
    public void onEvent(Event event) {

    }
}
