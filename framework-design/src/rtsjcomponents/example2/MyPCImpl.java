package rtsjcomponents.example2;
import rtsjcomponents.utils.IntHolder;
import rtsjcomponents.Context;
import rtsjcomponents.example2.MyAC;

public class MyPCImpl implements MyPC {
    
    private boolean theState = true;
    
    public void init(Context ctx) {
        // TODO Auto-generated method stub
    }

    public void terminate() {
        // TODO Auto-generated method stub
    }
    
    public int execSIM_0(int i) {
        MyAC.doWork(i);
        return i;
    }

    public Integer execSIM_1(int i) {
        MyAC.doWork(i);
        return new Integer(i);
    }

    public int execSDM_0(int i) {
        // TODO Access the state of the component
        MyAC.doWork(i);
        return i;
    }

    public Integer execSDM_1(int i) {
        // TODO Access the state of the component
        MyAC.doWork(i);
        return new Integer(i);
    }

    public IntHolder execSDM_2(int i) {
        // TODO Access the state of the component
        MyAC.doWork(i);
        IntHolder ih = null;
        
        try{
            ih = (IntHolder) javax.realtime.ImmortalMemory.instance()
                .newInstance(IntHolder.class);
            ih.setVal(i);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ih;
        
        /*
        try{
            java.lang.reflect.Constructor integerConstructor = Integer.class
                    .getConstructor(new Class [] {int.class});
            Object [] integerArgTypes = new Object[1];
            integerArgTypes[0] = new Integer(i);        
        
            return (Integer) javax.realtime.ImmortalMemory.instance().newInstance(
                    integerConstructor, integerArgTypes);  
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
        */
    }
    
}
