/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
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
/*     */ public class Level
/*     */   extends Priority
/*     */   implements Serializable
/*     */ {
/*     */   public static final int TRACE_INT = 5000;
/*     */   public static final int X_TRACE_INT = 9900;
/*  54 */   public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF", 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final Level FATAL = new Level(50000, "FATAL", 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   public static final Level ERROR = new Level(40000, "ERROR", 3);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   public static final Level WARN = new Level(30000, "WARN", 4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   public static final Level INFO = new Level(20000, "INFO", 6);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final Level DEBUG = new Level(10000, "DEBUG", 7);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   public static final Level TRACE = new Level(5000, "TRACE", 7);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL", 7);
/*     */   
/*     */ 
/*     */ 
/*     */   static final long serialVersionUID = 3491141966387921974L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Level(int level, String levelStr, int syslogEquivalent)
/*     */   {
/* 105 */     super(level, levelStr, syslogEquivalent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(String sArg)
/*     */   {
/* 113 */     return toLevel(sArg, DEBUG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(int val)
/*     */   {
/* 122 */     return toLevel(val, DEBUG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(int val, Level defaultLevel)
/*     */   {
/* 130 */     switch (val) {
/*     */     case -2147483648: 
/* 132 */       return ALL;
/*     */     case 10000: 
/* 134 */       return DEBUG;
/*     */     case 20000: 
/* 136 */       return INFO;
/*     */     case 30000: 
/* 138 */       return WARN;
/*     */     case 40000: 
/* 140 */       return ERROR;
/*     */     case 50000: 
/* 142 */       return FATAL;
/*     */     case 2147483647: 
/* 144 */       return OFF;
/*     */     case 5000: 
/* 146 */       return TRACE;
/*     */     }
/* 148 */     return defaultLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(String sArg, Level defaultLevel)
/*     */   {
/* 158 */     if (sArg == null) {
/* 159 */       return defaultLevel;
/*     */     }
/* 161 */     String s = sArg.toUpperCase();
/*     */     
/* 163 */     if (s.equals("ALL"))
/* 164 */       return ALL;
/* 165 */     if (s.equals("DEBUG"))
/* 166 */       return DEBUG;
/* 167 */     if (s.equals("INFO"))
/* 168 */       return INFO;
/* 169 */     if (s.equals("WARN"))
/* 170 */       return WARN;
/* 171 */     if (s.equals("ERROR"))
/* 172 */       return ERROR;
/* 173 */     if (s.equals("FATAL"))
/* 174 */       return FATAL;
/* 175 */     if (s.equals("OFF"))
/* 176 */       return OFF;
/* 177 */     if (s.equals("TRACE"))
/* 178 */       return TRACE;
/* 179 */     return defaultLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream s)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 189 */     s.defaultReadObject();
/* 190 */     this.level = s.readInt();
/* 191 */     this.syslogEquivalent = s.readInt();
/* 192 */     this.levelStr = s.readUTF();
/* 193 */     if (this.levelStr == null) {
/* 194 */       this.levelStr = "";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream s)
/*     */     throws IOException
/*     */   {
/* 204 */     s.defaultWriteObject();
/* 205 */     s.writeInt(this.level);
/* 206 */     s.writeInt(this.syslogEquivalent);
/* 207 */     s.writeUTF(this.levelStr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 220 */     if (getClass() == Level.class) {
/* 221 */       return toLevel(this.level);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 226 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\Level.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */