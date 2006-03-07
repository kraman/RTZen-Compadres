package rtsjcomponents.example2.generated;

import java.lang.reflect.Constructor;

import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.example2.MainRunnable;
import rtsjcomponents.utils.ObjectHolder;
import rtsjcomponents.utils.QualifiedClassNames;
import rtsjcomponents.utils.Queue;
import rtsjcomponents.utils.TempScopePortal;

public class MyPCReturnRunnable implements Runnable {

    // TODO This class might be general for all components.
    // TODO Verify this constants.
    private static final int NUM_OF_CALC_RET_RUNNABLES = MainRunnable.NUM_OF_ACTIVE_COMPONENTS;

    private static Queue poolOfReturnRunnables;

    static {
        try {
            poolOfReturnRunnables = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_CALC_RET_RUNNABLES; i++) {
                poolOfReturnRunnables.enqueue(new MyPCReturnRunnable());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static MyPCReturnRunnable getInstance() {
        MyPCReturnRunnable rr = null;
        if (!poolOfReturnRunnables.isEmpty()) {
            rr = (MyPCReturnRunnable) poolOfReturnRunnables.dequeue();
        }
        return rr;
    }

    public static void freeInstance(MyPCReturnRunnable rr) {
        // TODO enqueue only if it comes from the right mermory area.
        rr.reset();
        poolOfReturnRunnables.enqueue(rr);
    }

    private int intResult;
    private short shortResult;
    private long longResult;
    private double doubleResult;
    private char charResult;
    
    private Object objArg;

    private ScopedMemory targetScope;

    /** Indicates the operation to be executed */
    private int operation = MyPCRunnable.ILLEGAL_OP;

    /** Package constructor */
    MyPCReturnRunnable() {
    }

    /* @see java.lang.Runnable#run() */
    public void run() {
        switch (this.operation) {
        case MyPCRunnable.DO_execSDM_1:
            this.doExecSDM_1();
            break;
        default:
            System.out.println(this.getClass().getName()
                    + " ILLEGAL OPERATION: it must be prepare first.");
            System.exit(-1); //pedant
        }
    }
    
    void prepareForReturnForDoExecSDM_1(final Integer result, final ScopedMemory targetScope) {
        this.operation = MyPCRunnable.DO_execSDM_1;
        this.objArg = result;
        this.targetScope = targetScope;
    }
    
    private void doExecSDM_1() {
        
        // It is supposed that we are in the scope in which the call was originated. 
        ScopedMemory s = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
        if (s != this.targetScope) {
            System.err.println("Error accessing scope to return a value.");
            System.exit(-1); //pedant
        }
        
        // In this place we copy the result so that it is available in the scope where
        // the call was originated.
        Integer result = new Integer(((Integer) this.objArg).intValue());
        MyPCRunnable run = (MyPCRunnable) s.getPortal();
        run.setReturnValue(result);
    }

    /** 
     * Assign an illegal/dummy operation value to the field operation 
     */
    private void reset() {
        this.operation = MyPCRunnable.ILLEGAL_OP;
        this.targetScope = null;
        this.objArg = null;
        this.intResult = Integer.MIN_VALUE;
        this.shortResult = Short.MIN_VALUE;
        this.longResult = Long.MIN_VALUE;
        this.doubleResult = Double.NaN;
        this.charResult = Character.MIN_VALUE;
        
    }

}
