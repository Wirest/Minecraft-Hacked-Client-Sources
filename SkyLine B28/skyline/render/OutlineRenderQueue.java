package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Mineman;
import net.minecraft.client.renderer.GlStateManager;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventPriority;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventPriorityListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventSystem;
import skyline.specc.render.renderevnts.EventRenderWorld;
import skyline.specc.utils.Wrapper;

public class OutlineRenderQueue
{
    private static OutlineRenderQueue outlineRenderQueue;
    private ArrayList<OutlineRenderSubject> subjects;
    
    public OutlineRenderQueue() {
        this.subjects = new ArrayList<OutlineRenderSubject>();
        EventSystem.register(OutlineRenderQueue.outlineRenderQueue = this);
    }
    
    public void addRenderer(final OutlineRenderSubject subject) {
        this.subjects.add(subject);
    }
    
    public static OutlineRenderQueue getOutlineRenderQueue() {
        return OutlineRenderQueue.outlineRenderQueue;
    }
    
    @EventPriorityListener(priority = EventPriority.NORMAL)
    public void onRenderWorld(final EventRenderWorld event) {
        final Mineman mc = Wrapper.getMinecraft();
        for (final OutlineRenderSubject subject : this.subjects) {
            GL11.glPushMatrix();
            GlStateManager.disableLighting();
            subject.render(mc.timer.renderPartialTicks);
            GlStateManager.enableLighting();
            GL11.glPopMatrix();
        }
        final Color color = new Color(37, 83, 118);
        MCStencil.checkSetupFBO();
        final int list = GL11.glGenLists(1);
        Stencil.getInstance().startLayer();
        GL11.glPushMatrix();
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
        GL11.glDisable(2929);
        Stencil.getInstance().setBuffer(true);
        GL11.glNewList(list, 4864);
        GlStateManager.enableLighting();
        for (final OutlineRenderSubject subject2 : this.subjects) {
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GL11.glLineWidth(3.5f);
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GlStateManager.disableLighting();
            RenderUtil.setColor(color);
            subject2.render(mc.timer.renderPartialTicks);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
        GL11.glEndList();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        Stencil.getInstance().setBuffer(false);
        GL11.glPolygonMode(1032, 6914);
        GL11.glCallList(list);
        Stencil.getInstance().cropInside();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6914);
        Stencil.getInstance().stopLayer();
        GL11.glEnable(2929);
        GL11.glDeleteLists(list, 1);
        GL11.glPopMatrix();
        RenderUtil.setColor(Color.WHITE);
        this.subjects.clear();
    }
    
    public interface OutlineRenderSubject
    {
        void render(float p0);
    }
}
