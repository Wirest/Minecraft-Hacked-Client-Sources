/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ public class JsonToNBT
/*     */ {
/*  13 */   private static final org.apache.logging.log4j.Logger logger = ;
/*  14 */   private static final Pattern field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
/*     */   
/*     */   public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException
/*     */   {
/*  18 */     jsonString = jsonString.trim();
/*     */     
/*  20 */     if (!jsonString.startsWith("{"))
/*     */     {
/*  22 */       throw new NBTException("Invalid tag encountered, expected '{' as first char.");
/*     */     }
/*  24 */     if (func_150310_b(jsonString) != 1)
/*     */     {
/*  26 */       throw new NBTException("Encountered multiple top tags, only one expected");
/*     */     }
/*     */     
/*     */ 
/*  30 */     return (NBTTagCompound)func_150316_a("tag", jsonString).parse();
/*     */   }
/*     */   
/*     */   static int func_150310_b(String p_150310_0_)
/*     */     throws NBTException
/*     */   {
/*  36 */     int i = 0;
/*  37 */     boolean flag = false;
/*  38 */     Stack<Character> stack = new Stack();
/*     */     
/*  40 */     for (int j = 0; j < p_150310_0_.length(); j++)
/*     */     {
/*  42 */       char c0 = p_150310_0_.charAt(j);
/*     */       
/*  44 */       if (c0 == '"')
/*     */       {
/*  46 */         if (func_179271_b(p_150310_0_, j))
/*     */         {
/*  48 */           if (!flag)
/*     */           {
/*  50 */             throw new NBTException("Illegal use of \\\": " + p_150310_0_);
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/*  55 */           flag = !flag;
/*     */         }
/*     */       }
/*  58 */       else if (!flag)
/*     */       {
/*  60 */         if ((c0 != '{') && (c0 != '['))
/*     */         {
/*  62 */           if ((c0 == '}') && ((stack.isEmpty()) || (((Character)stack.pop()).charValue() != '{')))
/*     */           {
/*  64 */             throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
/*     */           }
/*     */           
/*  67 */           if ((c0 == ']') && ((stack.isEmpty()) || (((Character)stack.pop()).charValue() != '[')))
/*     */           {
/*  69 */             throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  74 */           if (stack.isEmpty())
/*     */           {
/*  76 */             i++;
/*     */           }
/*     */           
/*  79 */           stack.push(Character.valueOf(c0));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  84 */     if (flag)
/*     */     {
/*  86 */       throw new NBTException("Unbalanced quotation: " + p_150310_0_);
/*     */     }
/*  88 */     if (!stack.isEmpty())
/*     */     {
/*  90 */       throw new NBTException("Unbalanced brackets: " + p_150310_0_);
/*     */     }
/*     */     
/*     */ 
/*  94 */     if ((i == 0) && (!p_150310_0_.isEmpty()))
/*     */     {
/*  96 */       i = 1;
/*     */     }
/*     */     
/*  99 */     return i;
/*     */   }
/*     */   
/*     */   static Any func_179272_a(String... p_179272_0_)
/*     */     throws NBTException
/*     */   {
/* 105 */     return func_150316_a(p_179272_0_[0], p_179272_0_[1]);
/*     */   }
/*     */   
/*     */   static Any func_150316_a(String p_150316_0_, String p_150316_1_) throws NBTException
/*     */   {
/* 110 */     p_150316_1_ = p_150316_1_.trim();
/*     */     
/* 112 */     if (p_150316_1_.startsWith("{"))
/*     */     {
/* 114 */       p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
/*     */       
/*     */       String s1;
/*     */       
/* 118 */       for (Compound jsontonbt$compound = new Compound(p_150316_0_); p_150316_1_.length() > 0; p_150316_1_ = p_150316_1_.substring(s1.length() + 1))
/*     */       {
/* 120 */         s1 = func_150314_a(p_150316_1_, true);
/*     */         
/* 122 */         if (s1.length() > 0)
/*     */         {
/* 124 */           boolean flag1 = false;
/* 125 */           jsontonbt$compound.field_150491_b.add(func_179270_a(s1, flag1));
/*     */         }
/*     */         
/* 128 */         if (p_150316_1_.length() < s1.length() + 1) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 133 */         char c1 = p_150316_1_.charAt(s1.length());
/*     */         
/* 135 */         if ((c1 != ',') && (c1 != '{') && (c1 != '}') && (c1 != '[') && (c1 != ']'))
/*     */         {
/* 137 */           throw new NBTException("Unexpected token '" + c1 + "' at: " + p_150316_1_.substring(s1.length()));
/*     */         }
/*     */       }
/*     */       
/* 141 */       return jsontonbt$compound;
/*     */     }
/* 143 */     if ((p_150316_1_.startsWith("[")) && (!field_179273_b.matcher(p_150316_1_).matches()))
/*     */     {
/* 145 */       p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
/*     */       
/*     */       String s;
/*     */       
/* 149 */       for (List jsontonbt$list = new List(p_150316_0_); p_150316_1_.length() > 0; p_150316_1_ = p_150316_1_.substring(s.length() + 1))
/*     */       {
/* 151 */         s = func_150314_a(p_150316_1_, false);
/*     */         
/* 153 */         if (s.length() > 0)
/*     */         {
/* 155 */           boolean flag = true;
/* 156 */           jsontonbt$list.field_150492_b.add(func_179270_a(s, flag));
/*     */         }
/*     */         
/* 159 */         if (p_150316_1_.length() < s.length() + 1) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 164 */         char c0 = p_150316_1_.charAt(s.length());
/*     */         
/* 166 */         if ((c0 != ',') && (c0 != '{') && (c0 != '}') && (c0 != '[') && (c0 != ']'))
/*     */         {
/* 168 */           throw new NBTException("Unexpected token '" + c0 + "' at: " + p_150316_1_.substring(s.length()));
/*     */         }
/*     */       }
/*     */       
/* 172 */       return jsontonbt$list;
/*     */     }
/*     */     
/*     */ 
/* 176 */     return new Primitive(p_150316_0_, p_150316_1_);
/*     */   }
/*     */   
/*     */   private static Any func_179270_a(String p_179270_0_, boolean p_179270_1_)
/*     */     throws NBTException
/*     */   {
/* 182 */     String s = func_150313_b(p_179270_0_, p_179270_1_);
/* 183 */     String s1 = func_150311_c(p_179270_0_, p_179270_1_);
/* 184 */     return func_179272_a(new String[] { s, s1 });
/*     */   }
/*     */   
/*     */   private static String func_150314_a(String p_150314_0_, boolean p_150314_1_) throws NBTException
/*     */   {
/* 189 */     int i = func_150312_a(p_150314_0_, ':');
/* 190 */     int j = func_150312_a(p_150314_0_, ',');
/*     */     
/* 192 */     if (p_150314_1_)
/*     */     {
/* 194 */       if (i == -1)
/*     */       {
/* 196 */         throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
/*     */       }
/*     */       
/* 199 */       if ((j != -1) && (j < i))
/*     */       {
/* 201 */         throw new NBTException("Name error at: " + p_150314_0_);
/*     */       }
/*     */     }
/* 204 */     else if ((i == -1) || (i > j))
/*     */     {
/* 206 */       i = -1;
/*     */     }
/*     */     
/* 209 */     return func_179269_a(p_150314_0_, i);
/*     */   }
/*     */   
/*     */   private static String func_179269_a(String p_179269_0_, int p_179269_1_) throws NBTException
/*     */   {
/* 214 */     Stack<Character> stack = new Stack();
/* 215 */     int i = p_179269_1_ + 1;
/* 216 */     boolean flag = false;
/* 217 */     boolean flag1 = false;
/* 218 */     boolean flag2 = false;
/*     */     
/* 220 */     for (int j = 0; i < p_179269_0_.length(); i++)
/*     */     {
/* 222 */       char c0 = p_179269_0_.charAt(i);
/*     */       
/* 224 */       if (c0 == '"')
/*     */       {
/* 226 */         if (func_179271_b(p_179269_0_, i))
/*     */         {
/* 228 */           if (!flag)
/*     */           {
/* 230 */             throw new NBTException("Illegal use of \\\": " + p_179269_0_);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 235 */           flag = !flag;
/*     */           
/* 237 */           if ((flag) && (!flag2))
/*     */           {
/* 239 */             flag1 = true;
/*     */           }
/*     */           
/* 242 */           if (!flag)
/*     */           {
/* 244 */             j = i;
/*     */           }
/*     */         }
/*     */       }
/* 248 */       else if (!flag)
/*     */       {
/* 250 */         if ((c0 != '{') && (c0 != '['))
/*     */         {
/* 252 */           if ((c0 == '}') && ((stack.isEmpty()) || (((Character)stack.pop()).charValue() != '{')))
/*     */           {
/* 254 */             throw new NBTException("Unbalanced curly brackets {}: " + p_179269_0_);
/*     */           }
/*     */           
/* 257 */           if ((c0 == ']') && ((stack.isEmpty()) || (((Character)stack.pop()).charValue() != '[')))
/*     */           {
/* 259 */             throw new NBTException("Unbalanced square brackets []: " + p_179269_0_);
/*     */           }
/*     */           
/* 262 */           if ((c0 == ',') && (stack.isEmpty()))
/*     */           {
/* 264 */             return p_179269_0_.substring(0, i);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 269 */           stack.push(Character.valueOf(c0));
/*     */         }
/*     */       }
/*     */       
/* 273 */       if (!Character.isWhitespace(c0))
/*     */       {
/* 275 */         if ((!flag) && (flag1) && (j != i))
/*     */         {
/* 277 */           return p_179269_0_.substring(0, j + 1);
/*     */         }
/*     */         
/* 280 */         flag2 = true;
/*     */       }
/*     */     }
/*     */     
/* 284 */     return p_179269_0_.substring(0, i);
/*     */   }
/*     */   
/*     */   private static String func_150313_b(String p_150313_0_, boolean p_150313_1_) throws NBTException
/*     */   {
/* 289 */     if (p_150313_1_)
/*     */     {
/* 291 */       p_150313_0_ = p_150313_0_.trim();
/*     */       
/* 293 */       if ((p_150313_0_.startsWith("{")) || (p_150313_0_.startsWith("[")))
/*     */       {
/* 295 */         return "";
/*     */       }
/*     */     }
/*     */     
/* 299 */     int i = func_150312_a(p_150313_0_, ':');
/*     */     
/* 301 */     if (i == -1)
/*     */     {
/* 303 */       if (p_150313_1_)
/*     */       {
/* 305 */         return "";
/*     */       }
/*     */       
/*     */ 
/* 309 */       throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 314 */     return p_150313_0_.substring(0, i).trim();
/*     */   }
/*     */   
/*     */   private static String func_150311_c(String p_150311_0_, boolean p_150311_1_)
/*     */     throws NBTException
/*     */   {
/* 320 */     if (p_150311_1_)
/*     */     {
/* 322 */       p_150311_0_ = p_150311_0_.trim();
/*     */       
/* 324 */       if ((p_150311_0_.startsWith("{")) || (p_150311_0_.startsWith("[")))
/*     */       {
/* 326 */         return p_150311_0_;
/*     */       }
/*     */     }
/*     */     
/* 330 */     int i = func_150312_a(p_150311_0_, ':');
/*     */     
/* 332 */     if (i == -1)
/*     */     {
/* 334 */       if (p_150311_1_)
/*     */       {
/* 336 */         return p_150311_0_;
/*     */       }
/*     */       
/*     */ 
/* 340 */       throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 345 */     return p_150311_0_.substring(i + 1).trim();
/*     */   }
/*     */   
/*     */ 
/*     */   private static int func_150312_a(String p_150312_0_, char p_150312_1_)
/*     */   {
/* 351 */     int i = 0;
/*     */     
/* 353 */     for (boolean flag = true; i < p_150312_0_.length(); i++)
/*     */     {
/* 355 */       char c0 = p_150312_0_.charAt(i);
/*     */       
/* 357 */       if (c0 == '"')
/*     */       {
/* 359 */         if (!func_179271_b(p_150312_0_, i))
/*     */         {
/* 361 */           flag = !flag;
/*     */         }
/*     */       }
/* 364 */       else if (flag)
/*     */       {
/* 366 */         if (c0 == p_150312_1_)
/*     */         {
/* 368 */           return i;
/*     */         }
/*     */         
/* 371 */         if ((c0 == '{') || (c0 == '['))
/*     */         {
/* 373 */           return -1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 378 */     return -1;
/*     */   }
/*     */   
/*     */   private static boolean func_179271_b(String p_179271_0_, int p_179271_1_)
/*     */   {
/* 383 */     return (p_179271_1_ > 0) && (p_179271_0_.charAt(p_179271_1_ - 1) == '\\') && (!func_179271_b(p_179271_0_, p_179271_1_ - 1));
/*     */   }
/*     */   
/*     */   static abstract class Any
/*     */   {
/*     */     protected String json;
/*     */     
/*     */     public abstract NBTBase parse() throws NBTException;
/*     */   }
/*     */   
/*     */   static class Compound extends JsonToNBT.Any
/*     */   {
/* 395 */     protected List<JsonToNBT.Any> field_150491_b = Lists.newArrayList();
/*     */     
/*     */     public Compound(String p_i45137_1_)
/*     */     {
/* 399 */       this.json = p_i45137_1_;
/*     */     }
/*     */     
/*     */     public NBTBase parse() throws NBTException
/*     */     {
/* 404 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */       
/* 406 */       for (JsonToNBT.Any jsontonbt$any : this.field_150491_b)
/*     */       {
/* 408 */         nbttagcompound.setTag(jsontonbt$any.json, jsontonbt$any.parse());
/*     */       }
/*     */       
/* 411 */       return nbttagcompound;
/*     */     }
/*     */   }
/*     */   
/*     */   static class List extends JsonToNBT.Any
/*     */   {
/* 417 */     protected List<JsonToNBT.Any> field_150492_b = Lists.newArrayList();
/*     */     
/*     */     public List(String json)
/*     */     {
/* 421 */       this.json = json;
/*     */     }
/*     */     
/*     */     public NBTBase parse() throws NBTException
/*     */     {
/* 426 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/* 428 */       for (JsonToNBT.Any jsontonbt$any : this.field_150492_b)
/*     */       {
/* 430 */         nbttaglist.appendTag(jsontonbt$any.parse());
/*     */       }
/*     */       
/* 433 */       return nbttaglist;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Primitive extends JsonToNBT.Any
/*     */   {
/* 439 */     private static final Pattern DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
/* 440 */     private static final Pattern FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
/* 441 */     private static final Pattern BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
/* 442 */     private static final Pattern LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
/* 443 */     private static final Pattern SHORT = Pattern.compile("[-+]?[0-9]+[s|S]");
/* 444 */     private static final Pattern INTEGER = Pattern.compile("[-+]?[0-9]+");
/* 445 */     private static final Pattern DOUBLE_UNTYPED = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
/* 446 */     private static final Splitter SPLITTER = Splitter.on(',').omitEmptyStrings();
/*     */     protected String jsonValue;
/*     */     
/*     */     public Primitive(String p_i45139_1_, String p_i45139_2_)
/*     */     {
/* 451 */       this.json = p_i45139_1_;
/* 452 */       this.jsonValue = p_i45139_2_;
/*     */     }
/*     */     
/*     */     public NBTBase parse() throws NBTException
/*     */     {
/*     */       try
/*     */       {
/* 459 */         if (DOUBLE.matcher(this.jsonValue).matches())
/*     */         {
/* 461 */           return new NBTTagDouble(Double.parseDouble(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
/*     */         }
/*     */         
/* 464 */         if (FLOAT.matcher(this.jsonValue).matches())
/*     */         {
/* 466 */           return new NBTTagFloat(Float.parseFloat(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
/*     */         }
/*     */         
/* 469 */         if (BYTE.matcher(this.jsonValue).matches())
/*     */         {
/* 471 */           return new NBTTagByte(Byte.parseByte(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
/*     */         }
/*     */         
/* 474 */         if (LONG.matcher(this.jsonValue).matches())
/*     */         {
/* 476 */           return new NBTTagLong(Long.parseLong(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
/*     */         }
/*     */         
/* 479 */         if (SHORT.matcher(this.jsonValue).matches())
/*     */         {
/* 481 */           return new NBTTagShort(Short.parseShort(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
/*     */         }
/*     */         
/* 484 */         if (INTEGER.matcher(this.jsonValue).matches())
/*     */         {
/* 486 */           return new NBTTagInt(Integer.parseInt(this.jsonValue));
/*     */         }
/*     */         
/* 489 */         if (DOUBLE_UNTYPED.matcher(this.jsonValue).matches())
/*     */         {
/* 491 */           return new NBTTagDouble(Double.parseDouble(this.jsonValue));
/*     */         }
/*     */         
/* 494 */         if ((this.jsonValue.equalsIgnoreCase("true")) || (this.jsonValue.equalsIgnoreCase("false")))
/*     */         {
/* 496 */           return new NBTTagByte((byte)(Boolean.parseBoolean(this.jsonValue) ? 1 : 0));
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException var6)
/*     */       {
/* 501 */         this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
/* 502 */         return new NBTTagString(this.jsonValue);
/*     */       }
/*     */       
/* 505 */       if ((this.jsonValue.startsWith("[")) && (this.jsonValue.endsWith("]")))
/*     */       {
/* 507 */         String s = this.jsonValue.substring(1, this.jsonValue.length() - 1);
/* 508 */         String[] astring = (String[])com.google.common.collect.Iterables.toArray(SPLITTER.split(s), String.class);
/*     */         
/*     */         try
/*     */         {
/* 512 */           int[] aint = new int[astring.length];
/*     */           
/* 514 */           for (int j = 0; j < astring.length; j++)
/*     */           {
/* 516 */             aint[j] = Integer.parseInt(astring[j].trim());
/*     */           }
/*     */           
/* 519 */           return new NBTTagIntArray(aint);
/*     */         }
/*     */         catch (NumberFormatException var5)
/*     */         {
/* 523 */           return new NBTTagString(this.jsonValue);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 528 */       if ((this.jsonValue.startsWith("\"")) && (this.jsonValue.endsWith("\"")))
/*     */       {
/* 530 */         this.jsonValue = this.jsonValue.substring(1, this.jsonValue.length() - 1);
/*     */       }
/*     */       
/* 533 */       this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
/* 534 */       StringBuilder stringbuilder = new StringBuilder();
/*     */       
/* 536 */       for (int i = 0; i < this.jsonValue.length(); i++)
/*     */       {
/* 538 */         if ((i < this.jsonValue.length() - 1) && (this.jsonValue.charAt(i) == '\\') && (this.jsonValue.charAt(i + 1) == '\\'))
/*     */         {
/* 540 */           stringbuilder.append('\\');
/* 541 */           i++;
/*     */         }
/*     */         else
/*     */         {
/* 545 */           stringbuilder.append(this.jsonValue.charAt(i));
/*     */         }
/*     */       }
/*     */       
/* 549 */       return new NBTTagString(stringbuilder.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\nbt\JsonToNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */