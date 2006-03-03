package rtsjcomponents.passive.generated;

import javax.realtime.ScopedMemory;

import rtsjcomponents.utils.ObjectHolder;

public class MyPassiveComponentRunnable implements Runnable {

    // Operation codes
    static final int ILLEGAL_OP         = -1;
    static final int INIT_OP            =  0;
    static final int TERMINATE_OP       =  1;
    static final int DO_execSIM_0       =  2;
    static final int DO_execSIM_1       =  3;
    static final int DO_execSDM_0       =  4;
    static final int DO_execSDM_1       =  5;

    
    /** Indicates the operation to be executed */
    private int operation = ILLEGAL_OP;

    private boolean initialized = false;

    private Object arg;
    
    private Object returnValue;
    private ObjectHolder oh;
    private ScopedMemory implScope;
    
    /** Package constructor */
    MyPassiveComponentRunnable() {}    

    
    public void run() {
        // TODO Improve error management
        switch (this.operation)
        {
        case INIT_OP:
            this.init();
            break;
        case TERMINATE_OP:
            this.terminate();
            break;
        case DO_execSIM_0:
            //this.;
            break;
        case DO_execSIM_1 :
            //this.;
            break;
        case DO_execSDM_0:
            //this.;
            break;
        case DO_execSDM_1:
            //this.;
            break;
            
        default:
            System.out.println(this.getClass().getName() + 
                    " ILLEGAL OPERATION: it must be prepare first.");
            System.exit(-1);
        }
        

    }

}
