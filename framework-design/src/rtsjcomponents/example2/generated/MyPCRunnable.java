package rtsjcomponents.example2.generated;

import javax.realtime.MemoryArea;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.example2.MyPC;
import rtsjcomponents.example2.MyPCImpl;
import rtsjcomponents.utils.ExecuteInRunnable;
import rtsjcomponents.utils.ObjectHolder;
import rtsjcomponents.utils.WedgeRunnable;


public class MyPCRunnable implements Runnable {

    // Operation codes
    static final int ILLEGAL_OP = -1;

    static final int CREATE_COMP_OP = 0;

    static final int TERMINATE_OP = 1;

    static final int DO_execSIM_0 = 2;

    static final int DO_execSIM_1 = 3;

    static final int DO_execSDM_0 = 4;

    static final int DO_execSDM_1 = 5;

    /** Indicates the operation to be executed */
    private int operation = ILLEGAL_OP;

    private boolean initialized = false;

    private Throwable t;

    private int id;


//    private Object arg;
//    private Object returnValue;
//    private ObjectHolder oh;
//    private ScopedMemory implScope;

    /** Package constructor */
    MyPCRunnable() {
    }

    public void run() {
        // TODO Improve error management
        switch (this.operation) {
        case CREATE_COMP_OP:
            this.createPassiveComponent();
            break;
        case TERMINATE_OP:
            this.terminate();
            break;
        case DO_execSIM_0:
            // this.;
            break;
        case DO_execSIM_1:
            // this.;
            break;
        case DO_execSDM_0:
            // this.;
            break;
        case DO_execSDM_1:
            // this.;
            break;

        default:
            System.out.println(this.getClass().getName()
                    + " ILLEGAL OPERATION: it must be prepare first.");
            System.exit(-1);
        }
    }

    public final void prepareForCreatePassiveComponent(final int id) {
        this.operation = CREATE_COMP_OP;
        this.id = id;
    }

    private void createPassiveComponent() {
        // TODO Make this method general for every passive component
        
        try {
            MemoryArea area = RealtimeThread.getCurrentMemoryArea();

            if (!(area instanceof ScopedMemory)) {
                System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
                System.exit(-1); // pedant
            }

            ScopedMemory currentScope = (ScopedMemory) area;

            MyPCImpl comp = new MyPCImpl();
            MyPCPortal portal = new MyPCPortal(comp, new WedgeRunnable(currentScope));
            currentScope.setPortal(portal);
            
            NoHeapRealtimeThread wedgeThread = 
                new NoHeapRealtimeThread(null, null, null, currentScope, null, portal.getWedge());
            wedgeThread.setDaemon(false);

            comp.init(new rtsjcomponents.example2.ContextImpl(this.id));
            
            wedgeThread.start();            
            
            this.initialized = true;

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1); // pedant
        } finally {
            this.resetOperationCode();
        }
    }

    
    public final void prepareForTerminate() {
        this.operation = TERMINATE_OP;
    }

    private void terminate() {
        try
        {
            ScopedMemory scope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
            MyPCPortal portal = (MyPCPortal) scope.getPortal();
            portal.getWedge().deactivate();
            this.initialized = false;
        }
        finally
        {
            this.resetOperationCode();
        }        
    }

    
    /** Assign an illegal operation value to the field operation */
    protected void resetOperationCode() {
        this.operation = ILLEGAL_OP;
        this.t = null;
        this.id = Integer.MIN_VALUE;
    }

    boolean isThereAnyThrowable() {
        return (this.t != null);
    }

    Throwable getThrowable() {
        return this.t;
    }
}