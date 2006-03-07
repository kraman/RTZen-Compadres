package rtsjcomponents.example2.generated;

import java.lang.reflect.Constructor;

import javax.realtime.ImmortalMemory;
import javax.realtime.InaccessibleAreaException;
import javax.realtime.MemoryArea;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.example2.MyPCImpl;
import rtsjcomponents.utils.ExecuteInRunnable;
import rtsjcomponents.utils.ObjectHolder;
import rtsjcomponents.utils.ScopedMemoryPool;
import rtsjcomponents.utils.WedgeRunnable;
import rtsjcomponents.utils.QualifiedClassNames;

public class MyPCRunnable implements Runnable {

    // Operation codes
    static final int ILLEGAL_OP = -1;

    static final int CREATE_COMP_OP = 0;

    static final int TERMINATE_OP = 1;

    static final int DO_execSDM_0 = 4;

    static final int DO_execSDM_1 = 5;

    /** Indicates the operation to be executed */
    private int operation = ILLEGAL_OP;

    private boolean initialized = false;

    private int intArg_0;

    private ObjectHolder oh;
    private ScopedMemory compScope;
    
    // Return values
    private double doubleRetValue;
    private int    intRetValue;
    private long   longRetValue;
    private char   charRetValue;
    private Object returnValue;
    
    private Throwable t;


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
        case DO_execSDM_0:
            this.DoExecSDM_0();
            break;
        case DO_execSDM_1:
            this.DoExecSDM_1();
            break;

        default:
            System.out.println(this.getClass().getName()
                    + " ILLEGAL OPERATION: it must be prepare first.");
            System.exit(-1);
        }
    }

    public final void prepareForCreatePassiveComponent(final int id) {
        this.operation = CREATE_COMP_OP;
        this.intArg_0 = id;
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

            comp.init(new rtsjcomponents.example2.ContextImpl(this.intArg_0));
            
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
        try {
            ScopedMemory scope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
            MyPCPortal portal = (MyPCPortal) scope.getPortal();
            portal.getWedge().deactivate();
            this.initialized = false;
        }
        finally {
            this.resetOperationCode();
        }        
    }

    // 
    // Note that SIM methods don't need nothing from the runnable, only SDM methods do.
    //
    
    public void prepareForExecSDM_0(int i) {
        this.operation = DO_execSDM_0;
        this.intArg_0 = i;
    }
    
    private void DoExecSDM_0() {
        try {
            ScopedMemory scope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
            MyPCPortal portal = (MyPCPortal) scope.getPortal();
            this.intRetValue = portal.getImpl().execSDM_0(this.intArg_0);
        }
        finally {
            this.resetOperationCode();
        }        
    }
    
    
    public void prepareForExecSDM_1(final int i, final ScopedMemory compScope) {
        this.operation = DO_execSDM_1;
        this.intArg_0 = i;
        this.compScope = compScope;
    }
    
    private void DoExecSDM_1() {
        try {
            ScopedMemory scope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
            final MyPCPortal portal = (MyPCPortal) scope.getPortal();
            
            final ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
            
            tmpScope.enter(new Runnable(){
                public void run() {
                    Integer r = portal.getImpl().execSIM_1(intArg_0);

                    ScopedMemory targetScope = 
                        (ScopedMemory) MemoryArea.getMemoryArea(MyPCRunnable.this);
                    
                    MyPCRunnable.this.copyValueInOriginScope(r, targetScope);
                    
                    //MyPCRunnable.this.returnValue(MyPCRunnable.this.getReturnValue(), targetScope);
                    
               }
            });        

            ScopedMemoryPool.freeInstance(tmpScope);
        }
        finally {
            this.resetOperationCode();
        }
    }
    
    /**
     * Copy the result in the scope where the call initiated.
     * This methods must be generated per return value type of SDM. 
     */
    private void copyValueInOriginScope(Integer r, ScopedMemory targetScope) {
        Class cType;
        try
        {
            // System.out.println("CalculatorReturnRunnable.doSquare(...): " + RealtimeThread.getCurrentMemoryArea());
            cType = Class.forName(QualifiedClassNames.INTEGER_CLASS);
            Class[] paramTypes = { Integer.TYPE };
            Constructor constructor = cType.getConstructor(paramTypes);
            
            // potential memory leak, but this logic should execute in a temporary scope
            Object[] params = { new Integer(r.intValue()) }; 
            
            MyPCRunnable.this.returnValue = 
                (Integer) targetScope.newInstance(constructor, params);

            //System.out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }                            
    }
    
    
//    private void returnValue(final Object result, ScopedMemory targetScope) {
//        
//        // Prepare the ReturnRunnable for the jump
//        MyPCReturnRunnable rr = MyPCReturnRunnable.getInstance();
//        rr.prepareForReturnForDoExecSDM_1(result, targetScope); 
//        
//        ExecuteInRunnable eir2 = ExecuteInRunnable.getAnInitializedEIR(rr, compScope);
//        ExecuteInRunnable eir1 = ExecuteInRunnable.getAnInitializedEIR(eir2, targetScope); 
//
//        try {
//            ImmortalMemory.instance().executeInArea(eir1);
//        }
//        catch (InaccessibleAreaException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        finally {
//            ExecuteInRunnable.freeEIR(eir2);
//            ExecuteInRunnable.freeEIR(eir1);
//            MyPCReturnRunnable.freeInstance(rr);
//        }
//    }    
    
    /** Assign an illegal operation value to the field operation */
    protected void resetOperationCode() {
        this.operation = ILLEGAL_OP;
        this.t = null;
        this.intArg_0 = Integer.MIN_VALUE;
        this.compScope = null;
    }

    
    /* 
     * TODO These methods can be reused, so we need to put them in an abstract class 
     * (or something similar)
     */
    boolean isThereAnyThrowable() {
        return (this.t != null);
    }

    Throwable getThrowable() {
        return this.t;
    }

    /**
     * @return Returns the returnValue.
     */
    public Object getReturnValue()
    {
        return returnValue;
    }

    /** 
     * @param returnValue The returnValue to set.
     */
    private void setReturnValue(Object returnValue)
    {
        this.returnValue = returnValue;
    }
    /**
     * @return Returns the charRetValue.
     */
    public char getCharRetValue()
    {
        return charRetValue;
    }
    /**
     * @param charRetValue The charRetValue to set.
     */
    private void setCharRetValue(char charRetValue)
    {
        this.charRetValue = charRetValue;
    }
    /**
     * @return Returns the doubleRetValue.
     */
    public double getDoubleRetValue()
    {
        return doubleRetValue;
    }
    /**
     * @param doubleRetValue The doubleRetValue to set.
     */
    private void setDoubleRetValue(double doubleRetValue)
    {
        this.doubleRetValue = doubleRetValue;
    }
    /**
     * @return Returns the intRetValue.
     */
    public int getIntRetValue()
    {
        return intRetValue;
    }
    /**
     * @param intRetValue The intRetValue to set.
     */
    private void setIntRetValue(int intRetValue)
    {
        this.intRetValue = intRetValue;
    }
    /**
     * @return Returns the longRetValue.
     */
    public long getLongRetValue()
    {
        return longRetValue;
    }
    /**
     * @param longRetValue The longRetValue to set.
     */
    private void setLongRetValue(long longRetValue)
    {
        this.longRetValue = longRetValue;
    }
    


}

