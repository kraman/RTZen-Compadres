package rtsjcomponents.example1;

import rtsjcomponents.utils.Errors;
import rtsjcomponents.utils.Exceptions;

/**
 * Specific ContextImpl class for the example 1.
 * @author juancol
 */
public class ContextImpl implements rtsjcomponents.Context
{
    private int id = Integer.MIN_VALUE;

    public ContextImpl(final int i)
    {
        if (i < 0)
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;

        this.id = i;
    }
    
    public Object getComponent(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public char getLocalChar(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public int getLocalInt(String name)
    {
        if (name.equals(MyComponent.ITER_STR))
            return (this.id + 1);
        else 
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
    }

    public short getLocalShort(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public long getLocalLong(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public double getLocalDouble(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public boolean getLocalBoolean(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public char getGlobalChar(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public int getGlobalInt(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public short getGlobalShort(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public long getGlobalLong(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public double getGlobalDouble(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public boolean getGlobalBoolean(String name)
    {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }
}
