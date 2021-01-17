// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;
import java.util.List;

public class CompositeAction extends AbstractAction
{
    private final Action[] actions;
    private final boolean stopOnError;
    
    public CompositeAction(final List<Action> actions, final boolean stopOnError) {
        actions.toArray(this.actions = new Action[actions.size()]);
        this.stopOnError = stopOnError;
    }
    
    @Override
    public void run() {
        try {
            this.execute();
        }
        catch (IOException ex) {
            CompositeAction.LOGGER.warn("Exception during file rollover.", ex);
        }
    }
    
    @Override
    public boolean execute() throws IOException {
        if (this.stopOnError) {
            for (final Action action : this.actions) {
                if (!action.execute()) {
                    return false;
                }
            }
            return true;
        }
        boolean status = true;
        IOException exception = null;
        for (final Action action2 : this.actions) {
            try {
                status &= action2.execute();
            }
            catch (IOException ex) {
                status = false;
                if (exception == null) {
                    exception = ex;
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        return status;
    }
}
