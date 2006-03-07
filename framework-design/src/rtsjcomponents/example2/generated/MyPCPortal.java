package rtsjcomponents.example2.generated;

import rtsjcomponents.example2.MainRunnable;
import rtsjcomponents.example2.MyPCImpl;
import rtsjcomponents.utils.Exceptions;
import rtsjcomponents.utils.Queue;
import rtsjcomponents.utils.WedgeRunnable;

public class MyPCPortal {
    
    /** Passive Component implementation */
    private MyPCImpl pc;

    /** Wedge thread that keeps this scope alive */
    private WedgeRunnable wedge;
    
    private Queue poolOfInternalRunnables;
    private static final int NUM_OF_INTERNAL_RUNS = 
        MainRunnable.NUM_OF_ACTIVE_COMPONENTS;

    /**
     * Package constructor
     * @param pc The passive component implementation.
     * @param wedge The wedge runnable.
     */
    MyPCPortal(MyPCImpl pc, WedgeRunnable wedge) {
        if (pc == null && wedge == null) {
            throw Exceptions.NULL_POINTER_EXCEPTION;
        }

        this.pc = pc;
        this.wedge = wedge;
        
        this.poolOfInternalRunnables = new Queue();
        for (int i = 0; i < NUM_OF_INTERNAL_RUNS; i++) {
            poolOfInternalRunnables.enqueue(new MyPCRunnable.InternalRunnable());
        }
        
    }

    /**
     * Returns the passive component implementation.
     */
    final MyPCImpl getImpl() {
        return pc;
    }

    /**
     * Set the passive component implementation.
     */
    synchronized final void setImpl(MyPCImpl pc) {
        if (pc == null) {
            throw Exceptions.NULL_POINTER_EXCEPTION;
        }
        this.pc = pc;
    }

    /**
     * Returns the wedge runnable.
     */
    final WedgeRunnable getWedge() {
        return wedge;
    }

    /**
     * Set the wedge runnable.
     */
    synchronized final void setWedge(WedgeRunnable wedge) {
        if (wedge == null) {
            throw Exceptions.NULL_POINTER_EXCEPTION;
        }
        this.wedge = wedge;
    }
    
    /**
     * Returns the pool of internal runnables
     */
    final Queue getPoolOfInternalRunnables() {
        return this.poolOfInternalRunnables;
    }
}
