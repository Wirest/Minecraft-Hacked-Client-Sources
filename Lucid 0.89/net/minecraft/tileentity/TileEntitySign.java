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
    private EntityPlayer player;
    private final CommandResultStats field_174883_i = new CommandResultStats();

    @Override
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

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        this.isEditable = false;
        super.readFromNBT(compound);
        ICommandSender var2 = new ICommandSender()
        {
            @Override
			public String getCommandSenderName()
            {
                return "Sign";
            }
            @Override
			public IChatComponent getDisplayName()
            {
                return new ChatComponentText(this.getCommandSenderName());
            }
            @Override
			public void addChatMessage(IChatComponent component) {}
            @Override
			public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return true;
            }
            @Override
			public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            @Override
			public Vec3 getPositionVector()
            {
                return new Vec3(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
            }
            @Override
			public World getEntityWorld()
            {
                return TileEntitySign.this.worldObj;
            }
            @Override
			public Entity getCommandSenderEntity()
            {
                return null;
            }
            @Override
			public boolean sendCommandFeedback()
            {
                return false;
            }
            @Override
			public void setCommandStat(CommandResultStats.Type type, int amount) {}
        };

        for (int var3 = 0; var3 < 4; ++var3)
        {
            String var4 = compound.getString("Text" + (var3 + 1));

            try
            {
                IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);

                try
                {
                    this.signText[var3] = ChatComponentProcessor.processComponent(var2, var5, (Entity)null);
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
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    @Override
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
    public void setEditable(boolean isEditableIn)
    {
        this.isEditable = isEditableIn;

        if (!isEditableIn)
        {
            this.player = null;
        }
    }

    public void setPlayer(EntityPlayer playerIn)
    {
        this.player = playerIn;
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }

    public boolean executeCommand(final EntityPlayer playerIn)
    {
        ICommandSender var2 = new ICommandSender()
        {
            @Override
			public String getCommandSenderName()
            {
                return playerIn.getCommandSenderName();
            }
            @Override
			public IChatComponent getDisplayName()
            {
                return playerIn.getDisplayName();
            }
            @Override
			public void addChatMessage(IChatComponent component) {}
            @Override
			public boolean canCommandSenderUseCommand(int permLevel, String commandName)
            {
                return true;
            }
            @Override
			public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            @Override
			public Vec3 getPositionVector()
            {
                return new Vec3(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
            }
            @Override
			public World getEntityWorld()
            {
                return playerIn.getEntityWorld();
            }
            @Override
			public Entity getCommandSenderEntity()
            {
                return playerIn;
            }
            @Override
			public boolean sendCommandFeedback()
            {
                return false;
            }
            @Override
			public void setCommandStat(CommandResultStats.Type type, int amount)
            {
                TileEntitySign.this.field_174883_i.func_179672_a(this, type, amount);
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
