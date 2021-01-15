/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class ChatComponentTranslation extends ChatComponentStyle
/*     */ {
/*     */   private final String key;
/*     */   private final Object[] formatArgs;
/*  16 */   private final Object syncLock = new Object();
/*  17 */   private long lastTranslationUpdateTimeInMilliseconds = -1L;
/*  18 */   List<IChatComponent> children = Lists.newArrayList();
/*  19 */   public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */   
/*     */   public ChatComponentTranslation(String translationKey, Object... args)
/*     */   {
/*  23 */     this.key = translationKey;
/*  24 */     this.formatArgs = args;
/*     */     Object[] arrayOfObject;
/*  26 */     int j = (arrayOfObject = args).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject[i];
/*     */       
/*  28 */       if ((object instanceof IChatComponent))
/*     */       {
/*  30 */         ((IChatComponent)object).getChatStyle().setParentStyle(getChatStyle());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   synchronized void ensureInitialized()
/*     */   {
/*  40 */     synchronized (this.syncLock)
/*     */     {
/*  42 */       long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
/*     */       
/*  44 */       if (i == this.lastTranslationUpdateTimeInMilliseconds)
/*     */       {
/*  46 */         return;
/*     */       }
/*     */       
/*  49 */       this.lastTranslationUpdateTimeInMilliseconds = i;
/*  50 */       this.children.clear();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  55 */       initializeFromFormat(StatCollector.translateToLocal(this.key));
/*     */     }
/*     */     catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception)
/*     */     {
/*  59 */       this.children.clear();
/*     */       
/*     */       try
/*     */       {
/*  63 */         initializeFromFormat(StatCollector.translateToFallback(this.key));
/*     */       }
/*     */       catch (ChatComponentTranslationFormatException var5)
/*     */       {
/*  67 */         throw chatcomponenttranslationformatexception;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initializeFromFormat(String format)
/*     */   {
/*  77 */     boolean flag = false;
/*  78 */     Matcher matcher = stringVariablePattern.matcher(format);
/*  79 */     int i = 0;
/*  80 */     int j = 0;
/*     */     try
/*     */     {
/*     */       int l;
/*  86 */       for (; 
/*     */           
/*  86 */           matcher.find(j); j = l)
/*     */       {
/*  88 */         int k = matcher.start();
/*  89 */         l = matcher.end();
/*     */         
/*  91 */         if (k > j)
/*     */         {
/*  93 */           ChatComponentText chatcomponenttext = new ChatComponentText(String.format(format.substring(j, k), new Object[0]));
/*  94 */           chatcomponenttext.getChatStyle().setParentStyle(getChatStyle());
/*  95 */           this.children.add(chatcomponenttext);
/*     */         }
/*     */         
/*  98 */         String s2 = matcher.group(2);
/*  99 */         String s = format.substring(k, l);
/*     */         
/* 101 */         if (("%".equals(s2)) && ("%%".equals(s)))
/*     */         {
/* 103 */           ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
/* 104 */           chatcomponenttext2.getChatStyle().setParentStyle(getChatStyle());
/* 105 */           this.children.add(chatcomponenttext2);
/*     */         }
/*     */         else
/*     */         {
/* 109 */           if (!"s".equals(s2))
/*     */           {
/* 111 */             throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + s + "'");
/*     */           }
/*     */           
/* 114 */           String s1 = matcher.group(1);
/* 115 */           int i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;
/*     */           
/* 117 */           if (i1 < this.formatArgs.length)
/*     */           {
/* 119 */             this.children.add(getFormatArgumentAsComponent(i1));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 124 */       if (j < format.length())
/*     */       {
/* 126 */         ChatComponentText chatcomponenttext1 = new ChatComponentText(String.format(format.substring(j), new Object[0]));
/* 127 */         chatcomponenttext1.getChatStyle().setParentStyle(getChatStyle());
/* 128 */         this.children.add(chatcomponenttext1);
/*     */       }
/*     */     }
/*     */     catch (IllegalFormatException illegalformatexception)
/*     */     {
/* 133 */       throw new ChatComponentTranslationFormatException(this, illegalformatexception);
/*     */     }
/*     */   }
/*     */   
/*     */   private IChatComponent getFormatArgumentAsComponent(int index)
/*     */   {
/* 139 */     if (index >= this.formatArgs.length)
/*     */     {
/* 141 */       throw new ChatComponentTranslationFormatException(this, index);
/*     */     }
/*     */     
/*     */ 
/* 145 */     Object object = this.formatArgs[index];
/*     */     IChatComponent ichatcomponent;
/*     */     IChatComponent ichatcomponent;
/* 148 */     if ((object instanceof IChatComponent))
/*     */     {
/* 150 */       ichatcomponent = (IChatComponent)object;
/*     */     }
/*     */     else
/*     */     {
/* 154 */       ichatcomponent = new ChatComponentText(object == null ? "null" : object.toString());
/* 155 */       ichatcomponent.getChatStyle().setParentStyle(getChatStyle());
/*     */     }
/*     */     
/* 158 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */ 
/*     */   public IChatComponent setChatStyle(ChatStyle style)
/*     */   {
/* 164 */     super.setChatStyle(style);
/*     */     Object[] arrayOfObject;
/* 166 */     int j = (arrayOfObject = this.formatArgs).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject[i];
/*     */       
/* 168 */       if ((object instanceof IChatComponent))
/*     */       {
/* 170 */         ((IChatComponent)object).getChatStyle().setParentStyle(getChatStyle());
/*     */       }
/*     */     }
/*     */     
/* 174 */     if (this.lastTranslationUpdateTimeInMilliseconds > -1L)
/*     */     {
/* 176 */       for (IChatComponent ichatcomponent : this.children)
/*     */       {
/* 178 */         ichatcomponent.getChatStyle().setParentStyle(style);
/*     */       }
/*     */     }
/*     */     
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public Iterator<IChatComponent> iterator()
/*     */   {
/* 187 */     ensureInitialized();
/* 188 */     return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnformattedTextForChat()
/*     */   {
/* 197 */     ensureInitialized();
/* 198 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/* 200 */     for (IChatComponent ichatcomponent : this.children)
/*     */     {
/* 202 */       stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
/*     */     }
/*     */     
/* 205 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatComponentTranslation createCopy()
/*     */   {
/* 213 */     Object[] aobject = new Object[this.formatArgs.length];
/*     */     
/* 215 */     for (int i = 0; i < this.formatArgs.length; i++)
/*     */     {
/* 217 */       if ((this.formatArgs[i] instanceof IChatComponent))
/*     */       {
/* 219 */         aobject[i] = ((IChatComponent)this.formatArgs[i]).createCopy();
/*     */       }
/*     */       else
/*     */       {
/* 223 */         aobject[i] = this.formatArgs[i];
/*     */       }
/*     */     }
/*     */     
/* 227 */     ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.key, aobject);
/* 228 */     chatcomponenttranslation.setChatStyle(getChatStyle().createShallowCopy());
/*     */     
/* 230 */     for (IChatComponent ichatcomponent : getSiblings())
/*     */     {
/* 232 */       chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
/*     */     }
/*     */     
/* 235 */     return chatcomponenttranslation;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 240 */     if (this == p_equals_1_)
/*     */     {
/* 242 */       return true;
/*     */     }
/* 244 */     if (!(p_equals_1_ instanceof ChatComponentTranslation))
/*     */     {
/* 246 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 250 */     ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_equals_1_;
/* 251 */     return (Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs)) && (this.key.equals(chatcomponenttranslation.key)) && (super.equals(p_equals_1_));
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 257 */     int i = super.hashCode();
/* 258 */     i = 31 * i + this.key.hashCode();
/* 259 */     i = 31 * i + Arrays.hashCode(this.formatArgs);
/* 260 */     return i;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 265 */     return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*     */   }
/*     */   
/*     */   public String getKey()
/*     */   {
/* 270 */     return this.key;
/*     */   }
/*     */   
/*     */   public Object[] getFormatArgs()
/*     */   {
/* 275 */     return this.formatArgs;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatComponentTranslation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */