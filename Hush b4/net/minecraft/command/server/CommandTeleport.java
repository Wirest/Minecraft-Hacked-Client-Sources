// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTeleport extends CommandBase
{
    @Override
    public String getCommandName() {
        return "tp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.tp.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        int i = 0;
        Entity entity;
        if (args.length != 2 && args.length != 4 && args.length != 6) {
            entity = CommandBase.getCommandSenderAsPlayer(sender);
        }
        else {
            entity = CommandBase.func_175768_b(sender, args[0]);
            i = 1;
        }
        if (args.length != 1 && args.length != 2) {
            if (args.length < i + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (entity.worldObj != null) {
                int lvt_5_2_ = i + 1;
                final CoordinateArg commandbase$coordinatearg = CommandBase.parseCoordinate(entity.posX, args[i], true);
                final CoordinateArg commandbase$coordinatearg2 = CommandBase.parseCoordinate(entity.posY, args[lvt_5_2_++], 0, 0, false);
                final CoordinateArg commandbase$coordinatearg3 = CommandBase.parseCoordinate(entity.posZ, args[lvt_5_2_++], true);
                final CoordinateArg commandbase$coordinatearg4 = CommandBase.parseCoordinate(entity.rotationYaw, (args.length > lvt_5_2_) ? args[lvt_5_2_++] : "~", false);
                final CoordinateArg commandbase$coordinatearg5 = CommandBase.parseCoordinate(entity.rotationPitch, (args.length > lvt_5_2_) ? args[lvt_5_2_] : "~", false);
                if (entity instanceof EntityPlayerMP) {
                    final Set<S08PacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
                    if (commandbase$coordinatearg.func_179630_c()) {
                        set.add(S08PacketPlayerPosLook.EnumFlags.X);
                    }
                    if (commandbase$coordinatearg2.func_179630_c()) {
                        set.add(S08PacketPlayerPosLook.EnumFlags.Y);
                    }
                    if (commandbase$coordinatearg3.func_179630_c()) {
                        set.add(S08PacketPlayerPosLook.EnumFlags.Z);
                    }
                    if (commandbase$coordinatearg5.func_179630_c()) {
                        set.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
                    }
                    if (commandbase$coordinatearg4.func_179630_c()) {
                        set.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                    }
                    float f = (float)commandbase$coordinatearg4.func_179629_b();
                    if (!commandbase$coordinatearg4.func_179630_c()) {
                        f = MathHelper.wrapAngleTo180_float(f);
                    }
                    float f2 = (float)commandbase$coordinatearg5.func_179629_b();
                    if (!commandbase$coordinatearg5.func_179630_c()) {
                        f2 = MathHelper.wrapAngleTo180_float(f2);
                    }
                    if (f2 > 90.0f || f2 < -90.0f) {
                        f2 = MathHelper.wrapAngleTo180_float(180.0f - f2);
                        f = MathHelper.wrapAngleTo180_float(f + 180.0f);
                    }
                    entity.mountEntity(null);
                    ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg2.func_179629_b(), commandbase$coordinatearg3.func_179629_b(), f, f2, set);
                    entity.setRotationYawHead(f);
                }
                else {
                    float f3 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg4.func_179628_a());
                    float f4 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg5.func_179628_a());
                    if (f4 > 90.0f || f4 < -90.0f) {
                        f4 = MathHelper.wrapAngleTo180_float(180.0f - f4);
                        f3 = MathHelper.wrapAngleTo180_float(f3 + 180.0f);
                    }
                    entity.setLocationAndAngles(commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), commandbase$coordinatearg3.func_179628_a(), f3, f4);
                    entity.setRotationYawHead(f3);
                }
                CommandBase.notifyOperators(sender, this, "commands.tp.success.coordinates", entity.getName(), commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), commandbase$coordinatearg3.func_179628_a());
            }
        }
        else {
            final Entity entity2 = CommandBase.func_175768_b(sender, args[args.length - 1]);
            if (entity2.worldObj != entity.worldObj) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            entity.mountEntity(null);
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            else {
                entity.setLocationAndAngles(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            CommandBase.notifyOperators(sender, this, "commands.tp.success", entity.getName(), entity2.getName());
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length != 1 && args.length != 2) ? null : CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
