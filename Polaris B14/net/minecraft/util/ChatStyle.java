/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.event.HoverEvent.Action;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChatStyle
/*     */ {
/*     */   private ChatStyle parentStyle;
/*     */   private EnumChatFormatting color;
/*     */   private Boolean bold;
/*     */   private Boolean italic;
/*     */   private Boolean underlined;
/*     */   private Boolean strikethrough;
/*     */   private Boolean obfuscated;
/*     */   private ClickEvent chatClickEvent;
/*     */   private HoverEvent chatHoverEvent;
/*     */   private String insertion;
/*  34 */   private static final ChatStyle rootStyle = new ChatStyle()
/*     */   {
/*     */     public EnumChatFormatting getColor()
/*     */     {
/*  38 */       return null;
/*     */     }
/*     */     
/*     */     public boolean getBold() {
/*  42 */       return false;
/*     */     }
/*     */     
/*     */     public boolean getItalic() {
/*  46 */       return false;
/*     */     }
/*     */     
/*     */     public boolean getStrikethrough() {
/*  50 */       return false;
/*     */     }
/*     */     
/*     */     public boolean getUnderlined() {
/*  54 */       return false;
/*     */     }
/*     */     
/*     */     public boolean getObfuscated() {
/*  58 */       return false;
/*     */     }
/*     */     
/*     */     public ClickEvent getChatClickEvent() {
/*  62 */       return null;
/*     */     }
/*     */     
/*     */     public HoverEvent getChatHoverEvent() {
/*  66 */       return null;
/*     */     }
/*     */     
/*     */     public String getInsertion() {
/*  70 */       return null;
/*     */     }
/*     */     
/*     */     public ChatStyle setColor(EnumChatFormatting color) {
/*  74 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setBold(Boolean boldIn) {
/*  78 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setItalic(Boolean italic) {
/*  82 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setStrikethrough(Boolean strikethrough) {
/*  86 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setUnderlined(Boolean underlined) {
/*  90 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setObfuscated(Boolean obfuscated) {
/*  94 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setChatClickEvent(ClickEvent event) {
/*  98 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setChatHoverEvent(HoverEvent event) {
/* 102 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ChatStyle setParentStyle(ChatStyle parent) {
/* 106 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 110 */       return "Style.ROOT";
/*     */     }
/*     */     
/*     */     public ChatStyle createShallowCopy() {
/* 114 */       return this;
/*     */     }
/*     */     
/*     */     public ChatStyle createDeepCopy() {
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public String getFormattingCode() {
/* 122 */       return "";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumChatFormatting getColor()
/*     */   {
/* 131 */     return this.color == null ? getParent().getColor() : this.color;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBold()
/*     */   {
/* 139 */     return this.bold == null ? getParent().getBold() : this.bold.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getItalic()
/*     */   {
/* 147 */     return this.italic == null ? getParent().getItalic() : this.italic.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getStrikethrough()
/*     */   {
/* 155 */     return this.strikethrough == null ? getParent().getStrikethrough() : this.strikethrough.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUnderlined()
/*     */   {
/* 163 */     return this.underlined == null ? getParent().getUnderlined() : this.underlined.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getObfuscated()
/*     */   {
/* 171 */     return this.obfuscated == null ? getParent().getObfuscated() : this.obfuscated.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 179 */     return (this.bold == null) && (this.italic == null) && (this.strikethrough == null) && (this.underlined == null) && (this.obfuscated == null) && (this.color == null) && (this.chatClickEvent == null) && (this.chatHoverEvent == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClickEvent getChatClickEvent()
/*     */   {
/* 187 */     return this.chatClickEvent == null ? getParent().getChatClickEvent() : this.chatClickEvent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HoverEvent getChatHoverEvent()
/*     */   {
/* 195 */     return this.chatHoverEvent == null ? getParent().getChatHoverEvent() : this.chatHoverEvent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInsertion()
/*     */   {
/* 203 */     return this.insertion == null ? getParent().getInsertion() : this.insertion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setColor(EnumChatFormatting color)
/*     */   {
/* 212 */     this.color = color;
/* 213 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setBold(Boolean boldIn)
/*     */   {
/* 222 */     this.bold = boldIn;
/* 223 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setItalic(Boolean italic)
/*     */   {
/* 232 */     this.italic = italic;
/* 233 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setStrikethrough(Boolean strikethrough)
/*     */   {
/* 242 */     this.strikethrough = strikethrough;
/* 243 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setUnderlined(Boolean underlined)
/*     */   {
/* 252 */     this.underlined = underlined;
/* 253 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setObfuscated(Boolean obfuscated)
/*     */   {
/* 262 */     this.obfuscated = obfuscated;
/* 263 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setChatClickEvent(ClickEvent event)
/*     */   {
/* 271 */     this.chatClickEvent = event;
/* 272 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setChatHoverEvent(HoverEvent event)
/*     */   {
/* 280 */     this.chatHoverEvent = event;
/* 281 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setInsertion(String insertion)
/*     */   {
/* 289 */     this.insertion = insertion;
/* 290 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle setParentStyle(ChatStyle parent)
/*     */   {
/* 299 */     this.parentStyle = parent;
/* 300 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFormattingCode()
/*     */   {
/* 308 */     if (isEmpty())
/*     */     {
/* 310 */       return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
/*     */     }
/*     */     
/*     */ 
/* 314 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 316 */     if (getColor() != null)
/*     */     {
/* 318 */       stringbuilder.append(getColor());
/*     */     }
/*     */     
/* 321 */     if (getBold())
/*     */     {
/* 323 */       stringbuilder.append(EnumChatFormatting.BOLD);
/*     */     }
/*     */     
/* 326 */     if (getItalic())
/*     */     {
/* 328 */       stringbuilder.append(EnumChatFormatting.ITALIC);
/*     */     }
/*     */     
/* 331 */     if (getUnderlined())
/*     */     {
/* 333 */       stringbuilder.append(EnumChatFormatting.UNDERLINE);
/*     */     }
/*     */     
/* 336 */     if (getObfuscated())
/*     */     {
/* 338 */       stringbuilder.append(EnumChatFormatting.OBFUSCATED);
/*     */     }
/*     */     
/* 341 */     if (getStrikethrough())
/*     */     {
/* 343 */       stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);
/*     */     }
/*     */     
/* 346 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChatStyle getParent()
/*     */   {
/* 355 */     return this.parentStyle == null ? rootStyle : this.parentStyle;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 360 */     return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + getChatClickEvent() + ", hoverEvent=" + getChatHoverEvent() + ", insertion=" + getInsertion() + '}';
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 365 */     if (this == p_equals_1_)
/*     */     {
/* 367 */       return true;
/*     */     }
/* 369 */     if (!(p_equals_1_ instanceof ChatStyle))
/*     */     {
/* 371 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 378 */     ChatStyle chatstyle = (ChatStyle)p_equals_1_;
/*     */     
/* 380 */     if ((getBold() == chatstyle.getBold()) && (getColor() == chatstyle.getColor()) && (getItalic() == chatstyle.getItalic()) && (getObfuscated() == chatstyle.getObfuscated()) && (getStrikethrough() == chatstyle.getStrikethrough()) && (getUnderlined() == chatstyle.getUnderlined()))
/*     */     {
/*     */ 
/*     */ 
/* 384 */       if (getChatClickEvent() != null ? 
/*     */       
/* 386 */         getChatClickEvent().equals(chatstyle.getChatClickEvent()) : 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 391 */         chatstyle.getChatClickEvent() == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 396 */         if (getChatHoverEvent() != null ? 
/*     */         
/* 398 */           getChatHoverEvent().equals(chatstyle.getChatHoverEvent()) : 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 403 */           chatstyle.getChatHoverEvent() == null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 408 */           if (getInsertion() != null ? 
/*     */           
/* 410 */             getInsertion().equals(chatstyle.getInsertion()) : 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 415 */             chatstyle.getInsertion() == null) {
/*     */             break label193;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 422 */     boolean flag = false;
/* 423 */     return flag;
/*     */     label193:
/* 425 */     boolean flag = true;
/* 426 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 432 */     int i = this.color.hashCode();
/* 433 */     i = 31 * i + this.bold.hashCode();
/* 434 */     i = 31 * i + this.italic.hashCode();
/* 435 */     i = 31 * i + this.underlined.hashCode();
/* 436 */     i = 31 * i + this.strikethrough.hashCode();
/* 437 */     i = 31 * i + this.obfuscated.hashCode();
/* 438 */     i = 31 * i + this.chatClickEvent.hashCode();
/* 439 */     i = 31 * i + this.chatHoverEvent.hashCode();
/* 440 */     i = 31 * i + this.insertion.hashCode();
/* 441 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle createShallowCopy()
/*     */   {
/* 451 */     ChatStyle chatstyle = new ChatStyle();
/* 452 */     chatstyle.bold = this.bold;
/* 453 */     chatstyle.italic = this.italic;
/* 454 */     chatstyle.strikethrough = this.strikethrough;
/* 455 */     chatstyle.underlined = this.underlined;
/* 456 */     chatstyle.obfuscated = this.obfuscated;
/* 457 */     chatstyle.color = this.color;
/* 458 */     chatstyle.chatClickEvent = this.chatClickEvent;
/* 459 */     chatstyle.chatHoverEvent = this.chatHoverEvent;
/* 460 */     chatstyle.parentStyle = this.parentStyle;
/* 461 */     chatstyle.insertion = this.insertion;
/* 462 */     return chatstyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatStyle createDeepCopy()
/*     */   {
/* 471 */     ChatStyle chatstyle = new ChatStyle();
/* 472 */     chatstyle.setBold(Boolean.valueOf(getBold()));
/* 473 */     chatstyle.setItalic(Boolean.valueOf(getItalic()));
/* 474 */     chatstyle.setStrikethrough(Boolean.valueOf(getStrikethrough()));
/* 475 */     chatstyle.setUnderlined(Boolean.valueOf(getUnderlined()));
/* 476 */     chatstyle.setObfuscated(Boolean.valueOf(getObfuscated()));
/* 477 */     chatstyle.setColor(getColor());
/* 478 */     chatstyle.setChatClickEvent(getChatClickEvent());
/* 479 */     chatstyle.setChatHoverEvent(getChatHoverEvent());
/* 480 */     chatstyle.setInsertion(getInsertion());
/* 481 */     return chatstyle;
/*     */   }
/*     */   
/*     */   public static class Serializer implements JsonDeserializer<ChatStyle>, JsonSerializer<ChatStyle>
/*     */   {
/*     */     public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 488 */       if (p_deserialize_1_.isJsonObject())
/*     */       {
/* 490 */         ChatStyle chatstyle = new ChatStyle();
/* 491 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*     */         
/* 493 */         if (jsonobject == null)
/*     */         {
/* 495 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 499 */         if (jsonobject.has("bold"))
/*     */         {
/* 501 */           chatstyle.bold = Boolean.valueOf(jsonobject.get("bold").getAsBoolean());
/*     */         }
/*     */         
/* 504 */         if (jsonobject.has("italic"))
/*     */         {
/* 506 */           chatstyle.italic = Boolean.valueOf(jsonobject.get("italic").getAsBoolean());
/*     */         }
/*     */         
/* 509 */         if (jsonobject.has("underlined"))
/*     */         {
/* 511 */           chatstyle.underlined = Boolean.valueOf(jsonobject.get("underlined").getAsBoolean());
/*     */         }
/*     */         
/* 514 */         if (jsonobject.has("strikethrough"))
/*     */         {
/* 516 */           chatstyle.strikethrough = Boolean.valueOf(jsonobject.get("strikethrough").getAsBoolean());
/*     */         }
/*     */         
/* 519 */         if (jsonobject.has("obfuscated"))
/*     */         {
/* 521 */           chatstyle.obfuscated = Boolean.valueOf(jsonobject.get("obfuscated").getAsBoolean());
/*     */         }
/*     */         
/* 524 */         if (jsonobject.has("color"))
/*     */         {
/* 526 */           chatstyle.color = ((EnumChatFormatting)p_deserialize_3_.deserialize(jsonobject.get("color"), EnumChatFormatting.class));
/*     */         }
/*     */         
/* 529 */         if (jsonobject.has("insertion"))
/*     */         {
/* 531 */           chatstyle.insertion = jsonobject.get("insertion").getAsString();
/*     */         }
/*     */         
/* 534 */         if (jsonobject.has("clickEvent"))
/*     */         {
/* 536 */           JsonObject jsonobject1 = jsonobject.getAsJsonObject("clickEvent");
/*     */           
/* 538 */           if (jsonobject1 != null)
/*     */           {
/* 540 */             JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
/* 541 */             ClickEvent.Action clickevent$action = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
/* 542 */             JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
/* 543 */             String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();
/*     */             
/* 545 */             if ((clickevent$action != null) && (s != null) && (clickevent$action.shouldAllowInChat()))
/*     */             {
/* 547 */               chatstyle.chatClickEvent = new ClickEvent(clickevent$action, s);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 552 */         if (jsonobject.has("hoverEvent"))
/*     */         {
/* 554 */           JsonObject jsonobject2 = jsonobject.getAsJsonObject("hoverEvent");
/*     */           
/* 556 */           if (jsonobject2 != null)
/*     */           {
/* 558 */             JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
/* 559 */             HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive2.getAsString());
/* 560 */             IChatComponent ichatcomponent = (IChatComponent)p_deserialize_3_.deserialize(jsonobject2.get("value"), IChatComponent.class);
/*     */             
/* 562 */             if ((hoverevent$action != null) && (ichatcomponent != null) && (hoverevent$action.shouldAllowInChat()))
/*     */             {
/* 564 */               chatstyle.chatHoverEvent = new HoverEvent(hoverevent$action, ichatcomponent);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 569 */         return chatstyle;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 574 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 580 */       if (p_serialize_1_.isEmpty())
/*     */       {
/* 582 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 586 */       JsonObject jsonobject = new JsonObject();
/*     */       
/* 588 */       if (p_serialize_1_.bold != null)
/*     */       {
/* 590 */         jsonobject.addProperty("bold", p_serialize_1_.bold);
/*     */       }
/*     */       
/* 593 */       if (p_serialize_1_.italic != null)
/*     */       {
/* 595 */         jsonobject.addProperty("italic", p_serialize_1_.italic);
/*     */       }
/*     */       
/* 598 */       if (p_serialize_1_.underlined != null)
/*     */       {
/* 600 */         jsonobject.addProperty("underlined", p_serialize_1_.underlined);
/*     */       }
/*     */       
/* 603 */       if (p_serialize_1_.strikethrough != null)
/*     */       {
/* 605 */         jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
/*     */       }
/*     */       
/* 608 */       if (p_serialize_1_.obfuscated != null)
/*     */       {
/* 610 */         jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
/*     */       }
/*     */       
/* 613 */       if (p_serialize_1_.color != null)
/*     */       {
/* 615 */         jsonobject.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
/*     */       }
/*     */       
/* 618 */       if (p_serialize_1_.insertion != null)
/*     */       {
/* 620 */         jsonobject.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
/*     */       }
/*     */       
/* 623 */       if (p_serialize_1_.chatClickEvent != null)
/*     */       {
/* 625 */         JsonObject jsonobject1 = new JsonObject();
/* 626 */         jsonobject1.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
/* 627 */         jsonobject1.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
/* 628 */         jsonobject.add("clickEvent", jsonobject1);
/*     */       }
/*     */       
/* 631 */       if (p_serialize_1_.chatHoverEvent != null)
/*     */       {
/* 633 */         JsonObject jsonobject2 = new JsonObject();
/* 634 */         jsonobject2.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
/* 635 */         jsonobject2.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
/* 636 */         jsonobject.add("hoverEvent", jsonobject2);
/*     */       }
/*     */       
/* 639 */       return jsonobject;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */