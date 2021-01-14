package optifine;

public class VillagerProfession
{
    private int profession;
    private int[] careers;

    public VillagerProfession(int p_i113_1_)
    {
        this(p_i113_1_, (int[])null);
    }

    public VillagerProfession(int p_i114_1_, int p_i114_2_)
    {
        this(p_i114_1_, new int[] {p_i114_2_});
    }

    public VillagerProfession(int p_i115_1_, int[] p_i115_2_)
    {
        this.profession = p_i115_1_;
        this.careers = p_i115_2_;
    }

    public boolean matches(int p_matches_1_, int p_matches_2_)
    {
        return this.profession != p_matches_1_ ? false : this.careers == null || Config.equalsOne(p_matches_2_, this.careers);
    }

    private boolean hasCareer(int p_hasCareer_1_)
    {
        return this.careers == null ? false : Config.equalsOne(p_hasCareer_1_, this.careers);
    }

    public boolean addCareer(int p_addCareer_1_)
    {
        if (this.careers == null)
        {
            this.careers = new int[] {p_addCareer_1_};
            return true;
        }
        else if (this.hasCareer(p_addCareer_1_))
        {
            return false;
        }
        else
        {
            this.careers = Config.addIntToArray(this.careers, p_addCareer_1_);
            return true;
        }
    }

    public int getProfession()
    {
        return this.profession;
    }

    public int[] getCareers()
    {
        return this.careers;
    }

    public String toString()
    {
        return this.careers == null ? "" + this.profession : "" + this.profession + ":" + Config.arrayToString(this.careers);
    }
}
