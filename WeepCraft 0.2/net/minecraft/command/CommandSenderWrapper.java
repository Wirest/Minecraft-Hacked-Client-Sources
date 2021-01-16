package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender
{
    private final ICommandSender field_193043_a;
    @Nullable
    private final Vec3d field_194002_b;
    @Nullable
    private final BlockPos field_194003_c;
    @Nullable
    private final Integer field_194004_d;
    @Nullable
    private final Entity field_194005_e;
    @Nullable
    private final Boolean field_194006_f;

    public CommandSenderWrapper(ICommandSender p_i47599_1_, @Nullable Vec3d p_i47599_2_, @Nullable BlockPos p_i47599_3_, @Nullable Integer p_i47599_4_, @Nullable Entity p_i47599_5_, @Nullable Boolean p_i47599_6_)
    {
        this.field_193043_a = p_i47599_1_;
        this.field_194002_b = p_i47599_2_;
        this.field_194003_c = p_i47599_3_;
        this.field_194004_d = p_i47599_4_;
        this.field_194005_e = p_i47599_5_;
        this.field_194006_f = p_i47599_6_;
    }

    public static CommandSenderWrapper func_193998_a(ICommandSender p_193998_0_)
    {
        return p_193998_0_ instanceof CommandSenderWrapper ? (CommandSenderWrapper)p_193998_0_ : new CommandSenderWrapper(p_193998_0_, (Vec3d)null, (BlockPos)null, (Integer)null, (Entity)null, (Boolean)null);
    }

    public CommandSenderWrapper func_193997_a(Entity p_193997_1_, Vec3d p_193997_2_)
    {
        return this.field_194005_e == p_193997_1_ && Objects.equals(this.field_194002_b, p_193997_2_) ? this : new CommandSenderWrapper(this.field_193043_a, p_193997_2_, new BlockPos(p_193997_2_), this.field_194004_d, p_193997_1_, this.field_194006_f);
    }

    public CommandSenderWrapper func_193999_a(int p_193999_1_)
    {
        return this.field_194004_d != null && this.field_194004_d.intValue() <= p_193999_1_ ? this : new CommandSenderWrapper(this.field_193043_a, this.field_194002_b, this.field_194003_c, p_193999_1_, this.field_194005_e, this.field_194006_f);
    }

    public CommandSenderWrapper func_194001_a(boolean p_194001_1_)
    {
        return this.field_194006_f == null || this.field_194006_f.booleanValue() && !p_194001_1_ ? new CommandSenderWrapper(this.field_193043_a, this.field_194002_b, this.field_194003_c, this.field_194004_d, this.field_194005_e, p_194001_1_) : this;
    }

    public CommandSenderWrapper func_194000_i()
    {
        return this.field_194002_b != null ? this : new CommandSenderWrapper(this.field_193043_a, this.getPositionVector(), this.getPosition(), this.field_194004_d, this.field_194005_e, this.field_194006_f);
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.field_194005_e != null ? this.field_194005_e.getName() : this.field_193043_a.getName();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public ITextComponent getDisplayName()
    {
        return this.field_194005_e != null ? this.field_194005_e.getDisplayName() : this.field_193043_a.getDisplayName();
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(ITextComponent component)
    {
        if (this.field_194006_f == null || this.field_194006_f.booleanValue())
        {
            this.field_193043_a.addChatMessage(component);
        }
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return this.field_194004_d != null && this.field_194004_d.intValue() < permLevel ? false : this.field_193043_a.canCommandSenderUseCommand(permLevel, commandName);
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        if (this.field_194003_c != null)
        {
            return this.field_194003_c;
        }
        else
        {
            return this.field_194005_e != null ? this.field_194005_e.getPosition() : this.field_193043_a.getPosition();
        }
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3d getPositionVector()
    {
        if (this.field_194002_b != null)
        {
            return this.field_194002_b;
        }
        else
        {
            return this.field_194005_e != null ? this.field_194005_e.getPositionVector() : this.field_193043_a.getPositionVector();
        }
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return this.field_194005_e != null ? this.field_194005_e.getEntityWorld() : this.field_193043_a.getEntityWorld();
    }

    @Nullable

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    public Entity getCommandSenderEntity()
    {
        return this.field_194005_e != null ? this.field_194005_e.getCommandSenderEntity() : this.field_193043_a.getCommandSenderEntity();
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback()
    {
        return this.field_194006_f != null ? this.field_194006_f.booleanValue() : this.field_193043_a.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        if (this.field_194005_e != null)
        {
            this.field_194005_e.setCommandStat(type, amount);
        }
        else
        {
            this.field_193043_a.setCommandStat(type, amount);
        }
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this.field_193043_a.getServer();
    }
}
