package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventRender2D;
import modification.events.EventRender3D;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.RenderUtil;
import modification.utilities.TestShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

public final class Chams
        extends Module {
    private final TestShaderUtil chamsShader = new TestShaderUtil("chams");
    private final Value<Float> alpha = new Value("Alpha", Float.valueOf(0.5F), 0.1F, 1.0F, 1, this, new String[0]);
    private final Value<Boolean> players = new Value("Players", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> mobs = new Value("Mobs", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> chests = new Value("Chests", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> items = new Value("Items", Boolean.valueOf(true), this, new String[0]);
    private Framebuffer framebuffer;
    private boolean render;

    public Chams(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventRender3D)) {
            this.framebuffer = Modification.RENDER_UTIL.createFrameBuffer(this.framebuffer);
            this.framebuffer.framebufferClear();
            this.framebuffer.bindFramebuffer(true);
            this.render = renderEntities(((EventRender3D) paramEvent).partialTicks);
            this.framebuffer.unbindFramebuffer();
            MC.getFramebuffer().bindFramebuffer(true);
        }
        if (((paramEvent instanceof EventRender2D)) && (this.framebuffer != null) && (this.render)) {
            MC.getFramebuffer().bindFramebuffer(true);
            ARBShaderObjects.glUseProgramObjectARB(this.chamsShader.program);
            ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(this.chamsShader.program, "u_texture"), 0);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(this.chamsShader.program, "u_alpha"), ((Float) this.alpha.value).floatValue());
            GL11.glBindTexture(3553, this.framebuffer.framebufferTexture);
            this.chamsShader.renderShader(((EventRender2D) paramEvent).resolution);
        }
    }

    protected void onDeactivated() {
    }

    private boolean renderEntities(float paramFloat) {
        boolean bool = false;
        Object localObject;
        for (int i = 0; i < MC.theWorld.loadedTileEntityList.size(); i++) {
            localObject = (TileEntity) MC.theWorld.loadedTileEntityList.get(i);
            if (((localObject instanceof TileEntityChest)) && (((Boolean) this.chests.value).booleanValue())) {
                RenderUtil.renderOutlinesCustom = true;
                TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity) localObject, paramFloat, -1);
                RenderUtil.renderOutlinesCustom = false;
                bool = true;
            }
        }
        Iterator localIterator = MC.theWorld.loadedEntityList.iterator();
        while (localIterator.hasNext()) {
            localObject = (Entity) localIterator.next();
            if ((((localObject instanceof EntityItem)) && (((Boolean) this.items.value).booleanValue())) || (((localObject instanceof EntityPlayer)) && (((Boolean) this.players.value).booleanValue())) || (((localObject instanceof EntityMob)) && (((Boolean) this.mobs.value).booleanValue())) || (((localObject instanceof EntityAnimal)) && (((Boolean) this.mobs.value).booleanValue()))) {
                if ((localObject != MC.thePlayer) || (MC.gameSettings.thirdPersonView != 0)) {
                    RenderUtil.renderOutlinesCustom = true;
                    MC.getRenderManager().renderEntitySimple((Entity) localObject, paramFloat);
                    GlStateManager.disableLighting();
                    RenderUtil.renderOutlinesCustom = false;
                    bool = true;
                }
            }
        }
        return bool;
    }
}




