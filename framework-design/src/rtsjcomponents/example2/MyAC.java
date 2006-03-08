package rtsjcomponents.example2;

import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.Context;
import rtsjcomponents.utils.ExecuteInRunnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;

/**
 * A simple example of a periodic component.
 * @author juancol
 */
public class MyAC implements rtsjcomponents.ActiveComponent {
    
    public static final String RECORDS = "timeRecords-comp";
    public static final String CASE = "-case";
    public static final String TXT = ".txt";
    
    public static final String ID_STR = "id";
    public static final String EXAMPLE_ID_STR = "example_id";
    
    public static final int ITER_MULTIPLIER = 100;

    public static final AbsoluteTime at = new AbsoluteTime();

    public static final int MEASUREMENTS = 50; //Number of measurements needes

    public final long time[] = new long[100]; //in nanosec

    private int counter = 0;

    int iter = 0;

    Context ctx = null;

    private int id;
    private MyPC myPC;    
    
    private int testcase;
    
    public void init(Context ctx) {
        // System.out.println("MyComponent.init()");
        
        this.ctx = ctx;

        // iter = component ID times multiplier
        this.id = this.ctx.getLocalInt(ID_STR);
        this.iter =  (this.id + 1) * ITER_MULTIPLIER;
        this.myPC = (MyPC) this.ctx.getComponent(Integer.toString(this.id));
        this.testcase = this.ctx.getGlobalInt(EXAMPLE_ID_STR);
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
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            counter++;
	    //System.out.println(this.id+ " " + counter);
        }
	//else{
	    //System.out.println(this.id+ " Counter gt 50");
	//}
   } 
    
    private void executeCase1() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        int i = this.myPC.execSIM_0(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase2() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        Integer ii = this.myPC.execSIM_1(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase3() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        int i = this.myPC.execSDM_0(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            counter++;
        }
    }
    
    private void executeCase4() {
        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        
        Integer i = this.myPC.execSDM_1(this.iter);
        
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            counter++;
        }
    }
    
    public void terminate() {
        // System.out.println("MyComponent.terminate()");
        // Write time measurements into a log
        this.logRecords();

    }

    private void logRecords() {
        //System.out.println ("Saving timestamps in a file ...");
        try {
            FileOutputStream os = 
                new FileOutputStream(RECORDS + this.id + CASE + this.testcase + TXT);
            PrintWriter file = new PrintWriter(os);
            for (int i = 0; i < time.length; i++) {
                file.println(i + ": " + time[i]);
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
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
