package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.combat.InfiniteAura;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.Vec3;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

public class PremiumInfiniteAuraAddon extends Module {
    private InfiniteAura infiniteAura;
    private BlockPos lastGround;

    public PremiumInfiniteAuraAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));
        ModuleManager m = Client.getModuleManager();
        infiniteAura = (InfiniteAura) m.get(InfiniteAura.class);
        infiniteAura.setPremiumAddon(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
            	

            }
        }else if(event instanceof EventPacket){
        	EventPacket ep = (EventPacket)event;
        	Packet p = ep.getPacket();
        	if(p instanceof S08PacketPlayerPosLook && !infiniteAura.canReach){
        		infiniteAura.canReach = true;
        		infiniteAura.timer.reset();
                S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
                pac.yaw = mc.thePlayer.rotationYaw;
                pac.pitch = mc.thePlayer.rotationPitch;
        	}
        	if(p instanceof C03PacketPlayer && !infiniteAura.canReach){
        		ep.setCancelled(true);
        	}
        }
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }

}
