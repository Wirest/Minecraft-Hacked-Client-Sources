/*     */ package net.minecraft.event;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class HoverEvent
/*     */ {
/*     */   private final Action action;
/*     */   private final IChatComponent value;
/*     */   
/*     */   public HoverEvent(Action actionIn, IChatComponent valueIn)
/*     */   {
/*  14 */     this.action = actionIn;
/*  15 */     this.value = valueIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Action getAction()
/*     */   {
/*  23 */     return this.action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getValue()
/*     */   {
/*  32 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  37 */     if (this == p_equals_1_)
/*     */     {
/*  39 */       return true;
/*     */     }
/*  41 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */     {
/*  43 */       HoverEvent hoverevent = (HoverEvent)p_equals_1_;
/*     */       
/*  45 */       if (this.action != hoverevent.action)
/*     */       {
/*  47 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  51 */       if (this.value != null)
/*     */       {
/*  53 */         if (!this.value.equals(hoverevent.value))
/*     */         {
/*  55 */           return false;
/*     */         }
/*     */       }
/*  58 */       else if (hoverevent.value != null)
/*     */       {
/*  60 */         return false;
/*     */       }
/*     */       
/*  63 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  68 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  74 */     return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  79 */     int i = this.action.hashCode();
/*  80 */     i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
/*  81 */     return i;
/*     */   }
/*     */   
/*     */   public static enum Action
/*     */   {
/*  86 */     SHOW_TEXT("show_text", true), 
/*  87 */     SHOW_ACHIEVEMENT("show_achievement", true), 
/*  88 */     SHOW_ITEM("show_item", true), 
/*  89 */     SHOW_ENTITY("show_entity", true);
/*     */     
/*     */     private static final Map<String, Action> nameMapping;
/*     */     private final boolean allowedInChat;
/*     */     private final String canonicalName;
/*     */     
/*     */     private Action(String canonicalNameIn, boolean allowedInChatIn)
/*     */     {
/*  97 */       this.canonicalName = canonicalNameIn;
/*  98 */       this.allowedInChat = allowedInChatIn;
/*     */     }
/*     */     
/*     */     public boolean shouldAllowInChat()
/*     */     {
/* 103 */       return this.allowedInChat;
/*     */     }
/*     */     
/*     */     public String getCanonicalName()
/*     */     {
/* 108 */       return this.canonicalName;
/*     */     }
/*     */     
/*     */     public static Action getValueByCanonicalName(String canonicalNameIn)
/*     */     {
/* 113 */       return (Action)nameMapping.get(canonicalNameIn);
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  91 */       nameMapping = Maps.newHashMap();
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
/* 117 */       int j = (arrayOfAction = values()).length; for (int i = 0; i < j; i++) { Action hoverevent$action = arrayOfAction[i];
/*     */         
/* 119 */         nameMapping.put(hoverevent$action.getCanonicalName(), hoverevent$action);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\event\HoverEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */