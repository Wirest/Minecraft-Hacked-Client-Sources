package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;

/**
 * Created by Arithmo on 5/31/2017 at 7:51 PM.
 */
public class EventGetMouseOver extends Event {

    private double extraReach;
    private double hitboxExpansion;

    public double getExtraReach() {
        return this.extraReach;
    }

    public double getHitboxExpansion() {
        return this.hitboxExpansion;
    }

    public void setExtraReach(double extraReach) {
        this.extraReach = extraReach;
    }

    public void setHitboxExpansion(double hitboxExpansion) {
        this.hitboxExpansion = hitboxExpansion;
    }

}
