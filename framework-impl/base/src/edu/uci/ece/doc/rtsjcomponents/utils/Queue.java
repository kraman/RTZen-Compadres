package edu.uci.ece.doc.rtsjcomponents.utils;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;

/**
 * This class implements a queue which is scoped memory safe. 
 * It reuses as many data structures as possible.
 * @author Krishna Raman
 */
public class Queue
{
    private static final ImmortalMemory IMM = ImmortalMemory.instance();
    
    /** Head to the linked list of unused queue nodes. */
    private QueueNode freeListHead;

    /** Tail to the linked list of unused queue nodes. */
    private QueueNode freeListTail;
 
    private int size = 0;
    
    /** Head of the linked list (internal representation of the queue) */
    private QueueNode allocListHead;

    /** Tail of the linked list (internal representation of the queue) */
    private QueueNode allocListTail;

    /** Object to synchronize the access to the list of unused objects.*/
    private final int[] mutex1 = {0};
    
    /** Object to synchronize the access to the list of allocated objects. */
    private final int[] mutex2 = {0};   
    
    /**
     * Returns a Queue instance created in immortal memory.
     * @return a Queue instance created in immortal memory.
     */
    public static Queue fromImmortal()
    {
        Queue q = null;

        try
        {
            q = (Queue) IMM.newInstance(Queue.class);
            
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        return q;
    }
    
    /**
     * Now is public but it should be a 
     * private constructor because <code>new</code> is memory-leak prone in RTSJ.
     */
    public Queue() {}
    
    /**
     * Returns the size of the queue.
     * @return the size of the queue.
     */
    public int size()
    {
        return size;
    }

    /**
     * Enqueues an object onto the queue. Use a preexisting queue node if possible, 
     * otherwise create a new queue node.
     * @param data The object to enqueue.
     */
    public void enqueue(Object data)
    {
        QueueNode node = this.getNode();
        
        node.value = data;
        synchronized (mutex2)
        {
            size++;
            if (allocListHead == null)
            {
                allocListHead = allocListTail = node;
            } 
            else
            {
                allocListTail.next = node;
                allocListTail = node;
            }
        }
    }
    
    /**
     * Retrieve an empty queue node from the linked list
     * @return An empty QueueNode object
     */
    private QueueNode getNode()
    {
        QueueNode ret = null;
        synchronized (mutex1)
        {
            if (freeListHead == null)
            {
                try
                {
                    ret = (QueueNode) MemoryArea.getMemoryArea(this).newInstance(QueueNode.class);
                } 
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            else
            {
                ret = freeListHead;
                freeListHead = freeListHead.next;
            }
        }
        ret.next = null;
        return ret;
    }    
     
    /**
     * Peek the top of the queue.
     * @return Object at the top of the queue or null if the queue is empty.
     */
    public Object peek()
    {
        synchronized (mutex2)
        {
            if (allocListHead == null) 
                return null;
            else 
                return allocListHead.value;
        }
    }

    /**
     * Returns true if the queue is empty. If you are using the
     * 
     * @return true if queue is empty, false otherwise.
     */
    public boolean isEmpty()
    {
        synchronized (mutex2)
        {
            return (allocListHead == null);
        }
    }

    /**
     * Return the top value on the queue or null if none is available.
     * @return The top value on the queue or null.
     */
    public Object dequeue()
    {
        QueueNode ret;
        synchronized (mutex2)
        {
            if (allocListHead == null)
            {
                return null;
            }
            else
            {
                size--;
                ret = allocListHead;
                allocListHead = allocListHead.next;
            }
        }
        Object obj = ret.value;
        this.freeNode(ret);
        return obj;
    }
 
    /**
     * Return a QueueNode to the freee linked list
     * 
     * @param node The QueueNode object to put on the list.
     */
    private void freeNode(QueueNode node)
    {
        node.value = null;
        node.next = null;
        synchronized (mutex1)
        {
            if (freeListHead == null)
            {
                freeListHead = freeListTail = node;
            } 
            else
            {
                freeListTail.next = node;
                freeListTail = node;
            }
        }
    }
}

