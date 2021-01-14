package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventRender2D;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.Iterator;

public final class TargetAlert
        extends Module {
    private final Value<Boolean> showDistance = new Value("Show distance", Boolean.valueOf(true), this, new String[0]);
    private final Value<Float> maxDistance = new Value("Max distance", Float.valueOf(20.0F), 10.0F, 50.0F, 0, this, new String[0]);

    public TargetAlert(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventRender2D)) {
            EventRender2D localEventRender2D = (EventRender2D) paramEvent;
            Object localObject = null;
            float f1 = ((Float) this.maxDistance.value).floatValue();
            Iterator localIterator = MC.theWorld.playerEntities.iterator();
            while (localIterator.hasNext()) {
                EntityPlayer localEntityPlayer = (EntityPlayer) localIterator.next();
                if ((localEntityPlayer != null) && (localEntityPlayer != MC.thePlayer)) {
                    float f3 = MC.thePlayer.getDistanceToEntity(localEntityPlayer);
                    if (f3 <= f1) {
                        f1 = f3;
                        localObject = localEntityPlayer;
                    }
                }
            }
            if (localObject != null) {
                float f2 = MathHelper.wrapAngleTo180_float(MC.thePlayer.rotationYaw - modification.main.Modification.ROTATION_UTIL.rotationsToEntity(localObject)[0]);
                if (Math.abs(f2) >= 90.0F) {
                    double d1 = localEventRender2D.resolution.getScaledWidth() / 2.0F;
                    double d2 = localEventRender2D.resolution.getScaledHeight() / 2.0F;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(d1, d2, 1.0D);
                    GlStateManager.rotate(-(f2 + 100.0F), 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(-d1, -d2, 1.0D);
                    localEventRender2D.resolution.getScaledWidth().drawString(-2 | 0xA, localEventRender2D.resolution.getScaledHeight(), -2 - 2, Color.RED.getRGB());
                    GlStateManager.popMatrix();
                    if (!((Boolean) this.showDistance.value).booleanValue()) {
                        return;
                    }
                    String str = Integer.toString(Math.round(MC.thePlayer.getDistanceToEntity((Entity) localObject))).concat("m");
                    MC.fontRendererObj.drawStringWithShadow(str, (localEventRender2D.resolution.getScaledWidth() - MC.fontRendererObj.getStringWidth(str)) / 2.0F, (localEventRender2D.resolution.getScaledHeight() - MC.fontRendererObj.FONT_HEIGHT * 3) / 2.0F, Color.RED.getRGB());
                }
            }
        }
    }

    protected void onDeactivated() {
    }
}




