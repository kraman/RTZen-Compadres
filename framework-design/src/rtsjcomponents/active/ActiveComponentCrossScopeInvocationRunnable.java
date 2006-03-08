package rtsjcomponents.active;

import javax.realtime.AperiodicParameters;
import javax.realtime.MemoryArea;
import javax.realtime.MemoryParameters;
import javax.realtime.NoHeapRealtimeThread;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.realtime.SchedulingParameters;
import javax.realtime.ScopedMemory;
import javax.realtime.SporadicParameters;

import rtsjcomponents.ActiveComponent;
import rtsjcomponents.utils.ExecuteInRunnable;

/**
 * @author juancol
 *
 */
public class ActiveComponentCrossScopeInvocationRunnable implements Runnable
{
    // Operation codes
    private static final int ILLEGAL_OP          = -1;
//    private static final int INIT_OP             =  0;
    private static final int CREATE_COMP_OP      =  0;
    private static final int TERMINATE_OP        =  1;

    /** Indicates the operation to be executed */
    private int operation = ILLEGAL_OP;

//    private boolean initialized = false;
    
    private static final int INVALID_ACTIVE_COMPONENT_TYPE = -1;
    private static final int PERIODIC    =  0;
    private static final int APERIODIC   =  1;
    private static final int SPORADIC    =  2;
    
    private int activeComponentType = INVALID_ACTIVE_COMPONENT_TYPE;
    
    // Operation parameters
    private SchedulingParameters scheduling;
    private RelativeTime start; 
    private RelativeTime period;
    private RelativeTime cost;
    private RelativeTime deadline;
    private RelativeTime minInterarrival;
    private MemoryParameters memory;
    private Class componentClass;
    private int id;
    
    private Throwable t = null;
    
    /** Package constructor */
    public ActiveComponentCrossScopeInvocationRunnable() { }
    
    /* @see java.lang.Runnable#run() */
    public void run()
    {
        // TODO Improve error management
        switch (this.operation)
        {
        case CREATE_COMP_OP:
            this.createActiveComponent();
            break;
        case TERMINATE_OP:
            this.terminate();
            break;
        default:
            System.out.println(this.getClass().getName() + 
                    " ILLEGAL OPERATION: it must be prepare first.");
            System.exit(-1);
        }
    }


    void prepareForCreatePeriodicComponent(final PriorityParameters scheduling, 
            final RelativeTime start, final RelativeTime period, final RelativeTime cost,
            final RelativeTime deadline, final MemoryParameters memory, final Class componentClass, final int id)    
    {
        this.operation = CREATE_COMP_OP;
        this.activeComponentType = PERIODIC;
        this.scheduling = scheduling;
        this.start = start;
        this.period = period;
        this.cost = cost;
        this.deadline = deadline;
        this.memory = memory;
        this.componentClass = componentClass;
        this.id = id;
    }
    
    void prepareForCreateAperiodicComponent(final PriorityParameters scheduling, 
            final RelativeTime cost, final RelativeTime deadline, final MemoryParameters memory, 
            final Class componentClass, final int id)    
    {
        this.operation = CREATE_COMP_OP;
        this.activeComponentType = APERIODIC;
        this.scheduling = scheduling;
        this.cost = cost;
        this.deadline = deadline;
        this.memory = memory;
        this.componentClass = componentClass;
        this.id = id;
    }
    
    void prepareForCreateSporadicComponent(final PriorityParameters scheduling, 
            final RelativeTime minInterarrival, final RelativeTime cost, final RelativeTime deadline, 
            final MemoryParameters memory, final Class componentClass, final int id)    
    {
        this.operation = CREATE_COMP_OP;
        this.activeComponentType = SPORADIC;
        this.scheduling = scheduling;
        this.minInterarrival = minInterarrival;
        this.cost = cost;
        this.deadline = deadline;
        this.memory = memory;
        this.componentClass = componentClass;
        this.id = id;
    }
    
