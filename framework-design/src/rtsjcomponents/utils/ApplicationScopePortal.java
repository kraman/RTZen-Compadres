package rtsjcomponents.utils;

import javax.realtime.MemoryArea;

/**
 * This class represent the portal of the application scope. 
 * It should be initialize be the framework. 
 * @author juancol
 */
public class ApplicationScopePortal
{
    private Queue poolOfObjectHolders = new Queue();

    public ApplicationScopePortal(int poolSize) 
    {
        if (poolSize <= 0) throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
        
        for (int i = 0; i < poolSize; i++)
        {   
            poolOfObjectHolders.enqueue(new ObjectHolder());
        }
    }

    public ObjectHolder getObjectHolder()
    {
        ObjectHolder oh = null;
        if (!poolOfObjectHolders.isEmpty())
        {
            oh = (ObjectHolder) poolOfObjectHolders.dequeue();
            oh.held = null;
        }
        return oh;
    }

    // TODO Potential problem: I have to be sure to use the result soon. 
    public void freeObjectHolder(ObjectHolder oh)
    {
        if (MemoryArea.getMemoryArea(oh) != MemoryArea.getMemoryArea(this)) {
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
        }
        poolOfObjectHolders.enqueue(oh);
    }
    
}
