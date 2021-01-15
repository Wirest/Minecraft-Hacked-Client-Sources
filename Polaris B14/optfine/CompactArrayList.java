/*     */ package optfine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class CompactArrayList
/*     */ {
/*     */   private ArrayList list;
/*     */   private int initialCapacity;
/*     */   private float loadFactor;
/*     */   private int countValid;
/*     */   
/*     */   public CompactArrayList()
/*     */   {
/*  14 */     this(10, 0.75F);
/*     */   }
/*     */   
/*     */   public CompactArrayList(int p_i26_1_)
/*     */   {
/*  19 */     this(p_i26_1_, 0.75F);
/*     */   }
/*     */   
/*     */   public CompactArrayList(int p_i27_1_, float p_i27_2_)
/*     */   {
/*  24 */     this.list = null;
/*  25 */     this.initialCapacity = 0;
/*  26 */     this.loadFactor = 1.0F;
/*  27 */     this.countValid = 0;
/*  28 */     this.list = new ArrayList(p_i27_1_);
/*  29 */     this.initialCapacity = p_i27_1_;
/*  30 */     this.loadFactor = p_i27_2_;
/*     */   }
/*     */   
/*     */   public void add(int p_add_1_, Object p_add_2_)
/*     */   {
/*  35 */     if (p_add_2_ != null)
/*     */     {
/*  37 */       this.countValid += 1;
/*     */     }
/*     */     
/*  40 */     this.list.add(p_add_1_, p_add_2_);
/*     */   }
/*     */   
/*     */   public boolean add(Object p_add_1_)
/*     */   {
/*  45 */     if (p_add_1_ != null)
/*     */     {
/*  47 */       this.countValid += 1;
/*     */     }
/*     */     
/*  50 */     return this.list.add(p_add_1_);
/*     */   }
/*     */   
/*     */   public Object set(int p_set_1_, Object p_set_2_)
/*     */   {
/*  55 */     Object object = this.list.set(p_set_1_, p_set_2_);
/*     */     
/*  57 */     if (p_set_2_ != object)
/*     */     {
/*  59 */       if (object == null)
/*     */       {
/*  61 */         this.countValid += 1;
/*     */       }
/*     */       
/*  64 */       if (p_set_2_ == null)
/*     */       {
/*  66 */         this.countValid -= 1;
/*     */       }
/*     */     }
/*     */     
/*  70 */     return object;
/*     */   }
/*     */   
/*     */   public Object remove(int p_remove_1_)
/*     */   {
/*  75 */     Object object = this.list.remove(p_remove_1_);
/*     */     
/*  77 */     if (object != null)
/*     */     {
/*  79 */       this.countValid -= 1;
/*     */     }
/*     */     
/*  82 */     return object;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  87 */     this.list.clear();
/*  88 */     this.countValid = 0;
/*     */   }
/*     */   
/*     */   public void compact()
/*     */   {
/*  93 */     if ((this.countValid <= 0) && (this.list.size() <= 0))
/*     */     {
/*  95 */       clear();
/*     */     }
/*  97 */     else if (this.list.size() > this.initialCapacity)
/*     */     {
/*  99 */       float f = this.countValid * 1.0F / this.list.size();
/*     */       
/* 101 */       if (f <= this.loadFactor)
/*     */       {
/* 103 */         int i = 0;
/*     */         
/* 105 */         for (int j = 0; j < this.list.size(); j++)
/*     */         {
/* 107 */           Object object = this.list.get(j);
/*     */           
/* 109 */           if (object != null)
/*     */           {
/* 111 */             if (j != i)
/*     */             {
/* 113 */               this.list.set(i, object);
/*     */             }
/*     */             
/* 116 */             i++;
/*     */           }
/*     */         }
/*     */         
/* 120 */         for (int k = this.list.size() - 1; k >= i; k--)
/*     */         {
/* 122 */           this.list.remove(k);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean contains(Object p_contains_1_)
/*     */   {
/* 130 */     return this.list.contains(p_contains_1_);
/*     */   }
/*     */   
/*     */   public Object get(int p_get_1_)
/*     */   {
/* 135 */     return this.list.get(p_get_1_);
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 140 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 145 */     return this.list.size();
/*     */   }
/*     */   
/*     */   public int getCountValid()
/*     */   {
/* 150 */     return this.countValid;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CompactArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */