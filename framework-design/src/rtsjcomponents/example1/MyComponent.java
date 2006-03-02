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

import rtsjcomponents.utils.Constants;


/**
 * A simple example of a periodic component.
 * @author juancol
 */
public class MyComponent implements rtsjcomponents.ActiveComponent
{
    public static final String ITER_STR = "iter_base";
    public static final int ITER_MULTIPLIER = 100;
    public static final AbsoluteTime at= new AbsoluteTime();
    public static final int MEASUREMENTS = 50;
    public static final long time[] = new long[MEASUREMENTS]; //in nanosec 
    public static int runNumber=0;

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
		
		MemoryArea area = RealtimeThread.getCurrentMemoryArea();
	
		System.out.println("MyComponent.execute() with iterations:" + this.iter + " Memory area: " + area);
		
		if (!(area instanceof ScopedMemory))
		{
		    System.out.println(ExecuteInRunnable.RUNNABLE_NOT_IN_A_SCOPE_MSG);
		    System.exit(-1); // pedant
		}        
		

		for (int i = 0; i < MEASUREMENTS; i++)
		{
			Clock.getRealtimeClock().getTime(at);
			long t0 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
			this.doWork(j);	
			Clock.getRealtimeClock().getTime(at);
			long t1 = at.getNanoseconds() + at.getMilliseconds() * 1000000;
			time[i] = t1 - t0; 
		}
    }

    public void terminate()
    {
    //    System.out.println("MyComponent.terminate()");
 
    }
     public static void logRecords()
    {
        System.out.println ("Saving timestamps in a file ...");
        try 
        {
            PrintWriter file = 
                new PrintWriter(
                        new FileOutputStream("timeRecords-" + System.currentTimeMillis() + ".txt"));
            for (int i = 0; i < time.length; i++)
            {
		file.println(i + ": "+ time[i]);
            }
            file.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error writing to file: " + e);
        }
    }
    
    private void doWork(int j){
	    for (int m=0; m < this.iter ; m++){
		    int x = j*j;
	    }
    }

}
