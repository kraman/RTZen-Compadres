package rtsjcomponents.active;

import rtsjcomponents.utils.Exceptions;

public class ActiveComponentPortal
{
    private ActiveComponentRunnable runnable;

    ActiveComponentPortal(ActiveComponentRunnable runnable)
    {
        if (runnable == null) { throw Exceptions.NULL_POINTER_EXCEPTION; }
        this.runnable = runnable;
    }

    final ActiveComponentRunnable getActiveComponentRunnable()
    {
        return this.runnable;
    }
}
