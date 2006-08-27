package edu.uci.ece.doc.rtsjcomponents.active;

import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import edu.uci.ece.doc.rtsjcomponents.ActiveComponent;
//import rtsjcomponents.example2.MainRunnable;
//import edu.uci.ece.doc.rtsjcomponents.utils.ObjectHolder;
import edu.uci.ece.doc.rtsjcomponents.utils.ScopedMemoryPool;
//import edu.uci.ece.doc.rtsjcomponents.utils.TempScopePortal;

public class ACRunnable implements Runnable {
    
    private boolean terminated = false;

    private boolean terminateExecuted = false;

    private ActiveComponent component;
    private InternalRunnable interRun;

    ACRunnable(ActiveComponent c) {
        if (c == null)
            throw new NullPointerException("Active component is null.");
        this.component = c;
        this.interRun = new InternalRunnable();
    }

    public void run() {
        do {
            if (!terminated) {
                final ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
                tmpScope.enter(this.interRun);
                ScopedMemoryPool.freeInstance(tmpScope);
            }
        } while (RealtimeThread.currentRealtimeThread().waitForNextPeriod() && !terminated);

        if (terminated && !terminateExecuted) {
            this.terminateExecuted = true;
            component.terminate();
        }
    }

    synchronized public void terminate() {
        //System.out.println("In ACR terminate");
        terminated = true;
    }
    
    private class InternalRunnable implements Runnable {
        public void run() {
            ACRunnable.this.component.execute();
        }
    }
    
    
}
