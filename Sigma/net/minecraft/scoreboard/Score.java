package net.minecraft.scoreboard;

import java.util.Comparator;
import java.util.List;

public class Score {
    /**
     * Used for sorting score by points
     */
    public static final Comparator scoreComparator = new Comparator() {
        private static final String __OBFID = "CL_00000618";

        public int compare(Score p_compare_1_, Score p_compare_2_) {
            return p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints() ? 1 : (p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints() ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName()));
        }

        @Override
        public int compare(Object p_compare_1_, Object p_compare_2_) {
            return this.compare((Score) p_compare_1_, (Score) p_compare_2_);
        }
    };
    private final Scoreboard theScoreboard;
    private final ScoreObjective theScoreObjective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean field_178817_f;
    private boolean field_178818_g;
    private static final String __OBFID = "CL_00000617";

    public Score(Scoreboard p_i2309_1_, ScoreObjective p_i2309_2_, String p_i2309_3_) {
        theScoreboard = p_i2309_1_;
        theScoreObjective = p_i2309_2_;
        scorePlayerName = p_i2309_3_;
        field_178818_g = true;
    }

    public void increseScore(int p_96649_1_) {
        if (theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            setScorePoints(getScorePoints() + p_96649_1_);
        }
    }

    public void decreaseScore(int p_96646_1_) {
        if (theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            setScorePoints(getScorePoints() - p_96646_1_);
        }
    }

    public void func_96648_a() {
        if (theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            increseScore(1);
        }
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(int p_96647_1_) {
        int var2 = scorePoints;
        scorePoints = p_96647_1_;

        if (var2 != p_96647_1_ || field_178818_g) {
            field_178818_g = false;
            getScoreScoreboard().func_96536_a(this);
        }
    }

    public ScoreObjective getObjective() {
        return theScoreObjective;
    }

    /**
     * Returns the name of the player this score belongs to
     */
    public String getPlayerName() {
        return scorePlayerName;
    }

    public Scoreboard getScoreScoreboard() {
        return theScoreboard;
    }

    public boolean func_178816_g() {
        return field_178817_f;
    }

    public void func_178815_a(boolean p_178815_1_) {
        field_178817_f = p_178815_1_;
    }

    public void func_96651_a(List p_96651_1_) {
        setScorePoints(theScoreObjective.getCriteria().func_96635_a(p_96651_1_));
    }
}
