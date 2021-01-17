// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class TessMono
{
    static boolean __gl_meshTessellateMonoRegion(final GLUface face) {
        GLUhalfEdge up = face.anEdge;
        assert up.Lnext != up && up.Lnext.Lnext != up;
        while (Geom.VertLeq(up.Sym.Org, up.Org)) {
            up = up.Onext.Sym;
        }
        while (Geom.VertLeq(up.Org, up.Sym.Org)) {
            up = up.Lnext;
        }
        GLUhalfEdge lo = up.Onext.Sym;
        while (up.Lnext != lo) {
            if (Geom.VertLeq(up.Sym.Org, lo.Org)) {
                while (lo.Lnext != up && (Geom.EdgeGoesLeft(lo.Lnext) || Geom.EdgeSign(lo.Org, lo.Sym.Org, lo.Lnext.Sym.Org) <= 0.0)) {
                    final GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
                    if (tempHalfEdge == null) {
                        return false;
                    }
                    lo = tempHalfEdge.Sym;
                }
                lo = lo.Onext.Sym;
            }
            else {
                while (lo.Lnext != up && (Geom.EdgeGoesRight(up.Onext.Sym) || Geom.EdgeSign(up.Sym.Org, up.Org, up.Onext.Sym.Org) >= 0.0)) {
                    final GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(up, up.Onext.Sym);
                    if (tempHalfEdge == null) {
                        return false;
                    }
                    up = tempHalfEdge.Sym;
                }
                up = up.Lnext;
            }
        }
        assert lo.Lnext != up;
        while (lo.Lnext.Lnext != up) {
            final GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
            if (tempHalfEdge == null) {
                return false;
            }
            lo = tempHalfEdge.Sym;
        }
        return true;
    }
    
    public static boolean __gl_meshTessellateInterior(final GLUmesh mesh) {
        GLUface next;
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = next) {
            next = f.next;
            if (f.inside && !__gl_meshTessellateMonoRegion(f)) {
                return false;
            }
        }
        return true;
    }
    
    public static void __gl_meshDiscardExterior(final GLUmesh mesh) {
        GLUface next;
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = next) {
            next = f.next;
            if (!f.inside) {
                Mesh.__gl_meshZapFace(f);
            }
        }
    }
    
    public static boolean __gl_meshSetWindingNumber(final GLUmesh mesh, final int value, final boolean keepOnlyBoundary) {
        GLUhalfEdge eNext;
        for (GLUhalfEdge e = mesh.eHead.next; e != mesh.eHead; e = eNext) {
            eNext = e.next;
            if (e.Sym.Lface.inside != e.Lface.inside) {
                e.winding = (e.Lface.inside ? value : (-value));
            }
            else if (!keepOnlyBoundary) {
                e.winding = 0;
            }
            else if (!Mesh.__gl_meshDelete(e)) {
                return false;
            }
        }
        return true;
    }
}
