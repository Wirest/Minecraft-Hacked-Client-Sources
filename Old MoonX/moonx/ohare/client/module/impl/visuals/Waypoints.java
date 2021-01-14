package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.FontValue;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Waypoints extends Module {
    public BooleanValue font = new BooleanValue("Font", "Enable Custom Font", true);
    public FontValue fontValue = new FontValue("WaypointFont",new MCFontRenderer(new Font("Arial",Font.PLAIN, 16),true,true));
    public Waypoints() {
        super("Waypoints", Category.VISUALS, new Color(0x85DEE1).getRGB());
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        if (!getMc().isSingleplayer() && getMc().getCurrentServerData() != null) {
            Moonx.INSTANCE.getWaypointManager().getWaypoints().forEach(waypoint -> {
                if (getMc().getCurrentServerData().serverIP.equals(waypoint.getServer()) && getMc().thePlayer.dimension == waypoint.getDimension() || !isOnScreen(new Vec3(waypoint.getX(),waypoint.getY(),waypoint.getZ()))) {
                    double posX = waypoint.getX();
                    double posY = waypoint.getY();
                    double posZ = waypoint.getZ();
                    AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX - getMc().getRenderManager().getRenderPosX(), posY - getMc().getRenderManager().getRenderPosY(), posZ - getMc().getRenderManager().getRenderPosZ());
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + 1 / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - 1 / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - 1 / 2.0f));
                    getMc().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - getMc().getRenderManager().viewerPosX, vector.y - getMc().getRenderManager().viewerPosY, vector.z - getMc().getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    getMc().entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        float x = (float) position.x;
                        float x2 = (float) position.z;
                        float y = (float) position.y;
                        final String nametext = "(" + Math.round(getMc().thePlayer.getDistance(waypoint.getX(), waypoint.getY(), waypoint.getZ())) + "m) " + waypoint.getLabel();
                        RenderUtil.drawRect((x + (x2 - x) / 2) - (font.isEnabled() ? fontValue.getValue().getStringWidth(nametext) : getMc().fontRendererObj.getStringWidth(nametext) / 2) / 2 - 2.5, y - (font.isEnabled() ? fontValue.getValue().getHeight() + 6: getMc().fontRendererObj.FONT_HEIGHT), (font.isEnabled() ? fontValue.getValue().getStringWidth(nametext) : getMc().fontRendererObj.getStringWidth(nametext) / 2) + 5, (font.isEnabled() ? fontValue.getValue().getHeight() + 6: getMc().fontRendererObj.FONT_HEIGHT), new Color(0, 0, 0, 120).getRGB());
                        if (font.isEnabled())
                            fontValue.getValue().drawStringWithShadow(nametext, (x + ((x2 - x) / 2)) - (fontValue.getValue().getStringWidth(nametext) / 2), y - fontValue.getValue().getHeight() - 2, -1);
                        else {
                            GL11.glPushMatrix();
                            GL11.glScalef(0.5f, 0.5f, 0.5f);
                            getMc().fontRendererObj.drawStringWithShadow(nametext, ((x + ((x2 - x) / 2)) - (getMc().fontRendererObj.getStringWidth(nametext) / 4)) * 2, (y - getMc().fontRendererObj.FONT_HEIGHT / 2 - 2) * 2, -1);
                            GL11.glPopMatrix();
                            GL11.glScalef(1.0f, 1.0f, 1.0f);
                        }
                        GL11.glPopMatrix();
                    }
                }
            });
        }
    }
    private boolean isOnScreen(Vec3 pos) {
        if (pos.xCoord > -1 && pos.zCoord < 1)
            return pos.xCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) >= 0 && pos.xCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) <= Display.getWidth() && pos.yCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) >= 0 && pos.yCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) <= Display.getHeight();

        return false;
    }
}

