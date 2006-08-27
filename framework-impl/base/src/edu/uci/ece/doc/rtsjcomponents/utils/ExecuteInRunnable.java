package rtsjcomponents.utils;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;

/**
 * This class is used to jump between memory regions. It is used when the program needs to jump to a
 * parent memory region and then enter a sibling. To do this, the ExecuteInRunnable is initialized
 * with the next memory region and the runnable object to run in it. The ExecuteInRunnable is run in
 * the parent memory region and then the ExecuteInRunnable invokes the program provided runnable
 * object in the next memory region.
 * 
 * @author Krishna Raman
 * @author Juan Colmenares
 */
public class ExecuteInRunnable implements Runnable
{
    public static final String RUNNABLE_NOT_IN_A_SCOPE_MSG = 
        "This runnable must be executed in a memory scope.";
    
    private static final ImmortalMemory IMM = ImmortalMemory.instance();

    private final static int NUM_OF_EIR = 5; // TODO Constants can be assign from a property file.

    /** The next memory area to enter */
    private MemoryArea memArea;

    /** The runnable to invoke in the next memory region */
    private Runnable runnable;

    private final static Queue eirPool = Queue.fromImmortal();

    static
    {
        try
        {
            for (int i = 0; i < NUM_OF_EIR; i++)
            {                
                eirPool.enqueue(IMM.newInstance(ExecuteInRunnable.class));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    /**
     * Private constructor because <code>new</code> is memory-leak prone in RTSJ.
     */
    public ExecuteInRunnable() {} // TODO make private again

    /**
     * Returns an instance of this class created in immortal memory. If there is
     * no available object in the internal pool, a new one is created. 
     * @return
     */
    static public ExecuteInRunnable getEIR()
    {
        ExecuteInRunnable eir = (ExecuteInRunnable) eirPool.dequeue();
        if (eir == null)
        {
            try
            {
                eir = (ExecuteInRunnable) IMM.newInstance(ExecuteInRunnable.class);
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        return eir;
    }

    
    /**
     * Returns an instance of this class created in immortal memory and initialized 
     * using the specified parameters. 
     * @param  runnable 
     * @param  area 
     * @return  
     */
    static public ExecuteInRunnable getAnInitializedEIR(Runnable runnable, MemoryArea area)
    {
        if (runnable == null || area == null)
        {
            throw Exceptions.NULL_POINTER_EXCEPTION;  // TODO test this reusable exception.
        }
        
        ExecuteInRunnable eir = ExecuteInRunnable.getEIR();
        eir.init(runnable, area);
        
        return eir; 
    }
    
    
    /**
     * Puts the object into the pool so that the object used for others. 
     * @param an ExecuteInRunnable object.
     */
    static public void freeEIR(ExecuteInRunnable eir)
    {
        eirPool.enqueue(eir);
    }

    /**
     * Initializes the object.
     * @param runnable
     * @param area
     */
    public void init(Runnable runnable, MemoryArea area)
    {
        this.runnable = runnable;
        this.memArea = area;
    }
 
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        memArea.enter(runnable);
    }
}