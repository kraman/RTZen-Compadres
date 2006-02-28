package rtsjcomponents.example1;

import rtsjcomponents.Context;

/**
 * A simple example of a periodic component.
 * @author juancol
 */
public class MyComponent implements rtsjcomponents.ActiveComponent
{
    int iter = 0;

    Context ctx = null;

    public void init(Context ctx)
    {
        // System.out.println("MyComponent.init()");
        this.ctx = ctx;
        this.iter = this.ctx.getLocalInt("iter");
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
