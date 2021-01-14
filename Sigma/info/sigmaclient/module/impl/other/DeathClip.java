/**
 * Time: 5:04:38 AM
 * Date: Jan 2, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;

/**
 * @author cool1
 */
public class DeathClip extends Module {

    public DeathClip(ModuleData data) {
        super(data);
        settings.put(CLIP, new Setting<>(CLIP, new Options("Clip Mode", "VClip", new String[]{"VClip", "HClip"}), "Clip method."));
        settings.put(DIST, new Setting<>(DIST, 2.0, "Distance to clip.", 1, -10, 10));
        settings.put(MESSAGE, new Setting<>(MESSAGE, "/sethome", "Command to execute after clipping."));
    }

    private String DIST = "DIST";
    private String CLIP = "CLIP";
    private String MESSAGE = "MESSAGE";
    private boolean dead;
    private int waitTicks = 0;

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        String vclip = ((Options) settings.get(CLIP).getValue()).getSelected();
        float distance = ((Number) settings.get(DIST).getValue()).floatValue();
        if (em.isPre()) {
            if (vclip.equalsIgnoreCase("VClip") && mc.thePlayer.getHealth() == 0 && !dead) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + distance, mc.thePlayer.posZ);
                waitTicks++;
                dead = true;
            } else if (mc.thePlayer.getHealth() == 0 && !dead) {
                float yaw = mc.thePlayer.rotationYaw;
                {
                    mc.thePlayer.setPosition(
                            mc.thePlayer.posX + (distance * 2 * Math.cos(Math.toRadians(yaw + 90.0F))
                                    + 0 * 3 * Math.sin(Math.toRadians(yaw + 90.0F))),
                            mc.thePlayer.posY + +0.0010000000474974513D,
                            mc.thePlayer.posZ + (distance * 2 * Math.sin(Math.toRadians(yaw + 90.0F))
                                    - 0 * 3 * Math.cos(Math.toRadians(yaw + 90.0F))));
                }
                waitTicks++;
                dead = true;
            }
            if (this.waitTicks > 0 && dead) {
                waitTicks++;
                if (this.waitTicks >= 4) {
                    ChatUtil.sendChat(((String) settings.get(MESSAGE).getValue()));
                    this.waitTicks = 0;
                }
            }
            if (mc.thePlayer.getHealth() > 0) {
                dead = false;
            }
        }
    }

}
