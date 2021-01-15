/*     */ package net.minecraft.event;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ClickEvent
/*     */ {
/*     */   private final Action action;
/*     */   private final String value;
/*     */   
/*     */   public ClickEvent(Action theAction, String theValue)
/*     */   {
/*  13 */     this.action = theAction;
/*  14 */     this.value = theValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Action getAction()
/*     */   {
/*  22 */     return this.action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  31 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  36 */     if (this == p_equals_1_)
/*     */     {
/*  38 */       return true;
/*     */     }
/*  40 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */     {
/*  42 */       ClickEvent clickevent = (ClickEvent)p_equals_1_;
/*     */       
/*  44 */       if (this.action != clickevent.action)
/*     */       {
/*  46 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  50 */       if (this.value != null)
/*     */       {
/*  52 */         if (!this.value.equals(clickevent.value))
/*     */         {
/*  54 */           return false;
/*     */         }
/*     */       }
/*  57 */       else if (clickevent.value != null)
/*     */       {
/*  59 */         return false;
/*     */       }
/*     */       
/*  62 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  73 */     return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  78 */     int i = this.action.hashCode();
/*  79 */     i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
/*  80 */     return i;
/*     */   }
/*     */   
/*     */   public static enum Action
/*     */   {
/*  85 */     OPEN_URL("open_url", true), 
/*  86 */     OPEN_FILE("open_file", false), 
/*  87 */     RUN_COMMAND("run_command", true), 
/*  88 */     TWITCH_USER_INFO("twitch_user_info", false), 
/*  89 */     SUGGEST_COMMAND("suggest_command", true), 
/*  90 */     CHANGE_PAGE("change_page", true);
/*     */     
/*     */     private static final Map<String, Action> nameMapping;
/*     */     private final boolean allowedInChat;
/*     */     private final String canonicalName;
/*     */     
/*     */     private Action(String canonicalNameIn, boolean allowedInChatIn)
/*     */     {
/*  98 */       this.canonicalName = canonicalNameIn;
/*  99 */       this.allowedInChat = allowedInChatIn;
/*     */     }
/*     */     
/*     */     public boolean shouldAllowInChat()
/*     */     {
/* 104 */       return this.allowedInChat;
/*     */     }
/*     */     
/*     */     public String getCanonicalName()
/*     */     {
/* 109 */       return this.canonicalName;
/*     */     }
/*     */     
/*     */     public static Action getValueByCanonicalName(String canonicalNameIn)
/*     */     {
/* 114 */       return (Action)nameMapping.get(canonicalNameIn);
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  92 */       nameMapping = Maps.newHashMap();
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
/*     */       Action[] arrayOfAction;
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
/* 118 */       int j = (arrayOfAction = values()).length; for (int i = 0; i < j; i++) { Action clickevent$action = arrayOfAction[i];
/*     */         
/* 120 */         nameMapping.put(clickevent$action.getCanonicalName(), clickevent$action);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\event\ClickEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */