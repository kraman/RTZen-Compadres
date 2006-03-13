package rtsjcomponents.example2.generated;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.Context;
import rtsjcomponents.example2.MainRunnable;
import rtsjcomponents.example2.MyPC;
import rtsjcomponents.example2.MyPCImpl;
import rtsjcomponents.utils.Errors;
import rtsjcomponents.utils.ExecutorInArea;
import rtsjcomponents.utils.Queue;
import rtsjcomponents.utils.ScopedMemoryPool;


public class MyPCFacade implements MyPC{
    
    private static int NUM_OF_FACADES = 
        MainRunnable.NUM_OF_PASSIVE_COMPONENTS;
    public static final int NUM_OF_RUNNABLES = 
        MainRunnable.NUM_OF_RUNNABLES_PER_PASSIVE_COMPONENTS;
   
    public static final int NUM_OF_STATELESS_IMPLS = 
        MainRunnable.NUM_OF_STATELESS_PASSIVE_COMPONENT_IMPLS;

    private static Queue poolOfProxies;
    private static Queue poolOfRunnables;
    private static Queue poolOfCompImpl;
    // TODO How to manage runnables, sychro, etc.?

    // Static block for creating pools in immortal memory.
    static {
        try {
            poolOfProxies = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_FACADES; i++) {
                poolOfProxies.enqueue(new MyPCFacade());
            }
            
            poolOfRunnables = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_RUNNABLES; i++) {
                poolOfRunnables.enqueue(new MyPCRunnable());
            }

            poolOfCompImpl = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_STATELESS_IMPLS; i++) {
                MyPCImpl c = new MyPCImpl();
                // Here we should invoke c.init(Context ctx), in order to
                // initialize the instances correctly
                poolOfCompImpl.enqueue(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
        
    private boolean initialized = false;
    private ScopedMemory stateScope;
    
    public static MyPCFacade getInstance() {
        MyPCFacade f = null;
        if (!poolOfProxies.isEmpty()) {
            f = (MyPCFacade) poolOfProxies.dequeue();
        }
        return f;
    }

    public static void freeInstance(MyPCFacade f) {
        // This checking is not strict because jRate is not compliant with the standard.
        if (!(MemoryArea.getMemoryArea(f) instanceof ImmortalMemory)){
            System.out.println("WARN: Enqueueing  facade not allocated in immortal memory. It happens in jRate!");
        }
        f.deactivate();
        poolOfProxies.enqueue(f);            
    }

    
    /**  Private constructor to prevent memory leaks. */
    private MyPCFacade() {
        super();
    }
    
    /**
     * Intialize the facade and activate the component implementation.
     */
    public void createPassiveComponent(final int id)  {

        // TODO Make this method general for every passive component
        // TODO verify parameters
        // We assume that the component has state, which is true for our examples
        
        if (this.initialized) return; 
             
        this.stateScope = ScopedMemoryPool.getInstance();
        
        final ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
            
        tmpScope.enter(new Runnable(){
            public void run() {

              MyPCRunnable csir = new MyPCRunnable();
              csir.prepareForCreatePassiveComponent(id);
              ExecutorInArea.executeInArea(csir, stateScope, true);               
           }
        });
        
         ScopedMemoryPool.freeInstance(tmpScope);      
         
         this.initialized = true;
    }

    
    /**
     * Terminate the facade and deactivate the wedge thread in the working scope.
     */ 
    private void deactivate()
    {
        if (!this.initialized) return;
        
        final ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
        
        tmpScope.enter(new Runnable(){
            public void run() {
              MyPCRunnable csir = new MyPCRunnable();
              csir.prepareForTerminate();
              ExecutorInArea.executeInArea(csir, stateScope, true);               
           }
        });        

        ScopedMemoryPool.freeInstance(tmpScope);
        
        // TODO how to safely return the working scope to the pool
        ScopedMemoryPool.freeInstance(stateScope);
        
        this.initialized = false;
    }

    public int execSIM_0(int i) {
        MyPCImpl c = (MyPCImpl) poolOfCompImpl.dequeue();
        int result = c.execSIM_0(i);
        poolOfCompImpl.enqueue(c);
        return result; 
    }

    public Integer execSIM_1(int i) {
        MyPCImpl c = (MyPCImpl) poolOfCompImpl.dequeue();
        Integer result = c.execSIM_1(i);
        poolOfCompImpl.enqueue(c);
        return result; 
    }

    public int execSDM_0(int i) {
        MyPCRunnable csir = (MyPCRunnable) poolOfRunnables.dequeue();
        csir.prepareForExecSDM_0(i);
        ExecutorInArea.executeInArea(csir, stateScope, true);
        int result = csir.getIntRetValue();
        poolOfRunnables.enqueue(csir);
        return result;
    }

    public Integer execSDM_1(int i) {
        
        ScopedMemory currentScope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
        
        // Obtain the component scope which is parent of the current scope.
        ScopedMemory compScope = (ScopedMemory) 
            RealtimeThread.getOuterMemoryArea(RealtimeThread.getMemoryAreaStackDepth() - 2);

        MyPCRunnable csir = new MyPCRunnable();
        currentScope.setPortal(csir);
        
        csir.prepareForExecSDM_1(i, compScope);
        ExecutorInArea.executeInArea(csir, this.stateScope, true);
        
        Integer r = (Integer) csir.getReturnValue();
       
        currentScope.setPortal(null);
        
        return r;
    }
 
    public Integer execSDM_2(int i) {
        
        ScopedMemory currentScope = (ScopedMemory) RealtimeThread.getCurrentMemoryArea();
        
        // Obtain the component scope which is parent of the current scope.
        ScopedMemory compScope = (ScopedMemory) 
            RealtimeThread.getOuterMemoryArea(RealtimeThread.getMemoryAreaStackDepth() - 2);

        MyPCRunnable csir = new MyPCRunnable();
        currentScope.setPortal(csir);
        
        csir.prepareForExecSDM_2(i, compScope);
        ExecutorInArea.executeInArea(csir, this.stateScope, true);
        
        Integer r = (Integer) csir.getReturnValueStatic();
       
        currentScope.setPortal(null);
        
        return r;
    }
    
    public void init(Context ctx) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }
    
    public void terminate() {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }
    
    
    public int getComponentScopeHashCode() {
        if (this.stateScope == null) {
            return 0;    
        }
        
        return this.stateScope.hashCode();
    }
}

