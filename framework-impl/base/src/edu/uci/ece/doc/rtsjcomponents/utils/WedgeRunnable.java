package rtsjcomponents.utils;

import javax.realtime.ScopedMemory;

// TODO Think about sychronization of this.

/**
 * @author juancol
 */
public class WedgeRunnable implements Runnable
{
    private boolean active;

    private ScopedMemory sm;

    public WedgeRunnable(ScopedMemory sm)
    {
        if (sm == null) { throw Exceptions.NULL_POINTER_EXCEPTION; }
        active = true;
        this.sm = sm;
    }

    public synchronized boolean isActive()
    {
        return this.active;
    }
    
    public synchronized void activate()
    {
        this.active = true;
    }

    public synchronized void deactivate()
    {
        this.active = false;
    }
    
    public void run()
    {
        Object portal = sm.getPortal();

        synchronized (portal)
        {
            try
            {
                while (active)
                {
                    portal.wait();
                }
            } catch (InterruptedException ie)
            {

            }
            active = false;
        }
    }
}