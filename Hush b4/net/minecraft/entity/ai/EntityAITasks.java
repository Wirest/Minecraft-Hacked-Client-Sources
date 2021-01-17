// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.profiler.Profiler;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class EntityAITasks
{
    private static final Logger logger;
    private List<EntityAITaskEntry> taskEntries;
    private List<EntityAITaskEntry> executingTaskEntries;
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public EntityAITasks(final Profiler profilerIn) {
        this.taskEntries = (List<EntityAITaskEntry>)Lists.newArrayList();
        this.executingTaskEntries = (List<EntityAITaskEntry>)Lists.newArrayList();
        this.tickRate = 3;
        this.theProfiler = profilerIn;
    }
    
    public void addTask(final int priority, final EntityAIBase task) {
        this.taskEntries.add(new EntityAITaskEntry(priority, task));
    }
    
    public void removeTask(final EntityAIBase task) {
        final Iterator<EntityAITaskEntry> iterator = this.taskEntries.iterator();
        while (iterator.hasNext()) {
            final EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
            final EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
            if (entityaibase == task) {
                if (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
                    entityaibase.resetTask();
                    this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                }
                iterator.remove();
            }
        }
    }
    
    public void onUpdateTasks() {
        this.theProfiler.startSection("goalSetup");
        if (this.tickCount++ % this.tickRate == 0) {
            for (final EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
                final boolean flag = this.executingTaskEntries.contains(entityaitasks$entityaitaskentry);
                if (flag) {
                    if (this.canUse(entityaitasks$entityaitaskentry) && this.canContinue(entityaitasks$entityaitaskentry)) {
                        continue;
                    }
                    entityaitasks$entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                }
                if (this.canUse(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.action.shouldExecute()) {
                    entityaitasks$entityaitaskentry.action.startExecuting();
                    this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
                }
            }
        }
        else {
            final Iterator<EntityAITaskEntry> iterator2 = this.executingTaskEntries.iterator();
            while (iterator2.hasNext()) {
                final EntityAITaskEntry entityaitasks$entityaitaskentry2 = iterator2.next();
                if (!this.canContinue(entityaitasks$entityaitaskentry2)) {
                    entityaitasks$entityaitaskentry2.action.resetTask();
                    iterator2.remove();
                }
            }
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        for (final EntityAITaskEntry entityaitasks$entityaitaskentry3 : this.executingTaskEntries) {
            entityaitasks$entityaitaskentry3.action.updateTask();
        }
        this.theProfiler.endSection();
    }
    
    private boolean canContinue(final EntityAITaskEntry taskEntry) {
        final boolean flag = taskEntry.action.continueExecuting();
        return flag;
    }
    
    private boolean canUse(final EntityAITaskEntry taskEntry) {
        for (final EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
            if (entityaitasks$entityaitaskentry != taskEntry) {
                if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
                    if (!this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry) && this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
                        return false;
                    }
                    continue;
                }
                else {
                    if (!entityaitasks$entityaitaskentry.action.isInterruptible() && this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
                        return false;
                    }
                    continue;
                }
            }
        }
        return true;
    }
    
    private boolean areTasksCompatible(final EntityAITaskEntry taskEntry1, final EntityAITaskEntry taskEntry2) {
        return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0x0;
    }
    
    class EntityAITaskEntry
    {
        public EntityAIBase action;
        public int priority;
        
        public EntityAITaskEntry(final int priorityIn, final EntityAIBase task) {
            this.priority = priorityIn;
            this.action = task;
        }
    }
}
