package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.helpers.RenderHelper;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.module.modules.render.waypoint.*;
import net.minecraft.client.renderer.entity.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class WayPoints extends Module implements RenderHelper
{
    public WayPoints() {
        this.setName("WayPoints");
        this.setKey("");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + WayPointManager.points.size());
            for (final WayPoint point : WayPointManager.points) {
                this.drawName(point.name, point.pos.getX() - RenderManager.renderPosX - 0.5, point.pos.getY() - RenderManager.renderPosY + 2.4000000953674316, point.pos.getZ() - RenderManager.renderPosZ + 0.5);
                this.drawTracers(point.pos.getX(), point.pos.getY(), point.pos.getZ());
            }
        }
    }
    
    @EventTarget(4)
    public void setupViewBobbing(final EventBobWorld event) {
        if (this.isToggled()) {
            event.setCancelled(true);
        }
    }
    
    public void drawTracers(final double posX, final double posY, final double posZ) {
        final double n = posX - 0.5;
        WayPoints.mc.getRenderManager();
        final double x = n - RenderManager.renderPosX;
        final double n2 = posY + 1.7999999523162842;
        WayPoints.mc.getRenderManager();
        final double y = n2 - RenderManager.renderPosY;
        final double n3 = posZ + 0.5;
        WayPoints.mc.getRenderManager();
        final double z = n3 - RenderManager.renderPosZ;
        final GuiUtils guiUtils = WayPoints.guiUtils;
        GuiUtils.start3DGLConstants();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
        GL11.glBegin(1);
        GL11.glVertex3f(0.0f, WayPoints.mc.thePlayer.getEyeHeight(), 0.0f);
        GL11.glVertex3d(x, y - 1.5, z);
        GL11.glEnd();
        GL11.glDisable(2848);
        final GuiUtils guiUtils2 = WayPoints.guiUtils;
        GuiUtils.finish3DGLConstants();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void drawName(final String name, final double posX, final double posY, final double posZ) {
        float scale = (float)(0.07999999821186066 + WayPoints.mc.thePlayer.getDistance(posX, posY, posZ) / 10000.0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        if (scale >= 0.3f) {
            scale = 0.3f;
        }
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScaled(0.5, 0.5, 0.5);
        final GuiUtils guiUtils = WayPoints.guiUtils;
        Gui.drawRect(0 - WayPoints.mc.fontRendererObj.getStringWidth(name) / 2 - 2, -2, 0 + WayPoints.mc.fontRendererObj.getStringWidth(name) / 2 + 2, 10, -1441722095);
        WayPoints.guiUtils.drawOutlineRect(0 - WayPoints.mc.fontRendererObj.getStringWidth(name) / 2 - 2, -2.0f, 0 + WayPoints.mc.fontRendererObj.getStringWidth(name) / 2 + 2, 10.0f, -65281);
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);
        WayPoints.mc.fontRendererObj.func_175065_a(name, -WayPoints.mc.fontRendererObj.getStringWidth(name) / 2, 0.5f, -1, true);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }
}
