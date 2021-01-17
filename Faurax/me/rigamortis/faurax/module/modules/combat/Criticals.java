package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;

public class Criticals extends Module implements CombatHelper, MovementHelper
{
    private boolean stop;
    private int ticks;
    public static Value mode;
    
    static {
        Criticals.mode = new Value("Criticals", Boolean.TYPE, "Mode", "Jump", new String[] { "Packet", "New", "Mini Jumps", "Jump" });
    }
    
    public Criticals() {
        this.setName("Criticals");
        this.setKey("C");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Criticals.mode);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Jump")) {
                this.setModInfo(" §7Jump");
            }
            if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Packet")) {
                this.setModInfo(" §7Packet");
            }
            if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Mini Jumps")) {
                this.setModInfo(" §7Mini Jumps");
            }
            if (Criticals.mode.getSelectedOption().equalsIgnoreCase("New")) {
                this.setModInfo(" §7New");
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && Client.getClientHelper().canCrit()) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)e.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Jump")) {
                    Criticals.mc.thePlayer.jump();
                }
                if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Packet")) {
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.1625, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 4.0E-6, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 1.0E-6, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
                if (Criticals.mode.getSelectedOption().equalsIgnoreCase("Mini Jumps")) {
                    Criticals.mc.thePlayer.jump();
                    final EntityPlayerSP thePlayer = Criticals.mc.thePlayer;
                    thePlayer.motionY -= 0.30000001192092896;
                }
                if (Criticals.mode.getSelectedOption().equalsIgnoreCase("New")) {
                    Criticals.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.05000000074505806, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.012511000037193298, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                }
            }
        }
    }
}
