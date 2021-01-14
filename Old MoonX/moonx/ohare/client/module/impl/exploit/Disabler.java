package moonx.ohare.client.module.impl.exploit;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class Disabler extends Module {
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.FAITHFUL);
    private TimerUtil timer = new TimerUtil();
    private boolean sent;
    private LinkedList<Packet> verusQueue = new LinkedList<>();
    private int i;

    public Disabler() {
        super("Disabler", Category.EXPLOITS, new Color(0).getRGB());
        setDescription("Anticheat disabler");
        setHidden(true);
    }

    @Handler
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case POSCANCEL:
                if (event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
                    if (getMc().thePlayer.ticksExisted % 3 != 0)
                        event.setCanceled(true);
                }
                break;
            case NOPAYLOAD:
                if (event.isSending() && event.getPacket() instanceof C17PacketCustomPayload) {
                    event.setCanceled(true);
                }
                break;
            case TIMEOUT:
                if (event.isSending() && event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    event.setCanceled(true);
                }
                if (event.isSending() && event.getPacket() instanceof C00PacketKeepAlive) {
                    event.setCanceled(true);
                }
                break;
            case DEV:
                if (event.isSending()) {
                    if (event.getPacket() instanceof C03PacketPlayer) {
                        //Printer.print("d ");
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0CPacketInput(getMc().thePlayer.moveStrafing, getMc().thePlayer.moveForward, getMc().thePlayer.movementInput.jump, getMc().thePlayer.movementInput.sneak));
                    }
                    if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                        C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) event.getPacket();
                     //   Printer.print("trans " + packet.getUid() + " ");
                    }
                    if (event.getPacket() instanceof C00PacketKeepAlive) {
                        //event.setCanceled(true);
                        C00PacketKeepAlive packet = (C00PacketKeepAlive) event.getPacket();
                        /*long key = packet.getKey();
                        long seconds = (key * 1000000L)/(long) 1E9; // seconds
                        long minutes = seconds/60; // minutes
                        long hours = minutes/60; // hours
                        long days = hours/24; // hours
                        long day=minutes/1440;
                        long rem=minutes%1440;
                        long hour=rem/60;
                        long Minute=rem%60;
                        StringBuilder sb=new StringBuilder();
                        if(day>0)
                            sb.append(day+" days ");

                        if(hour>0)
                            sb.append(hour+" hours ");

                        if(Minute>0)
                            sb.append(Minute+" minutes");
                        Printer.print("alive " + packet.getKey() + " days: " + sb);*/
                    }
                }
                break;
            case FAITHFUL:
                if (getMc().thePlayer != null && getMc().thePlayer.getDistance(getMc().thePlayer.prevPosX, getMc().thePlayer.prevPosY, getMc().thePlayer.prevPosZ) > 0.6 && event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
                    if (i > 2) {
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.prevPosX + ((getMc().thePlayer.posX - getMc().thePlayer.prevPosX) / 2), getMc().thePlayer.prevPosY + ((getMc().thePlayer.posY - getMc().thePlayer.prevPosY) / 2), getMc().thePlayer.prevPosZ + ((getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ) / 2), ((C03PacketPlayer) event.getPacket()).isOnGround()));
                        i = 0;
                    } else {
                        event.setCanceled(true);
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(-Integer.MAX_VALUE));
                    }
                    i++;
                }
                break;
            case VELT:
                if (!event.isSending() && event.getPacket() instanceof S00PacketKeepAlive) {
                    sent = true;
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    event.setCanceled(true);
                }
                break;
            default:
                break;
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case SPECTATOR:
                if (event.isPre()) {
                    getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C18PacketSpectate((UUID.randomUUID())));
                }
                break;
            case TRANSACTIONSPAM:
                if (event.isPre()) {
                    getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0FPacketConfirmTransaction(0, (short) MathUtils.getRandomInRange(-32767, 32767), false));
                }
                break;
            case NULLPLACE:
                if (event.isPre()) {
                    BlockPos pos = new BlockPos(-1, -1, -1);
                    getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C08PacketPlayerBlockPlacement(pos, 1, null, 0, 0, 0));
                }
                break;
            case DEV:
                if (event.isPre()) {
                    if (getMc().thePlayer.ticksExisted % 8 == 0) {
                        //getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, 0, getMc().thePlayer.posZ, false));
                       // Printer.print("d ");
                       // event.setY(getMc().thePlayer.posY + 1E308);
                        //event.setOnGround(false);
                        //getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX + 1E308, getMc().thePlayer.posY, getMc().thePlayer.posZ, false));
                    }
                    /*if (getMc().thePlayer.ticksExisted <= 1) {
                        Printer.print("GAY " + getMc().thePlayer.ticksExisted);
                        for (int i = 0; i < 2500; ++i) {
                            getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0FPacketConfirmTransaction(0, (short) 0, false));
                        }
                        //getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(1));
                       // Printer.print("GAY");
                    }*/
                            //    getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0FPacketConfirmTransaction(0, (short) 0, false));

                        //getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(1));
                        // Printer.print("GAY");


                }
                break;
            case VELT:
                if (event.isPre()) {
                    if (sent) {
                        final float[] offsets = new float[]{0.08f, 0.0925f, -0.025f, 0.01F, -0.025f, 0.05F, -0.045f};
                        if (i > offsets.length - 1) i = 0;
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + offsets[i], getMc().thePlayer.posZ, getMc().thePlayer.onGround));
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C00PacketKeepAlive(-11111));
                        i++;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEnable() {
        timer.reset();
        sent = false;
        i = 0;
    }

    public enum Mode {
        FAITHFUL, VELT, TIMEOUT, NOPAYLOAD, POSCANCEL, NULLPLACE, TRANSACTIONSPAM, SPECTATOR, DEV
    }
}
