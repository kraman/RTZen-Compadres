package rtsjcomponents.active;

import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.ActiveComponent;
import rtsjcomponents.example2.MainRunnable;
import rtsjcomponents.utils.ObjectHolder;
import rtsjcomponents.utils.ScopedMemoryPool;
import rtsjcomponents.utils.TempScopePortal;

public class ActiveComponentRunnable implements Runnable {
    
    private boolean terminated = false;

    private boolean terminateExecuted = false;

    private ActiveComponent component;

    ActiveComponentRunnable(ActiveComponent c) {
        if (c == null)
            throw new NullPointerException("Active component is null.");
        this.component = c;
    }

    public void run() {
        do {
            if (!terminated) {
                final ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
                tmpScope.enter(new Runnable(){
                    public void run() {
                        ScopedMemory currentScope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
                        currentScope.setPortal(new ObjectHolder());
                        component.execute();
                    }
                 });        
                ScopedMemoryPool.freeInstance(tmpScope);
            }
        } while (RealtimeThread.waitForNextPeriod() && !terminated);

        if (terminated && !terminateExecuted) {
            this.terminateExecuted = true;
            component.terminate();
        }
    }

    synchronized public void terminate() {
        //System.out.println("In ACR terminate");
        terminated = true;
    }

}