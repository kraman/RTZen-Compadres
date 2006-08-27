package edu.uci.ece.doc.rtsjcomponents.active;

import javax.realtime.AperiodicParameters;
import javax.realtime.AsyncEventHandler;
import javax.realtime.MemoryArea;
import javax.realtime.MemoryParameters;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.ScopedMemory;
import javax.realtime.SporadicParameters;

import edu.uci.ece.doc.rtsjcomponents.utils.Constants;
import edu.uci.ece.doc.rtsjcomponents.utils.ExecuteInRunnable;
import edu.uci.ece.doc.rtsjcomponents.utils.ExecutorInArea;
import edu.uci.ece.doc.rtsjcomponents.utils.Queue;
import edu.uci.ece.doc.rtsjcomponents.utils.ScopedMemoryPool;

/**
 * @author juancol
 */
public class ACFacade
{
    //  TODO Constant generated or assigned from configuration file.
    private static final int MAX_NUM_OF_ACTIVE_COMPONENTS = 9;

    private ScopedMemory workingScope;
    private boolean initialized = false;
    
    /** Pool of immortal facades */
    private static Queue poolOfFacades;

    // Static block for creating pools in immortal memory.
    static 
    {   
        try 
        {
            poolOfFacades = Queue.fromImmortal();
            for (int i = 0; i < MAX_NUM_OF_ACTIVE_COMPONENTS; i++) 
            {   
                poolOfFacades.enqueue(new ACFacade());
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    /**
     * Returns an instance of this class instatiated in immortal memory.
     * @return an ACFacade object; <code>null</code> if there is no instance available.
     */
    public static ACFacade getInstance()
    {
        ACFacade gen = null;
        if (!poolOfFacades.isEmpty())
        {
            gen = (ACFacade) poolOfFacades.dequeue();
        }
        return gen;
    }

    /**
     * Puts the specified object into the pool.
     * @param genius an ACFacade object.
     */
    public static void freeInstance(ACFacade component)
    {
        //System.out.println("In facade.freeInstance"); 
	// TODO enqueue only if it comes from the right mermory area.
        //System.out.println("ACFacade.freeInstance");
        component.deactivate();
        poolOfFacades.enqueue(component);
    }

    /** Private constructor to prevent memory leaks. */
    private ACFacade() { }

    /**
     * Create and activate a periodic component 
     */
    public void createPeriodicComponent(final PriorityParameters scheduling, 
            final RelativeTime start, final RelativeTime period, final RelativeTime cost,
            final RelativeTime deadline, final MemoryParameters memory, final Class componentClass, final int id) 
    {
        // TODO Complete validation input parameters
        
        if (this.initialized) return; 
        
        this.workingScope = ScopedMemoryPool.getInstance();
        
        ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
        
        tmpScope.enter(new Runnable(){
           public void run() {
               ACXScopeInvocationRunnable crossScopeInvocator = 
                   new ACXScopeInvocationRunnable();
                 
               crossScopeInvocator.prepareForCreatePeriodicComponent(scheduling, start, period, 
                       cost, deadline, memory, componentClass, id); // key method
               
               ExecutorInArea.executeInArea(crossScopeInvocator, workingScope, true);               
               
           }
        });
        ScopedMemoryPool.freeInstance(tmpScope);

        this.initialized = true;
    }


    public void createAperiodicComponent(final PriorityParameters scheduling, 
            final RelativeTime cost, final RelativeTime deadline, final MemoryParameters memory, 
            final Class componentClass, final int key) 
    {
        // TODO Complete validation input parameters
        
        if (this.initialized) return; 
        
        this.workingScope = ScopedMemoryPool.getInstance();
        
        ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
        
        tmpScope.enter(new Runnable(){
           public void run() {
               ACXScopeInvocationRunnable crossScopeInvocator = 
                   new ACXScopeInvocationRunnable();
                 
               crossScopeInvocator.prepareForCreateAperiodicComponent(scheduling, cost, deadline,
                       memory, componentClass, key); // key method
               
               ExecutorInArea.executeInArea(crossScopeInvocator, workingScope, true);               
           }
        });
        ScopedMemoryPool.freeInstance(tmpScope);

        this.initialized = true;        
//        XSCOPE_INVOCATION_RUNNABLE.prepareForCreateAperiodicComponent(scheduling, cost, deadline, 
//                memory, componentClass);
    }
    
    
    public void createSporadicComponent(final PriorityParameters scheduling, 
            final RelativeTime minInterarrival, final RelativeTime cost, final RelativeTime deadline, 
            final MemoryParameters memory, final Class componentClass, final int key) 
    {
        
        // TODO Complete validation input parameters
        
        if (this.initialized) return; 
        
        this.workingScope = ScopedMemoryPool.getInstance();
        
        ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
        
        tmpScope.enter(new Runnable(){
           public void run() {
               ACXScopeInvocationRunnable crossScopeInvocator = 
                   new ACXScopeInvocationRunnable();
                 
               crossScopeInvocator.prepareForCreateSporadicComponent(scheduling, minInterarrival, 
                     cost, deadline, memory, componentClass, key); // key method
               
               ExecutorInArea.executeInArea(crossScopeInvocator, workingScope, true);               
           }
        });
        ScopedMemoryPool.freeInstance(tmpScope);

        this.initialized = true;        
//        XSCOPE_INVOCATION_RUNNABLE.prepareForCreateSporadicComponent(scheduling, minInterarrival, 
//                cost, deadline, memory, componentClass);
        
    }
    
    
    /**  Deactivate the component. */ 
    private void deactivate()
    {
        // TODO how to safely return the working scope to the pool
        if (!this.initialized) return;
 
        ScopedMemory tmpScope = ScopedMemoryPool.getInstance();
        
        tmpScope.enter(new Runnable(){
           public void run() {
               ACXScopeInvocationRunnable crossScopeInvocator = 
                   new ACXScopeInvocationRunnable();
               crossScopeInvocator.prepareForTerminate(); // key method
               ExecutorInArea.executeInArea(crossScopeInvocator, workingScope, true);               
           }
        });
        ScopedMemoryPool.freeInstance(tmpScope);        
        
        this.initialized = false;
    }
    
        
    public int getComponentScopeHashCode() {
        if (this.workingScope == null) {
            return 0;    
        }
        
        return this.workingScope.hashCode();
    }
    
    

    /**
     * Encapsulates the "jump's logic" 
     * @param r runnable for cross-scope invocation runnable.
     * @param s implementation scope.
     * @param temporalScope <code>true</code> to indicate that <code>s</code> is a temporary scope; 
     *                       <code>false</code> otherwise.
     *                        
     * /
    private void executeInArea(ActiveComponentCrossScopeInvocationRunnable r, ScopedMemory s, boolean temporalScope) 
    {
        ExecuteInRunnable eir;
        
        if (temporalScope)
        {
            eir = new ExecuteInRunnable();
            eir.init(r, s);
        }
        else
        {
            eir = ExecuteInRunnable.getAnInitializedEIR(r, s);
        }

        try 
        {
            MemoryArea ma = MemoryArea.getMemoryArea(this); // should be immortal memory
            // System.out.println("Source Area  " + ma + ' ' + " target into: " + s);
            ma.executeInArea(eir);
        }
        catch (Throwable t) 
        {
            t.printStackTrace();
            System.exit(-1);
        } 
        finally  
        {
            if (!temporalScope) ExecuteInRunnable.freeEIR(eir); 
        }
    }    
    */
}
