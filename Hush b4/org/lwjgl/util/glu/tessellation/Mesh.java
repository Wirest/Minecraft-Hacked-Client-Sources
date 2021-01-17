// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Mesh
{
    static final /* synthetic */ boolean $assertionsDisabled;
    
    private Mesh() {
    }
    
    static GLUhalfEdge MakeEdge(GLUhalfEdge eNext) {
        final GLUhalfEdge e = new GLUhalfEdge(true);
        final GLUhalfEdge eSym = new GLUhalfEdge(false);
        if (!eNext.first) {
            eNext = eNext.Sym;
        }
        final GLUhalfEdge ePrev = eNext.Sym.next;
        eSym.next = ePrev;
        ePrev.Sym.next = e;
        e.next = eNext;
        eNext.Sym.next = eSym;
        e.Sym = eSym;
        e.Onext = e;
        e.Lnext = eSym;
        e.Org = null;
        e.Lface = null;
        e.winding = 0;
        e.activeRegion = null;
        eSym.Sym = e;
        eSym.Onext = eSym;
        eSym.Lnext = e;
        eSym.Org = null;
        eSym.Lface = null;
        eSym.winding = 0;
        eSym.activeRegion = null;
        return e;
    }
    
    static void Splice(final GLUhalfEdge a, final GLUhalfEdge b) {
        final GLUhalfEdge aOnext = a.Onext;
        final GLUhalfEdge bOnext = b.Onext;
        aOnext.Sym.Lnext = b;
        bOnext.Sym.Lnext = a;
        a.Onext = bOnext;
        b.Onext = aOnext;
    }
    
    static void MakeVertex(final GLUvertex newVertex, final GLUhalfEdge eOrig, final GLUvertex vNext) {
        final GLUvertex vNew = newVertex;
        assert vNew != null;
        final GLUvertex vPrev = vNext.prev;
        vNew.prev = vPrev;
        vPrev.next = vNew;
        vNew.next = vNext;
        vNext.prev = vNew;
        vNew.anEdge = eOrig;
        vNew.data = null;
        GLUhalfEdge e = eOrig;
        do {
            e.Org = vNew;
            e = e.Onext;
        } while (e != eOrig);
    }
    
    static void MakeFace(final GLUface newFace, final GLUhalfEdge eOrig, final GLUface fNext) {
        final GLUface fNew = newFace;
        assert fNew != null;
        final GLUface fPrev = fNext.prev;
        fNew.prev = fPrev;
        fPrev.next = fNew;
        fNew.next = fNext;
        fNext.prev = fNew;
        fNew.anEdge = eOrig;
        fNew.data = null;
        fNew.trail = null;
        fNew.marked = false;
        fNew.inside = fNext.inside;
        GLUhalfEdge e = eOrig;
        do {
            e.Lface = fNew;
            e = e.Lnext;
        } while (e != eOrig);
    }
    
    static void KillEdge(GLUhalfEdge eDel) {
        if (!eDel.first) {
            eDel = eDel.Sym;
        }
        final GLUhalfEdge eNext = eDel.next;
        final GLUhalfEdge ePrev = eDel.Sym.next;
        eNext.Sym.next = ePrev;
        ePrev.Sym.next = eNext;
    }
    
    static void KillVertex(final GLUvertex vDel, final GLUvertex newOrg) {
        GLUhalfEdge e;
        final GLUhalfEdge eStart = e = vDel.anEdge;
        do {
            e.Org = newOrg;
            e = e.Onext;
        } while (e != eStart);
        final GLUvertex vPrev = vDel.prev;
        final GLUvertex vNext = vDel.next;
        vNext.prev = vPrev;
        vPrev.next = vNext;
    }
    
    static void KillFace(final GLUface fDel, final GLUface newLface) {
        GLUhalfEdge e;
        final GLUhalfEdge eStart = e = fDel.anEdge;
        do {
            e.Lface = newLface;
            e = e.Lnext;
        } while (e != eStart);
        final GLUface fPrev = fDel.prev;
        final GLUface fNext = fDel.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }
    
    public static GLUhalfEdge __gl_meshMakeEdge(final GLUmesh mesh) {
        final GLUvertex newVertex1 = new GLUvertex();
        final GLUvertex newVertex2 = new GLUvertex();
        final GLUface newFace = new GLUface();
        final GLUhalfEdge e = MakeEdge(mesh.eHead);
        if (e == null) {
            return null;
        }
        MakeVertex(newVertex1, e, mesh.vHead);
        MakeVertex(newVertex2, e.Sym, mesh.vHead);
        MakeFace(newFace, e, mesh.fHead);
        return e;
    }
    
    public static boolean __gl_meshSplice(final GLUhalfEdge eOrg, final GLUhalfEdge eDst) {
        boolean joiningLoops = false;
        boolean joiningVertices = false;
        if (eOrg == eDst) {
            return true;
        }
        if (eDst.Org != eOrg.Org) {
            joiningVertices = true;
            KillVertex(eDst.Org, eOrg.Org);
        }
        if (eDst.Lface != eOrg.Lface) {
            joiningLoops = true;
            KillFace(eDst.Lface, eOrg.Lface);
        }
        Splice(eDst, eOrg);
        if (!joiningVertices) {
            final GLUvertex newVertex = new GLUvertex();
            MakeVertex(newVertex, eDst, eOrg.Org);
            eOrg.Org.anEdge = eOrg;
        }
        if (!joiningLoops) {
            final GLUface newFace = new GLUface();
            MakeFace(newFace, eDst, eOrg.Lface);
            eOrg.Lface.anEdge = eOrg;
        }
        return true;
    }
    
    static boolean __gl_meshDelete(final GLUhalfEdge eDel) {
        final GLUhalfEdge eDelSym = eDel.Sym;
        boolean joiningLoops = false;
        if (eDel.Lface != eDel.Sym.Lface) {
            joiningLoops = true;
            KillFace(eDel.Lface, eDel.Sym.Lface);
        }
        if (eDel.Onext == eDel) {
            KillVertex(eDel.Org, null);
        }
        else {
            eDel.Sym.Lface.anEdge = eDel.Sym.Lnext;
            eDel.Org.anEdge = eDel.Onext;
            Splice(eDel, eDel.Sym.Lnext);
            if (!joiningLoops) {
                final GLUface newFace = new GLUface();
                MakeFace(newFace, eDel, eDel.Lface);
            }
        }
        if (eDelSym.Onext == eDelSym) {
            KillVertex(eDelSym.Org, null);
            KillFace(eDelSym.Lface, null);
        }
        else {
            eDel.Lface.anEdge = eDelSym.Sym.Lnext;
            eDelSym.Org.anEdge = eDelSym.Onext;
            Splice(eDelSym, eDelSym.Sym.Lnext);
        }
        KillEdge(eDel);
        return true;
    }
    
    static GLUhalfEdge __gl_meshAddEdgeVertex(final GLUhalfEdge eOrg) {
        final GLUhalfEdge eNew = MakeEdge(eOrg);
        final GLUhalfEdge eNewSym = eNew.Sym;
        Splice(eNew, eOrg.Lnext);
        eNew.Org = eOrg.Sym.Org;
        final GLUvertex newVertex = new GLUvertex();
        MakeVertex(newVertex, eNewSym, eNew.Org);
        final GLUhalfEdge glUhalfEdge = eNew;
        final GLUhalfEdge glUhalfEdge2 = eNewSym;
        final GLUface lface = eOrg.Lface;
        glUhalfEdge2.Lface = lface;
        glUhalfEdge.Lface = lface;
        return eNew;
    }
    
    public static GLUhalfEdge __gl_meshSplitEdge(final GLUhalfEdge eOrg) {
        final GLUhalfEdge tempHalfEdge = __gl_meshAddEdgeVertex(eOrg);
        final GLUhalfEdge eNew = tempHalfEdge.Sym;
        Splice(eOrg.Sym, eOrg.Sym.Sym.Lnext);
        Splice(eOrg.Sym, eNew);
        eOrg.Sym.Org = eNew.Org;
        eNew.Sym.Org.anEdge = eNew.Sym;
        eNew.Sym.Lface = eOrg.Sym.Lface;
        eNew.winding = eOrg.winding;
        eNew.Sym.winding = eOrg.Sym.winding;
        return eNew;
    }
    
    static GLUhalfEdge __gl_meshConnect(final GLUhalfEdge eOrg, final GLUhalfEdge eDst) {
        boolean joiningLoops = false;
        final GLUhalfEdge eNew = MakeEdge(eOrg);
        final GLUhalfEdge eNewSym = eNew.Sym;
        if (eDst.Lface != eOrg.Lface) {
            joiningLoops = true;
            KillFace(eDst.Lface, eOrg.Lface);
        }
        Splice(eNew, eOrg.Lnext);
        Splice(eNewSym, eDst);
        eNew.Org = eOrg.Sym.Org;
        eNewSym.Org = eDst.Org;
        final GLUhalfEdge glUhalfEdge = eNew;
        final GLUhalfEdge glUhalfEdge2 = eNewSym;
        final GLUface lface = eOrg.Lface;
        glUhalfEdge2.Lface = lface;
        glUhalfEdge.Lface = lface;
        eOrg.Lface.anEdge = eNewSym;
        if (!joiningLoops) {
            final GLUface newFace = new GLUface();
            MakeFace(newFace, eNew, eOrg.Lface);
        }
        return eNew;
    }
    
    static void __gl_meshZapFace(final GLUface fZap) {
        final GLUhalfEdge eStart = fZap.anEdge;
        GLUhalfEdge eNext = eStart.Lnext;
        GLUhalfEdge e;
        do {
            e = eNext;
            eNext = e.Lnext;
            e.Lface = null;
            if (e.Sym.Lface == null) {
                if (e.Onext == e) {
                    KillVertex(e.Org, null);
                }
                else {
                    e.Org.anEdge = e.Onext;
                    Splice(e, e.Sym.Lnext);
                }
                final GLUhalfEdge eSym = e.Sym;
                if (eSym.Onext == eSym) {
                    KillVertex(eSym.Org, null);
                }
                else {
                    eSym.Org.anEdge = eSym.Onext;
                    Splice(eSym, eSym.Sym.Lnext);
                }
                KillEdge(e);
            }
        } while (e != eStart);
        final GLUface fPrev = fZap.prev;
        final GLUface fNext = fZap.next;
        fNext.prev = fPrev;
        fPrev.next = fNext;
    }
    
    public static GLUmesh __gl_meshNewMesh() {
        final GLUmesh mesh = new GLUmesh();
        final GLUvertex v = mesh.vHead;
        final GLUface f = mesh.fHead;
        final GLUhalfEdge e = mesh.eHead;
        final GLUhalfEdge eSym = mesh.eHeadSym;
        final GLUvertex glUvertex = v;
        final GLUvertex glUvertex2 = v;
        final GLUvertex glUvertex3 = v;
        glUvertex2.prev = glUvertex3;
        glUvertex.next = glUvertex3;
        v.anEdge = null;
        v.data = null;
        final GLUface glUface = f;
        final GLUface glUface2 = f;
        final GLUface glUface3 = f;
        glUface2.prev = glUface3;
        glUface.next = glUface3;
        f.anEdge = null;
        f.data = null;
        f.trail = null;
        f.marked = false;
        f.inside = false;
        e.next = e;
        e.Sym = eSym;
        e.Onext = null;
        e.Lnext = null;
        e.Org = null;
        e.Lface = null;
        e.winding = 0;
        e.activeRegion = null;
        eSym.next = eSym;
        eSym.Sym = e;
        eSym.Onext = null;
        eSym.Lnext = null;
        eSym.Org = null;
        eSym.Lface = null;
        eSym.winding = 0;
        eSym.activeRegion = null;
        return mesh;
    }
    
    static GLUmesh __gl_meshUnion(final GLUmesh mesh1, final GLUmesh mesh2) {
        final GLUface f1 = mesh1.fHead;
        final GLUvertex v1 = mesh1.vHead;
        final GLUhalfEdge e1 = mesh1.eHead;
        final GLUface f2 = mesh2.fHead;
        final GLUvertex v2 = mesh2.vHead;
        final GLUhalfEdge e2 = mesh2.eHead;
        if (f2.next != f2) {
            f1.prev.next = f2.next;
            f2.next.prev = f1.prev;
            f2.prev.next = f1;
            f1.prev = f2.prev;
        }
        if (v2.next != v2) {
            v1.prev.next = v2.next;
            v2.next.prev = v1.prev;
            v2.prev.next = v1;
            v1.prev = v2.prev;
        }
        if (e2.next != e2) {
            e1.Sym.next.Sym.next = e2.next;
            e2.next.Sym.next = e1.Sym.next;
            e2.Sym.next.Sym.next = e1;
            e1.Sym.next = e2.Sym.next;
        }
        return mesh1;
    }
    
    static void __gl_meshDeleteMeshZap(final GLUmesh mesh) {
        final GLUface fHead = mesh.fHead;
        while (fHead.next != fHead) {
            __gl_meshZapFace(fHead.next);
        }
        assert mesh.vHead.next == mesh.vHead;
    }
    
    public static void __gl_meshDeleteMesh(final GLUmesh mesh) {
        GLUface fNext;
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; fNext = (f = f.next)) {}
        GLUvertex vNext;
        for (GLUvertex v = mesh.vHead.next; v != mesh.vHead; vNext = (v = v.next)) {}
        GLUhalfEdge eNext;
        for (GLUhalfEdge e = mesh.eHead.next; e != mesh.eHead; eNext = (e = e.next)) {}
    }
    
    public static void __gl_meshCheckMesh(final GLUmesh mesh) {
        final GLUface fHead = mesh.fHead;
        final GLUvertex vHead = mesh.vHead;
        final GLUhalfEdge eHead = mesh.eHead;
        GLUface fPrev = fHead;
        fPrev = fHead;
        GLUface f = null;
    Label_0021:
        while ((f = fPrev.next) != fHead) {
            assert f.prev == fPrev;
            GLUhalfEdge e = f.anEdge;
            while (Mesh.$assertionsDisabled || e.Sym != e) {
                assert e.Sym.Sym == e;
                assert e.Lnext.Onext.Sym == e;
                assert e.Onext.Sym.Lnext == e;
                assert e.Lface == f;
                e = e.Lnext;
                if (e == f.anEdge) {
                    fPrev = f;
                    continue Label_0021;
                }
            }
            throw new AssertionError();
        }
        assert f.prev == fPrev && f.anEdge == null && f.data == null;
        GLUvertex vPrev = vHead;
        vPrev = vHead;
        GLUvertex v;
        Label_0269:
        while ((v = vPrev.next) != vHead) {
            assert v.prev == vPrev;
            GLUhalfEdge e = v.anEdge;
            while (Mesh.$assertionsDisabled || e.Sym != e) {
                assert e.Sym.Sym == e;
                assert e.Lnext.Onext.Sym == e;
                assert e.Onext.Sym.Lnext == e;
                assert e.Org == v;
                e = e.Onext;
                if (e == v.anEdge) {
                    vPrev = v;
                    continue Label_0269;
                }
            }
            throw new AssertionError();
        }
        assert v.prev == vPrev && v.anEdge == null && v.data == null;
        GLUhalfEdge e;
        GLUhalfEdge ePrev;
        for (ePrev = eHead, ePrev = eHead; (e = ePrev.next) != eHead; ePrev = e) {
            assert e.Sym.next == ePrev.Sym;
            assert e.Sym != e;
            assert e.Sym.Sym == e;
            assert e.Org != null;
            assert e.Sym.Org != null;
            assert e.Lnext.Onext.Sym == e;
            assert e.Onext.Sym.Lnext == e;
        }
        assert e.Sym.next == ePrev.Sym && e.Sym == mesh.eHeadSym && e.Sym.Sym == e && e.Org == null && e.Sym.Org == null && e.Lface == null && e.Sym.Lface == null;
    }
}
