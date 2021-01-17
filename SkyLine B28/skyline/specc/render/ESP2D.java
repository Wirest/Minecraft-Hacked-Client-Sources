package skyline.specc.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.PrizonRenderUtils;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.specc.render.modules.ESP;
import skyline.specc.render.renderevnts.EventRenderScreen;
import skyline.specc.render.renderevnts.EventRenderWorld;
import skyline.specc.render.renderevnts.EventRenderer2D;
import java.awt.*;


public class ESP2D extends ModMode<ESP>
{
    public ESP2D(ESP parent) {
        super(parent, "2D");
    }

    @EventListener
    public void onScreenRender(EventRenderWorld event) {
        for (final Object entity : this.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                final EntityLivingBase p = (EntityPlayer)entity;
                if (!p.isEntityAlive() || p == this.mc.thePlayer || !RenderUtil.isInFrustumView(p)) {
                    continue;
                }
                final double x = RenderUtil.interpolate(p.posX, p.lastTickPosX);
                final double y = RenderUtil.interpolate(p.posY, p.lastTickPosY);
                final double z = RenderUtil.interpolate(p.posZ, p.lastTickPosZ);
/*                final int color = (p.hurtTime >= 1) ? new Color(255, 20, 20).getRGB() : new Color(255, 255, 255).getRGB();*/
                int color= (p.hurtTime >= 1) ? new Color(255, 20, 20).getRGB() : new Color(0, 128, 0).getRGB();
                RenderUtil.draw2DCorner(p, p.posX - RenderManager.renderPosX, p.posY - RenderManager.renderPosY, p.posZ - RenderManager.renderPosZ, color);
            }
        }
    }
}
