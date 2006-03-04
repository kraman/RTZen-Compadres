package rtsjcomponents.example2;

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
        return MyAC.doWork(i);
    }

    public Integer execSDM_1(int i) {
        // TODO Access the state of the component
        return new Integer(MyAC.doWork(i));
    }


}
