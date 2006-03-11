package rtsjcomponents.example2;

import rtsjcomponents.utils.Errors;
import rtsjcomponents.utils.Exceptions;

/**
 * Specific ContextImpl class for the example.
 * 
 * @author juancol
 */
public class ContextImpl implements rtsjcomponents.Context {
    private int id = Integer.MIN_VALUE;
    private int exampleId = Integer.MIN_VALUE;
    private int runId = Integer.MIN_VALUE;

    public ContextImpl(final int i, final int example, final int run) {
        if (i < 0 || example < 0 || run < 0 )
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;

        this.id = i;
        this.exampleId = example;
        this.runId = run;
    }

    public Object getComponent(String name) {
        int compId = Integer.parseInt(name);
        if (compId < 0 || compId > MainRunnable.NUM_OF_PASSIVE_COMPONENTS) {
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
        }

        return MainRunnable.myPCFacades[compId];
    }

    public char getLocalChar(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public int getLocalInt(String name) {
        if (name.equals(MyAC.ID_STR))
            return (this.id);
        else
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
    }

    public short getLocalShort(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public long getLocalLong(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public double getLocalDouble(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public boolean getLocalBoolean(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public char getGlobalChar(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public int getGlobalInt(String name) {
        if (name.equals(MyAC.EXAMPLE_ID_STR))
            return this.exampleId;
        else if (name.equals(MyAC.RUN_ID_STR))
            return this.runId;
        else
            throw Exceptions.ILLEGAL_ARGUMENT_EXCEPTION;
    }

    public short getGlobalShort(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public long getGlobalLong(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public double getGlobalDouble(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }

    public boolean getGlobalBoolean(String name) {
        throw Errors.NO_SUCH_METHOD_ERROR;
    }
}
