// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.Set;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import java.util.TreeMap;

public class GameRules
{
    private TreeMap theGameRules;
    private static final String __OBFID = "CL_00000136";
    
    public GameRules() {
        this.theGameRules = new TreeMap();
        this.addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
    }
    
    public void addGameRule(final String key, final String value, final ValueType type) {
        this.theGameRules.put(key, new Value(value, type));
    }
    
    public void setOrCreateGameRule(final String key, final String ruleValue) {
        final Value gamerules$value = this.theGameRules.get(key);
        if (gamerules$value != null) {
            gamerules$value.setValue(ruleValue);
        }
        else {
            this.addGameRule(key, ruleValue, ValueType.ANY_VALUE);
        }
    }
    
    public String getString(final String name) {
        final Value gamerules$value = this.theGameRules.get(name);
        return (gamerules$value != null) ? gamerules$value.getString() : "";
    }
    
    public boolean getBoolean(final String name) {
        final Value gamerules$value = this.theGameRules.get(name);
        return gamerules$value != null && gamerules$value.getBoolean();
    }
    
    public int getInt(final String name) {
        final Value gamerules$value = this.theGameRules.get(name);
        return (gamerules$value != null) ? gamerules$value.getInt() : 0;
    }
    
    public NBTTagCompound writeToNBT() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (final Object s : this.theGameRules.keySet()) {
            final Value gamerules$value = this.theGameRules.get(s);
            nbttagcompound.setString((String)s, gamerules$value.getString());
        }
        return nbttagcompound;
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        for (final String s : nbt.getKeySet()) {
            final String s2 = nbt.getString(s);
            this.setOrCreateGameRule(s, s2);
        }
    }
    
    public String[] getRules() {
        final Set set = this.theGameRules.keySet();
        return set.toArray(new String[set.size()]);
    }
    
    public boolean hasRule(final String name) {
        return this.theGameRules.containsKey(name);
    }
    
    public boolean areSameType(final String key, final ValueType otherValue) {
        final Value gamerules$value = this.theGameRules.get(key);
        return gamerules$value != null && (gamerules$value.getType() == otherValue || otherValue == ValueType.ANY_VALUE);
    }
    
    public enum ValueType
    {
        ANY_VALUE("ANY_VALUE", 0, "ANY_VALUE", 0), 
        BOOLEAN_VALUE("BOOLEAN_VALUE", 1, "BOOLEAN_VALUE", 1), 
        NUMERICAL_VALUE("NUMERICAL_VALUE", 2, "NUMERICAL_VALUE", 2);
        
        private static final ValueType[] $VALUES;
        private static final String __OBFID = "CL_00002151";
        
        static {
            $VALUES = new ValueType[] { ValueType.ANY_VALUE, ValueType.BOOLEAN_VALUE, ValueType.NUMERICAL_VALUE };
        }
        
        private ValueType(final String name, final int ordinal, final String p_i19_3_, final int p_i19_4_) {
        }
    }
    
    static class Value
    {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final ValueType type;
        private static final String __OBFID = "CL_00000137";
        
        public Value(final String value, final ValueType type) {
            this.type = type;
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.valueString = value;
            if (value != null) {
                if (value.equals("false")) {
                    this.valueBoolean = false;
                    return;
                }
                if (value.equals("true")) {
                    this.valueBoolean = true;
                    return;
                }
            }
            this.valueBoolean = Boolean.parseBoolean(value);
            this.valueInteger = (this.valueBoolean ? 1 : 0);
            try {
                this.valueInteger = Integer.parseInt(value);
            }
            catch (NumberFormatException ex) {}
            try {
                this.valueDouble = Double.parseDouble(value);
            }
            catch (NumberFormatException ex2) {}
        }
        
        public String getString() {
            return this.valueString;
        }
        
        public boolean getBoolean() {
            return this.valueBoolean;
        }
        
        public int getInt() {
            return this.valueInteger;
        }
        
        public ValueType getType() {
            return this.type;
        }
    }
}
