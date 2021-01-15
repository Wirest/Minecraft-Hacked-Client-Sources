/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSpdySettingsFrame
/*     */   implements SpdySettingsFrame
/*     */ {
/*     */   private boolean clear;
/*     */   private final Map<Integer, Setting> settingsMap;
/*     */   
/*     */   public DefaultSpdySettingsFrame()
/*     */   {
/*  30 */     this.settingsMap = new TreeMap();
/*     */   }
/*     */   
/*     */   public Set<Integer> ids() {
/*  34 */     return this.settingsMap.keySet();
/*     */   }
/*     */   
/*     */   public boolean isSet(int id)
/*     */   {
/*  39 */     Integer key = Integer.valueOf(id);
/*  40 */     return this.settingsMap.containsKey(key);
/*     */   }
/*     */   
/*     */   public int getValue(int id)
/*     */   {
/*  45 */     Integer key = Integer.valueOf(id);
/*  46 */     if (this.settingsMap.containsKey(key)) {
/*  47 */       return ((Setting)this.settingsMap.get(key)).getValue();
/*     */     }
/*  49 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public SpdySettingsFrame setValue(int id, int value)
/*     */   {
/*  55 */     return setValue(id, value, false, false);
/*     */   }
/*     */   
/*     */   public SpdySettingsFrame setValue(int id, int value, boolean persistValue, boolean persisted)
/*     */   {
/*  60 */     if ((id < 0) || (id > 16777215)) {
/*  61 */       throw new IllegalArgumentException("Setting ID is not valid: " + id);
/*     */     }
/*  63 */     Integer key = Integer.valueOf(id);
/*  64 */     if (this.settingsMap.containsKey(key)) {
/*  65 */       Setting setting = (Setting)this.settingsMap.get(key);
/*  66 */       setting.setValue(value);
/*  67 */       setting.setPersist(persistValue);
/*  68 */       setting.setPersisted(persisted);
/*     */     } else {
/*  70 */       this.settingsMap.put(key, new Setting(value, persistValue, persisted));
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public SpdySettingsFrame removeValue(int id)
/*     */   {
/*  77 */     Integer key = Integer.valueOf(id);
/*  78 */     if (this.settingsMap.containsKey(key)) {
/*  79 */       this.settingsMap.remove(key);
/*     */     }
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isPersistValue(int id)
/*     */   {
/*  86 */     Integer key = Integer.valueOf(id);
/*  87 */     if (this.settingsMap.containsKey(key)) {
/*  88 */       return ((Setting)this.settingsMap.get(key)).isPersist();
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public SpdySettingsFrame setPersistValue(int id, boolean persistValue)
/*     */   {
/*  96 */     Integer key = Integer.valueOf(id);
/*  97 */     if (this.settingsMap.containsKey(key)) {
/*  98 */       ((Setting)this.settingsMap.get(key)).setPersist(persistValue);
/*     */     }
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isPersisted(int id)
/*     */   {
/* 105 */     Integer key = Integer.valueOf(id);
/* 106 */     if (this.settingsMap.containsKey(key)) {
/* 107 */       return ((Setting)this.settingsMap.get(key)).isPersisted();
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public SpdySettingsFrame setPersisted(int id, boolean persisted)
/*     */   {
/* 115 */     Integer key = Integer.valueOf(id);
/* 116 */     if (this.settingsMap.containsKey(key)) {
/* 117 */       ((Setting)this.settingsMap.get(key)).setPersisted(persisted);
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public boolean clearPreviouslyPersistedSettings()
/*     */   {
/* 124 */     return this.clear;
/*     */   }
/*     */   
/*     */   public SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean clear)
/*     */   {
/* 129 */     this.clear = clear;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   private Set<Map.Entry<Integer, Setting>> getSettings() {
/* 134 */     return this.settingsMap.entrySet();
/*     */   }
/*     */   
/*     */   private void appendSettings(StringBuilder buf) {
/* 138 */     for (Map.Entry<Integer, Setting> e : getSettings()) {
/* 139 */       Setting setting = (Setting)e.getValue();
/* 140 */       buf.append("--> ");
/* 141 */       buf.append(e.getKey());
/* 142 */       buf.append(':');
/* 143 */       buf.append(setting.getValue());
/* 144 */       buf.append(" (persist value: ");
/* 145 */       buf.append(setting.isPersist());
/* 146 */       buf.append("; persisted: ");
/* 147 */       buf.append(setting.isPersisted());
/* 148 */       buf.append(')');
/* 149 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 155 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append(StringUtil.NEWLINE);
/*     */     
/*     */ 
/* 158 */     appendSettings(buf);
/*     */     
/* 160 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 161 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static final class Setting
/*     */   {
/*     */     private int value;
/*     */     private boolean persist;
/*     */     private boolean persisted;
/*     */     
/*     */     Setting(int value, boolean persist, boolean persisted) {
/* 171 */       this.value = value;
/* 172 */       this.persist = persist;
/* 173 */       this.persisted = persisted;
/*     */     }
/*     */     
/*     */     int getValue() {
/* 177 */       return this.value;
/*     */     }
/*     */     
/*     */     void setValue(int value) {
/* 181 */       this.value = value;
/*     */     }
/*     */     
/*     */     boolean isPersist() {
/* 185 */       return this.persist;
/*     */     }
/*     */     
/*     */     void setPersist(boolean persist) {
/* 189 */       this.persist = persist;
/*     */     }
/*     */     
/*     */     boolean isPersisted() {
/* 193 */       return this.persisted;
/*     */     }
/*     */     
/*     */     void setPersisted(boolean persisted) {
/* 197 */       this.persisted = persisted;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdySettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */