package rtsjcomponents.active;

import javax.realtime.RealtimeThread;

import rtsjcomponents.ActiveComponent;

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
            if (!terminated)
                component.execute();
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