package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class FakeHacker extends Module {
    public FakeHacker(){
        super("FakeHacker", Keyboard.KEY_NONE, Category.EXPLOIT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityOtherPlayerMP){
                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) e;

                otherPlayer.rotationYaw = ThreadLocalRandom.current().nextInt(-180, 180);
                otherPlayer.rotationPitch = ThreadLocalRandom.current().nextInt(-180, 180);
                otherPlayer.swingItem();
                otherPlayer.setSneaking(otherPlayer.ticksExisted % 2 == 0);

            }
        }
    }
}
