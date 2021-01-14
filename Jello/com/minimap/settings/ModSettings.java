package com.minimap.settings;

import net.minecraft.client.settings.*;
import com.minimap.events.*;
import com.minimap.*;
import net.minecraft.client.*;
import net.minecraft.server.*;
import com.minimap.minimap.*;
import java.util.*;
import java.io.*;
import com.minimap.interfaces.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.gui.*;
import java.awt.*;

public class ModSettings
{
    public static final int defaultSettings = Integer.MAX_VALUE;
    public static int ignoreUpdate;
    public static final String format = "§";
    private static boolean keysLoaded;
    public static final String[] ENCHANT_COLORS;
    public static final String[] ENCHANT_COLOR_NAMES;
    public static final int[] COLORS;
    public static final String[] MINIMAP_SIZE;
    public static int serverSettings;
    public static KeyBinding keyBindSettings;
    public static KeyBinding keyBindZoom;
    public static KeyBinding keyBindZoom1;
    public static KeyBinding newWaypoint;
    public static KeyBinding keyWaypoints;
    public static KeyBinding keyLargeMap;
    public static KeyBinding keyToggleMap;
    public static KeyBinding keyToggleWaypoints;
    public static KeyBinding keyToggleSlimes;
    public static KeyBinding keyToggleGrid;
    public static KeyBinding keyInstantWaypoint;
    private boolean minimap;
    public int zoom;
    public float[] zooms;
    public int entityAmount;
    private boolean showPlayers;
    private boolean showMobs;
    private boolean showHostile;
    private boolean showItems;
    private boolean showOther;
    private boolean caveMaps;
    private boolean showOtherTeam;
    private boolean showWaypoints;
    private boolean deathpoints;
    private boolean oldDeathpoints;
    public int chunkGrid;
    private boolean slimeChunks;
    private static HashMap<String, Long> serverSlimeSeeds;
    private boolean showIngameWaypoints;
    private boolean showCoords;
    private boolean lockNorth;
    private boolean antiAliasing;
    public boolean displayRedstone;
    public boolean mapSafeMode;
    public int distance;
    public static final String[] distanceTypes;
    public int blockColours;
    public static final String[] blockColourTypes;
    public boolean lighting;
    public boolean compassOverWaypoints;
    private int mapSize;
    public int playersColor;
    public int mobsColor;
    public int hostileColor;
    public int itemsColor;
    public int otherColor;
    public int otherTeamColor;
    public float minimapOpacity;
    public float waypointsScale;
    public float dotsScale;
    public static boolean settingsButton;
    public boolean showBiome;
    public static boolean updateNotification;
    public boolean showEntityHeight;
    public boolean showFlowers;
    public boolean keepWaypointNames;
    public float waypointsDistance;
    public float waypointsDistanceMin;
    public String waypointTp;
    public float arrowScale;
    public int arrowColour;
    public String[] arrowColourNames;
    public float[][] arrowColours;
    public boolean smoothDots;
    
    public ModSettings() {
        this.minimap = true;
        this.zoom = 2;
        this.zooms = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        this.entityAmount = 1;
        this.showPlayers = true;
        this.showMobs = true;
        this.showHostile = true;
        this.showItems = true;
        this.showOther = true;
        this.caveMaps = true;
        this.showOtherTeam = true;
        this.showWaypoints = true;
        this.deathpoints = true;
        this.oldDeathpoints = true;
        this.chunkGrid = -5;
        this.slimeChunks = false;
        this.showIngameWaypoints = true;
        this.showCoords = true;
        this.lockNorth = false;
        this.antiAliasing = true;
        this.displayRedstone = true;
        this.mapSafeMode = false;
        this.distance = 1;
        this.blockColours = 0;
        this.lighting = true;
        this.compassOverWaypoints = false;
        this.mapSize = -1;
        this.playersColor = 15;
        this.mobsColor = 14;
        this.hostileColor = 14;
        this.itemsColor = 12;
        this.otherColor = 5;
        this.otherTeamColor = -1;
        this.minimapOpacity = 100.0f;
        this.waypointsScale = 1.0f;
        this.dotsScale = 1.0f;
        this.showBiome = false;
        this.showEntityHeight = true;
        this.showFlowers = true;
        this.keepWaypointNames = false;
        this.waypointsDistance = 0.0f;
        this.waypointsDistanceMin = 0.0f;
        this.waypointTp = "tp";
        this.arrowScale = 1.5f;
        this.arrowColour = 0;
        this.arrowColourNames = new String[] { "gui.xaero_red", "gui.xaero_green", "gui.xaero_blue", "gui.xaero_yellow", "gui.xaero_purple", "gui.xaero_white", "gui.xaero_black", "gui.xaero_preset_classic" };
        this.arrowColours = new float[][] { { 0.8f, 0.1f, 0.1f, 1.0f }, { 0.09f, 0.57f, 0.0f, 1.0f }, { 0.0f, 0.55f, 1.0f, 1.0f }, { 1.0f, 0.93f, 0.0f, 1.0f }, { 0.73f, 0.33f, 0.83f, 1.0f }, { 1.0f, 1.0f, 1.0f, 1.0f }, { 0.0f, 0.0f, 0.0f, 1.0f }, { 0.4588f, 0.0f, 0.0f, 1.0f } };
        this.smoothDots = true;
        if (!ModSettings.keysLoaded) {
            XaeroMinimap.ch = new ControlsHandler();
            ModSettings.keysLoaded = true;
        }
    }
    
