package rtsjcomponents.example2;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;

import rtsjcomponents.utils.ScopedMemoryPool;

/**
 * Example2 is the main application for running several active components 
 * and apssive components using NoHeapRealtimeThread.
 * @author juancol
 */
public class Example2 extends RealtimeThread
{
    public static int testcase;
    
    public static void main(String[] args)
    {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: tjvm ... rtsjcomponents.Example2 'case#'");
            System.err.println("case#=(0,1,2,3,4)");
        }
            
        int testcase = Integer.parseInt(args[0]);
        if (testcase < 0 || testcase > 4) {
            throw new IllegalArgumentException("argument must be [0,4]");
        }
        
        try
        {
            System.out.println("Example starting ...");
            ImmortalMemory imm = ImmortalMemory.instance();
            RealtimeThread rtt = (RealtimeThread) imm.newInstance(Example2.class);
            Example2.testcase = testcase;
            rtt.setDaemon(false);
            rtt.start();
            rtt.join();

            System.out.println("Everything is OK");
            System.exit(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Example2()
    {
        super();
    }
    
    public void run()
    {
        MemoryArea area = RealtimeThread.getCurrentMemoryArea();
        System.out.println("Executing "+ this.getClass().getName() + ".run() in " + area);

        ScopedMemory appScope = ScopedMemoryPool.getInstance();
        appScope.enter(new MainRunnable());
    }
}
