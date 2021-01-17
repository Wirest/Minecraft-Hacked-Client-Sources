// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.routing;

import org.apache.http.util.Args;
import org.apache.http.annotation.Immutable;

@Immutable
public class BasicRouteDirector implements HttpRouteDirector
{
    public int nextStep(final RouteInfo plan, final RouteInfo fact) {
        Args.notNull(plan, "Planned route");
        int step = -1;
        if (fact == null || fact.getHopCount() < 1) {
            step = this.firstStep(plan);
        }
        else if (plan.getHopCount() > 1) {
            step = this.proxiedStep(plan, fact);
        }
        else {
            step = this.directStep(plan, fact);
        }
        return step;
    }
    
    protected int firstStep(final RouteInfo plan) {
        return (plan.getHopCount() > 1) ? 2 : 1;
    }
    
    protected int directStep(final RouteInfo plan, final RouteInfo fact) {
        if (fact.getHopCount() > 1) {
            return -1;
        }
        if (!plan.getTargetHost().equals(fact.getTargetHost())) {
            return -1;
        }
        if (plan.isSecure() != fact.isSecure()) {
            return -1;
        }
        if (plan.getLocalAddress() != null && !plan.getLocalAddress().equals(fact.getLocalAddress())) {
            return -1;
        }
        return 0;
    }
    
    protected int proxiedStep(final RouteInfo plan, final RouteInfo fact) {
        if (fact.getHopCount() <= 1) {
            return -1;
        }
        if (!plan.getTargetHost().equals(fact.getTargetHost())) {
            return -1;
        }
        final int phc = plan.getHopCount();
        final int fhc = fact.getHopCount();
        if (phc < fhc) {
            return -1;
        }
        for (int i = 0; i < fhc - 1; ++i) {
            if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
                return -1;
            }
        }
        if (phc > fhc) {
            return 4;
        }
        if ((fact.isTunnelled() && !plan.isTunnelled()) || (fact.isLayered() && !plan.isLayered())) {
            return -1;
        }
        if (plan.isTunnelled() && !fact.isTunnelled()) {
            return 3;
        }
        if (plan.isLayered() && !fact.isLayered()) {
            return 5;
        }
        if (plan.isSecure() != fact.isSecure()) {
            return -1;
        }
        return 0;
    }
}
