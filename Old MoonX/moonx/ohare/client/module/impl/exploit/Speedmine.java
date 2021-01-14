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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Mouse;

public class Speedmine extends Module {

    public Speedmine() {
        super("Speedmine", Category.EXPLOITS, new Color(0x4DAE99).getRGB());
        setDescription("Mine Faster.");
    }

    @Override
    public void onDisable() {
        getMc().thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }

    @Override
    public void onEnable() {
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        if (event.isPre()) {
            getMc().playerController.blockHitDelay = 0;
            boolean item = getMc().thePlayer.getCurrentEquippedItem() == null;
            getMc().thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, (int)(item ? 1 : 0)));
        }
    }
}
