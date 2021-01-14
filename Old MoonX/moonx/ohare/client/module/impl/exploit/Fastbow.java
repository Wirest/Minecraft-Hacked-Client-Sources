package moonx.ohare.client.module.impl.exploit;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Mouse;

public class Fastbow extends Module {

    public Fastbow() {
        super("Fastbow", Category.EXPLOITS, new Color(0x4DAE99).getRGB());
        setDescription("Shoot Faster.");
    }

    @Override
    public void onEnable() {
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null || getMc().thePlayer.getCurrentEquippedItem() == null) return;
        if (event.isPre()) {
            if (Mouse.isButtonDown(1) && getMc().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && getMc().thePlayer.onGround) {
                //if (getMc().thePlayer.ticksExisted % 2 == 0) {
                    for (int power = 20, i = 0; i < power; ++i) {
                        //getMc().thePlayer.sendQueue.addToSendQueueNoEvents(new C0CPacketInput(getMc().thePlayer.moveStrafing, getMc().thePlayer.moveForward, getMc().thePlayer.movementInput.jump, getMc().thePlayer.movementInput.sneak));
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, getMc().thePlayer.onGround));
                    }
                //}
                getMc().playerController.onStoppedUsingItem(getMc().thePlayer);
                getMc().rightClickDelayTimer = 1;
            }
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {

    }
}
