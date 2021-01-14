package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
    private TreeMap theGameRules = new TreeMap();
    private static final String __OBFID = "CL_00000136";

    public GameRules() {
        addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
    }

    public void addGameRule(String key, String value, GameRules.ValueType type) {
        theGameRules.put(key, new GameRules.Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue) {
        GameRules.Value var3 = (GameRules.Value) theGameRules.get(key);

        if (var3 != null) {
            var3.setValue(ruleValue);
        } else {
            addGameRule(key, ruleValue, GameRules.ValueType.ANY_VALUE);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getGameRuleStringValue(String name) {
        GameRules.Value var2 = (GameRules.Value) theGameRules.get(name);
        return var2 != null ? var2.getGameRuleStringValue() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getGameRuleBooleanValue(String name) {
        GameRules.Value var2 = (GameRules.Value) theGameRules.get(name);
        return var2 != null ? var2.getGameRuleBooleanValue() : false;
    }

    public int getInt(String name) {
        GameRules.Value var2 = (GameRules.Value) theGameRules.get(name);
        return var2 != null ? var2.getInt() : 0;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeGameRulesToNBT() {
        NBTTagCompound var1 = new NBTTagCompound();
        Iterator var2 = theGameRules.keySet().iterator();

        while (var2.hasNext()) {
            String var3 = (String) var2.next();
            GameRules.Value var4 = (GameRules.Value) theGameRules.get(var3);
            var1.setString(var3, var4.getGameRuleStringValue());
        }

        return var1;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readGameRulesFromNBT(NBTTagCompound nbt) {
        Set var2 = nbt.getKeySet();
        Iterator var3 = var2.iterator();

        while (var3.hasNext()) {
            String var4 = (String) var3.next();
            String var6 = nbt.getString(var4);
            setOrCreateGameRule(var4, var6);
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules() {
        return ((String[]) theGameRules.keySet().toArray(new String[0]));
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String name) {
        return theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, GameRules.ValueType otherValue) {
        GameRules.Value var3 = (GameRules.Value) theGameRules.get(key);
        return var3 != null && (var3.getType() == otherValue || otherValue == GameRules.ValueType.ANY_VALUE);
    }

    static class Value {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final GameRules.ValueType type;
        private static final String __OBFID = "CL_00000137";

        public Value(String value, GameRules.ValueType type) {
            this.type = type;
            setValue(value);
        }

        public void setValue(String value) {
            valueString = value;

            if (value != null) {
                if (value.equals("false")) {
                    valueBoolean = false;
                    return;
                }

                if (value.equals("true")) {
                    valueBoolean = true;
                    return;
                }
            }

            valueBoolean = Boolean.parseBoolean(value);
            valueInteger = valueBoolean ? 1 : 0;

            try {
                valueInteger = Integer.parseInt(value);
            } catch (NumberFormatException var4) {
                ;
            }

            try {
                valueDouble = Double.parseDouble(value);
            } catch (NumberFormatException var3) {
                ;
            }
        }

        public String getGameRuleStringValue() {
            return valueString;
        }

        public boolean getGameRuleBooleanValue() {
            return valueBoolean;
        }

        public int getInt() {
            return valueInteger;
        }

        public GameRules.ValueType getType() {
            return type;
        }
    }

    public static enum ValueType {
        ANY_VALUE("ANY_VALUE", 0, "ANY_VALUE", 0), BOOLEAN_VALUE("BOOLEAN_VALUE", 1, "BOOLEAN_VALUE", 1), NUMERICAL_VALUE("NUMERICAL_VALUE", 2, "NUMERICAL_VALUE", 2);
        private static final GameRules.ValueType[] $VALUES = new GameRules.ValueType[]{ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE};
        private static final String __OBFID = "CL_00002151";

        private static final GameRules.ValueType[] $VALUES$ = new GameRules.ValueType[]{ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE};

        private ValueType(String p_i46375_1_, int p_i46375_2_, String p_i45750_1_, int p_i45750_2_) {
        }
    }
}
