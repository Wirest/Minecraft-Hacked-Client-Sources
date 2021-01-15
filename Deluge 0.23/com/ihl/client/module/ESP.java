package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.comparator.EntityDistanceComparator;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventRender;
import com.ihl.client.module.option.*;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.ConvexHull;
import com.ihl.client.util.EntityUtil;
import com.ihl.client.util.RenderUtil2D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import javax.vecmath.Point2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventHandler(events = {EventRender.class})
public class ESP extends Module {

    public ESP(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("mode", new Option("Mode", "Entity render mode", new ValueChoice(2, new String[]{"normal", "wireframe", "outline"}), Option.Type.CHOICE));
        options.put("width", new Option("Width", "Width of the ESP outlines", new ValueDouble(2, new double[]{0, 10}, 0.1), Option.Type.NUMBER));
        options.put("players", new Option("Players", "Render players", new ValueBoolean(true), Option.Type.BOOLEAN, colors(0, 255, 255, 255)));
        options.put("animals", new Option("Animals", "Render animals", new ValueBoolean(true), Option.Type.BOOLEAN, colors(64, 255, 64, 255)));
        options.put("monsters", new Option("Monsters", "Render monsters", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 64, 64, 255)));
        options.put("neutrals", new Option("Neutrals", "Render neutral mobs", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 64, 255, 255)));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    private List<Option> colors(int r, int g, int b, int a) {
        List<Option> list = new ArrayList();
        list.add(new Option("Color", "Can be HEX (00FF00) or 'rainbow'", new ValueString(ColorUtil.hexFromInt(new Color(r, g, b, a))), Option.Type.STRING));
        return list;
    }

    protected void onEvent(Event event) {
        float w = (float) Option.get(options, "width").DOUBLE();
        String mode = Option.get(options, "mode").CHOICE();

        if (event instanceof EventRender) {
            EventRender e = (EventRender) event;
            if (e.type == Event.Type.POST) {
                GlStateManager.pushMatrix();
                double scale = Helper.scaled().getScaleFactor() / Math.pow(Helper.scaled().getScaleFactor(), 2);
                GlStateManager.scale(scale, scale, scale);

                List<Entity> list = new ArrayList();
                for (Entity entity : EntityUtil.entityData.keySet()) {
                    if (entity != Helper.player()) {
                        list.add(entity);
                    }
                }

                Collections.sort(list, new EntityDistanceComparator(Helper.player()));

                Friends friends = (Friends) Module.get("friends");

                for (Entity entity : list) {
                    String type = EntityUtil.getEntityTypeName(entity);

                    if (type != null) {
                        if (Option.get(options, type).BOOLEAN()) {
                            if (mode.equalsIgnoreCase("normal")) {
                                List<Vec3> data = EntityUtil.entityData.get(entity);

                                double minZ = Double.NaN;
                                double maxZ = Double.NaN;
                                for (int i = 5; i < data.size(); i++) {
                                    Vec3 point = data.get(i);
                                    minZ = Math.min(Double.isNaN(minZ) ? point.zCoord : minZ, point.zCoord);
                                    maxZ = Math.max(Double.isNaN(maxZ) ? point.zCoord : maxZ, point.zCoord);
                                }

                                if (minZ >= 0 && minZ < 1 && maxZ >= 0 && maxZ < 1) {
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(0, 0, -list.indexOf(entity));

                                    List<Point2d> points = new ArrayList();
                                    for (int i = 5; i < data.size(); i++) {
                                        Vec3 point = data.get(i);
                                        points.add(new Point2d(point.xCoord, point.yCoord));
                                    }

                                    List<Point2d> outline = ConvexHull.execute((ArrayList) points);

                                    String hex = Option.get(options, type, "color").STRING();
                                    int hexColor = ColorUtil.intFromHex(hex);

                                    Color color = new Color(hexColor);

                                    if (friends.active && Friends.isFriend(entity.getName())) {
                                        String friendHex = Option.get(friends.options, "color").STRING();
                                        int friendHexColor = ColorUtil.intFromHex(friendHex);
                                        color = new Color(friendHexColor);
                                    }

                                    RenderUtil2D.polygonBordered(outline, ColorUtil.transparency(color.getRGB(), 0.2), color.getRGB(), w, true);

                                    GlStateManager.popMatrix();
                                }
                            }
                        }
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }
}
