// This class should be an internal class but TJVM does not 
// support inner classes well. Just for now is default.
package rtsjcomponents.utils;

/**
 * Internal node of the linked list.
 * 
 * @author Krishna Raman
 * @author Juan Colmenares
 */
class QueueNode 
{
    QueueNode() { };

    /** The value of this node. */
    Object value;

    /** Next node. */
    QueueNode next;
}

