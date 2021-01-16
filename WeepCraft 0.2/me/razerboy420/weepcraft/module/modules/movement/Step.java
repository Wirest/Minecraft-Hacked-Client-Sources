/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPrePlayerUpdate;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;

@Module.Mod(category=Module.Category.MOVEMENT, description="Step up blocks on NCP", key=0, name="Step")
public class Step
extends Module {

    @EventTarget
    public void onUpdate(EventPrePlayerUpdate event) {
        	AxisAlignedBB box = Wrapper.getPlayer().getEntityBoundingBox().offset(0.0, 0.05, 0.0).expandXyz(0.05);
            if (!Minecraft.world.getCollisionBoxes(Wrapper.getPlayer(), box.offset(0.0, 1.0, 0.0)).isEmpty()) {
                return;
            }
            double stepHeight = -1.0;
            for (AxisAlignedBB bb : Minecraft.world.getCollisionBoxes(Wrapper.getPlayer(), box)) {
                if (bb.maxY <= stepHeight) continue;
                stepHeight = bb.maxY;
            }
            if ((stepHeight -= Wrapper.getPlayer().posY) < 0.0 || stepHeight > 1.0) {
                return;
            }
        	if(Wrapper.getPlayer().isCollidedHorizontally){
        		Wrapper.mc().player.connection.sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.42 * stepHeight, Wrapper.getPlayer().posZ, Wrapper.getPlayer().onGround));
        		Wrapper.mc().player.connection.sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.753 * stepHeight, Wrapper.getPlayer().posZ, Wrapper.getPlayer().onGround));
        		Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 1.0 * stepHeight, Wrapper.getPlayer().posZ);
//        		Wrapper.getPlayer().jump();
//        		Wrapper.getPlayer().motionY = -0.1f;
        }
    }

    @Override
    public void onDisable() {
        Wrapper.getPlayer().stepHeight = 0.6f;
        super.onDisable();
    }
}

