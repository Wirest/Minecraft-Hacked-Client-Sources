/**
 * Time: 1:48:23 AM
 * Date: Jan 2, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.render;

import java.awt.Color;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.module.Module;

/**
 * @author cool1
 */
public class DONOTFUCKINGDIEYOURETARD extends Module {

    /**
     * @param data
     */
    public DONOTFUCKINGDIEYOURETARD(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventRenderGui.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            int width = er.getResolution().getScaledWidth() / 2;
            int height = er.getResolution().getScaledHeight() / 2;
            String XD = "" + (int) mc.thePlayer.getHealth();
            int XDDD = mc.fontRendererObj.getStringWidth(XD);
            float health = mc.thePlayer.getHealth();
            if (health > 20) {
                health = 20;
            }
            int red = (int) Math.abs((((health * 5) * 0.01f) * 0) + ((1 - (((health * 5) * 0.01f))) * 255));
            int green = (int) Math.abs((((health * 5) * 0.01f) * 255) + ((1 - (((health * 5) * 0.01f))) * 0));
            Color customColor = new Color(red, green, 0).brighter();
            mc.fontRendererObj.drawStringWithShadow(XD, ((-XDDD / 2) + width), height - 17, customColor.getRGB());
        }
    }

}
