package rtsjcomponents.example1;

import rtsjcomponents.Context;

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
