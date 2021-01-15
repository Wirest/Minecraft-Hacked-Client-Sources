package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.comparator.EntityDistanceComparator;
import com.ihl.client.comparator.TileEntityDistanceComparator;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventRender;
import com.ihl.client.module.option.*;
import com.ihl.client.util.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import javax.vecmath.Point2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventHandler(events = {EventRender.class})
public class StorageESP extends Module {

    public StorageESP(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("width", new Option("Width", "Width of the ESP outlines", new ValueDouble(1, new double[]{0, 10}, 0.1), Option.Type.NUMBER));
        options.put("chests", new Option("Chests", "Render chests and trapped chests", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 128, 0, 255)));
        options.put("enderchests", new Option("Ender Chests", "Render ender chests", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 0, 255, 255)));
        options.put("mobspawners", new Option("Mob Spawners", "Render mob spawners", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 0, 0, 255)));
        options.put("other", new Option("Other", "Render all other storage blocks", new ValueBoolean(true), Option.Type.BOOLEAN, colors(255, 255, 255, 255)));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    private List<Option> colors(int r, int g, int b, int a) {
        List<Option> list = new ArrayList();
        list.add(new Option("Color", "Can be HEX (00FF00) or 'rainbow'", new ValueString(ColorUtil.hexFromInt(new Color(r, g, b, a))), Option.Type.STRING));
        return list;
    }

    protected void onEvent(Event event) {
        float w = (float) Option.get(options, "width").DOUBLE();

        if (event instanceof EventRender) {
            EventRender e = (EventRender) event;
            if (e.type == Event.Type.PRE) {
                for (Object object : Helper.world().loadedTileEntityList) {
                    TileEntity tileEntity = (TileEntity) object;
                    String type = EntityUtil.getTileEntityTypeName(tileEntity);

                    if (type != null) {
                        if (Option.get(options, type).BOOLEAN()) {
                            String hex = Option.get(options, type, "color").STRING();
                            int hexColor = ColorUtil.intFromHex(hex);

                            Color color = new Color(hexColor);

                            GlStateManager.disableDepth();
                            GlStateManager.depthMask(false);
                            RenderUtil3D.box(tileEntity, color.getRGB(), w);
                            GlStateManager.depthMask(true);
                            GlStateManager.enableDepth();
                        }
                    }
                }
            }
        }
    }
}
