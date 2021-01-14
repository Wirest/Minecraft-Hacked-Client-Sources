package net.minecraft.scoreboard;

public class ScoreObjective {
    private final Scoreboard theScoreboard;
    private final String name;

    /**
     * The ScoreObjectiveCriteria for this objetive
     */
    private final IScoreObjectiveCriteria objectiveCriteria;
    private IScoreObjectiveCriteria.EnumRenderType field_178768_d;
    private String displayName;
    private static final String __OBFID = "CL_00000614";

    public ScoreObjective(Scoreboard p_i2307_1_, String p_i2307_2_, IScoreObjectiveCriteria p_i2307_3_) {
        this.theScoreboard = p_i2307_1_;
        this.name = p_i2307_2_;
        this.objectiveCriteria = p_i2307_3_;
        this.displayName = p_i2307_2_;
        this.field_178768_d = p_i2307_3_.func_178790_c();
    }

    public Scoreboard getScoreboard() {
        return this.theScoreboard;
    }

    public String getName() {
        return this.name;
    }

    public IScoreObjectiveCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String p_96681_1_) {
        this.displayName = p_96681_1_;
        this.theScoreboard.func_96532_b(this);
    }

    public IScoreObjectiveCriteria.EnumRenderType func_178766_e() {
        return this.field_178768_d;
    }

    public void func_178767_a(IScoreObjectiveCriteria.EnumRenderType p_178767_1_) {
        this.field_178768_d = p_178767_1_;
        this.theScoreboard.func_96532_b(this);
    }
}