    public static boolean isKeyRepeat(final KeyBinding kb) {
        return kb != ModSettings.keyWaypoints && kb != ModSettings.keyBindSettings && kb != ModSettings.newWaypoint && kb != ModSettings.keyLargeMap && kb != ModSettings.keyToggleMap && kb != ModSettings.keyToggleWaypoints && kb != ModSettings.keyToggleSlimes && kb != ModSettings.keyToggleGrid && kb != ModSettings.keyInstantWaypoint;
    }
    
    public boolean getMinimap() {
        return this.minimap && !this.minimapDisabled();
    }
    
    public boolean getShowPlayers() {
        return this.showPlayers && !this.minimapDisplayPlayersDisabled();
    }
    
    public boolean getShowMobs() {
        return this.showMobs && !this.minimapDisplayMobsDisabled();
    }
    
    public boolean getShowHostile() {
        return this.showHostile && !this.minimapDisplayMobsDisabled();
    }
    
    public boolean getShowItems() {
        return this.showItems && !this.minimapDisplayItemsDisabled();
    }
    
    public boolean getShowOther() {
        return this.showOther && !this.minimapDisplayOtherDisabled();
    }
    
    public boolean getCaveMaps() {
        return this.caveMaps && !this.caveMapsDisabled();
    }
    
    public boolean getShowOtherTeam() {
        return this.showOtherTeam && !this.showOtherTeamDisabled();
    }
    
    public boolean getShowWaypoints() {
        return this.showWaypoints && !this.showWaypointsDisabled();
    }
    
    public boolean getDeathpoints() {
        return this.deathpoints && !this.deathpointsDisabled();
    }
    
    public boolean getOldDeathpoints() {
        return this.oldDeathpoints;
    }
    
    public void setSlimeChunksSeed(final long seed) {
        ModSettings.serverSlimeSeeds.put(Minimap.getCurrentWorldID(), seed);
    }
    
    public long getSlimeChunksSeed() {
        final MinecraftServer sp = (MinecraftServer)Minecraft.getMinecraft().getIntegratedServer();
        if (sp == null) {
            final Long saved = ModSettings.serverSlimeSeeds.get(Minimap.getCurrentWorldID());
            return (saved != null) ? saved : 0L;
        }
        final long seed = sp.getEntityWorld().getSeed();
        if (sp.getEntityWorld().provider.getDimensionId() != 0) {
            return 0L;
        }
        return seed;
    }
    
    /*public boolean customSlimeSeedNeeded() {
        return !(Minecraft.getMinecraft().currentScreen instanceof GuiSlimeSeed) && Minecraft.getMinecraft().getIntegratedServer() == null && Minecraft.getMinecraft().theWorld != null;
    }*/
    
    public boolean getSlimeChunks() {
        return this.slimeChunks && (Minecraft.getMinecraft().getIntegratedServer() != null || this.getSlimeChunksSeed() != 0L);
    }
    
    public boolean getShowIngameWaypoints() {
        return this.showIngameWaypoints && !this.showWaypointsDisabled();
    }
    
    public boolean getShowCoords() {
        return this.showCoords;
    }
    
    public boolean getLockNorth() {
        return this.lockNorth || Minimap.enlargedMap;
    }
    
    public boolean getAntiAliasing() {
        return !Minimap.triedFBO || (this.antiAliasing && Minimap.usingFBO());
    }
    
    public int getMinimapSize() {
        if (this.mapSize > -1) {
            return this.mapSize;
        }
        if(true)
        return 2;
        
        final int height = //480;//
        		Minecraft.getMinecraft().displayHeight;
        final int width = //854;//
        		Minecraft.getMinecraft().displayWidth;
        final int size = (height <= width) ? height : width;
        if (size <= 480) {
            return 0;
        }
        if (size <= 720) {
            return 1;
        }
        if (size <= 1080) {
            return 2;
        }
        return 3;
    }
    
    public boolean getSmoothDots() {
        return !Minimap.triedFBO || (this.smoothDots && Minimap.usingFBO());
    }
    
   /* public void saveWaypoints() throws IOException {
        final PrintWriter writer = new PrintWriter(new FileWriter(XaeroMinimap.waypointsFile));
        if (!Minimap.waypointMap.isEmpty()) {
            final Set<Map.Entry<String, WaypointWorld>> keyMap = Minimap.waypointMap.entrySet();
            for (final Map.Entry<String, WaypointWorld> entry : keyMap) {
                final String worldID = entry.getKey();
                final WaypointWorld world = entry.getValue();
                final Object[] keys = world.sets.keySet().toArray();
                if (keys.length > 1) {
                    writer.print("world:" + worldID + ":" + world.current);
                    for (int i = 0; i < keys.length; ++i) {
                        final String name = (String)keys[i];
                        if (!name.equals(world.current)) {
                            writer.print(":" + (String)keys[i]);
                        }
                    }
                    writer.println("");
                }
                for (int i = 0; i < keys.length; ++i) {
                    final String name = (String)keys[i];
                    final WaypointSet set = world.sets.get(name);
                    if (set != null) {
                        for (final Waypoint w : set.list) {
                            writer.println("waypoint:" + worldID + ":" + w.name.replaceAll(":", "§§") + ":" + w.symbol.replaceAll(":", "§§") + ":" + w.x + ":" + w.y + ":" + w.z + ":" + w.color + ":" + w.disabled + ":" + w.type + ":" + name + ":" + w.rotation + ":" + w.yaw);
                        }
                    }
                }
            }
        }
        writer.close();
    }*/
    
