package info.sigmaclient.management.account.relation;

import com.google.gson.annotations.Expose;

public class UserStatus {
    @Expose
    private final String userID;
    /**
     * Currently unused.
     */
    private int timesKilled, timesKilledBy;
    @Expose
    private EnumRelationship relationship;

    public UserStatus(String userID, EnumRelationship relationship, int timesKilled, int timesKilledBy) {
        this.userID = userID;
        this.relationship = relationship;
        this.timesKilled = timesKilled;
        this.timesKilledBy = timesKilledBy;
    }

    public UserStatus(String userID, EnumRelationship relationship) {
        this(userID, relationship, 0, 0);
    }

    public String getUserID() {
        return userID;
    }

    public int getTimesKilled() {
        return timesKilled;
    }

    public void addTimesKilled() {
        this.timesKilled += 1;
    }

    public int getTimesKilledBy() {
        return timesKilledBy;
    }

    public void addTimesKilledBy() {
        this.timesKilledBy += timesKilledBy;
    }

    public EnumRelationship getRelationship() {
        return relationship;
    }

    public boolean isRival() {
        return relationship.equals(EnumRelationship.Rival);
    }

    public boolean isFriend() {
        return relationship.equals(EnumRelationship.Friend);
    }

    public boolean isNeutral() {
        return relationship.equals(EnumRelationship.Neutral);
    }

    /**
     * Cycles throgh the relationship statuses
     */
    public void cycleRelationship() {
        switch (relationship) {
            case Friend:
                relationship = EnumRelationship.Rival;
                break;
            case Neutral:
                relationship = EnumRelationship.Friend;
                break;
            case Rival:
                relationship = EnumRelationship.Neutral;
                break;
            default:
                break;
        }
    }

    public void setRelationship(EnumRelationship relationship) {
        this.relationship = relationship;
    }
}
