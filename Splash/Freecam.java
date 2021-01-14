package splash.client.modules.visual;

import com.mojang.authlib.GameProfile;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.player.EventPlayerUpdate;

import java.util.UUID;

/**
 * Author: Ice
 * Created: 15:57, 13-Jun-20
 * Project: Client
 */
public class Freecam extends Module {

    private EntityOtherPlayerMP freecamPlayer = null;
    private double[] oldPosition;

    public Freecam() {
        super("Freecam", "Mika gets downed by his wife x2.", ModuleCategory.VISUALS);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        clonePositions();
        freecamPlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        freecamPlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        freecamPlayer.noClip = true;
        freecamPlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(-420, freecamPlayer);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.setPositionAndRotation(oldPosition[0], oldPosition[1], oldPosition[2], mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        mc.theWorld.removeEntityFromWorld(-420);
        freecamPlayer = null;
    }

    @Collect
    public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
        mc.thePlayer.capabilities.isFlying = true;
    }

    private void clonePositions() {
        oldPosition = new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
    }
}