    /*/public boolean checkWaypointsLine(final String[] args) {
        if (args[0].equalsIgnoreCase("world")) {
            if (!args[1].contains("_")) {
                args[1] += "_null";
            }
            Minimap.addWorld(args[1]);
            final WaypointWorld map = Minimap.waypointMap.get(args[1]);
            map.current = args[2];
            for (int i = 2; i < args.length; ++i) {
                if (map.sets.get(args[i]) == null) {
                    map.sets.put(args[i], new WaypointSet(map));
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("waypoint")) {
            if (!args[1].contains("_")) {
                args[1] += "_null";
            }
            Minimap.addWorld(args[1]);
            String setName = "gui.xaero_default";
            if (args.length > 10) {
                setName = args[10];
            }
            final WaypointWorld map2 = Minimap.waypointMap.get(args[1]);
            WaypointSet waypoints = map2.sets.get(setName);
            if (waypoints == null) {
                map2.sets.put(setName, waypoints = new WaypointSet(map2));
            }
            final Waypoint loadWaypoint = new Waypoint(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), args[2].replaceAll("§§", ":"), args[3].replaceAll("§§", ":"), Integer.parseInt(args[7]));
            if (args.length > 8) {
                loadWaypoint.disabled = args[8].equals("true");
            }
            if (args.length > 9) {
                loadWaypoint.type = Integer.parseInt(args[9]);
            }
            if (args.length > 11) {
                loadWaypoint.rotation = args[11].equals("true");
            }
            if (args.length > 12) {
                loadWaypoint.yaw = Integer.parseInt(args[12]);
            }
            waypoints.list.add(loadWaypoint);
            return true;
        }
        return false;
    }*/
    
    /*public boolean loadWaypoints(final File file) throws IOException {
        if (!file.exists()) {
            return false;
        }
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
        while ((s = reader.readLine()) != null) {
            final String[] args = s.split(":");
            try {
                this.checkWaypointsLine(args);
            }
            catch (Exception e) {
                System.out.println("Skipping setting:" + args[0]);
            }
        }
        return true;
    }*/
    
    public void saveSettings() throws IOException {
        final PrintWriter writer = new PrintWriter(new FileWriter(XaeroMinimap.optionsFile));
        writer.println("ignoreUpdate:" + ModSettings.ignoreUpdate);
        writer.println("updateNotification:" + ModSettings.updateNotification);
        writer.println("settingsButton:" + ModSettings.settingsButton);
        writer.println("minimap:" + this.minimap);
        writer.println("caveMaps:" + this.caveMaps);
        writer.println("showPlayers:" + this.showPlayers);
        writer.println("showHostile:" + this.showHostile);
        writer.println("showMobs:" + this.showMobs);
        writer.println("showItems:" + this.showItems);
        writer.println("showOther:" + this.showOther);
        writer.println("showOtherTeam:" + this.showOtherTeam);
        writer.println("showWaypoints:" + this.showWaypoints);
        writer.println("showIngameWaypoints:" + this.showIngameWaypoints);
        writer.println("displayRedstone:" + this.displayRedstone);
        writer.println("deathpoints:" + this.deathpoints);
        writer.println("oldDeathpoints:" + this.oldDeathpoints);
        writer.println("distance:" + this.distance);
        writer.println("showCoords:" + this.showCoords);
        writer.println("lockNorth:" + this.lockNorth);
        writer.println("zoom:" + this.zoom);
        writer.println("mapSize:" + this.mapSize);
        writer.println("entityAmount:" + this.entityAmount);
        writer.println("chunkGrid:" + this.chunkGrid);
        writer.println("slimeChunks:" + this.slimeChunks);
        writer.println("playersColor:" + this.playersColor);
        writer.println("mobsColor:" + this.mobsColor);
        writer.println("hostileColor:" + this.hostileColor);
        writer.println("itemsColor:" + this.itemsColor);
        writer.println("otherColor:" + this.otherColor);
        writer.println("otherTeamColor:" + this.otherTeamColor);
        writer.println("mapSafeMode:" + this.mapSafeMode);
        writer.println("minimapOpacity:" + this.minimapOpacity);
        writer.println("waypointsScale:" + this.waypointsScale);
        writer.println("antiAliasing:" + this.antiAliasing);
        writer.println("blockColours:" + this.blockColours);
        writer.println("lighting:" + this.lighting);
        writer.println("dotsScale:" + this.waypointsScale);
        writer.println("compassOverWaypoints:" + this.compassOverWaypoints);
        writer.println("showBiome:" + this.showBiome);
        writer.println("showEntityHeight:" + this.showEntityHeight);
        writer.println("showFlowers:" + this.showFlowers);
        writer.println("keepWaypointNames:" + this.keepWaypointNames);
        writer.println("waypointsDistance:" + this.waypointsDistance);
        writer.println("waypointsDistanceMin:" + this.waypointsDistanceMin);
        writer.println("waypointTp:" + this.waypointTp);
        writer.println("arrowScale:" + this.arrowScale);
        writer.println("arrowColour:" + this.arrowColour);
        writer.println("smoothDots:" + this.smoothDots);
        final Object[] keys = ModSettings.serverSlimeSeeds.keySet().toArray();
        final Object[] values = ModSettings.serverSlimeSeeds.values().toArray();
        for (int i = 0; i < keys.length; ++i) {
            writer.println("seed:" + keys[i] + ":" + values[i]);
        }
        for (final Interface l : InterfaceHandler.list) {
            writer.println("interface:" + l.iname + ":" + l.actualx + ":" + l.actualy + ":" + l.centered + ":" + l.flipped + ":" + l.fromRight);
        }
        writer.println("#WAYPOINTS HAVE BEEN MOVED TO xaerowaypoints.txt!");
        for (int i = 0; i < ControlsHandler.toAdd.length; ++i) {
            final KeyBinding kb = ControlsHandler.toAdd[i];
            writer.println("key_" + kb.getKeyDescription() + ":" + kb.getKeyCode());
        }
        writer.close();
       // this.saveWaypoints();
    }
    
