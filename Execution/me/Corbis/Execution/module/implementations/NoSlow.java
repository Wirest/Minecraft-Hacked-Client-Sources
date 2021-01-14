package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public Setting packet;
    public NoSlow(){
        super("NoSlowDown", Keyboard.KEY_X, Category.MOVEMENT);
        Execution.instance.settingsManager.rSetting(packet = new Setting("Packet", this, false));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        this.setDisplayName("NoSlowDown §f[NCP]");
        if(event.getState() == Event.State.PRE){
            if(mc.thePlayer.isBlocking() && packet.getValBoolean() && Execution.instance.moduleManager.getModule(Aura.class).target == null){ mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }else {
            if(mc.thePlayer.isBlocking() && packet.getValBoolean() && Execution.instance.moduleManager.getModule(Aura.class).target == null){
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));

            }
        }
    }
}
