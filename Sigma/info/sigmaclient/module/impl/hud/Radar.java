package info.sigmaclient.module.impl.hud;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.management.waypoints.Waypoint;
import info.sigmaclient.management.waypoints.WaypointManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;

import java.util.Objects;

public class Radar extends Module {

    private static final String SCALE = "SCALE";
    private static final String X = "X";
    private static final String Y = "Y";
    private String SIZE = "SIZE";
    private Timer timer = new Timer();

    public Radar(ModuleData data) {
        super(data);
        settings.put(SCALE, new Setting<>(SCALE, 2.0, "Scales the radar.", 0.1, 0.25, 5));
        settings.put(X, new Setting<>(X, 1, "X position for radar.", 5, 1, 1920));
        settings.put(Y, new Setting<>(Y, 100, "Y position for radar.", 5, 1, 1080));
        settings.put(SIZE, new Setting<>(SIZE, 125, "Size of the radar.", 5, 50, 500));
    }

    private boolean dragging;
    float hue;

    @Override
    @RegisterEvent(events = {EventRenderGui.class, EventTick.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            int size = ((Number) settings.get(SIZE).getValue()).intValue();
            float xOffset = ((Number) settings.get(X).getValue()).floatValue();
            float yOffset = ((Number) settings.get(Y).getValue()).floatValue(); // Global Y
            float playerOffsetX = (float) (mc.thePlayer.posX);
            float playerOffSetZ = (float) (mc.thePlayer.posZ);
            Gui.drawFilledCircle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, Colors.getColor(50, 100), 0);
            /*
             * Horizontal line of the cross
             */
            GlStateManager.pushMatrix();
            GlStateManager.translate(xOffset + size / 2, yOffset + size / 2, 0);
            GlStateManager.rotate(-mc.thePlayer.rotationYaw, 0, 0, 1);
            RenderingUtil.rectangle((-0.5), -size / 2 + 4, (0.5), size / 2 - 4, Colors.getColor(255, 80));

            /*
             * Vertical line of the cross
             */

            RenderingUtil.rectangle(-size / 2 + 4, (-0.5), size / 2 - 4, (+0.5),
                    Colors.getColor(255, 80));
            GlStateManager.popMatrix();

            RenderingUtil.drawCircle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, 72, Colors.getColor(0, 200));

