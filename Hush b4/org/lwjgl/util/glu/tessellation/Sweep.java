// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Sweep
{
    private static final boolean TOLERANCE_NONZERO = false;
    private static final double SENTINEL_COORD = 4.0E150;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    private Sweep() {
    }
    
    private static void DebugEvent(final GLUtessellatorImpl tess) {
    }
    
    private static void AddWinding(final GLUhalfEdge eDst, final GLUhalfEdge eSrc) {
        eDst.winding += eSrc.winding;
        final GLUhalfEdge sym = eDst.Sym;
        sym.winding += eSrc.Sym.winding;
    }
    
    private static ActiveRegion RegionBelow(final ActiveRegion r) {
        return (ActiveRegion)Dict.dictKey(Dict.dictPred(r.nodeUp));
    }
    
    private static ActiveRegion RegionAbove(final ActiveRegion r) {
        return (ActiveRegion)Dict.dictKey(Dict.dictSucc(r.nodeUp));
    }
    
    static boolean EdgeLeq(final GLUtessellatorImpl tess, final ActiveRegion reg1, final ActiveRegion reg2) {
        final GLUvertex event = tess.event;
        final GLUhalfEdge e1 = reg1.eUp;
        final GLUhalfEdge e2 = reg2.eUp;
        if (e1.Sym.Org == event) {
            if (e2.Sym.Org != event) {
                return Geom.EdgeSign(e2.Sym.Org, event, e2.Org) <= 0.0;
            }
            if (Geom.VertLeq(e1.Org, e2.Org)) {
                return Geom.EdgeSign(e2.Sym.Org, e1.Org, e2.Org) <= 0.0;
            }
            return Geom.EdgeSign(e1.Sym.Org, e2.Org, e1.Org) >= 0.0;
        }
        else {
            if (e2.Sym.Org == event) {
                return Geom.EdgeSign(e1.Sym.Org, event, e1.Org) >= 0.0;
            }
            final double t1 = Geom.EdgeEval(e1.Sym.Org, event, e1.Org);
            final double t2 = Geom.EdgeEval(e2.Sym.Org, event, e2.Org);
            return t1 >= t2;
        }
    }
    
    static void DeleteRegion(final GLUtessellatorImpl tess, final ActiveRegion reg) {
        if (reg.fixUpperEdge && !Sweep.$assertionsDisabled && reg.eUp.winding != 0) {
            throw new AssertionError();
        }
        reg.eUp.activeRegion = null;
        Dict.dictDelete(tess.dict, reg.nodeUp);
    }
    
    static boolean FixUpperEdge(final ActiveRegion reg, final GLUhalfEdge newEdge) {
        assert reg.fixUpperEdge;
        if (!Mesh.__gl_meshDelete(reg.eUp)) {
            return false;
        }
        reg.fixUpperEdge = false;
        reg.eUp = newEdge;
        newEdge.activeRegion = reg;
        return true;
    }
    
    static ActiveRegion TopLeftRegion(ActiveRegion reg) {
        final GLUvertex org = reg.eUp.Org;
        do {
            reg = RegionAbove(reg);
        } while (reg.eUp.Org == org);
        if (reg.fixUpperEdge) {
            final GLUhalfEdge e = Mesh.__gl_meshConnect(RegionBelow(reg).eUp.Sym, reg.eUp.Lnext);
            if (e == null) {
                return null;
            }
            if (!FixUpperEdge(reg, e)) {
                return null;
            }
            reg = RegionAbove(reg);
        }
        return reg;
    }
    
    static ActiveRegion TopRightRegion(ActiveRegion reg) {
        final GLUvertex dst = reg.eUp.Sym.Org;
        do {
            reg = RegionAbove(reg);
        } while (reg.eUp.Sym.Org == dst);
        return reg;
    }
    
    static ActiveRegion AddRegionBelow(final GLUtessellatorImpl tess, final ActiveRegion regAbove, final GLUhalfEdge eNewUp) {
        final ActiveRegion regNew = new ActiveRegion();
        if (regNew == null) {
            throw new RuntimeException();
        }
        regNew.eUp = eNewUp;
        regNew.nodeUp = Dict.dictInsertBefore(tess.dict, regAbove.nodeUp, regNew);
        if (regNew.nodeUp == null) {
            throw new RuntimeException();
        }
        regNew.fixUpperEdge = false;
        regNew.sentinel = false;
        regNew.dirty = false;
        return eNewUp.activeRegion = regNew;
    }
    
    static boolean IsWindingInside(final GLUtessellatorImpl tess, final int n) {
        switch (tess.windingRule) {
            case 100130: {
                return (n & 0x1) != 0x0;
            }
            case 100131: {
                return n != 0;
            }
            case 100132: {
                return n > 0;
            }
            case 100133: {
                return n < 0;
            }
            case 100134: {
                return n >= 2 || n <= -2;
            }
            default: {
                throw new InternalError();
            }
        }
    }
    
    static void ComputeWinding(final GLUtessellatorImpl tess, final ActiveRegion reg) {
        reg.windingNumber = RegionAbove(reg).windingNumber + reg.eUp.winding;
        reg.inside = IsWindingInside(tess, reg.windingNumber);
    }
    
    static void FinishRegion(final GLUtessellatorImpl tess, final ActiveRegion reg) {
        final GLUhalfEdge e = reg.eUp;
        final GLUface f = e.Lface;
        f.inside = reg.inside;
        f.anEdge = e;
        DeleteRegion(tess, reg);
    }
    
    static GLUhalfEdge FinishLeftRegions(final GLUtessellatorImpl tess, final ActiveRegion regFirst, final ActiveRegion regLast) {
        ActiveRegion regPrev = regFirst;
        GLUhalfEdge ePrev = regFirst.eUp;
        while (regPrev != regLast) {
            regPrev.fixUpperEdge = false;
            final ActiveRegion reg = RegionBelow(regPrev);
            GLUhalfEdge e = reg.eUp;
            if (e.Org != ePrev.Org) {
                if (!reg.fixUpperEdge) {
                    FinishRegion(tess, regPrev);
                    break;
                }
                e = Mesh.__gl_meshConnect(ePrev.Onext.Sym, e.Sym);
                if (e == null) {
                    throw new RuntimeException();
                }
                if (!FixUpperEdge(reg, e)) {
                    throw new RuntimeException();
                }
            }
            if (ePrev.Onext != e) {
                if (!Mesh.__gl_meshSplice(e.Sym.Lnext, e)) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(ePrev, e)) {
                    throw new RuntimeException();
                }
            }
            FinishRegion(tess, regPrev);
            ePrev = reg.eUp;
            regPrev = reg;
        }
        return ePrev;
    }
    
    static void AddRightEdges(final GLUtessellatorImpl tess, final ActiveRegion regUp, final GLUhalfEdge eFirst, final GLUhalfEdge eLast, GLUhalfEdge eTopLeft, final boolean cleanUp) {
        boolean firstTime = true;
        GLUhalfEdge e = eFirst;
        while (Sweep.$assertionsDisabled || Geom.VertLeq(e.Org, e.Sym.Org)) {
            AddRegionBelow(tess, regUp, e.Sym);
            e = e.Onext;
            if (e == eLast) {
                if (eTopLeft == null) {
                    eTopLeft = RegionBelow(regUp).eUp.Sym.Onext;
                }
                ActiveRegion regPrev = regUp;
                GLUhalfEdge ePrev = eTopLeft;
                while (true) {
                    final ActiveRegion reg = RegionBelow(regPrev);
                    e = reg.eUp.Sym;
                    if (e.Org != ePrev.Org) {
                        regPrev.dirty = true;
                        assert regPrev.windingNumber - e.winding == reg.windingNumber;
                        if (cleanUp) {
                            WalkDirtyRegions(tess, regPrev);
                        }
                        return;
                    }
                    else {
                        if (e.Onext != ePrev) {
                            if (!Mesh.__gl_meshSplice(e.Sym.Lnext, e)) {
                                throw new RuntimeException();
                            }
                            if (!Mesh.__gl_meshSplice(ePrev.Sym.Lnext, e)) {
                                throw new RuntimeException();
                            }
                        }
                        reg.windingNumber = regPrev.windingNumber - e.winding;
                        reg.inside = IsWindingInside(tess, reg.windingNumber);
                        regPrev.dirty = true;
                        if (!firstTime && CheckForRightSplice(tess, regPrev)) {
                            AddWinding(e, ePrev);
                            DeleteRegion(tess, regPrev);
                            if (!Mesh.__gl_meshDelete(ePrev)) {
                                throw new RuntimeException();
                            }
                        }
                        firstTime = false;
                        regPrev = reg;
                        ePrev = e;
                    }
                }
            }
        }
        throw new AssertionError();
    }
    
    static void CallCombine(final GLUtessellatorImpl tess, final GLUvertex isect, final Object[] data, final float[] weights, final boolean needed) {
        final double[] coords = { isect.coords[0], isect.coords[1], isect.coords[2] };
        final Object[] outData = { null };
        tess.callCombineOrCombineData(coords, data, weights, outData);
        isect.data = outData[0];
        if (isect.data == null) {
            if (!needed) {
                isect.data = data[0];
            }
            else if (!tess.fatalError) {
                tess.callErrorOrErrorData(100156);
                tess.fatalError = true;
            }
        }
    }
    
    static void SpliceMergeVertices(final GLUtessellatorImpl tess, final GLUhalfEdge e1, final GLUhalfEdge e2) {
        final Object[] data = new Object[4];
        final float[] weights = { 0.5f, 0.5f, 0.0f, 0.0f };
        data[0] = e1.Org.data;
        data[1] = e2.Org.data;
        CallCombine(tess, e1.Org, data, weights, false);
        if (!Mesh.__gl_meshSplice(e1, e2)) {
            throw new RuntimeException();
        }
    }
    
    static void VertexWeights(final GLUvertex isect, final GLUvertex org, final GLUvertex dst, final float[] weights) {
        final double t1 = Geom.VertL1dist(org, isect);
        final double t2 = Geom.VertL1dist(dst, isect);
        weights[0] = (float)(0.5 * t2 / (t1 + t2));
        weights[1] = (float)(0.5 * t1 / (t1 + t2));
        final double[] coords = isect.coords;
        final int n = 0;
        coords[n] += weights[0] * org.coords[0] + weights[1] * dst.coords[0];
        final double[] coords2 = isect.coords;
        final int n2 = 1;
        coords2[n2] += weights[0] * org.coords[1] + weights[1] * dst.coords[1];
        final double[] coords3 = isect.coords;
        final int n3 = 2;
        coords3[n3] += weights[0] * org.coords[2] + weights[1] * dst.coords[2];
    }
    
    static void GetIntersectData(final GLUtessellatorImpl tess, final GLUvertex isect, final GLUvertex orgUp, final GLUvertex dstUp, final GLUvertex orgLo, final GLUvertex dstLo) {
        final Object[] data = new Object[4];
        final float[] weights = new float[4];
        final float[] weights2 = new float[2];
        final float[] weights3 = new float[2];
        data[0] = orgUp.data;
        data[1] = dstUp.data;
        data[2] = orgLo.data;
        data[3] = dstLo.data;
        final double[] coords = isect.coords;
        final int n = 0;
        final double[] coords2 = isect.coords;
        final int n2 = 1;
        final double[] coords3 = isect.coords;
        final int n3 = 2;
        final double n4 = 0.0;
        coords3[n3] = n4;
        coords[n] = (coords2[n2] = n4);
        VertexWeights(isect, orgUp, dstUp, weights2);
        VertexWeights(isect, orgLo, dstLo, weights3);
        System.arraycopy(weights2, 0, weights, 0, 2);
        System.arraycopy(weights3, 0, weights, 2, 2);
        CallCombine(tess, isect, data, weights, true);
    }
    
    static boolean CheckForRightSplice(final GLUtessellatorImpl tess, final ActiveRegion regUp) {
        final ActiveRegion regLo = RegionBelow(regUp);
        final GLUhalfEdge eUp = regUp.eUp;
        final GLUhalfEdge eLo = regLo.eUp;
        if (Geom.VertLeq(eUp.Org, eLo.Org)) {
            if (Geom.EdgeSign(eLo.Sym.Org, eUp.Org, eLo.Org) > 0.0) {
                return false;
            }
            if (!Geom.VertEq(eUp.Org, eLo.Org)) {
                if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eUp, eLo.Sym.Lnext)) {
                    throw new RuntimeException();
                }
                final ActiveRegion activeRegion = regLo;
                final boolean b = true;
                activeRegion.dirty = b;
                regUp.dirty = b;
            }
            else if (eUp.Org != eLo.Org) {
                tess.pq.pqDelete(eUp.Org.pqHandle);
                SpliceMergeVertices(tess, eLo.Sym.Lnext, eUp);
            }
        }
        else {
            if (Geom.EdgeSign(eUp.Sym.Org, eLo.Org, eUp.Org) < 0.0) {
                return false;
            }
            final ActiveRegion regionAbove = RegionAbove(regUp);
            final boolean b2 = true;
            regUp.dirty = b2;
            regionAbove.dirty = b2;
            if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eLo.Sym.Lnext, eUp)) {
                throw new RuntimeException();
            }
        }
        return true;
    }
    
    static boolean CheckForLeftSplice(final GLUtessellatorImpl tess, final ActiveRegion regUp) {
        final ActiveRegion regLo = RegionBelow(regUp);
        final GLUhalfEdge eUp = regUp.eUp;
        final GLUhalfEdge eLo = regLo.eUp;
        assert !Geom.VertEq(eUp.Sym.Org, eLo.Sym.Org);
        if (Geom.VertLeq(eUp.Sym.Org, eLo.Sym.Org)) {
            if (Geom.EdgeSign(eUp.Sym.Org, eLo.Sym.Org, eUp.Org) < 0.0) {
                return false;
            }
            final ActiveRegion regionAbove = RegionAbove(regUp);
            final boolean b = true;
            regUp.dirty = b;
            regionAbove.dirty = b;
            final GLUhalfEdge e = Mesh.__gl_meshSplitEdge(eUp);
            if (e == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eLo.Sym, e)) {
                throw new RuntimeException();
            }
            e.Lface.inside = regUp.inside;
        }
        else {
            if (Geom.EdgeSign(eLo.Sym.Org, eUp.Sym.Org, eLo.Org) > 0.0) {
                return false;
            }
            final ActiveRegion activeRegion = regLo;
            final boolean b2 = true;
            activeRegion.dirty = b2;
            regUp.dirty = b2;
            final GLUhalfEdge e = Mesh.__gl_meshSplitEdge(eLo);
            if (e == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eUp.Lnext, eLo.Sym)) {
                throw new RuntimeException();
            }
            e.Sym.Lface.inside = regUp.inside;
        }
        return true;
    }
    
    static boolean CheckForIntersect(final GLUtessellatorImpl tess, ActiveRegion regUp) {
        ActiveRegion regLo = RegionBelow(regUp);
        GLUhalfEdge eUp = regUp.eUp;
        GLUhalfEdge eLo = regLo.eUp;
        final GLUvertex orgUp = eUp.Org;
        final GLUvertex orgLo = eLo.Org;
        final GLUvertex dstUp = eUp.Sym.Org;
        final GLUvertex dstLo = eLo.Sym.Org;
        final GLUvertex isect = new GLUvertex();
        assert !Geom.VertEq(dstLo, dstUp);
        assert Geom.EdgeSign(dstUp, tess.event, orgUp) <= 0.0;
        assert Geom.EdgeSign(dstLo, tess.event, orgLo) >= 0.0;
        assert orgUp != tess.event && orgLo != tess.event;
        assert !regUp.fixUpperEdge && !regLo.fixUpperEdge;
        if (orgUp == orgLo) {
            return false;
        }
        final double tMinUp = Math.min(orgUp.t, dstUp.t);
        final double tMaxLo = Math.max(orgLo.t, dstLo.t);
        if (tMinUp > tMaxLo) {
            return false;
        }
        if (Geom.VertLeq(orgUp, orgLo)) {
            if (Geom.EdgeSign(dstLo, orgUp, orgLo) > 0.0) {
                return false;
            }
        }
        else if (Geom.EdgeSign(dstUp, orgLo, orgUp) < 0.0) {
            return false;
        }
        DebugEvent(tess);
        Geom.EdgeIntersect(dstUp, orgUp, dstLo, orgLo, isect);
        assert Math.min(orgUp.t, dstUp.t) <= isect.t;
        assert isect.t <= Math.max(orgLo.t, dstLo.t);
        assert Math.min(dstLo.s, dstUp.s) <= isect.s;
        assert isect.s <= Math.max(orgLo.s, orgUp.s);
        if (Geom.VertLeq(isect, tess.event)) {
            isect.s = tess.event.s;
            isect.t = tess.event.t;
        }
        final GLUvertex orgMin = Geom.VertLeq(orgUp, orgLo) ? orgUp : orgLo;
        if (Geom.VertLeq(orgMin, isect)) {
            isect.s = orgMin.s;
            isect.t = orgMin.t;
        }
        if (Geom.VertEq(isect, orgUp) || Geom.VertEq(isect, orgLo)) {
            CheckForRightSplice(tess, regUp);
            return false;
        }
        if ((!Geom.VertEq(dstUp, tess.event) && Geom.EdgeSign(dstUp, tess.event, isect) >= 0.0) || (!Geom.VertEq(dstLo, tess.event) && Geom.EdgeSign(dstLo, tess.event, isect) <= 0.0)) {
            if (dstLo == tess.event) {
                if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eLo.Sym, eUp)) {
                    throw new RuntimeException();
                }
                regUp = TopLeftRegion(regUp);
                if (regUp == null) {
                    throw new RuntimeException();
                }
                eUp = RegionBelow(regUp).eUp;
                FinishLeftRegions(tess, RegionBelow(regUp), regLo);
                AddRightEdges(tess, regUp, eUp.Sym.Lnext, eUp, eUp, true);
                return true;
            }
            else {
                if (dstUp != tess.event) {
                    if (Geom.EdgeSign(dstUp, tess.event, isect) >= 0.0) {
                        final ActiveRegion regionAbove = RegionAbove(regUp);
                        final ActiveRegion activeRegion = regUp;
                        final boolean b = true;
                        activeRegion.dirty = b;
                        regionAbove.dirty = b;
                        if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                            throw new RuntimeException();
                        }
                        eUp.Org.s = tess.event.s;
                        eUp.Org.t = tess.event.t;
                    }
                    if (Geom.EdgeSign(dstLo, tess.event, isect) <= 0.0) {
                        final ActiveRegion activeRegion2 = regUp;
                        final ActiveRegion activeRegion3 = regLo;
                        final boolean b2 = true;
                        activeRegion3.dirty = b2;
                        activeRegion2.dirty = b2;
                        if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                            throw new RuntimeException();
                        }
                        eLo.Org.s = tess.event.s;
                        eLo.Org.t = tess.event.t;
                    }
                    return false;
                }
                if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                    throw new RuntimeException();
                }
                if (!Mesh.__gl_meshSplice(eUp.Lnext, eLo.Sym.Lnext)) {
                    throw new RuntimeException();
                }
                regLo = regUp;
                regUp = TopRightRegion(regUp);
                final GLUhalfEdge e = RegionBelow(regUp).eUp.Sym.Onext;
                regLo.eUp = eLo.Sym.Lnext;
                eLo = FinishLeftRegions(tess, regLo, null);
                AddRightEdges(tess, regUp, eLo.Onext, eUp.Sym.Onext, e, true);
                return true;
            }
        }
        else {
            if (Mesh.__gl_meshSplitEdge(eUp.Sym) == null) {
                throw new RuntimeException();
            }
            if (Mesh.__gl_meshSplitEdge(eLo.Sym) == null) {
                throw new RuntimeException();
            }
            if (!Mesh.__gl_meshSplice(eLo.Sym.Lnext, eUp)) {
                throw new RuntimeException();
            }
            eUp.Org.s = isect.s;
            eUp.Org.t = isect.t;
            eUp.Org.pqHandle = tess.pq.pqInsert(eUp.Org);
            if (eUp.Org.pqHandle == Long.MAX_VALUE) {
                tess.pq.pqDeletePriorityQ();
                tess.pq = null;
                throw new RuntimeException();
            }
            GetIntersectData(tess, eUp.Org, orgUp, dstUp, orgLo, dstLo);
            final ActiveRegion regionAbove2 = RegionAbove(regUp);
            final ActiveRegion activeRegion4 = regUp;
            final ActiveRegion activeRegion5 = regLo;
            final boolean dirty = true;
            activeRegion5.dirty = dirty;
            activeRegion4.dirty = dirty;
            regionAbove2.dirty = dirty;
            return false;
        }
    }
    
    static void WalkDirtyRegions(final GLUtessellatorImpl tess, ActiveRegion regUp) {
        ActiveRegion regLo = RegionBelow(regUp);
        while (true) {
            if (regLo.dirty) {
                regUp = regLo;
                regLo = RegionBelow(regLo);
            }
            else {
                if (!regUp.dirty) {
                    regLo = regUp;
                    regUp = RegionAbove(regUp);
                    if (regUp == null || !regUp.dirty) {
                        return;
                    }
                }
                regUp.dirty = false;
                GLUhalfEdge eUp = regUp.eUp;
                GLUhalfEdge eLo = regLo.eUp;
                if (eUp.Sym.Org != eLo.Sym.Org && CheckForLeftSplice(tess, regUp)) {
                    if (regLo.fixUpperEdge) {
                        DeleteRegion(tess, regLo);
                        if (!Mesh.__gl_meshDelete(eLo)) {
                            throw new RuntimeException();
                        }
                        regLo = RegionBelow(regUp);
                        eLo = regLo.eUp;
                    }
                    else if (regUp.fixUpperEdge) {
                        DeleteRegion(tess, regUp);
                        if (!Mesh.__gl_meshDelete(eUp)) {
                            throw new RuntimeException();
                        }
                        regUp = RegionAbove(regLo);
                        eUp = regUp.eUp;
                    }
                }
                if (eUp.Org != eLo.Org) {
                    if (eUp.Sym.Org != eLo.Sym.Org && !regUp.fixUpperEdge && !regLo.fixUpperEdge && (eUp.Sym.Org == tess.event || eLo.Sym.Org == tess.event)) {
                        if (CheckForIntersect(tess, regUp)) {
                            return;
                        }
                    }
                    else {
                        CheckForRightSplice(tess, regUp);
                    }
                }
                if (eUp.Org != eLo.Org || eUp.Sym.Org != eLo.Sym.Org) {
                    continue;
                }
                AddWinding(eLo, eUp);
                DeleteRegion(tess, regUp);
                if (!Mesh.__gl_meshDelete(eUp)) {
                    throw new RuntimeException();
                }
                regUp = RegionAbove(regLo);
            }
        }
    }
    
    static void ConnectRightVertex(final GLUtessellatorImpl tess, ActiveRegion regUp, GLUhalfEdge eBottomLeft) {
        GLUhalfEdge eTopLeft = eBottomLeft.Onext;
        final ActiveRegion regLo = RegionBelow(regUp);
        final GLUhalfEdge eUp = regUp.eUp;
        final GLUhalfEdge eLo = regLo.eUp;
        boolean degenerate = false;
        if (eUp.Sym.Org != eLo.Sym.Org) {
            CheckForIntersect(tess, regUp);
        }
        if (Geom.VertEq(eUp.Org, tess.event)) {
            if (!Mesh.__gl_meshSplice(eTopLeft.Sym.Lnext, eUp)) {
                throw new RuntimeException();
            }
            regUp = TopLeftRegion(regUp);
            if (regUp == null) {
                throw new RuntimeException();
            }
            eTopLeft = RegionBelow(regUp).eUp;
            FinishLeftRegions(tess, RegionBelow(regUp), regLo);
            degenerate = true;
        }
        if (Geom.VertEq(eLo.Org, tess.event)) {
            if (!Mesh.__gl_meshSplice(eBottomLeft, eLo.Sym.Lnext)) {
                throw new RuntimeException();
            }
            eBottomLeft = FinishLeftRegions(tess, regLo, null);
            degenerate = true;
        }
        if (degenerate) {
            AddRightEdges(tess, regUp, eBottomLeft.Onext, eTopLeft, eTopLeft, true);
            return;
        }
        GLUhalfEdge eNew;
        if (Geom.VertLeq(eLo.Org, eUp.Org)) {
            eNew = eLo.Sym.Lnext;
        }
        else {
            eNew = eUp;
        }
        eNew = Mesh.__gl_meshConnect(eBottomLeft.Onext.Sym, eNew);
        if (eNew == null) {
            throw new RuntimeException();
        }
        AddRightEdges(tess, regUp, eNew, eNew.Onext, eNew.Onext, false);
        eNew.Sym.activeRegion.fixUpperEdge = true;
        WalkDirtyRegions(tess, regUp);
    }
    
    static void ConnectLeftDegenerate(final GLUtessellatorImpl tess, ActiveRegion regUp, final GLUvertex vEvent) {
        final GLUhalfEdge e = regUp.eUp;
        if (Geom.VertEq(e.Org, vEvent)) {
            assert false;
            SpliceMergeVertices(tess, e, vEvent.anEdge);
        }
        else if (!Geom.VertEq(e.Sym.Org, vEvent)) {
            if (Mesh.__gl_meshSplitEdge(e.Sym) == null) {
                throw new RuntimeException();
            }
            if (regUp.fixUpperEdge) {
                if (!Mesh.__gl_meshDelete(e.Onext)) {
                    throw new RuntimeException();
                }
                regUp.fixUpperEdge = false;
            }
            if (!Mesh.__gl_meshSplice(vEvent.anEdge, e)) {
                throw new RuntimeException();
            }
            SweepEvent(tess, vEvent);
        }
        else {
            assert false;
            regUp = TopRightRegion(regUp);
            final ActiveRegion reg = RegionBelow(regUp);
            GLUhalfEdge eTopRight = reg.eUp.Sym;
            GLUhalfEdge eTopLeft;
            final GLUhalfEdge eLast = eTopLeft = eTopRight.Onext;
            if (reg.fixUpperEdge) {
                assert eTopLeft != eTopRight;
                DeleteRegion(tess, reg);
                if (!Mesh.__gl_meshDelete(eTopRight)) {
                    throw new RuntimeException();
                }
                eTopRight = eTopLeft.Sym.Lnext;
            }
            if (!Mesh.__gl_meshSplice(vEvent.anEdge, eTopRight)) {
                throw new RuntimeException();
            }
            if (!Geom.EdgeGoesLeft(eTopLeft)) {
                eTopLeft = null;
            }
            AddRightEdges(tess, regUp, eTopRight.Onext, eLast, eTopLeft, true);
        }
    }
    
    static void ConnectLeftVertex(final GLUtessellatorImpl tess, final GLUvertex vEvent) {
        final ActiveRegion tmp = new ActiveRegion();
        tmp.eUp = vEvent.anEdge.Sym;
        final ActiveRegion regUp = (ActiveRegion)Dict.dictKey(Dict.dictSearch(tess.dict, tmp));
        final ActiveRegion regLo = RegionBelow(regUp);
        final GLUhalfEdge eUp = regUp.eUp;
        final GLUhalfEdge eLo = regLo.eUp;
        if (Geom.EdgeSign(eUp.Sym.Org, vEvent, eUp.Org) == 0.0) {
            ConnectLeftDegenerate(tess, regUp, vEvent);
            return;
        }
        final ActiveRegion reg = Geom.VertLeq(eLo.Sym.Org, eUp.Sym.Org) ? regUp : regLo;
        if (regUp.inside || reg.fixUpperEdge) {
            GLUhalfEdge eNew;
            if (reg == regUp) {
                eNew = Mesh.__gl_meshConnect(vEvent.anEdge.Sym, eUp.Lnext);
                if (eNew == null) {
                    throw new RuntimeException();
                }
            }
            else {
                final GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(eLo.Sym.Onext.Sym, vEvent.anEdge);
                if (tempHalfEdge == null) {
                    throw new RuntimeException();
                }
                eNew = tempHalfEdge.Sym;
            }
            if (reg.fixUpperEdge) {
                if (!FixUpperEdge(reg, eNew)) {
                    throw new RuntimeException();
                }
            }
            else {
                ComputeWinding(tess, AddRegionBelow(tess, regUp, eNew));
            }
            SweepEvent(tess, vEvent);
        }
        else {
            AddRightEdges(tess, regUp, vEvent.anEdge, vEvent.anEdge, null, true);
        }
    }
    
    static void SweepEvent(final GLUtessellatorImpl tess, final GLUvertex vEvent) {
        tess.event = vEvent;
        DebugEvent(tess);
        GLUhalfEdge e = vEvent.anEdge;
        while (e.activeRegion == null) {
            e = e.Onext;
            if (e == vEvent.anEdge) {
                ConnectLeftVertex(tess, vEvent);
                return;
            }
        }
        final ActiveRegion regUp = TopLeftRegion(e.activeRegion);
        if (regUp == null) {
            throw new RuntimeException();
        }
        final ActiveRegion reg = RegionBelow(regUp);
        final GLUhalfEdge eTopLeft = reg.eUp;
        final GLUhalfEdge eBottomLeft = FinishLeftRegions(tess, reg, null);
        if (eBottomLeft.Onext == eTopLeft) {
            ConnectRightVertex(tess, regUp, eBottomLeft);
        }
        else {
            AddRightEdges(tess, regUp, eBottomLeft.Onext, eTopLeft, eTopLeft, true);
        }
    }
    
    static void AddSentinel(final GLUtessellatorImpl tess, final double t) {
        final ActiveRegion reg = new ActiveRegion();
        if (reg == null) {
            throw new RuntimeException();
        }
        final GLUhalfEdge e = Mesh.__gl_meshMakeEdge(tess.mesh);
        if (e == null) {
            throw new RuntimeException();
        }
        e.Org.s = 4.0E150;
        e.Org.t = t;
        e.Sym.Org.s = -4.0E150;
        e.Sym.Org.t = t;
        tess.event = e.Sym.Org;
        reg.eUp = e;
        reg.windingNumber = 0;
        reg.inside = false;
        reg.fixUpperEdge = false;
        reg.sentinel = true;
        reg.dirty = false;
        reg.nodeUp = Dict.dictInsert(tess.dict, reg);
        if (reg.nodeUp == null) {
            throw new RuntimeException();
        }
    }
    
    static void InitEdgeDict(final GLUtessellatorImpl tess) {
        tess.dict = Dict.dictNewDict(tess, new Dict.DictLeq() {
            public boolean leq(final Object frame, final Object key1, final Object key2) {
                return Sweep.EdgeLeq(tess, (ActiveRegion)key1, (ActiveRegion)key2);
            }
        });
        if (tess.dict == null) {
            throw new RuntimeException();
        }
        AddSentinel(tess, -4.0E150);
        AddSentinel(tess, 4.0E150);
    }
    
    static void DoneEdgeDict(final GLUtessellatorImpl tess) {
        int fixedEdges = 0;
        ActiveRegion reg;
        while ((reg = (ActiveRegion)Dict.dictKey(Dict.dictMin(tess.dict))) != null) {
            if (!reg.sentinel) {
                assert reg.fixUpperEdge;
                assert ++fixedEdges == 1;
            }
            assert reg.windingNumber == 0;
            DeleteRegion(tess, reg);
        }
        Dict.dictDeleteDict(tess.dict);
    }
    
    static void RemoveDegenerateEdges(final GLUtessellatorImpl tess) {
        GLUhalfEdge eNext;
        for (GLUhalfEdge eHead = tess.mesh.eHead, e = eHead.next; e != eHead; e = eNext) {
            eNext = e.next;
            GLUhalfEdge eLnext = e.Lnext;
            if (Geom.VertEq(e.Org, e.Sym.Org) && e.Lnext.Lnext != e) {
                SpliceMergeVertices(tess, eLnext, e);
                if (!Mesh.__gl_meshDelete(e)) {
                    throw new RuntimeException();
                }
                e = eLnext;
                eLnext = e.Lnext;
            }
            if (eLnext.Lnext == e) {
                if (eLnext != e) {
                    if (eLnext == eNext || eLnext == eNext.Sym) {
                        eNext = eNext.next;
                    }
                    if (!Mesh.__gl_meshDelete(eLnext)) {
                        throw new RuntimeException();
                    }
                }
                if (e == eNext || e == eNext.Sym) {
                    eNext = eNext.next;
                }
                if (!Mesh.__gl_meshDelete(e)) {
                    throw new RuntimeException();
                }
            }
        }
    }
    
    static boolean InitPriorityQ(final GLUtessellatorImpl tess) {
        final PriorityQ pqNewPriorityQ = PriorityQ.pqNewPriorityQ(new PriorityQ.Leq() {
            public boolean leq(final Object key1, final Object key2) {
                return Geom.VertLeq((GLUvertex)key1, (GLUvertex)key2);
            }
        });
        tess.pq = pqNewPriorityQ;
        final PriorityQ pq = pqNewPriorityQ;
        if (pq == null) {
            return false;
        }
        GLUvertex vHead;
        GLUvertex v;
        for (vHead = tess.mesh.vHead, v = vHead.next; v != vHead; v = v.next) {
            v.pqHandle = pq.pqInsert(v);
            if (v.pqHandle == Long.MAX_VALUE) {
                break;
            }
        }
        if (v != vHead || !pq.pqInit()) {
            tess.pq.pqDeletePriorityQ();
            tess.pq = null;
            return false;
        }
        return true;
    }
    
    static void DonePriorityQ(final GLUtessellatorImpl tess) {
        tess.pq.pqDeletePriorityQ();
    }
    
    static boolean RemoveDegenerateFaces(final GLUmesh mesh) {
        GLUface fNext;
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = fNext) {
            fNext = f.next;
            final GLUhalfEdge e = f.anEdge;
            assert e.Lnext != e;
            if (e.Lnext.Lnext == e) {
                AddWinding(e.Onext, e);
                if (!Mesh.__gl_meshDelete(e)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean __gl_computeInterior(final GLUtessellatorImpl tess) {
        tess.fatalError = false;
        RemoveDegenerateEdges(tess);
        if (!InitPriorityQ(tess)) {
            return false;
        }
        InitEdgeDict(tess);
        GLUvertex v;
        while ((v = (GLUvertex)tess.pq.pqExtractMin()) != null) {
            while (true) {
                GLUvertex vNext = (GLUvertex)tess.pq.pqMinimum();
                if (vNext == null || !Geom.VertEq(vNext, v)) {
                    break;
                }
                vNext = (GLUvertex)tess.pq.pqExtractMin();
                SpliceMergeVertices(tess, v.anEdge, vNext.anEdge);
            }
            SweepEvent(tess, v);
        }
        tess.event = ((ActiveRegion)Dict.dictKey(Dict.dictMin(tess.dict))).eUp.Org;
        DebugEvent(tess);
        DoneEdgeDict(tess);
        DonePriorityQ(tess);
        if (!RemoveDegenerateFaces(tess.mesh)) {
            return false;
        }
        Mesh.__gl_meshCheckMesh(tess.mesh);
        return true;
    }
}
