package com.minimap.events;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventRender2D;
import com.mentalfrostbyte.jello.main.Jello;
import com.minimap.*;
import com.minimap.settings.*;

import java.io.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;
import com.minimap.interfaces.*;
import org.lwjgl.opengl.*;
import com.minimap.animation.*;
import net.minecraft.entity.player.*;
import com.minimap.minimap.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.gui.*;

public class Events
{
    public Object lastGuiOpen;
    int deathCounter;
    private static boolean askedToUpdate;
    
    public Events() {
        this.deathCounter = 0;
        EventManager.register(this);
    }
    
    
    /*public void drawPost(final GuiOpenEvent event) {
        final Minecraft mc = XaeroMinimap.mc;
        if (event.gui instanceof GuiOptions) {
            if (!ModSettings.settingsButton) {
                return;
            }
            event.gui = (GuiScreen)new MyOptions(null, mc.gameSettings);
            try {
                XaeroMinimap.getSettings().saveSettings();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.lastGuiOpen = event.gui;
    }*/
    
    //@SubscribeEvent
    @EventTarget
    public void drawPre(EventRender2D event) {
    		if(!Jello.getModule("MiniMap").isToggled())
    			return;
    	
        if (Keyboard.isKeyDown(1)) {
            InterfaceHandler.cancel();
        }
        
        //if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            InterfaceHandler.drawInterfaces(Minecraft.getMinecraft().timer.renderPartialTicks);
            Animation.tick();
       // }
    }
    
    /*@SubscribeEvent
    public void guiOpen(final GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu || event.gui instanceof GuiMultiplayer) {
            XaeroMinimap.getSettings().resetServerSettings();
        }
        if (event.gui instanceof GuiGameOver) {
            ++this.deathCounter;
            if ((this.deathCounter & 0x1) == 0x0) {
                this.createDeathpoint((EntityPlayer)XaeroMinimap.mc.player);
            }
        }
    }*/
    
    /*public void createDeathpoint(final EntityPlayer p) {
        boolean disabled = false;
        if (Minimap.waypoints == null) {
            return;
        }
        for (final Waypoint w : Minimap.waypoints.list) {
            if (w.type == 1) {
                if (!XaeroMinimap.getSettings().getDeathpoints() || !XaeroMinimap.getSettings().getOldDeathpoints()) {
                    Minimap.waypoints.list.remove(w);
                    break;
                }
                disabled = w.disabled;
                w.type = 0;
                w.name = "gui.xaero_deathpoint_old";
                break;
            }
        }
        if (XaeroMinimap.getSettings().getDeathpoints()) {
            final Waypoint deathpoint = new Waypoint(Minimap.myFloor(p.posX), Minimap.myFloor(p.posY), Minimap.myFloor(p.posZ), "gui.xaero_deathpoint", "D", 0, 1);
            deathpoint.disabled = disabled;
            Minimap.waypoints.list.add(deathpoint);
        }
        try {
            XaeroMinimap.settings.saveWaypoints();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    /*@SubscribeEvent
    public void chatMessage(final ClientChatReceivedEvent e) {
        final String text = e.message.getFormattedText();
        if (text.contains("§c §r§5 §r§1 §r§f")) {
            String code = text.substring(text.indexOf("f") + 1);
            code = code.replaceAll("§", "").replaceAll("r", "").replaceAll(" ", "");
            XaeroMinimap.getSettings().resetServerSettings();
            XaeroMinimap.getSettings();
            ModSettings.serverSettings &= Integer.parseInt(code);
            System.out.println("Code: " + code);
        }
    }*/
    
    /*@SubscribeEvent
    public void renderLast(final RenderWorldLastEvent event) {
        if (XaeroMinimap.settings.getShowIngameWaypoints() && Minimap.waypoints != null) {
            final Entity entity = XaeroMinimap.mc.getRenderViewEntity();
            final double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks;
            final double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks;
            final double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getBuffer();
            for (final Waypoint w : Minimap.waypoints.list) {
                if (!w.disabled) {
                    if (w.type == 1 && !XaeroMinimap.getSettings().getDeathpoints()) {
                        continue;
                    }
                    this.renderIngameWaypoint(w, 12.0, d3, d4, d5, entity, worldrenderer, tessellator);
                }
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }
    }*/
    
