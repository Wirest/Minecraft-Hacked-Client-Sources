// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.dnd;

import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.util.Arrays;
import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.datatransfer.DataFlavor;
import java.util.List;
import java.awt.dnd.DropTargetListener;

public abstract class DropHandler implements DropTargetListener
{
    private int acceptedActions;
    private List<DataFlavor> acceptedFlavors;
    private DropTarget dropTarget;
    private boolean active;
    private DropTargetPainter painter;
    private String lastAction;
    
    public DropHandler(final Component c, final int acceptedActions) {
        this(c, acceptedActions, new DataFlavor[0]);
    }
    
    public DropHandler(final Component c, final int acceptedActions, final DataFlavor[] acceptedFlavors) {
        this(c, acceptedActions, acceptedFlavors, null);
    }
    
    public DropHandler(final Component c, final int acceptedActions, final DataFlavor[] acceptedFlavors, final DropTargetPainter painter) {
        this.active = true;
        this.acceptedActions = acceptedActions;
        this.acceptedFlavors = Arrays.asList(acceptedFlavors);
        this.painter = painter;
        this.dropTarget = new DropTarget(c, acceptedActions, this, this.active);
    }
    
    protected DropTarget getDropTarget() {
        return this.dropTarget;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
        if (this.dropTarget != null) {
            this.dropTarget.setActive(active);
        }
    }
    
    protected int getDropActionsForFlavors(final DataFlavor[] dataFlavors) {
        return this.acceptedActions;
    }
    
    protected int getDropAction(final DropTargetEvent e) {
        int currentAction = 0;
        int sourceActions = 0;
        Point location = null;
        DataFlavor[] flavors = new DataFlavor[0];
        if (e instanceof DropTargetDragEvent) {
            final DropTargetDragEvent ev = (DropTargetDragEvent)e;
            currentAction = ev.getDropAction();
            sourceActions = ev.getSourceActions();
            flavors = ev.getCurrentDataFlavors();
            location = ev.getLocation();
        }
        else if (e instanceof DropTargetDropEvent) {
            final DropTargetDropEvent ev2 = (DropTargetDropEvent)e;
            currentAction = ev2.getDropAction();
            sourceActions = ev2.getSourceActions();
            flavors = ev2.getCurrentDataFlavors();
            location = ev2.getLocation();
        }
        if (this.isSupported(flavors)) {
            final int availableActions = this.getDropActionsForFlavors(flavors);
            currentAction = this.getDropAction(e, currentAction, sourceActions, availableActions);
            if (currentAction != 0 && this.canDrop(e, currentAction, location)) {
                return currentAction;
            }
        }
        return 0;
    }
    
    protected int getDropAction(final DropTargetEvent e, int currentAction, final int sourceActions, final int acceptedActions) {
        final boolean modifiersActive = this.modifiersActive(currentAction);
        if ((currentAction & acceptedActions) == 0x0 && !modifiersActive) {
            final int action = currentAction = (acceptedActions & sourceActions);
        }
        else if (modifiersActive) {
            final int action = currentAction & acceptedActions & sourceActions;
            if (action != currentAction) {
                currentAction = action;
            }
        }
        return currentAction;
    }
    
    protected boolean modifiersActive(final int dropAction) {
        final int mods = DragHandler.getModifiers();
        if (mods == -1) {
            return dropAction == 1073741824 || dropAction == 1;
        }
        return mods != 0;
    }
    
    private void describe(final String type, final DropTargetEvent e) {
    }
    
    protected int acceptOrReject(final DropTargetDragEvent e) {
        final int action = this.getDropAction(e);
        if (action != 0) {
            e.acceptDrag(action);
        }
        else {
            e.rejectDrag();
        }
        return action;
    }
    
    public void dragEnter(final DropTargetDragEvent e) {
        this.describe("enter(tgt)", e);
        final int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }
    
    public void dragOver(final DropTargetDragEvent e) {
        this.describe("over(tgt)", e);
        final int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }
    
    public void dragExit(final DropTargetEvent e) {
        this.describe("exit(tgt)", e);
        this.paintDropTarget(e, 0, null);
    }
    
    public void dropActionChanged(final DropTargetDragEvent e) {
        this.describe("change(tgt)", e);
        final int action = this.acceptOrReject(e);
        this.paintDropTarget(e, action, e.getLocation());
    }
    
    public void drop(final DropTargetDropEvent e) {
        this.describe("drop(tgt)", e);
        final int action = this.getDropAction(e);
        if (action != 0) {
            e.acceptDrop(action);
            try {
                this.drop(e, action);
                e.dropComplete(true);
            }
            catch (Exception ex) {
                e.dropComplete(false);
            }
        }
        else {
            e.rejectDrop();
        }
        this.paintDropTarget(e, 0, e.getLocation());
    }
    
    protected boolean isSupported(final DataFlavor[] flavors) {
        final Set<DataFlavor> set = new HashSet<DataFlavor>(Arrays.asList(flavors));
        set.retainAll(this.acceptedFlavors);
        return !set.isEmpty();
    }
    
    protected void paintDropTarget(final DropTargetEvent e, final int action, final Point location) {
        if (this.painter != null) {
            this.painter.paintDropTarget(e, action, location);
        }
    }
    
    protected boolean canDrop(final DropTargetEvent e, final int action, final Point location) {
        return true;
    }
    
    protected abstract void drop(final DropTargetDropEvent p0, final int p1) throws UnsupportedFlavorException, IOException;
}