    public void loadSettings() throws IOException {
        if (!XaeroMinimap.optionsFile.exists()) {
            this.saveSettings();
            return;
        }
        //Minimap.waypointMap.clear();
        final BufferedReader reader = new BufferedReader(new FileReader(XaeroMinimap.optionsFile));
        boolean saveWaypoints = false;
        String s;
        while ((s = reader.readLine()) != null) {
            final String[] args = s.split(":");
            try {
                if (args[0].equalsIgnoreCase("ignoreUpdate")) {
                    ModSettings.ignoreUpdate = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("updateNotification")) {
                    ModSettings.updateNotification = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("settingsButton")) {
                    ModSettings.settingsButton = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("minimap")) {
                    this.minimap = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("caveMaps")) {
                    this.caveMaps = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showPlayers")) {
                    this.showPlayers = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showHostile")) {
                    this.showHostile = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showMobs")) {
                    this.showMobs = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showItems")) {
                    this.showItems = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showOther")) {
                    this.showOther = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showOtherTeam")) {
                    this.showOtherTeam = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showWaypoints")) {
                    this.showWaypoints = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("deathpoints")) {
                    this.deathpoints = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("oldDeathpoints")) {
                    this.oldDeathpoints = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showIngameWaypoints")) {
                    this.showIngameWaypoints = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("displayRedstone")) {
                    this.displayRedstone = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("distance")) {
                    this.distance = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("showCoords")) {
                    this.showCoords = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("lockNorth")) {
                    this.lockNorth = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("zoom")) {
                    this.zoom = Integer.parseInt(args[1]);
                    if (this.zoom < this.zooms.length) {
                        continue;
                    }
                    this.zoom = this.zooms.length - 1;
                }
                else if (args[0].equalsIgnoreCase("mapSize")) {
                    this.mapSize = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("entityAmount")) {
                    this.entityAmount = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("chunkGrid")) {
                    this.chunkGrid = (args[1].equals("true") ? 4 : (args[1].equals("false") ? -5 : Integer.parseInt(args[1])));
                }
                else if (args[0].equalsIgnoreCase("slimeChunks")) {
                    this.slimeChunks = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("playersColor")) {
                    this.playersColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("mobsColor")) {
                    this.mobsColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("hostileColor")) {
                    this.hostileColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("itemsColor")) {
                    this.itemsColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("otherColor")) {
                    this.otherColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("otherTeamColor")) {
                    this.otherTeamColor = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("mapSafeMode")) {
                    this.mapSafeMode = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("minimapOpacity")) {
                    this.minimapOpacity = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("waypointsScale")) {
                    this.waypointsScale = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("antiAliasing")) {
                    this.antiAliasing = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("blockColours")) {
                    this.blockColours = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("lighting")) {
                    this.lighting = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("dotsScale")) {
                    this.dotsScale = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("compassOverWaypoints")) {
                    this.compassOverWaypoints = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showBiome")) {
                    this.showBiome = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showEntityHeight")) {
                    this.showEntityHeight = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("showFlowers")) {
                    this.showFlowers = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("keepWaypointNames")) {
                    this.keepWaypointNames = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("waypointsDistance")) {
                    this.waypointsDistance = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("waypointsDistanceMin")) {
                    this.waypointsDistanceMin = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("waypointTp")) {
                    this.waypointTp = args[1];
                }
                else if (args[0].equalsIgnoreCase("arrowScale")) {
                    this.arrowScale = Float.valueOf(args[1]);
                }
                else if (args[0].equalsIgnoreCase("arrowColour")) {
                    this.arrowColour = Integer.parseInt(args[1]);
                }
                else if (args[0].equalsIgnoreCase("seed")) {
                    ModSettings.serverSlimeSeeds.put(args[1], Long.parseLong(args[2]));
                }
                else if (args[0].equalsIgnoreCase("smoothDots")) {
                    this.smoothDots = args[1].equals("true");
                }
                else if (args[0].equalsIgnoreCase("interface")) {
                    for (final Interface l : InterfaceHandler.list) {
                        if (args[1].equals(l.iname)) {
                            final Interface interface1 = l;
                            final Interface interface2 = l;
                            final Interface interface3 = l;
                            final int int1 = Integer.parseInt(args[2]);
                            interface3.x = int1;
                            interface2.actualx = int1;
                            interface1.bx = int1;
                            final Interface interface4 = l;
                            final Interface interface5 = l;
                            final Interface interface6 = l;
                            final int int2 = Integer.parseInt(args[3]);
                            interface6.y = int2;
                            interface5.actualy = int2;
                            interface4.by = int2;
                            final Interface interface7 = l;
                            final Interface interface8 = l;
                            final boolean equals = args[4].equals("true");
                            interface8.centered = equals;
                            interface7.bcentered = equals;
                            final Interface interface9 = l;
                            final Interface interface10 = l;
                            final boolean equals2 = args[5].equals("true");
                            interface10.flipped = equals2;
                            interface9.bflipped = equals2;
                            final Interface interface11 = l;
                            final Interface interface12 = l;
                            final boolean equals3 = args[6].equals("true");
                            interface12.fromRight = equals3;
                            interface11.bfromRight = equals3;
                        }
                    }
                }
                else if (args[0].startsWith("key_")) {
                    for (int i = 0; i < ControlsHandler.toAdd.length; ++i) {
                        final KeyBinding kb = ControlsHandler.toAdd[i];
                        if (kb.getKeyDescription().equals(args[0].substring(4))) {
                            kb.setKeyCode(Integer.parseInt(args[1]));
                            break;
                        }
                    }
                }
                else {
                   /* if (!this.checkWaypointsLine(args)) {
                        continue;
                    }
                    saveWaypoints = true;*/
                }
            }
            catch (Exception e) {
                System.out.println("Skipping setting:" + args[0]);
            }
        }
        //this.loadWaypoints(XaeroMinimap.waypointsFile);
        if (saveWaypoints) {
            //this.saveWaypoints();
            this.saveSettings();
        }
        KeyBinding.resetKeyBindingArrayAndHash();
    }
    
