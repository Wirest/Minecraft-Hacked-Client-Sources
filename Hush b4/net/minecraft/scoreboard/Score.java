// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import java.util.Comparator;

public class Score
{
    public static final Comparator<Score> scoreComparator;
    private final Scoreboard theScoreboard;
    private final ScoreObjective theScoreObjective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean locked;
    private boolean field_178818_g;
    
    static {
        scoreComparator = new Comparator<Score>() {
            @Override
            public int compare(final Score p_compare_1_, final Score p_compare_2_) {
                return (p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints()) ? 1 : ((p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints()) ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName()));
            }
        };
    }
    
    public Score(final Scoreboard theScoreboardIn, final ScoreObjective theScoreObjectiveIn, final String scorePlayerNameIn) {
        this.theScoreboard = theScoreboardIn;
        this.theScoreObjective = theScoreObjectiveIn;
        this.scorePlayerName = scorePlayerNameIn;
        this.field_178818_g = true;
    }
    
    public void increseScore(final int amount) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() + amount);
    }
    
    public void decreaseScore(final int amount) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() - amount);
    }
    
    public void func_96648_a() {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.increseScore(1);
    }
    
    public int getScorePoints() {
        return this.scorePoints;
    }
    
    public void setScorePoints(final int points) {
        final int i = this.scorePoints;
        this.scorePoints = points;
        if (i != points || this.field_178818_g) {
            this.field_178818_g = false;
            this.getScoreScoreboard().func_96536_a(this);
        }
    }
    
    public ScoreObjective getObjective() {
        return this.theScoreObjective;
    }
    
    public String getPlayerName() {
        return this.scorePlayerName;
    }
    
    public Scoreboard getScoreScoreboard() {
        return this.theScoreboard;
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void setLocked(final boolean locked) {
        this.locked = locked;
    }
    
    public void func_96651_a(final List<EntityPlayer> p_96651_1_) {
        this.setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(p_96651_1_));
    }
}
