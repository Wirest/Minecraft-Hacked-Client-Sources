package net.minecraft.util;

import java.util.Iterator;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ChatComponentScore extends ChatComponentStyle
{
    private final String name;
    private final String objective;

    /** The value displayed instead of the real score (may be null) */
    private String value = "";

    public ChatComponentScore(String nameIn, String objectiveIn)
    {
        this.name = nameIn;
        this.objective = objectiveIn;
    }

    public String getName()
    {
        return this.name;
    }

    public String getObjective()
    {
        return this.objective;
    }

    /**
     * Sets the value displayed instead of the real score.
     *  
     * @param valueIn The value to display instead
     */
    public void setValue(String valueIn)
    {
        this.value = valueIn;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    @Override
	public String getUnformattedTextForChat()
    {
        MinecraftServer var1 = MinecraftServer.getServer();

        if (var1 != null && var1.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value))
        {
            Scoreboard var2 = var1.worldServerForDimension(0).getScoreboard();
            ScoreObjective var3 = var2.getObjective(this.objective);

            if (var2.entityHasObjective(this.name, var3))
            {
                Score var4 = var2.getValueFromObjective(this.name, var3);
                this.setValue(String.format("%d", new Object[] {Integer.valueOf(var4.getScorePoints())}));
            }
            else
            {
                this.value = "";
            }
        }

        return this.value;
    }

    public ChatComponentScore createCopyImpl()
    {
        ChatComponentScore var1 = new ChatComponentScore(this.name, this.objective);
        var1.setValue(this.value);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator var2 = this.getSiblings().iterator();

        while (var2.hasNext())
        {
            IChatComponent var3 = (IChatComponent)var2.next();
            var1.appendSibling(var3.createCopy());
        }

        return var1;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChatComponentScore))
        {
            return false;
        }
        else
        {
            ChatComponentScore var2 = (ChatComponentScore)p_equals_1_;
            return this.name.equals(var2.name) && this.objective.equals(var2.objective) && super.equals(p_equals_1_);
        }
    }

    @Override
	public String toString()
    {
        return "ScoreComponent{name=\'" + this.name + '\'' + "objective=\'" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    @Override
	public IChatComponent createCopy()
    {
        return this.createCopyImpl();
    }
}
