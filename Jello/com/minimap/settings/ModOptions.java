package com.minimap.settings;

import net.minecraft.util.*;
import net.minecraft.client.resources.*;

public enum ModOptions
{
    EDIT("gui.xaero_change_position", false, true), 
    RESET("gui.xaero_reset_defaults", false, true), 
    DEFAULT("Default", false, true), 
    DOTS("gui.xaero_entity_colours", false, true), 
    MINIMAP("gui.xaero_minimap", false, true), 
    CAVE_MAPS("gui.xaero_cave_maps", false, true), 
    DISPLAY_OTHER_TEAM("gui.xaero_display_teams", false, true), 
    WAYPOINTS("gui.xaero_display_waypoints", false, true), 
    INGAME_WAYPOINTS("gui.xaero_ingame_waypoints", false, true), 
    PLAYERS("gui.xaero_display_players", false, true), 
    HOSTILE("gui.xaero_display_hostile", false, true), 
    MOBS("gui.xaero_display_mobs", false, true), 
    ITEMS("gui.xaero_display_items", false, true), 
    ENTITIES("gui.xaero_display_other", false, true), 
    ZOOM("gui.xaero_zoom", false, true), 
    SIZE("gui.xaero_minimap_size", false, true), 
    EAMOUNT("gui.xaero_entity_amount", false, true), 
    COORDS("gui.xaero_display_coords", false, true), 
    NORTH("gui.xaero_lock_north", false, true), 
    DEATHPOINTS("gui.xaero_deathpoints", false, true), 
    OLD_DEATHPOINTS("gui.xaero_old_deathpoints", false, true), 
    CHUNK_GRID("gui.xaero_chunkgrid", false, true), 
    SLIME_CHUNKS("gui.xaero_slime_chunks", false, true), 
    SAFE_MAP("gui.xaero_safe_mode", false, true), 
    OPACITY("gui.xaero_opacity", true, false, 30.0f, 100.0f, 1.0f), 
    WAYPOINTS_SCALE("gui.xaero_waypoints_scale", true, false, 1.0f, 5.0f, 0.5f), 
    AA("gui.xaero_antialiasing", false, true), 
    DISTANCE("gui.xaero_show_distance", false, true), 
    COLOURS("gui.xaero_block_colours", false, true), 
    LIGHT("gui.xaero_lighting", false, true), 
    REDSTONE("gui.xaero_display_redstone", false, true), 
    DOTS_SCALE("gui.xaero_dots_scale", true, false, 1.0f, 2.0f, 0.5f), 
    COMPASS("gui.xaero_compass_over_wp", false, true), 
    BIOME("gui.xaero_current_biome", false, true), 
    ENTITY_HEIGHT("gui.xaero_entity_depth", false, true), 
    FLOWERS("gui.xaero_show_flowers", false, true), 
    KEEP_WP_NAMES("gui.xaero_waypoint_names", false, true), 
    WAYPOINTS_DISTANCE("gui.xaero_waypoints_distance", true, false, 0.0f, 50000.0f, 500.0f), 
    WAYPOINTS_DISTANCE_MIN("gui.xaero_waypoints_distance_min", true, false, 0.0f, 100.0f, 5.0f), 
    WAYPOINTS_TP("gui.xaero_teleport_command", false, true), 
    ARROW_SCALE("gui.xaero_arrow_scale", true, false, 1.0f, 2.0f, 0.1f), 
    ARROW_COLOUR("gui.xaero_arrow_colour", false, true), 
    SMOOTH_DOTS("gui.xaero_smooth_dots", false, true);
    
    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;
    private float valueMin;
    private float valueMax;
    private float valueStep;
    
    public static ModOptions getModOptions(final int par0) {
        for (final ModOptions enumoptions : values()) {
            if (enumoptions.returnEnumOrdinal() == par0) {
                return enumoptions;
            }
        }
        return null;
    }
    
    private ModOptions(final String par3Str, final boolean par4, final boolean par5) {
        this.enumString = par3Str;
        this.enumFloat = par4;
        this.enumBoolean = par5;
    }
    
    private ModOptions(final String p_i45004_3_, final boolean p_i45004_4_, final boolean p_i45004_5_, final float p_i45004_6_, final float p_i45004_7_, final float p_i45004_8_) {
        this.enumString = p_i45004_3_;
        this.enumFloat = p_i45004_4_;
        this.enumBoolean = p_i45004_5_;
        this.valueMin = p_i45004_6_;
        this.valueMax = p_i45004_7_;
        this.valueStep = p_i45004_8_;
    }
    
    public boolean getEnumFloat() {
        return this.enumFloat;
    }
    
    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }
    
    public int returnEnumOrdinal() {
        return this.ordinal();
    }
    
    public float getValueMax() {
        return this.valueMax;
    }
    
    public void setValueMax(final float p_148263_1_) {
        this.valueMax = p_148263_1_;
    }
    
    public float normalizeValue(final float p_148266_1_) {
        return MathHelper.clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
    }
    
    public float denormalizeValue(final float p_148262_1_) {
        return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0f, 1.0f));
    }
    
    public float snapToStepClamp(float p_148268_1_) {
        p_148268_1_ = this.snapToStep(p_148268_1_);
        return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
    }
    
    protected float snapToStep(float p_148264_1_) {
        if (this.valueStep > 0.0f) {
            p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
        }
        return p_148264_1_;
    }
    
    public String getEnumString() {
        return I18n.format(this.enumString, new Object[0]);
    }
}