    /**
     * Creates an active component
     */
    private void createActiveComponent() 
    {
        try
        {
            MemoryArea area = RealtimeThread.getCurrentMemoryArea();
            
            if (!(area instanceof ScopedMemory))
            {
                System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
                System.exit(-1); // pedant
            }

            ScopedMemory currentScope = (ScopedMemory) area;
     
            //System.out.println("here 1");
            ActiveComponent comp = (ActiveComponent) currentScope.newInstance(componentClass);
            //System.out.println("here 2");
            ActiveComponentRunnable componentRunnable = new ActiveComponentRunnable(comp);
            //System.out.println("here 3");
            ActiveComponentPortal portal = new ActiveComponentPortal(componentRunnable);
            //System.out.println("here 4");
            currentScope.setPortal(portal);
            //System.out.println("here 5");
            
            // Copying parameters
            PriorityParameters mySchedulingParams =  
                new PriorityParameters(((PriorityParameters)this.scheduling).getPriority());  
            //System.out.println("here 6");
            
            // ReleaseParameters.clone() is NOT scope safe.
            ReleaseParameters myReleaseParams;
            //System.out.println("here 7");
            RelativeTime myCost = new RelativeTime(this.cost);
            //System.out.println("here 8");
            RelativeTime myDeadline = new RelativeTime(this.deadline);
            //System.out.println("here 9");
            
            if (activeComponentType == PERIODIC)
            {
                RelativeTime myStart = new RelativeTime(this.start);
                //System.out.println("here 10");
                RelativeTime myPeriod = new RelativeTime(this.period);
                //System.out.println("here 11");
                myReleaseParams = new PeriodicParameters(myStart, myPeriod, myCost, myDeadline, 
                        null, null);
                //System.out.println("here 12");
            }
            else if (activeComponentType == APERIODIC)
            {
                myReleaseParams = new AperiodicParameters(myCost, myDeadline, null, null);
            }
            else // if (activeComponentType == SPORADIC)
            {
                RelativeTime myMinInterarrival = new RelativeTime(this.minInterarrival);
                myReleaseParams = new SporadicParameters(myMinInterarrival, myCost, myDeadline, null, null);
            }
            
            MemoryParameters myMemoryParams = null; // new MemoryParameters(this.memory.getMaxMemoryArea(), this.memory.getMaxImmortal());
            //System.out.println("here 13");
            
            NoHeapRealtimeThread periodicThread =  
                new NoHeapRealtimeThread(mySchedulingParams, 
                                         myReleaseParams, 
                                         myMemoryParams, 
                                         currentScope, 
                                         null, 
                                         componentRunnable);
	   periodicThread.setDaemon(true);
            //System.out.println("here 14");
          
            // TODO specific ContextImpl class for experiment (hardcoded).
            comp.init(new rtsjcomponents.example2.ContextImpl(this.id, rtsjcomponents.example2.Example2.testcase));       
            periodicThread.setDaemon(false);
            periodicThread.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1); // pedant
        }
        finally {
            this.resetOperationCode();
        }
    }
    
    /**
     * Prepares this runnable for executing <code>terminate</code> operation.
     */
    void prepareForTerminate() {   
        //System.out.println("GeniusRunnable.prepareForTerminate()");
        this.operation = TERMINATE_OP;
    }

    /** Deactivates the wedge thread in order to reclaim the scope. */
    private void terminate() {   
        try
        {        
          MemoryArea area = RealtimeThread.getCurrentMemoryArea();
        
          if (!(area instanceof ScopedMemory))
          {
              System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
              System.exit(-1); // pedant
          }

          ScopedMemory thisScope = (ScopedMemory) area;
        
          ActiveComponentPortal portal = (ActiveComponentPortal) thisScope.getPortal();
	  portal.getActiveComponentRunnable().terminate();
	  
        }
        finally
        {
            this.resetOperationCode();
        }
    }

    
    
    /** Assign an illegal operation value to the field operation */
    private void resetOperationCode()
    {
        this.operation = ILLEGAL_OP;
        this.activeComponentType = INVALID_ACTIVE_COMPONENT_TYPE;
        this.scheduling = null;
        this.start = null; 
        this.period = null;
        this.cost = null;
        this.deadline = null;
        this.minInterarrival = null;
        this.memory = null;
        this.componentClass = null;
        this.t = null;
        this.id = Integer.MIN_VALUE;
    }
    
    
    boolean isThereAnyThrowable() 
    {
        return (this.t != null);
    }
    
    Throwable getThrowable() 
    {
        return this.t;
    }
}
