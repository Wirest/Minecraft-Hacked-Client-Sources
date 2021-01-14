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
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Mouse;

public class FastUse extends Module {

    private NumberValue<Float> delay = new NumberValue<>("Speed", 10f, 1f, 31F, 1f);

    public FastUse() {
        super("FastUse", Category.EXPLOITS, new Color(0x4DAE99).getRGB());
        setDescription("Eat Faster.");
    }

    @Override
    public void onEnable() {
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        if (event.isPre()) {
            if (getMc().thePlayer.isUsingItem() && getMc().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood && getMc().thePlayer.onGround) {
                if (getMc().thePlayer.getItemInUseDuration() == delay.getValue()) {
                    for (int power = (getMc().thePlayer.getItemInUse().getMaxItemUseDuration() - getMc().thePlayer.getItemInUseDuration()), i = 0; i < power; ++i) {
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ, getMc().thePlayer.onGround));
                    }
                }
            }
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {

    }
}
