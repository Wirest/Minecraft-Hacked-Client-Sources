package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.tileentity.*;
import me.rigamortis.faurax.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;

public class Interact extends Module implements WorldHelper, RenderHelper
{
    public Interact() {
        this.setName("Interact");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    public TileEntity getBestChest(final double range) {
        TileEntity tempEntity = null;
        double dist = range;
        for (final Object o : Interact.mc.theWorld.loadedTileEntityList) {
            final TileEntity tileEntity = (TileEntity)o;
            final double curDist = Client.getClientHelper().getDistanceToEntity(tileEntity);
            if (curDist <= dist) {
                dist = curDist;
                tempEntity = tileEntity;
            }
        }
        return tempEntity;
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            final TileEntity tileEntity = this.getBestChest(6.0);
            if (Interact.mc.currentScreen == null && Interact.mc.gameSettings.keyBindUseItem.pressed) {
                Interact.mc.gameSettings.keyBindUseItem.pressed = false;
                final double posX = tileEntity.getPos().getX();
                final double posY = tileEntity.getPos().getY();
                final double posZ = tileEntity.getPos().getZ();
                Interact.mc.thePlayer.swingItem();
                Interact.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Client.getClientHelper().getBlockPos(posX, posY, posZ), Client.getClientHelper().getEnumFacing((float)posX, (float)posY, (float)posZ).getIndex(), Interact.mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
            }
        }
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            final TileEntity tileEntity = this.getBestChest(6.0);
            final double n = tileEntity.getPos().getX();
            Interact.mc.getRenderManager();
            final double x = n - RenderManager.renderPosX;
            final double n2 = tileEntity.getPos().getY();
            Interact.mc.getRenderManager();
            final double y = n2 - RenderManager.renderPosY;
            final double n3 = tileEntity.getPos().getZ();
            Interact.mc.getRenderManager();
            final double z = n3 - RenderManager.renderPosZ;
            Interact.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 289699737);
            Interact.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 290813760);
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
