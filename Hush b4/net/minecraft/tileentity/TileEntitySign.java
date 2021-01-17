// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.ChatStyle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.Packet;
import com.google.gson.JsonParseException;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class TileEntitySign extends TileEntity
{
    public final IChatComponent[] signText;
    public int lineBeingEdited;
    private boolean isEditable;
    private EntityPlayer player;
    private final CommandResultStats stats;
    
    public TileEntitySign() {
        this.signText = new IChatComponent[] { new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("") };
        this.lineBeingEdited = -1;
        this.isEditable = true;
        this.stats = new CommandResultStats();
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        for (int i = 0; i < 4; ++i) {
            final String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
        this.stats.writeStatsToNBT(compound);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        this.isEditable = false;
        super.readFromNBT(compound);
        final ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
                return "Sign";
            }
            
            @Override
            public IChatComponent getDisplayName() {
                return new ChatComponentText(this.getName());
            }
            
            @Override
            public void addChatMessage(final IChatComponent component) {
            }
            
            @Override
            public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                return true;
            }
            
            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return TileEntitySign.this.worldObj;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return null;
            }
            
            @Override
            public boolean sendCommandFeedback() {
                return false;
            }
            
            @Override
            public void setCommandStat(final CommandResultStats.Type type, final int amount) {
            }
        };
        for (int i = 0; i < 4; ++i) {
            final String s = compound.getString("Text" + (i + 1));
            try {
                final IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                try {
                    this.signText[i] = ChatComponentProcessor.processComponent(icommandsender, ichatcomponent, null);
                }
                catch (CommandException var7) {
                    this.signText[i] = ichatcomponent;
                }
            }
            catch (JsonParseException var8) {
                this.signText[i] = new ChatComponentText(s);
            }
        }
        this.stats.readStatsFromNBT(compound);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final IChatComponent[] aichatcomponent = new IChatComponent[4];
        System.arraycopy(this.signText, 0, aichatcomponent, 0, 4);
        return new S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
    }
    
    @Override
    public boolean func_183000_F() {
        return true;
    }
    
    public boolean getIsEditable() {
        return this.isEditable;
    }
    
    public void setEditable(final boolean isEditableIn) {
        if (!(this.isEditable = isEditableIn)) {
            this.player = null;
        }
    }
    
    public void setPlayer(final EntityPlayer playerIn) {
        this.player = playerIn;
    }
    
    public EntityPlayer getPlayer() {
        return this.player;
    }
    
    public boolean executeCommand(final EntityPlayer playerIn) {
        final ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
                return playerIn.getName();
            }
            
            @Override
            public IChatComponent getDisplayName() {
                return playerIn.getDisplayName();
            }
            
            @Override
            public void addChatMessage(final IChatComponent component) {
            }
            
            @Override
            public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                return permLevel <= 2;
            }
            
            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return playerIn.getEntityWorld();
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return playerIn;
            }
            
            @Override
            public boolean sendCommandFeedback() {
                return false;
            }
            
            @Override
            public void setCommandStat(final CommandResultStats.Type type, final int amount) {
                TileEntitySign.this.stats.func_179672_a(this, type, amount);
            }
        };
        for (int i = 0; i < this.signText.length; ++i) {
            final ChatStyle chatstyle = (this.signText[i] == null) ? null : this.signText[i].getChatStyle();
            if (chatstyle != null && chatstyle.getChatClickEvent() != null) {
                final ClickEvent clickevent = chatstyle.getChatClickEvent();
                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    MinecraftServer.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
                }
            }
        }
        return true;
    }
    
    public CommandResultStats getStats() {
        return this.stats;
    }
}
