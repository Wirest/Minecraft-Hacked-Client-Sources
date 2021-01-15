/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class ChatComponentStyle implements IChatComponent
/*     */ {
/*  11 */   protected List<IChatComponent> siblings = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   private ChatStyle style;
/*     */   
/*     */ 
/*     */   public IChatComponent appendSibling(IChatComponent component)
/*     */   {
/*  19 */     component.getChatStyle().setParentStyle(getChatStyle());
/*  20 */     this.siblings.add(component);
/*  21 */     return this;
/*     */   }
/*     */   
/*     */   public List<IChatComponent> getSiblings()
/*     */   {
/*  26 */     return this.siblings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent appendText(String text)
/*     */   {
/*  34 */     return appendSibling(new ChatComponentText(text));
/*     */   }
/*     */   
/*     */   public IChatComponent setChatStyle(ChatStyle style)
/*     */   {
/*  39 */     this.style = style;
/*     */     
/*  41 */     for (IChatComponent ichatcomponent : this.siblings)
/*     */     {
/*  43 */       ichatcomponent.getChatStyle().setParentStyle(getChatStyle());
/*     */     }
/*     */     
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   public ChatStyle getChatStyle()
/*     */   {
/*  51 */     if (this.style == null)
/*     */     {
/*  53 */       this.style = new ChatStyle();
/*     */       
/*  55 */       for (IChatComponent ichatcomponent : this.siblings)
/*     */       {
/*  57 */         ichatcomponent.getChatStyle().setParentStyle(this.style);
/*     */       }
/*     */     }
/*     */     
/*  61 */     return this.style;
/*     */   }
/*     */   
/*     */   public Iterator<IChatComponent> iterator()
/*     */   {
/*  66 */     return Iterators.concat(Iterators.forArray(new ChatComponentStyle[] { this }), createDeepCopyIterator(this.siblings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getUnformattedText()
/*     */   {
/*  74 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  76 */     for (IChatComponent ichatcomponent : this)
/*     */     {
/*  78 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*     */     }
/*     */     
/*  81 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getFormattedText()
/*     */   {
/*  89 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  91 */     for (IChatComponent ichatcomponent : this)
/*     */     {
/*  93 */       stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
/*  94 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*  95 */       stringbuilder.append(EnumChatFormatting.RESET);
/*     */     }
/*     */     
/*  98 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */   public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components)
/*     */   {
/* 103 */     Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function()
/*     */     {
/*     */       public Iterator<IChatComponent> apply(IChatComponent p_apply_1_)
/*     */       {
/* 107 */         return p_apply_1_.iterator();
/*     */       }
/* 109 */     }));
/* 110 */     iterator = Iterators.transform(iterator, new Function()
/*     */     {
/*     */       public IChatComponent apply(IChatComponent p_apply_1_)
/*     */       {
/* 114 */         IChatComponent ichatcomponent = p_apply_1_.createCopy();
/* 115 */         ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
/* 116 */         return ichatcomponent;
/*     */       }
/* 118 */     });
/* 119 */     return iterator;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 124 */     if (this == p_equals_1_)
/*     */     {
/* 126 */       return true;
/*     */     }
/* 128 */     if (!(p_equals_1_ instanceof ChatComponentStyle))
/*     */     {
/* 130 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 134 */     ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
/* 135 */     return (this.siblings.equals(chatcomponentstyle.siblings)) && (getChatStyle().equals(chatcomponentstyle.getChatStyle()));
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 141 */     return 31 * this.style.hashCode() + this.siblings.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 146 */     return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatComponentStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */