package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class Sprint extends Module {
    private boolean print;
    private StringBuilder builder = new StringBuilder();

    public Sprint() {
        super("Sprint", Category.MOVEMENT, new Color(0, 255, 0).getRGB());
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        getMc().thePlayer.setSprinting(canSprint());
    }

     private boolean canSprint() {
        return getMc().thePlayer.getFoodStats().getFoodLevel() > 7 && (getMc().gameSettings.keyBindForward.isKeyDown() | getMc().gameSettings.keyBindBack.isKeyDown() || getMc().gameSettings.keyBindLeft.isKeyDown() || getMc().gameSettings.keyBindRight.isKeyDown());
    }
}
