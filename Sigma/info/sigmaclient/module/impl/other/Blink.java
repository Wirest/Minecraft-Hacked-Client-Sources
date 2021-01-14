/**
 * Time: 11:39:36 PM
 * Date: Dec 25, 2016
 * Creator: cool1
 */
package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.*;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author cool1
 */
public class Blink extends Module {

    private List<Packet> packets = new CopyOnWriteArrayList<>();
    private List<Vec3> crumbs = new CopyOnWriteArrayList<>();
    private String BREADCRUMBS = "CRUMBS";

    public Blink(ModuleData data) {
        super(data);
        settings.put(BREADCRUMBS, new Setting<>(BREADCRUMBS, true, "Draws a line on your blink path."));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        crumbs.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        crumbs.clear();
        for (Packet packet : packets) {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
        }
        packets.clear();
    }

    private info.sigmaclient.util.Timer timer = new Timer();

    @Override
    @RegisterEvent(events = {EventPacket.class, EventRender3D.class})
    public void onEvent(Event event) {
        setSuffix((PlayerUtil.isMoving() ? "\2479" : "\2478") + packets.size());
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isOutgoing() && ep.isPre()&& (ep.getPacket() instanceof C03PacketPlayer || ep.getPacket() instanceof C00PacketKeepAlive)) {
                packets.add(ep.getPacket());
                ep.setCancelled(true);
            }
            if(ep.isIncoming() && ep.isPre()){
            	if(ep.getPacket() instanceof S08PacketPlayerPosLook){
            		ep.setCancelled(true);
            	}
            }
        }
        if (event instanceof EventRender3D && ((Boolean) settings.get(BREADCRUMBS).getValue())) {
            if (timer.delay(50)) {
                crumbs.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
                timer.reset();
            }
            if (!crumbs.isEmpty() && crumbs.size() > 2) {
                for (int i = 1; i < crumbs.size(); i++) {
                    Vec3 vecBegin = crumbs.get(i - 1);
                    Vec3 vecEnd = crumbs.get(i);
                    int color = Colors.getColor(164, 24, 188);
                    float beginX = (float) ((float) vecBegin.xCoord - RenderManager.renderPosX);
                    float beginY = (float) ((float) vecBegin.yCoord - RenderManager.renderPosY);
                    float beginZ = (float) ((float) vecBegin.zCoord - RenderManager.renderPosZ);
                    float endX = (float) ((float) vecEnd.xCoord - RenderManager.renderPosX);
                    float endY = (float) ((float) vecEnd.yCoord - RenderManager.renderPosY);
                    float endZ = (float) ((float) vecEnd.zCoord - RenderManager.renderPosZ);
                    final boolean bobbing = mc.gameSettings.viewBobbing;
                    mc.gameSettings.viewBobbing = false;
                    RenderingUtil.drawLine3D(beginX, beginY, beginZ, endX, endY, endZ, color);
                    mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
    }

}
