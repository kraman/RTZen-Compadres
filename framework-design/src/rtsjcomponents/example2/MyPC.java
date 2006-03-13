package rtsjcomponents.example2;

import rtsjcomponents.PassiveComponent;
import rtsjcomponents.utils.IntHolder;

/**
 * Defines the business methods of the passive component
 * @author juancol
 */
public interface MyPC extends PassiveComponent{

    int execSIM_0(int i);
    
    Integer execSIM_1(int i);
    
    int execSDM_0(int i);
    
    Integer execSDM_1(int i);
    
    IntHolder execSDM_2(int i);    
}
