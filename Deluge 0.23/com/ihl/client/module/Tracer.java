package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventRender;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.module.option.ValueString;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.EntityUtil;
import com.ihl.client.util.RenderUtil3D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@EventHandler(events = {EventRender.class})
public class Tracer extends Module {

    public Tracer(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("width", new Option("Width", "Width of tracer lines", new ValueDouble(1, new double[]{0, 10}, 0.1), Option.Type.NUMBER));
        options.put("players", new Option("Players", "Trace players", new ValueBoolean(true), Option.Type.BOOLEAN, colors(0, 255, 255, 255, false)));
        options.put("animals", new Option("Animals", "Trace animals", new ValueBoolean(false), Option.Type.BOOLEAN, colors(64, 255, 64, 255, false)));
        options.put("monsters", new Option("Monsters", "Trace monsters", new ValueBoolean(false), Option.Type.BOOLEAN, colors(255, 64, 64, 255, false)));
        options.put("neutrals", new Option("Neutrals", "Trace neutral mobs", new ValueBoolean(false), Option.Type.BOOLEAN, colors(255, 64, 255, 255, false)));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    private List<Option> colors(int r, int g, int b, int a, boolean rainbow) {
        List<Option> list = new ArrayList();
        list.add(new Option("Color", "Can be HEX (00FF00) or 'rainbow'", new ValueString(ColorUtil.hexFromInt(new Color(r, g, b, a))), Option.Type.STRING));
        return list;
    }

    protected void onEvent(Event event) {
        float width = (float) Option.get(options, "width").DOUBLE();

        if (event instanceof EventRender) {
            EventRender e = (EventRender) event;
            if (e.type == Event.Type.PRE) {
                GlStateManager.pushAttrib();
                GlStateManager.loadIdentity();
                boolean temp = Helper.mc().gameSettings.viewBobbing;
                Helper.mc().gameSettings.viewBobbing = false;
                Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 2);
                Helper.mc().gameSettings.viewBobbing = temp;

                Friends friends = (Friends) Module.get("friends");

                GlStateManager.disableDepth();
                for (Entity entity : EntityUtil.entityData.keySet()) {
                    if (entity instanceof EntityLivingBase && entity != Helper.player()) {
                        String type = EntityUtil.getEntityTypeName(entity);

                        if (type != null) {
                            if (Option.get(options, type).BOOLEAN()) {
                                Vec3 entityPos = EntityUtil.getEntityRenderPosition(entity);

                                String hex = Option.get(options, type, "color").STRING();
                                int hexColor = ColorUtil.intFromHex(hex);

                                Color color = new Color(hexColor);

                                if (friends.active && Friends.isFriend(entity.getName())) {
                                    String friendHex = Option.get(friends.options, "color").STRING();
                                    int friendHexColor = ColorUtil.intFromHex(friendHex);
                                    color = new Color(friendHexColor);
                                }

                                RenderUtil3D.line(0, Helper.player().getEyeHeight(), 0, entityPos.xCoord, entityPos.yCoord + (entity.height / 2), entityPos.zCoord, color.getRGB(), width);
                            }
                        }
                    }
                }
                GlStateManager.popAttrib();
            }
        }
    }

}
