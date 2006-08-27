package edu.uci.ece.doc.rtsjcomponents.utils;

import javax.realtime.MemoryArea;

/**
 * This class represent the portal of temporary scopes active components.
 * In particular, it is used to return objects between components in sibling scopes. 
 * @author juancol
 */
public class TempScopePortal
{
    private Queue poolOfObjectHolders = new Queue();

    public TempScopePortal(int poolSize) {
        if (poolSize <= 0) throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
        
        for (int i = 0; i < poolSize; i++) {   
            poolOfObjectHolders.enqueue(new ObjectHolder());
        }
    }

    public ObjectHolder getObjectHolder() {
        ObjectHolder oh = null;
        if (!poolOfObjectHolders.isEmpty()) {
            oh = (ObjectHolder) poolOfObjectHolders.dequeue();
            oh.held = null;
        }
        return oh;
    }

    // TODO Potential problem: I have to be sure to use the result soon. 
    public void freeObjectHolder(ObjectHolder oh) {
        if (MemoryArea.getMemoryArea(oh) != MemoryArea.getMemoryArea(this)) {
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
        }
        poolOfObjectHolders.enqueue(oh);
    }
}
