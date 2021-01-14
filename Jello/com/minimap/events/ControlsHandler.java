package com.minimap.events;

import net.minecraft.client.*;
import net.minecraft.client.settings.*;
import com.minimap.*;
import net.minecraft.client.gui.*;
import com.minimap.minimap.*;
import com.minimap.settings.*;
import java.io.*;

public class ControlsHandler
{
    public static KeyBinding[] toAdd;
    
    public ControlsHandler() {
        final GameSettings settings = Minecraft.getMinecraft().gameSettings;
        final KeyBinding[] binds = settings.keyBindings;
        final int size = binds.length + ControlsHandler.toAdd.length;
        settings.keyBindings = new KeyBinding[size];
        for (int i = 0; i < binds.length; ++i) {
            settings.keyBindings[i] = binds[i];
        }
        for (int i = binds.length; i < size; ++i) {
            settings.keyBindings[i] = ControlsHandler.toAdd[i - binds.length];
        }
    }
    
    public static void setKeyState(final KeyBinding kb, final boolean pressed) {
        KeyBinding.setKeyBindState(kb.getKeyCode(), pressed);
    }
    
    public static boolean isDown(final KeyBinding kb) {
        return GameSettings.isKeyDown(kb);
    }
    
    public void keyDown(final KeyBinding kb, final boolean tickEnd, final boolean isRepeat) {
        final Minecraft mc = XaeroMinimap.mc;
        if (!tickEnd) {
            /*if (kb == ModSettings.keyBindSettings) {
                mc.displayGuiScreen((GuiScreen)new GuiMinimap(null, XaeroMinimap.getSettings()));
            }
            if (kb == ModSettings.newWaypoint && Minimap.waypoints != null) {
                mc.displayGuiScreen((GuiScreen)new GuiAddWaypoint(null, XaeroMinimap.getSettings(), null));
            }
            if (kb == ModSettings.keyWaypoints && Minimap.waypoints != null) {
                mc.displayGuiScreen((GuiScreen)new GuiWaypoints(null));
            }*/
            if (kb == ModSettings.keyLargeMap) {
                Minimap.enlargedMap = true;
                Minimap.resetImage();
            }
            if (kb == ModSettings.keyToggleWaypoints) {
                try {
                    XaeroMinimap.getSettings().setOptionValue(ModOptions.INGAME_WAYPOINTS, 0);
                    XaeroMinimap.getSettings().setOptionValue(ModOptions.WAYPOINTS, 0);
                    XaeroMinimap.getSettings().saveSettings();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (kb == ModSettings.keyToggleMap) {
                try {
                    XaeroMinimap.getSettings().setOptionValue(ModOptions.MINIMAP, 0);
                    XaeroMinimap.getSettings().saveSettings();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (kb == ModSettings.keyToggleSlimes) {
                try {
                    XaeroMinimap.getSettings().setOptionValue(ModOptions.SLIME_CHUNKS, 0);
                    XaeroMinimap.getSettings().saveSettings();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (kb == ModSettings.keyToggleGrid) {
                try {
                    XaeroMinimap.getSettings().chunkGrid = -XaeroMinimap.getSettings().chunkGrid - 1;
                    XaeroMinimap.getSettings().saveSettings();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*if (kb == ModSettings.keyInstantWaypoint && Minimap.waypoints != null && Minimap.waypoints.list != null) {
                final Waypoint instant = new Waypoint(Minimap.myFloor(mc.thePlayer.posX), Minimap.myFloor(mc.thePlayer.posY), Minimap.myFloor(mc.thePlayer.posZ), "Waypoint", "X", (int)(Math.random() * ModSettings.ENCHANT_COLORS.length));
                Minimap.waypoints.list.add(instant);
                try {
                    XaeroMinimap.settings.saveWaypoints();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }*/
        }
    }
    
    public void keyUp(final KeyBinding kb, final boolean tickEnd) {
        if (!tickEnd) {
            if (kb == ModSettings.keyBindZoom) {
                try {
                    XaeroMinimap.getSettings().setOptionValue(ModOptions.ZOOM, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (kb == ModSettings.keyBindZoom1) {
                final ModSettings settings = XaeroMinimap.getSettings();
                --settings.zoom;
                if (XaeroMinimap.getSettings().zoom == -1) {
                    XaeroMinimap.getSettings().zoom = XaeroMinimap.getSettings().zooms.length - 1;
                }
                try {
                    XaeroMinimap.getSettings().saveSettings();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (kb == ModSettings.keyLargeMap) {
                Minimap.enlargedMap = false;
                Minimap.resetImage();
                Minimap.frameUpdateNeeded = Minimap.usingFBO();
            }
        }
    }
    
    static {
        ControlsHandler.toAdd = new KeyBinding[] { ModSettings.keyInstantWaypoint, ModSettings.keyToggleSlimes, ModSettings.keyToggleGrid, ModSettings.keyToggleWaypoints, ModSettings.keyToggleMap, ModSettings.keyLargeMap, ModSettings.keyWaypoints, ModSettings.keyBindZoom, ModSettings.keyBindZoom1, ModSettings.keyBindSettings, ModSettings.newWaypoint };
    }
}
