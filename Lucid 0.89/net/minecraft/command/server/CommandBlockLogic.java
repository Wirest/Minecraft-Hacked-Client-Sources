package net.minecraft.command.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public abstract class CommandBlockLogic implements ICommandSender
{
    /** The formatting for the timestamp on commands run. */
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");

    /** The number of successful commands run. (used for redstone output) */
    private int successCount;
    private boolean trackOutput = true;

    /** The previously run command. */
    private IChatComponent lastOutput = null;

    /** The command stored in the command block. */
    private String commandStored = "";

    /** The custom name of the command block. (defaults to "@") */
    private String customName = "@";
    private final CommandResultStats resultStats = new CommandResultStats();

    /**
     * returns the successCount int.
     */
    public int getSuccessCount()
    {
        return this.successCount;
    }

    /**
     * Returns the lastOutput.
     */
    public IChatComponent getLastOutput()
    {
        return this.lastOutput;
    }

    /**
     * Stores data to NBT format.
     */
    public void writeDataToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setString("Command", this.commandStored);
        tagCompound.setInteger("SuccessCount", this.successCount);
        tagCompound.setString("CustomName", this.customName);
        tagCompound.setBoolean("TrackOutput", this.trackOutput);

        if (this.lastOutput != null && this.trackOutput)
        {
            tagCompound.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }

        this.resultStats.func_179670_b(tagCompound);
    }

    /**
     * Reads NBT formatting and stored data into variables.
     */
    public void readDataFromNBT(NBTTagCompound nbt)
    {
        this.commandStored = nbt.getString("Command");
        this.successCount = nbt.getInteger("SuccessCount");

        if (nbt.hasKey("CustomName", 8))
        {
            this.customName = nbt.getString("CustomName");
        }

        if (nbt.hasKey("TrackOutput", 1))
        {
            this.trackOutput = nbt.getBoolean("TrackOutput");
        }

        if (nbt.hasKey("LastOutput", 8) && this.trackOutput)
        {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
        }

        this.resultStats.func_179668_a(nbt);
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     *  
     * @param permLevel The permission level required to execute the command
     * @param commandName The name of the command
     */
    @Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return permLevel <= 2;
    }

    /**
     * Sets the command.
     */
    public void setCommand(String command)
    {
        this.commandStored = command;
        this.successCount = 0;
    }

    /**
     * Returns the customName of the command block.
     */
    public String getCustomName()
    {
        return this.commandStored;
    }

    public void trigger(World worldIn)
    {
        if (worldIn.isRemote)
        {
            this.successCount = 0;
        }

        MinecraftServer var2 = MinecraftServer.getServer();

        if (var2 != null && var2.isAnvilFileSet() && var2.isCommandBlockEnabled())
        {
            ICommandManager var3 = var2.getCommandManager();

            try
            {
                this.lastOutput = null;
                this.successCount = var3.executeCommand(this, this.commandStored);
            }
            catch (Throwable var7)
            {
                CrashReport var5 = CrashReport.makeCrashReport(var7, "Executing command block");
                CrashReportCategory var6 = var5.makeCategory("Command to be executed");
                var6.addCrashSectionCallable("Command", new Callable()
                {
                    public String func_180324_a()
                    {
                        return CommandBlockLogic.this.getCustomName();
                    }
                    @Override
					public Object call()
                    {
                        return this.func_180324_a();
                    }
                });
                var6.addCrashSectionCallable("Name", new Callable()
                {
                    public String func_180326_a()
                    {
                        return CommandBlockLogic.this.getCommandSenderName();
                    }
                    @Override
					public Object call()
                    {
                        return this.func_180326_a();
                    }
                });
                throw new ReportedException(var5);
            }
        }
        else
        {
            this.successCount = 0;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        return this.customName;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
	public IChatComponent getDisplayName()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public void setName(String p_145754_1_)
    {
        this.customName = p_145754_1_;
    }

    /**
     * Send a chat message to the CommandSender
     *  
     * @param component The ChatComponent to send
     */
    @Override
	public void addChatMessage(IChatComponent component)
    {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote)
        {
            this.lastOutput = (new ChatComponentText("[" + timestampFormat.format(new Date()) + "] ")).appendSibling(component);
            this.func_145756_e();
        }
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    @Override
	public boolean sendCommandFeedback()
    {
        MinecraftServer var1 = MinecraftServer.getServer();
        return var1 == null || !var1.isAnvilFileSet() || var1.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
    }

    @Override
	public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        this.resultStats.func_179672_a(this, type, amount);
    }

    public abstract void func_145756_e();

    public abstract int func_145751_f();

    public abstract void func_145757_a(ByteBuf var1);

    public void setLastOutput(IChatComponent lastOutputMessage)
    {
        this.lastOutput = lastOutputMessage;
    }

    public void setTrackOutput(boolean shouldTrackOutput)
    {
        this.trackOutput = shouldTrackOutput;
    }

    public boolean shouldTrackOutput()
    {
        return this.trackOutput;
    }

    public boolean func_175574_a(EntityPlayer p_175574_1_)
    {
        if (!p_175574_1_.capabilities.isCreativeMode)
        {
            return false;
        }
        else
        {
            if (p_175574_1_.getEntityWorld().isRemote)
            {
                p_175574_1_.openEditCommandBlock(this);
            }

            return true;
        }
    }

    public CommandResultStats getCommandResultStats()
    {
        return this.resultStats;
    }
}
