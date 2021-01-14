package de.iotacb.client.minimap.settings;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import de.iotacb.client.minimap.Minimap;
import de.iotacb.client.minimap.XaeroMinimap;
import de.iotacb.client.minimap.interfaces.Interface;
import de.iotacb.client.minimap.interfaces.InterfaceHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.server.MinecraftServer;

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
    }
    
    public boolean getMinimap() {
        return this.minimap && !this.minimapDisabled();
    }
    
    public boolean getShowPlayers() {
        return true;
    }
    
    public boolean getShowMobs() {
        return false;
    }
    
    public boolean getShowHostile() {
        return this.showHostile && !this.minimapDisplayMobsDisabled();
    }
    
    public boolean getShowItems() {
        return false;
    }
    
    public boolean getShowOther() {
        return false;
    }
    
    public boolean getCaveMaps() {
        return false;
    }
    
    public boolean getShowOtherTeam() {
        return false;
    }
    
    public boolean getShowWaypoints() {
        return false;
    }
    
    public boolean getDeathpoints() {
        return false;
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
        return 2;
    }
    
    public boolean getSmoothDots() {
        return !Minimap.triedFBO || (this.smoothDots && Minimap.usingFBO());
    }
    
    
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
        writer.close();
    }
    
    public void loadSettings() throws IOException {
        if (!XaeroMinimap.optionsFile.exists()) {
            this.saveSettings();
            return;
        }
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
//                else if (args[0].equalsIgnoreCase("interface")) {
//                    for (final Interface l : InterfaceHandler.list) {
//                        if (args[1].equals(l.iname)) {
//                            final Interface interface1 = l;
//                            final Interface interface2 = l;
//                            final Interface interface3 = l;
//                            final int int1 = Integer.parseInt(args[2]);
//                            interface3.x = int1;
//                            interface2.actualx = int1;
//                            interface1.bx = int1;
//                            final Interface interface4 = l;
//                            final Interface interface5 = l;
//                            final Interface interface6 = l;
//                            final int int2 = Integer.parseInt(args[3]);
//                            interface6.y = int2;
//                            interface5.actualy = int2;
//                            interface4.by = int2;
//                            final Interface interface7 = l;
//                            final Interface interface8 = l;
//                            final boolean equals = args[4].equals("true");
//                            interface8.centered = equals;
//                            interface7.bcentered = equals;
//                            final Interface interface9 = l;
//                            final Interface interface10 = l;
//                            final boolean equals2 = args[5].equals("true");
//                            interface10.flipped = equals2;
//                            interface9.bflipped = equals2;
//                            final Interface interface11 = l;
//                            final Interface interface12 = l;
//                            final boolean equals3 = args[6].equals("true");
//                            interface12.fromRight = equals3;
//                            interface11.bfromRight = equals3;
//                        }
//                    }
//                }
            }
            catch (Exception e) {
                System.out.println("Skipping setting:" + args[0]);
            }
        }
        if (saveWaypoints) {
            this.saveSettings();
        }
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
        return this.smoothDots;
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
        ENCHANT_COLORS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        ENCHANT_COLOR_NAMES = new String[] { "gui.xaero_black", "gui.xaero_dark_blue", "gui.xaero_dark_green", "gui.xaero_dark_aqua", "gui.xaero_dark_red", "gui.xaero_dark_purple", "gui.xaero_gold", "gui.xaero_gray", "gui.xaero_dark_gray", "gui.xaero_blue", "gui.xaero_green", "gui.xaero_aqua", "gui.xaero_red", "gui.xaero_purple", "gui.xaero_yellow", "gui.xaero_white" };
        COLORS = new int[] { new Color(0, 0, 0, 255).hashCode(), new Color(0, 0, 170, 255).hashCode(), new Color(0, 170, 0, 255).hashCode(), new Color(0, 170, 170, 255).hashCode(), new Color(170, 0, 0, 255).hashCode(), new Color(170, 0, 170, 255).hashCode(), new Color(255, 170, 0, 255).hashCode(), new Color(170, 170, 170, 255).hashCode(), new Color(85, 85, 85, 255).hashCode(), new Color(85, 85, 255, 255).hashCode(), new Color(85, 255, 85, 255).hashCode(), new Color(85, 255, 255, 255).hashCode(), new Color(255, 0, 0, 255).hashCode(), new Color(255, 85, 255, 255).hashCode(), new Color(255, 255, 85, 255).hashCode(), new Color(255, 255, 255, 255).hashCode() };
        MINIMAP_SIZE = new String[] { "gui.xaero_tiny", "gui.xaero_small", "gui.xaero_medium", "gui.xaero_large" };
        ModSettings.serverSettings = Integer.MAX_VALUE;
        ModSettings.serverSlimeSeeds = new HashMap<String, Long>();
        distanceTypes = new String[] { "gui.xaero_off", "gui.xaero_looking_at", "gui.xaero_all" };
        blockColourTypes = new String[] { "gui.xaero_accurate", "gui.xaero_vanilla" };
        ModSettings.settingsButton = true;
        ModSettings.updateNotification = true;
    }
}
