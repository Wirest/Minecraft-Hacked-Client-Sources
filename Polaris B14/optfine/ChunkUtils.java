/*     */ package optfine;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ 
/*     */ public class ChunkUtils
/*     */ {
/*  10 */   private static Field fieldHasEntities = null;
/*  11 */   private static boolean fieldHasEntitiesMissing = false;
/*     */   
/*     */   public static boolean hasEntities(Chunk p_hasEntities_0_)
/*     */   {
/*  15 */     if (fieldHasEntities == null)
/*     */     {
/*  17 */       if (fieldHasEntitiesMissing)
/*     */       {
/*  19 */         return true;
/*     */       }
/*     */       
/*  22 */       fieldHasEntities = fildFieldHasEntities(p_hasEntities_0_);
/*     */       
/*  24 */       if (fieldHasEntities == null)
/*     */       {
/*  26 */         fieldHasEntitiesMissing = true;
/*  27 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  33 */       return fieldHasEntities.getBoolean(p_hasEntities_0_);
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  37 */       Config.warn("Error calling Chunk.hasEntities");
/*  38 */       Config.warn(exception.getClass().getName() + " " + exception.getMessage());
/*  39 */       fieldHasEntitiesMissing = true; }
/*  40 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static Field fildFieldHasEntities(Chunk p_fildFieldHasEntities_0_)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       List list = new ArrayList();
/*  49 */       List list1 = new ArrayList();
/*  50 */       Field[] afield = Chunk.class.getDeclaredFields();
/*     */       
/*  52 */       for (int i = 0; i < afield.length; i++)
/*     */       {
/*  54 */         Field field = afield[i];
/*     */         
/*  56 */         if (field.getType() == Boolean.TYPE)
/*     */         {
/*  58 */           field.setAccessible(true);
/*  59 */           list.add(field);
/*  60 */           list1.add(field.get(p_fildFieldHasEntities_0_));
/*     */         }
/*     */       }
/*     */       
/*  64 */       p_fildFieldHasEntities_0_.setHasEntities(false);
/*  65 */       List list2 = new ArrayList();
/*     */       
/*  67 */       for (Object field1 : list)
/*     */       {
/*  69 */         list2.add(((Field)field1).get(p_fildFieldHasEntities_0_));
/*     */       }
/*     */       
/*  72 */       p_fildFieldHasEntities_0_.setHasEntities(true);
/*  73 */       List list3 = new ArrayList();
/*     */       
/*  75 */       for (Object field2 : list)
/*     */       {
/*  77 */         list3.add(((Field)field2).get(p_fildFieldHasEntities_0_));
/*     */       }
/*     */       
/*  80 */       Object list4 = new ArrayList();
/*     */       
/*  82 */       for (int j = 0; j < list.size(); j++)
/*     */       {
/*  84 */         Field field3 = (Field)list.get(j);
/*  85 */         Boolean obool = (Boolean)list2.get(j);
/*  86 */         Boolean obool1 = (Boolean)list3.get(j);
/*     */         
/*  88 */         if ((!obool.booleanValue()) && (obool1.booleanValue()))
/*     */         {
/*  90 */           ((List)list4).add(field3);
/*  91 */           Boolean obool2 = (Boolean)list1.get(j);
/*  92 */           field3.set(p_fildFieldHasEntities_0_, obool2);
/*     */         }
/*     */       }
/*     */       
/*  96 */       if (((List)list4).size() == 1)
/*     */       {
/*  98 */         return (Field)((List)list4).get(0);
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 104 */       Config.warn(exception.getClass().getName() + " " + exception.getMessage());
/*     */       
/*     */ 
/* 107 */       Config.warn("Error finding Chunk.hasEntities"); }
/* 108 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ChunkUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */