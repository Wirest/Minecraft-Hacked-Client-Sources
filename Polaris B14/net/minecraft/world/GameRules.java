/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ public class GameRules
/*     */ {
/*   9 */   private TreeMap theGameRules = new TreeMap();
/*     */   private static final String __OBFID = "CL_00000136";
/*     */   
/*     */   public GameRules()
/*     */   {
/*  14 */     addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
/*  15 */     addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
/*  16 */     addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
/*  17 */     addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
/*  18 */     addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
/*  19 */     addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
/*  20 */     addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
/*  21 */     addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
/*  22 */     addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
/*  23 */     addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
/*  24 */     addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
/*  25 */     addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
/*  26 */     addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
/*  27 */     addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
/*  28 */     addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
/*     */   }
/*     */   
/*     */   public void addGameRule(String key, String value, ValueType type)
/*     */   {
/*  33 */     this.theGameRules.put(key, new Value(value, type));
/*     */   }
/*     */   
/*     */   public void setOrCreateGameRule(String key, String ruleValue)
/*     */   {
/*  38 */     Value gamerules$value = (Value)this.theGameRules.get(key);
/*     */     
/*  40 */     if (gamerules$value != null)
/*     */     {
/*  42 */       gamerules$value.setValue(ruleValue);
/*     */     }
/*     */     else
/*     */     {
/*  46 */       addGameRule(key, ruleValue, ValueType.ANY_VALUE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString(String name)
/*     */   {
/*  55 */     Value gamerules$value = (Value)this.theGameRules.get(name);
/*  56 */     return gamerules$value != null ? gamerules$value.getString() : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBoolean(String name)
/*     */   {
/*  64 */     Value gamerules$value = (Value)this.theGameRules.get(name);
/*  65 */     return gamerules$value != null ? gamerules$value.getBoolean() : false;
/*     */   }
/*     */   
/*     */   public int getInt(String name)
/*     */   {
/*  70 */     Value gamerules$value = (Value)this.theGameRules.get(name);
/*  71 */     return gamerules$value != null ? gamerules$value.getInt() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound writeToNBT()
/*     */   {
/*  79 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/*  81 */     for (Object s : this.theGameRules.keySet())
/*     */     {
/*  83 */       Value gamerules$value = (Value)this.theGameRules.get(s);
/*  84 */       nbttagcompound.setString((String)s, gamerules$value.getString());
/*     */     }
/*     */     
/*  87 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound nbt)
/*     */   {
/*  95 */     for (String s : nbt.getKeySet())
/*     */     {
/*  97 */       String s1 = nbt.getString(s);
/*  98 */       setOrCreateGameRule(s, s1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRules()
/*     */   {
/* 107 */     Set set = this.theGameRules.keySet();
/* 108 */     return (String[])set.toArray(new String[set.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasRule(String name)
/*     */   {
/* 116 */     return this.theGameRules.containsKey(name);
/*     */   }
/*     */   
/*     */   public boolean areSameType(String key, ValueType otherValue)
/*     */   {
/* 121 */     Value gamerules$value = (Value)this.theGameRules.get(key);
/* 122 */     return (gamerules$value != null) && ((gamerules$value.getType() == otherValue) || (otherValue == ValueType.ANY_VALUE));
/*     */   }
/*     */   
/*     */   static class Value
/*     */   {
/*     */     private String valueString;
/*     */     private boolean valueBoolean;
/*     */     private int valueInteger;
/*     */     private double valueDouble;
/*     */     private final GameRules.ValueType type;
/*     */     private static final String __OBFID = "CL_00000137";
/*     */     
/*     */     public Value(String value, GameRules.ValueType type)
/*     */     {
/* 136 */       this.type = type;
/* 137 */       setValue(value);
/*     */     }
/*     */     
/*     */     public void setValue(String value)
/*     */     {
/* 142 */       this.valueString = value;
/*     */       
/* 144 */       if (value != null)
/*     */       {
/* 146 */         if (value.equals("false"))
/*     */         {
/* 148 */           this.valueBoolean = false;
/* 149 */           return;
/*     */         }
/*     */         
/* 152 */         if (value.equals("true"))
/*     */         {
/* 154 */           this.valueBoolean = true;
/* 155 */           return;
/*     */         }
/*     */       }
/*     */       
/* 159 */       this.valueBoolean = Boolean.parseBoolean(value);
/* 160 */       this.valueInteger = (this.valueBoolean ? 1 : 0);
/*     */       
/*     */       try
/*     */       {
/* 164 */         this.valueInteger = Integer.parseInt(value);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 173 */         this.valueDouble = Double.parseDouble(value);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException1) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getString()
/*     */     {
/* 183 */       return this.valueString;
/*     */     }
/*     */     
/*     */     public boolean getBoolean()
/*     */     {
/* 188 */       return this.valueBoolean;
/*     */     }
/*     */     
/*     */     public int getInt()
/*     */     {
/* 193 */       return this.valueInteger;
/*     */     }
/*     */     
/*     */     public GameRules.ValueType getType()
/*     */     {
/* 198 */       return this.type;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum ValueType
/*     */   {
/* 204 */     ANY_VALUE("ANY_VALUE", 0), 
/* 205 */     BOOLEAN_VALUE("BOOLEAN_VALUE", 1), 
/* 206 */     NUMERICAL_VALUE("NUMERICAL_VALUE", 2);
/*     */     
/* 208 */     private static final ValueType[] $VALUES = { ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE };
/*     */     private static final String __OBFID = "CL_00002151";
/*     */     
/*     */     private ValueType(String p_i15_3_, int p_i15_4_) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\GameRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */