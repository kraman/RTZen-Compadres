package edu.uci.ece.doc.rtsjcomponents.active;

import edu.uci.ece.doc.rtsjcomponents.utils.Exceptions;

public class ACPortal
{
    private ACRunnable runnable;

    ACPortal(ACRunnable runnable)
    {
        if (runnable == null) { throw Exceptions.NULL_POINTER_EXCEPTION; }
        this.runnable = runnable;
    }

    final ACRunnable getActiveComponentRunnable()
    {
        return this.runnable;
    }
}
