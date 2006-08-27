package example1;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import edu.uci.ece.doc.rtsjcomponents.utils.IntHolder;
import edu.uci.ece.doc.rtsjcomponents.Context;
import edu.uci.ece.doc.rtsjcomponents.ActiveComponent;
import edu.uci.ece.doc.rtsjcomponents.utils.ExecuteInRunnable;

/**
 * A simple example of a periodic component.
 * @author juancol
 */
public class MyAC implements ActiveComponent {
    
    public static final String ID_STR = "id";
    public static final String EXAMPLE_ID_STR = "example_id";
    public static final String RUN_ID_STR = "run_id";
    
    private static final int ITER_MULTIPLIER = 100;// MainRunnable.ITER_MULTIPLIER;

    private AbsoluteTime at = new AbsoluteTime();

    public static final int MEASUREMENTS = 100; //MainRunnable.MEASUREMENTS; //Number of measurements needed
    private long times[];

    private int counter = 0;
    private int iter = 0;

    private Context ctx = null;

    private int id;
    // private MyPC myPC;    
    
    private int testcase;
    private int run;
    
    public void init(Context ctx) {
        // System.out.println("MyComponent.init()");
        this.ctx = ctx;

//        this.id = this.ctx.getLocalInt(ID_STR);
//        this.iter =  (this.id + 1) * ITER_MULTIPLIER;
//        this.myPC = (MyPC) this.ctx.getComponent(Integer.toString(this.id));
//        this.testcase = this.ctx.getGlobalInt(EXAMPLE_ID_STR);
//        this.run = this.ctx.getGlobalInt(RUN_ID_STR);
//        this.times = MainRunnable.AC_TIMES[this.id]; // From *IMMORTAL*
    }

    public void execute() {
        //System.out.println("MyComponent.execute()");

        MemoryArea area = RealtimeThread.getCurrentMemoryArea();
        if (!(area instanceof ScopedMemory)) {
            System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
            System.exit(-1); // pedant
        }
        // System.out.println("MyComponent.execute() with iterations:" +
        // this.iter + " Memory area: " + area);

        switch (this.testcase) {
        case 0:
            this.executeCase0();
            break;
        case 1:
            this.executeCase1();
            break;
        case 2:
            this.executeCase2();
            break;
        case 3:
            this.executeCase3();
            break;
        case 4:
            this.executeCase4();
            break;
        case 5:
            this.executeCase5();
            break;            
        case 6:
            this.executeCase6();
            break;               
        default:
            System.err.println("Invalid testcase number");
            System.exit(-1); // pedant
            break;
        }
    }
    
    private void executeCase0() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        int i = MyAC.doWork(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
   } 
    
    private void executeCase1() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        int i = MyAC.doWork(this.iter);
        Integer ii = new Integer(i);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
   }    
    
    private void executeCase2() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        // int i = this.myPC.execSIM_0(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase3() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        // Integer ii = this.myPC.execSIM_1(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase4() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        // int i = this.myPC.execSDM_0(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase5() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        // Integer i = this.myPC.execSDM_1(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
    }
    
    /**
        Test for serialization
    */
    private void executeCase6() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        // IntHolder i = this.myPC.execSDM_2(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter < MEASUREMENTS) {
            times[counter] = t1 - t0;
            counter++;
        }
    }    
    
    public void terminate() {
        // System.out.println("MyComponent.terminate()");
    }
    
    /**
     * Testing logic
     * @param j number of iterations
     * @return j^2 
     */
    public static int doWork(int j) {
        int x = 0;
        for (int m = 0; m < j; m++) {
            x = j * j;
        }
        return x;
    }
    
}
