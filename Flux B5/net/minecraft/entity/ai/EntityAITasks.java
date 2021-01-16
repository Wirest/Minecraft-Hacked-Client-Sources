package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks
{
    private static final Logger logger = LogManager.getLogger();

    /** A list of EntityAITaskEntrys in EntityAITasks. */
    private List taskEntries = Lists.newArrayList();

    /** A list of EntityAITaskEntrys that are currently being executed. */
    private List executingTaskEntries = Lists.newArrayList();

    /** Instance of Profiler. */
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate = 3;
    private static final String __OBFID = "CL_00001588";

    public EntityAITasks(Profiler p_i1628_1_)
    {
        this.theProfiler = p_i1628_1_;
    }

    /**
     * Add a now AITask. Args : priority, task
     */
    public void addTask(int p_75776_1_, EntityAIBase p_75776_2_)
    {
        this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(p_75776_1_, p_75776_2_));
    }

    /**
     * removes the indicated task from the entity's AI tasks.
     */
    public void removeTask(EntityAIBase p_85156_1_)
    {
        Iterator var2 = this.taskEntries.iterator();

        while (var2.hasNext())
        {
            EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry)var2.next();
            EntityAIBase var4 = var3.action;

            if (var4 == p_85156_1_)
            {
                if (this.executingTaskEntries.contains(var3))
                {
                    var4.resetTask();
                    this.executingTaskEntries.remove(var3);
                }

                var2.remove();
            }
        }
    }

    public void onUpdateTasks()
    {
        this.theProfiler.startSection("goalSetup");
        Iterator var1;
        EntityAITasks.EntityAITaskEntry var2;

        if (this.tickCount++ % this.tickRate == 0)
        {
            var1 = this.taskEntries.iterator();

            while (var1.hasNext())
            {
                var2 = (EntityAITasks.EntityAITaskEntry)var1.next();
                boolean var3 = this.executingTaskEntries.contains(var2);

                if (var3)
                {
                    if (this.canUse(var2) && this.canContinue(var2))
                    {
                        continue;
                    }

                    var2.action.resetTask();
                    this.executingTaskEntries.remove(var2);
                }

                if (this.canUse(var2) && var2.action.shouldExecute())
                {
                    var2.action.startExecuting();
                    this.executingTaskEntries.add(var2);
                }
            }
        }
        else
        {
            var1 = this.executingTaskEntries.iterator();

            while (var1.hasNext())
            {
                var2 = (EntityAITasks.EntityAITaskEntry)var1.next();

                if (!this.canContinue(var2))
                {
                    var2.action.resetTask();
                    var1.remove();
                }
            }
        }

        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        var1 = this.executingTaskEntries.iterator();

        while (var1.hasNext())
        {
            var2 = (EntityAITasks.EntityAITaskEntry)var1.next();
            var2.action.updateTask();
        }

        this.theProfiler.endSection();
    }

    /**
     * Determine if a specific AI Task should continue being executed.
     */
    private boolean canContinue(EntityAITasks.EntityAITaskEntry p_75773_1_)
    {
        boolean var2 = p_75773_1_.action.continueExecuting();
        return var2;
    }

    /**
     * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
     * tasks are compatible with it or all lower priority tasks can be interrupted.
     */
    private boolean canUse(EntityAITasks.EntityAITaskEntry p_75775_1_)
    {
        Iterator var2 = this.taskEntries.iterator();

        while (var2.hasNext())
        {
            EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry)var2.next();

            if (var3 != p_75775_1_)
            {
                if (p_75775_1_.priority >= var3.priority)
                {
                    if (!this.areTasksCompatible(p_75775_1_, var3) && this.executingTaskEntries.contains(var3))
                    {
                        return false;
                    }
                }
                else if (!var3.action.isInterruptible() && this.executingTaskEntries.contains(var3))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns whether two EntityAITaskEntries can be executed concurrently
     */
    private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry p_75777_1_, EntityAITasks.EntityAITaskEntry p_75777_2_)
    {
        return (p_75777_1_.action.getMutexBits() & p_75777_2_.action.getMutexBits()) == 0;
    }

    class EntityAITaskEntry
    {
        public EntityAIBase action;
        public int priority;
        private static final String __OBFID = "CL_00001589";

        public EntityAITaskEntry(int p_i1627_2_, EntityAIBase p_i1627_3_)
        {
            this.priority = p_i1627_2_;
            this.action = p_i1627_3_;
        }
    }
}
