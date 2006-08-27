package edu.uci.ece.doc.rtsjcomponents.utils;

public class IntHolder
{
    public IntHolder(){
        this.val = 0;
    }

    public IntHolder(int val){
        this.val = val;
    }
    
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