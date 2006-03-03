package rtsjcomponents.passive;

import rtsjcomponents.Context;
import rtsjcomponents.example1.MyActiveComponent;

public class MyPassiveComponentImpl implements MyPassiveComponent {

    public MyPassiveComponentImpl() {
        super();
    }
    
    public void init(Context ctx) {
        // TODO Auto-generated method stub
    }

    public void terminate() {
        // TODO Auto-generated method stub
    }
    
    public int execSIM_0(int i) {
        MyActiveComponent.doWork(i);
        return i;
    }

    public Integer execSIM_1(int i) {
        MyActiveComponent.doWork(i);
        return new Integer(i);
    }

    public int execSDM_0(int i) {
        // TODO Access the state of the component
        MyActiveComponent.doWork(i);
        return i;
    }

    public Integer execSDM_1(int i) {
        // TODO Access the state of the component
        return null;
    }


}
