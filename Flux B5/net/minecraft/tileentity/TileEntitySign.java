package net.minecraft.tileentity;

import com.google.gson.JsonParseException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity
{
    public final IChatComponent[] signText = new IChatComponent[] {new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("")};

    /**
     * The index of the line currently being edited. Only used on client side, but defined on both. Note this is only
     * really used when the > < are going to be visible.
     */
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer field_145917_k;
    private final CommandResultStats field_174883_i = new CommandResultStats();
    private static final String __OBFID = "CL_00000363";

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            String var3 = IChatComponent.Serializer.componentToJson(this.signText[var2]);
            compound.setString("Text" + (var2 + 1), var3);
        }

        this.field_174883_i.func_179670_b(compound);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.isEditable = false;
        super.readFromNBT(compound);
        ICommandSender var2 = new ICommandSender()
        {
            private static final String __OBFID = "CL_00002039";
            public String getName()
            {
                return "Sign";
            }
            public IChatComponent getDisplayName()
            {
                return new ChatComponentText(this.getName());
            }
            public void addChatMessage(IChatComponent message) {}
            public boolean canCommandSenderUseCommand(int permissionLevel, String command)
            {
                return true;
            }
            public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3 getPositionVector()
            {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return TileEntitySign.this.worldObj;
            }
            public Entity getCommandSenderEntity()
            {
                return null;
            }
            public boolean sendCommandFeedback()
            {
                return false;
            }
            public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {}
        };

        for (int var3 = 0; var3 < 4; ++var3)
        {
            String var4 = compound.getString("Text" + (var3 + 1));

            try
            {
                IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);

                try
                {
                    this.signText[var3] = ChatComponentProcessor.func_179985_a(var2, var5, (Entity)null);
                }
                catch (CommandException var7)
                {
                    this.signText[var3] = var5;
                }
            }
            catch (JsonParseException var8)
            {
                this.signText[var3] = new ChatComponentText(var4);
            }
        }

        this.field_174883_i.func_179668_a(compound);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        IChatComponent[] var1 = new IChatComponent[4];
        System.arraycopy(this.signText, 0, var1, 0, 4);
        return new S33PacketUpdateSign(this.worldObj, this.pos, var1);
    }

    public boolean getIsEditable()
    {
        return this.isEditable;
    }

    /**
     * Sets the sign's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean p_145913_1_)
    {
        this.isEditable = p_145913_1_;

        if (!p_145913_1_)
        {
            this.field_145917_k = null;
        }
    }

    public void func_145912_a(EntityPlayer p_145912_1_)
    {
        this.field_145917_k = p_145912_1_;
    }

    public EntityPlayer func_145911_b()
    {
        return this.field_145917_k;
    }

    public boolean func_174882_b(final EntityPlayer p_174882_1_)
    {
        ICommandSender var2 = new ICommandSender()
        {
            private static final String __OBFID = "CL_00002038";
            public String getName()
            {
                return p_174882_1_.getName();
            }
            public IChatComponent getDisplayName()
            {
                return p_174882_1_.getDisplayName();
            }
            public void addChatMessage(IChatComponent message) {}
            public boolean canCommandSenderUseCommand(int permissionLevel, String command)
            {
                return true;
            }
            public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3 getPositionVector()
            {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return p_174882_1_.getEntityWorld();
            }
            public Entity getCommandSenderEntity()
            {
                return p_174882_1_;
            }
            public boolean sendCommandFeedback()
            {
                return false;
            }
            public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_)
            {
                TileEntitySign.this.field_174883_i.func_179672_a(this, p_174794_1_, p_174794_2_);
            }
        };

        for (int var3 = 0; var3 < this.signText.length; ++var3)
        {
            ChatStyle var4 = this.signText[var3] == null ? null : this.signText[var3].getChatStyle();

            if (var4 != null && var4.getChatClickEvent() != null)
            {
                ClickEvent var5 = var4.getChatClickEvent();

                if (var5.getAction() == ClickEvent.Action.RUN_COMMAND)
                {
                    MinecraftServer.getServer().getCommandManager().executeCommand(var2, var5.getValue());
                }
            }
        }

        return true;
    }

    public CommandResultStats func_174880_d()
    {
        return this.field_174883_i;
    }
}
