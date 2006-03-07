package rtsjcomponents.example2;

import javax.realtime.MemoryArea;
import javax.realtime.MemoryParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import rtsjcomponents.active.ActiveComponentFacade;
import rtsjcomponents.example2.generated.MyPCFacade;
import rtsjcomponents.utils.Constants;

/**
 * Example of main runnable for running active and passive components.
 * 
 * @author juancol
 */
public class MainRunnable implements Runnable {
    
    public static final int BASE_PERIOD = 3;
    public static final int BASE_DEADLINE = 3;
    public static final int NUM_OF_ACTIVE_COMPONENTS = 5;
    public static final int NUM_OF_PASSIVE_COMPONENTS = 5;
    public static final int NUM_OF_RUNNABLES_PER_PASSIVE_COMPONENTS = 10;
    public static final int NUM_OF_STATELESS_PASSIVE_COMPONENT_IMPLS = 10;

    public static final MyPC[] myPCFacades = new MyPC[MainRunnable.NUM_OF_PASSIVE_COMPONENTS];
  
    /**
     * Creates a set of active components
     * 
     * @param num number of active components to be created
     * @param facade active component facade
     */
    private ActiveComponentFacade[] createActiveComponents(final int num) {
        if (num <= 0) {
            System.out.println("num must be greater than zero.");
            System.exit(-1);
        }

        int priority = 
            PriorityScheduler.getMaxPriority(RealtimeThread.currentRealtimeThread());

        ActiveComponentFacade[] facades = new ActiveComponentFacade[num];

        for (int i = 0; i < num; i++) {
            facades[i] = ActiveComponentFacade.getInstance();

            // TODO Set parameters of active components properly
            PriorityParameters priorityParams = 
                new PriorityParameters(priority - i);
            RelativeTime start = new RelativeTime(Constants.A_SECOND, 0);
            RelativeTime period = new RelativeTime((BASE_PERIOD + i) * Constants.A_SECOND, 0);
            RelativeTime cost = new RelativeTime((Constants.A_SECOND + i) / 2, 0);
            RelativeTime deadline = new RelativeTime((BASE_DEADLINE + i) * Constants.A_SECOND, 0);
            MemoryParameters memoryParams = new MemoryParameters(MemoryParameters.NO_MAX, MemoryParameters.NO_MAX);

            facades[i].createPeriodicComponent(priorityParams, start, period,
                    cost, deadline, memoryParams, MyAC.class, i);
        }

        return facades;
    }

    /**
     * Creates a set of passive components
     * @param num number of active components to be created
     * @param facade active component facade
     */
    private MyPCFacade[] createPassiveComponents(final int num) {
        if (num <= 0) {
            System.out.println("num must be greater than zero.");
            System.exit(-1);
        }

        MyPCFacade[] facades = new MyPCFacade[num];

        for (int i = 0; i < num; i++) {
            facades[i] = MyPCFacade.getInstance();
            facades[i].createPassiveComponent(i);
            MainRunnable.myPCFacades[i] = facades[i];
        }

        return facades;
    }

    public void run() {
        MemoryArea area = RealtimeThread.getCurrentMemoryArea();
        System.out.println("Executing MainRunnable.run() in " + area);

        ActiveComponentFacade[] actFacades = null;
        MyPCFacade[] pasFacades = null;

        try {
            pasFacades = this.createPassiveComponents(NUM_OF_PASSIVE_COMPONENTS);
            System.out.println("Passive components created ...");
            
            actFacades = this.createActiveComponents(NUM_OF_ACTIVE_COMPONENTS);
            System.out.println("Active components created ...");
            
            RealtimeThread.sleep(20 * Constants.A_SECOND);

            for (int i = 0; i < actFacades.length; i++) {
                ActiveComponentFacade.freeInstance(actFacades[i]);
                MyPCFacade.freeInstance(pasFacades[i]);
            }

            // During this time we let the components terminate.
            // We had to do this because of a Timesys RTSJ-RI's bug.
            RealtimeThread.sleep(20 * Constants.A_SECOND);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
