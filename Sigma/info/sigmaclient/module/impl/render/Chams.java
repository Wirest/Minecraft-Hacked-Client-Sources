package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRenderEntity;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {

    public Chams(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    @Override
    @RegisterEvent(events = {EventRenderEntity.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderEntity) {
            EventRenderEntity er = (EventRenderEntity) event;
            if (er.isPre() && er.getEntity() instanceof EntityPlayer) {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);
            } else if (er.isPost() && er.getEntity() instanceof EntityPlayer) {
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1.0f, 1100000.0f);
            }
        }
    }

}