    public String getKeyBinding(final ModOptions par1EnumOptions) {
        String s = par1EnumOptions.getEnumString() + ": ";
        if (par1EnumOptions == ModOptions.DOTS_SCALE && !Minimap.usingFBO()) {
            return s + "§e" + getTranslation(false);
        }
        if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE) {
            if (this.waypointsDistance == 0.0f) {
                s += I18n.format("gui.xaero_unlimited", new Object[0]);
            }
            else {
                s = s + (int)this.waypointsDistance + "m";
            }
        }
        else if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE_MIN) {
            if (this.waypointsDistanceMin == 0.0f) {
                s += I18n.format("gui.xaero_off", new Object[0]);
            }
            else {
                s = s + (int)this.waypointsDistanceMin + "m";
            }
        }
        else {
            if (par1EnumOptions.getEnumFloat()) {
                String f1 = "" + this.getOptionFloatValue(par1EnumOptions);
                if (par1EnumOptions == ModOptions.ARROW_SCALE) {
                    f1 += "x";
                }
                return s + f1;
            }
            if (par1EnumOptions == ModOptions.CHUNK_GRID) {
                s += ((this.chunkGrid > -1) ? ("§" + ModSettings.ENCHANT_COLORS[this.chunkGrid] + I18n.format(ModSettings.ENCHANT_COLOR_NAMES[this.chunkGrid], new Object[0])) : I18n.format("gui.xaero_off", new Object[0]));
            }
            else if (par1EnumOptions == ModOptions.EDIT) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.DOTS) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.RESET) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.WAYPOINTS_TP) {
                s = par1EnumOptions.getEnumString();
            }
            else if (par1EnumOptions == ModOptions.ZOOM) {
                s = s + this.zooms[this.zoom] + "x";
            }
            else if (par1EnumOptions == ModOptions.COLOURS) {
                s += I18n.format(ModSettings.blockColourTypes[this.blockColours], new Object[0]);
            }
            else if (par1EnumOptions == ModOptions.DISTANCE) {
                s += I18n.format(ModSettings.distanceTypes[this.distance], new Object[0]);
            }
           /* else if (par1EnumOptions == ModOptions.SLIME_CHUNKS && this.customSlimeSeedNeeded()) {
                s = par1EnumOptions.getEnumString();
            }*/
            else if (par1EnumOptions == ModOptions.SIZE) {
                s += I18n.format((this.mapSize > -1) ? ModSettings.MINIMAP_SIZE[this.mapSize] : "gui.xaero_auto_map_size", new Object[0]);
            }
            else if (par1EnumOptions == ModOptions.EAMOUNT) {
                s = ((this.entityAmount == 0) ? (s + I18n.format("gui.xaero_unlimited", new Object[0])) : (s + 100 * this.entityAmount));
            }
            else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
                s += I18n.format(this.arrowColourNames[this.arrowColour], new Object[0]);
            }
            else {
                final boolean clientSetting = this.getClientBooleanValue(par1EnumOptions);
                final boolean serverSetting = this.getBooleanValue(par1EnumOptions);
                s = s + getTranslation(clientSetting) + ((serverSetting != clientSetting) ? ("§e (" + getTranslation(serverSetting) + ")") : "");
            }
        }
        return s;
    }
    
    public boolean getBooleanValue(final ModOptions o) {
        if (o == ModOptions.MINIMAP) {
            return this.getMinimap();
        }
        if (o == ModOptions.CAVE_MAPS) {
            return this.getCaveMaps();
        }
        if (o == ModOptions.DISPLAY_OTHER_TEAM) {
            return this.getShowOtherTeam();
        }
        if (o == ModOptions.WAYPOINTS) {
            return this.getShowWaypoints();
        }
        if (o == ModOptions.DEATHPOINTS) {
            return this.getDeathpoints();
        }
        if (o == ModOptions.OLD_DEATHPOINTS) {
            return this.getOldDeathpoints();
        }
        if (o == ModOptions.INGAME_WAYPOINTS) {
            return this.getShowIngameWaypoints();
        }
        if (o == ModOptions.COORDS) {
            return this.getShowCoords();
        }
        if (o == ModOptions.NORTH) {
            return this.getLockNorth();
        }
        if (o == ModOptions.PLAYERS) {
            return this.getShowPlayers();
        }
        if (o == ModOptions.HOSTILE) {
            return this.getShowHostile();
        }
        if (o == ModOptions.MOBS) {
            return this.getShowMobs();
        }
        if (o == ModOptions.ITEMS) {
            return this.getShowItems();
        }
        if (o == ModOptions.ENTITIES) {
            return this.getShowOther();
        }
        if (o == ModOptions.SAFE_MAP) {
            return Minimap.triedFBO && !Minimap.usingFBO();
        }
        if (o == ModOptions.AA) {
            return this.getAntiAliasing();
        }
        if (o == ModOptions.SMOOTH_DOTS) {
            return this.getSmoothDots();
        }
        return this.getClientBooleanValue(o);
    }
    
    public boolean getClientBooleanValue(final ModOptions o) {
        if (o == ModOptions.MINIMAP) {
            return this.minimap;
        }
        if (o == ModOptions.CAVE_MAPS) {
            return this.caveMaps;
        }
        if (o == ModOptions.DISPLAY_OTHER_TEAM) {
            return this.showOtherTeam;
        }
        if (o == ModOptions.WAYPOINTS) {
            return this.showWaypoints;
        }
        if (o == ModOptions.DEATHPOINTS) {
            return this.deathpoints;
        }
        if (o == ModOptions.OLD_DEATHPOINTS) {
            return this.oldDeathpoints;
        }
        if (o == ModOptions.INGAME_WAYPOINTS) {
            return this.showIngameWaypoints;
        }
        if (o == ModOptions.REDSTONE) {
            return this.displayRedstone;
        }
        if (o == ModOptions.COORDS) {
            return this.showCoords;
        }
        if (o == ModOptions.NORTH) {
            return this.lockNorth;
        }
        if (o == ModOptions.PLAYERS) {
            return this.showPlayers;
        }
        if (o == ModOptions.HOSTILE) {
            return this.showHostile;
        }
        if (o == ModOptions.MOBS) {
            return this.showMobs;
        }
        if (o == ModOptions.ITEMS) {
            return this.showItems;
        }
        if (o == ModOptions.ENTITIES) {
            return this.showOther;
        }
        if (o == ModOptions.SLIME_CHUNKS) {
            return this.slimeChunks;
        }
        if (o == ModOptions.SAFE_MAP) {
            return this.mapSafeMode;
        }
        if (o == ModOptions.AA) {
            return this.antiAliasing;
        }
        if (o == ModOptions.LIGHT) {
            return this.lighting;
        }
        if (o == ModOptions.COMPASS) {
            return this.compassOverWaypoints;
        }
        if (o == ModOptions.BIOME) {
            return this.showBiome;
        }
        if (o == ModOptions.ENTITY_HEIGHT) {
            return this.showEntityHeight;
        }
        if (o == ModOptions.FLOWERS) {
            return this.showFlowers;
        }
        if (o == ModOptions.KEEP_WP_NAMES) {
            return this.keepWaypointNames;
        }
        return o == ModOptions.SMOOTH_DOTS && this.smoothDots;
    }
    
    private static String getTranslation(final boolean o) {
        return I18n.format("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
    }
    
    public void setOptionValue(final ModOptions par1EnumOptions, final int par2) throws IOException {
        if (par1EnumOptions == ModOptions.ZOOM) {
            this.zoom = (this.zoom + 1) % this.zooms.length;
        }
        else if (par1EnumOptions == ModOptions.SIZE) {
            if (this.mapSize == 3) {
                this.mapSize = -1;
            }
            else {
                this.mapSize = (this.mapSize + 1) % 4;
            }
        }
        else if (par1EnumOptions == ModOptions.EAMOUNT) {
            this.entityAmount = (this.entityAmount + 1) % 11;
        }
        else if (par1EnumOptions == ModOptions.MINIMAP) {
            this.minimap = !this.minimap;
        }
        else if (par1EnumOptions == ModOptions.CAVE_MAPS) {
            this.caveMaps = !this.caveMaps;
        }
        else if (par1EnumOptions == ModOptions.DISPLAY_OTHER_TEAM) {
            this.showOtherTeam = !this.showOtherTeam;
        }
        else if (par1EnumOptions == ModOptions.WAYPOINTS) {
            this.showWaypoints = !this.showWaypoints;
        }
        else if (par1EnumOptions == ModOptions.DEATHPOINTS) {
            this.deathpoints = !this.deathpoints;
        }
        else if (par1EnumOptions == ModOptions.OLD_DEATHPOINTS) {
            this.oldDeathpoints = !this.oldDeathpoints;
        }
        else if (par1EnumOptions == ModOptions.INGAME_WAYPOINTS) {
            this.showIngameWaypoints = !this.showIngameWaypoints;
        }
        else if (par1EnumOptions == ModOptions.REDSTONE) {
            this.displayRedstone = !this.displayRedstone;
        }
        else if (par1EnumOptions == ModOptions.DISTANCE) {
            this.distance = (this.distance + 1) % ModSettings.distanceTypes.length;
        }
        else if (par1EnumOptions == ModOptions.COORDS) {
            this.showCoords = !this.showCoords;
        }
        else if (par1EnumOptions == ModOptions.NORTH) {
            this.lockNorth = !this.lockNorth;
        }
        else if (par1EnumOptions == ModOptions.PLAYERS) {
            this.showPlayers = !this.showPlayers;
        }
        else if (par1EnumOptions == ModOptions.HOSTILE) {
            this.showHostile = !this.showHostile;
        }
        else if (par1EnumOptions == ModOptions.MOBS) {
            this.showMobs = !this.showMobs;
        }
        else if (par1EnumOptions == ModOptions.ITEMS) {
            this.showItems = !this.showItems;
        }
        else if (par1EnumOptions == ModOptions.ENTITIES) {
            this.showOther = !this.showOther;
        }
        else if (par1EnumOptions == ModOptions.CHUNK_GRID) {
            if (this.chunkGrid == ModSettings.COLORS.length - 1) {
                this.chunkGrid = -4;
            }
            else {
                if (this.chunkGrid < -1) {
                    this.chunkGrid = -1;
                }
                ++this.chunkGrid;
            }
            Minimap.frameUpdateNeeded = Minimap.usingFBO();
        }
        else if (par1EnumOptions == ModOptions.SLIME_CHUNKS) {
           /* if (this.customSlimeSeedNeeded()) {
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiSlimeSeed(Minecraft.getMinecraft().currentScreen));
                return;
            }*/
            this.slimeChunks = !this.slimeChunks;
            Minimap.frameUpdateNeeded = Minimap.usingFBO();
        }
        else if (par1EnumOptions == ModOptions.SAFE_MAP) {
            this.mapSafeMode = !this.mapSafeMode;
            Minimap.resetImage();
            Minimap.frameUpdateNeeded = Minimap.usingFBO();
        }
        else if (par1EnumOptions == ModOptions.AA) {
            this.antiAliasing = !this.antiAliasing;
        }
        else if (par1EnumOptions == ModOptions.COLOURS) {
            this.blockColours = (this.blockColours + 1) % ModSettings.blockColourTypes.length;
        }
        else if (par1EnumOptions == ModOptions.LIGHT) {
            this.lighting = !this.lighting;
        }
        else if (par1EnumOptions == ModOptions.COMPASS) {
            this.compassOverWaypoints = !this.compassOverWaypoints;
        }
        else if (par1EnumOptions == ModOptions.BIOME) {
            this.showBiome = !this.showBiome;
        }
        else if (par1EnumOptions == ModOptions.ENTITY_HEIGHT) {
            this.showEntityHeight = !this.showEntityHeight;
        }
        else if (par1EnumOptions == ModOptions.FLOWERS) {
            this.showFlowers = !this.showFlowers;
        }
        else if (par1EnumOptions == ModOptions.KEEP_WP_NAMES) {
            this.keepWaypointNames = !this.keepWaypointNames;
        }
        else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
            this.arrowColour = (this.arrowColour + 1) % this.arrowColours.length;
        }
        else if (par1EnumOptions == ModOptions.SMOOTH_DOTS) {
            this.smoothDots = !this.smoothDots;
        }
        this.saveSettings();
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().currentScreen.initGui();
        }
    }
    
    public void setOptionFloatValue(final ModOptions options, final float f) throws IOException {
        if (options == ModOptions.OPACITY) {
            this.minimapOpacity = f;
        }
        if (options == ModOptions.WAYPOINTS_SCALE) {
            this.waypointsScale = f;
        }
        if (options == ModOptions.DOTS_SCALE) {
            this.dotsScale = f;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE) {
            this.waypointsDistance = (int)f;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
            this.waypointsDistanceMin = (int)f;
        }
        if (options == ModOptions.ARROW_SCALE) {
            this.arrowScale = f;
        }
        this.saveSettings();
    }
    
    public float getOptionFloatValue(final ModOptions options) {
        if (options == ModOptions.OPACITY) {
            return this.minimapOpacity;
        }
        if (options == ModOptions.WAYPOINTS_SCALE) {
            return this.waypointsScale;
        }
        if (options == ModOptions.DOTS_SCALE) {
            return this.dotsScale;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE) {
            return this.waypointsDistance;
        }
        if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
            return this.waypointsDistanceMin;
        }
        if (options == ModOptions.ARROW_SCALE) {
            return this.arrowScale;
        }
        return 1.0f;
    }
    
    public boolean minimapDisabled() {
        return (ModSettings.serverSettings & 0x1) != 0x1;
    }
    
    public boolean minimapDisplayPlayersDisabled() {
        return (ModSettings.serverSettings & 0x400) != 0x400;
    }
    
    public boolean minimapDisplayMobsDisabled() {
        return (ModSettings.serverSettings & 0x800) != 0x800;
    }
    
    public boolean minimapDisplayItemsDisabled() {
        return (ModSettings.serverSettings & 0x1000) != 0x1000;
    }
    
    public boolean minimapDisplayOtherDisabled() {
        return (ModSettings.serverSettings & 0x2000) != 0x2000;
    }
    
    public boolean caveMapsDisabled() {
        return (ModSettings.serverSettings & 0x4000) != 0x4000;
    }
    
    public boolean showOtherTeamDisabled() {
        return (ModSettings.serverSettings & 0x8000) != 0x8000;
    }
    
    public boolean showWaypointsDisabled() {
        return (ModSettings.serverSettings & 0x10000) != 0x10000;
    }
    
    public boolean deathpointsDisabled() {
        return (ModSettings.serverSettings & 0x200000) == 0x0;
    }
    
    public void resetServerSettings() {
        ModSettings.serverSettings = Integer.MAX_VALUE;
    }
    
    public static void setServerSettings() {
    }
    
    static {
        ModSettings.keysLoaded = false;
        ENCHANT_COLORS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        ENCHANT_COLOR_NAMES = new String[] { "gui.xaero_black", "gui.xaero_dark_blue", "gui.xaero_dark_green", "gui.xaero_dark_aqua", "gui.xaero_dark_red", "gui.xaero_dark_purple", "gui.xaero_gold", "gui.xaero_gray", "gui.xaero_dark_gray", "gui.xaero_blue", "gui.xaero_green", "gui.xaero_aqua", "gui.xaero_red", "gui.xaero_purple", "gui.xaero_yellow", "gui.xaero_white" };
        COLORS = new int[] { new Color(0, 0, 0, 255).hashCode(), new Color(0, 0, 170, 255).hashCode(), new Color(0, 170, 0, 255).hashCode(), new Color(0, 170, 170, 255).hashCode(), new Color(170, 0, 0, 255).hashCode(), new Color(170, 0, 170, 255).hashCode(), new Color(255, 170, 0, 255).hashCode(), new Color(170, 170, 170, 255).hashCode(), new Color(85, 85, 85, 255).hashCode(), new Color(85, 85, 255, 255).hashCode(), new Color(85, 255, 85, 255).hashCode(), new Color(85, 255, 255, 255).hashCode(), new Color(255, 0, 0, 255).hashCode(), new Color(255, 85, 255, 255).hashCode(), new Color(255, 255, 85, 255).hashCode(), new Color(255, 255, 255, 255).hashCode() };
        MINIMAP_SIZE = new String[] { "gui.xaero_tiny", "gui.xaero_small", "gui.xaero_medium", "gui.xaero_large" };
        ModSettings.serverSettings = Integer.MAX_VALUE;
        ModSettings.keyBindSettings = new KeyBinding("gui.xaero_minimap_settings", 21, "Xaero's Minimap");
        ModSettings.keyBindZoom = new KeyBinding("gui.xaero_zoom_in", 23, "Xaero's Minimap");
        ModSettings.keyBindZoom1 = new KeyBinding("gui.xaero_zoom_out", 24, "Xaero's Minimap");
        ModSettings.newWaypoint = new KeyBinding("gui.xaero_new_waypoint", 48, "Xaero's Minimap");
        ModSettings.keyWaypoints = new KeyBinding("gui.xaero_waypoints_key", 22, "Xaero's Minimap");
        ModSettings.keyLargeMap = new KeyBinding("gui.xaero_enlarge_map", 44, "Xaero's Minimap");
        ModSettings.keyToggleMap = new KeyBinding("gui.xaero_toggle_map", 35, "Xaero's Minimap");
        ModSettings.keyToggleWaypoints = new KeyBinding("gui.xaero_toggle_waypoints", 40, "Xaero's Minimap");
        ModSettings.keyToggleSlimes = new KeyBinding("gui.xaero_toggle_slime", 0, "Xaero's Minimap");
        ModSettings.keyToggleGrid = new KeyBinding("gui.xaero_toggle_grid", 0, "Xaero's Minimap");
        ModSettings.keyInstantWaypoint = new KeyBinding("gui.xaero_instant_waypoint", 78, "Xaero's Minimap");
        ModSettings.serverSlimeSeeds = new HashMap<String, Long>();
        distanceTypes = new String[] { "gui.xaero_off", "gui.xaero_looking_at", "gui.xaero_all" };
        blockColourTypes = new String[] { "gui.xaero_accurate", "gui.xaero_vanilla" };
        ModSettings.settingsButton = true;
        ModSettings.updateNotification = true;
    }
}
