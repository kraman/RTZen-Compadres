package rtsjcomponents.example1;

import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.Context;
import rtsjcomponents.utils.ExecuteInRunnable;

/**
 * A simple example of a periodic component.
 * @author juancol
 */
public class MyComponent implements rtsjcomponents.ActiveComponent
{
    public static final String ITER_STR = "iter_base";
    public static final int ITER_MULTIPLIER = 100;
    
    int iter = 0;

    Context ctx = null;

    public void init(Context ctx)
    {
        // System.out.println("MyComponent.init()");
        this.ctx = ctx;
        this.iter = this.ctx.getLocalInt(ITER_STR) * ITER_MULTIPLIER;
    }

    public void execute()
    {
        // System.out.println("MyComponent.execute()");

        int j = 0;
        
        //System.out.println("MyComponent.execute() with iterations:" + this.iter);
        
        MemoryArea area = RealtimeThread.getCurrentMemoryArea();

        System.out.println("MyComponent.execute() with iterations:" + this.iter + " Memory area: " + area);
        
        if (!(area instanceof ScopedMemory))
        {
            System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
            System.exit(-1); // pedant
        }        
        
        for (int i = 0; i < this.iter; i++)
        {
            j++;
        }
    }

    public void terminate()
    {
        // System.out.println("MyComponent.terminate()");
    }

}
