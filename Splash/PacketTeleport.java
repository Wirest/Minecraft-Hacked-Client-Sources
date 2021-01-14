package splash.client.commands;

import org.apache.commons.lang3.math.NumberUtils;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import splash.Splash;
import splash.api.command.Command;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

public class PacketTeleport extends Command {


    private int x, z;
    private boolean gotonigga, niggay, fags;
    private final Minecraft mc = Minecraft.getMinecraft();
    private int moveUnder;
    private Stopwatch timerUtil = new Stopwatch();

    public PacketTeleport() {
        super("tp");
    }

    @Override
    public String usage() {
        return "tp <x> <y> <z>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        if (!gotonigga) {
            if (commandArguments.length > 1 && NumberUtils.isNumber(commandArguments[1]) && NumberUtils.isNumber(commandArguments[2])) {
                x = Integer.parseInt(commandArguments[1]);
                z = Integer.parseInt(commandArguments[2]);
                gotonigga = true;
                mc.timer.timerSpeed = 0.3f;
                ClientLogger.printToMinecraft("Teleporting to x:" + x + " z:" + z + ".");
                Splash.getInstance().getEventBus().register(this);
                timerUtil.reset();
            } else {
            	ClientLogger.printToMinecraft("Invalid arguments.");
            }
        } else {
        	ClientLogger.printToMinecraft("Already active!");
        }
    }
    

    @Collect
    public void onTick(EventPlayerUpdate event) {
        if (mc.thePlayer != null && moveUnder == 1) {
            if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) > 1) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 255, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                moveUnder = 0;
            }
        }
    }

    @Collect
    public void onPacket(EventPacketSend event) {
        if (event.getSentPacket() instanceof C03PacketPlayer) {
            if (!mc.thePlayer.isMoving() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
                event.setCancelled(true);
            }
            C03PacketPlayer packet = (C03PacketPlayer) event.getSentPacket();
            if (gotonigga) {
                mc.thePlayer.motionY = mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;
                if (!niggay) {
                    packet.setY(255);
                    packet.setX(Double.POSITIVE_INFINITY);
                    packet.setZ(Double.POSITIVE_INFINITY);
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                    moveUnder = 2;
                    niggay = true;
                }
                if (niggay && !fags) {
                    if (timerUtil.delay(400)) {
                        packet.setX(x);
                        packet.setZ(z);
                        mc.thePlayer.setPosition(x, mc.thePlayer.posY, z);
                        fags = true;
                        timerUtil.reset();
                    }
                } else {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, 255, z, false));
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true));
                    ClientLogger.printToMinecraft("Finished you have arrived at x:" + x + " z:" + z);
                    mc.timer.timerSpeed = 1f;
                    gotonigga = false;
                    niggay = false;
                    fags = false;
                    mc.renderGlobal.loadRenderers(); 
                }
            }
        } else {
            if (event.getSentPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
            if (event.getSentPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getSentPacket();
                if (packet.getChatComponent().getFormattedText().contains("You cannot go past the border.")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}