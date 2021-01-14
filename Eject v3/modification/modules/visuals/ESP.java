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

import java.awt.*;
import java.util.Iterator;

public final class ESP
        extends Module {
    private final TestShaderUtil outlineShader = new TestShaderUtil("entity_outline");
    private final TestShaderUtil blurShader = new TestShaderUtil("blur");
    private final TestShaderUtil icarusShader = new TestShaderUtil("icarus_outline");
    private final Value<Boolean> players = new Value("Players", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> mobs = new Value("Mobs", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> chests = new Value("Chests", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> items = new Value("Items", Boolean.valueOf(true), this, new String[0]);
    private final Value<Float> outlineRadius = new Value("Outline radius", Float.valueOf(2.0F), 1.0F, 5.0F, 0, this, new String[0]);
    private final Value<Float> blurRadius = new Value("Blur radius", Float.valueOf(2.0F), 0.0F, 10.0F, 1, this, new String[]{"Mode", "Blur"});
    private final Value<Float> blurAlpha = new Value("Blur alpha", Float.valueOf(0.2F), 0.0F, 1.0F, 2, this, new String[]{"Mode", "Blur"});
    private final Value<Float> red = new Value("Color Red", Float.valueOf(28.0F), 0.0F, 255.0F, 0, this, new String[0]);
    private final Value<Float> green = new Value("Color Green", Float.valueOf(195.0F), 0.0F, 255.0F, 0, this, new String[0]);
    private final Value<Float> blue = new Value("Color Blue", Float.valueOf(195.0F), 0.0F, 255.0F, 0, this, new String[0]);
    private final Value<Float> outlineAlpha = new Value("Outline alpha", Float.valueOf(5.0F), 1.0F, 100.0F, 0, this, new String[]{"Mode", "Icarus"});
    private final Value<Float> fade = new Value("Fade", Float.valueOf(10.0F), 1.0F, 50.0F, 0, this, new String[]{"Mode", "Blur"});
    private final Value<String> mode = new Value("Mode", "Blur", new String[]{"Blur", "Icarus"}, this, new String[0]);
    private Framebuffer framebuffer;
    private Framebuffer framebufferShader;
    private Framebuffer framebufferBlur;
    private Framebuffer fbTest;
    private boolean render;

    public ESP(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventRender3D)) {
            switch ((String) this.mode.value) {
                case "Blur":
                case "Icarus":
                    this.framebuffer = Modification.RENDER_UTIL.createFrameBuffer(this.framebuffer);
                    this.framebufferShader = Modification.RENDER_UTIL.createFrameBuffer(this.framebufferShader);
                    this.framebufferBlur = Modification.RENDER_UTIL.createFrameBuffer(this.framebufferBlur);
                    this.fbTest = Modification.RENDER_UTIL.createFrameBuffer(this.fbTest);
                    this.framebuffer.framebufferClear();
                    this.framebuffer.bindFramebuffer(true);
                    this.render = renderedEntity(((EventRender3D) paramEvent).partialTicks);
                    this.framebuffer.unbindFramebuffer();
                    MC.getFramebuffer().bindFramebuffer(true);
                    GlStateManager.disableLighting();
            }
        }
        if (((paramEvent instanceof EventRender2D)) && (this.framebuffer != null) && (this.framebufferShader != null) && (this.render) && (this.framebufferBlur != null) && (this.fbTest != null)) {
            int i = new Color(((Float) this.red.value).floatValue() / 255.0F, ((Float) this.green.value).floatValue() / 255.0F, ((Float) this.blue.value).floatValue() / 255.0F, 1.0F).getRGB();
            switch ((String) this.mode.value) {
                case "Blur":
                    this.framebufferShader.framebufferClear();
                    this.framebufferShader.bindFramebuffer(true);
                    renderOutlines(paramEvent, 0.0F, 1.0F, i, this.outlineShader);
                    renderOutlines(paramEvent, 1.0F, 0.0F, i, this.outlineShader);
                    this.framebufferShader.unbindFramebuffer();
                    this.framebufferBlur.framebufferClear();
                    this.framebufferBlur.bindFramebuffer(true);
                    renderBlur(paramEvent, 0.0F, 1.0F, i, this.framebufferShader);
                    renderBlur(paramEvent, 1.0F, 0.0F, i, this.framebufferShader);
                    this.framebufferBlur.unbindFramebuffer();
                    MC.getFramebuffer().bindFramebuffer(true);
                    renderBlur(paramEvent, 0.0F, 1.0F, i, this.framebufferBlur);
                    renderBlur(paramEvent, 1.0F, 0.0F, i, this.framebufferBlur);
                    break;
                case "Icarus":
                    MC.getFramebuffer().bindFramebuffer(true);
                    renderOutlines(paramEvent, 0.0F, 1.0F, i, this.icarusShader);
                    break;
            }
        }
    }

    private void renderBlur(Event paramEvent, float paramFloat1, float paramFloat2, int paramInt, Framebuffer paramFramebuffer) {
        ARBShaderObjects.glUseProgramObjectARB(this.blurShader.program);
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_texture"), 0);
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_texelSize"), 1.0F / MC.displayWidth, 1.0F / MC.displayHeight);
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_direction"), paramFloat1, paramFloat2);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_radius"), ((Float) this.blurRadius.value).floatValue());
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_alpha"), ((Float) this.blurAlpha.value).floatValue());
        ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(this.blurShader.program, "u_color"), Modification.RENDER_UTIL.rgba(paramInt)[0], Modification.RENDER_UTIL.rgba(paramInt)[1], Modification.RENDER_UTIL.rgba(paramInt)[2]);
        GL11.glBindTexture(3553, paramFramebuffer.framebufferTexture);
        this.blurShader.renderShader(((EventRender2D) paramEvent).resolution);
    }

    private void renderOutlines(Event paramEvent, float paramFloat1, float paramFloat2, int paramInt, TestShaderUtil paramTestShaderUtil) {
        ARBShaderObjects.glUseProgramObjectARB(paramTestShaderUtil.program);
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_texture"), 0);
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_texelSize"), 1.0F / MC.displayWidth, 1.0F / MC.displayHeight);
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_direction"), paramFloat1, paramFloat2);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_alpha"), ((Float) this.outlineAlpha.value).floatValue());
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_fade"), ((String) this.mode.value).equals("Blur") ? ((Float) this.fade.value).floatValue() : 1.0F);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "u_radius"), ((Float) this.outlineRadius.value).floatValue());
        ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(paramTestShaderUtil.program, "color"), Modification.RENDER_UTIL.rgba(paramInt)[0], Modification.RENDER_UTIL.rgba(paramInt)[1], Modification.RENDER_UTIL.rgba(paramInt)[2]);
        GL11.glBindTexture(3553, this.framebuffer.framebufferTexture);
        paramTestShaderUtil.renderShader(((EventRender2D) paramEvent).resolution);
    }

    protected void onDeactivated() {
    }

    private boolean renderedEntity(float paramFloat) {
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
                    GlStateManager.enableLighting();
                    RenderUtil.renderOutlinesCustom = false;
                    bool = true;
                }
            }
        }
        return bool;
    }
}




