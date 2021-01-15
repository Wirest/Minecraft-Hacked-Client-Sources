/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityAITasks
/*     */ {
/*  12 */   private static final Logger logger = ;
/*  13 */   private List<EntityAITaskEntry> taskEntries = Lists.newArrayList();
/*  14 */   private List<EntityAITaskEntry> executingTaskEntries = Lists.newArrayList();
/*     */   
/*     */   private final Profiler theProfiler;
/*     */   
/*     */   private int tickCount;
/*  19 */   private int tickRate = 3;
/*     */   
/*     */   public EntityAITasks(Profiler profilerIn)
/*     */   {
/*  23 */     this.theProfiler = profilerIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTask(int priority, EntityAIBase task)
/*     */   {
/*  31 */     this.taskEntries.add(new EntityAITaskEntry(priority, task));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTask(EntityAIBase task)
/*     */   {
/*  39 */     Iterator<EntityAITaskEntry> iterator = this.taskEntries.iterator();
/*     */     
/*  41 */     while (iterator.hasNext())
/*     */     {
/*  43 */       EntityAITaskEntry entityaitasks$entityaitaskentry = (EntityAITaskEntry)iterator.next();
/*  44 */       EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
/*     */       
/*  46 */       if (entityaibase == task)
/*     */       {
/*  48 */         if (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry))
/*     */         {
/*  50 */           entityaibase.resetTask();
/*  51 */           this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
/*     */         }
/*     */         
/*  54 */         iterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onUpdateTasks()
/*     */   {
/*  61 */     this.theProfiler.startSection("goalSetup");
/*     */     
/*  63 */     if (this.tickCount++ % this.tickRate == 0)
/*     */     {
/*  65 */       Iterator iterator = this.taskEntries.iterator();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */       while (iterator.hasNext())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  79 */         EntityAITaskEntry entityaitasks$entityaitaskentry = (EntityAITaskEntry)iterator.next();
/*  80 */         boolean flag = this.executingTaskEntries.contains(entityaitasks$entityaitaskentry);
/*     */         
/*  82 */         if (flag)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  87 */           if ((!canUse(entityaitasks$entityaitaskentry)) || (!canContinue(entityaitasks$entityaitaskentry)))
/*     */           {
/*  89 */             entityaitasks$entityaitaskentry.action.resetTask();
/*  90 */             this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*  95 */         else if ((canUse(entityaitasks$entityaitaskentry)) && (entityaitasks$entityaitaskentry.action.shouldExecute()))
/*     */         {
/*  97 */           entityaitasks$entityaitaskentry.action.startExecuting();
/*  98 */           this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 104 */     Iterator<EntityAITaskEntry> iterator1 = this.executingTaskEntries.iterator();
/*     */     EntityAITaskEntry entityaitasks$entityaitaskentry1;
/* 106 */     while (iterator1.hasNext())
/*     */     {
/* 108 */       entityaitasks$entityaitaskentry1 = (EntityAITaskEntry)iterator1.next();
/*     */       
/* 110 */       if (!canContinue(entityaitasks$entityaitaskentry1))
/*     */       {
/* 112 */         entityaitasks$entityaitaskentry1.action.resetTask();
/* 113 */         iterator1.remove();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 118 */     this.theProfiler.endSection();
/* 119 */     this.theProfiler.startSection("goalTick");
/*     */     
/* 121 */     for (EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries)
/*     */     {
/* 123 */       entityaitasks$entityaitaskentry2.action.updateTask();
/*     */     }
/*     */     
/* 126 */     this.theProfiler.endSection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canContinue(EntityAITaskEntry taskEntry)
/*     */   {
/* 134 */     boolean flag = taskEntry.action.continueExecuting();
/* 135 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canUse(EntityAITaskEntry taskEntry)
/*     */   {
/* 144 */     for (EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries)
/*     */     {
/* 146 */       if (entityaitasks$entityaitaskentry != taskEntry)
/*     */       {
/* 148 */         if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority)
/*     */         {
/* 150 */           if ((!areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry)) && (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)))
/*     */           {
/* 152 */             return false;
/*     */           }
/*     */         }
/* 155 */         else if ((!entityaitasks$entityaitaskentry.action.isInterruptible()) && (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)))
/*     */         {
/* 157 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 162 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean areTasksCompatible(EntityAITaskEntry taskEntry1, EntityAITaskEntry taskEntry2)
/*     */   {
/* 170 */     return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
/*     */   }
/*     */   
/*     */   class EntityAITaskEntry
/*     */   {
/*     */     public EntityAIBase action;
/*     */     public int priority;
/*     */     
/*     */     public EntityAITaskEntry(int priorityIn, EntityAIBase task)
/*     */     {
/* 180 */       this.priority = priorityIn;
/* 181 */       this.action = task;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAITasks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */