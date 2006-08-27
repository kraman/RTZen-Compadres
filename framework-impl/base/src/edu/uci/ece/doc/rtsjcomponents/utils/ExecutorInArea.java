package rtsjcomponents.utils;

import javax.realtime.ImmortalMemory;
import javax.realtime.ScopedMemory;

public class ExecutorInArea {

    /**
     * Encapsulates the "jump" logic. From the current memory area goes to Immortal Memory
     * and then enters a scoped memory area. 
     * @param r runnable for cross-scope invocation runnable.
     * @param s implementation scope.
     * @param temporalScope <code>true</code> to indicate that <code>s</code> is a temporary scope; 
     *                       <code>false</code> otherwise.
     *                        
     */
    public static void executeInArea(Runnable r, ScopedMemory s, boolean temporalScope) 
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
            ImmortalMemory.instance().executeInArea(eir);
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
    
    
}
