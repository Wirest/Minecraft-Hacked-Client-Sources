// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.lwjgl.opengl.GL30;
import net.minecraft.client.renderer.RenderHelper;
import optifine.Reflector;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.opengl.GL20;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.Minecraft;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.Frustum;

public class ShadersRender
{
    public static void setFrustrumPosition(final Frustum frustrum, final double x, final double y, final double z) {
        frustrum.setPosition(x, y, z);
    }
    
    public static void setupTerrain(final RenderGlobal renderGlobal, final Entity viewEntity, final double partialTicks, final ICamera camera, final int frameCount, final boolean playerSpectator) {
        renderGlobal.setupTerrain(viewEntity, partialTicks, camera, frameCount, playerSpectator);
    }
    
    public static void beginTerrainSolid() {
        if (Shaders.isRenderingWorld) {
            Shaders.fogEnabled = true;
            Shaders.useProgram(7);
        }
    }
    
    public static void beginTerrainCutoutMipped() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(7);
        }
    }
    
    public static void beginTerrainCutout() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(7);
        }
    }
    
    public static void endTerrain() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(3);
        }
    }
    
    public static void beginTranslucent() {
        if (Shaders.isRenderingWorld) {
            if (Shaders.usedDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33995);
                Shaders.checkGLError("pre copy depth");
                GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
                Shaders.checkGLError("copy depth");
                GlStateManager.setActiveTexture(33984);
            }
            Shaders.useProgram(12);
        }
    }
    
    public static void endTranslucent() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(3);
        }
    }
    
    public static void renderHand0(final EntityRenderer er, final float par1, final int par2) {
        if (!Shaders.isShadowPass) {
            final boolean flag = Shaders.isItemToRenderMainTranslucent();
            if (!flag) {
                Shaders.readCenterDepth();
                Shaders.beginHand();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                er.renderHand(par1, par2, true, false, false);
                Shaders.endHand();
                Shaders.setHandRenderedMain(true);
            }
        }
    }
    
    public static void renderHand1(final EntityRenderer er, final float par1, final int par2) {
        if (!Shaders.isShadowPass && !Shaders.isHandRenderedMain()) {
            Shaders.readCenterDepth();
            GlStateManager.enableBlend();
            Shaders.beginHand();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            er.renderHand(par1, par2, true, false, true);
            Shaders.endHand();
            Shaders.setHandRenderedMain(true);
        }
    }
    
    public static void renderItemFP(final ItemRenderer itemRenderer, final float par1, final boolean renderTranslucent) {
        GlStateManager.depthMask(true);
        if (renderTranslucent) {
            GlStateManager.depthFunc(519);
            GL11.glPushMatrix();
            final IntBuffer intbuffer = Shaders.activeDrawBuffers;
            Shaders.setDrawBuffers(Shaders.drawBuffersNone);
            Shaders.renderItemKeepDepthMask = true;
            itemRenderer.renderItemInFirstPerson(par1);
            Shaders.renderItemKeepDepthMask = false;
            Shaders.setDrawBuffers(intbuffer);
            GL11.glPopMatrix();
        }
        GlStateManager.depthFunc(515);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        itemRenderer.renderItemInFirstPerson(par1);
    }
    
    public static void renderFPOverlay(final EntityRenderer er, final float par1, final int par2) {
        if (!Shaders.isShadowPass) {
            Shaders.beginFPOverlay();
            er.renderHand(par1, par2, false, true, false);
            Shaders.endFPOverlay();
        }
    }
    
    public static void beginBlockDamage() {
        if (Shaders.isRenderingWorld) {
            Shaders.useProgram(11);
            if (Shaders.programsID[11] == Shaders.programsID[7]) {
                Shaders.setDrawBuffers(Shaders.drawBuffersColorAtt0);
                GlStateManager.depthMask(false);
            }
        }
    }
    
    public static void endBlockDamage() {
        if (Shaders.isRenderingWorld) {
            GlStateManager.depthMask(true);
            Shaders.useProgram(3);
        }
    }
    
    public static void renderShadowMap(final EntityRenderer entityRenderer, final int pass, final float partialTicks, final long finishTimeNano) {
        if (Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
            final Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.mcProfiler.endStartSection("shadow pass");
            final RenderGlobal renderglobal = minecraft.renderGlobal;
            Shaders.isShadowPass = true;
            Shaders.shadowPassCounter = Shaders.shadowPassInterval;
            Shaders.preShadowPassThirdPersonView = minecraft.gameSettings.thirdPersonView;
            minecraft.gameSettings.thirdPersonView = 1;
            Shaders.checkGLError("pre shadow");
            GL11.glMatrixMode(5889);
            GL11.glPushMatrix();
            GL11.glMatrixMode(5888);
            GL11.glPushMatrix();
            minecraft.mcProfiler.endStartSection("shadow clear");
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.sfb);
            Shaders.checkGLError("shadow bind sfb");
            Shaders.useProgram(30);
            minecraft.mcProfiler.endStartSection("shadow camera");
            entityRenderer.setupCameraTransform(partialTicks, 2);
            Shaders.setCameraShadow(partialTicks);
            ActiveRenderInfo.updateRenderInfo(Minecraft.thePlayer, minecraft.gameSettings.thirdPersonView == 2);
            Shaders.checkGLError("shadow camera");
            GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers");
            GL11.glReadBuffer(0);
            Shaders.checkGLError("shadow readbuffer");
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.sfbDepthTextures.get(0), 0);
            if (Shaders.usedShadowColorBuffers != 0) {
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, Shaders.sfbColorTextures.get(0), 0);
            }
            Shaders.checkFramebufferStatus("shadow fb");
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glClear((Shaders.usedShadowColorBuffers != 0) ? 16640 : 256);
            Shaders.checkGLError("shadow clear");
            minecraft.mcProfiler.endStartSection("shadow frustum");
            final ClippingHelper clippinghelper = ClippingHelperShadow.getInstance();
            minecraft.mcProfiler.endStartSection("shadow culling");
            final Frustum frustum = new Frustum(clippinghelper);
            final Entity entity = minecraft.getRenderViewEntity();
            final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            frustum.setPosition(d0, d2, d3);
            GlStateManager.shadeModel(7425);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.disableCull();
            minecraft.mcProfiler.endStartSection("shadow prepareterrain");
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            minecraft.mcProfiler.endStartSection("shadow setupterrain");
            int i = 0;
            i = entityRenderer.frameCount++;
            renderglobal.setupTerrain(entity, partialTicks, frustum, i, Minecraft.thePlayer.isSpectator());
            minecraft.mcProfiler.endStartSection("shadow updatechunks");
            minecraft.mcProfiler.endStartSection("shadow terrain");
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, 2, entity);
            Shaders.checkGLError("shadow terrain solid");
            GlStateManager.enableAlpha();
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, 2, entity);
            Shaders.checkGLError("shadow terrain cutoutmipped");
            minecraft.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, 2, entity);
            Shaders.checkGLError("shadow terrain cutout");
            minecraft.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            GlStateManager.shadeModel(7424);
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            minecraft.mcProfiler.endStartSection("shadow entities");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
            }
            renderglobal.renderEntities(entity, frustum, partialTicks);
            Shaders.checkGLError("shadow entities");
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1f);
            if (Shaders.usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33989);
                Shaders.checkGLError("pre copy shadow depth");
                GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
                Shaders.checkGLError("copy shadow depth");
                GlStateManager.setActiveTexture(33984);
            }
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.shadeModel(7425);
            Shaders.checkGLError("shadow pre-translucent");
            GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
            Shaders.checkGLError("shadow drawbuffers pre-translucent");
            Shaders.checkFramebufferStatus("shadow pre-translucent");
            if (Shaders.isRenderShadowTranslucent()) {
                minecraft.mcProfiler.endStartSection("shadow translucent");
                renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, 2, entity);
                Shaders.checkGLError("shadow translucent");
            }
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                RenderHelper.enableStandardItemLighting();
                Reflector.call(Reflector.ForgeHooksClient_setRenderPass, 1);
                renderglobal.renderEntities(entity, frustum, partialTicks);
                Reflector.call(Reflector.ForgeHooksClient_setRenderPass, -1);
                RenderHelper.disableStandardItemLighting();
                Shaders.checkGLError("shadow entities 1");
            }
            GlStateManager.shadeModel(7424);
            GlStateManager.depthMask(true);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GL11.glFlush();
            Shaders.checkGLError("shadow flush");
            Shaders.isShadowPass = false;
            minecraft.gameSettings.thirdPersonView = Shaders.preShadowPassThirdPersonView;
            minecraft.mcProfiler.endStartSection("shadow postprocess");
            if (Shaders.hasGlGenMipmap) {
                if (Shaders.usedShadowDepthBuffers >= 1) {
                    if (Shaders.shadowMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture(33988);
                        GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(0));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[0] ? 9984 : 9987);
                    }
                    if (Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture(33989);
                        GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(1));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[1] ? 9984 : 9987);
                    }
                    GlStateManager.setActiveTexture(33984);
                }
                if (Shaders.usedShadowColorBuffers >= 1) {
                    if (Shaders.shadowColorMipmapEnabled[0]) {
                        GlStateManager.setActiveTexture(33997);
                        GlStateManager.bindTexture(Shaders.sfbColorTextures.get(0));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[0] ? 9984 : 9987);
                    }
                    if (Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                        GlStateManager.setActiveTexture(33998);
                        GlStateManager.bindTexture(Shaders.sfbColorTextures.get(1));
                        GL30.glGenerateMipmap(3553);
                        GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[1] ? 9984 : 9987);
                    }
                    GlStateManager.setActiveTexture(33984);
                }
            }
            Shaders.checkGLError("shadow postprocess");
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
            GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.activeDrawBuffers = null;
            minecraft.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            Shaders.useProgram(7);
            GL11.glMatrixMode(5888);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            Shaders.checkGLError("shadow end");
        }
    }
    
    public static void preRenderChunkLayer(final EnumWorldBlockLayer blockLayerIn) {
        if (Shaders.isRenderBackFace(blockLayerIn)) {
            GlStateManager.disableCull();
        }
        if (OpenGlHelper.useVbo()) {
            GL11.glEnableClientState(32885);
            GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
        }
    }
    
    public static void postRenderChunkLayer(final EnumWorldBlockLayer blockLayerIn) {
        if (OpenGlHelper.useVbo()) {
            GL11.glDisableClientState(32885);
            GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
        }
        if (Shaders.isRenderBackFace(blockLayerIn)) {
            GlStateManager.enableCull();
        }
    }
    
    public static void setupArrayPointersVbo() {
        final int i = 14;
        GL11.glVertexPointer(3, 5126, 56, 0L);
        GL11.glColorPointer(4, 5121, 56, 12L);
        GL11.glTexCoordPointer(2, 5126, 56, 16L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexCoordPointer(2, 5122, 56, 24L);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glNormalPointer(5120, 56, 28L);
        GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, 56, 32L);
        GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, 56, 40L);
        GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, 56, 48L);
    }
    
    public static void beaconBeamBegin() {
        Shaders.useProgram(14);
    }
    
    public static void beaconBeamStartQuad1() {
    }
    
    public static void beaconBeamStartQuad2() {
    }
    
    public static void beaconBeamDraw1() {
    }
    
    public static void beaconBeamDraw2() {
        GlStateManager.disableBlend();
    }
    
    public static void renderEnchantedGlintBegin() {
        Shaders.useProgram(17);
    }
    
    public static void renderEnchantedGlintEnd() {
        if (Shaders.isRenderingWorld) {
            if (Shaders.isRenderBothHands()) {
                Shaders.useProgram(19);
            }
            else {
                Shaders.useProgram(16);
            }
        }
        else {
            Shaders.useProgram(0);
        }
    }
}
