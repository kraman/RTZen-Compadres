package rtsjcomponents.example1;

import javax.realtime.MemoryArea;
import javax.realtime.MemoryParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import rtsjcomponents.active.ActiveComponentFacade;
import rtsjcomponents.utils.Constants;

/**
 * Example of main runnable for running active components.
 * @author juancol
 */
public class MainRunnable implements Runnable
{    
    public static final int BASE_PERIOD = 3;
    public static final int BASE_DEADLINE = 2;
    public static final int NUM_OF_ACTIVE_COMPONENTS = 5;  
    
    /**
     * Creates a set of active components 
     * @param num number of active components to be created
     * @param facade active component facade
     */
    private ActiveComponentFacade[] createActiveComponents(final int num)
    {
        if (num <= 0)
        {
            System.out.println("num must be greater than zero."); 
            System.exit(-1);
        }
        
        int priority = PriorityScheduler.getMaxPriority(RealtimeThread.currentRealtimeThread());
        
        
        ActiveComponentFacade[] facades = new ActiveComponentFacade[num];
        
        for (int i = 0; i < num; i++)
        {
            facades[i] = ActiveComponentFacade.getInstance();
            
            //TODO Set parameters of active components properly
            PriorityParameters priorityParams = new PriorityParameters(priority - num);
            RelativeTime start = new RelativeTime(Constants.A_SECOND, 0);
            RelativeTime period = new RelativeTime((BASE_PERIOD + num) * Constants.A_SECOND, 0);
            RelativeTime cost = new RelativeTime((Constants.A_SECOND + num) / 2 , 0);
            RelativeTime deadline = new RelativeTime((BASE_DEADLINE + num) * Constants.A_SECOND, 0);
            MemoryParameters memoryParams = 
                new MemoryParameters(MemoryParameters.NO_MAX, MemoryParameters.NO_MAX);
            
            facades[i].createPeriodicComponent(priorityParams, start, period, cost, 
                    deadline, memoryParams, MyComponent.class, num);
        }
        
        return facades;
    }
    
    public void run()
    {
        MemoryArea area = RealtimeThread.getCurrentMemoryArea();
        System.out.println("Executing MainRunnable.run() in " + area);
        
        ActiveComponentFacade[] facades = null;

        try
        {
            facades = this.createActiveComponents(NUM_OF_ACTIVE_COMPONENTS);            
            RealtimeThread.sleep(20 * Constants.A_SECOND);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            for (int i = 0; i < facades.length; i++) {
                ActiveComponentFacade.freeInstance(facades[i]);
            }
        }       
    }
}
