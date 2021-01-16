package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules
{
    private TreeMap theGameRules = new TreeMap();

    public GameRules()
    {
        this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
    }

    public void addGameRule(String key, String value, GameRules.ValueType type)
    {
        this.theGameRules.put(key, new GameRules.Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue)
    {
        GameRules.Value var3 = (GameRules.Value)this.theGameRules.get(key);

        if (var3 != null)
        {
            var3.setValue(ruleValue);
        }
        else
        {
            this.addGameRule(key, ruleValue, GameRules.ValueType.ANY_VALUE);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getGameRuleStringValue(String name)
    {
        GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(name);
        return var2 != null ? var2.getGameRuleStringValue() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getGameRuleBooleanValue(String name)
    {
        GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(name);
        return var2 != null ? var2.getGameRuleBooleanValue() : false;
    }

    public int getInt(String name)
    {
        GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(name);
        return var2 != null ? var2.getInt() : 0;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeGameRulesToNBT()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        Iterator var2 = this.theGameRules.keySet().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            GameRules.Value var4 = (GameRules.Value)this.theGameRules.get(var3);
            var1.setString(var3, var4.getGameRuleStringValue());
        }

        return var1;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readGameRulesFromNBT(NBTTagCompound nbt)
    {
        Set var2 = nbt.getKeySet();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            String var6 = nbt.getString(var4);
            this.setOrCreateGameRule(var4, var6);
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules()
    {
        return (String[])((String[])this.theGameRules.keySet().toArray(new String[0]));
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String name)
    {
        return this.theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, GameRules.ValueType otherValue)
    {
        GameRules.Value var3 = (GameRules.Value)this.theGameRules.get(key);
        return var3 != null && (var3.getType() == otherValue || otherValue == GameRules.ValueType.ANY_VALUE);
    }

    static class Value
    {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final GameRules.ValueType type;
        private static final String __OBFID = "CL_00000137";

        public Value(String value, GameRules.ValueType type)
        {
            this.type = type;
            this.setValue(value);
        }

        public void setValue(String value)
        {
            this.valueString = value;

            if (value != null)
            {
                if (value.equals("false"))
                {
                    this.valueBoolean = false;
                    return;
                }

                if (value.equals("true"))
                {
                    this.valueBoolean = true;
                    return;
                }
            }

            this.valueBoolean = Boolean.parseBoolean(value);
            this.valueInteger = this.valueBoolean ? 1 : 0;

            try
            {
                this.valueInteger = Integer.parseInt(value);
            }
            catch (NumberFormatException var4)
            {
                ;
            }

            try
            {
                this.valueDouble = Double.parseDouble(value);
            }
            catch (NumberFormatException var3)
            {
                ;
            }
        }

        public String getGameRuleStringValue()
        {
            return this.valueString;
        }

        public boolean getGameRuleBooleanValue()
        {
            return this.valueBoolean;
        }

        public int getInt()
        {
            return this.valueInteger;
        }

        public GameRules.ValueType getType()
        {
            return this.type;
        }
    }

    public static enum ValueType
    {
        ANY_VALUE("ANY_VALUE", 0, "ANY_VALUE", 0),
        BOOLEAN_VALUE("BOOLEAN_VALUE", 1, "BOOLEAN_VALUE", 1),
        NUMERICAL_VALUE("NUMERICAL_VALUE", 2, "NUMERICAL_VALUE", 2);
        private static final GameRules.ValueType[] $VALUES = new GameRules.ValueType[]{ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE};
        private ValueType(String p_i46394_1_, int p_i46394_2_, String p_i45750_1_, int p_i45750_2_) {}
    }
}
