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
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Example1 starting ...");
            ImmortalMemory imm = ImmortalMemory.instance();
            RealtimeThread rtt = (RealtimeThread) imm.newInstance(Example2.class);
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