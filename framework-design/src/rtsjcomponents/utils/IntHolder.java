package rtsjcomponents.utils;

public class IntHolder
{
    public IntHolder(){};
    
    private int val = 0;
    
    public void setVal(int val){
        this.val = val;
    }
    
    public int getVal() {
        return val;
    }
    
    public String toString(){
        return val + "";
    }
    
}