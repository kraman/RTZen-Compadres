package rtsjcomponents.example1;

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
    
    public static final String ID_STR = "id";
    public static final int ITER_MULTIPLIER = 10;

    public static final AbsoluteTime at = new AbsoluteTime();

    public static final int MEASUREMENTS = 50; //Number of measurements needes

    public final long time[] = new long[MEASUREMENTS]; //in nanosec

    private int counter = 0;

    int iter = 0;

    Context ctx = null;

    public void init(Context ctx) {
        // System.out.println("MyComponent.init()");
        this.ctx = ctx;
        // iter = component ID times multiplier
        this.iter = this.ctx.getLocalInt(ID_STR) * ITER_MULTIPLIER;
    }

    public void execute() {
        //System.out.println("MyComponent.execute()");

        MemoryArea area = RealtimeThread.getCurrentMemoryArea();

        //System.out.println("MyComponent.execute() with iterations:" +
        // this.iter + " Memory area: " + area);

        if (!(area instanceof ScopedMemory)) {
            System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
            System.exit(-1); // pedant
        }

        Clock.getRealtimeClock().getTime(at);
        long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        MyAC.doWork(this.iter);
        Clock.getRealtimeClock().getTime(at);
        long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
        if (counter <= MEASUREMENTS) {
            time[counter] = t1 - t0;
            //	System.out.println(counter+" "+ t1+ "," + t0);
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
            PrintWriter file = new PrintWriter(new FileOutputStream(
                    "timeRecords-" + this.iter + "-" + ".txt"));
            for (int i = 0; i < time.length; i++) {
                file.println(i + ": " + time[i]);
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }

    public static int doWork(int j) {
        int x = 0;

        for (int m = 0; m < j; m++) {
            x = j * j;
        }
        
        return x;
    }

}
