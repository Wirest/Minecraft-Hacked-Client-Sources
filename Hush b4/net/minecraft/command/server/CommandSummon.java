// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collection;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.command.CommandException;
import net.minecraft.util.BlockPos;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSummon extends CommandBase
{
    @Override
    public String getCommandName() {
        return "summon";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.summon.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        final String s = args[0];
        BlockPos blockpos = sender.getPosition();
        final Vec3 vec3 = sender.getPositionVector();
        double d0 = vec3.xCoord;
        double d2 = vec3.yCoord;
        double d3 = vec3.zCoord;
        if (args.length >= 4) {
            d0 = CommandBase.parseDouble(d0, args[1], true);
            d2 = CommandBase.parseDouble(d2, args[2], false);
            d3 = CommandBase.parseDouble(d3, args[3], true);
            blockpos = new BlockPos(d0, d2, d3);
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }
        if ("LightningBolt".equals(s)) {
            world.addWeatherEffect(new EntityLightningBolt(world, d0, d2, d3));
            CommandBase.notifyOperators(sender, this, "commands.summon.success", new Object[0]);
        }
        else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            boolean flag = false;
            if (args.length >= 5) {
                final IChatComponent ichatcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 4);
                try {
                    nbttagcompound = JsonToNBT.getTagFromJson(ichatcomponent.getUnformattedText());
                    flag = true;
                }
                catch (NBTException nbtexception) {
                    throw new CommandException("commands.summon.tagError", new Object[] { nbtexception.getMessage() });
                }
            }
            nbttagcompound.setString("id", s);
            Entity entity2;
            try {
                entity2 = EntityList.createEntityFromNBT(nbttagcompound, world);
            }
            catch (RuntimeException var19) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            if (entity2 == null) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            entity2.setLocationAndAngles(d0, d2, d3, entity2.rotationYaw, entity2.rotationPitch);
            if (!flag && entity2 instanceof EntityLiving) {
                ((EntityLiving)entity2).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity2)), null);
            }
            world.spawnEntityInWorld(entity2);
            Entity entity3 = entity2;
            Entity entity4;
            for (NBTTagCompound nbttagcompound2 = nbttagcompound; entity3 != null && nbttagcompound2.hasKey("Riding", 10); entity3 = entity4, nbttagcompound2 = nbttagcompound2.getCompoundTag("Riding")) {
                entity4 = EntityList.createEntityFromNBT(nbttagcompound2.getCompoundTag("Riding"), world);
                if (entity4 != null) {
                    entity4.setLocationAndAngles(d0, d2, d3, entity4.rotationYaw, entity4.rotationPitch);
                    world.spawnEntityInWorld(entity4);
                    entity3.mountEntity(entity4);
                }
            }
            CommandBase.notifyOperators(sender, this, "commands.summon.success", new Object[0]);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : null);
    }
}