    /*protected void renderIngameWaypoint(final Waypoint w, final double radius, final double d3, final double d4, final double d5, final Entity entity, final WorldRenderer worldrenderer, final Tessellator tessellator) {
        float offX = w.x - (float)d3 + 0.5f;
        float offY = w.y - (float)d4 + 1.0f;
        float offZ = w.z - (float)d5 + 0.5f;
        final double distance = Math.sqrt(offX * offX + offY * offY + offZ * offZ);
        w.lastDistance = distance;
        if ((XaeroMinimap.getSettings().waypointsDistance != 0.0f && distance > XaeroMinimap.getSettings().waypointsDistance) || (XaeroMinimap.getSettings().waypointsDistanceMin != 0.0f && distance < XaeroMinimap.getSettings().waypointsDistanceMin)) {
            return;
        }
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final FontRenderer fontrenderer = renderManager.getFontRenderer();
        if (fontrenderer == null) {
            return;
        }
        final float f = 1.6f;
        final float f2 = 0.016666668f * f;
        GlStateManager.pushMatrix();
        float textSize = 1.0f;
        String name = w.getName();
        String distanceText = "";
        boolean showDistance = false;
        float zoomer2 = 1.0f;
        if (XaeroMinimap.getSettings().keepWaypointNames) {
            textSize = 1.6f;
        }
        if (distance > radius) {
            final double maxDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16.0;
            if (distance > maxDistance) {
                zoomer2 = (float)(maxDistance / radius);
                final float zoomer3 = (float)(maxDistance / distance);
                offX *= zoomer3;
                offY *= zoomer3;
                offY += entity.getEyeHeight() * (1.0f - zoomer3);
                offZ *= zoomer3;
            }
            else {
                zoomer2 = (float)(distance / radius);
            }
            if (distance > 20.0) {
                textSize = 1.6f;
                if (XaeroMinimap.getSettings().distance == 1) {
                    final float Z = (float)((offZ == 0.0f) ? 0.001 : offZ);
                    float angle = (float)Math.toDegrees(Math.atan(-offX / Z));
                    if (offZ < 0.0f) {
                        if (offX < 0.0f) {
                            angle += 180.0f;
                        }
                        else {
                            angle -= 180.0f;
                        }
                    }
                    final float cameraAngle = MathHelper.wrapDegrees(entity.rotationYaw);
                    final float offset = MathHelper.wrapDegrees(angle - cameraAngle);
                    showDistance = (offset > -20.0f && offset < 20.0f);
                }
                else if (XaeroMinimap.getSettings().distance == 2) {
                    showDistance = true;
                }
                if (showDistance) {
                    distanceText = GuiMisc.simpleFormat.format(distance) + "m";
                    if (!XaeroMinimap.getSettings().keepWaypointNames) {
                        name = "";
                    }
                }
                else {
                    name = "";
                }
            }
        }
        GlStateManager.translate(offX, offY, offZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-f2, -f2, f2);
        GlStateManager.scale(zoomer2, zoomer2, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        w.drawIconInWorld(worldrenderer, tessellator, fontrenderer, name, distanceText, textSize, showDistance);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }*/
  /*  
    @SubscribeEvent
    public void renderTick(final GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof GuiUpdate) {
            Events.askedToUpdate = true;
        }
        else if (!Events.askedToUpdate && XaeroMinimap.isOutdated && event.gui instanceof GuiMainMenu) {
            XaeroMinimap.mc.displayGuiScreen((GuiScreen)new GuiUpdate());
            System.out.println("Minimap is outdated!");
        }
        else if (XaeroMinimap.isOutdated) {
            XaeroMinimap.isOutdated = false;
        }
    }*/
    
   /* @SubscribeEvent
    public void modelBake(final TextureStitchEvent.Post event) {
        Minimap.clearBlockColours = true;
    }*/
    
    static {
        Events.askedToUpdate = false;
    }
}
