package rtsjcomponents.passive.generated;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.ScopedMemory;

import rtsjcomponents.Context;
import rtsjcomponents.active.ActiveComponentCrossScopeInvocationRunnable;
import rtsjcomponents.example2.MainRunnable;
import rtsjcomponents.passive.MyPassiveComponent;
import rtsjcomponents.utils.Queue;
import rtsjcomponents.utils.ScopedMemoryPool;

public class MyPassiveComponentFacade implements MyPassiveComponent {
    
    private static int NUM_OF_FACADES = 
        MainRunnable.NUM_OF_PASSIVE_COMPONENTS;
    public static final int NUM_OF_RUNNABLES = 
        MainRunnable.NUM_OF_RUNNABLES_PER_PASSIVE_COMPONENTS;
   

    private static Queue poolOfProxies;
    private static Queue poolOfRunnables;

    private boolean initialized = false;
    
    // This may be obtain from a pool, but for this example I only need one from
    // immortal memory.
//    private static final MyPassiveComponentRunnable RUNNABLE =  
//        new MyPassiveComponentRunnable(); 
    // TODO How to manage runnables, sychro,etc.?

    // Static block for creating pools in immortal memory.
    static {
        try {
            poolOfProxies = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_FACADES; i++) {
                poolOfProxies.enqueue(new MyPassiveComponentFacade());
            }
            
            poolOfRunnables = Queue.fromImmortal();
            for (int i = 0; i < NUM_OF_RUNNABLES; i++) {
                poolOfProxies.enqueue(new MyPassiveComponentRunnable());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    
    private ScopedMemory stateScope;

    
    public static MyPassiveComponentFacade getInstance() {
        MyPassiveComponentFacade f = null;
        if (!poolOfProxies.isEmpty()) {
            f = (MyPassiveComponentFacade) poolOfProxies.dequeue();
        }
        return f;
    }

    public static void freeInstance(MyPassiveComponentFacade f) {

        if (MemoryArea.getMemoryArea(f) instanceof ImmortalMemory){
            f.terminate();
            poolOfProxies.enqueue(f);            
        } else {
            System.out.println("trying to enqueue a facade not " +
                    "allocated in immortal memory");
            System.exit(-1); //pedant
        }
    }

    
    /**  Private constructor to prevent memory leaks. */
    private MyPassiveComponentFacade() {
        super();
    }
    
    
    /**
     * Intialize the facade and the implementation in the specified working scope.
     */
    public void createPassiveComponent()
    {
        if (this.initialized) return; 
        
        // Pure stateless components do not require 
        
        
        this.stateScope = ScopedMemoryPool.getInstance();
        
       ScopedMemory tmpScope = ScopedMemoryPool.getInstance();

       MyPassiveComponentRunnable r = (MyPassiveComponentRunnable) poolOfRunnables.dequeue();
       // r.prepareForInit();
       // executeInArea(r, this.stateScope, false);
       
        tmpScope.enter(new Runnable(){
           public void run() {
//               ActiveComponentCrossScopeInvocationRunnable crossScopeInvocator = 
//                   new ActiveComponentCrossScopeInvocationRunnable();
//                 
//               crossScopeInvocator.prepareForCreatePeriodicComponent(scheduling, start, period, 
//                       cost, deadline, memory, componentClass, id); // key method
//               
//               executeInArea(crossScopeInvocator, workingScope, true);               
               
           }
        });
        ScopedMemoryPool.freeInstance(tmpScope);      
       
        
        
        


    }

    
    /**
     * Terminate the proxy and deactivate the wedge thread in the working scope.
     */ 
    public void deactivate()
    {
        if (!this.initialized) return;
        
        // TODO how to safely return the working scope to the pool 
//        CALC_RUNNABLE.prepareForTerminate();
//        executeInArea(CALC_RUNNABLE, this.stateScope, false);
    }
            
    
    
    public void init(Context ctx) {
        // just to 
    }
    
    public void terminate() {}
    

    public int execSIM_0(int i) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Integer execSIM_1(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    public int execSDM_0(int i) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Integer execSDM_1(int i) {
        // TODO Auto-generated method stub
        return null;
    }

}