            TTFFontRenderer normal = Client.fm.getFont("SFB 8");
            float angle2 = -mc.thePlayer.rotationYaw + 90;
            float x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2))) + xOffset + size / 2; // angle is in radians
            float y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2))) + yOffset + size / 2;
            normal.drawStringWithShadow("N", x2 - normal.getWidth("N") / 2, y2 - 1, -1);
            x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 90))) + xOffset + size / 2; // angle is in radians
            y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 90))) + yOffset + size / 2;
            normal.drawStringWithShadow("E", x2 - normal.getWidth("E") / 2, y2 - 1, -1);
            x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 180))) + xOffset + size / 2; // angle is in radians
            y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 180))) + yOffset + size / 2;
            normal.drawStringWithShadow("S", x2 - normal.getWidth("S") / 2, y2 - 1, -1);
            x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 - 90))) + xOffset + size / 2; // angle is in radians
            y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 - 90))) + yOffset + size / 2;
            normal.drawStringWithShadow("W", x2 - normal.getWidth("W") / 2, y2 - 1, -1);

            int var141 = er.getResolution().getScaledWidth();
            int var151 = er.getResolution().getScaledHeight();
            final int mouseX = Mouse.getX() * var141 / mc.displayWidth;
            final int mouseY = var151 - Mouse.getY() * var151 / mc.displayHeight - 1;

            if (mouseX >= xOffset && mouseX <= xOffset + size && mouseY >= yOffset - 3 && mouseY <= yOffset + 10 && Mouse.getEventButton() == 0 && timer.delay(20)) {
                timer.reset();
                dragging = !dragging;
            }
            if (dragging && mc.currentScreen instanceof GuiChat) {
                Object newValue = (StringConversions.castNumber(Double.toString(mouseX - size / 2), 5));
                settings.get(X).setValue(newValue);
                Object newValueY = (StringConversions.castNumber(Double.toString(mouseY - 2), 5));
                settings.get(Y).setValue(newValueY);
            } else {
                dragging = false;
            }

            /*
             * The offsets are used here for the first rectangle. + 100 is the
             * current size to extend the Y and Z
             */

            /*RenderingUtil.rectangleBordered(xOffset, yOffset, xOffset + size, yOffset + size, 0.5, Colors.getColor(90), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 1, yOffset + 1, xOffset + size - 1, yOffset + size - 1, 1, Colors.getColor(90), Colors.getColor(61));

            RenderingUtil.rectangleBordered(xOffset + 2.5, yOffset + 2.5, xOffset + size - 2.5, yOffset + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 3, yOffset + 3, xOffset + size - 3, yOffset + size - 3, 0.5, Colors.getColor(27), Colors.getColor(61));
           */
            //RenderingUtil.rectangle(xOffset + 3, yOffset + 3, xOffset + size - 3, yOffset + 3.5, Colors.getColor(56, 195, 255));



            /*
             * For every entity (Player or valid entity)
             */

            for (Object o : mc.theWorld.getLoadedEntityList()) {
                if (o instanceof EntityPlayer) {
                    EntityPlayer ent = (EntityPlayer) o;
                    if (ent.isEntityAlive() && ent != mc.thePlayer && !(ent.isInvisible() || ent.isInvisibleToPlayer(mc.thePlayer))) {
                        /*
                         * (targetPlayer posX - localPlayer posX) * Distance
                         * Scale
                         */

                        float pTicks = mc.timer.renderPartialTicks;
                        float posX = (float) (((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks) -
                                playerOffsetX) * ((Number) settings.get(SCALE).getValue()).doubleValue());
                        /*
                         * (targetPlayer posZ - localPlayer posZ) * Distance
                         * Scale
                         */
                        float posZ = (float) (((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks) -
                                playerOffSetZ) * ((Number) settings.get(SCALE).getValue()).doubleValue());
                        int color;
                        // Gay Friend Check
                        if (FriendManager.isFriend(ent.getName())) {
                            color = mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(0, 195, 255)
                                    : Colors.getColor(0, 195, 255);
                        } else {
                            color = mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(255, 0, 0)
                                    : Colors.getColor(255, 255, 0);
                        }

                        /*
                         * Fuck Ms. Goble's geometry class.
                         * Rotate the circle based off of the player yaw with some gay trig.
                         */

                        float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                        float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                        float rotY = -(posZ * cos - posX * sin);
                        float rotX = -(posX * cos + posZ * sin);
                        float var7 = 0 - rotX;
                        float var9 = 0 - rotY;
                        if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) > size / 2 - 4) {
                            float angle = findAngle(0, rotX, 0, rotY);
                            float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2; // angle is in radians
                            float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(x, y, 0);
                            GlStateManager.rotate(angle, 0, 0, 1);
                            GlStateManager.scale(1.5f, 0.5, 0.5);

                            RenderingUtil.drawCircle(0, 0, 1.5f, 3, Colors.getColor(46));
                            RenderingUtil.drawCircle(0, 0, 1, 3, color);
                            GlStateManager.popMatrix();
                        } else {
                            RenderingUtil.rectangleBordered(xOffset + (size / 2) + rotX - 1.5,
                                    yOffset + (size / 2) + rotY - 1.5, xOffset + (size / 2) + rotX + 1.5,
                                    yOffset + (size / 2) + rotY + 1.5, 0.5, color, Colors.getColor(46));
                        }

                        /*
                         * Clamps to the edge of the radar, have it less than
                         * the radar if you don't want squares to come out.
                         */


                    }
                }
            }
            if (mc.getCurrentServerData() != null)
                for (Waypoint waypoint : WaypointManager.getManager().getWaypoints()) {
                    if (Objects.equals(waypoint.getAddress(), mc.getCurrentServerData().serverIP)) {
                        /*
                         * (targetPlayer posX - localPlayer posX) * Distance
                         * Scale
                         */

                        float posX = (float) (waypoint.getVec3().xCoord - playerOffsetX * ((Number) settings.get(SCALE).getValue()).doubleValue());
                        /*
                         * (targetPlayer posZ - localPlayer posZ) * Distance
                         * Scale
                         */
                        float posZ = (float) ((float) waypoint.getVec3().zCoord - playerOffSetZ * ((Number) settings.get(SCALE).getValue()).doubleValue());

                        /*
                         * Fuck Ms. Goble's geometry class.
                         * Rotate the circle based off of the player yaw with some gay trig.
                         */

                        float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                        float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                        float rotY = -(posZ * cos - posX * sin);
                        float rotX = -(posX * cos + posZ * sin);
                        float var7 = 0 - rotX;
                        float var9 = 0 - rotY;

                        if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) > size / 2 - 4) {
                            float angle = findAngle(0, rotX, 0, rotY);
                            float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2; // angle is in radians
                            float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(x, y, 0);
                            GlStateManager.rotate(angle, 0, 0, 1);
                            GlStateManager.scale(1.5f, 0.5, 0.5);

                            RenderingUtil.drawCircle(0, 0, 1.5f, 3, Colors.getColor(46));
                            RenderingUtil.drawCircle(0, 0, 1, 3, waypoint.getColor());
                            GlStateManager.popMatrix();
                        } else {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(xOffset + (size / 2) + rotX, yOffset + (size / 2) + rotY, 0);
                            GlStateManager.scale(0.5, 0.5, 1);
                            mc.fontRendererObj.drawStringWithShadow(waypoint.getName(), -mc.fontRendererObj.getStringWidth(waypoint.getName()) / 2 + 1, 5, -1);
                            GlStateManager.popMatrix();
                            RenderingUtil.rectangleBordered(xOffset + (size / 2) + rotX - 1.5,
                                    yOffset + (size / 2) + rotY - 1.5, xOffset + (size / 2) + rotX + 1.5,
                                    yOffset + (size / 2) + rotY + 1.5, 0.5, waypoint.getColor(), Colors.getColor(46));
                        }
                    }
                }
            /*
             * LocalPlayer square, doesn't need any pointers just rendered on
             * the radar.
             */


        }

    }

    private float findAngle(float x, float x2, float y, float y2) {
        return (float) (Math.atan2(y2 - y, x2 - x) * 180 / Math.PI);
    }

}
